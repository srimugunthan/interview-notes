# Chapter 5: Code Review Skill for the AI Era

The syllabus calls this "arguably now more valuable than raw writing speed." The reasoning: AI tools can generate a plausible-looking React component in seconds, but "plausible-looking" and "correct" are different bars. Your job shifts from *typing code* to *knowing what's wrong with code you didn't write*. This chapter is a checklist of the specific failure patterns to scan for, each with a broken example and the fix — practice spotting the bug *before* reading the fix.

## 5.1 Unhandled loading/error states

The most common shortcut AI-generated code takes: showing only the happy path.

```jsx
// 🚩 Broken: what does the user see for the ~1 second before data arrives?
// What happens if the fetch fails — a network blip, a 500, a typo'd URL?
function UserList() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    fetch("/api/users")
      .then((res) => res.json())
      .then((data) => setUsers(data));
  }, []);

  return (
    <ul>
      {users.map((u) => (
        <li key={u.id}>{u.name}</li>
      ))}
    </ul>
  );
}
```

Two bugs stacked here: no loading state (renders an empty `<ul>` with zero feedback while waiting), and no error handling at all — `.then((res) => res.json())` doesn't check `res.ok`, so a 404 page's HTML gets silently `JSON.parse`'d and throws inside a `.then` with no `.catch` to see it, or the fetch just quietly does nothing on rejection.

```jsx
// ✅ Fixed — see Chapter 3.5 for the full pattern
function UserList() {
  const [users, setUsers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let cancelled = false;
    fetch("/api/users")
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      .then((data) => {
        if (!cancelled) setUsers(data);
      })
      .catch((err) => {
        if (!cancelled) setError(err.message);
      })
      .finally(() => {
        if (!cancelled) setIsLoading(false);
      });
    return () => { cancelled = true; };
  }, []);

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p role="alert">Failed to load users: {error}</p>;
  return <ul>{users.map((u) => <li key={u.id}>{u.name}</li>)}</ul>;
}
```

**Review question to ask of every fetch you review:** "What renders while this is pending, and what renders if it rejects?" If you can't answer both from reading the code, that's a finding.

## 5.2 Missing or wrong `key` props

```jsx
// 🚩 Broken: index as key
function TodoList({ todos }) {
  return (
    <ul>
      {todos.map((todo, index) => (
        <li key={index}>
          <input type="checkbox" defaultChecked={todo.done} />
          {todo.text}
        </li>
      ))}
    </ul>
  );
}
```

This looks fine and often *works* — until an item is deleted from the middle of the list, or the list is sorted. React uses `key` to match old DOM nodes to new ones across a re-render. With an index key, deleting item 1 shifts item 2 into index 1's slot — React thinks item 1 was merely *edited*, not that item 2 moved into its place. Any uncontrolled local state per item — like that `defaultChecked` checkbox — stays attached to the *position*, not the *item*, so checkboxes appear to jump to the wrong todos.

```jsx
// ✅ Fixed — use a stable identifier from the data itself
function TodoList({ todos }) {
  return (
    <ul>
      {todos.map((todo) => (
        <li key={todo.id}>
          <input type="checkbox" defaultChecked={todo.done} />
          {todo.text}
        </li>
      ))}
    </ul>
  );
}
```

**Review question:** does this list ever get reordered, filtered, or have items removed from the middle? If yes, and the key is the array index, that's a finding — even if it "looks right" in the current render.

## 5.3 Stale/wrong `useEffect` dependencies

Covered conceptually in [Chapter 3.3](./03-react.md#33-effects--useeffect) — here's what it looks like as a bug in the wild.

```jsx
// 🚩 Broken: effect reads `query` but doesn't declare it as a dependency
function SearchResults({ query }) {
  const [results, setResults] = useState([]);

  useEffect(() => {
    fetch(`/api/search?q=${query}`)
      .then((res) => res.json())
      .then(setResults);
  }, []); // <- query is missing here

  return <ResultsList items={results} />;
}
```

When `query` changes (say, the user types a new search term and a parent updates the prop), this effect does **not** re-run — it ran once on mount with whatever `query` was *then*, and the closure over `query` is now stale. Results silently stop updating. This is subtle because the component still renders without errors; it just quietly does the wrong thing.

```jsx
// ✅ Fixed
useEffect(() => {
  fetch(`/api/search?q=${query}`)
    .then((res) => res.json())
    .then(setResults);
}, [query]);
```

**Review question:** list every variable the effect body reads from component scope. Is each one either in the dependency array, or a value that provably never changes (like a `setState` function, which React guarantees is stable)? Anything read-but-not-declared is a finding.

## 5.4 Unnecessary re-renders

```jsx
// 🚩 Broken: a new function and a new object are created on every render
// of Parent, so React.memo on Child never actually prevents a re-render —
// the props are never `===` equal to the previous render's props.
function Parent() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <button onClick={() => setCount(count + 1)}>Increment: {count}</button>
      <ExpensiveChild
        onSave={() => console.log("saved")}
        options={{ theme: "dark" }}
      />
    </div>
  );
}

const ExpensiveChild = React.memo(function ExpensiveChild({ onSave, options }) {
  console.log("ExpensiveChild rendered"); // fires every single time Parent renders
  // ...expensive rendering work...
  return <button onClick={onSave}>Save ({options.theme})</button>;
});
```

`React.memo` skips a re-render only if props are shallowly equal to last time. An inline arrow function or object literal is a *brand new reference* every render, so the memo check always fails and the optimization does nothing — worse, it adds an equality check for no benefit.

```jsx
// ✅ Fixed: stable references via useCallback/useMemo
function Parent() {
  const [count, setCount] = useState(0);

  const handleSave = useCallback(() => console.log("saved"), []);
  const options = useMemo(() => ({ theme: "dark" }), []);

  return (
    <div>
      <button onClick={() => setCount(count + 1)}>Increment: {count}</button>
      <ExpensiveChild onSave={handleSave} options={options} />
    </div>
  );
}
```

**Review question:** is this optimization (`React.memo`, `useCallback`, `useMemo`) actually doing anything, or is it decorative because the props passed in are re-created every render? Don't just check that the hook is *present* — check that it's *effective*. Also worth asking the inverse: is this memoization even needed here, or is it premature complexity around a component that's cheap to re-render anyway? Not every component needs `React.memo`.

## 5.5 Prop drilling that should be context

```jsx
// 🚩 Smell: `theme` is threaded through three components that don't use it
// themselves — they only pass it along to a grandchild.
function App() {
  const [theme, setTheme] = useState("dark");
  return <Page theme={theme} />;
}
function Page({ theme }) {
  return <Sidebar theme={theme} />;
}
function Sidebar({ theme }) {
  return <ThemeToggleButton theme={theme} />;
}
```

This isn't wrong, exactly — it works. But it's a maintenance liability: adding a fourth layer means editing three files, and it's not obvious from `Page`'s or `Sidebar`'s signature that `theme` is just passing through. This is a *judgment call*, not a hard rule — two or three levels of drilling for a couple of props is often fine and simpler than the alternative. Reach for Context when the chain gets long, or many unrelated components across the tree all need the same value.

```jsx
// ✅ Fixed with Context — used sparingly, for genuinely cross-cutting state
const ThemeContext = createContext("dark");

function App() {
  const [theme, setTheme] = useState("dark");
  return (
    <ThemeContext.Provider value={theme}>
      <Page />
    </ThemeContext.Provider>
  );
}
function Page() {
  return <Sidebar />; // no longer needs to know about theme at all
}
function Sidebar() {
  return <ThemeToggleButton />;
}
function ThemeToggleButton() {
  const theme = useContext(ThemeContext); // pulled directly, no drilling
  return <button>{theme}</button>;
}
```

**Review question:** does this prop's name keep reappearing in components that never read it, only forward it? If a value threads through 3+ intermediate components untouched, flag it as a context candidate — but don't reflexively demand Context for two levels of drilling; that's often the simpler, more readable option.

## 5.6 A review checklist to run on any AI-generated component

Run through these every time, in this order — cheapest checks first:

1. **Loading/error states** — does every async call handle pending and rejected, not just resolved?
2. **Keys** — is every `.map()`-rendered list using a stable id, not the array index?
3. **Effect dependencies** — does the dependency array include everything the effect body reads from component scope?
4. **Memoization sanity** — if `useCallback`/`useMemo`/`React.memo` are present, are they actually preventing anything, or decorative? If absent, is a genuinely expensive render happening on every keystroke?
5. **State mutation** — is state ever mutated directly (`state.x = y`) instead of replaced (`setState({...state, x: y})`)?
6. **Prop drilling** — is a prop threading through several components that never use it themselves?
7. **Cleanup** — does an effect that subscribes, sets a timer, or fetches also clean up (unsubscribe, `clearTimeout`, ignore late responses) on unmount?

## Try it

1. Take the broken `SearchResults` component from 5.3 and, without looking at the fix, write down in one sentence what user-visible symptom this bug produces (not "the dependency array is wrong" — what does someone *using the app* actually experience?). Then verify by pasting the broken version into a small test app and reproducing it.
2. Ask an AI assistant to generate a React component that fetches and displays a list of items with a delete button per item. Run the checklist above against what it gives you — you should find at least one issue on the first try more often than not. Fix it yourself before asking the AI to.
3. Take a component you're proud of from Chapter 3's `PostList` exercise and run the same seven-point checklist against your *own* code. This is the actual point of the chapter: the checklist isn't AI-specific, it's just correctness — AI-generated code just gives you more reps to practice on, faster.
