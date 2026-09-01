# Python 3 Beginner Curriculum

> **Version anchor:** This curriculum targets **Python 3.14** (released October 7, 2025),
> the current stable release. Features introduced in 3.10–3.14 are highlighted where
> they meaningfully improve the beginner experience. All examples use modern Python idioms:
> f-strings, type hints, structural pattern matching, and dataclasses.

---

## Learning Goals

By the end of this curriculum, you can:
- Write clean, idiomatic Python 3.14 code
- Model real-world problems with functions, classes, and data structures
- Handle errors gracefully using exceptions and the `try/except` pattern
- Work with files, JSON, and external APIs
- Write and run automated tests
- Use Python's standard library confidently
- Understand async programming well enough to build basic concurrent programs

---

## Module 1 — Getting Started with Python 3.14

**Topics:**
- Installing Python 3.14 (python.org or `pyenv`)
- The REPL — Python 3.14's REPL has real-time syntax highlighting and smarter auto-completion
- Running scripts: `python script.py`
- `print()`, `input()`, comments
- Variables and assignment
- Built-in types: `int`, `float`, `str`, `bool`, `None`
- Type inference — Python is dynamically typed, but you can annotate
- f-strings: `f"Hello, {name}!"` — the standard since 3.6, essential from day one
- `type()` and `isinstance()`

**Python 3.14 highlight — t-strings (PEP 750):**
t-strings are a new `t""` prefix that returns a `Template` object instead of a plain string,
allowing safe, custom processing of interpolated values. Useful for SQL, HTML, and logging:
```python
from string.templatelib import Template

name = "Alice"
greeting = t"Hello, {name}!"
# greeting is a Template object, not a plain string — can be processed safely
```
Note: t-strings are an advanced feature. They are introduced here for awareness; the curriculum
uses f-strings throughout.

**Mini project:** Personal greeting card — ask the user for their name, age, and city, then
print a formatted summary using f-strings.

---

## Module 2 — Control Flow

**Topics:**
- `if / elif / else`
- Comparison operators: `==`, `!=`, `<`, `>`, `<=`, `>=`
- Boolean operators: `and`, `or`, `not`
- `while` loops and `break` / `continue`
- `for` loops over ranges and sequences
- `range(start, stop, step)`
- `pass` statement
- Ternary expression: `value_if_true if condition else value_if_false`

**Mini project:** Number guessing game — the program picks a random number, the user
guesses, and the program says "too high" / "too low" / "correct" using a `while` loop.

---

## Module 3 — Functions

**Topics:**
- Defining functions with `def`
- Parameters and return values
- Default parameter values
- Keyword arguments
- `*args` and `**kwargs`
- Docstrings: `"""Description of what this function does"""`
- Type hints: `def greet(name: str) -> str:`
- Scope: local vs global variables
- `lambda` functions for short anonymous expressions
- Pure functions — functions without side effects; prefer them where possible

**Type hints mindset:** Python doesn't enforce type hints at runtime, but they serve
as documentation and enable static analysis with tools like `mypy` and Pyright.

```python
def calculate_tax(amount: float, rate: float = 0.18) -> float:
    """Return the tax amount for a given principal and rate."""
    return amount * rate
```

**Mini project:** Calculator module — implement `add`, `subtract`, `multiply`, `divide`,
and `power` as pure functions with docstrings and type hints.

---

## Module 4 — Data Structures

**Topics:**
- `list` — ordered, mutable: `[1, 2, 3]`
- `tuple` — ordered, immutable: `(1, 2, 3)`
- `dict` — key-value store: `{"name": "Alice", "age": 30}`
- `set` — unordered, unique elements: `{1, 2, 3}`
- Common operations: `append`, `pop`, `insert`, `remove`, `update`, `keys`, `values`, `items`
- List comprehensions: `[x * 2 for x in range(10) if x % 2 == 0]`
- Dict comprehensions: `{k: v for k, v in pairs}`
- Set comprehensions
- Unpacking: `a, b, c = [1, 2, 3]`
- Slicing: `my_list[1:4]`, `my_list[::-1]`
- Nested data structures

**List comprehension vs loop:** List comprehensions are idiomatic Python for building
lists. Prefer them for simple transformations; use a regular loop when logic is complex.

**Mini project:** Student grade book — store student names and scores in a dict,
compute average, find top scorer, and list students who passed — all using comprehensions.

---

## Module 5 — Strings and Text Processing

**Topics:**
- String methods: `upper`, `lower`, `strip`, `split`, `join`, `replace`, `find`, `startswith`, `endswith`
- Multi-line strings with triple quotes
- String formatting: f-strings (primary), `.format()` (legacy awareness)
- Raw strings: `r"C:\Users\name"` — important for file paths and regex
- `str.encode()` / `bytes.decode()` — basics of encoding
- Introduction to `re` module: `re.search`, `re.findall`, `re.sub`
- Common regex patterns: digits `\d+`, words `\w+`, email matching

**Mini project:** Text analyser — given a paragraph of text, count words, find the most
frequent word, replace all occurrences of a target word, and extract all email addresses
using regex.

---

## Module 6 — Object-Oriented Programming: Classes

**Topics:**
- Defining classes with `class`
- `__init__` constructor
- Instance attributes and methods
- `self` keyword
- Class attributes vs instance attributes
- `__str__` and `__repr__` dunder methods
- `@property` decorator for computed attributes
- `@staticmethod` and `@classmethod`
- Private by convention: `_protected`, `__mangled`
- Object identity: `is` vs `==`

```python
class BankAccount:
    def __init__(self, owner: str, balance: float = 0.0) -> None:
        self.owner = owner
        self._balance = balance

    @property
    def balance(self) -> float:
        return self._balance

    def deposit(self, amount: float) -> None:
        if amount <= 0:
            raise ValueError("Deposit amount must be positive")
        self._balance += amount

    def __str__(self) -> str:
        return f"BankAccount({self.owner}, {self._balance:.2f})"
```

**Mini project:** `BankAccount` class with deposit, withdraw, transaction history,
and a `summary()` method.

---

## Module 7 — Inheritance and Polymorphism

**Topics:**
- `class Child(Parent):` — inheritance syntax
- Overriding methods
- `super()` — calling the parent implementation
- `isinstance()` and `issubclass()`
- Multiple inheritance and MRO (Method Resolution Order)
- Abstract base classes with `abc.ABC` and `@abstractmethod`
- Duck typing — "if it walks like a duck and quacks like a duck..."
- Protocols (structural subtyping) introduced in Python 3.8, used idiomatically in 3.14

**Duck typing vs inheritance:** Python polymorphism is primarily duck-typed. An object
doesn't need to inherit from a base class to be used polymorphically — it just needs the
right methods. This is a significant difference from Java and Scala.

```python
from abc import ABC, abstractmethod

class Account(ABC):
    @abstractmethod
    def apply_interest(self) -> None: ...

class SavingsAccount(Account):
    def apply_interest(self) -> None:
        self._balance *= 1.04
```

**Mini project:** `Payment` hierarchy — `CreditCard`, `UPI`, and `NetBanking` classes
inheriting from an abstract `Payment` base. A `process_payment(p: Payment)` function
that works polymorphically with all three.

---

## Module 8 — Exception Handling

**Topics:**
- The exception hierarchy: `BaseException → Exception → ValueError / TypeError / etc.`
- `try / except / else / finally`
- Catching specific exceptions vs broad `except Exception`
- `raise` — throwing exceptions
- `raise ... from ...` — exception chaining (preserves original context)
- Creating custom exception classes
- `with` statement and context managers (`__enter__` / `__exit__`)
- Common built-in exceptions: `ValueError`, `TypeError`, `KeyError`, `IndexError`,
  `FileNotFoundError`, `AttributeError`, `StopIteration`

```python
class InsufficientFundsError(Exception):
    def __init__(self, amount: float, balance: float) -> None:
        super().__init__(f"Cannot withdraw {amount:.2f}, balance is {balance:.2f}")
        self.amount = amount
        self.balance = balance

def withdraw(account: BankAccount, amount: float) -> None:
    if amount > account.balance:
        raise InsufficientFundsError(amount, account.balance)
    account._balance -= amount
```

**Common mistakes to teach explicitly:**
- Catching `Exception` or `BaseException` without re-raising — silences real bugs
- Using exceptions for normal control flow
- Not using `with` for resources that need cleanup (files, connections)
- Bare `except:` without specifying the exception type

**Mini project:** File-based record reader — read student records from a CSV,
raise a custom `InvalidRecordError` for malformed lines, use `try/except/finally`
to ensure the file is always closed (then refactor to use `with`).

---

## Module 9 — Dataclasses and Structural Pattern Matching

**Topics:**
- `@dataclass` — auto-generate `__init__`, `__repr__`, `__eq__` from field declarations
- `field()` for defaults, `default_factory` for mutable defaults
- `frozen=True` for immutable dataclasses
- `@dataclass(slots=True)` for memory efficiency (Python 3.10+)
- Structural pattern matching with `match / case` (Python 3.10+, idiomatic in 3.14):
  ```python
  match command:
      case "quit":
          quit_game()
      case "go" | "move":
          move_player()
      case _:
          print("Unknown command")
  ```
- Matching on structure: sequences, mappings, class patterns
- Guard clauses: `case Point(x, y) if x > 0:`
- Exhaustiveness and the wildcard `_`

**Dataclasses vs regular classes:** `@dataclass` is the modern Python equivalent of
Java records or Scala case classes for pure data containers. Prefer it over writing
`__init__` and `__repr__` manually.

```python
from dataclasses import dataclass, field

@dataclass(frozen=True)
class Transaction:
    id: str
    amount: float
    currency: str = "INR"
    tags: list[str] = field(default_factory=list)
```

**Mini project:** `Shape` hierarchy using dataclasses (`Circle`, `Rectangle`, `Triangle`)
with an area calculator using structural pattern matching.

---

## Module 10 — Modules, Packages, and the Standard Library

**Topics:**
- `import module`, `from module import name`, `import module as alias`
- Writing your own modules and packages (`__init__.py`)
- The `if __name__ == "__main__":` guard
- Virtual environments: `python -m venv venv`, `pip install`
- `uv` — the modern, fast Python package manager (2024+, rapidly becoming standard)
- Key standard library modules:
  - `os` and `pathlib` — file system operations (prefer `pathlib` in 3.14)
  - `sys` — interpreter info and `argv`
  - `datetime` — dates and times
  - `collections` — `Counter`, `defaultdict`, `deque`, `namedtuple`
  - `itertools` — `chain`, `product`, `groupby`, `islice`
  - `functools` — `reduce`, `partial`, `lru_cache`, `cache`
  - `json` — encode/decode JSON
  - `random` — random number generation
  - `math` — mathematical functions
  - `typing` — `Optional`, `Union`, `Any`, `Callable`, `TypeVar`

**Mini project:** CLI expense tracker — read/write expenses to a JSON file using `pathlib`
and `json`, use `collections.Counter` to summarise by category, and package as a proper
module with `__init__.py`.

---

## Module 11 — File I/O and Data Formats

**Topics:**
- Reading and writing text files with `open()` and `with`
- File modes: `r`, `w`, `a`, `rb`, `wb`
- `pathlib.Path` — the modern, object-oriented path API
- Reading and writing CSV with the `csv` module
- JSON: `json.loads`, `json.dumps`, `json.load`, `json.dump`
- Working with environment variables: `os.environ`, `python-dotenv`
- Introduction to `dataclasses` + JSON serialisation pattern
- Basics of `sqlite3` — Python's built-in SQLite interface

**Mini project:** Student record system — store records in a JSON file, support add,
update, delete, and search operations, and export a summary report to CSV.

---

## Module 12 — Functional Programming Tools

**Topics:**
- First-class functions — assigning, passing, and returning functions
- `map()`, `filter()`, `zip()`, `enumerate()`
- `functools.reduce()`
- `functools.partial` — partial application
- `functools.lru_cache` / `@cache` — memoisation
- Closures — functions that capture variables from their enclosing scope
- Decorators — functions that wrap other functions:
  ```python
  def timer(func):
      def wrapper(*args, **kwargs):
          start = time.time()
          result = func(*args, **kwargs)
          print(f"{func.__name__} took {time.time() - start:.4f}s")
          return result
      return wrapper
  ```
- `@functools.wraps` — preserving docstrings in decorators
- Generator functions with `yield` and `yield from`
- Generator expressions: `(x * 2 for x in range(1000))`
- Lazy evaluation and memory efficiency

**Mini project:** Data pipeline — read a large CSV of transactions, use generators to
process lazily, apply a chain of filter/transform steps, and write results without
loading everything into memory.

---

## Module 13 — Type Hints and Static Analysis

**Topics:**
- Why type hints matter: documentation, IDE support, `mypy` / Pyright catches bugs
- Basic annotations: `int`, `str`, `float`, `bool`, `None`
- Container types: `list[int]`, `dict[str, float]`, `tuple[int, str]`, `set[str]`
- `Optional[X]` is `X | None` — prefer the union syntax in 3.10+
- `Union[X, Y]` → `X | Y` union syntax
- `Any` — opt-out of type checking
- `TypeVar` for generic functions
- `Protocol` — structural typing without inheritance:
  ```python
  from typing import Protocol

  class Drawable(Protocol):
      def draw(self) -> None: ...
  ```
- `TypedDict` for typed dicts
- `dataclasses` and type hints working together
- Running `mypy --strict` on a module and fixing the errors

**Mindset:** Type hints don't change runtime behaviour — Python remains dynamically typed.
They are a documentation and tooling layer. The goal is to write code that is both
readable and verifiable.

**Mini project:** Fully annotate the expense tracker from Module 10, run `mypy --strict`,
and fix all reported type errors.

---

## Module 14 — Testing with pytest

**Topics:**
- Why testing matters: catch regressions, document intent, enable refactoring
- `pytest` — installation and running tests
- Writing test functions: `def test_something():`
- `assert` statements
- Test discovery conventions: `test_*.py` files
- Fixtures: `@pytest.fixture` for setup and teardown
- Parametrised tests: `@pytest.mark.parametrize`
- Testing exceptions: `pytest.raises`
- Mocking with `unittest.mock.patch` and `MagicMock`
- Code coverage with `pytest-cov`
- Test-driven development (TDD) mindset: write the test first

```python
import pytest
from bank import BankAccount, InsufficientFundsError

def test_deposit_increases_balance():
    account = BankAccount("Alice", 100.0)
    account.deposit(50.0)
    assert account.balance == 150.0

def test_withdraw_raises_on_insufficient_funds():
    account = BankAccount("Alice", 50.0)
    with pytest.raises(InsufficientFundsError):
        account.withdraw(100.0)
```

**Mini project:** Full test suite for the `BankAccount` class — cover deposits,
withdrawals, edge cases (zero amount, negative amount), and the custom exception.
Achieve >90% code coverage.

---

## Capstone Project (After Module 14)

Build a **Personal Finance CLI** that demonstrates the full Python 3.14 stack:

- `@dataclass(frozen=True)` for `Transaction`, `Account`, `Category`
- Structural pattern matching for CLI command dispatch
- Custom exception hierarchy: `FinanceError → InsufficientFundsError / InvalidCategoryError`
- JSON persistence with `pathlib` for all account data
- Generator-based report engine (lazy processing of transaction history)
- Decorator-based `@audit_log` to record every mutating operation
- Fully type-annotated with `mypy --strict` passing
- `pytest` test suite with fixtures and parametrised tests
- Packaged as a proper module with `uv` for dependency management

---

## Module 15 — Async Programming with asyncio

**Prerequisites:** Complete Modules 1–14 and the capstone. Comfort with generators
and the `with` statement is essential.

**Suggested Time:** 2 weeks

---

### Part A — Concurrency Concepts

**Topics:**
- Concurrency vs parallelism — Python's GIL and what it means in practice
- Three concurrency models in Python:
  - `threading` — I/O-bound tasks, limited by the GIL for CPU-bound work
  - `multiprocessing` — CPU-bound tasks, bypasses the GIL with separate processes
  - `asyncio` — cooperative concurrency for I/O-bound tasks (the modern default)
- When to use which:
  - I/O-bound (HTTP, DB, file) → `asyncio`
  - CPU-bound (ML training, image processing) → `multiprocessing`
  - Legacy code with blocking libraries → `threading`

**Python 3.14 note:** The experimental free-threaded mode (no GIL, PEP 703) is
available in Python 3.13+ as an opt-in build. It is not yet production-ready and is
not covered here — worth tracking as Python's concurrency story continues to evolve.

---

### Part B — asyncio Fundamentals

**Topics:**
- `async def` — defining a coroutine
- `await` — suspending a coroutine until a result is ready
- `asyncio.run()` — entry point for running a coroutine
- `asyncio.sleep()` — non-blocking sleep (vs `time.sleep`)
- `asyncio.gather()` — run multiple coroutines concurrently
- `asyncio.create_task()` — schedule a coroutine as a background task
- `async with` — async context managers
- `async for` — async iterators
- Event loop basics — understanding that `asyncio` is single-threaded cooperative multitasking

```python
import asyncio

async def fetch_price(ticker: str) -> float:
    await asyncio.sleep(0.1)  # simulate I/O
    return 100.0

async def main() -> None:
    prices = await asyncio.gather(
        fetch_price("AAPL"),
        fetch_price("GOOG"),
        fetch_price("MSFT"),
    )
    print(prices)

asyncio.run(main())
```

**Mini project:** Async API aggregator — simulate fetching data from 5 endpoints
concurrently using `asyncio.gather`, with a timeout per request using `asyncio.wait_for`.

---

### Part C — aiohttp and Real Async I/O

**Topics:**
- `aiohttp` — async HTTP client/server library
- Making concurrent HTTP requests with `aiohttp.ClientSession`
- Error handling in async code: `try/except` works the same inside `async def`
- `asyncio.Semaphore` — limiting concurrent requests to avoid overloading a server
- `asyncio.Queue` — producer/consumer patterns
- Running blocking code in a thread pool: `asyncio.to_thread(blocking_fn, args)`

```python
import asyncio
import aiohttp

async def fetch(session: aiohttp.ClientSession, url: str) -> dict:
    async with session.get(url) as response:
        return await response.json()

async def main() -> None:
    urls = ["https://api.example.com/item/1", "https://api.example.com/item/2"]
    async with aiohttp.ClientSession() as session:
        results = await asyncio.gather(*[fetch(session, url) for url in urls])
    print(results)
```

**Mini project:** Async currency converter — concurrently fetch exchange rates for
5 currency pairs from a public API, with a semaphore limiting to 3 concurrent requests,
and a `asyncio.to_thread` wrapper for any blocking CSV export.

---

### Part D — concurrent.interpreters (Python 3.14)

Python 3.14 adds `concurrent.interpreters` (PEP 734), which exposes Python's long-existing
multiple-interpreter support to Python code. This is distinct from `multiprocessing` —
interpreters share the same process but are isolated from each other.

**Topics:**
- What sub-interpreters are and how they differ from processes and threads
- `interpreters.create()` and `interp.exec()`
- Use cases: running isolated Python code, plugin sandboxing, multi-core CPU tasks
  without the overhead of separate processes
- Current limitations: not all C extensions support sub-interpreters yet

**Note:** This is a new and evolving API. Introduced here for awareness; `asyncio` and
`multiprocessing` remain the production defaults for most tasks.

**Mini exercise:** Run two isolated interpreters concurrently, each computing a Fibonacci
number, and collect their results.

---

### Key Mental Models for Module 15

| Model | Best for | Python tool |
|---|---|---|
| Cooperative concurrency | I/O-bound: HTTP, DB, file | `asyncio` |
| Thread-based concurrency | I/O-bound with blocking libraries | `threading` |
| Process-based parallelism | CPU-bound: ML, image processing | `multiprocessing` |
| Sub-interpreters | Isolation + multi-core (emerging) | `concurrent.interpreters` |

---

### What to Avoid Teaching at This Stage

- `asyncio` internals (event loop implementation, `call_soon`, `call_later`) — not needed for application code
- `trio` and `anyio` — worth learning later; introduced after `asyncio` foundations are solid
- `concurrent.futures.ProcessPoolExecutor` internals — use it, but don't over-explain the machinery

---

## Suggested Pacing (Full Curriculum)

| Module | Topic | Suggested Time |
|--------|-------|---------------|
| 1–3 | Basics, Control Flow, Functions | Week 1–2 |
| 4–5 | Data Structures, Strings | Week 3 |
| 6–7 | OOP: Classes, Inheritance, Polymorphism | Week 4 |
| 8–9 | Exceptions, Dataclasses, Pattern Matching | Week 5 |
| 10–11 | Modules, Standard Library, File I/O | Week 6 |
| 12–13 | Functional Tools, Type Hints | Week 7 |
| 14 | Testing with pytest | Week 8 |
| Capstone | Personal Finance CLI | Week 9 |
| 15 | Async Programming with asyncio | Week 10–11 |

---

## How Python Differs from Java and Scala — Key Mindset Shifts

| Java / Scala habit | Python idiom |
|---|---|
| Verbose class boilerplate | `@dataclass` for data containers |
| Checked exceptions | No checked exceptions — document with type hints |
| `instanceof` + cast | `isinstance()` or duck typing |
| `static` members | Module-level functions and `@staticmethod` |
| Interfaces / traits | `Protocol` for structural typing |
| `Optional<T>` | `X | None` type annotation (no wrapper type) |
| `Future` / `IO` monad | `async def` / `await` |
| `List<T>`, `Map<K,V>` | `list[T]`, `dict[K, V]` (lowercase, built-in) |
| Explicit `new` | No `new` — call the class directly: `MyClass()` |

---

## Recommended Resources

- **Official:** [docs.python.org/3.14](https://docs.python.org/3.14) — always the authoritative source
- **Book:** *Python Crash Course* by Eric Matthes (3rd edition) — best pure beginner book
- **Book:** *Fluent Python* by Luciano Ramalho (2nd edition covers Python 3.10+) — for going deep
- **Interactive:** [realpython.com](https://realpython.com) — high-quality tutorials anchored to modern Python
- **Practice:** Exercism.io Python track, Advent of Code in Python
- **Type checking:** `mypy` or Pyright (built into VS Code's Pylance extension)
- **Tooling:** VS Code with Pylance + Ruff, or PyCharm Community Edition
- **Package manager:** `uv` (modern, fast) or `pip` with `venv`

---

## Curriculum Design Notes

Python is often taught as a scripting language first, with OOP as an afterthought.
This curriculum reverses that for developers coming from a software engineering background:
OOP (Modules 6–7), exceptions (Module 8), and dataclasses (Module 9) are taught early
and seriously, reflecting how production Python is actually written.

Type hints (Module 13) are given a dedicated module rather than scattered mentions.
In a team setting, untyped Python codebases become maintenance burdens quickly —
establishing the habit early pays dividends.

Structural pattern matching (Module 9) is taught alongside dataclasses because the two
features are most powerful together, mirroring how Scala's case classes and `match`
expressions work. Developers coming from Scala or Java 21+ will find this immediately
familiar.

The async module (Module 15) is intentionally positioned after testing. Writing async code
without a test suite is painful — having `pytest` and `pytest-asyncio` fluency first makes
the async module far more productive.
