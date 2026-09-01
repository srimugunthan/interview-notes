
# Java syntax
Here is the structured and organized breakdown of the key programming concepts covered in the video tutorial:

https://www.youtube.com/watch?v=o33cQL6-2Sg

## 1. Environment Setup & First Program

* **JDK vs. Java:** Java is the programming language, but you need the **JDK** (Java Development Kit) to compile and run your code `[00:01:13]`.
* **Verification:** You can verify your installation by opening a terminal and running `javac --version` `[00:02:00]`.
* **IDE Selection:** The instructor uses **IntelliJ IDEA** (Community Edition) as the Integrated Development Environment `[00:02:44]`.
* **File Constraints:** Every Java file must end with `.java`, and the name of your public class **must exactly match** the name of the file `[00:05:04]`, `[00:08:13]`.
* **Compilation vs. Execution:** When you run a program, it first **compiles** (translates human-readable code into zeros and ones/bytecode) and then **runs** (executes the compiled code) `[00:12:14]`.

## 2. Java Basics: Syntax, Variables, and Data Types

* **Comments:** Written with two forward slashes `//`. They are ignored by the compiler and are purely for developer notes `[00:05:16]`.
* **Statements:** Every instruction or line of code ends with a semicolon `;` `[00:10:29]`.
* **Variables:** A variable is a named storage location in memory holding a value. It requires a data type and name, and uses the single equal sign `=` as an **assignment operator** (moving right-to-left) `[00:13:18]`, `[00:14:54]`.
* **Primitive & Core Data Types:**
* `int`: For whole numbers/integers (e.g., `10`) `[00:13:41]`.
* `double`: For decimal/floating-point numbers (e.g., `112.6`) `[00:15:59]`.
* `String`: For sequences of text, wrapped in double quotes (e.g., `"Sam"`) `[00:16:58]`.
* `char`: For a single character, wrapped in single quotes (e.g., `'a'`) `[00:17:26]`.
* `boolean`: Holds only a `true` or `false` state `[00:17:42]`.


* **Type Safety:** You cannot assign a decimal (`double`) value to an integer (`int`) variable without causing a compilation error `[00:16:29]`.

## 3. Operators & Precedence

* **Arithmetic Operators:** Standard math operations include `+`, `-`, `*`, and `/` `[00:18:58]`.
* **Integer Division:** Dividing two integers drops/truncates the decimal portion (e.g., `10 / 4` results in `2`, not `2.5`). To keep the decimal, at least one number must be explicitly written as a double (e.g., `10.0 / 4.0`) `[00:21:59]`.
* **Shorthand Operators:** `result += 7` is a cleaner shorthand way of writing `result = result + 7` `[00:23:34]`.
* **Modulus (`%`):** Returns the *remainder* of a division operation (e.g., `10 % 4` is `2`). It is frequently used to determine if a number is even (`num % 2 == 0`) or odd `[00:24:03]`, `[00:24:51]`.
* **Increment/Decrement:** `i++` adds 1 to a variable, and `i--` subtracts 1 `[00:25:11]`.
* **Operator Precedence:** Multiplication and division take priority over addition and subtraction. Use parentheses `()` to explicitly dictate evaluation order `[00:26:14]`, `[00:27:19]`.

## 4. User Input (The `Scanner` Class)

* **Setup:** To get console input, you must instantiate a `Scanner` object: `Scanner in = new new Scanner(System.in);` `[00:28:58]`.
* **Imports:** Unlike basic data types, the `Scanner` must be explicitly imported at the very top of your file (`import java.util.Scanner;`) `[00:29:40]`.
* **Reading Inputs:**
* `in.nextLine()` halts program execution and waits for user text `[00:30:08]`.
* Because `nextLine()` outputs a `String`, you must use parsing methods like `Integer.parseInt(input)` or `Double.parseDouble(input)` if you want to perform math calculations with that input `[00:48:51]`, `[01:05:46]`.



## 5. Control Flow: Conditional Statements

* **If/Else Conditions:** An `if` statement executes a code block only if its underlying boolean condition is true. The `else` statement acts as a fallback block when the condition is false `[00:33:40]`, `[00:36:10]`.
* **Relational Operators:** Used to compare values: `==` (equality), `!=` (not equal), `<`, `>`, `<=`, `>=` `[00:38:13]`. Note that a double equals `==` checks for equality, whereas a single equals `=` assigns values `[00:38:56]`.
* **Logical Operators:**
* `&&` (AND): Both sides of the condition **must** be true `[01:41:09]`.
* `||` (OR): Only one side needs to be true `[01:42:16]`.
* `!` (NOT): Inverts/flips a boolean value `[01:44:17]`.


* **String Equality Warning:** You **cannot** safely compare strings using `==`. Instead, you must use the `.equals()` method (e.g., `str1.equals(str2)`) `[01:06:55]`.
* **If-Else Ladder & Switch Case:**
* An `else if` ladder checks conditions sequentially; only the *first* matching block will run `[00:53:26]`, `[00:56:26]`.
* A `switch` statement maps an expression directly against specified `case` blocks. You must explicitly include a `break;` at the end of each case, or the program will experience "fall-through" bugs and execute subsequent cases blindly `[00:57:26]`, `[01:01:21]`.


* **Ternary Operator (`?:`):** A inline, condensed shorthand for an if-else statement. Syntax: `variable = (condition) ? valueIfTrue : valueIfFalse;` `[01:02:40]`.

## 6. Loops (Iteration)

* **For Loop:** Best when you know exactly how many times you want code to run. It groups initialization, condition, and iteration into a single line: `for(int i = 0; i < 4; i++)` `[01:10:56]`.
* **While Loop:** Continues running a block of code as long as a condition remains true. The condition is validated at the *top* of the loop, meaning it may run zero times if the condition is initially false `[01:15:22]`.
* **Do-While Loop:** Similar to a while loop, except it evaluates its condition at the *bottom*. This guarantees that the loop body will always execute **at least once** `[01:20:19]`.
* **Loop Modifiers:**
* `break`: Instantly terminates the loop and jumps past it `[01:24:32]`.
* `continue`: Stops the current iteration early and immediately jumps back to the top of the loop for the next cycle `[01:26:54]`.



## 7. Methods (Functions)

* **Definition:** A modular, reusable block of code designed to execute a specific unit of work `[00:08:59]`, `[01:29:32]`.
* **Void vs Return Types:** If a method does not return a value, its header uses the `void` keyword `[01:37:30]`. If it *does* pass a value back, you must specify that exact data type (like `int` or `String`) and end the logic with a `return` statement `[01:37:43]`.
* **Parameters vs. Arguments:** Variables defined in the method's header are *parameters* `[01:35:53]`. The actual values you pass into the method when calling it are *arguments* `[01:35:47]`.

## 8. Variable Scope & Lifetime

* **Scope Blocks:** A scope block is defined by an opening and closing curly brace `{}` `[01:39:53]`.
* **Visibility Rules:** Variables created inside an inner/nested block are local to that block and completely invisible to outer layers. They are destroyed ("go out of scope") as soon as that specific code block finishes executing `[01:40:47]`, `[01:42:01]`.

## 9. Object-Oriented Programming (OOP) Essentials

* **Classes vs. Objects:** A class acts as an abstract architectural blueprint `[01:45:53]`. An object is a concrete, living instance constructed from that blueprint `[01:45:57]`.
* **Instance Variables:** Variables defined within a class body that track the state/attributes of an object `[01:46:23]`. Every created object gets its own isolated, unique set of instance variables `[01:50:20]`.
* **The `new` Keyword:** Used to allocate memory space and initialize a brand new object: `Pokemon p1 = new Pokemon();` `[01:48:22]`.
* **Dot Operator (`.`):** Used to access or modify an object's internal fields and invoke its methods (e.g., `p1.name = "Pikachu";`) `[01:48:58]`.
* **Constructors:** Special initialization methods with no return type that share the exact same name as the class. They execute automatically the split second a new object is instantiated via the `new` keyword `[01:51:06]`, `[01:51:22]`.
* **The `this` Keyword:** Explicitly points to the active object's instance variables. It resolves naming conflicts when a method's incoming parameters share the exact same names as the class fields `[01:54:52]`.
