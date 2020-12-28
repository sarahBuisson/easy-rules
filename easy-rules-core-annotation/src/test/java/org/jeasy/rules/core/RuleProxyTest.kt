package org.jeasy.rules.core

class RuleProxyTest {
    @Test
    fun proxyingHappensEvenWhenRuleIsAnnotatedWithMetaRuleAnnotation() {
        // Given
        val rule = AnnotatedRuleWithMetaRuleAnnotation()

        // When
        val proxy: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)

        // Then
        Assert.assertNotNull(proxy.description)
        Assert.assertNotNull(proxy.name)
    }

    @Test
    fun asRuleForObjectThatImplementsRule() {
        val rule: Any = BasicRule()
        val proxy: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
        Assert.assertNotNull(proxy.description)
        Assert.assertNotNull(proxy.name)
    }

    @Test
    fun asRuleForObjectThatHasProxied() {
        val rule: Any = DummyRule()
        val proxy1: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
        val proxy2: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(proxy1)
        Assert.assertEquals(proxy1.description, proxy2.description)
        Assert.assertEquals(proxy1.name, proxy2.name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun asRuleForPojo() {
        val rule = Any()
        val proxy: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
    }

    @Test
    fun invokeEquals() {
        val rule: Any = DummyRule()
        val proxy1: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
        val proxy2: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(proxy1)
        val proxy3: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(proxy2)
        // @see Object#equals(Object) reflexive
        Assert.assertEquals(rule, rule)
        Assert.assertEquals(proxy1, proxy1)
        Assert.assertEquals(proxy2, proxy2)
        Assert.assertEquals(proxy3, proxy3)
        // @see Object#equals(Object) symmetric
        Assert.assertNotEquals(rule, proxy1)
        Assert.assertNotEquals(proxy1, rule)
        Assert.assertEquals(proxy1, proxy2)
        Assert.assertEquals(proxy2, proxy1)
        // @see Object#equals(Object) transitive consistent
        Assert.assertEquals(proxy1, proxy2)
        Assert.assertEquals(proxy2, proxy3)
        Assert.assertEquals(proxy3, proxy1)
        // @see Object#equals(Object) non-null
        Assert.assertNotEquals(rule, null)
        Assert.assertNotEquals(proxy1, null)
        Assert.assertNotEquals(proxy2, null)
        Assert.assertNotEquals(proxy3, null)
    }

    @Test
    fun invokeHashCode() {
        val rule: Any = DummyRule()
        val proxy1: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
        val proxy2: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(proxy1)
        // @see Object#hashCode rule1
        Assert.assertEquals(proxy1.hashCode().toLong(), proxy1.hashCode().toLong())
        // @see Object#hashCode rule2
        Assert.assertEquals(proxy1, proxy2)
        Assert.assertEquals(proxy1.hashCode().toLong(), proxy2.hashCode().toLong())
        // @see Object#hashCode rule3
        Assert.assertNotEquals(rule, proxy1)
        Assert.assertNotEquals(rule.hashCode().toLong(), proxy1.hashCode().toLong())
    }

    @Test
    fun invokeToString() {
        val rule: Any = DummyRule()
        val proxy1: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
        val proxy2: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(proxy1)
        Assert.assertEquals(proxy1.toString(), proxy1.toString())
        Assert.assertEquals(proxy1.toString(), proxy2.toString())
        Assert.assertEquals(rule.toString(), proxy1.toString())
    }

    @Test
    fun testCompareTo() {
        @Rule
        internal class MyComparableRule(var comparisonCriteria: Int) : Comparable<MyComparableRule?> {
            @Condition
            fun `when`(): Boolean {
                return true
            }

            @Action
            fun then() {
            }

            override fun compareTo(otherRule: MyComparableRule?): Int {
                return Integer.compare(comparisonCriteria, otherRule.comparisonCriteria)
            }
        }

        val rule1: Any = MyComparableRule(1)
        val rule2: Any = MyComparableRule(2)
        val rule3: Any = MyComparableRule(2)
        val proxy1: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule1)
        val proxy2: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule2)
        val proxy3: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule3)
        Assert.assertEquals(proxy1.compareTo(proxy2).toLong(), -1)
        Assert.assertEquals(proxy2.compareTo(proxy1).toLong(), 1)
        Assert.assertEquals(proxy2.compareTo(proxy3).toLong(), 0)
        try {
            val rules = Rules()
            rules.register(rule1, rule2)
            val mixedRules = Rules(rule3)
            mixedRules.register(proxy1, proxy2)
            val yetAnotherRulesSet = Rules(proxy1, proxy2)
            yetAnotherRulesSet.register(rule3)
        } catch (exception: Exception) {
            Assert.fail("Should not fail with " + exception.message)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testCompareToWithIncorrectSignature() {
        @Rule
        internal class InvalidComparableRule {
            @Condition
            fun `when`(): Boolean {
                return true
            }

            @Action
            fun then() {
            }

            fun compareTo(): Int {
                return 0
            }
        }

        val rule: Any = InvalidComparableRule()
        val rules = Rules()
        rules.register(rule)
    }

    @Test
    fun testPriorityFromAnnotation() {
        @Rule(priority = 1)
        internal class MyRule {
            @Condition
            fun `when`(): Boolean {
                return true
            }

            @Action
            fun then() {
            }
        }

        val rule: Any = MyRule()
        val proxy: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
        Assert.assertEquals(1, proxy.priority.toLong())
    }

    @Test
    fun testPriorityFromMethod() {
        @Rule
        internal class MyRule {
            @Condition
            fun `when`(): Boolean {
                return true
            }

            @Action
            fun then() {
            }

            @Priority
            fun getPriority(): Int {
                return 2
            }
        }

        val rule: Any = MyRule()
        val proxy: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
        Assert.assertEquals(2, proxy.priority.toLong())
    }

    @Test
    fun testPriorityPrecedence() {
        @Rule(priority = 1)
        internal class MyRule {
            @Condition
            fun `when`(): Boolean {
                return true
            }

            @Action
            fun then() {
            }

            @Priority
            fun getPriority(): Int {
                return 2
            }
        }

        val rule: Any = MyRule()
        val proxy: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
        Assert.assertEquals(2, proxy.priority.toLong())
    }

    @Test
    fun testDefaultPriority() {
        @Rule
        internal class MyRule {
            @Condition
            fun `when`(): Boolean {
                return true
            }

            @Action
            fun then() {
            }
        }

        val rule: Any = MyRule()
        val proxy: org.jeasy.rules.api.Rule = RuleProxy.Companion.asRule(rule)
        Assert.assertEquals(org.jeasy.rules.api.Rule.Companion.DEFAULT_PRIORITY.toLong(), proxy.priority.toLong())
    }

    @Rule
    internal class DummyRule {
        @Condition
        fun `when`(): Boolean {
            return true
        }

        @Action
        fun then() {
        }

        override fun toString(): String {
            return "I am a Dummy rule"
        }
    }
}
