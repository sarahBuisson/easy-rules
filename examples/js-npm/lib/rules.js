(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin', 'easy-rules-kotlin-easy-rules-core', 'kotlinx-serialization-kotlinx-serialization-runtime'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'), require('easy-rules-kotlin-easy-rules-core'), require('kotlinx-serialization-kotlinx-serialization-runtime'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'easy-rules-kotlin-example-gradle'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'easy-rules-kotlin-example-gradle'.");
    }
    if (typeof this['easy-rules-kotlin-easy-rules-core'] === 'undefined') {
      throw new Error("Error loading module 'easy-rules-kotlin-example-gradle'. Its dependency 'easy-rules-kotlin-easy-rules-core' was not found. Please, check whether 'easy-rules-kotlin-easy-rules-core' is loaded prior to 'easy-rules-kotlin-example-gradle'.");
    }
    if (typeof this['kotlinx-serialization-kotlinx-serialization-runtime'] === 'undefined') {
      throw new Error("Error loading module 'easy-rules-kotlin-example-gradle'. Its dependency 'kotlinx-serialization-kotlinx-serialization-runtime' was not found. Please, check whether 'kotlinx-serialization-kotlinx-serialization-runtime' is loaded prior to 'easy-rules-kotlin-example-gradle'.");
    }
    root['easy-rules-kotlin-example-gradle'] = factory(typeof this['easy-rules-kotlin-example-gradle'] === 'undefined' ? {} : this['easy-rules-kotlin-example-gradle'], kotlin, this['easy-rules-kotlin-easy-rules-core'], this['kotlinx-serialization-kotlinx-serialization-runtime']);
  }
}(this, function (_, Kotlin, $module$easy_rules_kotlin_easy_rules_core, $module$kotlinx_serialization_kotlinx_serialization_runtime) {
  'use strict';
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var throwCCE = Kotlin.throwCCE;
  var Kind_OBJECT = Kotlin.Kind.OBJECT;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var Action = $module$easy_rules_kotlin_easy_rules_core.org.jeasy.rules.api.Action;
  var ensureNotNull = Kotlin.ensureNotNull;
  var Condition = $module$easy_rules_kotlin_easy_rules_core.org.jeasy.rules.api.Condition;
  var FactsMap_init = $module$easy_rules_kotlin_easy_rules_core.org.jeasy.rules.api.FactsMap_init_t8rdyi$;
  var RuleBuilder = $module$easy_rules_kotlin_easy_rules_core.org.jeasy.rules.core.RuleBuilder;
  var Rules_init = $module$easy_rules_kotlin_easy_rules_core.org.jeasy.rules.api.Rules_init_287e2$;
  var InferenceRulesEngine = $module$easy_rules_kotlin_easy_rules_core.org.jeasy.rules.core.InferenceRulesEngine;
  var SerialClassDescImpl = $module$kotlinx_serialization_kotlinx_serialization_runtime.kotlinx.serialization.internal.SerialClassDescImpl;
  var UnknownFieldException = $module$kotlinx_serialization_kotlinx_serialization_runtime.kotlinx.serialization.UnknownFieldException;
  var internal = $module$kotlinx_serialization_kotlinx_serialization_runtime.kotlinx.serialization.internal;
  var GeneratedSerializer = $module$kotlinx_serialization_kotlinx_serialization_runtime.kotlinx.serialization.internal.GeneratedSerializer;
  var MissingFieldException = $module$kotlinx_serialization_kotlinx_serialization_runtime.kotlinx.serialization.MissingFieldException;
  var equals = Kotlin.equals;
  var getKClass = Kotlin.getKClass;
  var ReferenceArraySerializer = $module$kotlinx_serialization_kotlinx_serialization_runtime.kotlinx.serialization.internal.ReferenceArraySerializer;
  var throwUPAE = Kotlin.throwUPAE;
  var contains = Kotlin.kotlin.collections.contains_mjy6jw$;
  var setOf = Kotlin.kotlin.collections.setOf_i5x0yv$;
  var Rules_init_0 = $module$easy_rules_kotlin_easy_rules_core.org.jeasy.rules.api.Rules_init_igtdxu$;
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  var copyToArray = Kotlin.kotlin.collections.copyToArray;
  var Enum = Kotlin.kotlin.Enum;
  var throwISE = Kotlin.throwISE;
  var contains_0 = Kotlin.kotlin.collections.contains_2ws7j4$;
  var MutableCollection = Kotlin.kotlin.collections.MutableCollection;
  Player_0.prototype = Object.create(Vivant.prototype);
  Player_0.prototype.constructor = Player_0;
  Nouriture.prototype = Object.create(Item_0.prototype);
  Nouriture.prototype.constructor = Nouriture;
  Eau.prototype = Object.create(Item_0.prototype);
  Eau.prototype.constructor = Eau;
  Epee.prototype = Object.create(Item_0.prototype);
  Epee.prototype.constructor = Epee;
  ActionEnum.prototype = Object.create(Enum.prototype);
  ActionEnum.prototype.constructor = ActionEnum;
  FactEnum.prototype = Object.create(Enum.prototype);
  FactEnum.prototype.constructor = FactEnum;
  function DecreaseTemperatureAction() {
    DecreaseTemperatureAction$Companion_getInstance();
  }
  DecreaseTemperatureAction.prototype.execute_11rb$ = function (facts) {
    var tmp$;
    println('It is hot! cooling air..');
    var temperature = typeof (tmp$ = facts.get_ytbaoo$('temperature')) === 'number' ? tmp$ : throwCCE();
    facts.put_4w9ihe$('temperature', temperature - 1 | 0);
  };
  function DecreaseTemperatureAction$Companion() {
    DecreaseTemperatureAction$Companion_instance = this;
  }
  DecreaseTemperatureAction$Companion.prototype.decreaseTemperature_8be2vx$ = function () {
    return new DecreaseTemperatureAction();
  };
  DecreaseTemperatureAction$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var DecreaseTemperatureAction$Companion_instance = null;
  function DecreaseTemperatureAction$Companion_getInstance() {
    if (DecreaseTemperatureAction$Companion_instance === null) {
      new DecreaseTemperatureAction$Companion();
    }
    return DecreaseTemperatureAction$Companion_instance;
  }
  DecreaseTemperatureAction.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DecreaseTemperatureAction',
    interfaces: [Action]
  };
  function HighTemperatureCondition() {
    HighTemperatureCondition$Companion_getInstance();
  }
  HighTemperatureCondition.prototype.evaluate_11rb$ = function (facts) {
    var tmp$;
    var temperature = (tmp$ = facts.get_ytbaoo$('temperature')) == null || typeof tmp$ === 'number' ? tmp$ : throwCCE();
    return ensureNotNull(temperature) > 25;
  };
  function HighTemperatureCondition$Companion() {
    HighTemperatureCondition$Companion_instance = this;
  }
  HighTemperatureCondition$Companion.prototype.itIsHot_8be2vx$ = function () {
    return new HighTemperatureCondition();
  };
  HighTemperatureCondition$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var HighTemperatureCondition$Companion_instance = null;
  function HighTemperatureCondition$Companion_getInstance() {
    if (HighTemperatureCondition$Companion_instance === null) {
      new HighTemperatureCondition$Companion();
    }
    return HighTemperatureCondition$Companion_instance;
  }
  HighTemperatureCondition.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'HighTemperatureCondition',
    interfaces: [Condition]
  };
  function Launcher() {
  }
  Launcher.prototype.exec_kand9s$ = function (args) {
    var facts = FactsMap_init();
    facts.put_4w9ihe$('temperature', 30);
    var airConditioningRule = (new RuleBuilder()).name_61zpoe$('air conditioning rule').when_ieqdlb$(HighTemperatureCondition$Companion_getInstance().itIsHot_8be2vx$()).then_ptknqy$(DecreaseTemperatureAction$Companion_getInstance().decreaseTemperature_8be2vx$()).build();
    var rules = Rules_init();
    rules.register_7crgn4$(airConditioningRule);
    var rulesEngine = new InferenceRulesEngine();
    rulesEngine.fire_qkxksy$(rules, facts);
    return facts;
  };
  Launcher.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Launcher',
    interfaces: []
  };
  function ItemType(name) {
    ItemType$Companion_getInstance();
    this.name = name;
  }
  function ItemType$Companion() {
    ItemType$Companion_instance = this;
  }
  ItemType$Companion.prototype.serializer = function () {
    return ItemType$$serializer_getInstance();
  };
  ItemType$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var ItemType$Companion_instance = null;
  function ItemType$Companion_getInstance() {
    if (ItemType$Companion_instance === null) {
      new ItemType$Companion();
    }
    return ItemType$Companion_instance;
  }
  function ItemType$$serializer() {
    this.descriptor_bhteil$_0 = new SerialClassDescImpl('spaceMarket.model.ItemType', this);
    this.descriptor.addElement_ivxn3r$('name', false);
    ItemType$$serializer_instance = this;
  }
  Object.defineProperty(ItemType$$serializer.prototype, 'descriptor', {
    get: function () {
      return this.descriptor_bhteil$_0;
    }
  });
  ItemType$$serializer.prototype.serialize_awe97i$ = function (encoder, obj) {
    var output = encoder.beginStructure_r0sa6z$(this.descriptor, []);
    output.encodeStringElement_bgm7zs$(this.descriptor, 0, obj.name);
    output.endStructure_qatsm0$(this.descriptor);
  };
  ItemType$$serializer.prototype.deserialize_nts5qn$ = function (decoder) {
    var index, readAll = false;
    var bitMask0 = 0;
    var local0;
    var input = decoder.beginStructure_r0sa6z$(this.descriptor, []);
    loopLabel: while (true) {
      index = input.decodeElementIndex_qatsm0$(this.descriptor);
      switch (index) {
        case -2:
          readAll = true;
        case 0:
          local0 = input.decodeStringElement_3zr2iy$(this.descriptor, 0);
          bitMask0 |= 1;
          if (!readAll)
            break;
        case -1:
          break loopLabel;
        default:throw new UnknownFieldException(index);
      }
    }
    input.endStructure_qatsm0$(this.descriptor);
    return ItemType_init(bitMask0, local0, null);
  };
  ItemType$$serializer.prototype.childSerializers = function () {
    return [internal.StringSerializer];
  };
  ItemType$$serializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: '$serializer',
    interfaces: [GeneratedSerializer]
  };
  var ItemType$$serializer_instance = null;
  function ItemType$$serializer_getInstance() {
    if (ItemType$$serializer_instance === null) {
      new ItemType$$serializer();
    }
    return ItemType$$serializer_instance;
  }
  function ItemType_init(seen1, name, serializationConstructorMarker) {
    var $this = serializationConstructorMarker || Object.create(ItemType.prototype);
    if ((seen1 & 1) === 0)
      throw new MissingFieldException('name');
    else
      $this.name = name;
    return $this;
  }
  ItemType.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'ItemType',
    interfaces: []
  };
  ItemType.prototype.component1 = function () {
    return this.name;
  };
  ItemType.prototype.copy_61zpoe$ = function (name) {
    return new ItemType(name === void 0 ? this.name : name);
  };
  ItemType.prototype.toString = function () {
    return 'ItemType(name=' + Kotlin.toString(this.name) + ')';
  };
  ItemType.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.name) | 0;
    return result;
  };
  ItemType.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && Kotlin.equals(this.name, other.name))));
  };
  function Item(type, quantity) {
    Item$Companion_getInstance();
    if (quantity === void 0)
      quantity = 1;
    this.type = type;
    this.quantity = quantity;
  }
  function Item$Companion() {
    Item$Companion_instance = this;
  }
  Item$Companion.prototype.serializer = function () {
    return Item$$serializer_getInstance();
  };
  Item$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var Item$Companion_instance = null;
  function Item$Companion_getInstance() {
    if (Item$Companion_instance === null) {
      new Item$Companion();
    }
    return Item$Companion_instance;
  }
  function Item$$serializer() {
    this.descriptor_1h75zr$_0 = new SerialClassDescImpl('spaceMarket.model.Item', this);
    this.descriptor.addElement_ivxn3r$('type', false);
    this.descriptor.addElement_ivxn3r$('quantity', true);
    Item$$serializer_instance = this;
  }
  Object.defineProperty(Item$$serializer.prototype, 'descriptor', {
    get: function () {
      return this.descriptor_1h75zr$_0;
    }
  });
  Item$$serializer.prototype.serialize_awe97i$ = function (encoder, obj) {
    var output = encoder.beginStructure_r0sa6z$(this.descriptor, []);
    output.encodeSerializableElement_blecud$(this.descriptor, 0, ItemType$$serializer_getInstance(), obj.type);
    if (!equals(obj.quantity, 1) || output.shouldEncodeElementDefault_3zr2iy$(this.descriptor, 1))
      output.encodeIntElement_4wpqag$(this.descriptor, 1, obj.quantity);
    output.endStructure_qatsm0$(this.descriptor);
  };
  Item$$serializer.prototype.deserialize_nts5qn$ = function (decoder) {
    var index, readAll = false;
    var bitMask0 = 0;
    var local0
        , local1;
    var input = decoder.beginStructure_r0sa6z$(this.descriptor, []);
    loopLabel: while (true) {
      index = input.decodeElementIndex_qatsm0$(this.descriptor);
      switch (index) {
        case -2:
          readAll = true;
        case 0:
          local0 = (bitMask0 & 1) === 0 ? input.decodeSerializableElement_s44l7r$(this.descriptor, 0, ItemType$$serializer_getInstance()) : input.updateSerializableElement_ehubvl$(this.descriptor, 0, ItemType$$serializer_getInstance(), local0);
          bitMask0 |= 1;
          if (!readAll)
            break;
        case 1:
          local1 = input.decodeIntElement_3zr2iy$(this.descriptor, 1);
          bitMask0 |= 2;
          if (!readAll)
            break;
        case -1:
          break loopLabel;
        default:throw new UnknownFieldException(index);
      }
    }
    input.endStructure_qatsm0$(this.descriptor);
    return Item_init(bitMask0, local0, local1, null);
  };
  Item$$serializer.prototype.childSerializers = function () {
    return [ItemType$$serializer_getInstance(), internal.IntSerializer];
  };
  Item$$serializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: '$serializer',
    interfaces: [GeneratedSerializer]
  };
  var Item$$serializer_instance = null;
  function Item$$serializer_getInstance() {
    if (Item$$serializer_instance === null) {
      new Item$$serializer();
    }
    return Item$$serializer_instance;
  }
  function Item_init(seen1, type, quantity, serializationConstructorMarker) {
    var $this = serializationConstructorMarker || Object.create(Item.prototype);
    if ((seen1 & 1) === 0)
      throw new MissingFieldException('type');
    else
      $this.type = type;
    if ((seen1 & 2) === 0)
      $this.quantity = 1;
    else
      $this.quantity = quantity;
    return $this;
  }
  Item.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Item',
    interfaces: []
  };
  Item.prototype.component1 = function () {
    return this.type;
  };
  Item.prototype.component2 = function () {
    return this.quantity;
  };
  Item.prototype.copy_vck73k$ = function (type, quantity) {
    return new Item(type === void 0 ? this.type : type, quantity === void 0 ? this.quantity : quantity);
  };
  Item.prototype.toString = function () {
    return 'Item(type=' + Kotlin.toString(this.type) + (', quantity=' + Kotlin.toString(this.quantity)) + ')';
  };
  Item.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.type) | 0;
    result = result * 31 + Kotlin.hashCode(this.quantity) | 0;
    return result;
  };
  Item.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.type, other.type) && Kotlin.equals(this.quantity, other.quantity)))));
  };
  function Planet(name, inventory, products) {
    Planet$Companion_getInstance();
    if (inventory === void 0)
      inventory = [];
    if (products === void 0)
      products = [];
    this.name = name;
    this.inventory = inventory;
    this.products = products;
  }
  function Planet$Companion() {
    Planet$Companion_instance = this;
  }
  Planet$Companion.prototype.serializer = function () {
    return Planet$$serializer_getInstance();
  };
  Planet$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var Planet$Companion_instance = null;
  function Planet$Companion_getInstance() {
    if (Planet$Companion_instance === null) {
      new Planet$Companion();
    }
    return Planet$Companion_instance;
  }
  function Planet$$serializer() {
    this.descriptor_dw9xw2$_0 = new SerialClassDescImpl('spaceMarket.model.Planet', this);
    this.descriptor.addElement_ivxn3r$('name', false);
    this.descriptor.addElement_ivxn3r$('inventory', true);
    this.descriptor.addElement_ivxn3r$('products', true);
    Planet$$serializer_instance = this;
  }
  Object.defineProperty(Planet$$serializer.prototype, 'descriptor', {
    get: function () {
      return this.descriptor_dw9xw2$_0;
    }
  });
  Planet$$serializer.prototype.serialize_awe97i$ = function (encoder, obj) {
    var output = encoder.beginStructure_r0sa6z$(this.descriptor, []);
    output.encodeStringElement_bgm7zs$(this.descriptor, 0, obj.name);
    if (!equals(obj.inventory, []) || output.shouldEncodeElementDefault_3zr2iy$(this.descriptor, 1))
      output.encodeSerializableElement_blecud$(this.descriptor, 1, new ReferenceArraySerializer(getKClass(Item), Item$$serializer_getInstance()), obj.inventory);
    if (!equals(obj.products, []) || output.shouldEncodeElementDefault_3zr2iy$(this.descriptor, 2))
      output.encodeSerializableElement_blecud$(this.descriptor, 2, new ReferenceArraySerializer(getKClass(ItemType), ItemType$$serializer_getInstance()), obj.products);
    output.endStructure_qatsm0$(this.descriptor);
  };
  Planet$$serializer.prototype.deserialize_nts5qn$ = function (decoder) {
    var index, readAll = false;
    var bitMask0 = 0;
    var local0
        , local1
        , local2;
    var input = decoder.beginStructure_r0sa6z$(this.descriptor, []);
    loopLabel: while (true) {
      index = input.decodeElementIndex_qatsm0$(this.descriptor);
      switch (index) {
        case -2:
          readAll = true;
        case 0:
          local0 = input.decodeStringElement_3zr2iy$(this.descriptor, 0);
          bitMask0 |= 1;
          if (!readAll)
            break;
        case 1:
          local1 = (bitMask0 & 2) === 0 ? input.decodeSerializableElement_s44l7r$(this.descriptor, 1, new ReferenceArraySerializer(getKClass(Item), Item$$serializer_getInstance())) : input.updateSerializableElement_ehubvl$(this.descriptor, 1, new ReferenceArraySerializer(getKClass(Item), Item$$serializer_getInstance()), local1);
          bitMask0 |= 2;
          if (!readAll)
            break;
        case 2:
          local2 = (bitMask0 & 4) === 0 ? input.decodeSerializableElement_s44l7r$(this.descriptor, 2, new ReferenceArraySerializer(getKClass(ItemType), ItemType$$serializer_getInstance())) : input.updateSerializableElement_ehubvl$(this.descriptor, 2, new ReferenceArraySerializer(getKClass(ItemType), ItemType$$serializer_getInstance()), local2);
          bitMask0 |= 4;
          if (!readAll)
            break;
        case -1:
          break loopLabel;
        default:throw new UnknownFieldException(index);
      }
    }
    input.endStructure_qatsm0$(this.descriptor);
    return Planet_init(bitMask0, local0, local1, local2, null);
  };
  Planet$$serializer.prototype.childSerializers = function () {
    return [internal.StringSerializer, new ReferenceArraySerializer(getKClass(Item), Item$$serializer_getInstance()), new ReferenceArraySerializer(getKClass(ItemType), ItemType$$serializer_getInstance())];
  };
  Planet$$serializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: '$serializer',
    interfaces: [GeneratedSerializer]
  };
  var Planet$$serializer_instance = null;
  function Planet$$serializer_getInstance() {
    if (Planet$$serializer_instance === null) {
      new Planet$$serializer();
    }
    return Planet$$serializer_instance;
  }
  function Planet_init(seen1, name, inventory, products, serializationConstructorMarker) {
    var $this = serializationConstructorMarker || Object.create(Planet.prototype);
    if ((seen1 & 1) === 0)
      throw new MissingFieldException('name');
    else
      $this.name = name;
    if ((seen1 & 2) === 0)
      $this.inventory = [];
    else
      $this.inventory = inventory;
    if ((seen1 & 4) === 0)
      $this.products = [];
    else
      $this.products = products;
    return $this;
  }
  Planet.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Planet',
    interfaces: []
  };
  Planet.prototype.component1 = function () {
    return this.name;
  };
  Planet.prototype.component2 = function () {
    return this.inventory;
  };
  Planet.prototype.component3 = function () {
    return this.products;
  };
  Planet.prototype.copy_bd9ee4$ = function (name, inventory, products) {
    return new Planet(name === void 0 ? this.name : name, inventory === void 0 ? this.inventory : inventory, products === void 0 ? this.products : products);
  };
  Planet.prototype.toString = function () {
    return 'Planet(name=' + Kotlin.toString(this.name) + (', inventory=' + Kotlin.toString(this.inventory)) + (', products=' + Kotlin.toString(this.products)) + ')';
  };
  Planet.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.name) | 0;
    result = result * 31 + Kotlin.hashCode(this.inventory) | 0;
    result = result * 31 + Kotlin.hashCode(this.products) | 0;
    return result;
  };
  Planet.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.name, other.name) && Kotlin.equals(this.inventory, other.inventory) && Kotlin.equals(this.products, other.products)))));
  };
  function Player(name, inventory, money) {
    Player$Companion_getInstance();
    if (inventory === void 0)
      inventory = [];
    this.name = name;
    this.inventory = inventory;
    this.money = money;
  }
  function Player$Companion() {
    Player$Companion_instance = this;
  }
  Player$Companion.prototype.serializer = function () {
    return Player$$serializer_getInstance();
  };
  Player$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var Player$Companion_instance = null;
  function Player$Companion_getInstance() {
    if (Player$Companion_instance === null) {
      new Player$Companion();
    }
    return Player$Companion_instance;
  }
  function Player$$serializer() {
    this.descriptor_gnjhiv$_0 = new SerialClassDescImpl('spaceMarket.model.Player', this);
    this.descriptor.addElement_ivxn3r$('name', false);
    this.descriptor.addElement_ivxn3r$('inventory', true);
    this.descriptor.addElement_ivxn3r$('money', false);
    Player$$serializer_instance = this;
  }
  Object.defineProperty(Player$$serializer.prototype, 'descriptor', {
    get: function () {
      return this.descriptor_gnjhiv$_0;
    }
  });
  Player$$serializer.prototype.serialize_awe97i$ = function (encoder, obj) {
    var output = encoder.beginStructure_r0sa6z$(this.descriptor, []);
    output.encodeStringElement_bgm7zs$(this.descriptor, 0, obj.name);
    if (!equals(obj.inventory, []) || output.shouldEncodeElementDefault_3zr2iy$(this.descriptor, 1))
      output.encodeSerializableElement_blecud$(this.descriptor, 1, new ReferenceArraySerializer(getKClass(Item), Item$$serializer_getInstance()), obj.inventory);
    output.encodeDoubleElement_imzr5k$(this.descriptor, 2, obj.money);
    output.endStructure_qatsm0$(this.descriptor);
  };
  Player$$serializer.prototype.deserialize_nts5qn$ = function (decoder) {
    var index, readAll = false;
    var bitMask0 = 0;
    var local0
        , local1
        , local2;
    var input = decoder.beginStructure_r0sa6z$(this.descriptor, []);
    loopLabel: while (true) {
      index = input.decodeElementIndex_qatsm0$(this.descriptor);
      switch (index) {
        case -2:
          readAll = true;
        case 0:
          local0 = input.decodeStringElement_3zr2iy$(this.descriptor, 0);
          bitMask0 |= 1;
          if (!readAll)
            break;
        case 1:
          local1 = (bitMask0 & 2) === 0 ? input.decodeSerializableElement_s44l7r$(this.descriptor, 1, new ReferenceArraySerializer(getKClass(Item), Item$$serializer_getInstance())) : input.updateSerializableElement_ehubvl$(this.descriptor, 1, new ReferenceArraySerializer(getKClass(Item), Item$$serializer_getInstance()), local1);
          bitMask0 |= 2;
          if (!readAll)
            break;
        case 2:
          local2 = input.decodeDoubleElement_3zr2iy$(this.descriptor, 2);
          bitMask0 |= 4;
          if (!readAll)
            break;
        case -1:
          break loopLabel;
        default:throw new UnknownFieldException(index);
      }
    }
    input.endStructure_qatsm0$(this.descriptor);
    return Player_init(bitMask0, local0, local1, local2, null);
  };
  Player$$serializer.prototype.childSerializers = function () {
    return [internal.StringSerializer, new ReferenceArraySerializer(getKClass(Item), Item$$serializer_getInstance()), internal.DoubleSerializer];
  };
  Player$$serializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: '$serializer',
    interfaces: [GeneratedSerializer]
  };
  var Player$$serializer_instance = null;
  function Player$$serializer_getInstance() {
    if (Player$$serializer_instance === null) {
      new Player$$serializer();
    }
    return Player$$serializer_instance;
  }
  function Player_init(seen1, name, inventory, money, serializationConstructorMarker) {
    var $this = serializationConstructorMarker || Object.create(Player.prototype);
    if ((seen1 & 1) === 0)
      throw new MissingFieldException('name');
    else
      $this.name = name;
    if ((seen1 & 2) === 0)
      $this.inventory = [];
    else
      $this.inventory = inventory;
    if ((seen1 & 4) === 0)
      throw new MissingFieldException('money');
    else
      $this.money = money;
    return $this;
  }
  Player.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Player',
    interfaces: []
  };
  Player.prototype.component1 = function () {
    return this.name;
  };
  Player.prototype.component2 = function () {
    return this.inventory;
  };
  Player.prototype.component3 = function () {
    return this.money;
  };
  Player.prototype.copy_ixeje2$ = function (name, inventory, money) {
    return new Player(name === void 0 ? this.name : name, inventory === void 0 ? this.inventory : inventory, money === void 0 ? this.money : money);
  };
  Player.prototype.toString = function () {
    return 'Player(name=' + Kotlin.toString(this.name) + (', inventory=' + Kotlin.toString(this.inventory)) + (', money=' + Kotlin.toString(this.money)) + ')';
  };
  Player.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.name) | 0;
    result = result * 31 + Kotlin.hashCode(this.inventory) | 0;
    result = result * 31 + Kotlin.hashCode(this.money) | 0;
    return result;
  };
  Player.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.name, other.name) && Kotlin.equals(this.inventory, other.inventory) && Kotlin.equals(this.money, other.money)))));
  };
  function MarketFact() {
    MarketFact$Companion_getInstance();
    this.product_sy7ig0$_0 = this.product_sy7ig0$_0;
    this.objet_zcsvqv$_0 = this.objet_zcsvqv$_0;
    this.vendeur_alh70y$_0 = this.vendeur_alh70y$_0;
    this.acheteur_tvaobc$_0 = this.acheteur_tvaobc$_0;
    this.producer_py03n1$_0 = this.producer_py03n1$_0;
    this.prix = 0.0;
  }
  Object.defineProperty(MarketFact.prototype, 'product', {
    get: function () {
      if (this.product_sy7ig0$_0 == null)
        return throwUPAE('product');
      return this.product_sy7ig0$_0;
    },
    set: function (product) {
      this.product_sy7ig0$_0 = product;
    }
  });
  Object.defineProperty(MarketFact.prototype, 'objet', {
    get: function () {
      if (this.objet_zcsvqv$_0 == null)
        return throwUPAE('objet');
      return this.objet_zcsvqv$_0;
    },
    set: function (objet) {
      this.objet_zcsvqv$_0 = objet;
    }
  });
  Object.defineProperty(MarketFact.prototype, 'vendeur', {
    get: function () {
      if (this.vendeur_alh70y$_0 == null)
        return throwUPAE('vendeur');
      return this.vendeur_alh70y$_0;
    },
    set: function (vendeur) {
      this.vendeur_alh70y$_0 = vendeur;
    }
  });
  Object.defineProperty(MarketFact.prototype, 'acheteur', {
    get: function () {
      if (this.acheteur_tvaobc$_0 == null)
        return throwUPAE('acheteur');
      return this.acheteur_tvaobc$_0;
    },
    set: function (acheteur) {
      this.acheteur_tvaobc$_0 = acheteur;
    }
  });
  Object.defineProperty(MarketFact.prototype, 'producer', {
    get: function () {
      if (this.producer_py03n1$_0 == null)
        return throwUPAE('producer');
      return this.producer_py03n1$_0;
    },
    set: function (producer) {
      this.producer_py03n1$_0 = producer;
    }
  });
  function MarketFact$Companion() {
    MarketFact$Companion_instance = this;
  }
  MarketFact$Companion.prototype.serializer = function () {
    return MarketFact$$serializer_getInstance();
  };
  MarketFact$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var MarketFact$Companion_instance = null;
  function MarketFact$Companion_getInstance() {
    if (MarketFact$Companion_instance === null) {
      new MarketFact$Companion();
    }
    return MarketFact$Companion_instance;
  }
  function MarketFact$$serializer() {
    this.descriptor_joc4pq$_0 = new SerialClassDescImpl('spaceMarket.model.MarketFact', this);
    this.descriptor.addElement_ivxn3r$('product', false);
    this.descriptor.addElement_ivxn3r$('objet', false);
    this.descriptor.addElement_ivxn3r$('vendeur', false);
    this.descriptor.addElement_ivxn3r$('acheteur', false);
    this.descriptor.addElement_ivxn3r$('producer', false);
    this.descriptor.addElement_ivxn3r$('prix', true);
    MarketFact$$serializer_instance = this;
  }
  Object.defineProperty(MarketFact$$serializer.prototype, 'descriptor', {
    get: function () {
      return this.descriptor_joc4pq$_0;
    }
  });
  MarketFact$$serializer.prototype.serialize_awe97i$ = function (encoder, obj) {
    var output = encoder.beginStructure_r0sa6z$(this.descriptor, []);
    output.encodeSerializableElement_blecud$(this.descriptor, 0, ItemType$$serializer_getInstance(), obj.product);
    output.encodeSerializableElement_blecud$(this.descriptor, 1, Item$$serializer_getInstance(), obj.objet);
    output.encodeSerializableElement_blecud$(this.descriptor, 2, Player$$serializer_getInstance(), obj.vendeur);
    output.encodeSerializableElement_blecud$(this.descriptor, 3, Player$$serializer_getInstance(), obj.acheteur);
    output.encodeSerializableElement_blecud$(this.descriptor, 4, Planet$$serializer_getInstance(), obj.producer);
    if (!equals(obj.prix, 0.0) || output.shouldEncodeElementDefault_3zr2iy$(this.descriptor, 5))
      output.encodeDoubleElement_imzr5k$(this.descriptor, 5, obj.prix);
    output.endStructure_qatsm0$(this.descriptor);
  };
  MarketFact$$serializer.prototype.deserialize_nts5qn$ = function (decoder) {
    var index, readAll = false;
    var bitMask0 = 0;
    var local0
        , local1
        , local2
        , local3
        , local4
        , local5;
    var input = decoder.beginStructure_r0sa6z$(this.descriptor, []);
    loopLabel: while (true) {
      index = input.decodeElementIndex_qatsm0$(this.descriptor);
      switch (index) {
        case -2:
          readAll = true;
        case 0:
          local0 = (bitMask0 & 1) === 0 ? input.decodeSerializableElement_s44l7r$(this.descriptor, 0, ItemType$$serializer_getInstance()) : input.updateSerializableElement_ehubvl$(this.descriptor, 0, ItemType$$serializer_getInstance(), local0);
          bitMask0 |= 1;
          if (!readAll)
            break;
        case 1:
          local1 = (bitMask0 & 2) === 0 ? input.decodeSerializableElement_s44l7r$(this.descriptor, 1, Item$$serializer_getInstance()) : input.updateSerializableElement_ehubvl$(this.descriptor, 1, Item$$serializer_getInstance(), local1);
          bitMask0 |= 2;
          if (!readAll)
            break;
        case 2:
          local2 = (bitMask0 & 4) === 0 ? input.decodeSerializableElement_s44l7r$(this.descriptor, 2, Player$$serializer_getInstance()) : input.updateSerializableElement_ehubvl$(this.descriptor, 2, Player$$serializer_getInstance(), local2);
          bitMask0 |= 4;
          if (!readAll)
            break;
        case 3:
          local3 = (bitMask0 & 8) === 0 ? input.decodeSerializableElement_s44l7r$(this.descriptor, 3, Player$$serializer_getInstance()) : input.updateSerializableElement_ehubvl$(this.descriptor, 3, Player$$serializer_getInstance(), local3);
          bitMask0 |= 8;
          if (!readAll)
            break;
        case 4:
          local4 = (bitMask0 & 16) === 0 ? input.decodeSerializableElement_s44l7r$(this.descriptor, 4, Planet$$serializer_getInstance()) : input.updateSerializableElement_ehubvl$(this.descriptor, 4, Planet$$serializer_getInstance(), local4);
          bitMask0 |= 16;
          if (!readAll)
            break;
        case 5:
          local5 = input.decodeDoubleElement_3zr2iy$(this.descriptor, 5);
          bitMask0 |= 32;
          if (!readAll)
            break;
        case -1:
          break loopLabel;
        default:throw new UnknownFieldException(index);
      }
    }
    input.endStructure_qatsm0$(this.descriptor);
    return MarketFact_init(bitMask0, local0, local1, local2, local3, local4, local5, null);
  };
  MarketFact$$serializer.prototype.childSerializers = function () {
    return [ItemType$$serializer_getInstance(), Item$$serializer_getInstance(), Player$$serializer_getInstance(), Player$$serializer_getInstance(), Planet$$serializer_getInstance(), internal.DoubleSerializer];
  };
  MarketFact$$serializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: '$serializer',
    interfaces: [GeneratedSerializer]
  };
  var MarketFact$$serializer_instance = null;
  function MarketFact$$serializer_getInstance() {
    if (MarketFact$$serializer_instance === null) {
      new MarketFact$$serializer();
    }
    return MarketFact$$serializer_instance;
  }
  function MarketFact_init(seen1, product, objet, vendeur, acheteur, producer, prix, serializationConstructorMarker) {
    var $this = serializationConstructorMarker || Object.create(MarketFact.prototype);
    if ((seen1 & 1) === 0)
      throw new MissingFieldException('product');
    else
      $this.product_sy7ig0$_0 = product;
    if ((seen1 & 2) === 0)
      throw new MissingFieldException('objet');
    else
      $this.objet_zcsvqv$_0 = objet;
    if ((seen1 & 4) === 0)
      throw new MissingFieldException('vendeur');
    else
      $this.vendeur_alh70y$_0 = vendeur;
    if ((seen1 & 8) === 0)
      throw new MissingFieldException('acheteur');
    else
      $this.acheteur_tvaobc$_0 = acheteur;
    if ((seen1 & 16) === 0)
      throw new MissingFieldException('producer');
    else
      $this.producer_py03n1$_0 = producer;
    if ((seen1 & 32) === 0)
      $this.prix = 0.0;
    else
      $this.prix = prix;
    return $this;
  }
  MarketFact.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'MarketFact',
    interfaces: []
  };
  function ruleBuy$ObjectLiteral() {
  }
  ruleBuy$ObjectLiteral.prototype.evaluate_11rb$ = function (facts) {
    var acheteur = facts.acheteur;
    var vendeur = facts.vendeur;
    var objet = facts.objet;
    var prix = facts.prix;
    return acheteur.money >= prix && contains(vendeur.inventory, objet);
  };
  ruleBuy$ObjectLiteral.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Condition]
  };
  function ruleBuy$ObjectLiteral_0() {
  }
  ruleBuy$ObjectLiteral_0.prototype.execute_11rb$ = function (facts) {
    var acheteur = facts.acheteur;
    var vendeur = facts.vendeur;
    var objet = {v: facts.objet};
    var prix = facts.prix;
    acheteur.money = acheteur.money - prix;
    vendeur.money = vendeur.money + prix;
    var $receiver = vendeur.inventory;
    var destination = ArrayList_init();
    var tmp$;
    for (tmp$ = 0; tmp$ !== $receiver.length; ++tmp$) {
      var element = $receiver[tmp$];
      if (element != null ? element.equals(objet.v) : null)
        destination.add_11rb$(element);
    }
    vendeur.inventory = copyToArray(destination);
    var $receiver_0 = acheteur.inventory;
    var element_0 = objet.v;
    acheteur.inventory = $receiver_0.concat([element_0]);
  };
  ruleBuy$ObjectLiteral_0.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Action]
  };
  var ruleBuy;
  function ruleProduce$ObjectLiteral() {
  }
  ruleProduce$ObjectLiteral.prototype.evaluate_11rb$ = function (facts) {
    var acheteur = facts.acheteur;
    var producer = facts.producer;
    var what = facts.product;
    return contains(producer.products, what);
  };
  ruleProduce$ObjectLiteral.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Condition]
  };
  function ruleProduce$ObjectLiteral_0() {
  }
  ruleProduce$ObjectLiteral_0.prototype.execute_11rb$ = function (facts) {
    var acheteur = facts.acheteur;
    var producer = facts.producer;
    var what = facts.product;
    var $receiver = producer.inventory;
    var element = new Item(what, 1);
    producer.inventory = $receiver.concat([element]);
  };
  ruleProduce$ObjectLiteral_0.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Action]
  };
  var ruleProduce;
  function ruleTake$ObjectLiteral() {
  }
  ruleTake$ObjectLiteral.prototype.evaluate_11rb$ = function (facts) {
    var acheteur = facts.acheteur;
    var producer = facts.producer;
    var what = {v: facts.product};
    var $receiver = producer.inventory;
    var destination = ArrayList_init();
    var tmp$;
    for (tmp$ = 0; tmp$ !== $receiver.length; ++tmp$) {
      var element = $receiver[tmp$];
      var tmp$_0;
      if ((tmp$_0 = element.type) != null ? tmp$_0.equals(what.v) : null)
        destination.add_11rb$(element);
    }
    return !destination.isEmpty();
  };
  ruleTake$ObjectLiteral.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Condition]
  };
  function ruleTake$ObjectLiteral_0() {
  }
  ruleTake$ObjectLiteral_0.prototype.execute_11rb$ = function (facts) {
    var acheteur = facts.acheteur;
    var producer = facts.producer;
    var what = facts.product;
    var $receiver = acheteur.inventory;
    var element = new Item(what);
    acheteur.inventory = $receiver.concat([element]);
  };
  ruleTake$ObjectLiteral_0.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Action]
  };
  var ruleTake;
  function buildActionRules() {
    return Rules_init_0(setOf([ruleBuy, ruleProduce, ruleTake]));
  }
  function SpaceRule() {
  }
  SpaceRule.prototype.runRules = function (arg) {
    var rules = buildActionRules();
    var rulesEngine = new InferenceRulesEngine();
    var facts;
    facts = arg;
    rulesEngine.fire_qkxksy$(rules, facts);
    return facts;
  };
  SpaceRule.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'SpaceRule',
    interfaces: []
  };
  function Player_0() {
    Vivant.call(this);
    this.hydratation = 100;
    this.energie = 100;
    this.calorie = 100;
    this.temperature = 100;
    this.oxygen = 100;
  }
  Player_0.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Player',
    interfaces: [Vivant]
  };
  function Vivant() {
    this.santé = 100;
    this.inventaire = ArrayList_init();
    this.location_rykfhn$_0 = this.location_rykfhn$_0;
  }
  Object.defineProperty(Vivant.prototype, 'location', {
    get: function () {
      if (this.location_rykfhn$_0 == null)
        return throwUPAE('location');
      return this.location_rykfhn$_0;
    },
    set: function (location) {
      this.location_rykfhn$_0 = location;
    }
  });
  Vivant.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Vivant',
    interfaces: []
  };
  function Item_0() {
  }
  Item_0.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Item',
    interfaces: []
  };
  function Nouriture() {
    Item_0.call(this);
  }
  Nouriture.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Nouriture',
    interfaces: [Item_0]
  };
  function Eau() {
    Item_0.call(this);
  }
  Eau.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Eau',
    interfaces: [Item_0]
  };
  function Epee() {
    Item_0.call(this);
  }
  Epee.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Epee',
    interfaces: [Item_0]
  };
  function ActionEnum(name, ordinal) {
    Enum.call(this);
    this.name$ = name;
    this.ordinal$ = ordinal;
  }
  function ActionEnum_initFields() {
    ActionEnum_initFields = function () {
    };
    ActionEnum$attaquer_instance = new ActionEnum('attaquer', 0);
    ActionEnum$bouger_instance = new ActionEnum('bouger', 1);
    ActionEnum$rammasser_instance = new ActionEnum('rammasser', 2);
    ActionEnum$utiliser_instance = new ActionEnum('utiliser', 3);
  }
  var ActionEnum$attaquer_instance;
  function ActionEnum$attaquer_getInstance() {
    ActionEnum_initFields();
    return ActionEnum$attaquer_instance;
  }
  var ActionEnum$bouger_instance;
  function ActionEnum$bouger_getInstance() {
    ActionEnum_initFields();
    return ActionEnum$bouger_instance;
  }
  var ActionEnum$rammasser_instance;
  function ActionEnum$rammasser_getInstance() {
    ActionEnum_initFields();
    return ActionEnum$rammasser_instance;
  }
  var ActionEnum$utiliser_instance;
  function ActionEnum$utiliser_getInstance() {
    ActionEnum_initFields();
    return ActionEnum$utiliser_instance;
  }
  ActionEnum.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'ActionEnum',
    interfaces: [Enum]
  };
  function ActionEnum$values() {
    return [ActionEnum$attaquer_getInstance(), ActionEnum$bouger_getInstance(), ActionEnum$rammasser_getInstance(), ActionEnum$utiliser_getInstance()];
  }
  ActionEnum.values = ActionEnum$values;
  function ActionEnum$valueOf(name) {
    switch (name) {
      case 'attaquer':
        return ActionEnum$attaquer_getInstance();
      case 'bouger':
        return ActionEnum$bouger_getInstance();
      case 'rammasser':
        return ActionEnum$rammasser_getInstance();
      case 'utiliser':
        return ActionEnum$utiliser_getInstance();
      default:throwISE('No enum constant survival.ActionEnum.' + name);
    }
  }
  ActionEnum.valueOf_61zpoe$ = ActionEnum$valueOf;
  function FactEnum(name, ordinal) {
    Enum.call(this);
    this.name$ = name;
    this.ordinal$ = ordinal;
  }
  function FactEnum_initFields() {
    FactEnum_initFields = function () {
    };
    FactEnum$qui_instance = new FactEnum('qui', 0);
    FactEnum$surQui_instance = new FactEnum('surQui', 1);
    FactEnum$faitQuoi_instance = new FactEnum('faitQuoi', 2);
  }
  var FactEnum$qui_instance;
  function FactEnum$qui_getInstance() {
    FactEnum_initFields();
    return FactEnum$qui_instance;
  }
  var FactEnum$surQui_instance;
  function FactEnum$surQui_getInstance() {
    FactEnum_initFields();
    return FactEnum$surQui_instance;
  }
  var FactEnum$faitQuoi_instance;
  function FactEnum$faitQuoi_getInstance() {
    FactEnum_initFields();
    return FactEnum$faitQuoi_instance;
  }
  FactEnum.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'FactEnum',
    interfaces: [Enum]
  };
  function FactEnum$values() {
    return [FactEnum$qui_getInstance(), FactEnum$surQui_getInstance(), FactEnum$faitQuoi_getInstance()];
  }
  FactEnum.values = FactEnum$values;
  function FactEnum$valueOf(name) {
    switch (name) {
      case 'qui':
        return FactEnum$qui_getInstance();
      case 'surQui':
        return FactEnum$surQui_getInstance();
      case 'faitQuoi':
        return FactEnum$faitQuoi_getInstance();
      default:throwISE('No enum constant survival.FactEnum.' + name);
    }
  }
  FactEnum.valueOf_61zpoe$ = FactEnum$valueOf;
  function Location() {
    this.content = ArrayList_init();
  }
  Location.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Location',
    interfaces: []
  };
  function GameFacts() {
    this.qui = null;
    this.surQui = null;
    this.faitQuoi_2rc431$_0 = this.faitQuoi_2rc431$_0;
  }
  Object.defineProperty(GameFacts.prototype, 'faitQuoi', {
    get: function () {
      if (this.faitQuoi_2rc431$_0 == null)
        return throwUPAE('faitQuoi');
      return this.faitQuoi_2rc431$_0;
    },
    set: function (faitQuoi) {
      this.faitQuoi_2rc431$_0 = faitQuoi;
    }
  });
  GameFacts.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'GameFacts',
    interfaces: []
  };
  function buildActionRules$ObjectLiteral() {
  }
  buildActionRules$ObjectLiteral.prototype.evaluate_11rb$ = function (facts) {
    return contains_0(ensureNotNull(facts.qui).inventaire, facts.surQui) && Kotlin.isType(facts.surQui, Nouriture) && facts.faitQuoi === ActionEnum$utiliser_getInstance();
  };
  buildActionRules$ObjectLiteral.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Condition]
  };
  function buildActionRules$ObjectLiteral_0() {
  }
  buildActionRules$ObjectLiteral_0.prototype.execute_11rb$ = function (facts) {
    var tmp$, tmp$_0;
    println('run');
    var $receiver = ensureNotNull(facts.qui).inventaire;
    var element = facts.surQui;
    var tmp$_1;
    (Kotlin.isType(tmp$_1 = $receiver, MutableCollection) ? tmp$_1 : throwCCE()).remove_11rb$(element);
    tmp$_0 = Kotlin.isType(tmp$ = facts.qui, Player_0) ? tmp$ : throwCCE();
    tmp$_0.calorie = tmp$_0.calorie + 10 | 0;
  };
  buildActionRules$ObjectLiteral_0.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Action]
  };
  function buildActionRules$ObjectLiteral_1() {
  }
  buildActionRules$ObjectLiteral_1.prototype.evaluate_11rb$ = function (facts) {
    var tmp$;
    return facts.faitQuoi.equals(ActionEnum$bouger_getInstance()) && (Kotlin.isType(tmp$ = facts.qui, Player_0) ? tmp$ : throwCCE()).energie > 0;
  };
  buildActionRules$ObjectLiteral_1.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Condition]
  };
  function buildActionRules$ObjectLiteral_2() {
  }
  buildActionRules$ObjectLiteral_2.prototype.execute_11rb$ = function (facts) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    tmp$_0 = Kotlin.isType(tmp$ = facts.qui, Player_0) ? tmp$ : throwCCE();
    tmp$_0.energie = tmp$_0.energie - 10 | 0;
    (Kotlin.isType(tmp$_2 = facts.qui, Player_0) ? tmp$_2 : throwCCE()).location = Kotlin.isType(tmp$_1 = facts.surQui, Location) ? tmp$_1 : throwCCE();
  };
  buildActionRules$ObjectLiteral_2.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Action]
  };
  function buildActionRules$ObjectLiteral_3() {
  }
  buildActionRules$ObjectLiteral_3.prototype.evaluate_11rb$ = function (facts) {
    var tmp$, tmp$_0, tmp$_1;
    return facts.faitQuoi.equals(ActionEnum$rammasser_getInstance()) && (Kotlin.isType(tmp$ = facts.qui, Player_0) ? tmp$ : throwCCE()).energie > 0 ? (Kotlin.isType(tmp$_0 = facts.qui, Player_0) ? tmp$_0 : throwCCE()).location.content.contains_11rb$(Kotlin.isType(tmp$_1 = facts.surQui, Item_0) ? tmp$_1 : throwCCE()) : false;
  };
  buildActionRules$ObjectLiteral_3.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Condition]
  };
  function buildActionRules$ObjectLiteral_4() {
  }
  buildActionRules$ObjectLiteral_4.prototype.execute_11rb$ = function (facts) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2, tmp$_3, tmp$_4, tmp$_5, tmp$_6;
    tmp$_0 = Kotlin.isType(tmp$ = facts.qui, Player_0) ? tmp$ : throwCCE();
    tmp$_0.energie = tmp$_0.energie - 10 | 0;
    tmp$_2 = Kotlin.isType(tmp$_1 = facts.qui, Player_0) ? tmp$_1 : throwCCE();
    tmp$_2.calorie = tmp$_2.calorie - 10 | 0;
    (Kotlin.isType(tmp$_3 = facts.qui, Player_0) ? tmp$_3 : throwCCE()).inventaire.add_11rb$(Kotlin.isType(tmp$_4 = facts.surQui, Item_0) ? tmp$_4 : throwCCE());
    (Kotlin.isType(tmp$_5 = facts.qui, Player_0) ? tmp$_5 : throwCCE()).location.content.remove_11rb$(Kotlin.isType(tmp$_6 = facts.surQui, Item_0) ? tmp$_6 : throwCCE());
  };
  buildActionRules$ObjectLiteral_4.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [Action]
  };
  function buildActionRules_0() {
    var mangeRule = (new RuleBuilder()).name_61zpoe$('mange').when_ieqdlb$(new buildActionRules$ObjectLiteral()).then_ptknqy$(new buildActionRules$ObjectLiteral_0()).build();
    var rules = Rules_init();
    rules.register_7crgn4$(mangeRule);
    register(rules, 'bouge', new buildActionRules$ObjectLiteral_1(), new buildActionRules$ObjectLiteral_2());
    register(rules, 'rammasser', new buildActionRules$ObjectLiteral_3(), new buildActionRules$ObjectLiteral_4());
    return rules;
  }
  function register(rules, name, condition, action) {
    rules.register_7crgn4$((new RuleBuilder()).name_61zpoe$(name).when_ieqdlb$(condition).then_ptknqy$(action).build());
  }
  function main() {
    var tmp$, tmp$_0;
    var facts = new GameFacts();
    facts.qui = new Player_0();
    facts.surQui = new Nouriture();
    facts.faitQuoi = ActionEnum$utiliser_getInstance();
    println((Kotlin.isType(tmp$ = facts.qui, Player_0) ? tmp$ : throwCCE()).calorie);
    (new InferenceRulesEngine()).fire_qkxksy$(buildActionRules_0(), facts);
    println((Kotlin.isType(tmp$_0 = facts.qui, Player_0) ? tmp$_0 : throwCCE()).calorie);
  }
  function RuleBookRules() {
    RuleBookRules$Companion_getInstance();
  }
  function RuleBookRules$Companion() {
    RuleBookRules$Companion_instance = this;
  }
  RuleBookRules$Companion.prototype.runSurvivalRules_4mlbmj$ = function (facts) {
    (new InferenceRulesEngine()).fire_qkxksy$(buildActionRules_0(), facts);
    return facts;
  };
  RuleBookRules$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var RuleBookRules$Companion_instance = null;
  function RuleBookRules$Companion_getInstance() {
    if (RuleBookRules$Companion_instance === null) {
      new RuleBookRules$Companion();
    }
    return RuleBookRules$Companion_instance;
  }
  RuleBookRules.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'RuleBookRules',
    interfaces: []
  };
  Object.defineProperty(DecreaseTemperatureAction, 'Companion', {
    get: DecreaseTemperatureAction$Companion_getInstance
  });
  var package$airco = _.airco || (_.airco = {});
  package$airco.DecreaseTemperatureAction = DecreaseTemperatureAction;
  Object.defineProperty(HighTemperatureCondition, 'Companion', {
    get: HighTemperatureCondition$Companion_getInstance
  });
  package$airco.HighTemperatureCondition = HighTemperatureCondition;
  package$airco.Launcher = Launcher;
  Object.defineProperty(ItemType, 'Companion', {
    get: ItemType$Companion_getInstance
  });
  Object.defineProperty(ItemType, '$serializer', {
    get: ItemType$$serializer_getInstance
  });
  var package$spaceMarket = _.spaceMarket || (_.spaceMarket = {});
  var package$model = package$spaceMarket.model || (package$spaceMarket.model = {});
  package$model.ItemType = ItemType;
  Object.defineProperty(Item, 'Companion', {
    get: Item$Companion_getInstance
  });
  Object.defineProperty(Item, '$serializer', {
    get: Item$$serializer_getInstance
  });
  package$model.Item = Item;
  Object.defineProperty(Planet, 'Companion', {
    get: Planet$Companion_getInstance
  });
  Object.defineProperty(Planet, '$serializer', {
    get: Planet$$serializer_getInstance
  });
  package$model.Planet = Planet;
  Object.defineProperty(Player, 'Companion', {
    get: Player$Companion_getInstance
  });
  Object.defineProperty(Player, '$serializer', {
    get: Player$$serializer_getInstance
  });
  package$model.Player = Player;
  Object.defineProperty(MarketFact, 'Companion', {
    get: MarketFact$Companion_getInstance
  });
  Object.defineProperty(MarketFact, '$serializer', {
    get: MarketFact$$serializer_getInstance
  });
  package$model.MarketFact = MarketFact;
  Object.defineProperty(package$model, 'ruleBuy', {
    get: function () {
      return ruleBuy;
    }
  });
  Object.defineProperty(package$model, 'ruleProduce', {
    get: function () {
      return ruleProduce;
    }
  });
  Object.defineProperty(package$model, 'ruleTake', {
    get: function () {
      return ruleTake;
    }
  });
  package$model.buildActionRules = buildActionRules;
  package$model.SpaceRule = SpaceRule;
  var package$survival = _.survival || (_.survival = {});
  package$survival.Player = Player_0;
  package$survival.Vivant = Vivant;
  package$survival.Item = Item_0;
  package$survival.Nouriture = Nouriture;
  package$survival.Eau = Eau;
  package$survival.Epee = Epee;
  Object.defineProperty(ActionEnum, 'attaquer', {
    get: ActionEnum$attaquer_getInstance
  });
  Object.defineProperty(ActionEnum, 'bouger', {
    get: ActionEnum$bouger_getInstance
  });
  Object.defineProperty(ActionEnum, 'rammasser', {
    get: ActionEnum$rammasser_getInstance
  });
  Object.defineProperty(ActionEnum, 'utiliser', {
    get: ActionEnum$utiliser_getInstance
  });
  package$survival.ActionEnum = ActionEnum;
  Object.defineProperty(FactEnum, 'qui', {
    get: FactEnum$qui_getInstance
  });
  Object.defineProperty(FactEnum, 'surQui', {
    get: FactEnum$surQui_getInstance
  });
  Object.defineProperty(FactEnum, 'faitQuoi', {
    get: FactEnum$faitQuoi_getInstance
  });
  package$survival.FactEnum = FactEnum;
  package$survival.Location = Location;
  package$survival.GameFacts = GameFacts;
  package$survival.buildActionRules = buildActionRules_0;
  package$survival.register_3nt09a$ = register;
  package$survival.main = main;
  Object.defineProperty(RuleBookRules, 'Companion', {
    get: RuleBookRules$Companion_getInstance
  });
  package$survival.RuleBookRules = RuleBookRules;
  ItemType$$serializer.prototype.patch_mynpiu$ = GeneratedSerializer.prototype.patch_mynpiu$;
  Item$$serializer.prototype.patch_mynpiu$ = GeneratedSerializer.prototype.patch_mynpiu$;
  Planet$$serializer.prototype.patch_mynpiu$ = GeneratedSerializer.prototype.patch_mynpiu$;
  Player$$serializer.prototype.patch_mynpiu$ = GeneratedSerializer.prototype.patch_mynpiu$;
  MarketFact$$serializer.prototype.patch_mynpiu$ = GeneratedSerializer.prototype.patch_mynpiu$;
  ruleBuy = (new RuleBuilder()).name_61zpoe$('buy').when_ieqdlb$(new ruleBuy$ObjectLiteral()).then_ptknqy$(new ruleBuy$ObjectLiteral_0()).build();
  ruleProduce = (new RuleBuilder()).name_61zpoe$('produce').when_ieqdlb$(new ruleProduce$ObjectLiteral()).then_ptknqy$(new ruleProduce$ObjectLiteral_0()).build();
  ruleTake = (new RuleBuilder()).name_61zpoe$('take').when_ieqdlb$(new ruleTake$ObjectLiteral()).then_ptknqy$(new ruleTake$ObjectLiteral_0()).build();
  main();
  Kotlin.defineModule('easy-rules-kotlin-example-gradle', _);
  return _;
}));

//# sourceMappingURL=easy-rules-kotlin-example-gradle.js.map
