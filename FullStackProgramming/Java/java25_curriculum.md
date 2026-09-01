# Java 25 Beginner Curriculum

## Learning Goals
By the end of this curriculum, you can: write clean idiomatic Java 25 code, model real-world problems with OOP, handle exceptions gracefully, and understand where Java 25 makes things simpler than older versions. By Module 13, you will also understand Java's modern concurrency model including virtual threads and structured concurrency.

---

## Module 1 — Getting Started with Java 25

**Topics:**
- Installing JDK 25, setting up IntelliJ IDEA or VS Code
- Your first program — Java 25's new compact source files mean you no longer need a public class wrapper for simple scripts (JEP 512)
- How the JVM works: compile → bytecode → run
- Primitive types, variables, `var` keyword
- Reading input with `Scanner`

**Java 25 highlight:** With compact source files, a beginner's first program is just:
```java
void main() {
    System.out.println("Hello, Java 25!");
}
```
No `public class`, no `public static void main(String[] args)` boilerplate. Much friendlier.

**Mini project:** Temperature converter (Celsius ↔ Fahrenheit) using `var` and `Scanner`.

---

## Module 2 — Control Flow and Methods

**Topics:**
- `if/else`, ternary operator
- `switch` expressions (the modern, arrow-form syntax — finalized since Java 14, used naturally in Java 25)
- `for`, `while`, `do-while` loops
- Defining methods, return types, parameters
- Method overloading

**Mini project:** Grade calculator that uses a `switch` expression to return a letter grade.

---

## Module 3 — Arrays and Collections

**Topics:**
- Arrays: declaration, iteration, `Arrays` utility class
- `ArrayList`, `LinkedList`, `HashMap`, `HashSet`
- Enhanced for-loop
- Iterators
- Choosing the right collection

**Mini project:** Student marks tracker — store, update, and print average scores using `ArrayList` and `HashMap`.

---

## Module 4 — Object-Oriented Programming: Classes and Objects

**Topics:**
- Defining classes, fields, constructors
- `this` keyword
- Getters and setters
- Instance vs. static members
- Java 25 Flexible Constructor Bodies — you can now validate or compute values *before* calling `super()`, which beginners will encounter in inheritance later

**Mini project:** `BankAccount` class with deposit, withdraw, and balance methods.

---

## Module 5 — Inheritance

**Topics:**
- `extends` keyword
- Method overriding vs overloading
- `super` keyword — and how Java 25's flexible constructor bodies change what you can do before `super()`
- `final` classes and methods
- The `Object` class: `toString()`, `equals()`, `hashCode()`

**Mini project:** `SavingsAccount` and `CurrentAccount` extending `BankAccount` with account-specific behavior.

---

## Module 6 — Polymorphism

**Topics:**
- Compile-time polymorphism (overloading)
- Runtime polymorphism (overriding + dynamic dispatch)
- Upcasting and downcasting
- `instanceof` — classic usage, then the modern pattern matching form:
  ```java
  if (account instanceof SavingsAccount sa) {
      sa.applyInterest();
  }
  ```
- Why polymorphism matters: writing code against interfaces and base types

**Java 25 highlight:** Primitive types in patterns (`instanceof` and `switch`) are in preview in Java 25, letting you write uniform pattern matching across both reference and primitive types. Not required for beginners, but worth noting as the language direction.

**Mini project:** `PaymentProcessor` that accepts a list of `Payment` objects (subtypes: `CreditCard`, `UPI`, `NetBanking`) and dispatches correctly using polymorphism.

---

## Module 7 — Interfaces and Abstract Classes

**Topics:**
- `abstract` classes and methods
- Interfaces: defining and implementing
- Default and static methods in interfaces
- When to use abstract class vs interface
- Multiple interface implementation

**Mini project:** `Taxable` and `Discountable` interfaces applied to a `Product` hierarchy in a shopping cart.

---

## Module 8 — Exception Handling

**Topics:**
- What exceptions are and why they exist
- The exception hierarchy: `Throwable → Exception → RuntimeException`
- Checked vs unchecked exceptions
- `try / catch / finally`
- `try-with-resources` (for `AutoCloseable` — essential for file and DB work)
- Throwing exceptions: `throw` and `throws`
- Creating custom exception classes
- Multi-catch blocks: `catch (IOException | SQLException e)`
- Best practices: don't swallow exceptions, log meaningfully, fail fast

**Common mistakes to teach explicitly:**
- Catching `Exception` broadly and ignoring it
- Using exceptions for control flow
- Not closing resources

**Mini project:** File-based student record reader with custom `InvalidRecordException`, proper `try-with-resources`, and meaningful error messages.

---

## Module 9 — Records and Sealed Classes (Modern Java OOP)

**Topics:**
- `record` — immutable data carriers, concise syntax, auto-generated `equals`/`hashCode`/`toString`
- When to use records vs regular classes
- `sealed` classes and `permits` — restricting inheritance hierarchies
- Pattern matching with `switch` over sealed types (very idiomatic Java 25)

```java
sealed interface Shape permits Circle, Rectangle, Triangle {}

String describe(Shape s) {
    return switch (s) {
        case Circle c    -> "Circle with radius " + c.radius();
        case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
        case Triangle t  -> "Triangle";
    };
}
```

**Mini project:** A `Shape` hierarchy using sealed interfaces + records, with an area calculator using pattern matching switch.

---

## Module 10 — Generics

**Topics:**
- Why generics exist (type safety without casting)
- Generic classes and methods
- Bounded type parameters: `<T extends Comparable<T>>`
- Wildcards: `? extends` and `? super`
- Generic collections in practice

**Mini project:** Generic `Pair<A, B>` class and a generic `findMax` utility method.

---

## Module 11 — Functional Programming with Lambdas and Streams

**Topics:**
- Functional interfaces: `Predicate`, `Function`, `Consumer`, `Supplier`
- Lambda expressions
- Method references
- `Optional<T>` — avoiding null pointer exceptions
- Stream API: `filter`, `map`, `reduce`, `collect`, `sorted`

**Mini project:** Rewrite the student marks tracker from Module 3 using Streams — find top scorers, average, filter by pass/fail.

---

## Module 12 — File I/O and Modern APIs

**Topics:**
- `java.nio.file`: `Path`, `Files` utility class
- Reading and writing text files
- Serialization basics
- Introduction to `java.net.http.HttpClient` (modern HTTP client)

**Mini project:** Export student records to a CSV file, then re-read and parse them.

---

## Capstone Project (After Module 12)

Build a **Mini Banking CLI Application** that uses everything learned:

- Class hierarchy: `Account → SavingsAccount / LoanAccount`
- Records for `Transaction`
- Sealed interface for `TransactionResult`
- Polymorphic method dispatch for interest calculation
- Custom exceptions (`InsufficientFundsException`, `AccountNotFoundException`)
- Stream-based reporting (monthly summary, largest transactions)
- File persistence for account data

---

## Module 13 — Concurrency and Project Loom

**Prerequisites:** Complete Modules 1–12, especially the Stream API (Module 11) and exception handling (Module 8). You should be comfortable with lambdas before starting this module.

**Suggested Time:** 2 weeks

---

### Part A — Classical Concurrency (Foundation)

Before Loom makes sense, you need to understand what problem it solves.

**Topics:**
- What is a thread? Process vs thread model
- Creating threads: `Thread` class and `Runnable`
- `ExecutorService` and thread pools (`Executors.newFixedThreadPool`)
- `Callable<T>` and `Future<T>` — getting results back from threads
- The core problems: race conditions, shared mutable state
- `synchronized` keyword and `volatile`
- Why platform threads are expensive — each one maps to an OS thread, so thousands of them is a problem

**Key insight to drive home:** Traditional Java concurrency works well up to hundreds of threads. Beyond that, you're fighting the OS scheduler and memory overhead. This is the wall Loom breaks through.

**Mini exercise:** Write a simple thread pool that fetches mock "prices" for 10 stocks concurrently using `ExecutorService` and `Future`.

---

### Part B — Virtual Threads (Project Loom Core)

**Topics:**
- What virtual threads are: lightweight threads managed by the JVM, not the OS
- Creating virtual threads:
  ```java
  Thread vt = Thread.ofVirtual().start(() -> {
      System.out.println("Running on: " + Thread.currentThread());
  });
  ```
- `Executors.newVirtualThreadPerTaskExecutor()` — the idiomatic way to use virtual threads in a pool
- How virtual threads are mounted/unmounted on carrier threads (conceptual understanding, not internals)
- When to use virtual threads: I/O-bound tasks (HTTP calls, DB queries, file reads) — not CPU-bound work
- What doesn't change: `synchronized`, thread locals, existing APIs all still work

**Comparison exercise:** Run 10,000 tasks with a fixed thread pool vs a virtual thread executor. Observe the difference in behaviour and resource use.

**Mini project:** A mock API aggregator — fetch results from 5 "services" concurrently using virtual threads, collect and print results.

---

### Part C — Structured Concurrency (Java 25 Late Preview)

Classical concurrency with `Future` has a subtle problem: if one subtask fails, others keep running and you have to manually cancel and clean up. Structured concurrency fixes this.

**Topics:**
- The concept: a unit of work that fans out into subtasks should succeed or fail as a unit
- `StructuredTaskScope` — the main API
- `ShutdownOnFailure` — cancel remaining tasks if any subtask fails
- `ShutdownOnSuccess` — cancel remaining tasks as soon as one succeeds (useful for redundant calls)

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var userTask    = scope.fork(() -> fetchUser(userId));
    var accountTask = scope.fork(() -> fetchAccount(userId));

    scope.join().throwIfFailed();

    return new Dashboard(userTask.get(), accountTask.get());
}
```

- Error propagation — exceptions from subtasks surface cleanly to the parent
- Note: still in preview in Java 25 — understand the direction, avoid in production

**Mini project:** A dashboard loader that fetches a user profile, recent transactions, and account balance concurrently using `StructuredTaskScope.ShutdownOnFailure`. If any fetch fails, the whole operation fails cleanly.

---

### Part D — Scoped Values

**Topics:**
- The problem with `ThreadLocal`: mutable, inherited by child threads unpredictably, expensive at scale with virtual threads
- `ScopedValue` as the modern alternative: immutable, explicitly scoped, works cleanly with virtual threads
- Binding and reading a scoped value:
  ```java
  static final ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();

  ScopedValue.runWhere(CURRENT_USER, "alice", () -> {
      processRequest(); // CURRENT_USER.get() accessible anywhere in this call tree
  });
  ```
- When to use: passing contextual data (user ID, request ID, tenant ID) down a call stack without threading it through every method signature
- Finalized in Java 25 (JEP 506)

**Mini exercise:** Simulate a web request handler that passes a request ID through multiple service layers using `ScopedValue` instead of method parameters.

---

### Module 13 Capstone Extension

Extend the **Module 12 capstone Banking CLI** with a concurrency layer:

- Use `Executors.newVirtualThreadPerTaskExecutor()` to process a batch of 1,000 simulated transactions concurrently
- Use `StructuredTaskScope.ShutdownOnFailure()` to fetch account + fraud score concurrently before approving a transaction
- Use a `ScopedValue` to carry the current `sessionId` through the processing pipeline
- Handle `InterruptedException` and task failures with the custom exceptions from Module 8

---

### Key Mental Models for Module 13

| Concept | One-line summary |
|---|---|
| Platform thread | One OS thread, expensive, limited to ~thousands |
| Virtual thread | JVM-managed, cheap, millions possible, best for I/O |
| Structured concurrency | Fan out subtasks, fail/succeed as a unit |
| Scoped value | Immutable context passed down a call tree, Loom-friendly |

---

### What to Avoid Teaching at This Stage

- `ReentrantLock`, `Semaphore`, `CountDownLatch` — useful, but introduce friction before the student is ready
- `CompletableFuture` chains — powerful but the callback style is harder to reason about than structured concurrency
- `ForkJoinPool` internals — relevant for CPU-bound parallelism, a separate topic

These are worth a Module 14 later, but not here.

---

## Suggested Pacing (Full Curriculum)

| Module | Topic | Suggested Time |
|--------|-------|---------------|
| 1–3 | Basics, Control Flow, Collections | Week 1–2 |
| 4–6 | OOP: Classes, Inheritance, Polymorphism | Week 3–4 |
| 7–8 | Interfaces, Exception Handling | Week 5 |
| 9–10 | Records, Sealed Classes, Generics | Week 6 |
| 11–12 | Streams, File I/O | Week 7 |
| Capstone | Mini Banking CLI | Week 8 |
| 13 | Concurrency and Project Loom | Week 9–10 |

---

## Recommended Resources

- **Official:** [dev.java](https://dev.java) — Oracle's official learning portal, updated for Java 25
- **Book:** *Head First Java* (3rd edition covers modern Java well for beginners)
- **Concurrency deep-dive:** *Java Concurrency in Practice* by Brian Goetz (classic, still relevant)
- **Practice:** Exercism.io Java track, LeetCode Easy problems in Java
- **Tooling:** IntelliJ IDEA Community Edition (free, excellent for beginners)

---

## Curriculum Design Notes

The curriculum deliberately front-loads the OOP core (Modules 4–8) because polymorphism and exception handling are where most beginners hit conceptual walls. The Java 25 features (compact source files, flexible constructors, records, sealed classes, pattern matching) are woven in naturally rather than treated as add-ons — they represent how idiomatic modern Java is actually written.

Module 13 is positioned after the capstone intentionally. Concurrency is not a beginner topic; it requires comfort with lambdas, exceptions, and object design before it makes sense. Students who complete Module 12 and the capstone have exactly the foundation needed to appreciate what Loom solves and why structured concurrency is a significant improvement over raw `Future`-based code.
