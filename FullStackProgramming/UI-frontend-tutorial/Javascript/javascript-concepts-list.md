# Exhaustive JavaScript Programming Concepts

A structured, cumulative list — basics first, then intermediate, advanced, and finally the features finalized in **ECMAScript 2025 (ES2025 / ES16)**, approved June 25, 2025, the latest standardized edition as of this writing. JavaScript is the language; ECMAScript (maintained by TC39) is the specification it implements.

---

## 1. Setup & Fundamentals
- JavaScript engines (V8, SpiderMonkey, JavaScriptCore) vs runtimes (browser, Node.js, Deno, Bun)
- `<script>` tags, `.js` files, module vs script mode
- The console (`console.log`, `console.error`, `console.table`, etc.)
- Comments (`//`, `/* */`)
- Semicolons and Automatic Semicolon Insertion (ASI)
- Strict mode (`"use strict"`)
- ECMAScript versioning (ES5, ES6/ES2015, ...through ES2025) and TC39 stages

## 2. Variables & Data Types
- `var`, `let`, `const` — scoping and hoisting differences
- Primitive types: `Number`, `String`, `Boolean`, `null`, `undefined`, `Symbol`, `BigInt`
- Reference types: `Object`, `Array`, `Function`
- Type coercion (implicit vs explicit)
- `typeof`, `instanceof`
- Truthy/falsy values
- Template literals (`` `${expr}` ``)
- `NaN`, `Infinity`, numeric separators (`1_000_000`)

## 3. Operators
- Arithmetic (`+ - * / % **`)
- Comparison (`== ===  != !==  > < >= <=`)
- Logical (`&& || !`)
- Nullish coalescing (`??`)
- Optional chaining (`?.`)
- Logical assignment (`&&= ||= ??=`)
- Bitwise (`& | ^ ~ << >> >>>`)
- Assignment operators (`= += -=` etc.)
- Ternary (`?:`)
- Spread (`...`) and rest (`...`) syntax
- `typeof`, `delete`, `in`, `void`

## 4. Control Flow
- `if / else if / else`
- `switch / case / default`
- `for`, `for...in`, `for...of`
- `while`, `do...while`
- `break`, `continue`, labeled statements
- Error handling flow (`try / catch / finally`)

## 5. Functions
- Function declarations vs function expressions
- Arrow functions (and lexical `this`)
- Default parameters
- Rest parameters (`...args`)
- Immediately Invoked Function Expressions (IIFEs)
- First-class functions, functions as values
- Higher-order functions
- Closures
- Recursion
- The `arguments` object
- `call`, `apply`, `bind`
- Pure functions vs side effects
- Function hoisting

## 6. Objects
- Object literals, properties, methods
- Property shorthand, computed property names
- `this` binding rules (default, implicit, explicit, `new`, arrow)
- Prototypes and the prototype chain (`__proto__`, `Object.getPrototypeOf`)
- `Object.create`, `Object.assign`, `Object.freeze`, `Object.seal`
- Getters and setters (`get`/`set`)
- Object destructuring
- `Object.keys`, `Object.values`, `Object.entries`
- `Object.groupBy` (baseline ES2024, widely used alongside ES2025 features)
- JSON (`JSON.stringify`, `JSON.parse`)

## 7. Arrays
- Array literals, indexing
- Common methods: `push`, `pop`, `shift`, `unshift`, `slice`, `splice`, `concat`
- Iteration methods: `map`, `filter`, `reduce`, `forEach`, `find`, `findIndex`, `some`, `every`, `sort`, `flat`, `flatMap`
- Array destructuring
- Spread operator with arrays
- Array-like objects vs true arrays
- Typed arrays (`Int8Array`, `Float32Array`, `Float64Array`, etc.)
- **`Float16Array` and related `DataView.getFloat16`/`setFloat16`, `Math.f16round` (ES2025)** — half-precision floats, useful for ML/graphics workloads
- **`Array.fromAsync()` (ES2025)** — builds an array from an async iterable

## 8. Strings
- String immutability
- Common methods: `slice`, `substring`, `split`, `replace`, `replaceAll`, `trim`, `padStart`/`padEnd`, `includes`, `startsWith`, `endsWith`
- Template literals and tagged templates
- String iteration (code points vs code units, Unicode handling)

## 9. Classes & OOP
- `class` syntax (syntactic sugar over prototypes)
- Constructors
- Instance fields and methods
- Static methods and static fields
- Private fields/methods (`#field`)
- Inheritance (`extends`, `super`)
- Getters/setters in classes
- `instanceof`, abstract-pattern emulation
- Mixins (composition pattern since JS lacks multiple inheritance)
- `Object.getPrototypeOf` / class hierarchy inspection

## 10. Error Handling
- `try / catch / finally`
- `throw`, custom `Error` subclasses
- Built-in error types (`TypeError`, `RangeError`, `SyntaxError`, `ReferenceError`)
- `Error.cause` (chained error context)
- Async error handling (`.catch()`, `try/catch` with `await`)

## 11. Asynchronous JavaScript
- The event loop, call stack, task queue, microtask queue
- Callbacks and callback hell
- `Promise`: `then`, `catch`, `finally`, states (pending/fulfilled/rejected)
- `Promise.all`, `Promise.race`, `Promise.allSettled`, `Promise.any`
- **`Promise.try()` (ES2025)** — runs a function synchronously when possible while always returning a Promise, unifying sync/async error handling
- `async`/`await`
- `setTimeout`, `setInterval`, `queueMicrotask`
- Async iterators and generators (`for await...of`)

## 12. Iterators & Generators
- Iterable & iterator protocols (`Symbol.iterator`, `next()`)
- Generator functions (`function*`, `yield`)
- `yield*` delegation
- Async generators
- **Iterator Helpers (ES2025)** — chainable, lazily-evaluated methods directly on iterators: `.map()`, `.filter()`, `.take()`, `.drop()`, `.flatMap()`, `.reduce()`, `.toArray()`, `.forEach()`, `.some()`, `.every()`, `.find()` — avoids materializing intermediate arrays

## 13. Collections
- `Map`, `WeakMap`
- `Set`, `WeakSet`
- **New `Set` composition methods (ES2025)**: `union()`, `intersection()`, `difference()`, `symmetricDifference()`, `isSubsetOf()`, `isSupersetOf()`, `isDisjointFrom()`
- `WeakRef` and `FinalizationRegistry`

## 14. Modules
- `import` / `export` (named and default)
- Dynamic `import()`
- Module scope vs script scope
- CommonJS (`require`/`module.exports`) vs ES Modules — conceptual contrast
- **JSON modules via import attributes (ES2025)**: `import config from './data.json' with { type: 'json' }` — standardized static and dynamic JSON imports, replacing build-step workarounds

## 15. Regular Expressions
- Regex literals and the `RegExp` object
- Flags (`g`, `i`, `m`, `s`, `u`, `y`, `d`)
- Groups, named capture groups, backreferences
- Lookahead/lookbehind assertions
- **`RegExp.escape()` (ES2025)** — safely escapes special characters in a string before embedding it in a regex, preventing regex-injection issues
- **Duplicate named capture groups across alternatives (ES2025)**

## 16. Browser & DOM APIs (Web-context concepts)
- The DOM tree, `document`, `window`
- Selecting/manipulating elements (`querySelector`, `createElement`, etc.)
- Event handling (`addEventListener`, event bubbling/capturing, delegation)
- `fetch()` API, `XMLHttpRequest` (legacy)
- Local storage, session storage, cookies
- Web Workers (background threads)
- The Fetch/Streams/Observer APIs (Intersection, Mutation, Resize)

## 17. Node.js / Server-side Concepts (if relevant to JS broadly)
- Modules (`require`, `module.exports`, ESM in Node)
- `npm`/`yarn`/`pnpm`, `package.json`
- Event-driven, non-blocking I/O model
- Streams, buffers
- `process`, environment variables
- Building simple servers (conceptual, `http` module)

## 18. Functional Programming Patterns
- Immutability practices
- Pure functions
- Function composition
- Currying
- Memoization
- Declarative vs imperative style

## 19. Design Patterns & Architecture (conceptual, language-adjacent)
- Module pattern, Revealing Module pattern
- Singleton, Factory, Observer patterns in JS
- MVC/MVVM concepts as applied in frontend frameworks (conceptual only)
- Event-driven architecture

## 20. Tooling & Ecosystem
- Transpilers (Babel) and bundlers (Webpack, Vite, esbuild) — conceptual
- Linters/formatters (ESLint, Prettier) — conceptual
- TypeScript as a superset (conceptual bridge, not JS itself)
- Testing (Jest, Mocha — conceptual)
- Source maps, debugging tools

---

### Notes on ECMAScript 2025 specifically
ES2025 (the 16th edition) was approved by the Ecma General Assembly on June 25, 2025. Its finalized, shipped features are deliberately incremental and non-breaking: **Iterator Helpers**, new **Set composition methods**, **`Promise.try()`**, **`Float16Array`** (plus related `DataView`/`Math` support), **JSON modules via import attributes**, **`RegExp.escape()`**, **duplicate named capture groups**, and **`Array.fromAsync()`**.

A few features sometimes mislabeled as "ES2025" online — **Records & Tuples**, a **pipeline operator (`|>`)**, general **pattern matching** (`match`), a native **Decimal** type, and the **Temporal API** — are **not** part of the finalized ES2025 spec. They remain earlier-stage TC39 proposals (some, like Temporal and Explicit Resource Management, are on track for ES2026); worth knowing about but not yet standard.
