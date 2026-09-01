# JavaScript Curriculum — ES2025 to React 19 and TypeScript

> **Version anchor:** This curriculum targets **ECMAScript 2025 (ES2025)**, approved June 2025.
> The React modules target **React 19.2** (October 2025, latest stable). The TypeScript
> module targets **TypeScript 5.x**. All examples use modern JS idioms: `const`/`let`,
> arrow functions, destructuring, modules, and async/await throughout.

---

## Learning Goals

By the end of this curriculum, you can:
- Write modern, idiomatic JavaScript using ES2025 features
- Model programs with functions, objects, classes, and modules
- Handle asynchronous code confidently with Promises and async/await
- Manipulate the DOM and respond to browser events
- Build React 19 applications using hooks, components, and the Actions API
- Read and write TypeScript and understand why it matters

---

## Module 1 — Getting Started with JavaScript

**Topics:**
- Where JavaScript runs: the browser console, Node.js, and Deno
- Setting up: VS Code, Node.js 22+, browser DevTools
- `console.log()`, comments, semicolons (and why they're optional but worth being consistent)
- Variables: `var` (avoid), `let` (mutable), `const` (default choice)
- Primitive types: `number`, `string`, `boolean`, `null`, `undefined`, `symbol`, `bigint`
- `typeof` operator
- Template literals: `` `Hello, ${name}!` ``
- Type coercion pitfalls: `==` vs `===` — always use `===`
- `null` vs `undefined` — two ways of saying "nothing", for different reasons

**Mindset from day one:** JavaScript is dynamically typed and very permissive. This
is both its strength and its biggest source of bugs. Good habits (strict equality,
`const` by default, TypeScript later) mitigate most of the risk.

**Mini project:** Interactive greeting — ask for the user's name via `prompt()`,
compute their birth year from their age, and display a formatted summary using
template literals.

---

## Module 2 — Control Flow and Functions

**Topics:**
- `if / else if / else`
- `switch` statements
- Ternary operator: `condition ? valueIfTrue : valueIfFalse`
- `for`, `while`, `do...while` loops
- `for...of` — iterating over arrays and iterables (preferred over index-based loops)
- `for...in` — iterating over object keys (use with care)
- `break` and `continue`
- Function declarations vs function expressions
- Arrow functions: `const double = x => x * 2`
- Default parameters: `function greet(name = "World")`
- Rest parameters: `function sum(...nums)`
- `return` values and early returns

**Arrow functions vs regular functions:** Arrow functions are the default choice
for short callbacks and expressions. Regular function declarations are preferred for
named, top-level functions — they are hoisted and easier to debug.

**Mini project:** Grade calculator — a pure function that takes a score and returns
a letter grade using a `switch` statement, plus a `for...of` loop to process a list
of scores.

---

## Module 3 — Arrays and Objects

**Topics:**
- Array creation, indexing, `length`
- Core array methods: `push`, `pop`, `shift`, `unshift`, `splice`, `slice`
- Functional array methods: `map`, `filter`, `reduce`, `find`, `findIndex`,
  `some`, `every`, `flat`, `flatMap`
- Spread operator: `[...arr1, ...arr2]`
- Destructuring arrays: `const [first, second, ...rest] = arr`
- Object literals: `{ key: value }`
- Accessing and setting properties: dot notation and bracket notation
- Shorthand properties: `{ name, age }` instead of `{ name: name, age: age }`
- Computed property names: `{ [dynamicKey]: value }`
- Object destructuring: `const { name, age } = person`
- Spread for objects: `{ ...defaults, ...overrides }`
- `Object.keys()`, `Object.values()`, `Object.entries()`

**ES2025 — Iterator helpers:** ES2025 adds native functional methods directly on
iterators (`map`, `filter`, `take`, `drop`, `flatMap`) without converting to an array first.
This avoids intermediate allocations for large data sets:
```javascript
const result = someMap.values()
  .filter(x => x > 10)
  .map(x => x * 2)
  .toArray();
```

**ES2025 — Set methods:** ES2025 finalises `union`, `intersection`, `difference`,
and `symmetricDifference` directly on `Set`:
```javascript
const a = new Set([1, 2, 3]);
const b = new Set([2, 3, 4]);
console.log(a.intersection(b)); // Set { 2, 3 }
```

**Mini project:** Student grade book — store students as an array of objects,
use `map`, `filter`, and `reduce` to compute averages, top scorer, and pass rate.

---

## Module 4 — Scope, Closures, and the `this` Keyword

**Topics:**
- Block scope with `let` and `const` vs function scope with `var`
- Hoisting — why `var` declarations are hoisted but `let`/`const` are not
- Lexical scope — inner functions access outer variables
- Closures — a function bundled with its surrounding state:
  ```javascript
  function makeCounter() {
    let count = 0;
    return () => ++count;
  }
  const counter = makeCounter();
  counter(); // 1
  counter(); // 2
  ```
- IIFE (Immediately Invoked Function Expression) — historical pattern, still seen in older code
- `this` in different contexts: global, method, arrow function, event handler
- Why arrow functions don't have their own `this` — and why that's usually what you want
- `bind`, `call`, `apply`

**Closures are fundamental:** Almost everything in JavaScript — callbacks, event handlers,
module patterns, React hooks — relies on closures. Spend extra time here.

**Mini project:** A counter factory using closures — `makeCounter(start, step)` returns
an object with `increment`, `decrement`, and `reset` methods that share private state.

---

## Module 5 — Classes and Object-Oriented Programming

**Topics:**
- `class` syntax, `constructor`, methods
- Inheritance with `extends` and `super()`
- `static` methods and properties
- Private fields with `#`: `#balance` — fully private, enforced by the engine
- Getters and setters: `get balance()`, `set balance(val)`
- `instanceof` for type checking
- Prototype chain — understanding that `class` is syntactic sugar over prototypes
- When to use classes vs plain objects and functions — OOP is not the default in JS

```javascript
class BankAccount {
  #balance;

  constructor(owner, initialBalance = 0) {
    this.owner = owner;
    this.#balance = initialBalance;
  }

  get balance() { return this.#balance; }

  deposit(amount) {
    if (amount <= 0) throw new Error("Amount must be positive");
    this.#balance += amount;
  }
}
```

**Mini project:** `BankAccount` with private `#balance`, `SavingsAccount` extending it
with an `applyInterest()` method.

---

## Module 6 — Error Handling

**Topics:**
- `try / catch / finally`
- `throw` — throwing any value (best practice: throw `Error` objects)
- The `Error` class and its subtypes: `TypeError`, `RangeError`, `SyntaxError`
- Custom error classes:
  ```javascript
  class InsufficientFundsError extends Error {
    constructor(amount, balance) {
      super(`Cannot withdraw ${amount}, balance is ${balance}`);
      this.name = "InsufficientFundsError";
    }
  }
  ```
- `error.message`, `error.name`, `error.stack`
- Re-throwing errors — catching, inspecting, then re-throwing if not handled
- Optional chaining `?.` — safely accessing nested properties without null checks:
  `user?.address?.city`
- Nullish coalescing `??` — default for `null`/`undefined` only (not falsy values):
  `const name = user.name ?? "Anonymous"`

**Common mistakes:**
- Catching all errors and swallowing them silently
- Throwing strings instead of `Error` objects (loses the stack trace)
- Using `||` for defaults when `0` or `""` are valid values — use `??` instead

**Mini project:** Safe JSON parser — a `safeParse(jsonString)` function that returns
`{ ok: true, data }` or `{ ok: false, error }` using `try/catch`, never throwing to callers.

---

## Module 7 — The DOM and Browser Events

**Topics:**
- What the DOM is: the browser's tree representation of an HTML page
- `document.querySelector()` and `document.querySelectorAll()`
- Reading and setting content: `.textContent`, `.innerHTML` (with XSS caution), `.value`
- Creating and appending elements: `createElement`, `appendChild`, `insertAdjacentHTML`
- Removing elements: `element.remove()`
- Modifying styles and classes: `.style`, `.classList.add/remove/toggle`
- HTML attributes: `.getAttribute`, `.setAttribute`
- Event listeners: `addEventListener("click", handler)`
- Event object: `event.target`, `event.preventDefault()`, `event.stopPropagation()`
- Common events: `click`, `input`, `change`, `submit`, `keydown`, `DOMContentLoaded`
- Event delegation — attaching one listener to a parent instead of many to children

**Mini project:** Interactive to-do list — add, complete (toggle), and delete tasks
entirely in vanilla JS with DOM manipulation and event delegation.

---

## Module 8 — Asynchronous JavaScript: Callbacks, Promises, and Async/Await

**Topics:**
- The event loop — why JavaScript is single-threaded but non-blocking
- Callbacks — the original async pattern and callback hell
- `Promise` — a cleaner model for async results: `.then()`, `.catch()`, `.finally()`
- Creating promises: `new Promise((resolve, reject) => ...)`
- `Promise.all()` — wait for all to succeed
- `Promise.allSettled()` — wait for all, get each result regardless of success/failure
- `Promise.race()` — first one to settle wins
- `Promise.any()` — first one to succeed wins
- **ES2025 — `Promise.try()`** — wrap potentially synchronous or async code uniformly:
  ```javascript
  Promise.try(() => JSON.parse(input))
    .then(data => process(data))
    .catch(err => handleError(err));
  ```
- `async` / `await` — syntactic sugar over promises; the default way to write async code
- Error handling with `try/catch` inside `async` functions
- Running async functions in parallel vs sequentially

```javascript
// Sequential (slow — each waits for the previous)
const user = await fetchUser(id);
const orders = await fetchOrders(user.id);

// Parallel (fast — both fire at once)
const [user, config] = await Promise.all([fetchUser(id), fetchConfig()]);
```

**Mini project:** Weather dashboard — fetch current weather and a 5-day forecast from
a public API concurrently using `Promise.all`, display results in the DOM, handle
network errors gracefully.

---

## Module 9 — JavaScript Modules and Tooling

**Topics:**
- ES Modules: `export`, `export default`, `import`, `import as`
- Named vs default exports — prefer named exports for discoverability
- **ES2025 — JSON modules:** Import JSON directly without a build step:
  ```javascript
  import config from './config.json' with { type: 'json' };
  ```
- Dynamic import: `const module = await import('./heavy-module.js')`
- Module bundlers — what they do and why: Vite (recommended), Webpack (legacy)
- npm basics: `package.json`, `npm install`, `node_modules`, `package-lock.json`
- `npx` for running tools without installing globally
- ESLint — linting for code quality
- Prettier — code formatting
- Environment variables: `.env` files and `import.meta.env` in Vite
- Introduction to Vite: `npm create vite@latest`

**Mini project:** Refactor the to-do list (Module 7) into ES modules — split into
`storage.js`, `ui.js`, and `main.js`. Bundle with Vite.

---

## Module 10 — Functional Programming Patterns

**Topics:**
- Pure functions and side effects
- Immutability — why mutating state is a source of bugs
- Higher-order functions — functions that take or return functions
- Function composition: `const pipeline = (...fns) => x => fns.reduce((v, f) => f(v), x)`
- Currying and partial application
- `Array.prototype.reduce` as a universal aggregator
- Avoiding mutation: `map`/`filter`/`reduce` over `push`/`splice`/`sort` (which mutate)
- Memoisation with `Map` as a cache

**Functional vs OOP in JS:** JavaScript supports both. In the browser and React world,
functional patterns (pure functions, immutable data, composition) are increasingly
preferred over class hierarchies.

**Mini project:** Data pipeline — take an array of raw transaction objects, apply a
chain of pure transform functions (filter invalid, normalise currency, apply tax),
and produce a summary — no mutation anywhere.

---

## Module 11 — Storage, APIs, and the Fetch API

**Topics:**
- `fetch()` API — the browser's built-in HTTP client
- Request and Response objects
- Reading response bodies: `.json()`, `.text()`, `.blob()`
- Setting headers, method, and body for POST/PUT requests
- Handling HTTP errors — `fetch` doesn't throw on 4xx/5xx; check `response.ok`
- `AbortController` — cancelling in-flight requests
- `localStorage` and `sessionStorage` — key/value storage in the browser
- `IndexedDB` — browser database for larger structured data (overview only)
- CORS — what it is and why you'll encounter it

```javascript
async function postJSON(url, data) {
  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!response.ok) throw new Error(`HTTP ${response.status}`);
  return response.json();
}
```

**Mini project:** Notes app with persistence — save and load notes from `localStorage`,
sync with a mock REST API using `fetch`, handle offline gracefully.

---

## Capstone Project (After Module 11)

Build a **Vanilla JS Budget Tracker** that consolidates everything before React:

- ES modules: `transactions.js`, `ui.js`, `api.js`, `utils.js`
- Private class fields for the `Account` data model
- `async/await` + `Promise.all` for fetching initial data and saving updates
- DOM manipulation and event delegation for the UI
- `localStorage` persistence
- Custom `BudgetError` class hierarchy
- Functional pipeline for report generation (filter, group, sum)
- Bundled with Vite, linted with ESLint

---

## Module 12 — Introduction to React 19

**Prerequisites:** Comfortable with ES modules, destructuring, arrow functions, and
async/await. Complete the capstone before starting this module.

**Topics:**
- What React is and the problem it solves: declarative UI, component model, virtual DOM
- Setting up a React project: `npm create vite@latest my-app -- --template react`
- JSX — HTML-like syntax compiled to `React.createElement()`:
  ```jsx
  function Greeting({ name }) {
    return <h1>Hello, {name}!</h1>;
  }
  ```
- Components: functions that return JSX
- Props — passing data from parent to child
- Rendering lists: `array.map()` and the `key` prop
- Conditional rendering: ternary and `&&` short-circuit
- Fragments: `<>...</>` to avoid extra DOM nodes
- Component composition — building complex UIs from small, focused components
- React 19.2 current stable version — `v19.2.1` (December 2025)

**Mini project:** Static product catalogue — a list of product cards rendered from
a JS array, with a filter by category UI — no state yet, all data passed as props.

---

## Module 13 — React Hooks: State and Effects

**Topics:**
- `useState` — local component state:
  ```jsx
  const [count, setCount] = useState(0);
  ```
- State is immutable — always call the setter, never mutate directly
- Updating state based on previous state: `setCount(prev => prev + 1)`
- State with objects and arrays — spread to create new references
- `useEffect` — synchronising with the outside world (timers, subscriptions, fetch)
- Dependency array: `[]` (run once), `[dep]` (run when dep changes), no array (run every render)
- Cleanup functions in `useEffect` — preventing memory leaks
- `useRef` — accessing DOM nodes and persisting values without triggering re-renders
- `useMemo` and `useCallback` — memoisation (React 19's compiler reduces need for these)
- The Rules of Hooks: only call at top level, only in React functions

**React 19 note — the React Compiler:** React 19 ships an automatic compiler that
handles most memoisation. In React 19, you rarely need `useMemo` and `useCallback`
for performance — the compiler inserts them automatically. Learn what they do, but
don't over-use them manually.

**Mini project:** Live search — a component with a text input (`useState`), a
`useEffect` that debounces the input and fetches results from a public API, and
displays results in a list.

---

## Module 14 — React Forms and the Actions API

**Topics:**
- Controlled inputs — `value` + `onChange` wired to state
- Uncontrolled inputs with `useRef` — when to use
- Form submission with `onSubmit` and `event.preventDefault()`
- **React 19 — Actions API:** The new way to handle async form submissions:
  ```jsx
  async function submitAction(formData) {
    await saveToServer(formData.get("name"));
  }

  function MyForm() {
    return (
      <form action={submitAction}>
        <input name="name" />
        <button type="submit">Save</button>
      </form>
    );
  }
  ```
- `useActionState` — track pending/error/success state for an action
- `useFormStatus` — access the parent form's submission state from a child component
- `useOptimistic` — show instant UI feedback while an async operation is pending
- Form validation patterns — client-side before submission

**Mini project:** Contact form with Actions API — submit a form, show an optimistic
"Saving..." message with `useOptimistic`, handle success and error states with
`useActionState`.

---

## Module 15 — Component Patterns and State Management

**Topics:**
- Lifting state up — moving state to the nearest common ancestor
- Prop drilling and when it becomes a problem
- `useContext` and `createContext` — sharing state without prop drilling:
  ```jsx
  const ThemeContext = createContext("light");

  function App() {
    return (
      <ThemeContext value="dark">
        <Page />
      </ThemeContext>
    );
  }
  ```
- **React 19 — simplified Context:** No more `<Context.Provider>` wrapper — pass
  `value` directly to `<Context>` as above
- Custom hooks — extracting reusable stateful logic into `use*` functions
- `useReducer` — managing complex state transitions with a reducer pattern
- Introduction to Zustand — a lightweight external state manager for larger apps
- When to use: local state → context → Zustand → (server state tools later)

**Mini project:** Shopping cart — a multi-component app with a `CartContext` providing
cart state globally, `useReducer` for cart operations (add, remove, update quantity),
and a checkout form using the Actions API.

---

## Module 16 — React Router and Navigation

**Topics:**
- Why client-side routing exists: SPAs and the URL
- React Router v7 — installation and setup
- `<BrowserRouter>`, `<Routes>`, `<Route>`
- `<Link>` and `<NavLink>` for navigation without page reloads
- Route parameters: `/products/:id` and `useParams()`
- `useNavigate()` — programmatic navigation
- Nested routes and layout routes
- Loader functions — fetching data before a route renders (React Router v7 pattern)
- 404 handling with a catch-all route

**Mini project:** Multi-page product app — home page listing products, a product detail
page at `/products/:id`, a cart page at `/cart`, and a nav bar with `NavLink` active
highlighting.

---

## Module 17 — Fetching Data in React

**Topics:**
- The problem with `useEffect` for data fetching — race conditions, loading states,
  error states all need manual management
- `use()` hook in React 19 — read a promise directly in render, React suspends automatically:
  ```jsx
  function ProductDetail({ id }) {
    const product = use(fetchProduct(id)); // React suspends until resolved
    return <h1>{product.name}</h1>;
  }
  ```
- `<Suspense>` — showing a fallback while a component suspends
- `<ErrorBoundary>` — catching render errors and showing fallback UI
- Introduction to TanStack Query (React Query) — the ecosystem standard for server state:
  `useQuery`, `useMutation`, query keys, caching, and background refetching
- When to use `use()` vs TanStack Query vs Actions API

**Mini project:** Refactor the product app (Module 16) to use TanStack Query for all
data fetching — query caching, loading skeletons with `<Suspense>`, and error boundaries.

---

## Module 18 — React 19 Activity, Performance, and Tooling

**Topics:**
- **React 19.2 — `<Activity />`:** Pre-render hidden UI without impacting performance.
  Replaces conditional rendering for tabs and panels that should stay mounted but hidden:
  ```jsx
  <Activity mode={activeTab === "settings" ? "visible" : "hidden"}>
    <SettingsPanel />
  </Activity>
  ```
- React DevTools — inspecting component trees, props, state, and performance tracks
- Code splitting with `React.lazy()` and `<Suspense>`
- Key reconciliation — understanding how React's diffing algorithm uses `key`
- Avoiding common performance pitfalls: unnecessary re-renders, missing keys, large lists
- Introduction to Next.js 15 — when to graduate from Vite to a framework:
  file-based routing, React Server Components, SSR, ISR

**Mini project:** Tabbed dashboard with `<Activity />` — pre-render all tab panels on
mount, switch between them instantly, lazy-load a heavy chart component with
`React.lazy()`.

---

## Module 19 — Getting Started with TypeScript

**Prerequisites:** All JavaScript and React modules. TypeScript makes the most sense
once you know what JavaScript pain points it solves.

**Suggested time:** 2 weeks

---

### Part A — Why TypeScript?

- JavaScript's type coercion and runtime errors vs compile-time safety
- TypeScript = JavaScript + a static type layer that compiles away
- TypeScript does not change JavaScript's runtime behaviour
- The TypeScript compiler (`tsc`) and `tsconfig.json`
- TypeScript in Vite: `npm create vite@latest my-app -- --template react-ts`
- IDE integration — VS Code's Intellisense becomes dramatically more useful with types

---

### Part B — TypeScript Fundamentals

**Topics:**
- Type annotations: `let name: string = "Alice"`
- Type inference — TypeScript infers most types; annotate only when needed
- Primitive types: `string`, `number`, `boolean`, `null`, `undefined`, `void`, `never`
- Arrays: `number[]` or `Array<number>`
- Tuples: `[string, number]`
- `any` — the escape hatch (avoid it; it disables type checking)
- `unknown` — safe alternative to `any`; must narrow before use
- Union types: `string | number`
- Intersection types: `TypeA & TypeB`
- Type aliases: `type UserId = string`
- Interfaces: `interface User { name: string; age: number }`
- `type` vs `interface` — use `interface` for object shapes, `type` for unions and aliases
- Optional properties: `age?: number`
- Readonly: `readonly id: string`

```typescript
interface Product {
  readonly id: string;
  name: string;
  price: number;
  category?: string;
}
```

---

### Part C — Functions, Generics, and Narrowing

**Topics:**
- Typing function parameters and return types
- Void functions: `function log(msg: string): void`
- Generics — reusable types parameterised over a type variable:
  ```typescript
  function identity<T>(value: T): T {
    return value;
  }
  ```
- Generic constraints: `<T extends { id: string }>`
- Type narrowing: `typeof`, `instanceof`, `in`, discriminated unions
- Discriminated unions — the TypeScript equivalent of sealed classes:
  ```typescript
  type Result<T> =
    | { ok: true; data: T }
    | { ok: false; error: string };
  ```
- `as const` — inferring literal types
- Non-null assertion `!` — use sparingly
- Utility types: `Partial<T>`, `Required<T>`, `Readonly<T>`, `Pick<T, K>`, `Omit<T, K>`

---

### Part D — TypeScript with React

**Topics:**
- Typing component props with an `interface`:
  ```typescript
  interface ButtonProps {
    label: string;
    onClick: () => void;
    disabled?: boolean;
  }

  function Button({ label, onClick, disabled = false }: ButtonProps) {
    return <button onClick={onClick} disabled={disabled}>{label}</button>;
  }
  ```
- Typing `useState`: `useState<Product[]>([])`
- Typing `useRef`: `useRef<HTMLInputElement>(null)`
- Typing event handlers: `React.ChangeEvent<HTMLInputElement>`, `React.FormEvent`
- Typing `useContext` — creating typed context with a non-null default
- Typing custom hooks — return type inference usually works; annotate when it doesn't
- `React.FC` — why many teams avoid it (doesn't add much, can obscure return type)
- Typing children: `React.ReactNode` for any renderable content

**Mini project:** Migrate the shopping cart app (Module 15) to TypeScript — add
interfaces for all domain types, type all component props, fix all `tsc --strict` errors.

---

### Part E — TypeScript Tooling and Next Steps

**Topics:**
- `tsconfig.json` important settings: `strict`, `target`, `module`, `paths`
- `strict: true` — enables all strict checks; use it from the start
- ESLint with `typescript-eslint` — catch type-aware linting errors
- Path aliases: `@/components/Button` instead of `../../components/Button`
- Declaration files `.d.ts` — what they are and when you'll encounter them
- `@types/*` packages — type definitions for untyped JavaScript libraries

**Where to go next with TypeScript:**
- Mapped types and conditional types — for advanced library code
- Template literal types
- `satisfies` operator (TypeScript 4.9+)
- Zod — runtime validation that integrates with TypeScript's type system

---

## Suggested Pacing (Full Curriculum)

| Module | Topic | Suggested Time |
|--------|-------|----------------|
| 1–3 | JS Basics, Control Flow, Arrays & Objects | Week 1–2 |
| 4–5 | Scope, Closures, Classes | Week 3 |
| 6–7 | Error Handling, DOM & Events | Week 4 |
| 8–9 | Async JS, Modules & Tooling | Week 5 |
| 10–11 | Functional Patterns, Fetch & Storage | Week 6 |
| Capstone | Vanilla JS Budget Tracker | Week 7 |
| 12–13 | React Intro, Hooks | Week 8–9 |
| 14–15 | Forms/Actions API, State Management | Week 10 |
| 16–17 | Router, Data Fetching | Week 11 |
| 18 | Performance, Activity, Next.js intro | Week 12 |
| 19 | TypeScript Fundamentals + React TS | Week 13–14 |

---

## ES2025 Features Reference (Woven Through Curriculum)

| Feature | Module | What it does |
|---|---|---|
| Iterator helpers (`map`, `filter` on iterators) | 3 | Chain operations without intermediate arrays |
| Set methods (`union`, `intersection`, `difference`) | 3 | Native set algebra |
| `Promise.try()` | 8 | Wrap sync/async code in a uniform promise |
| `RegExp.escape()` | 5/6 | Safely escape regex metacharacters |
| JSON modules with `import ... with { type: 'json' }` | 9 | Import JSON directly as a module |

---

## Recommended Resources

- **JavaScript:** [javascript.info](https://javascript.info) — the best free JavaScript book, covers ES2025
- **MDN:** [developer.mozilla.org](https://developer.mozilla.org) — authoritative reference for every API
- **React:** [react.dev](https://react.dev) — official React docs, updated for React 19
- **TypeScript:** [typescriptlang.org/docs](https://www.typescriptlang.org/docs/) — official handbook
- **Practice:** Exercism.io JavaScript track, Frontend Mentor for UI projects
- **Tooling:** VS Code, Vite, ESLint, Prettier, TypeScript strict mode
- **Next step after this curriculum:** Next.js 15 (full-stack React), TanStack Query deep dive, Zod for validation

---

## Curriculum Design Notes

The curriculum deliberately delays React until Module 12, after a solid vanilla JS
foundation. Most React bugs beginners encounter — stale closures, mutation of state,
async races — are JavaScript problems, not React problems. Understanding closures
(Module 4), immutability (Module 10), and the fetch API (Module 11) before touching
React makes the framework far easier to reason about.

The React section (Modules 12–18) is organised around React 19's actual programming
model: the Actions API replaces most manual loading/error state patterns, `<Activity />`
replaces conditional rendering for tabs, and the React Compiler reduces the need for
manual memoisation. Teaching these patterns from the start avoids the common pitfall
of learning outdated React 16/17 patterns that React 19 has superseded.

TypeScript is placed last intentionally. Learning TypeScript before JavaScript is
solid creates confusion about what is JavaScript and what is TypeScript. After the
React capstone, the learner has enough real JavaScript pain — unexpected `undefined`,
mistyped prop names, wrong event handler signatures — that TypeScript's value is
immediately obvious and motivating.
