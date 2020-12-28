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
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.Parameter
import java.util.*

/**
 * This component validates that an annotated rule object is well defined.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
internal class RuleDefinitionValidator {
    fun validateRuleDefinition(rule: Any) {
        checkRuleClass(rule)
        checkConditionMethod(rule)
        checkActionMethods(rule)
        checkPriorityMethod(rule)
    }

    private fun checkRuleClass(rule: Any) {
        require(isRuleClassWellDefined(rule)) {
            String.format(
                "Rule '%s' is not annotated with '%s'",
                rule.javaClass.name,
                Rule::class.java.name
            )
        }
    }

    private fun checkConditionMethod(rule: Any) {
        val conditionMethods = getMethodsAnnotatedWith(
            Condition::class.java, rule
        )
        require(!conditionMethods.isEmpty()) {
            String.format(
                "Rule '%s' must have a public method annotated with '%s'",
                rule.javaClass.name,
                Condition::class.java.name
            )
        }
        require(conditionMethods.size <= 1) {
            String.format(
                "Rule '%s' must have exactly one method annotated with '%s'",
                rule.javaClass.name,
                Condition::class.java.name
            )
        }
        val conditionMethod = conditionMethods.get(0)
        require(isConditionMethodWellDefined(conditionMethod)) {
            String.format(
                "Condition method '%s' defined in rule '%s' must be public, must return boolean type and may have parameters annotated with @Fact (and/or exactly one parameter of type Facts or one of its sub-types).",
                conditionMethod,
                rule.javaClass.name
            )
        }
    }

    private fun checkActionMethods(rule: Any) {
        val actionMethods = getMethodsAnnotatedWith(Action::class.java, rule)
        require(!actionMethods.isEmpty()) {
            String.format(
                "Rule '%s' must have at least one public method annotated with '%s'",
                rule.javaClass.name,
                Action::class.java.name
            )
        }
        for (actionMethod in actionMethods) {
            require(isActionMethodWellDefined(actionMethod)) {
                String.format(
                    "Action method '%s' defined in rule '%s' must be public, must return void type and may have parameters annotated with @Fact (and/or exactly one parameter of type Facts or one of its sub-types).",
                    actionMethod,
                    rule.javaClass.name
                )
            }
        }
    }

    private fun checkPriorityMethod(rule: Any) {
        val priorityMethods = getMethodsAnnotatedWith(Priority::class.java, rule)
        if (priorityMethods.isEmpty()) {
            return
        }
        require(priorityMethods.size <= 1) {
            String.format(
                "Rule '%s' must have exactly one method annotated with '%s'",
                rule.javaClass.name,
                Priority::class.java.name
            )
        }
        val priorityMethod = priorityMethods.get(0)
        require(isPriorityMethodWellDefined(priorityMethod)) {
            String.format(
                "Priority method '%s' defined in rule '%s' must be public, have no parameters and return integer type.",
                priorityMethod,
                rule.javaClass.name
            )
        }
    }

    private fun isRuleClassWellDefined(rule: Any): Boolean {
        return Utils.isAnnotationPresent(Rule::class.java, rule.javaClass)
    }

    private fun isConditionMethodWellDefined(method: Method): Boolean {
        return (Modifier.isPublic(method.getModifiers())
                && method.getReturnType() == java.lang.Boolean.TYPE && validParameters(method))
    }

    private fun validParameters(method: Method): Boolean {
        var notAnnotatedParameterCount = 0
        val parameterAnnotations = method.getParameterAnnotations()
        for (annotations in parameterAnnotations) {
            if (annotations.size == 0) {
                notAnnotatedParameterCount += 1
            } else {
                //Annotation types has to be Fact
                for (annotation in annotations) {
                    if (annotation.annotationType != Fact::class.java) {
                        return false
                    }
                }
            }
        }
        if (notAnnotatedParameterCount > 1) {
            return false
        }
        if (notAnnotatedParameterCount == 1) {
            val notAnnotatedParameter = getNotAnnotatedParameter(method)
            if (notAnnotatedParameter != null) {
                return Facts::class.java.isAssignableFrom(notAnnotatedParameter.type)
            }
        }
        return true
    }

    private fun getNotAnnotatedParameter(method: Method): Parameter? {
        val parameters = method.getParameters()
        for (parameter in parameters) {
            if (parameter.annotations.size == 0) {
                return parameter
            }
        }
        return null
    }

    private fun isActionMethodWellDefined(method: Method): Boolean {
        return (Modifier.isPublic(method.getModifiers())
                && method.getReturnType() == Void.TYPE && validParameters(method))
    }

    private fun isPriorityMethodWellDefined(method: Method): Boolean {
        return (Modifier.isPublic(method.getModifiers())
                && method.getReturnType() == Integer.TYPE && method.getParameterTypes().size == 0)
    }

    private fun getMethodsAnnotatedWith(annotation: Class<out Annotation>, rule: Any): MutableList<Method> {
        val methods = getMethods(rule)
        val annotatedMethods: MutableList<Method> = ArrayList()
        for (method in methods) {
            if (method.isAnnotationPresent(annotation)) {
                annotatedMethods.add(method)
            }
        }
        return annotatedMethods
    }

    private fun getMethods(rule: Any): Array<Method> {
        return rule.javaClass.methods
    }
}
