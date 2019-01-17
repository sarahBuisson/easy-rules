package org.jeasy.rules.core

import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules

//TODO : remerge with rules when kotlin alow reflexivity in js
class Rules2 : Rules {


    /**
     * Create a new [Rules] object.
     *
     * @param rules to register
     */

    constructor(rules: Set<Rule>) : super(rules) {

    }

    /**
     * Create a new [Rules] object.
     *
     * @param rules to register
     */
    constructor(vararg rules: Any) : super(setOf()) {

        for (rule in rules) {
            if (rule is Rule)
                this.rules.add(rule)
            else
                this.register(RuleProxy.asRule(rule))
        }
    }


    /**
     * Register a new rule.
     *
     * @param rule to register
     */
    fun register(rule: Any) {
        this.rules.add(RuleProxy.asRule(rule))
    }

    /**
     * Unregister a rule.
     *
     * @param rule to unregister
     */
    fun unregister(rule: Any) {
        this.rules.remove(RuleProxy.asRule(rule))
    }



    /**
     * Unregister a rule by name.
     *
     * @param ruleName the name of the rule to unregister
     */
    fun unregister(ruleName: String) {
        val rule = findRuleByName(ruleName)
        if (rule != null) {
            unregister(rule)
        }
    }

}
