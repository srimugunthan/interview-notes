# Chapter 3: React Fundamentals

React is the one framework the syllabus picks — not because it's objectively best, but because it's the most common target for AI-generated code you'll be reading. Everything here assumes Chapter 2's JavaScript is solid; React is "just" JavaScript with a particular set of conventions layered on top.

Setup (needs Node.js + npm — see [Chapter 4](./04-tooling.md) if you haven't set that up yet):

```bash
npm create vite@latest my-app -- --template react
cd my-app
npm install
npm run dev
```

## 3.1 Components and props

A component is a function that returns UI (via JSX — HTML-like syntax inside JavaScript). **Props** are how a parent passes data down to a child — read-only from the child's perspective.

```jsx
// Card.jsx
function Card({ title, children }) {
  return (
    <div className="card">
      <h2>{title}</h2>
      <div>{children}</div>
    </div>
  );
}

// App.jsx
function App() {
  return (
    <Card title="Welcome">
      <p>This content is passed as `children`.</p>
    </Card>
  );
}

export default App;
```

- Component names are capitalized (`Card`, not `card`) — that's how JSX tells a component apart from a plain HTML tag.
- Props flow **one direction: down**. A parent re-renders and passes new props; the child never reaches up and mutates the parent's data directly. If a child needs to change something in the parent, the parent passes a *function* down as a prop, and the child calls it — that's "events flow up."

```jsx
function Parent() {
  const handleSave = (value) => console.log("Saved:", value);
  return <Child onSave={handleSave} />;
}

function Child({ onSave }) {
  return <button onClick={() => onSave("some data")}>Save</button>;
}
```

## 3.2 State — `useState`

Props are data passed in from outside; **state** is data a component owns and can change over time. Changing state triggers a re-render.

```jsx
import { useState } from "react";

function Counter() {
  const [count, setCount] = useState(0); // [currentValue, setterFunction]

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>+1</button>
      <button onClick={() => setCount((prev) => prev - 1)}>-1</button>
    </div>
  );
}
```

Two rules that cause most beginner bugs:

1. **State updates are asynchronous and batched.** `count` inside the same render doesn't change immediately after calling `setCount`. If your new value depends on the old one, use the function form: `setCount((prev) => prev + 1)`.
2. **Never mutate state directly.** `setUser(user.name = "new")` is wrong. React compares references to decide whether to re-render — mutating in place doesn't create a new reference, so React may not notice.

```jsx
// Wrong — mutates the existing object
const [user, setUser] = useState({ name: "Ada", age: 30 });
user.age = 31;
setUser(user); // same reference — React may skip the re-render

// Right — create a new object
setUser({ ...user, age: 31 });

// Same idea for arrays
const [items, setItems] = useState([1, 2, 3]);
setItems([...items, 4]);              // add
setItems(items.filter((i) => i !== 2)); // remove
```

## 3.3 Effects — `useEffect`

`useEffect` runs code in response to a component rendering — typically for anything that reaches *outside* React: fetching data, subscriptions, timers, manually touching the DOM.

```jsx
import { useState, useEffect } from "react";

function UserProfile({ userId }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    let cancelled = false;

    async function loadUser() {
      const response = await fetch(`/api/users/${userId}`);
      const data = await response.json();
      if (!cancelled) setUser(data); // guard against setting state after unmount
    }

    loadUser();

    return () => {
      cancelled = true; // cleanup function — runs before the next effect or on unmount
    };
  }, [userId]); // dependency array: re-run this effect only when userId changes

  if (!user) return <p>Loading...</p>;
  return <p>{user.name}</p>;
}
```

The dependency array is the single most common source of AI-generated React bugs — see [Chapter 5](./05-code-review.md) for how to audit it. The mental model:

- `useEffect(fn)` — no array — runs after **every** render. Rarely what you want.
- `useEffect(fn, [])` — empty array — runs **once**, after the first render only.
- `useEffect(fn, [a, b])` — runs after the first render, and again whenever `a` or `b` changes between renders.
- Every value from component scope that the effect *reads* should be in the array, or you're reading stale data — a "stale closure," same root cause as the closure loop bug from Chapter 2.

## 3.4 Conditional rendering, lists, and keys

```jsx
function ItemList({ items, isLoading }) {
  if (isLoading) {
    return <p>Loading...</p>;
  }

  if (items.length === 0) {
    return <p>No items yet.</p>;
  }

  return (
    <ul>
      {items.map((item) => (
        <li key={item.id}>{item.name}</li>
      ))}
    </ul>
  );
}
```

- Conditional rendering is just JavaScript — early `return`, ternaries (`{condition ? <A /> : <B />}`), or `&&` (`{items.length > 0 && <List />}`).
- `key` on list items must be a **stable, unique identifier** (a database id, not the array index) so React can correctly track which DOM node corresponds to which data item across re-renders — especially when items are reordered, inserted, or removed. Using the array index as a key breaks this tracking when the list order changes and can cause state to leak between items.

## 3.5 A full fetch-based app: list, loading, and error states

This ties 3.1–3.4 together — the shape the syllabus specifically calls out: "call an API, render a list, handle loading/error states."

```jsx
import { useState, useEffect } from "react";

function PostList() {
  const [posts, setPosts] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let cancelled = false;

    async function loadPosts() {
      setIsLoading(true);
      setError(null);
      try {
        const response = await fetch("https://jsonplaceholder.typicode.com/posts?_limit=10");
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const data = await response.json();
        if (!cancelled) setPosts(data);
      } catch (err) {
        if (!cancelled) setError(err.message);
      } finally {
        if (!cancelled) setIsLoading(false);
      }
    }

    loadPosts();
    return () => {
      cancelled = true;
    };
  }, []);

  if (isLoading) return <p>Loading posts...</p>;
  if (error) return <p role="alert">Something went wrong: {error}</p>;

  return (
    <ul>
      {posts.map((post) => (
        <li key={post.id}>
          <strong>{post.title}</strong>
          <p>{post.body}</p>
        </li>
      ))}
    </ul>
  );
}

export default PostList;
```

Notice all three states are handled explicitly: loading, error, and success — this three-state pattern is the baseline every data-fetching component should hit, and it's exactly what's easy to skip when AI generates a "happy path only" version.

## Try it

1. Build the `Counter` component above, then add a second button that resets to 0. Add a piece of derived text below it: `"even"` or `"odd"` depending on `count` — no extra state needed, just compute it during render.
2. Build `PostList` above from scratch (don't copy-paste — type it), then deliberately break it: comment out the `[]` dependency array's brackets so it becomes `useEffect(fn)` with no array, and watch the network tab flood with requests. Put it back, then try `[somethingThatNeverChanges]` and confirm the effect only runs once either way — get a feel for *why* the array matters, not just the rule.
3. Modify `PostList` to accept a `userId` prop and fetch `https://jsonplaceholder.typicode.com/posts?userId=${userId}&_limit=10` instead, adding `userId` to the dependency array. Render two `<PostList userId={1} />` and `<PostList userId={2} />` side by side and confirm each fetches independently.
