# Exhaustive Python 3 Programming Concepts

A structured, cumulative list — basics first, then intermediate, advanced, and finally the features new as of Python 3.14 (released Oct 7, 2025), the latest stable release as of this writing.

---

## 1. Setup & Fundamentals
- Interpreter vs script execution, `.py` files
- CPython vs other implementations (PyPy, Jython, MicroPython)
- The REPL (interactive shell)
- **Python 3.14 REPL upgrades** — real-time syntax highlighting, module-name autocompletion in imports
- `pip`, virtual environments (`venv`), `python -m`
- Indentation-based blocks (no braces)
- Comments (`#`), docstrings (`"""..."""`)
- `print()`, `input()`
- PEP 8 style conventions

## 2. Variables & Data Types
- Dynamic typing, variable assignment
- Numeric types: `int`, `float`, `complex`
- `bool`
- `str` (Unicode by default)
- `NoneType`
- Type conversion (`int()`, `str()`, `float()`, etc.)
- Multiple assignment, chained assignment, unpacking (`a, b = 1, 2`)
- Constants (convention: `UPPER_CASE`, no true immutability)
- `id()`, `type()`, mutability vs immutability
- Type hints / annotations (`x: int = 5`)
- **PEP 649 — deferred evaluation of annotations (Python 3.14)**: annotations no longer evaluated eagerly; `annotationlib` module for introspection

## 3. Operators
- Arithmetic (`+ - * / // % **`)
- Comparison (`== != > < >= <=`)
- Logical (`and or not`)
- Bitwise (`& | ^ ~ << >>`)
- Assignment operators (`= += -= *= /=` etc.)
- Identity operators (`is`, `is not`)
- Membership operators (`in`, `not in`)
- Walrus operator (`:=`, assignment expressions, since 3.8)
- Operator precedence

## 4. Control Flow
- `if`, `elif`, `else`
- Conditional (ternary) expressions (`x if cond else y`)
- `for` loops, iterating over sequences
- `while` loops
- `break`, `continue`, `else` clause on loops
- `pass` statement
- `match` / `case` — structural pattern matching (since 3.10): literal patterns, capture patterns, wildcard `_`, guards (`if`), OR patterns (`|`), class/sequence/mapping patterns
- **Multiple exception types with commas in `except`** (Python 3.14 shorthand for `except (A, B):`)

## 5. Data Structures (Built-in)
- Strings: slicing, immutability, methods (`split`, `join`, `strip`, `format`, f-strings)
- **f-strings** (formatted string literals, since 3.6) — expressions, format specs, `=` debug specifier
- **t-strings / template string literals (PEP 750, Python 3.14)** — deferred string templates returning a `Template` object of static + interpolated parts, for safer custom string processing
- Lists: creation, indexing, slicing, mutability, comprehensions
- Tuples: immutability, packing/unpacking, named tuples
- Sets & frozensets: uniqueness, set operations (union, intersection, difference)
- Dictionaries: key-value pairs, methods, dict comprehensions, insertion order guarantee (3.7+)
- `collections` module: `namedtuple`, `deque`, `Counter`, `OrderedDict`, `defaultdict`, `ChainMap`
- `array` module
- Comprehensions (list, dict, set, generator)
- Nested data structures

## 6. Functions
- `def`, parameters, return values
- Default arguments
- Keyword arguments, positional-only (`/`) and keyword-only (`*`) parameters
- `*args`, `**kwargs`
- Variable scope (local, enclosing, global, built-in — LEGB rule)
- `global` and `nonlocal` keywords
- Lambda expressions
- Recursion
- Higher-order functions (`map`, `filter`, `reduce`)
- Closures
- Decorators (function and class decorators, `functools.wraps`)
- Docstrings and function annotations
- `functools`: `partial`, `lru_cache`, `cache`, `reduce`, `singledispatch`

## 7. Object-Oriented Programming (OOP)
- Classes and objects, `class` keyword
- `__init__`, `self`
- Instance vs class attributes
- Instance, class (`@classmethod`), and static (`@staticmethod`) methods
- Encapsulation conventions (`_protected`, `__private`, name mangling)
- Inheritance, `super()`
- Multiple inheritance, Method Resolution Order (MRO), `mro()`
- Polymorphism, duck typing
- Abstraction (`abc` module, `ABC`, `@abstractmethod`)
- Magic/dunder methods (`__str__`, `__repr__`, `__eq__`, `__len__`, `__getitem__`, `__iter__`, `__call__`, `__enter__`/`__exit__`, operator overloading)
- Properties (`@property`, getters/setters, `@x.setter`)
- `dataclasses` (`@dataclass`, `field`, `frozen=True`)
- Enums (`enum.Enum`, `IntEnum`, `Flag`, `auto()`)
- Metaclasses (`type`, `__new__` vs `__init__`, `__init_subclass__`)
- `__slots__`
- Structural subtyping / `Protocol` (from `typing`)

## 8. Exception Handling
- `try` / `except` / `else` / `finally`
- Built-in exception hierarchy (`BaseException`, `Exception`, `ValueError`, `TypeError`, etc.)
- Raising exceptions (`raise`)
- Custom exception classes
- Exception chaining (`raise ... from ...`)
- `assert` statements
- Exception groups (`ExceptionGroup`, `except*`, since 3.11)
- Context managers and cleanup (`with`, `contextlib`)

## 9. Iterators & Generators
- Iterables vs iterators (`__iter__`, `__next__`)
- `iter()`, `next()`
- Generator functions (`yield`)
- Generator expressions
- `yield from`
- Coroutines (generator-based, legacy)
- `itertools` module (`chain`, `cycle`, `islice`, `groupby`, `product`, `permutations`, `combinations`)

## 10. Modules & Packages
- `import`, `from ... import ...`, `as` aliasing
- Module search path, `sys.path`
- Packages, `__init__.py`
- Relative vs absolute imports
- `__name__ == "__main__"` idiom
- Standard library overview (`os`, `sys`, `math`, `random`, `datetime`, `json`, `re`, `pathlib`, `subprocess`, `logging`, `argparse`)
- Namespace packages
- `importlib`

## 11. File & I/O Handling
- Opening files (`open()`, modes: `r`, `w`, `a`, `b`)
- Context managers for files (`with open(...) as f`)
- Reading/writing text and binary data
- `pathlib.Path` API
- Working with CSV, JSON, and other formats
- Standard streams (`stdin`, `stdout`, `stderr`)

## 12. Concurrency & Parallelism
- `threading` module, the Global Interpreter Lock (GIL)
- `multiprocessing` module
- `concurrent.futures` (`ThreadPoolExecutor`, `ProcessPoolExecutor`)
- `asyncio`: event loop, `async def`, `await`, coroutines, `Task`, `gather`
- Async context managers/iterators (`async with`, `async for`)
- Queues, locks, semaphores for synchronization
- **Free-threaded Python officially supported (PEP 779, Python 3.14)** — GIL made optional in CPython builds
- **`concurrent.interpreters` standard library module (Python 3.14)** — end-user API for subinterpreters, each with its own GIL, exposing a middle ground between threads and processes
- **`InterpreterPoolExecutor` (Python 3.14)** in `concurrent.futures`

## 13. Type System & Static Typing
- `typing` module (`List`, `Dict`, `Optional`, `Union`, `Any`, `Callable`, generics)
- PEP 604 union syntax (`int | str`)
- Generic classes/functions (PEP 585, PEP 695 `type` statement and generic syntax)
- `TypedDict`, `NamedTuple`, `Protocol`, `Literal`, `Final`
- Static type checkers (mypy, pyright) — conceptual, not enforced at runtime
- `typing.TYPE_CHECKING`

## 14. Memory Management & Internals
- Reference counting and garbage collection (`gc` module)
- Mutability and object identity
- Shallow vs deep copy (`copy` module)
- Namespaces and scope resolution internals
- `__dict__`, object internals
- Weak references (`weakref`)
- **Tail-call interpreter optimization (Python 3.14, internal CPython change, opt-in with supporting compilers)**
- **Experimental JIT compiler included in official macOS/Windows binaries (Python 3.14)**

## 15. Testing & Debugging
- `unittest` module (TestCase, assertions, fixtures)
- `pytest` (conceptually, third-party but ubiquitous)
- `doctest`
- `pdb` debugger
- **`pdb` remote attach to a running process (Python 3.14)**
- **New CLI to inspect running asyncio tasks (Python 3.14)**
- Logging vs print debugging
- Profiling (`cProfile`, `timeit`)
- Improved, more actionable syntax/runtime error messages (ongoing since 3.10, refined in 3.14)

## 16. Packaging & Environment Management
- `pip`, `requirements.txt`
- Virtual environments (`venv`, `virtualenv`)
- `pyproject.toml`, build backends (setuptools, hatch, poetry — conceptual)
- Wheels vs source distributions
- Entry points, console scripts
- **Official Android binary releases (Python 3.14)**
- **New Windows install manager (Python 3.14)**

## 17. Advanced / Meta Concepts
- Descriptors (`__get__`, `__set__`, `__delete__`)
- Context manager protocol in depth (`contextlib.contextmanager`, `ExitStack`)
- Abstract Syntax Trees (`ast` module)
- Introspection (`inspect` module)
- Monkey patching
- Duck typing vs structural typing
- `__future__` imports
- C extensions / the C API (conceptual)
- Global vs per-interpreter state (relevant with free-threading and subinterpreters)

## 18. Standard Library Highlights (Selected)
- `re` (regular expressions)
- `datetime`, `time`, `zoneinfo`
- `math`, `statistics`, `decimal`, `fractions`
- `random`, `secrets`
- `json`, `csv`, `xml`, `configparser`
- `os`, `sys`, `shutil`, `subprocess`
- `socket`, `http.client`, `urllib`
- `argparse`, `getopt`
- `dataclasses`, `enum`, `functools`, `itertools`, `operator`
- `unittest`, `logging`
- **`compression.zstd` (Python 3.14)** — Zstandard compression added to the standard library

---

### Notes on Python 3.14 specifically
Python 3.14 was released October 7, 2025. The headline changes are: **PEP 649** deferred annotation evaluation, **PEP 750** t-strings (template string literals), **PEP 779** officially supported free-threaded builds, the new **`concurrent.interpreters`** standard-library module for subinterpreters, an experimental **JIT compiler** in official binaries, REPL syntax highlighting/autocompletion, colorized output across standard-library CLIs (including `unittest`), remote `pdb` attaching, and the shorthand `except A, B:` syntax for multiple exception types. As always, Python maintains strong backward compatibility, so this entire list applies whether you're running 3.14 or an older Python 3.x.
