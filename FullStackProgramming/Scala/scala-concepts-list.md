# Exhaustive Scala Programming Concepts

A structured, cumulative list — basics first, then intermediate, advanced, and finally the newer language features that have landed across the Scala 3 line (3.3 LTS through 3.8, with **Scala 3.9 as the new LTS succeeding 3.3**, feature-frozen to match 3.8 and requiring JDK 17+). Scala 2 concepts are noted where they differ meaningfully from Scala 3.

---

## 1. Setup & Fundamentals
- The Scala compiler (`scalac`), REPL, and **Scala CLI** (the official `scala` command since 3.5.0, replacing older launcher scripts)
- `.scala` and `.sc` (script) files
- Scala 2 vs Scala 3 — major differences overview
- Interop with Java (calling Java from Scala and vice versa)
- SBT, Mill, and other build tools (conceptual)
- `sbt console`, worksheets
- Comments (`//`, `/* */`, Scaladoc `/** */`)
- Compilation model, running on the JVM (also Scala.js for JS, Scala Native for LLVM)
- **Scala 3.8: standard library now compiled by Scala 3 itself** (previously Scala 2.13-compiled)

## 2. Variables & Data Types
- `val` (immutable) vs `var` (mutable)
- Type inference
- Primitive-ish types: `Int`, `Long`, `Double`, `Float`, `Boolean`, `Char`, `Byte`, `Short`
- `String`
- `Unit`, `Null`, `Nothing`, `Any`, `AnyVal`, `AnyRef` (the top/bottom type hierarchy)
- Type ascription (`x: Int`)
- Literal types
- String interpolation (`s"..."`, `f"..."`, `raw"..."`)
- Multiline strings (triple-quoted)
- Lazy values (`lazy val`)

## 3. Operators & Expressions
- Arithmetic, relational, logical, bitwise operators
- Everything is an expression (blocks return values)
- Operators as methods (`a + b` is `a.+(b)`)
- Infix, prefix, and postfix notation
- Ternary-equivalent (`if` as an expression)
- Equality (`==`, `equals`, `eq` for reference equality)

## 4. Control Flow
- `if / else` as expressions
- `for` loops and **for-comprehensions** (`for { ... } yield`)
- **"Better Fors" (SIP-62, stabilized in Scala 3.8)** — improved desugaring for `for`-comprehensions, better error messages and type inference
- `while`, `do-while`
- `match` expressions (pattern matching) — the core control-flow idiom in Scala
- Guards in pattern matches (`case x if ...`)
- Sealed hierarchies + exhaustiveness checking
- `break`/`continue` equivalents (`scala.util.control.Breaks`, generally discouraged in favor of functional style)

## 5. Functions
- `def` method definitions
- Function values / function literals (`(x: Int) => x + 1`)
- Anonymous functions and placeholder syntax (`_`)
- Higher-order functions
- Partial application, currying (`def f(x: Int)(y: Int)`)
- Default and named parameters
- Variadic parameters (`Int*`)
- **Flexible/multi-spread varargs (SIP-70, experimental as of 3.8)** — use `*` spread more than once in a call
- Recursion, tail recursion (`@tailrec`)
- Nested (local) functions
- By-name parameters (`=> T`)
- Pure functions and referential transparency (functional-programming emphasis)

## 6. Object-Oriented Programming
- `class`, `object` (singleton), `case class`, `case object`
- Constructors (primary and auxiliary `this(...)`)
- Fields, methods, `this`
- Access modifiers (`private`, `protected`, `private[this]`, package-private)
- Inheritance (`extends`), method overriding (`override`)
- Abstract classes and abstract members
- Traits (interfaces with implementation, mixin composition)
- **Trait parameters** (traits can take constructor-like parameters, Scala 3)
- Multiple trait mixing (`with`), linearization order
- Companion objects and companion classes
- `apply`/`unapply` (factory methods and extractors)
- **Universal apply methods** (Scala 3 — case-class-like `apply` for regular classes)
- Case classes: auto-generated `equals`, `hashCode`, `toString`, `copy`, pattern-match support
- Enums (`enum` keyword, first-class in Scala 3, replacing the old sealed-trait-plus-case-object idiom)
- `final`, sealed classes/traits
- Structural types

## 7. The Type System
- Generics / type parameters (`class Box[T]`)
- Variance annotations (`+T` covariant, `-T` contravariant, invariant by default)
- Bounded types (`T <: Upper`, `T >: Lower`, mixed bounds)
- Union types (`A | B`, Scala 3)
- Intersection types (`A & B`, Scala 3)
- Opaque type aliases (Scala 3) — zero-cost abstractions with compile-time-only type distinctions
- **Named tuples** (`(name = "Ann", age = 30)`, stabilized around Scala 3.7–3.8) — tuples with named field access
- Match types (type-level pattern matching, Scala 3)
- Dependent function types
- Higher-kinded types (`F[_]`)
- Self types
- Existential types (mostly legacy, Scala 2 interop)
- Path-dependent types

## 8. Contextual Abstraction (Scala 3) / Implicits (Scala 2)
- `given` / `using` clauses (Scala 3) replacing Scala 2's `implicit`
- Context bounds (`[T: Ordering]`)
- **Context bounds now desugar to `given`, requiring `using`** (source change introduced in Scala 3.8)
- Extension methods (`extension (x: T) def ...`, Scala 3) replacing implicit classes
- Type classes and their idiomatic Scala 3 encoding via `given`/`using`
- Implicit conversions — `Conversion` trait, and the **new `into` keyword (SIP-71, preview in 3.8)** for fully-implicit `into` conversions
- Given imports (`import given`)
- Multiversal equality (`CanEqual`, `derives CanEqual`) for strict equality checks

## 9. Pattern Matching (Deep Dive)
- Literal, variable, wildcard (`_`), and typed patterns
- Constructor/case-class patterns (deconstruction)
- Tuple patterns
- Sequence patterns (`List(1, 2, _*)`)
- `@` binding patterns
- Guards
- `sealed` + exhaustiveness warnings
- Custom extractors (`unapply`, `unapplySeq`)
- **Strict-equality pattern matching (SIP-67, experimental as of 3.8)**

## 10. Collections
- Immutable vs mutable collections (`scala.collection.immutable` vs `.mutable`)
- `List`, `Vector`, `Seq`, `Array`, `Set`, `Map`, `Range`
- `Option`, `Some`, `None` — null-safety idiom
- `Either`, `Left`, `Right`
- `Try`, `Success`, `Failure`
- Common combinators: `map`, `flatMap`, `filter`, `fold`/`foldLeft`/`foldRight`, `reduce`, `zip`, `groupBy`, `sortBy`, `collect`
- Lazy collections (`LazyList`, formerly `Stream`)
- Iterators
- `for`-comprehensions over collections/monads (desugars to `map`/`flatMap`/`withFilter`)
- Builders and `CanBuildFrom`/factory-based collection construction

## 11. Functional Programming Concepts
- Immutability by default
- Function composition (`andThen`, `compose`)
- Currying
- Monads (informally: `Option`, `Either`, `Try`, `Future` as monadic types)
- Functors/applicatives (conceptual, often via cats/scalaz in practice)
- Partial functions (`PartialFunction`, `isDefinedAt`)
- Tail-call optimization
- Algebraic data types (ADTs) via sealed traits/enums + case classes

## 12. Concurrency & Asynchrony
- `Future` and `Promise` (`scala.concurrent`)
- `ExecutionContext`
- `Await` (blocking, generally discouraged)
- Actors (conceptual — Akka/Apache Pekko model)
- **Akka relicensed back to Apache 2.0** (after its BSL term expired) — open-source actor toolkit again; **Apache Pekko** as the community fork/continuation
- Parallel collections (`.par`, via `scala-parallel-collections` module)
- Java interop concurrency (`java.util.concurrent` usable directly)

## 13. Error Handling
- Exceptions (`try / catch / finally`, `throw`)
- `Option` for absence instead of `null`
- `Either` for recoverable errors with context
- `Try` for wrapping exception-prone code functionally
- **`runtimeChecked` (SIP-57, stabilized in Scala 3.8)** — safer, checked casts

## 14. Metaprogramming
- Macros (Scala 3 macros: `inline`, `quotes`/`splices`, compile-time reflection)
- `inline` methods and `inline` parameters
- Compile-time computation (`scala.compiletime` package)
- Mirrors and automatic type class derivation (`derives` clause)
- Scala 2 macros (legacy, whitebox/blackbox — largely superseded in Scala 3)

## 15. Modules & Project Structure
- Packages (`package` declarations, nested packages)
- Imports (`import`, wildcard `import pkg.*` in Scala 3 vs `pkg._` in Scala 2)
- Export clauses (`export`, Scala 3) — re-exporting members from another object/trait
- Package objects
- Visibility scoping (`private[pkg]`)
- Top-level definitions (Scala 3 allows defs/vals outside of any class/object)

## 16. Syntax Style (Scala 3 specific)
- Optional braces / significant indentation as an alternative to `{ }`
- New control-syntax (`if x then ... else ...`, `while x do ...`)
- **Single-line lambdas after `:`** (experimental as of 3.8) — more concise trailing-lambda syntax

## 17. Tooling & Ecosystem
- Build tools: sbt, Mill (with **Mill 1.0's GraalVM native launcher**), Gradle-Scala interop
- Testing frameworks (ScalaTest, munit, specs2 — conceptual)
- Metals (language server) — now with **MCP support** for AI-agent-driven compile/test/inspect workflows
- Dependency management (Maven Central coordinates, `%%` cross-versioning operator)
- Scaladoc generation
- REPL improvements (**Scala 3.8's REPL rewrite** using `fansi` + `pprint`, shipped as a separate artifact)

## 18. Interop & Alternate Backends
- Scala.js — compiling Scala to JavaScript
- Scala Native — compiling to native code via LLVM, with C interop
- Java interoperability (using Java libraries, Java generics/collections bridging)

---

### Notes on the current Scala release line
As of writing, **Scala 3.8.x** is the current "Next" release (bringing a Scala-3-compiled standard library, stabilized Better Fors and `runtimeChecked`, and a JDK 17+ requirement), and **Scala 3.9** is the newly-frozen **LTS** release succeeding **Scala 3.3 LTS** — it carries forward everything shipped in the Next line since 3.3 (named tuples, Better Fors, the new REPL, trait parameters, export clauses, and more) as a stable target for production use. Scala 3.3 LTS continues to receive support for a transition period. Several items above (strict-equality matching, flexible varargs, `into` conversions, single-line lambdas after `:`) were still **experimental/preview** features as of Scala 3.8 and worth double-checking against the release notes before relying on them in production code.
