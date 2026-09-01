# Python syntax

https://www.youtube.com/watch?v=PNSIWjWAA7o 

Here is the structured breakdown of the Python syntax covered in the tutorial, organized by topic with relevant video timestamps:

### 1. Basics, Math, & Strings

* **The Shell vs. Script:** Code snippets starting with `>>>` represent the interactive Python shell running lines individually. Plain text blocks represent script files meant to run all at once `[00:00:11]`.
* **Basic Arithmetic:** Standard operations follow standard mathematical order of operations, utilizing parentheses for grouping `[00:00:28]`.
* **String Manipulation:**
* Concatenate strings using the `+` operator or implicitly by leaving a space between two string literals `[00:00:34]`.
* Multiply a string by an integer (e.g., `"Alice" * 5`) to repeat it `[00:00:44]`.


* **Variables & Comments:** Use a single `=` for variable assignment. Single-line comments start with `#`, while multi-line strings can act as documentation comments `[00:00:50]`.

### 2. I/O & Type Conversion

* **Console Output:** The `print()` function accepts multiple arguments separated by commas, automatically inserting a space between them `[00:01:03]`.
* **User Input:** `input()` captures text from the console into a variable `[00:01:12]`.
* **Type Casting & Inspection:**
* `len()` returns the character length of a string or item count of a collection `[00:01:26]`.
* `str()` converts integers/floats to strings, while `int()` converts floats to integers by truncating the decimal `[00:01:26]`.



### 3. Logic & Control Flow

* **Comparisons:** `==` tests equality, whereas `=` is strictly for assignment. An integer and a float representing the same value (e.g., `1 == 1.0`) evaluate as equal `[00:01:39]`.
* **Boolean Evaluation:** Evaluate multiple expressions using `and` or `or`. It is recommended to use `is` / `is not` or implicit evaluations for truthiness rather than comparing directly to Booleans `[00:02:06]`.
* **Indentation & Conditionals:** Python relies on strict code indentation rather than braces to define block scope. Conditional flow structures include `if`, `elif`, and `else` `[00:02:36]`.

### 4. Loops & Scope

* **While Loops:** Continuously execute code blocks as long as the condition evaluates to true `[00:03:19]`.
* **Loop Control:** `break` exits the loop block entirely `[00:03:34]`. `continue` halts the current iteration and jumps directly back to the loop's evaluation condition `[00:03:49]`.
* **For Loops & Ranges:** `for i in range(5)` steps through numbers 0 up to (but excluding) 5. `range(start, stop, step)` allows tracking specialized increments or counting down backwards `[00:04:11]`.
* **For-Else Clause:** A `for ... else` structural pattern runs its `else` block only if the loop completes fully without encountering a `break` `[00:04:54]`.
* **Global Variables:** Variables declared inside functions remain local unless explicitly declared globally via the `global` keyword `[00:06:37]`.

### 5. Exception Handling

* **Try/Except:** Wrap volatile operations inside a `try` block. Catch explicit errors using `except ErrorType` to prevent the software from crashing `[00:07:01]`.
* **Finally:** Code placed within a `finally` block runs unconditionally, regardless of whether an exception occurred or was handled `[00:07:36]`.

### 6. Lists & Tuples

* **List Indexing & Slicing:** Lists are zero-indexed ordered arrays. Use negative indices (like `-1`) to slice or address elements from the back end `[00:07:43]`.
* **Slicing Snytax:** `spam[start:stop]` captures data up to but excluding the stop index. Complete slices `[:]` return a shallow duplicate copy of the list `[00:08:20]`.
* **List Modification:**
* Append elements via `.append()`, inject at designated coordinates with `.insert()`, and strip out target data using `.remove()` or the `del` statement `[00:08:54]`.
* Organize strings or digits alphabetically/numerically using `.sort()`, or use the `sorted()` function to get a brand-new sorted sequence `[00:11:25]`.


* **Multi-List Operations:** `enumerate()` yields pairs of indices along with their items. `zip()` threads multiple sequences together so you can loop through them concurrently `[00:09:47]`.
* **Tuples:** Defined using parentheses, tuples act exactly like lists but are completely immutable (cannot be modified after creation) `[00:11:37]`.

### 7. Dictionaries & Sets

* **Dictionaries:** Collections of unordered key-value definitions. Extract values, keys, or pair tuples using `.values()`, `.keys()`, and `.items()` loops respectively `[00:12:22]`.
* **Dictionary Operations:**
* Use `.get(key, default)` to safely retrieve items without triggering errors if a key does not exist `[00:13:17]`.
* `.setdefault(key, default)` maps a fallback value only if that key is missing `[00:13:36]`.
* Merge two independent dictionaries using the dictionary unpacking operator `` `[00:13:56]`.


* **Sets:** Unordered groups containing entirely unique elements. They support algebraic behaviors such as `.union()`, `.intersection()`, `.difference()`, and `.symmetric_difference()` `[00:14:07]`.

### 8. Comprehensions & String Formatting

* **Comprehensions:** Compact, single-line shorthand tools to generate new lists, sets, or dictionaries by transforming inline sequences `[00:15:24]`.
* **Escape Syntax:** Handle formatting characters like tabs (`\t`) or line breaks (`\n`) via backslashes. Preface a string literal with `r` (e.g., `r"text"`) to handle it as a raw string that completely ignores escape characters `[00:16:03]`.
* **Multi-Line Strings:** Enclose blocks in triple quotes (`"""`) to preserve literal formatting and layout breaks `[00:16:39]`.
* **Alignment & Padding:** Justify text layouts within spacing frames using `.ljust()`, `.rjust()`, or `.center()`. Strip outer whitespace using `.strip()` options `[00:18:58]`.
* **F-Strings:** Better than `.format()`, pre-fixing string templates with `f` allows evaluating expressions or raw variables directly inside matching curly braces `{}` `[00:19:38]`.

### 9. Advanced Mechanics

* **Assertions:** Statements using `assert` validate critical runtime safety conditions, raising an `AssertionError` if the check fails `[00:20:47]`.
* **Lambda Functions:** Anonymous, single-expression utility functions declared in-line using the `lambda` keyword `[00:21:38]`.
* **Variadic Arguments (`*args` and `kwargs`):**
* `*args` packages excess positional parameters into a single Tuple variable.
* `kwargs` maps variable-length key-value sequences into a manageable Dictionary `[00:22:58]`.


* **Main Module Guard:** The boilerplate wrapper `if __name__ == "__main__":` ensures specific logic execution only when a script file is launched directly, preventing that code from running if it is imported into another file `[00:23:49]`.

---
# Python OOP
https://www.youtube.com/watch?v=OemVdsibSFQ 

Here is a comprehensive breakdown of the core Object-Oriented Programming (OOP) concepts in Python covered in the tutorial, structured as an intuitive reference guide with accompanying timestamps:

---

### 1. The Core Entities: Classes vs. Objects

Using the real-world analogy of a restaurant setup, OOP maps code blocks directly to distinct roles `[00:01:47]`.

* **Class (The Blueprint):** A class serves as a structural template or a role description (e.g., the concept of a `Waiter`) `[00:03:23]`.
* **Object (The Instance):** An individual item created out of that parent blueprint template (e.g., `Raj` or `Simran` who are specific waiters) `[00:03:23]`.

```python
# Defining the Blueprint
class Waiter:
    pass

# Building specific staff members (Objects)
raj = Waiter()
simran = Waiter()

```

---

### 2. Properties & Behaviors: Attributes vs. Methods

* **Attributes (What an object *has*):** Variables tied strictly to an object or a class scope (e.g., a waiter's list of assigned tables) `[00:02:36]`.
* **Methods (What an object *does*):** Functions nested directly inside a class blueprint to coordinate behaviors `[00:02:36]`.
* **Encapsulation:** Bundling related data (attributes) and behavior (methods) inside an object container, allowing it to modify its own internal "state" without external interference `[00:03:04]`, `[00:10:18]`.

---

### 3. Dynamic Controls: `__init__` and `self`

* **Class Variables vs. Instance Variables:** Defining an attribute directly underneath a class creates a variable shared globally by default among all instances `[00:04:25]`. To ensure each object gets isolated, private tracking data, you use Python's automatic constructor `[00:06:32]`.
* **The `__init__` Constructor Method:** A special magic initialization method that executes automatically the moment a brand-new object instance is spun up into existence `[00:06:45]`.
* **The `self` Keyword:** Refers explicitly to *this particular object* currently handling the operation. It acts as an identifier keeping each instance's data partition isolated from the others `[00:07:34]`.

```python
class Waiter:
    def __init__(self):
        # Every waiter object starts with its own distinct tables list
        self.tables = []
        
    def take_order(self, table_number):
        # Accessing instance data securely through self
        self.tables.append(table_number)

raj = Waiter()
raj.take_order(5)  # Raj modifies his own isolated data state

```

---

### 4. Code Sharing: Inheritance

* **Definition:** Allows a specialized child class to automatically adopt attributes and methods defined inside a generic parent class `[00:15:05]`.
* **Relationship:** It maps an **"is-a"** relationship (e.g., a `Waiter` *is a* `Staff` member) `[00:15:36]`.

```python
class Staff:
    def __init__(self, name, shift):
        self.name = name
        self.shift = shift
        
    def start_work(self):
        print(f"{self.name} started the shift.")

# Waiter inherits everything from Staff automatically
class Waiter(Staff):
    def take_order(self):
        print("Taking order...")

```

---

### 5. Adaptive Context: Polymorphism

* **Definition:** Derived from "many forms"—it allows different object types to respond uniquely to the exact same method signature or command line call `[00:17:30]`.
* **Method Overriding:** A child class can declare its own customized version of a parent method. When executed, Python prefers the child version over the fallback parent contract `[00:16:43]`.

```python
class Staff:
    def work(self):
        pass

class Waiter(Staff):
    def work(self):
        print("Serving tables...")

class Chef(Staff):
    def work(self):
        print("Cooking gourmet food...")

# The same loop execution yields entirely distinct behaviors
for employee in [Waiter(), Chef()]:
    employee.work()

```

---

### 6. Contract Enforcement: Abstraction

* **Definition:** Setting up strict operational blueprints and governance rules rather than just hiding code blocks. It relies on Python's built-in `abc` (Abstract Base Classes) framework `[00:19:13]`, `[00:21:46]`.
* **Abstract Class / Method:** An abstract class is an enforcement template that cannot be directly instantiated. If it declares an `@abstractmethod`, any child class *must* write an explicit implementation of that method before Python allows the object to exist `[00:19:26]`.

```python
from abc import ABC, abstractmethod

class Staff(ABC):
    @abstractmethod
    def experience(self):
        """Must be implemented by child classes, or Python blocks instantiation."""
        pass

class Waiter(Staff):
    def experience(self):
        return "2 years of service"

```

---

### 7. Structural Assembly: Composition

* **Definition:** Building highly scalable architectural logic by coordinating smaller, completely independent entities instead of relying exclusively on wide inheritance deep-chains `[00:25:16]`.
* **Relationship:** It maps a **"has-a"** relationship (e.g., a `Waiter` *has a* `Chef` reference to handle food logistics, but a waiter is *not* a chef) `[00:25:29]`.

```python
class Chef:
    def cook(self):
        print("Chef is cooking the meal.")

class Waiter:
    def __init__(self, assigned_chef):
        # Cooperating via injection rather than subclass matching
        self.chef = assigned_chef 

    def complete_service(self):
        self.chef.cook()

```
