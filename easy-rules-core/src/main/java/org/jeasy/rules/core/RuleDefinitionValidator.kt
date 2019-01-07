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

import org.jeasy.rules.annotation.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

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
        if (!isRuleClassWellDefined(rule)) {
            throw IllegalArgumentException(String.format("Rule '%s' is not annotated with '%s'", rule::class.qualifiedName, Rule::class.qualifiedName))
        }
    }

    private fun checkConditionMethod(rule: Any) {
        val conditionMethods = getMethodsAnnotatedWith(Condition::class, rule)
        if (conditionMethods.isEmpty()) {
            throw IllegalArgumentException(String.format("Rule '%s' must have a public method annotated with '%s'", rule::class.qualifiedName, Condition::class.qualifiedName))
        }

        if (conditionMethods.size > 1) {
            throw IllegalArgumentException(String.format("Rule '%s' must have exactly one method annotated with '%s'", rule::class.qualifiedName, Condition::class.qualifiedName))
        }

        val conditionMethod = conditionMethods[0]

        if (!isConditionMethodWellDefined(conditionMethod)) {
            throw IllegalArgumentException(String.format("Condition method '%s' defined in rule '%s' must be public, may have parameters annotated with @Fact (and/or exactly one parameter of type or extending Facts) and return boolean type.", conditionMethod, rule::class.qualifiedName))
        }
    }

    private fun checkActionMethods(rule: Any) {
        val actionMethods = getMethodsAnnotatedWith(Action::class, rule)
        if (actionMethods.isEmpty()) {
            throw IllegalArgumentException(String.format("Rule '%s' must have at least one public method annotated with '%s'", rule::class.qualifiedName, Action::class.qualifiedName))
        }

        for (actionMethod in actionMethods) {
            if (!isActionMethodWellDefined(actionMethod)) {
                throw IllegalArgumentException(String.format("Action method '%s' defined in rule '%s' must be public, must return void type and may have parameters annotated with @Fact (and/or exactly one parameter of type or extending Facts).", actionMethod, rule::class.qualifiedName))
            }
        }
    }

    private fun checkPriorityMethod(rule: Any) {

        val priorityMethods = getMethodsAnnotatedWith(Priority::class, rule)

        if (priorityMethods.isEmpty()) {
            return
        }

        if (priorityMethods.size > 1) {
            throw IllegalArgumentException(String.format("Rule '%s' must have exactly one method annotated with '%s'", rule::class.qualifiedName, Priority::class.qualifiedName))
        }

        val priorityMethod = priorityMethods[0]

        if (!isPriorityMethodWellDefined(priorityMethod)) {
            throw IllegalArgumentException(String.format("Priority method '%s' defined in rule '%s' must be public, have no parameters and return integer type.", priorityMethod, rule::class.qualifiedName))
        }
    }

    private fun isRuleClassWellDefined(rule: Any): Boolean {
        return Utils.isAnnotationPresent(Rule::class, rule::class)
    }

    private fun isConditionMethodWellDefined(method: KFunction<*>): Boolean {
        return (method.visibility==KVisibility.PUBLIC
                && (method.returnType.toString().contains("kotlin.Boolean")) //TODO
                && validParameters(method))
    }

    private fun validParameters(method: KFunction<*>): Boolean {
        var notAnnotatedParameterCount = 0
      
        for (param in method.parameters) {
            if (param.kind != KParameter.Kind.INSTANCE)
                if (param.annotations.size == 0) {
                    notAnnotatedParameterCount += 1
                } else if (param.findAnnotation<Fact>() == null) {
                        return false
                    }



        }
        if (notAnnotatedParameterCount > 1) {
            return false
        }
        val parameterTypes = method.parameters
        return if (parameterTypes.filter { it.kind!=KParameter.Kind.INSTANCE }.size == 1 && notAnnotatedParameterCount == 1) {
            return parameterTypes.filter { it.kind!=KParameter.Kind.INSTANCE }.map{it.type.toString()}.filter { it.contains("Facts") }.isNotEmpty() //TODO
        } else true
    }

    private fun isActionMethodWellDefined(method: KFunction<*>): Boolean {
        return (method.visibility== KVisibility.PUBLIC
               // && method.returnType.equals(Void.TYPE)//TODO : return void
                && validParameters(method))
    }

    private fun isPriorityMethodWellDefined(method: KFunction<*>): Boolean {
        return (method.visibility==KVisibility.PUBLIC
                && method.returnType.toString().contains("kotlin.Int")
                && method.typeParameters.size === 0)
    }

    private fun getMethodsAnnotatedWith(annotation: KClass<out Annotation>, rule: Any): List<KFunction<*>> {
        val methods = getMethods(rule)
        val annotatedMethods = ArrayList<KFunction<*>>()
        for (method in methods) {
            if (method.annotations.find { it.annotationClass == annotation }!=null) {
                annotatedMethods.add(method)
            }
        }
        return annotatedMethods
    }

    private fun getMethods(rule: Any): Collection<KFunction<*>> {
        return rule::class.memberFunctions
    }

}
