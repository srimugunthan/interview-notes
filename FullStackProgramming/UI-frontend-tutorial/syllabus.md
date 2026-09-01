Here's the minimal, high-leverage path — skip anything not listed, since AI tools cover the rest.

## 1. HTML/CSS (1-2 weeks, just enough)
- Semantic tags, forms, basic accessibility (labels, alt text)
- Flexbox and CSS Grid — these two solve 95% of layout problems
- Skip: memorizing all CSS properties, deep animation work — AI generates this fine

## 2. JavaScript core (2-3 weeks, this is the non-negotiable part)
- Variables, functions, arrays/objects, destructuring
- Async/await, promises, fetch — how network calls actually work
- DOM basics: what happens when you call `document.querySelector`, event listeners, event bubbling
- Closures and `this` — enough to debug, not to write from scratch
- **Why this can't be skipped:** you need this to read and critique AI-generated code. If you can't tell whether a `useEffect` dependency array is wrong, you can't ship reliably.

## 3. One framework — React (2-3 weeks)
- Components, props, state (`useState`), effects (`useEffect`)
- Conditional rendering, lists/keys
- How data flows down, events flow up
- One fetch-based app end to end (call an API, render a list, handle loading/error states)
- Skip: Redux, complex state libraries, animation libraries — learn only if a project demands it

## 4. Tooling literacy (days, not weeks)
- npm/package.json basics
- What a bundler does conceptually (Vite is standard now) — you don't need to configure one from scratch
- Browser DevTools: console, network tab, elements panel — this is where AI can't help you, you have to look yourself

## 5. The AI-era addition: code review skill
- Practice reading AI-generated components and spotting: unnecessary re-renders, missing keys, prop drilling that should be context, unhandled loading/error states
- This is arguably now more valuable than raw writing speed

## What to explicitly skip
- TypeScript (learn later, once JS is solid — don't front-load it)
- CSS-in-JS, styled-components, Tailwind deep dives (pick up whichever your project uses, in a day)
- Testing frameworks (learn when you need to test something real)
- Webpack config, build tool internals
- Multiple frameworks (Vue, Svelte, Angular) — read-fluency only, via AI explanations, not hands-on projects

**Total realistic timeline:** 6-8 weeks of focused work to be genuinely productive building real apps with AI assistance, versus the old 6-12 month bootcamp timeline. The compression comes entirely from skipping syntax memorization and deep tooling config — the conceptual core (state, data flow, async, DOM) is unchanged and still has to be learned by you, not delegated.