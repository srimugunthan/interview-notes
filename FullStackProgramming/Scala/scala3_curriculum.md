# Scala 3 Beginner Curriculum

> **Version anchor:** This curriculum targets **Scala 3.8.x (Scala Next)** and is compatible with
> the upcoming **Scala 3.9 LTS** (expected Q2 2026). All examples use Scala 3 syntax — indentation-based
> blocks, `enum`, `given`/`using`, and modern `match` expressions. Scala 2 syntax is noted only where
> the difference would confuse a beginner reading older tutorials.

---

## Learning Goals

By the end of this curriculum, you can:
- Write clean, idiomatic Scala 3 code using both OOP and functional styles
- Model domains with case classes, sealed traits, and enums
- Handle errors without null or unchecked exceptions
- Use pattern matching fluently
- Work with collections using higher-order functions and for-comprehensions
- Understand Scala's type system well enough to read library code

---

## Module 1 — Getting Started with Scala 3

**Topics:**
- Installing Scala via Coursier (`cs setup`) and sbt or scala-cli
- Running your first program — Scala 3 supports a simple top-level `@main` entry point
- The REPL: `scala` interactive shell for experimentation
- `val` vs `var` — immutability is the default in Scala; prefer `val`
- Basic types: `Int`, `Double`, `Boolean`, `String`, `Char`
- String interpolation: `s"Hello, $name"`
- Type inference — Scala infers types, but explicit annotations are fine and recommended for learning

**Scala 3 highlight:** No boilerplate entry point needed:
```scala
@main def hello(): Unit =
  println("Hello, Scala 3!")
```
Indentation replaces braces — Python-like, but statically typed.

**Mini project:** Unit converter (km ↔ miles, kg ↔ lbs) using `val`, string interpolation, and `@main`.

---

## Module 2 — Control Flow and Functions

**Topics:**
- `if/else` as an expression (returns a value — everything in Scala is an expression)
- `match` expressions — Scala's supercharged switch (introduced properly here, deepened in Module 8)
- `while` loops — used sparingly in idiomatic Scala
- Defining functions with `def`
- Default and named parameters
- Pure functions — functions with no side effects; the Scala ideal
- Recursion and tail recursion (`@tailrec`)

**Key mindset shift from Java:** `if/else` and `match` return values. You assign them:
```scala
val label = if score >= 50 then "Pass" else "Fail"
```

**Mini project:** Grade calculator using a `match` expression that returns a letter grade, written as a pure function.

---

## Module 3 — Collections

**Topics:**
- `List`, `Vector`, `Map`, `Set` — all immutable by default
- `ArrayBuffer`, `mutable.Map` — mutable variants (use consciously)
- `head`, `tail`, `::` (cons), `:::` (concatenation)
- Higher-order functions: `map`, `filter`, `foldLeft`, `foreach`, `flatMap`
- `for` comprehensions — Scala's readable syntax for chaining `map` and `flatMap`
- Tuples: `(Int, String)`, `._1`, `._2`
- `Range`: `1 to 10`, `1 until 10`

**For comprehension example:**
```scala
val evens = for
  i <- 1 to 20
  if i % 2 == 0
yield i
```

**Key insight:** Scala collections are immutable and operations return new collections. No in-place mutation.

**Mini project:** Student marks processor — compute average, top scorer, and pass/fail split using `map`, `filter`, and `foldLeft` on a `List[Int]`.

---

## Module 4 — Object-Oriented Programming: Classes and Objects

**Topics:**
- Defining classes with constructor parameters
- `val` fields vs `var` fields in constructors
- Methods inside classes
- Companion objects — Scala's replacement for static members
- `object` keyword for singletons
- `apply` method — enables `MyClass(args)` without `new`
- `private` and `protected` access modifiers
- `this`

**Scala 3 example:**
```scala
class BankAccount(val owner: String, private var balance: Double):
  def deposit(amount: Double): Unit = balance += amount
  def withdraw(amount: Double): Unit =
    if amount <= balance then balance -= amount
    else throw IllegalArgumentException("Insufficient funds")
  def getBalance: Double = balance
```

**Mini project:** `BankAccount` class with companion object `apply` factory, deposit, withdraw, and balance.

---

## Module 5 — Case Classes and Immutable Data Modeling

**Topics:**
- `case class` — immutable data carriers with auto-generated `equals`, `hashCode`, `toString`, `copy`
- Why case classes are preferred over regular classes for data
- `copy` for creating modified versions: `alice.copy(age = 31)`
- `case object` for singleton cases
- Destructuring in `val` assignments:
  ```scala
  val Person(name, age) = Person("Alice", 30)
  ```
- Nested case class destructuring

**Scala highlight:** Case classes are Scala's answer to Java records — but more powerful because they integrate deeply with pattern matching.

```scala
case class Transaction(id: String, amount: Double, currency: String)

val tx = Transaction("T001", 250.0, "INR")
val adjusted = tx.copy(amount = 300.0)
```

**Mini project:** Model an e-commerce order system with `case class Order`, `case class Item`, and `case class Customer`. Use `copy` to apply a discount.

---

## Module 6 — Traits and Inheritance

**Topics:**
- `trait` — Scala's interface (can have concrete methods and fields)
- Mixing in multiple traits with `extends` and `with`
- `abstract` classes — when to use over traits (constructor parameters)
- Method overriding with `override`
- `super` keyword
- Trait linearization — how Scala resolves multiple inheritance
- `final` to prevent overriding

**Traits vs Java interfaces:** Scala traits can have state and implementation, making them more powerful than Java interfaces but requiring care with linearization order.

```scala
trait Printable:
  def print(): Unit = println(toString)

trait Taxable:
  def taxRate: Double
  def tax(amount: Double): Double = amount * taxRate

case class Product(name: String, price: Double) extends Printable, Taxable:
  def taxRate = 0.18
```

**Mini project:** `Printable`, `Taxable`, and `Discountable` traits applied to a `Product` hierarchy.

---

## Module 7 — Polymorphism and Type Hierarchies

**Topics:**
- Runtime polymorphism via trait/abstract class method dispatch
- `sealed trait` — restricts subtypes to the same file, enables exhaustive matching
- Enum in Scala 3 — clean syntax for algebraic data types:
  ```scala
  enum Color:
    case Red, Green, Blue
  ```
- Enums with parameters:
  ```scala
  enum Shape:
    case Circle(radius: Double)
    case Rectangle(width: Double, height: Double)
  ```
- `isInstanceOf` and `asInstanceOf` — used sparingly; pattern matching is preferred
- `Any`, `AnyVal`, `AnyRef` — Scala's type hierarchy root
- `Nothing` and `Unit`

**Key insight:** Sealed traits + case classes form **Algebraic Data Types (ADTs)** — the idiomatic Scala way to model "one of these possibilities." This replaces the Java subtype + instanceof pattern.

**Mini project:** `Shape` sealed trait with `Circle`, `Rectangle`, `Triangle` case classes. Area calculator using pattern matching on the sealed type.

---

## Module 8 — Pattern Matching (Deep Dive)

**Topics:**
- Match on values, types, and structures
- Destructuring in `match`:
  ```scala
  person match
    case Person(name, age) if age >= 18 => s"$name is an adult"
    case Person(name, _)                => s"$name is a minor"
  ```
- Guard clauses with `if` inside `case`
- Wildcard `_` and variable binding
- Nested pattern matching
- Pattern matching in `for` comprehensions
- `@` binding: match a pattern and bind the whole value
- Exhaustiveness checking — the compiler warns if you miss a case on a sealed type

**Scala 3 highlight:** The compiler statically checks that `match` on a `sealed trait` covers all cases. This turns runtime bugs into compile-time errors.

**Mini project:** Notification dispatcher — `sealed trait Notification` with `Email`, `SMS`, `PushAlert` subtypes. Pattern match to format each type differently, using guards and destructuring.

---

## Module 9 — Error Handling the Scala Way

**Topics:**
- Why `null` is dangerous and how Scala discourages it
- `Option[A]`: `Some(value)` and `None` — the safe alternative to null
- `map`, `flatMap`, `getOrElse`, `fold` on `Option`
- `Either[L, R]`: `Left(error)` and `Right(value)` — typed error handling
- `Try[A]`: `Success(value)` and `Failure(exception)` — wrapping exceptions
- Chaining with `for` comprehensions over `Option` and `Either`
- When to use each: `Option` for absence, `Either` for recoverable errors, `Try` for exception-throwing code

```scala
def divide(a: Int, b: Int): Either[String, Int] =
  if b == 0 then Left("Division by zero") else Right(a / b)

val result = for
  x <- divide(10, 2)
  y <- divide(x, 0)
yield y
// result: Left("Division by zero")
```

- Traditional `try/catch/finally` — still available and needed at system boundaries
- Custom exception classes (extending `Exception`)

**Key mindset shift from Java:** In Scala, you model errors in the type system using `Option`/`Either`/`Try`. Exceptions are for truly exceptional, unrecoverable conditions — not for normal control flow.

**Mini project:** Safe student record parser — read records from a `List[String]`, return `Either[ParseError, StudentRecord]` for each line, separate successes from failures.

---

## Module 10 — Generics and Type Parameters

**Topics:**
- Generic classes: `class Box[A](value: A)`
- Generic methods: `def identity[A](x: A): A = x`
- Type bounds: `[A <: Animal]` (upper), `[A >: Dog]` (lower)
- Variance: covariance `[+A]` and contravariance `[-A]` — conceptual understanding
- Generic collections in practice
- `Ordering[A]` for comparison-based generics

**Variance intuition:** A `List[Dog]` is a `List[Animal]` if `List` is covariant (`List[+A]`). Scala's built-in `List` is covariant. Understanding this helps you read library error messages.

**Mini project:** Generic `Pair[A, B]` class with `swap` method, and a generic `findMax[A: Ordering]` function.

---

## Module 11 — Functional Programming Patterns

**Topics:**
- Functions as first-class values: assign, pass, return
- Anonymous functions (lambdas): `x => x * 2`
- Partial application and currying: `def add(x: Int)(y: Int): Int = x + y`
- Function composition: `f andThen g`, `f compose g`
- Higher-order functions revisited: writing your own `map`, `filter`, `fold`
- `LazyList` — infinite lazy sequences
- Immutability discipline: building up state with recursion and `foldLeft` instead of mutation

```scala
val double: Int => Int = _ * 2
val addOne: Int => Int = _ + 1
val doubleThenAdd = double andThen addOne
doubleThenAdd(5) // 11
```

**Mini project:** A data pipeline — take a list of raw transaction strings, parse them, filter invalid ones, apply a currency conversion, and produce a summary report — all using function composition and `for` comprehensions.

---

## Module 12 — Type Classes and `given`/`using` (Scala 3 Implicits)

**Topics:**
- What a type class is: a pattern for adding behaviour to types without modifying them
- Defining a type class as a trait: `trait Serializable[A]`
- Providing instances with `given`:
  ```scala
  given Serializable[Int] with
    def serialize(x: Int): String = x.toString
  ```
- Using instances with `using`:
  ```scala
  def serialize[A](value: A)(using s: Serializable[A]): String = s.serialize(value)
  ```
- Context bounds as shorthand: `[A: Serializable]`
- Built-in type classes: `Ordering`, `Numeric`, `CanEqual`
- Extension methods in Scala 3:
  ```scala
  extension (s: String)
    def shout: String = s.toUpperCase + "!"
  ```

**Why this matters:** Type classes are how Scala libraries like Cats and Spark add behaviour. Understanding `given`/`using` unlocks reading and using the ecosystem.

**Mini project:** `Printable[A]` type class with instances for `Int`, `String`, and a custom `Product` case class. A generic `printAll[A: Printable](items: List[A])` function.

---

## Module 13 — Collections Deep Dive and For-Comprehensions

**Topics:**
- `List` vs `Vector` vs `LazyList` — when to use each
- `Map` operations: `get`, `getOrElse`, `updated`, `removed`, `groupBy`
- `Set` operations: union, intersection, difference
- `collect` — `map` + `filter` combined using partial functions
- `zip`, `unzip`, `zipWithIndex`
- `partition`, `span`, `groupBy`
- For-comprehensions over multiple collections (cartesian products)
- For-comprehensions over `Option`, `Either`, `Try` — monadic chaining
- Performance characteristics: why `List` prepend is O(1) but append is O(n)

```scala
val students = List("Alice" -> 85, "Bob" -> 42, "Charlie" -> 91)
val topStudents = students.collect { case (name, score) if score >= 80 => name }
```

**Mini project:** Course enrollment system — map students to courses, compute per-course averages, find students enrolled in multiple courses, using `groupBy`, `mapValues`, and `collect`.

---

## Module 14 — File I/O, sbt, and Project Structure

**Topics:**
- sbt basics: `build.sbt`, `project/`, adding dependencies
- scala-cli as a lightweight alternative for scripts
- Reading and writing files with `scala.io.Source` and `java.nio.file`
- Working with JSON using the `circe` library (a popular Scala JSON library)
- Basic HTTP with `sttp` or `requests-scala`
- Project layout conventions: `src/main/scala`, `src/test/scala`
- Unit testing with `munit` or `ScalaTest` — write and run a test suite

**Mini project:** Read a JSON file of transactions, parse into case classes using circe, compute a summary report, and write results to a CSV file.

---

## Capstone Project (After Module 14)

Build a **Functional Banking CLI** that demonstrates the full Scala 3 stack:

- Domain model: `sealed trait Account` with `SavingsAccount`, `CurrentAccount`, `LoanAccount` case classes
- `Transaction` as a case class; `TransactionResult` as an `Either[BankingError, Transaction]`
- All business logic as pure functions — no mutation
- Error handling via `Either` and `Option` throughout — no thrown exceptions in business logic
- `for` comprehensions to chain operations: validate → check balance → apply → record
- Type class `Auditable[A]` to generate audit log entries for any domain event
- Collection-based reporting with `groupBy`, `foldLeft`, and `map`
- File persistence — serialize account state to JSON using circe
- sbt project with a passing `munit` test suite

---

## Module 15 — Concurrency with Scala (Futures and Beyond)

**Prerequisites:** Complete Modules 1–14 and the capstone. Comfort with `for` comprehensions over monadic types is essential.

**Suggested Time:** 2 weeks

---

### Part A — Futures (Standard Concurrency)

**Topics:**
- `Future[A]` — represents a value that will be available asynchronously
- `ExecutionContext` — the thread pool that runs futures
- Creating futures: `Future { ... }`
- Transforming futures: `map`, `flatMap`, `for` comprehensions
- Combining futures: `Future.sequence`, `zip`
- Error handling in futures: `recover`, `recoverWith`, `failed`
- `Await.result` — blocking for tests and scripts (avoid in production)
- Why futures have limitations: no cancellation, callback-based under the hood

```scala
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

val priceF: Future[Double] = Future { fetchPrice("AAPL") }
val rateF: Future[Double]  = Future { fetchExchangeRate("USD", "INR") }

val resultF = for
  price <- priceF
  rate  <- rateF
yield price * rate
```

**Mini project:** Concurrent stock price aggregator — fetch prices for 5 tickers using `Future`, combine results, handle failures gracefully with `recover`.

---

### Part B — Virtual Threads via Java Interop

Since Scala runs on the JVM, it gets Java 25's virtual threads for free.

**Topics:**
- Using `Thread.ofVirtual()` from Scala code
- `Executors.newVirtualThreadPerTaskExecutor()` as an `ExecutionContext`
- When to prefer virtual threads over standard `Future` with a fixed pool: high-concurrency I/O
- Mixing Scala futures with a virtual-thread executor

```scala
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

val vtExecutor = Executors.newVirtualThreadPerTaskExecutor()
given ExecutionContext = ExecutionContext.fromExecutor(vtExecutor)

val result = Future { callExternalService() }
```

**Key insight:** You get the benefit of Java 25 virtual threads with zero extra library — just plug in the executor.

---

### Part C — Introduction to Cats Effect (Modern Scala Concurrency)

For production Scala, the ecosystem has converged on **Cats Effect** (`IO`) as the standard concurrency model. This part is an introduction, not a deep dive.

**Topics:**
- Why `Future` is insufficient for large systems: eager evaluation, no cancellation, implicit `ExecutionContext` threading
- `IO[A]` — a description of an effect, not its execution
- `IO.pure`, `IO.delay`, `IO.println`
- Running an `IO`: `unsafeRunSync` (entry point only), `IOApp`
- Composing `IO` with `map`, `flatMap`, `for` comprehensions
- `IO.parMapN` — parallel execution
- Fibers — Cats Effect's equivalent of virtual threads, but purely functional

```scala
import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  def run: IO[Unit] =
    for
      _ <- IO.println("Fetching data...")
      x <- IO.delay(fetchFromDB())
      _ <- IO.println(s"Got: $x")
    yield ()
```

**Note:** Cats Effect is a large ecosystem. This module introduces the mental model. Full mastery is a separate learning path.

**Mini project:** Rewrite the concurrent stock aggregator using `IO` and `IO.parMapN` for parallel fetches.

---

### Key Mental Models for Module 15

| Concept | One-line summary |
|---|---|
| `Future[A]` | Async value, eager, standard library |
| Virtual thread executor | Java 25 lightweight threads plugged into `ExecutionContext` |
| `IO[A]` (Cats Effect) | Lazy description of an effect, composable, cancellable |
| Fiber | Cats Effect's lightweight green thread |

---

### What to Avoid Teaching at This Stage

- Akka actors — a large framework with its own mental model; better after Cats Effect foundations
- `ZIO` — a competing effect system; worth learning later, not at this stage
- Raw `java.util.concurrent` locks and semaphores — available but not idiomatic Scala

---

## Suggested Pacing (Full Curriculum)

| Module | Topic | Suggested Time |
|--------|-------|---------------|
| 1–3 | Basics, Control Flow, Collections | Week 1–2 |
| 4–6 | Case Classes, Traits, OOP | Week 3–4 |
| 7–8 | Polymorphism, Pattern Matching | Week 5 |
| 9–10 | Error Handling, Generics | Week 6 |
| 11–12 | FP Patterns, Type Classes | Week 7 |
| 13–14 | Collections Deep Dive, I/O, sbt | Week 8 |
| Capstone | Functional Banking CLI | Week 9 |
| 15 | Concurrency: Futures, Cats Effect | Week 10–11 |

---

## How Scala Differs from Java — Key Mindset Shifts

| Java habit | Scala idiom |
|---|---|
| `null` for absence | `Option[A]` |
| Exceptions for errors | `Either[Error, A]` or `Try[A]` |
| Mutable state + loops | Immutable values + recursion/`foldLeft` |
| `instanceof` + cast | Pattern matching |
| `static` members | Companion `object` |
| Checked exceptions | Encoded in return type |
| Verbose getters/setters | `case class` fields are public `val` by default |

---

## Recommended Resources

- **Official:** [docs.scala-lang.org](https://docs.scala-lang.org) — Scala 3 Book is the best free starting point
- **Book:** *Programming in Scala* by Martin Odersky (5th edition covers Scala 3)
- **Book:** *Scala with Cats* (free online) — for the type class and Cats Effect path
- **Practice:** Exercism.io Scala track, Advent of Code in Scala
- **Tooling:** IntelliJ IDEA with the Scala plugin, or VS Code with Metals
- **Build tool:** scala-cli for scripts and small projects; sbt for larger ones

---

## Curriculum Design Notes

This curriculum is structured to exploit Scala's strengths rather than teach it as "Java with different syntax." The OOP modules (4–7) are intentionally brief because Scala's real power is in the combination of OOP and functional programming. Pattern matching (Module 8) and error handling (Module 9) are given full modules because they represent the biggest conceptual shift for Java or Python developers.

Type classes (Module 12) are included because they are unavoidable in real Scala — every major library (`circe`, `cats`, `doobie`, `http4s`) uses `given`/`using`. Introducing them before the capstone means students can read library documentation without being blocked.

Concurrency (Module 15) follows the same philosophy as the Java curriculum — it presupposes comfort with monadic composition before introducing async execution.
