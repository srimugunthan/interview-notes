# Exhaustive Java Programming Concepts (up to Java 25)

A structured, cumulative list — basics first, then intermediate, advanced, and finally the features new as of Java 25 (LTS, released Sept 16, 2025). Java is backward-compatible, so this whole list is "Java 25" in the sense that all of it runs on a Java 25 JVM.

---

## 1. Setup & Fundamentals
- JDK vs JRE vs JVM
- `javac`, `java`, `jar`, `jshell` (REPL)
- Compilation model (bytecode, class files, classloading)
- `main` method signature and entry points
- **Compact Source Files & Instance Main Methods (JEP 512, finalized in Java 25)** — no `public class`, no `static`, no `String[] args` needed for simple single-file programs
- Comments (`//`, `/* */`, `/** */` Javadoc)
- Packages and the classpath vs module path

## 2. Variables & Data Types
- Primitive types: `byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`
- Reference types (objects, arrays)
- Literals (integer, floating-point, char, String, `_` underscores in numeric literals)
- Type casting: implicit (widening) vs explicit (narrowing)
- `var` (local variable type inference, since Java 10)
- Wrapper classes & autoboxing/unboxing
- Constants (`final`)
- Scope and lifetime of variables

## 3. Operators
- Arithmetic (`+ - * / %`)
- Relational (`== != > < >= <=`)
- Logical (`&& || !`)
- Bitwise (`& | ^ ~ << >> >>>`)
- Assignment (`= += -= *= /= %=` etc.)
- Ternary (`?:`)
- `instanceof` (including pattern-matching form)
- Operator precedence

## 4. Control Flow
- `if`, `if-else`, `if-else-if`
- `switch` statement (classic)
- **Switch expressions** (`->` arrow form, `yield`)
- **Pattern matching for `switch`** (finalized Java 21+)
- **Pattern matching for `instanceof`** (finalized Java 16)
- **Primitive types in patterns, `instanceof`, and `switch` (JEP 507, 3rd preview in Java 25)**
- `for` loop (classic three-part)
- Enhanced `for` (for-each)
- `while` loop
- `do-while` loop
- `break`, `continue`, labeled `break`/`continue`
- `return`

## 5. Arrays & Strings
- 1D and multidimensional arrays
- Array initialization, default values
- `java.util.Arrays` utility methods
- `String` immutability
- `StringBuilder` / `StringBuffer`
- String pool / interning
- Text blocks (`"""..."""`, finalized Java 15)
- String formatting (`String.format`, `printf`, `%` specifiers)
- String methods (`split`, `strip`, `repeat`, `isBlank`, `chars()`, etc.)

## 6. Object-Oriented Programming (OOP)
- Classes and objects
- Constructors (default, parameterized, overloading, constructor chaining `this()`/`super()`)
- **Flexible Constructor Bodies (JEP 513, finalized Java 25)** — statements before `super()`/`this()`
- Instance vs static members
- `this` keyword
- Encapsulation (access modifiers: `private`, `default`, `protected`, `public`)
- Inheritance (`extends`)
- Polymorphism (compile-time/overloading, runtime/overriding)
- `super` keyword
- Abstraction (`abstract` classes and methods)
- Interfaces (default methods, static methods, private interface methods)
- Multiple interface inheritance
- `final` classes, methods, variables
- Object class methods (`equals`, `hashCode`, `toString`, `clone`, `getClass`)
- Nested classes: static nested, inner (non-static), local, anonymous
- **Records** (`record`, finalized Java 16) — compact constructors, canonical constructors
- **Sealed classes/interfaces** (`sealed`, `permits`, `non-sealed`, finalized Java 17)
- Enums (basic, with fields/methods/constructors, abstract methods per constant)
- Annotations (built-in: `@Override`, `@Deprecated`, `@SuppressWarnings`, `@FunctionalInterface`; custom annotations, meta-annotations)

## 7. Exception Handling
- Checked vs unchecked exceptions
- `try-catch-finally`
- Multi-catch (`catch (A | B e)`)
- **Try-with-resources** and `AutoCloseable`
- Custom exceptions
- `throw` vs `throws`
- Exception chaining (`getCause`)
- Stack traces
- `Error` vs `Exception` hierarchy

## 8. Generics
- Generic classes and methods
- Bounded type parameters (`<T extends X>`)
- Wildcards (`?`, `? extends T`, `? super T`)
- Type erasure
- Generic interfaces
- Varargs (`...`)

## 9. Collections Framework
- `Collection`, `List`, `Set`, `Queue`, `Deque`, `Map` hierarchies
- `ArrayList`, `LinkedList`, `Vector`, `Stack`
- `HashSet`, `LinkedHashSet`, `TreeSet`
- `HashMap`, `LinkedHashMap`, `TreeMap`, `Hashtable`
- `PriorityQueue`, `ArrayDeque`
- Iterators (`Iterator`, `ListIterator`, fail-fast vs fail-safe)
- `Comparable` vs `Comparator`
- Immutable collections (`List.of`, `Map.of`, `Collections.unmodifiableX`)
- `Collections` utility class
- Concurrent collections (`ConcurrentHashMap`, `CopyOnWriteArrayList`, `BlockingQueue`)

## 10. Functional Programming
- Lambda expressions
- Functional interfaces (`Runnable`, `Supplier`, `Consumer`, `Function`, `Predicate`, `BiFunction`, etc.)
- Method references (`Class::method`, `instance::method`, `Class::new`)
- `java.util.function` package
- Stream API (`Stream`, `IntStream`, `LongStream`, `DoubleStream`)
  - Intermediate ops: `map`, `filter`, `sorted`, `distinct`, `flatMap`, `peek`, `limit`, `skip`
  - Terminal ops: `collect`, `forEach`, `reduce`, `count`, `anyMatch`, `allMatch`, `findFirst`
  - `Collectors` (`toList`, `toMap`, `groupingBy`, `partitioningBy`, `joining`)
- `Optional<T>`

## 11. Pattern Matching & Records (Modern Java)
- Record patterns (deconstruction, finalized Java 21)
- Nested record patterns
- Guarded patterns (`when` clause in `switch`)
- Exhaustiveness checking with sealed types
- **Stable Values (JEP 502, preview in Java 25)** — deferred, at-most-once-initialized immutable holders as a lighter alternative to `final` + lazy init

## 12. Concurrency & Multithreading
- `Thread` class, `Runnable` interface
- Thread lifecycle, `start()` vs `run()`
- Synchronization (`synchronized` blocks/methods, intrinsic locks)
- `wait()`, `notify()`, `notifyAll()`
- `volatile` keyword
- `java.util.concurrent` package (`ExecutorService`, thread pools, `Future`, `Callable`)
- `CompletableFuture`
- Locks (`ReentrantLock`, `ReadWriteLock`)
- Atomic classes (`AtomicInteger`, etc.)
- **Virtual Threads** (Project Loom, finalized Java 21) — lightweight threads for high-throughput concurrency
- **Structured Concurrency (JEP 505, 5th preview in Java 25)** — `StructuredTaskScope` for treating related tasks as one unit of work
- **Scoped Values (JEP 506, finalized in Java 25)** — immutable, thread-safe alternative to `ThreadLocal` for sharing data within a scope

## 13. I/O & NIO
- `java.io`: `File`, streams (`InputStream`/`OutputStream`, `Reader`/`Writer`), serialization
- `java.nio`: `Path`, `Files`, channels, buffers
- Try-with-resources for I/O
- Serialization (`Serializable`, `transient`, `serialVersionUID`)

## 14. Modules & Packaging
- Java Platform Module System (JPMS, `module-info.java`)
- `requires`, `exports`, `opens`, `provides...with`
- **Module Import Declarations (JEP 511, finalized in Java 25)** — `import module java.base;` style imports of everything a module exports
- JAR files, fat/uber JARs
- `jlink` (custom runtime images), `jpackage` (native installers)

## 15. Reflection & Metaprogramming
- `java.lang.reflect` (inspecting classes, methods, fields at runtime)
- Dynamic proxies
- Annotation processing
- `MethodHandles` / `invokedynamic`

## 16. Security & Cryptography APIs
- `java.security` basics
- **PEM Encodings of Cryptographic Objects (JEP 470, preview in Java 25)** — encode/decode keys, certs, CRLs to/from PEM format
- **Key Derivation Function API (JEP 510, finalized in Java 25)**
- New message digest algorithms (SHAKE128-256, SHAKE256-512)

## 17. Performance, JVM & Runtime (Advanced/Internals)
- Garbage collection basics (generational GC, GC algorithms: G1, ZGC, Shenandoah)
- **Generational Shenandoah (JEP 521, finalized in Java 25)**
- **Compact Object Headers (JEP 519, finalized/product feature in Java 25)** — smaller object headers, reduced memory footprint
- **Ahead-of-Time Method Profiling (JEP 515, Java 25)** and **Command-Line Ergonomics (JEP 514, Java 25)** — Project Leyden groundwork for faster startup
- **JFR Cooperative Sampling (JEP 518)** and **JFR Method Timing & Tracing (JEP 520)** — Java Flight Recorder observability improvements
- **JFR CPU-Time Profiling (JEP 509, experimental in Java 25)**
- **Vector API (JEP 508, 10th incubator round in Java 25)** — SIMD-style vector computation
- Class Data Sharing (CDS), Application CDS
- JIT compilation basics (C1/C2 tiers)
- Removed: 32-bit x86 port (JEP 503), the experimental Graal JIT compiler

## 18. Build Tools & Ecosystem
- Maven (POM, lifecycle, dependencies)
- Gradle (build scripts, tasks)
- Dependency management concepts
- Unit testing (JUnit 5, assertions, test lifecycle)
- Mocking (Mockito basics)
- Logging (SLF4J, java.util.logging)

## 19. Design & Best Practices (conceptual, language-adjacent)
- SOLID principles as applied in Java
- Common design patterns (Singleton, Factory, Builder, Observer, Strategy)
- Effective Java–style idioms (favor immutability, composition over inheritance)
- Null-safety patterns (`Optional`, `Objects.requireNonNull`)

---

### Notes on Java 25 specifically
Java 25 is an **LTS release** (support through ~2030). Of its ~18 JEPs, the notable **finalized/production** features are: Flexible Constructor Bodies, Module Import Declarations, Compact Source Files and Instance Main Methods, Scoped Values, Compact Object Headers (now a product feature), Key Derivation Function API, and Generational Shenandoah. The rest — Structured Concurrency, Primitive Types in Patterns, PEM Encodings, Stable Values, Vector API — remain in **preview/incubator** stages.
