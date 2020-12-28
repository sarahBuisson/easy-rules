/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.jeasy.rules.core

import org.jeasy.rules.annotation.*
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.core.RuleProxy
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

/**
 * Main class to create rule proxies from annotated objects.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class RuleProxy private constructor(private val target: Any) : InvocationHandler {
    private lateinit var name: String
    private lateinit var description: String
    private var priority: Int = Rule.DEFAULT_PRIORITY
    private lateinit var methods: Array<Method>
    private lateinit var conditionMethod: Method
    private lateinit var actionMethods: MutableSet<ActionMethodOrderBean>
    private lateinit var compareToMethod: Method
    private lateinit var toStringMethod: Method
    private lateinit var annotation: org.jeasy.rules.annotation.Rule
    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        val methodName = method.getName()
        return when (methodName) {
            "getName" -> getRuleName()
            "getDescription" -> getRuleDescription()
            "getPriority" -> getRulePriority()
            "compareTo" -> compareToMethod(args)
            "evaluate" -> evaluateMethod(args)
            "execute" -> executeMethod(args)
            "equals" -> equalsMethod(args)
            "hashCode" -> hashCodeMethod()
            "toString" -> toStringMethod()
            else -> null
        }
    }

    @Throws(IllegalAccessException::class, InvocationTargetException::class)
    private fun evaluateMethod(args: Array<Any>): Any {
        val facts = args.get(0) as Facts
        val conditionMethod = getConditionMethod()
        return try {
            val actualParameters = getActualParameters(conditionMethod, facts)
            conditionMethod.invoke(target, *actualParameters.toTypedArray()) // validated upfront
        } catch (e: NoSuchFactException) {
            LOGGER.warn(
                "Rule '{}' has been evaluated to false due to a declared but missing fact '{}' in {}",
                getTargetClass().getName(), e.getMissingFact(), facts
            )
            false
        } catch (e: IllegalArgumentException) {
            LOGGER.warn(
                "Types of injected facts in method '{}' in rule '{}' do not match parameters types",
                conditionMethod.getName(), getTargetClass().getName(), e
            )
            false
        }
    }

    @Throws(IllegalAccessException::class, InvocationTargetException::class)
    private fun executeMethod(args: Array<Any>): Any? {
        val facts = args.get(0) as Facts
        for (actionMethodBean in getActionMethodBeans()) {
            val actionMethod = actionMethodBean.getMethod()
            val actualParameters = getActualParameters(actionMethod, facts)
            actionMethod.invoke(target, *actualParameters.toTypedArray())
        }
        return null
    }

    @Throws(Exception::class)
    private fun compareToMethod(args: Array<Any>): Any {
        val compareToMethod = getCompareToMethod()
        val otherRule = args.get(0) // validated upfront
        return if (compareToMethod != null && Proxy.isProxyClass(otherRule.javaClass)) {
            require(compareToMethod.parameters.size == 1) { "compareTo method must have a single argument" }
            val ruleProxy = Proxy.getInvocationHandler(otherRule) as RuleProxy
            compareToMethod.invoke(target, ruleProxy.getTarget())
        } else {
            compareTo(otherRule as Rule)
        }
    }

    private fun getActualParameters(method: Method, facts: Facts): MutableList<Any> {
        val actualParameters: MutableList<Any> = ArrayList()
        val parameterAnnotations = method.getParameterAnnotations()
        for (annotations in parameterAnnotations) {
            if (annotations.size == 1) {
                val factName: String = (annotations[0] as Fact).value //validated upfront.
                val fact = facts.get<Any>(factName)
                if (fact == null && !facts.asMap().containsKey(factName)) {
                    throw NoSuchFactException(
                        String.format(
                            "No fact named '%s' found in known facts: %n%s",
                            factName,
                            facts
                        ), factName
                    )
                }
                actualParameters.add(fact!!)
            } else {
                actualParameters.add(facts) //validated upfront, there may be only one parameter not annotated and which is of type Facts.class
            }
        }
        return actualParameters
    }

    @Throws(Exception::class)
    private fun equalsMethod(args: Array<Any>): Boolean {
        if (args.get(0) !is Rule) {
            return false
        }
        val otherRule = args.get(0) as Rule
        val otherPriority = otherRule.getPriority()
        val priority = getRulePriority()
        if (priority != otherPriority) {
            return false
        }
        val otherName = otherRule.getName()
        val name = getRuleName()
        if (name != otherName) {
            return false
        }
        val otherDescription = otherRule.getDescription()
        val description = getRuleDescription()
        return description == otherDescription
    }

    @Throws(Exception::class)
    private fun hashCodeMethod(): Int {
        var result = getRuleName().hashCode()
        val priority = getRulePriority()
        val description = getRuleDescription()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + priority
        return result
    }

    private fun getToStringMethod(): Method? {
        if (toStringMethod == null) {
            val methods = getMethods()
            for (method in methods) {
                if ("toString" == method.getName()) {
                    toStringMethod = method
                    return toStringMethod
                }
            }
        }
        return toStringMethod
    }

    @Throws(Exception::class)
    private fun toStringMethod(): String? {
        val toStringMethod = getToStringMethod()
        return if (toStringMethod != null) {
            toStringMethod.invoke(target) as String
        } else {
            getRuleName()
        }
    }

    @Throws(Exception::class)
    private operator fun compareTo(otherRule: Rule): Int {
        val otherPriority = otherRule.getPriority()
        val priority = getRulePriority()
        return if (priority < otherPriority) {
            -1
        } else if (priority > otherPriority) {
            1
        } else {
            val otherName = otherRule.getName()
            val name = getRuleName()
            name.compareTo(otherName)
        }
    }

    @Throws(Exception::class)
    private fun getRulePriority(): Int {
        if (priority == null) {
            var priority: Int = Rule.Companion.DEFAULT_PRIORITY
            val rule = getRuleAnnotation()
            if (rule.priority != Rule.Companion.DEFAULT_PRIORITY) {
                priority = rule.priority
            }
            val methods = getMethods()
            for (method in methods) {
                if (method.isAnnotationPresent(Priority::class.java)) {
                    priority = method.invoke(target) as Int
                    break
                }
            }
            this.priority = priority
        }
        return priority
    }

    private fun getConditionMethod(): Method {
        if (conditionMethod == null) {
            val methods = getMethods()
            for (method in methods) {
                if (method.isAnnotationPresent(Condition::class.java)) {
                    conditionMethod = method
                    return conditionMethod
                }
            }
        }
        return conditionMethod
    }

    private fun getActionMethodBeans(): MutableSet<ActionMethodOrderBean> {
        if (actionMethods == null) {
            actionMethods = TreeSet()
            val methods = getMethods()
            for (method in methods) {
                if (method.isAnnotationPresent(Action::class.java)) {
                    val actionAnnotation = method.getAnnotation(Action::class.java)
                    val order: Int = actionAnnotation.order
                    actionMethods.add(ActionMethodOrderBean(method, order))
                }
            }
        }
        return actionMethods
    }

    private fun getCompareToMethod(): Method? {
        if (compareToMethod == null) {
            val methods = getMethods()
            for (method in methods) {
                if (method.getName() == "compareTo") {
                    compareToMethod = method
                    return compareToMethod
                }
            }
        }
        return compareToMethod
    }

    private fun getMethods(): Array<Method> {
        if (methods == null) {
            methods = getTargetClass().getMethods()
        }
        return methods
    }

    private fun getRuleAnnotation(): org.jeasy.rules.annotation.Rule {
        if (annotation == null) {
            annotation = Utils.findAnnotation(org.jeasy.rules.annotation.Rule::class, getTargetClass())
        }
        return annotation
    }

    private fun getRuleName(): String {
        if (name == null) {
            val rule = getRuleAnnotation()
            name = if (rule.name == Rule.DEFAULT_NAME) getTargetClass().getSimpleName() else rule.name
        }
        return name
    }

    private fun getRuleDescription(): String? {
        if (description == null) {
            // Default description = "when " + conditionMethodName + " then " + comma separated actionMethodsNames
            val description = StringBuilder()
            appendConditionMethodName(description)
            appendActionMethodsNames(description)
            val rule = getRuleAnnotation()
            this.description =
                if (rule.description == Rule.DEFAULT_DESCRIPTION) description.toString() else rule.description
        }
        return description
    }

    private fun appendConditionMethodName(description: StringBuilder) {
        val method = getConditionMethod()
        if (method != null) {
            description.append("when ")
            description.append(method.name)
            description.append(" then ")
        }
    }

    private fun appendActionMethodsNames(description: StringBuilder) {
        val iterator = getActionMethodBeans().iterator()
        while (iterator.hasNext()) {
            description.append(iterator.next().getMethod().name)
            if (iterator.hasNext()) {
                description.append(",")
            }
        }
    }

    fun getTarget(): Any? {
        return target
    }

    private fun getTargetClass(): Class<*> {
        return target::class.java
    }

    companion object {
        private val ruleDefinitionValidator: RuleDefinitionValidator = RuleDefinitionValidator()
        private val LOGGER = LoggerFactory.getLogger(RuleProxy::class.java)

        /**
         * Makes the rule object implement the [Rule] interface.
         *
         * @param rule the annotated rule object.
         * @return a proxy that implements the [Rule] interface.
         */
        fun asRule(rule: Any): Rule {
            val result: Rule
            result = if (rule is Rule) {
                rule
            } else {
                ruleDefinitionValidator.validateRuleDefinition(rule)
                Proxy.newProxyInstance(
                    Rule::class.java.classLoader,
                    arrayOf<Class<*>?>(Rule::class.java, Comparable::class.java),
                    RuleProxy(rule)
                ) as Rule
            }
            return result
        }
    }
}
