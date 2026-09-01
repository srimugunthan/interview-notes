# Chapter 2: JavaScript Core

This is the non-negotiable chapter. The syllabus is blunt about why: **you need this to read and critique AI-generated code.** If you can't tell whether a `useEffect` dependency array is wrong, or whether an `async` function is missing an `await`, you can't ship reliably — you're just trusting output you can't verify.

## 2.1 Variables, functions, arrays/objects, destructuring

```javascript
// let/const, not var — var has confusing function-level scoping rules
let count = 0;
const name = "Ada"; // const = can't be reassigned (but object/array contents CAN change)

// Function forms you'll see constantly
function add(a, b) {
  return a + b;
}

const multiply = (a, b) => a * b; // arrow function, implicit return

const greet = (name = "friend") => { // default parameter
  console.log(`Hello, ${name}`);
};

// Arrays and objects
const user = { id: 1, name: "Ada", roles: ["admin", "editor"] };
const numbers = [1, 2, 3, 4, 5];

// Destructuring — pulling values out by shape
const { name: userName, roles } = user;
const [first, second, ...rest] = numbers;
console.log(userName, roles, first, second, rest); // "Ada" [...] 1 2 [3,4,5]

// Common array methods — you'll use these constantly instead of for-loops
const doubled = numbers.map((n) => n * 2);
const evens = numbers.filter((n) => n % 2 === 0);
const total = numbers.reduce((sum, n) => sum + n, 0);
const hasAdmin = user.roles.includes("admin");

// Spread — copying/merging arrays and objects without mutating the original
const moreNumbers = [...numbers, 6, 7];
const updatedUser = { ...user, name: "Ada Lovelace" }; // shallow copy + override
```

Why destructuring and spread matter for React later: `setUser({ ...user, name: "New Name" })` is the standard pattern for updating state immutably.

## 2.2 Promises, async/await, fetch

A **Promise** represents a value that isn't ready yet. `async/await` is syntax sugar that lets you write asynchronous code that *reads* like synchronous code.

```javascript
// Promise-based (what fetch returns under the hood)
fetch("https://api.example.com/users")
  .then((response) => response.json())
  .then((data) => console.log(data))
  .catch((error) => console.error("Request failed:", error));

// The same thing with async/await — generally easier to read and debug
async function loadUsers() {
  try {
    const response = await fetch("https://api.example.com/users");
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`);
    }
    const data = await response.json();
    console.log(data);
  } catch (error) {
    console.error("Request failed:", error);
  }
}

loadUsers();
```

Key facts that trip people up:
- `await` only works inside a function marked `async`.
- `fetch` does **not** reject on HTTP error statuses (404, 500) — only on network failure. You must check `response.ok` yourself.
- Forgetting `await` gives you a `Promise` object instead of the actual value — a classic bug source ("why does my data say `[object Promise]`?").
- `Promise.all([...])` runs multiple async calls in parallel and waits for all of them — much faster than awaiting one at a time when the calls don't depend on each other.

```javascript
const [users, posts] = await Promise.all([
  fetch("/api/users").then((r) => r.json()),
  fetch("/api/posts").then((r) => r.json()),
]);
```

## 2.3 The DOM: querySelector, event listeners, event bubbling

The DOM (Document Object Model) is the browser's live, in-memory tree representation of your HTML. JavaScript reads and mutates it.

```html
<button id="save-btn">Save</button>
<ul id="item-list">
  <li>Item 1</li>
  <li>Item 2</li>
</ul>
```

```javascript
// Selecting elements
const button = document.querySelector("#save-btn"); // first match, CSS-selector syntax
const items = document.querySelectorAll("#item-list li"); // all matches, returns a NodeList

// Reacting to events
button.addEventListener("click", (event) => {
  console.log("Saved!", event.target);
});

// Creating and inserting elements
const newItem = document.createElement("li");
newItem.textContent = "Item 3";
document.querySelector("#item-list").appendChild(newItem);
```

### Event bubbling

An event fired on a child element also fires on every ancestor, in order, unless stopped. This is why you can attach **one** listener to a parent and handle clicks on many children — called *event delegation*.

```javascript
document.querySelector("#item-list").addEventListener("click", (event) => {
  if (event.target.tagName === "LI") {
    console.log("You clicked:", event.target.textContent);
  }
});
```

```javascript
// Stopping it from bubbling further, when you need to
child.addEventListener("click", (event) => {
  event.stopPropagation();
});
```

Why this matters for React later: React's synthetic event system is built on top of this same bubbling behavior, and understanding it is what makes debugging "why did my modal close when I clicked inside it" possible.

## 2.4 Closures

A closure is a function that "remembers" the variables from the scope it was created in, even after that outer scope has finished running.

```javascript
function makeCounter() {
  let count = 0; // this variable is "closed over"
  return function () {
    count += 1;
    return count;
  };
}

const counter = makeCounter();
console.log(counter()); // 1
console.log(counter()); // 2
console.log(counter()); // 3 — count persisted between calls, privately
```

The classic closure bug — capturing a variable by reference in a loop:

```javascript
// Bug: logs 3, 3, 3 — by the time the timeouts fire, i is already 3
for (var i = 0; i < 3; i++) {
  setTimeout(() => console.log(i), 100);
}

// Fixed: let creates a new binding per loop iteration
for (let i = 0; i < 3; i++) {
  setTimeout(() => console.log(i), 100);
}
```

This is exactly the shape of bug that shows up in React `useEffect` closures over stale state — recognizing it here is what lets you spot it there.

## 2.5 `this`

`this` refers to different things depending on *how* a function is called, not where it's defined. This is the single most confusing part of JS for people coming from other languages.

```javascript
const user = {
  name: "Ada",
  greet() {
    console.log(`Hi, I'm ${this.name}`); // `this` = user, because called as user.greet()
  },
};

user.greet(); // "Hi, I'm Ada"

const detachedGreet = user.greet;
detachedGreet(); // "Hi, I'm undefined" — `this` is lost, called with no receiver

// Arrow functions don't have their own `this` — they inherit it from
// the enclosing scope. This is why they're the default choice for callbacks.
const user2 = {
  name: "Grace",
  delayedGreet() {
    setTimeout(() => {
      console.log(`Hi, I'm ${this.name}`); // `this` still = user2, inherited
    }, 100);
  },
};
user2.delayedGreet(); // "Hi, I'm Grace"
```

Practical rule: in modern React function components, you mostly won't wrestle with `this` at all (no classes, no `this.setState`) — but you need to recognize it to read older React class components or plain JS libraries.

## Try it

1. Write a function `fetchAndSummarize(url)` that fetches JSON from a URL, awaits it, and returns just the array's `.length`. Wrap it in try/catch and test it against a bad URL to see your error handling fire. (Try `https://jsonplaceholder.typicode.com/users` as a free test API.)
2. Build a tiny to-do list with plain HTML + JS: an `<input>`, an "Add" `<button>`, and a `<ul>`. Use `addEventListener` and `document.createElement` to add items. Use event delegation on the `<ul>` to handle a "delete" click on any item, including ones added after the page loaded.
3. Predict the output of the closure loop bug above *before* running it, then run it and confirm. Then explain out loud (or in writing) *why* `let` fixes it — that explanation is the actual skill.
