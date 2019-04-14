(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'kotlin-logging'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'kotlin-logging'.");
    }
    root['kotlin-logging'] = factory(typeof this['kotlin-logging'] === 'undefined' ? {} : this['kotlin-logging'], kotlin);
  }
}(this, function (_, Kotlin) {
  'use strict';
  var $$importsForInline$$ = _.$$importsForInline$$ || (_.$$importsForInline$$ = {});
  var defineInlineFunction = Kotlin.defineInlineFunction;
  var wrapFunction = Kotlin.wrapFunction;
  var Kind_INTERFACE = Kotlin.Kind.INTERFACE;
  var Kind_OBJECT = Kotlin.Kind.OBJECT;
  var get_js = Kotlin.kotlin.js.get_js_1yb8b7$;
  var Enum = Kotlin.kotlin.Enum;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var throwISE = Kotlin.throwISE;
  var toString = Kotlin.toString;
  var equals = Kotlin.equals;
  KotlinLoggingLevel.prototype = Object.create(Enum.prototype);
  KotlinLoggingLevel.prototype.constructor = KotlinLoggingLevel;
  var toStringSafe = defineInlineFunction('kotlin-logging.mu.internal.toStringSafe_qhgloa$', wrapFunction(function () {
    var toString = Kotlin.toString;
    var Exception = Kotlin.kotlin.Exception;
    return function ($receiver) {
      var tmp$;
      try {
        tmp$ = toString($receiver());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$ = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      return tmp$;
    };
  }));
  function KLogger() {
  }
  KLogger.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'KLogger',
    interfaces: []
  };
  function KMarkerFactory() {
    KMarkerFactory_instance = this;
  }
  KMarkerFactory.prototype.getMarker_61zpoe$ = function (name) {
    return new MarkerJS(name);
  };
  KMarkerFactory.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'KMarkerFactory',
    interfaces: []
  };
  var KMarkerFactory_instance = null;
  function KMarkerFactory_getInstance() {
    if (KMarkerFactory_instance === null) {
      new KMarkerFactory();
    }
    return KMarkerFactory_instance;
  }
  function KotlinLogging() {
    KotlinLogging_instance = this;
  }
  KotlinLogging.prototype.logger_o14v8n$ = function (func) {
    return new KLoggerJS(get_js(Kotlin.getKClassFromExpression(func)).name);
  };
  KotlinLogging.prototype.logger_61zpoe$ = function (name) {
    return new KLoggerJS(name);
  };
  KotlinLogging.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'KotlinLogging',
    interfaces: []
  };
  var KotlinLogging_instance = null;
  function KotlinLogging_getInstance() {
    if (KotlinLogging_instance === null) {
      new KotlinLogging();
    }
    return KotlinLogging_instance;
  }
  var LOG_LEVEL;
  function KotlinLoggingLevel(name, ordinal) {
    Enum.call(this);
    this.name$ = name;
    this.ordinal$ = ordinal;
  }
  function KotlinLoggingLevel_initFields() {
    KotlinLoggingLevel_initFields = function () {
    };
    KotlinLoggingLevel$TRACE_instance = new KotlinLoggingLevel('TRACE', 0);
    KotlinLoggingLevel$DEBUG_instance = new KotlinLoggingLevel('DEBUG', 1);
    KotlinLoggingLevel$INFO_instance = new KotlinLoggingLevel('INFO', 2);
    KotlinLoggingLevel$WARN_instance = new KotlinLoggingLevel('WARN', 3);
    KotlinLoggingLevel$ERROR_instance = new KotlinLoggingLevel('ERROR', 4);
  }
  var KotlinLoggingLevel$TRACE_instance;
  function KotlinLoggingLevel$TRACE_getInstance() {
    KotlinLoggingLevel_initFields();
    return KotlinLoggingLevel$TRACE_instance;
  }
  var KotlinLoggingLevel$DEBUG_instance;
  function KotlinLoggingLevel$DEBUG_getInstance() {
    KotlinLoggingLevel_initFields();
    return KotlinLoggingLevel$DEBUG_instance;
  }
  var KotlinLoggingLevel$INFO_instance;
  function KotlinLoggingLevel$INFO_getInstance() {
    KotlinLoggingLevel_initFields();
    return KotlinLoggingLevel$INFO_instance;
  }
  var KotlinLoggingLevel$WARN_instance;
  function KotlinLoggingLevel$WARN_getInstance() {
    KotlinLoggingLevel_initFields();
    return KotlinLoggingLevel$WARN_instance;
  }
  var KotlinLoggingLevel$ERROR_instance;
  function KotlinLoggingLevel$ERROR_getInstance() {
    KotlinLoggingLevel_initFields();
    return KotlinLoggingLevel$ERROR_instance;
  }
  KotlinLoggingLevel.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'KotlinLoggingLevel',
    interfaces: [Enum]
  };
  function KotlinLoggingLevel$values() {
    return [KotlinLoggingLevel$TRACE_getInstance(), KotlinLoggingLevel$DEBUG_getInstance(), KotlinLoggingLevel$INFO_getInstance(), KotlinLoggingLevel$WARN_getInstance(), KotlinLoggingLevel$ERROR_getInstance()];
  }
  KotlinLoggingLevel.values = KotlinLoggingLevel$values;
  function KotlinLoggingLevel$valueOf(name) {
    switch (name) {
      case 'TRACE':
        return KotlinLoggingLevel$TRACE_getInstance();
      case 'DEBUG':
        return KotlinLoggingLevel$DEBUG_getInstance();
      case 'INFO':
        return KotlinLoggingLevel$INFO_getInstance();
      case 'WARN':
        return KotlinLoggingLevel$WARN_getInstance();
      case 'ERROR':
        return KotlinLoggingLevel$ERROR_getInstance();
      default:throwISE('No enum constant mu.KotlinLoggingLevel.' + name);
    }
  }
  KotlinLoggingLevel.valueOf_61zpoe$ = KotlinLoggingLevel$valueOf;
  function isLoggingEnabled($receiver) {
    return $receiver.ordinal >= LOG_LEVEL.ordinal;
  }
  function Marker() {
  }
  Marker.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'Marker',
    interfaces: []
  };
  function KLoggerJS(loggerName) {
    this.loggerName_0 = loggerName;
  }
  var Exception = Kotlin.kotlin.Exception;
  KLoggerJS.prototype.trace_nq59yw$ = function (msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$TRACE_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'TRACE: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.log(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.debug_nq59yw$ = function (msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$DEBUG_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'DEBUG: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.log(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.info_nq59yw$ = function (msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$INFO_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'INFO: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.info(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.warn_nq59yw$ = function (msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$WARN_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'WARN: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.warn(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.error_nq59yw$ = function (msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$ERROR_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'ERROR: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.error(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.trace_ca4k3s$ = function (t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$TRACE_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'TRACE: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.log(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.debug_ca4k3s$ = function (t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$DEBUG_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'DEBUG: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.log(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.info_ca4k3s$ = function (t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$INFO_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'INFO: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.info(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.warn_ca4k3s$ = function (t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$WARN_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'WARN: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.warn(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.error_ca4k3s$ = function (t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$ERROR_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'ERROR: [' + this.loggerName_0 + '] ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.error(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.trace_8jakm3$ = function (marker, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$TRACE_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'TRACE: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.log(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.debug_8jakm3$ = function (marker, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$DEBUG_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'DEBUG: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.log(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.info_8jakm3$ = function (marker, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$INFO_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'INFO: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.info(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.warn_8jakm3$ = function (marker, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$WARN_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'WARN: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.warn(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.error_8jakm3$ = function (marker, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$ERROR_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'ERROR: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.error(tmp$_0 + tmp$_1);
    }
  };
  KLoggerJS.prototype.trace_o4svvp$ = function (marker, t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$TRACE_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'TRACE: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.log(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.debug_o4svvp$ = function (marker, t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$DEBUG_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'DEBUG: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.log(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.info_o4svvp$ = function (marker, t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$INFO_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'INFO: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.info(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.warn_o4svvp$ = function (marker, t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$WARN_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'WARN: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.warn(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.error_o4svvp$ = function (marker, t, msg) {
    if (isLoggingEnabled(KotlinLoggingLevel$ERROR_getInstance())) {
      var tmp$ = console;
      var tmp$_0 = 'ERROR: [' + this.loggerName_0 + '] ' + toString(marker != null ? marker.getName() : null) + ' ';
      var tmp$_1;
      try {
        tmp$_1 = toString(msg());
      }
       catch (e) {
        if (Kotlin.isType(e, Exception)) {
          tmp$_1 = 'Log message invocation failed: ' + e;
        }
         else
          throw e;
      }
      tmp$.error(tmp$_0 + tmp$_1 + this.throwableToString_0(t));
    }
  };
  KLoggerJS.prototype.throwableToString_0 = function ($receiver) {
    if ($receiver == null) {
      return '';
    }
    var msg = '';
    var current = $receiver;
    while (current != null && !equals(current.cause, current)) {
      msg += ", Caused by: '" + toString(current.message) + "'";
      current = current.cause;
    }
    return msg;
  };
  KLoggerJS.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'KLoggerJS',
    interfaces: [KLogger]
  };
  function MarkerJS(name) {
    this.name_0 = name;
  }
  MarkerJS.prototype.getName = function () {
    return this.name_0;
  };
  MarkerJS.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'MarkerJS',
    interfaces: [Marker]
  };
  var package$mu = _.mu || (_.mu = {});
  var package$internal = package$mu.internal || (package$mu.internal = {});
  package$internal.toStringSafe_qhgloa$ = toStringSafe;
  package$mu.KLogger = KLogger;
  Object.defineProperty(package$mu, 'KMarkerFactory', {
    get: KMarkerFactory_getInstance
  });
  Object.defineProperty(package$mu, 'KotlinLogging', {
    get: KotlinLogging_getInstance
  });
  Object.defineProperty(package$mu, 'LOG_LEVEL', {
    get: function () {
      return LOG_LEVEL;
    },
    set: function (value) {
      LOG_LEVEL = value;
    }
  });
  Object.defineProperty(KotlinLoggingLevel, 'TRACE', {
    get: KotlinLoggingLevel$TRACE_getInstance
  });
  Object.defineProperty(KotlinLoggingLevel, 'DEBUG', {
    get: KotlinLoggingLevel$DEBUG_getInstance
  });
  Object.defineProperty(KotlinLoggingLevel, 'INFO', {
    get: KotlinLoggingLevel$INFO_getInstance
  });
  Object.defineProperty(KotlinLoggingLevel, 'WARN', {
    get: KotlinLoggingLevel$WARN_getInstance
  });
  Object.defineProperty(KotlinLoggingLevel, 'ERROR', {
    get: KotlinLoggingLevel$ERROR_getInstance
  });
  package$mu.KotlinLoggingLevel = KotlinLoggingLevel;
  package$mu.isLoggingEnabled_pm19j7$ = isLoggingEnabled;
  package$mu.Marker = Marker;
  $$importsForInline$$['kotlin-logging'] = _;
  package$internal.KLoggerJS = KLoggerJS;
  package$internal.MarkerJS = MarkerJS;
  LOG_LEVEL = KotlinLoggingLevel$INFO_getInstance();
  Kotlin.defineModule('kotlin-logging', _);
  return _;
}));

//# sourceMappingURL=kotlin-logging.js.map
