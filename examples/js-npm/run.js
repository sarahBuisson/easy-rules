
let kotlin = require('kotlin')
console.log(Object.keys(require('module')._cache))
console.log(require)
let log = require('./lib/kotlin-logging')
let easy = require('./lib/easy-rules-core').org.jeasy

console.log(easy);

console.log(new easy.rules.api.Action());
