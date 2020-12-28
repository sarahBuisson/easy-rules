package org.jeasy.rules.core

class RuleDefinitionValidatorTest {
    private var ruleDefinitionValidator: RuleDefinitionValidator? = null
    @Before
    fun setup() {
        ruleDefinitionValidator = RuleDefinitionValidator()
    }

    /*
     * Rule annotation test
     */
    @Test(expected = IllegalArgumentException::class)
    fun notAnnotatedRuleMustNotBeAccepted() {
        ruleDefinitionValidator.validateRuleDefinition(Any())
    }

    @Test
    fun withCustomAnnotationThatIsItselfAnnotatedWithTheRuleAnnotation() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithMetaRuleAnnotation())
    }

    /*
     * Conditions methods tests
     */
    @Test(expected = IllegalArgumentException::class)
    fun conditionMethodMustBeDefined() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithoutConditionMethod())
    }

    @Test(expected = IllegalArgumentException::class)
    fun conditionMethodMustBePublic() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithNonPublicConditionMethod())
    }

    @Test(expected = IllegalArgumentException::class)
    fun whenConditionMethodHasOneNonAnnotatedParameter_thenThisParameterMustBeOfTypeFacts() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithConditionMethodHavingOneArgumentNotOfTypeFacts())
    }

    @Test(expected = IllegalArgumentException::class)
    fun conditionMethodMustReturnBooleanType() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithConditionMethodHavingNonBooleanReturnType())
    }

    @Test(expected = IllegalArgumentException::class)
    fun conditionMethodParametersShouldAllBeAnnotatedWithFactUnlessExactlyOneOfThemIsOfTypeFacts() {
        ruleDefinitionValidator.validateRuleDefinition(
            AnnotatedRuleWithOneParameterNotAnnotatedWithFactAndNotOfTypeFacts()
        )
    }

    /*
     * Action method tests
     */
    @Test(expected = IllegalArgumentException::class)
    fun actionMethodMustBeDefined() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithoutActionMethod())
    }

    @Test(expected = IllegalArgumentException::class)
    fun actionMethodMustBePublic() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithNonPublicActionMethod())
    }

    @Test(expected = IllegalArgumentException::class)
    fun actionMethodMustHaveAtMostOneArgumentOfTypeFacts() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithActionMethodHavingOneArgumentNotOfTypeFacts())
    }

    @Test(expected = IllegalArgumentException::class)
    fun actionMethodMustHaveExactlyOneArgumentOfTypeFactsIfAny() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithActionMethodHavingMoreThanOneArgumentOfTypeFacts())
    }

    @Test(expected = IllegalArgumentException::class)
    fun actionMethodMustReturnVoid() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithActionMethodThatReturnsNonVoidType())
    }

    @Test(expected = IllegalArgumentException::class)
    fun actionMethodParametersShouldAllBeAnnotatedWithFactUnlessExactlyOneOfThemIsOfTypeFacts() {
        ruleDefinitionValidator.validateRuleDefinition(
            AnnotatedRuleWithOneParameterNotAnnotatedWithFactAndNotOfTypeFacts()
        )
    }

    /*
     * Priority method tests
     */
    @Test(expected = IllegalArgumentException::class)
    fun priorityMethodMustBeUnique() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithMoreThanOnePriorityMethod())
    }

    @Test(expected = IllegalArgumentException::class)
    fun priorityMethodMustBePublic() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithNonPublicPriorityMethod())
    }

    @Test(expected = IllegalArgumentException::class)
    fun priorityMethodMustHaveNoArguments() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithPriorityMethodHavingArguments())
    }

    @Test(expected = IllegalArgumentException::class)
    fun priorityMethodReturnTypeMustBeInteger() {
        ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithPriorityMethodHavingNonIntegerReturnType())
    }

    /*
     * Valid definition tests
     */
    @Test
    fun validAnnotationsShouldBeAccepted() {
        try {
            ruleDefinitionValidator.validateRuleDefinition(
                AnnotatedRuleWithMultipleAnnotatedParametersAndOneParameterOfTypeFacts()
            )
            ruleDefinitionValidator.validateRuleDefinition(
                AnnotatedRuleWithMultipleAnnotatedParametersAndOneParameterOfSubTypeFacts()
            )
            ruleDefinitionValidator.validateRuleDefinition(AnnotatedRuleWithActionMethodHavingOneArgumentOfTypeFacts())
        } catch (throwable: Throwable) {
            Assertions.fail<Any?>("Should not throw exception for valid rule definitions")
        }
    }
}
