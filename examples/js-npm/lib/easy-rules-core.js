(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin', 'kotlin-logging'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'), require('./kotlin-logging'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'easy-rules-core'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'easy-rules-core'.");
    }
    if (typeof this['kotlin-logging'] === 'undefined') {
      throw new Error("Error loading module 'easy-rules-core'. Its dependency 'kotlin-logging' was not found. Please, check whether 'kotlin-logging' is loaded prior to 'easy-rules-core'.");
    }
    root['easy-rules-core'] = factory(typeof this['easy-rules-core'] === 'undefined' ? {} : this['easy-rules-core'], kotlin, this['kotlin-logging']);
  }
}(this, function (_, Kotlin, $module$kotlin_logging) {
  'use strict';
  var Kind_INTERFACE = Kotlin.Kind.INTERFACE;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var Kind_OBJECT = Kotlin.Kind.OBJECT;
  var Any = Object;
  var throwCCE = Kotlin.throwCCE;
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_mqih57$;
  var toString = Kotlin.toString;
  var Iterable = Kotlin.kotlin.collections.Iterable;
  var Comparable = Kotlin.kotlin.Comparable;
  var sorted = Kotlin.kotlin.collections.sorted_exjks8$;
  var toList = Kotlin.kotlin.collections.toList_7wnvza$;
  var equals = Kotlin.equals;
  var Unit = Kotlin.kotlin.Unit;
  var ensureNotNull = Kotlin.ensureNotNull;
  var hashCode = Kotlin.hashCode;
  var mu = $module$kotlin_logging.mu;
  var Exception = Kotlin.kotlin.Exception;
  var HashMap_init = Kotlin.kotlin.collections.HashMap_init_q3lmfv$;
  var ArrayList_init_0 = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  var toSet = Kotlin.kotlin.collections.toSet_7wnvza$;
  DefaultRule.prototype = Object.create(BasicRule.prototype);
  DefaultRule.prototype.constructor = DefaultRule;
  function Action() {
  }
  Action.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'Action',
    interfaces: []
  };
  function Condition() {
    Condition$Companion_getInstance();
  }
  function Condition$Companion() {
    Condition$Companion_instance = this;
    this.FALSE = new Condition$Companion$FALSE$ObjectLiteral();
    this.TRUE = new Condition$Companion$TRUE$ObjectLiteral();
  }
  function Condition$Companion$FALSE$ObjectLiteral() {
  }
  Condition$Companion$FALSE$ObjectLiteral.prototype.evaluate_qykua8$ = function (facts) {
    return false;
  };
  Condition$Companion$FALSE$ObjectLiteral.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Condition]
  };
  function Condition$Companion$TRUE$ObjectLiteral() {
  }
  Condition$Companion$TRUE$ObjectLiteral.prototype.evaluate_qykua8$ = function (facts) {
    return true;
  };
  Condition$Companion$TRUE$ObjectLiteral.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Condition]
  };
  Condition$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var Condition$Companion_instance = null;
  function Condition$Companion_getInstance() {
    if (Condition$Companion_instance === null) {
      new Condition$Companion();
    }
    return Condition$Companion_instance;
  }
  Condition.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'Condition',
    interfaces: []
  };
  var LinkedHashMap_init = Kotlin.kotlin.collections.LinkedHashMap_init_q3lmfv$;
  function Facts() {
    this.facts_vl31fz$_0 = LinkedHashMap_init();
  }
  Object.defineProperty(Facts.prototype, 'size', {
    get: function () {
      return this.facts_vl31fz$_0.size;
    }
  });
  Object.defineProperty(Facts.prototype, 'isEmpty', {
    get: function () {
      return this.facts_vl31fz$_0.isEmpty();
    }
  });
  Facts.prototype.put_4w9ihe$ = function (name, fact) {
    return this.facts_vl31fz$_0.put_xwzc9p$(name, fact);
  };
  Facts.prototype.remove_61zpoe$ = function (name) {
    return this.facts_vl31fz$_0.remove_11rb$(name);
  };
  Facts.prototype.get_ytbaoo$ = function (name) {
    var tmp$;
    return (tmp$ = this.facts_vl31fz$_0.get_11rb$(name)) == null || Kotlin.isType(tmp$, Any) ? tmp$ : throwCCE();
  };
  Facts.prototype.asMap = function () {
    return this.facts_vl31fz$_0;
  };
  Facts.prototype.iterator = function () {
    return this.facts_vl31fz$_0.entries.iterator();
  };
  Facts.prototype.toString = function () {
    var tmp$;
    var stringBuilder = '[';
    var entries = ArrayList_init(this.facts_vl31fz$_0.entries);
    tmp$ = entries.size;
    for (var i = 0; i < tmp$; i++) {
      var entry = entries.get_za3lpa$(i);
      stringBuilder += ' { ' + entry.key + ' : ' + toString(entry.value) + '  } ';
      if (i < (entries.size - 1 | 0)) {
        stringBuilder += ',';
      }
    }
    stringBuilder += ']';
    return stringBuilder.toString();
  };
  Facts.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Facts',
    interfaces: [Iterable]
  };
  function Rule() {
    Rule$Companion_getInstance();
  }
  function Rule$Companion() {
    Rule$Companion_instance = this;
    this.DEFAULT_NAME = 'rule';
    this.DEFAULT_DESCRIPTION = 'description';
    this.DEFAULT_PRIORITY = 2147483646;
  }
  Rule$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var Rule$Companion_instance = null;
  function Rule$Companion_getInstance() {
    if (Rule$Companion_instance === null) {
      new Rule$Companion();
    }
    return Rule$Companion_instance;
  }
  Rule.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'Rule',
    interfaces: [Comparable]
  };
  function RuleListener() {
  }
  RuleListener.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'RuleListener',
    interfaces: []
  };
  var LinkedHashSet_init = Kotlin.kotlin.collections.LinkedHashSet_init_287e2$;
  function Rules() {
    this.rules = LinkedHashSet_init();
  }
  Object.defineProperty(Rules.prototype, 'isEmpty', {
    get: function () {
      return this.rules.isEmpty();
    }
  });
  Object.defineProperty(Rules.prototype, 'size', {
    get: function () {
      return this.rules.size;
    }
  });
  Rules.prototype.clear = function () {
    this.rules.clear();
  };
  Rules.prototype.iterator = function () {
    return sorted(this.rules).iterator();
  };
  Rules.prototype.register_sddt03$ = function (rule) {
    this.rules.add_11rb$(rule);
  };
  Rules.prototype.unregister_sddt03$ = function (rule) {
    this.rules.remove_11rb$(rule);
  };
  Rules.prototype.get_za3lpa$ = function (i) {
    return toList(sorted(this.rules)).get_za3lpa$(i);
  };
  Rules.prototype.findRuleByName_61zpoe$ = function (ruleName) {
    var tmp$;
    tmp$ = this.rules.iterator();
    while (tmp$.hasNext()) {
      var rule = tmp$.next();
      if (equals(rule.name.toUpperCase(), ruleName.toUpperCase()))
        return rule;
    }
    return null;
  };
  Rules.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Rules',
    interfaces: [Iterable]
  };
  function Rules_init(rules, $this) {
    $this = $this || Object.create(Rules.prototype);
    Rules.call($this);
    $this.rules.addAll_brywnq$(rules);
    return $this;
  }
  function Rules_init_0($this) {
    $this = $this || Object.create(Rules.prototype);
    Rules.call($this);
    return $this;
  }
  function RulesEngine() {
  }
  RulesEngine.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'RulesEngine',
    interfaces: []
  };
  function RulesEngineListener() {
  }
  RulesEngineListener.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'RulesEngineListener',
    interfaces: []
  };
  function BasicRule(name, description, priority) {
    if (name === void 0)
      name = Rule$Companion_getInstance().DEFAULT_NAME;
    if (description === void 0)
      description = Rule$Companion_getInstance().DEFAULT_DESCRIPTION;
    if (priority === void 0)
      priority = Rule$Companion_getInstance().DEFAULT_PRIORITY;
    this.name_dhhza9$_0 = name;
    this.description_aydjde$_0 = description;
    this.priority_ujb21y$_0 = priority;
  }
  Object.defineProperty(BasicRule.prototype, 'name', {
    get: function () {
      return this.name_dhhza9$_0;
    },
    set: function (name) {
      this.name_dhhza9$_0 = name;
    }
  });
  Object.defineProperty(BasicRule.prototype, 'description', {
    get: function () {
      return this.description_aydjde$_0;
    },
    set: function (description) {
      this.description_aydjde$_0 = description;
    }
  });
  Object.defineProperty(BasicRule.prototype, 'priority', {
    get: function () {
      return this.priority_ujb21y$_0;
    },
    set: function (priority) {
      this.priority_ujb21y$_0 = priority;
    }
  });
  BasicRule.prototype.evaluate_qykua8$ = function (facts) {
    return false;
  };
  BasicRule.prototype.execute_qykua8$ = function (facts) {
  };
  BasicRule.prototype.equals = function (o) {
    var tmp$;
    if (this === o)
      return true;
    if (o == null || Kotlin.getKClassFromExpression(this) !== Kotlin.getKClassFromExpression(ensureNotNull(o)))
      return false;
    var basicRule = (tmp$ = o) == null || Kotlin.isType(tmp$, BasicRule) ? tmp$ : throwCCE();
    if (this.priority !== ensureNotNull(basicRule).priority)
      return false;
    return !equals(this.name, basicRule.name) ? false : !(this.description != null ? !equals(ensureNotNull(this.description), basicRule.description) : basicRule.description != null);
  };
  BasicRule.prototype.hashCode = function () {
    var result = hashCode(this.name);
    result = (31 * result | 0) + (this.description != null ? hashCode(ensureNotNull(this.description)) : 0) | 0;
    result = (31 * result | 0) + this.priority | 0;
    return result;
  };
  BasicRule.prototype.toString = function () {
    return this.name;
  };
  BasicRule.prototype.compareTo_11rb$ = function (rule) {
    var tmp$;
    if (this.priority < rule.priority) {
      tmp$ = -1;
    }
     else if (this.priority > rule.priority) {
      tmp$ = 1;
    }
     else {
      tmp$ = Kotlin.compareTo(this.name, rule.name);
    }
    return tmp$;
  };
  BasicRule.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'BasicRule',
    interfaces: [Rule]
  };
  function DefaultRule(name, description, priority, condition, actions) {
    if (condition === void 0)
      condition = Condition$Companion_getInstance().FALSE;
    if (actions === void 0) {
      actions = ArrayList_init_0();
    }
    BasicRule.call(this, name, description, priority);
    this.condition = condition;
    this.actions = actions;
  }
  DefaultRule.prototype.evaluate_qykua8$ = function (facts) {
    return this.condition.evaluate_qykua8$(facts);
  };
  DefaultRule.prototype.execute_qykua8$ = function (facts) {
    var tmp$;
    tmp$ = this.actions.iterator();
    while (tmp$.hasNext()) {
      var action = tmp$.next();
      action.execute_qykua8$(facts);
    }
  };
  DefaultRule.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DefaultRule',
    interfaces: [BasicRule]
  };
  function DefaultRuleListener() {
    DefaultRuleListener$Companion_getInstance();
  }
  DefaultRuleListener.prototype.beforeEvaluate_p4kgl3$ = function (rule, facts) {
    return true;
  };
  function DefaultRuleListener$afterEvaluate$lambda(closure$ruleName) {
    return function () {
      return "Rule '" + closure$ruleName + "' triggered";
    };
  }
  function DefaultRuleListener$afterEvaluate$lambda_0(closure$ruleName) {
    return function () {
      return "Rule '" + closure$ruleName + "' has been evaluated to false, it has not been executed";
    };
  }
  DefaultRuleListener.prototype.afterEvaluate_df759s$ = function (rule, facts, evaluationResult) {
    var ruleName = rule.name;
    if (evaluationResult) {
      DefaultRuleListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRuleListener$afterEvaluate$lambda(ruleName));
    }
     else {
      DefaultRuleListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRuleListener$afterEvaluate$lambda_0(ruleName));
    }
  };
  DefaultRuleListener.prototype.beforeExecute_p4kgl3$ = function (rule, facts) {
  };
  function DefaultRuleListener$onSuccess$lambda(closure$rule) {
    return function () {
      return "Rule '" + closure$rule.name + "' performed successfully";
    };
  }
  DefaultRuleListener.prototype.onSuccess_p4kgl3$ = function (rule, facts) {
    DefaultRuleListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRuleListener$onSuccess$lambda(rule));
  };
  function DefaultRuleListener$onFailure$lambda(closure$rule) {
    return function () {
      return "Rule '" + closure$rule.name + "' performed with error";
    };
  }
  function DefaultRuleListener$onFailure$lambda_0(closure$exception) {
    return function () {
      return closure$exception;
    };
  }
  DefaultRuleListener.prototype.onFailure_wkz2jb$ = function (rule, facts, exception) {
    DefaultRuleListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRuleListener$onFailure$lambda(rule));
    DefaultRuleListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRuleListener$onFailure$lambda_0(exception));
  };
  function DefaultRuleListener$Companion() {
    DefaultRuleListener$Companion_instance = this;
    this.LOGGER_0 = mu.KotlinLogging.logger_o14v8n$(DefaultRuleListener$Companion$LOGGER$lambda);
  }
  function DefaultRuleListener$Companion$LOGGER$lambda() {
    return Unit;
  }
  DefaultRuleListener$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var DefaultRuleListener$Companion_instance = null;
  function DefaultRuleListener$Companion_getInstance() {
    if (DefaultRuleListener$Companion_instance === null) {
      new DefaultRuleListener$Companion();
    }
    return DefaultRuleListener$Companion_instance;
  }
  DefaultRuleListener.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DefaultRuleListener',
    interfaces: [RuleListener]
  };
  function DefaultRulesEngine() {
    DefaultRulesEngine$Companion_getInstance();
    this.parameters_kunhqe$_0 = null;
    this.ruleListeners_iyzsqp$_0 = null;
    this.rulesEngineListeners_27qc3a$_0 = null;
  }
  Object.defineProperty(DefaultRulesEngine.prototype, 'parameters', {
    get: function () {
      return this.parameters_kunhqe$_0;
    }
  });
  Object.defineProperty(DefaultRulesEngine.prototype, 'ruleListeners', {
    get: function () {
      return this.ruleListeners_iyzsqp$_0;
    }
  });
  Object.defineProperty(DefaultRulesEngine.prototype, 'rulesEngineListeners', {
    get: function () {
      return this.rulesEngineListeners_27qc3a$_0;
    }
  });
  DefaultRulesEngine.prototype.fire_eo0djw$ = function (rules, facts) {
    this.triggerListenersBeforeRules_0(rules, facts);
    this.doFire_k2hesn$(rules, facts);
    this.triggerListenersAfterRules_0(rules, facts);
  };
  function DefaultRulesEngine$doFire$lambda(this$DefaultRulesEngine, closure$name, closure$priority) {
    return function () {
      return 'Rule priority threshold (' + this$DefaultRulesEngine.parameters.priorityThreshold + ") exceeded at rule '" + closure$name + "' with priority=" + closure$priority + ', next rules will be skipped';
    };
  }
  function DefaultRulesEngine$doFire$lambda_0(closure$name) {
    return function () {
      return "Rule '" + closure$name + "' has been skipped before being evaluated";
    };
  }
  function DefaultRulesEngine$doFire$lambda_1() {
    return 'Next rules will be skipped since parameter skipOnFirstAppliedRule is set';
  }
  function DefaultRulesEngine$doFire$lambda_2() {
    return 'Next rules will be skipped since parameter skipOnFirstFailedRule is set';
  }
  function DefaultRulesEngine$doFire$lambda_3() {
    return 'Next rules will be skipped since parameter skipOnFirstNonTriggeredRule is set';
  }
  DefaultRulesEngine.prototype.doFire_k2hesn$ = function (rules, facts) {
    var tmp$;
    tmp$ = rules.iterator();
    while (tmp$.hasNext()) {
      var rule = tmp$.next();
      var name = rule.name;
      var priority = rule.priority;
      if (priority > this.parameters.priorityThreshold) {
        DefaultRulesEngine$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngine$doFire$lambda(this, name, priority));
        break;
      }
      if (!this.shouldBeEvaluated_0(rule, facts)) {
        DefaultRulesEngine$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngine$doFire$lambda_0(name));
        continue;
      }
      if (rule.evaluate_qykua8$(facts)) {
        this.triggerListenersAfterEvaluate_0(rule, facts, true);
        try {
          this.triggerListenersBeforeExecute_0(rule, facts);
          rule.execute_qykua8$(facts);
          this.triggerListenersOnSuccess_0(rule, facts);
          if (this.parameters.isSkipOnFirstAppliedRule) {
            DefaultRulesEngine$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngine$doFire$lambda_1);
            break;
          }
        }
         catch (exception) {
          if (Kotlin.isType(exception, Exception)) {
            this.triggerListenersOnFailure_0(rule, exception, facts);
            if (this.parameters.isSkipOnFirstFailedRule) {
              DefaultRulesEngine$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngine$doFire$lambda_2);
              break;
            }
          }
           else
            throw exception;
        }
      }
       else {
        this.triggerListenersAfterEvaluate_0(rule, facts, false);
        if (this.parameters.isSkipOnFirstNonTriggeredRule) {
          DefaultRulesEngine$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngine$doFire$lambda_3);
          break;
        }
      }
    }
  };
  DefaultRulesEngine.prototype.check_eo0djw$ = function (rules, facts) {
    this.triggerListenersBeforeRules_0(rules, facts);
    var result = this.doCheck_0(rules, facts);
    this.triggerListenersAfterRules_0(rules, facts);
    return result;
  };
  function DefaultRulesEngine$doCheck$lambda() {
    return 'Checking rules';
  }
  DefaultRulesEngine.prototype.doCheck_0 = function (rules, facts) {
    var tmp$;
    DefaultRulesEngine$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngine$doCheck$lambda);
    var result = HashMap_init();
    tmp$ = rules.iterator();
    while (tmp$.hasNext()) {
      var rule = tmp$.next();
      if (this.shouldBeEvaluated_0(rule, facts)) {
        result.put_xwzc9p$(rule, rule.evaluate_qykua8$(facts));
      }
    }
    return result;
  };
  DefaultRulesEngine.prototype.triggerListenersOnFailure_0 = function (rule, exception, facts) {
    var tmp$;
    tmp$ = this.ruleListeners.iterator();
    while (tmp$.hasNext()) {
      var ruleListener = tmp$.next();
      ruleListener.onFailure_wkz2jb$(rule, facts, exception);
    }
  };
  DefaultRulesEngine.prototype.triggerListenersOnSuccess_0 = function (rule, facts) {
    var tmp$;
    tmp$ = this.ruleListeners.iterator();
    while (tmp$.hasNext()) {
      var ruleListener = tmp$.next();
      ruleListener.onSuccess_p4kgl3$(rule, facts);
    }
  };
  DefaultRulesEngine.prototype.triggerListenersBeforeExecute_0 = function (rule, facts) {
    var tmp$;
    tmp$ = this.ruleListeners.iterator();
    while (tmp$.hasNext()) {
      var ruleListener = tmp$.next();
      ruleListener.beforeExecute_p4kgl3$(rule, facts);
    }
  };
  DefaultRulesEngine.prototype.triggerListenersBeforeEvaluate_0 = function (rule, facts) {
    var tmp$;
    tmp$ = this.ruleListeners.iterator();
    while (tmp$.hasNext()) {
      var ruleListener = tmp$.next();
      if (!ruleListener.beforeEvaluate_p4kgl3$(rule, facts)) {
        return false;
      }
    }
    return true;
  };
  DefaultRulesEngine.prototype.triggerListenersAfterEvaluate_0 = function (rule, facts, evaluationResult) {
    var tmp$;
    tmp$ = this.ruleListeners.iterator();
    while (tmp$.hasNext()) {
      var ruleListener = tmp$.next();
      ruleListener.afterEvaluate_df759s$(rule, facts, evaluationResult);
    }
  };
  DefaultRulesEngine.prototype.triggerListenersBeforeRules_0 = function (rule, facts) {
    var tmp$;
    tmp$ = this.rulesEngineListeners.iterator();
    while (tmp$.hasNext()) {
      var rulesEngineListener = tmp$.next();
      rulesEngineListener.beforeEvaluate_eo0djw$(rule, facts);
    }
  };
  DefaultRulesEngine.prototype.triggerListenersAfterRules_0 = function (rule, facts) {
    var tmp$;
    tmp$ = this.rulesEngineListeners.iterator();
    while (tmp$.hasNext()) {
      var rulesEngineListener = tmp$.next();
      rulesEngineListener.afterExecute_eo0djw$(rule, facts);
    }
  };
  DefaultRulesEngine.prototype.shouldBeEvaluated_0 = function (rule, facts) {
    return this.triggerListenersBeforeEvaluate_0(rule, facts);
  };
  DefaultRulesEngine.prototype.registerRuleListener_6oj9y1$ = function (ruleListener) {
    this.ruleListeners.add_11rb$(ruleListener);
  };
  DefaultRulesEngine.prototype.registerRuleListeners_3vaisc$ = function (ruleListeners) {
    this.ruleListeners.addAll_brywnq$(ruleListeners);
  };
  DefaultRulesEngine.prototype.registerRulesEngineListener_4py9rq$ = function (rulesEngineListener) {
    this.rulesEngineListeners.add_11rb$(rulesEngineListener);
  };
  DefaultRulesEngine.prototype.registerRulesEngineListeners_rs93jv$ = function (rulesEngineListeners) {
    this.rulesEngineListeners.addAll_brywnq$(rulesEngineListeners);
  };
  function DefaultRulesEngine$Companion() {
    DefaultRulesEngine$Companion_instance = this;
    this.LOGGER_0 = mu.KotlinLogging.logger_o14v8n$(DefaultRulesEngine$Companion$LOGGER$lambda);
  }
  function DefaultRulesEngine$Companion$LOGGER$lambda() {
    return Unit;
  }
  DefaultRulesEngine$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var DefaultRulesEngine$Companion_instance = null;
  function DefaultRulesEngine$Companion_getInstance() {
    if (DefaultRulesEngine$Companion_instance === null) {
      new DefaultRulesEngine$Companion();
    }
    return DefaultRulesEngine$Companion_instance;
  }
  DefaultRulesEngine.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DefaultRulesEngine',
    interfaces: [RulesEngine]
  };
  function DefaultRulesEngine_init($this) {
    $this = $this || Object.create(DefaultRulesEngine.prototype);
    DefaultRulesEngine.call($this);
    $this.parameters_kunhqe$_0 = RulesEngineParameters_init();
    $this.ruleListeners_iyzsqp$_0 = ArrayList_init_0();
    $this.ruleListeners.add_11rb$(new DefaultRuleListener());
    $this.rulesEngineListeners_27qc3a$_0 = ArrayList_init_0();
    $this.rulesEngineListeners.add_11rb$(new DefaultRulesEngineListener($this.parameters));
    return $this;
  }
  function DefaultRulesEngine_init_0(parameters, $this) {
    $this = $this || Object.create(DefaultRulesEngine.prototype);
    DefaultRulesEngine.call($this);
    $this.parameters_kunhqe$_0 = parameters;
    $this.ruleListeners_iyzsqp$_0 = ArrayList_init_0();
    $this.ruleListeners.add_11rb$(new DefaultRuleListener());
    $this.rulesEngineListeners_27qc3a$_0 = ArrayList_init_0();
    $this.rulesEngineListeners.add_11rb$(new DefaultRulesEngineListener(parameters));
    return $this;
  }
  function DefaultRulesEngineListener(parameters) {
    DefaultRulesEngineListener$Companion_getInstance();
    this.parameters_0 = parameters;
  }
  function DefaultRulesEngineListener$beforeEvaluate$lambda() {
    return 'Rules evaluation started';
  }
  function DefaultRulesEngineListener$beforeEvaluate$lambda_0() {
    return 'No rules registered! Nothing to apply';
  }
  DefaultRulesEngineListener.prototype.beforeEvaluate_eo0djw$ = function (rules, facts) {
    if (!rules.isEmpty) {
      this.logEngineParameters_0();
      this.log_0(rules);
      this.log_1(facts);
      DefaultRulesEngineListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngineListener$beforeEvaluate$lambda);
    }
     else {
      DefaultRulesEngineListener$Companion_getInstance().LOGGER_0.warn_nq59yw$(DefaultRulesEngineListener$beforeEvaluate$lambda_0);
    }
  };
  DefaultRulesEngineListener.prototype.afterExecute_eo0djw$ = function (rules, facts) {
  };
  function DefaultRulesEngineListener$logEngineParameters$lambda(this$DefaultRulesEngineListener) {
    return function () {
      return this$DefaultRulesEngineListener.parameters_0.toString();
    };
  }
  DefaultRulesEngineListener.prototype.logEngineParameters_0 = function () {
    DefaultRulesEngineListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngineListener$logEngineParameters$lambda(this));
  };
  function DefaultRulesEngineListener$log$lambda() {
    return 'Registered rules:';
  }
  function DefaultRulesEngineListener$log$lambda_0(closure$rule) {
    return function () {
      return "Rule { name = '" + closure$rule.name + "', description = '" + closure$rule.description + "', priority = '{" + closure$rule + ".priority}'}";
    };
  }
  DefaultRulesEngineListener.prototype.log_0 = function (rules) {
    var tmp$;
    DefaultRulesEngineListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngineListener$log$lambda);
    tmp$ = rules.iterator();
    while (tmp$.hasNext()) {
      var rule = tmp$.next();
      DefaultRulesEngineListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngineListener$log$lambda_0(rule));
    }
  };
  function DefaultRulesEngineListener$log$lambda_1() {
    return 'Known facts:';
  }
  function DefaultRulesEngineListener$log$lambda_2(closure$fact) {
    return function () {
      return 'Fact { ' + closure$fact.key + ' : {' + closure$fact + '.value} }';
    };
  }
  DefaultRulesEngineListener.prototype.log_1 = function (facts) {
    var tmp$;
    DefaultRulesEngineListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngineListener$log$lambda_1);
    tmp$ = facts.iterator();
    while (tmp$.hasNext()) {
      var fact = tmp$.next();
      DefaultRulesEngineListener$Companion_getInstance().LOGGER_0.info_nq59yw$(DefaultRulesEngineListener$log$lambda_2(fact));
    }
  };
  function DefaultRulesEngineListener$Companion() {
    DefaultRulesEngineListener$Companion_instance = this;
    this.LOGGER_0 = mu.KotlinLogging.logger_o14v8n$(DefaultRulesEngineListener$Companion$LOGGER$lambda);
  }
  function DefaultRulesEngineListener$Companion$LOGGER$lambda() {
    return Unit;
  }
  DefaultRulesEngineListener$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var DefaultRulesEngineListener$Companion_instance = null;
  function DefaultRulesEngineListener$Companion_getInstance() {
    if (DefaultRulesEngineListener$Companion_instance === null) {
      new DefaultRulesEngineListener$Companion();
    }
    return DefaultRulesEngineListener$Companion_instance;
  }
  DefaultRulesEngineListener.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DefaultRulesEngineListener',
    interfaces: [RulesEngineListener]
  };
  function InferenceRulesEngine(parameters) {
    InferenceRulesEngine$Companion_getInstance();
    if (parameters === void 0)
      parameters = RulesEngineParameters_init();
    this.parameters_kvcvxq$_0 = parameters;
    this.ruleListeners_ygsbh5$_0 = null;
    this.rulesEngineListeners_ni64y6$_0 = null;
    this.delegate_0 = null;
    this.delegate_0 = DefaultRulesEngine_init_0(this.parameters);
    this.ruleListeners_ygsbh5$_0 = ArrayList_init_0();
    this.rulesEngineListeners_ni64y6$_0 = ArrayList_init_0();
  }
  Object.defineProperty(InferenceRulesEngine.prototype, 'parameters', {
    get: function () {
      return this.parameters_kvcvxq$_0;
    }
  });
  Object.defineProperty(InferenceRulesEngine.prototype, 'ruleListeners', {
    get: function () {
      return this.ruleListeners_ygsbh5$_0;
    }
  });
  Object.defineProperty(InferenceRulesEngine.prototype, 'rulesEngineListeners', {
    get: function () {
      return this.rulesEngineListeners_ni64y6$_0;
    }
  });
  function InferenceRulesEngine$fire$lambda(closure$facts) {
    return function () {
      return 'Selecting candidate rules based on the following facts: ' + closure$facts;
    };
  }
  function InferenceRulesEngine$fire$lambda_0(closure$facts) {
    return function () {
      return 'No candidate rules found for facts: ' + closure$facts;
    };
  }
  InferenceRulesEngine.prototype.fire_eo0djw$ = function (rules, facts) {
    var selectedRules;
    do {
      InferenceRulesEngine$Companion_getInstance().LOGGER_0.info_nq59yw$(InferenceRulesEngine$fire$lambda(facts));
      selectedRules = this.selectCandidates_0(rules, facts);
      if (!selectedRules.isEmpty()) {
        this.delegate_0.doFire_k2hesn$(Rules_init(selectedRules), facts);
      }
       else {
        InferenceRulesEngine$Companion_getInstance().LOGGER_0.info_nq59yw$(InferenceRulesEngine$fire$lambda_0(facts));
      }
    }
     while (!selectedRules.isEmpty());
  };
  InferenceRulesEngine.prototype.selectCandidates_0 = function (rules, facts) {
    var tmp$;
    var candidates = LinkedHashSet_init();
    tmp$ = rules.iterator();
    while (tmp$.hasNext()) {
      var rule = tmp$.next();
      if (rule.evaluate_qykua8$(facts)) {
        candidates.add_11rb$(rule);
      }
    }
    return toSet(sorted(candidates));
  };
  InferenceRulesEngine.prototype.check_eo0djw$ = function (rules, facts) {
    return this.delegate_0.check_eo0djw$(rules, facts);
  };
  InferenceRulesEngine.prototype.registerRuleListener_6oj9y1$ = function (ruleListener) {
    this.ruleListeners.add_11rb$(ruleListener);
    this.delegate_0.registerRuleListener_6oj9y1$(ruleListener);
  };
  InferenceRulesEngine.prototype.registerRuleListeners_3vaisc$ = function (ruleListeners) {
    this.ruleListeners.addAll_brywnq$(ruleListeners);
    this.delegate_0.registerRuleListeners_3vaisc$(ruleListeners);
  };
  InferenceRulesEngine.prototype.registerRulesEngineListener_4py9rq$ = function (rulesEngineListener) {
    this.rulesEngineListeners.add_11rb$(rulesEngineListener);
    this.delegate_0.registerRulesEngineListener_4py9rq$(rulesEngineListener);
  };
  InferenceRulesEngine.prototype.registerRulesEngineListeners_rs93jv$ = function (rulesEngineListeners) {
    this.rulesEngineListeners.addAll_brywnq$(rulesEngineListeners);
    this.delegate_0.registerRulesEngineListeners_rs93jv$(rulesEngineListeners);
  };
  function InferenceRulesEngine$Companion() {
    InferenceRulesEngine$Companion_instance = this;
    this.LOGGER_0 = mu.KotlinLogging.logger_o14v8n$(InferenceRulesEngine$Companion$LOGGER$lambda);
  }
  function InferenceRulesEngine$Companion$LOGGER$lambda() {
    return Unit;
  }
  InferenceRulesEngine$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var InferenceRulesEngine$Companion_instance = null;
  function InferenceRulesEngine$Companion_getInstance() {
    if (InferenceRulesEngine$Companion_instance === null) {
      new InferenceRulesEngine$Companion();
    }
    return InferenceRulesEngine$Companion_instance;
  }
  InferenceRulesEngine.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'InferenceRulesEngine',
    interfaces: [RulesEngine]
  };
  function RuleBuilder() {
    this.name_0 = Rule$Companion_getInstance().DEFAULT_NAME;
    this.description_0 = Rule$Companion_getInstance().DEFAULT_DESCRIPTION;
    this.priority_0 = Rule$Companion_getInstance().DEFAULT_PRIORITY;
    this.condition_0 = Condition$Companion_getInstance().FALSE;
    this.actions_0 = ArrayList_init_0();
  }
  RuleBuilder.prototype.name_61zpoe$ = function (name) {
    this.name_0 = name;
    return this;
  };
  RuleBuilder.prototype.description_61zpoe$ = function (description) {
    this.description_0 = description;
    return this;
  };
  RuleBuilder.prototype.priority_za3lpa$ = function (priority) {
    this.priority_0 = priority;
    return this;
  };
  RuleBuilder.prototype.when_dkfa78$ = function (condition) {
    this.condition_0 = condition;
    return this;
  };
  RuleBuilder.prototype.then_ix8h2r$ = function (action) {
    this.actions_0.add_11rb$(action);
    return this;
  };
  RuleBuilder.prototype.build = function () {
    return new DefaultRule(this.name_0, this.description_0, this.priority_0, this.condition_0, this.actions_0);
  };
  RuleBuilder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'RuleBuilder',
    interfaces: []
  };
  function RulesEngineParameters() {
    RulesEngineParameters$Companion_getInstance();
    this.isSkipOnFirstAppliedRule = false;
    this.isSkipOnFirstNonTriggeredRule = false;
    this.isSkipOnFirstFailedRule = false;
    this.priorityThreshold = 0;
  }
  RulesEngineParameters.prototype.priorityThreshold_za3lpa$ = function (priorityThreshold) {
    this.priorityThreshold = priorityThreshold;
    return this;
  };
  RulesEngineParameters.prototype.skipOnFirstAppliedRule_6taknv$ = function (skipOnFirstAppliedRule) {
    this.isSkipOnFirstAppliedRule = skipOnFirstAppliedRule;
    return this;
  };
  RulesEngineParameters.prototype.skipOnFirstNonTriggeredRule_6taknv$ = function (skipOnFirstNonTriggeredRule) {
    this.isSkipOnFirstNonTriggeredRule = skipOnFirstNonTriggeredRule;
    return this;
  };
  RulesEngineParameters.prototype.skipOnFirstFailedRule_6taknv$ = function (skipOnFirstFailedRule) {
    this.isSkipOnFirstFailedRule = skipOnFirstFailedRule;
    return this;
  };
  RulesEngineParameters.prototype.toString = function () {
    return 'Engine parameters { ' + 'skipOnFirstAppliedRule = ' + toString(this.isSkipOnFirstAppliedRule) + ', skipOnFirstNonTriggeredRule = ' + toString(this.isSkipOnFirstNonTriggeredRule) + ', skipOnFirstFailedRule = ' + toString(this.isSkipOnFirstFailedRule) + ', priorityThreshold = ' + toString(this.priorityThreshold) + ' }';
  };
  function RulesEngineParameters$Companion() {
    RulesEngineParameters$Companion_instance = this;
    this.DEFAULT_RULE_PRIORITY_THRESHOLD = 2147483647;
  }
  RulesEngineParameters$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var RulesEngineParameters$Companion_instance = null;
  function RulesEngineParameters$Companion_getInstance() {
    if (RulesEngineParameters$Companion_instance === null) {
      new RulesEngineParameters$Companion();
    }
    return RulesEngineParameters$Companion_instance;
  }
  RulesEngineParameters.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'RulesEngineParameters',
    interfaces: []
  };
  function RulesEngineParameters_init($this) {
    $this = $this || Object.create(RulesEngineParameters.prototype);
    RulesEngineParameters.call($this);
    $this.priorityThreshold = RulesEngineParameters$Companion_getInstance().DEFAULT_RULE_PRIORITY_THRESHOLD;
    return $this;
  }
  function RulesEngineParameters_init_0(skipOnFirstAppliedRule, skipOnFirstFailedRule, skipOnFirstNonTriggeredRule, priorityThreshold, $this) {
    $this = $this || Object.create(RulesEngineParameters.prototype);
    RulesEngineParameters.call($this);
    $this.isSkipOnFirstAppliedRule = skipOnFirstAppliedRule;
    $this.isSkipOnFirstFailedRule = skipOnFirstFailedRule;
    $this.isSkipOnFirstNonTriggeredRule = skipOnFirstNonTriggeredRule;
    $this.priorityThreshold = priorityThreshold;
    return $this;
  }
  var package$org = _.org || (_.org = {});
  var package$jeasy = package$org.jeasy || (package$org.jeasy = {});
  var package$rules = package$jeasy.rules || (package$jeasy.rules = {});
  var package$api = package$rules.api || (package$rules.api = {});
  package$api.Action = Action;
  Object.defineProperty(Condition, 'Companion', {
    get: Condition$Companion_getInstance
  });
  package$api.Condition = Condition;
  package$api.Facts = Facts;
  Object.defineProperty(Rule, 'Companion', {
    get: Rule$Companion_getInstance
  });
  package$api.Rule = Rule;
  package$api.RuleListener = RuleListener;
  package$api.Rules_init_g9zim8$ = Rules_init;
  package$api.Rules_init = Rules_init_0;
  package$api.Rules = Rules;
  package$api.RulesEngine = RulesEngine;
  package$api.RulesEngineListener = RulesEngineListener;
  var package$core = package$rules.core || (package$rules.core = {});
  package$core.BasicRule = BasicRule;
  package$core.DefaultRule = DefaultRule;
  Object.defineProperty(DefaultRuleListener, 'Companion', {
    get: DefaultRuleListener$Companion_getInstance
  });
  package$core.DefaultRuleListener = DefaultRuleListener;
  Object.defineProperty(DefaultRulesEngine, 'Companion', {
    get: DefaultRulesEngine$Companion_getInstance
  });
  package$core.DefaultRulesEngine_init = DefaultRulesEngine_init;
  package$core.DefaultRulesEngine_init_3wjk2x$ = DefaultRulesEngine_init_0;
  package$core.DefaultRulesEngine = DefaultRulesEngine;
  Object.defineProperty(DefaultRulesEngineListener, 'Companion', {
    get: DefaultRulesEngineListener$Companion_getInstance
  });
  package$core.DefaultRulesEngineListener = DefaultRulesEngineListener;
  Object.defineProperty(InferenceRulesEngine, 'Companion', {
    get: InferenceRulesEngine$Companion_getInstance
  });
  package$core.InferenceRulesEngine = InferenceRulesEngine;
  package$core.RuleBuilder = RuleBuilder;
  Object.defineProperty(RulesEngineParameters, 'Companion', {
    get: RulesEngineParameters$Companion_getInstance
  });
  package$core.RulesEngineParameters_init = RulesEngineParameters_init;
  package$core.RulesEngineParameters_init_4cvozx$ = RulesEngineParameters_init_0;
  package$core.RulesEngineParameters = RulesEngineParameters;
  Kotlin.defineModule('easy-rules-core', _);
  return _;
}));
