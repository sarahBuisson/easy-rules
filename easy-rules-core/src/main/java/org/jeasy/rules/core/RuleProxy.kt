/**
 * The MIT License
 *
 * Copyright (c) 2018, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jeasy.rules.core

import org.jeasy.rules.annotation.Action
import org.jeasy.rules.annotation.Condition
import org.jeasy.rules.annotation.Fact
import org.jeasy.rules.annotation.Priority
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.slf4j.LoggerFactory
import java.lang.String.format
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties

/**
 * Main class to create rule proxies from annotated objects.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
open class RuleProxy private constructor(private val target: Any) : InvocationHandler {

    protected val rulePriority: Int
        @Throws(Exception::class)
        get() {
            var priority = Rule.DEFAULT_PRIORITY

            val rule = ruleAnnotation
            if (rule.priority !== Rule.DEFAULT_PRIORITY) {
                priority = rule.priority
            }

            val methods = methods
            for (method in methods) {
                if (method.findAnnotation<Priority>()!=null) {
                    priority = method.call(target) as Int
                    break
                }
            }
            return priority
        }


    private val priority: Int
        @Throws(Exception::class)
        get() {

            return rulePriority
        }

    protected val conditionMethod: KCallable<*>?
        get() {
            val methods = methods
            for (method in methods) {
                if (method.annotations.filter { it is Condition }.isNotEmpty()) {
                    return method
                }
            }
            return null
        }

    private val actionMethodBeans: Set<ActionMethodOrderBean>
        get() {
            val methods = methods
            val actionMethodBeans = TreeSet<ActionMethodOrderBean>()
            for (method in methods) {
                val actionAnnotation = method.annotations.filter { it.annotationClass == Action::class }.firstOrNull()
                if (actionAnnotation != null) {
                    val order =  method.annotations.indexOf(actionAnnotation) //TODO
                    actionMethodBeans.add(ActionMethodOrderBean(method, order))
                }
            }
            return actionMethodBeans
        }

    private val compareToMethod: KCallable<*>?
        get() {
            val methods = methods
            for (method in methods) {
                if (method.name.equals("compareTo")) {
                    return method
                }
            }
            return null
        }

    protected val methods: Collection<KCallable<*>>
        get() = targetClass.members

    protected val ruleAnnotation: org.jeasy.rules.annotation.Rule
        get() = Utils.findAnnotation(org.jeasy.rules.annotation.Rule::class, targetClass)!!

    protected val ruleName: String
        get() {
            val rule = ruleAnnotation
            return if (rule.name.equals(Rule.DEFAULT_NAME)) targetClass.simpleName!! else rule.name
        }

    protected// Default description = "when " + conditionMethodName + " then " + comma separated actionMethodsNames
    val ruleDescription: String
        get() {
            val description = StringBuilder()
            appendConditionMethodName(description)
            appendActionMethodsNames(description)

            val rule = ruleAnnotation
            return if (rule.description.equals(Rule.DEFAULT_DESCRIPTION)) description.toString() else rule.description
        }

    protected val targetClass: KClass<*>
        get() = target::class

    @Override
    @Throws(Throwable::class)
    override operator fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        val methodName = method.name
        when (methodName) {
            "getName" -> return ruleName
            "getDescription" -> return ruleDescription
            "getPriority" -> return rulePriority
            "compareTo" -> return compareToMethod(args)
            "evaluate" -> return evaluateMethod(args)
            "execute" -> return executeMethod(args)
            "equals" -> return equalsMethod(args)
            "hashCode" -> return hashCodeMethod()
            "toString" -> return toStringMethod()
            else -> return null
        }
    }

    @Throws(IllegalAccessException::class, InvocationTargetException::class)
    private fun evaluateMethod(args: Array<Any>): Any? {
        val facts = args[0] as Facts
        val conditionMethod = conditionMethod
        try {
            val actualParameters = getActualParameters(conditionMethod!!, facts)
            return conditionMethod!!.call(target, actualParameters) // validated upfront
        } catch (e: NoSuchFactException) {
            LOGGER.info("Rule '{}' has been evaluated to false due to a declared but missing fact '{}' in {}",
                    targetClass.simpleName, e.missingFact, facts)
            return false
        } catch (e: IllegalArgumentException) {
            val error = "Types of injected facts in method '%s' in rule '%s' do not match parameters types"
            throw RuntimeException(format(error, conditionMethod!!.name, targetClass.simpleName), e)
        }

    }

    @Throws(IllegalAccessException::class, InvocationTargetException::class)
    private fun executeMethod(args: Array<Any>): Any? {
        val facts = args[0] as Facts
        for (actionMethodBean in actionMethodBeans) {
            val actionMethod = actionMethodBean.method
            val actualParameters = getActualParameters(actionMethod, facts)
            actionMethod.call(target, actualParameters.toTypedArray())
        }
        return null
    }

    @Throws(Exception::class)
    private fun compareToMethod(args: Array<Any>): Any? {
        val compareToMethod = compareToMethod
        if (compareToMethod != null) {
            return compareToMethod!!.call(target, args)
        } else {
            val otherRule = args[0]
            return compareTo(otherRule)
        }
    }

    private fun getActualParameters(method: KCallable<*>, facts: Facts): List<Any> {
        val actualParameters = ArrayList<Any>()
        val parameterAnnotations = method.parameters.map { it.annotations }
        for (annotations in parameterAnnotations) {
            if (annotations.size == 1) {
                val factName = (annotations[0] as Fact).value //validated upfront.
                val fact = facts.get<Fact>(factName)
                if (fact == null && !facts.asMap().containsKey(factName)) {
                    throw NoSuchFactException(format("No fact named '%s' found in known facts: %n%s", factName, facts), factName)
                }
                actualParameters.add(fact)
            } else {
                actualParameters.add(facts) //validated upfront, there may be only one parameter not annotated and which is of type Facts.class
            }
        }
        return actualParameters
    }

    @Throws(Exception::class)
    private fun equalsMethod(args: Array<Any>): Boolean {
        if (args[0] !is Rule) {
            return false
        }
        val otherRule = args[0] as Rule
        val otherPriority = otherRule.priority
        val priority = rulePriority
        if (priority != otherPriority) {
            return false
        }
        val otherName = otherRule.name
        val name = ruleName
        if (!name.equals(otherName)) {
            return false
        }
        val otherDescription = otherRule.description
        val description = ruleDescription
        return !if (description != null) !description.equals(otherDescription) else otherDescription != null
    }

    @Throws(Exception::class)
    private fun hashCodeMethod(): Int {
        var result = ruleName.hashCode()
        val priority = rulePriority
        val description = ruleDescription
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + priority
        return result
    }

    @Throws(Exception::class)
    private fun toStringMethod(): String {
        val methods = methods
        for (method in methods) {
            if ("toString".equals(method.name)) {
                return method.call(target).toString()
            }
        }
        return ruleName
    }

    @Throws(Exception::class)
    private operator fun compareTo(otherRule: Any): Int {
        if (otherRule is RuleProxy) {
            val otherPriority = otherRule.rulePriority
            val priority = rulePriority
            if (priority < otherPriority) {
                return -1
            } else if (priority > otherPriority) {
                return 1
            } else {
                val otherName = otherRule.ruleName
                val name = ruleName
                return name.compareTo(otherName)
            }
        } else if (otherRule is Rule) {
            val otherPriority = otherRule.priority
            val priority = rulePriority
            if (priority < otherPriority) {
                return -1
            } else if (priority > otherPriority) {
                return 1
            } else {
                val otherName = otherRule.name
                val name = ruleName
                return name.compareTo(otherName)
            }
        }
        return 0;
    }

    private fun appendConditionMethodName(description: StringBuilder) {
        val method = conditionMethod
        if (method != null) {
            description.append("when ")
            description.append(method!!.name)
            description.append(" then ")
        }
    }

    private fun appendActionMethodsNames(description: StringBuilder) {
        description.append(actionMethodBeans.map { it.method.name }
                .joinToString(","))
    }

    companion object {

        private val ruleDefinitionValidator = RuleDefinitionValidator()

        private val LOGGER = LoggerFactory.getLogger(RuleProxy::class.java)

        /**
         * Makes the rule object implement the [Rule] interface.
         *
         * @param rule the annotated rule object.
         * @return a proxy that implements the [Rule] interface.
         */
        fun asRule(rule: Any): Rule {
            val result: Rule
            if (rule is Rule) {
                result = rule as Rule
            } else {
                val annotation = Utils.findAnnotation<org.jeasy.rules.annotation.Rule>(org.jeasy.rules.annotation.Rule::class, rule::class as KClass<org.jeasy.rules.annotation.Rule>)

                ruleDefinitionValidator.validateRuleDefinition(rule)

                result = object :RuleProxy(rule), Rule, Comparable<Rule>  {

                    override val name: String
                        get() = this.ruleName//To change initializer of created properties use File | Settings | File Templates.

                    override val description: String
                        get() = this.ruleDescription!!//To change initializer of created properties use File | Settings | File Templates.

                    override val priority: Int
                        get() = this.rulePriority //To change initializer of created properties use File | Settings | File Templates.

                    override fun evaluate(facts: Facts): Boolean {
                        try {
                            return rule::class.functions.filter { it.findAnnotation<Condition>() != null }
                                    .map { it.callBy(toArg(it, facts, rule)) as Boolean }
                                    .reduce { acc: Boolean, it: Boolean -> return acc && it }//To change body of created functions use File | Settings | File Templates.
                        }catch (e:java.lang.Exception){
                            e.printStackTrace()
                            return false
                        }
                    }

                    override fun execute(facts: Facts) {
                        try {
                        rule::class.functions.filter { it.findAnnotation<Action>() != null }
                                .map { it.callBy(toArg(it, facts, rule)) }//To change body of created functions use File | Settings | File Templates.
                        }catch (e:java.lang.Exception){
                            e.printStackTrace()
                        }
                    }

                    override fun compareTo(other: Rule): Int {
                        if (priority < other.priority) {
                            return -1
                        } else if (priority > other.priority) {
                            return 1
                        } else {
                            val otherName = other.name
                            return name.compareTo(otherName)
                        }
                    }
                    override fun toString():String{
                        return rule.toString()
                    }

                }


            }
            return result
        }

        private fun toArg(function: KFunction<*>, facts: Facts, rule: Any): Map<KParameter, Any?> {


            val argsMap = mutableMapOf<KParameter, Any?>()

            function.parameters.forEach { param ->
                val annotation = param.findAnnotation<Fact>()
                if (annotation != null) {
                    argsMap.put(param, facts.get<Any?>(annotation.value))

                } else if (param.name != null) {
                    argsMap.put(param, facts.get<Any?>(param.name!!))
                } else if (param.kind == KParameter.Kind.INSTANCE) {
                    argsMap.put(param, rule)
                }
            }
            return argsMap
        }
    }

}
