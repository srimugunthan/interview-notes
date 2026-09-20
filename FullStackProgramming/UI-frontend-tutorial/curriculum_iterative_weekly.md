# HTML, CSS & TypeScript — Iterative Learning Plan

**Duration:** 16 Weeks  
**Philosophy:** Get productive immediately. Week 1 gives you the essential 20% that handles 80% of real work. Every subsequent week layers depth on top of a working mental model.

---

## Week 1 — Core Essentials of HTML, CSS & TypeScript (All Three Together)

**Goal:** By end of Week 1, you can build and inspect a working, styled, interactive web page with TypeScript.

### HTML Essentials
- Document structure: `<html>`, `<head>`, `<body>`, `<main>`, `<section>`, `<article>`
- Block vs inline elements; void elements (`<img>`, `<input>`, `<br>`)
- Key semantic elements: `<nav>`, `<header>`, `<footer>`, `<h1>`–`<h6>`, `<p>`, `<ul>`, `<a>`
- Basic form: `<form>`, `<input>`, `<label>`, `<button>`
- The DOM tree: how browsers read HTML and build the element tree

### CSS Essentials
- How to link a stylesheet; inline vs class vs ID selectors
- Box model basics: `margin`, `padding`, `border`, `width`, `height`
- `box-sizing: border-box` — set it globally, never fight widths again
- Colors, fonts, units: `px`, `rem`, `%`
- One layout tool: **Flexbox** — `display: flex`, `justify-content`, `align-items`, `gap`

### TypeScript Essentials
- TypeScript setup: `tsconfig.json` with `strict: true`, `lib: ["DOM"]`, source maps
- Primitive types: `string`, `number`, `boolean`
- Selecting DOM elements: `document.querySelector` typed as `HTMLElement`
- Handling null: `if (el)` checks before use; optional chaining `?.`
- A basic event listener: `el.addEventListener('click', (e: MouseEvent) => {})`

### DevTools Essentials
- Open Elements panel — inspect any element live
- Open Console — read JS/TS errors
- Edit CSS live in the Styles panel
- `console.log` for quick variable inspection

### Lab
Build a styled card component with a button that toggles a class on click — written in TypeScript, styled in CSS, structured in semantic HTML.

---

## Week 2 — HTML Depth: Semantics, Forms & DOM Debugging

### Topics
- Full semantic HTML: landmark roles, `<aside>`, `<figure>`, `<time>`, `<details>`
- Forms deep dive: all input types, native validation attributes, `novalidate`, custom error messages
- ARIA basics: `aria-label`, `aria-describedby`, `role` — when to use vs native semantics
- `defer` vs `async` on script tags — why order matters for DOM access
- DOM breakpoints in DevTools: subtree modifications, attribute changes, node removal

### Debug Skills
- Fix missing closing tags and invalid nesting
- Debug form submission failures (wrong `name`, missing `action`, button type)
- "Why is my element not showing?" → verify it exists in the live DOM (not just source)

### Lab
Fix a deliberately broken form: missing labels, broken submit, inaccessible markup — using only DevTools.

---

## Week 3 — CSS Depth: Cascade, Specificity & the Box Model

### Topics
- Specificity scoring: inline > ID > class > element (calculate it, don't guess)
- Inheritance: which properties cascade down and which don't
- The `!important` trap — how it creates unresolvable conflicts
- Cascade layers (`@layer`) — the modern way to control CSS priority
- Margin collapse: adjacent siblings and parent-child edge cases
- `overflow: hidden/scroll/clip` — impact on layout, scrollbars, and stacking
- User-agent stylesheet — the baseline you're always overriding

### Debug Skills
- Unapplied styles → check Styles panel for crossed-out rules
- Collapsing margins → use `display: flow-root` or `padding: 1px` to escape
- Computed tab — see the final resolved value after cascade and inheritance

### Lab
Given a component with a specificity war and margin collapse bug — fix without using `!important`.

---

## Week 4 — CSS Layout: Flexbox Deep Dive & Grid Introduction

### Topics
- **Flexbox complete:** `flex-grow`, `flex-shrink`, `flex-basis`, `align-self`, `flex-wrap`, `order`
- Unexpected overflow in flex: `min-width: 0` fix
- **CSS Grid:** `grid-template-columns`, `grid-template-rows`, `grid-template-areas`
- Explicit vs implicit tracks; `auto-fill` vs `auto-fit`
- Grid gap, `grid-column`, `grid-row` spanning
- Flex vs Grid — when to choose which

### Debug Skills
- Flex/Grid overlays in DevTools Layout panel
- Track numbers, gap visualisation, alignment problems
- "Why is my flex item overflowing?" → `min-width: 0`

### Lab
Build a responsive page layout (header, sidebar, main content, footer) using Grid for the outer shell and Flexbox for inner components.

---

## Week 5 — CSS Layout: Positioning, Stacking & Responsive Design

### Topics
- `position: static / relative / absolute / fixed / sticky` — what each is relative to
- Stacking context: what creates one (`z-index`, `opacity`, `transform`, `filter`)
- `z-index` wars: auditing stacking contexts across a page
- `@media` queries: `min-width` vs `max-width`; mobile-first strategy
- Viewport meta tag; `dvh`/`svh`/`lvh` for mobile viewports
- CSS variables: `--token: value`, fallbacks, inheritance in the cascade
- `clamp()`, `min()`, `max()` for fluid typography and spacing
- Container queries — component-level breakpoints

### Debug Skills
- `z-index` not working → find the stacking context ancestor
- Mobile layout broken → check viewport meta + `100vh` on iOS
- DevTools device emulation for responsive testing

### Lab
Fix a broken responsive card grid: z-index overlap, mobile breakpoint failure, sticky nav not sticking.

---

## Week 6 — TypeScript Depth: Type System & Narrowing

### Topics
- `type` aliases vs `interface` — differences and when to choose each
- Union types (`|`) for state modelling: `'loading' | 'success' | 'error'`
- Intersection types (`&`) for composing shapes
- Literal types for precise values
- Generics: `Array<T>`, `Promise<T>`, writing your own `<T>`
- `Partial<T>`, `Required<T>`, `Pick<T>`, `Omit<T>`, `Record<K,V>` utility types
- `unknown` vs `any` — always prefer `unknown` for external data

### Debug Skills
- `property does not exist on type` → model the shape correctly
- Reading complex compiler error messages — decode them step by step

### Lab
Model a UI state machine for a data-fetching component — `idle | loading | success | error` — with discriminated unions and exhaustiveness checking using `never`.

---

## Week 7 — TypeScript Depth: DOM Typing & Event Handling

### Topics
- `HTMLElement` hierarchy: `HTMLDivElement`, `HTMLInputElement`, `HTMLButtonElement`
- Typing `querySelector`: `as HTMLInputElement` vs non-null assertion `!` — when each is safe
- `EventTarget` vs `Element` vs `HTMLElement` — the type chain
- Typed event handlers: `MouseEvent`, `KeyboardEvent`, `InputEvent`, `FocusEvent`
- `e.target` vs `e.currentTarget` — why they are different types
- `strictNullChecks` patterns: null guards, `?.`, `??`
- Null-safe DOM access: timing issues (onload vs dynamic injection)

### Debug Skills
- `Cannot read property of null` → trace DOM query timing
- Event handler type mismatch → TS catches it before runtime
- Non-null assertion misuse — know when `!` is a lie

### Lab
Build a typed form validator: read input values with correct types, show inline errors, handle submit — no `any`, no unchecked assertions.

---

## Week 8 — TypeScript Depth: Async Patterns & Error Handling

### Topics
- `Promise<T>` typing; `async`/`await` with explicit return types
- Typed `fetch` wrapper with response validation
- Error handling: `try/catch` with `unknown` error type, `instanceof Error` narrowing
- Custom error classes with typed properties
- Race conditions: `AbortController` for cancellable requests
- `Promise.all`, `Promise.allSettled`, `Promise.race` — use cases and types

### Debug Skills
- Stale closures and missing cleanup in event listeners
- Async DOM update race conditions — why your element is null on first render
- Unhandled promise rejections in the console

### Lab
Build a typed search-as-you-type component with debounce, AbortController cancellation, and typed error handling for failed fetches.

---

## Week 9 — TypeScript Toolchain: Build Setup, Source Maps & Compiler Mastery

### Topics
- `tsconfig.json` deep dive: `strict`, `noUncheckedIndexedAccess`, `exactOptionalPropertyTypes`
- Source maps: how to configure them and use them in DevTools breakpoints
- Build tooling: Vite setup for TypeScript projects, ESLint + Prettier configuration
- `ts-node` / `tsx` for running TS directly
- VS Code power features: hover types, go-to-definition, rename refactor, "Find all references"
- Declaration files (`.d.ts`) — reading them to understand library types

### Debug Skills
- Trace a production runtime error back to a TypeScript line via source maps
- Read and fix complex type mismatch compiler errors
- Use VS Code's type inference instead of guessing

### Lab
Configure a full Vite + TypeScript project from scratch. Intentionally introduce three compiler errors and fix them using only the compiler output and VS Code hover types — no Googling the fix directly.

---

## Week 10 — CSS Advanced: Animations, Transitions & Performance

### Topics
- `transition`: property, duration, timing, delay — common pitfalls
- `@keyframes` and `animation` shorthand
- `transform` for performant animations (GPU-accelerated; does not trigger layout)
- `will-change`: what it does and when it backfires
- CSS containment (`contain: layout style`) for performance isolation
- Paint vs layout vs composite — which CSS properties trigger which pipeline stage

### Debug Skills
- Animation jank → Performance panel flame chart
- `transform` creates a stacking context → breaks `z-index` children
- Paint flashing in DevTools Rendering tab

### Lab
Fix animation jank in a slide-in card component: replace layout-triggering properties with `transform`, eliminate unintended stacking context.

---

## Week 11 — Integrated Debugging: Rendering, Network & Runtime

**Goal:** Diagnose bugs that span all three layers simultaneously.

### The Systematic Debug Process (Formalised Here)
1. Reproduce reliably — find the minimum reproduction steps
2. Observe — read the full error message before acting
3. Hypothesize — one specific theory
4. Isolate — smallest failing case (CodePen/StackBlitz)
5. Test — prove or disprove
6. Fix — one change at a time
7. Verify — confirm no regressions

### Module: Rendering Bugs
- Invisible elements: `display: none` vs `visibility: hidden` vs `opacity: 0` vs `height: 0`
- FOUC (Flash of Unstyled Content): CSS load order, critical CSS
- Layout thrashing: reading `offsetWidth` in a loop → Performance panel reflow markers

### Module: Network & Data Bugs
- Network panel: XHR/Fetch filtering, timing breakdown, waterfall analysis
- CORS errors: reading the error message, what the browser blocks and why
- Content-Type mismatches; JSON with unexpected `null` fields

### Module: Runtime Debugging
- Breakpoints: line, conditional, logpoint
- Call stack reading — trace how you got to the error
- `console.table`, `console.group`, `console.time` — structured debugging

### Labs
- Fix a component where data is undefined at render time (network timing bug)
- Fix a CORS error without changing the backend (header + proxy config)

---

## Week 12 — Performance Debugging & Core Web Vitals

### Topics
- Performance panel: recording, reading the flame chart, identifying long tasks
- Core Web Vitals: LCP, CLS, INP — definitions, tools, thresholds
- Layout thrashing: reading and writing layout properties in alternation
- `requestAnimationFrame` vs `setTimeout` for visual updates
- Heap snapshots: identifying memory leaks and detached DOM nodes
- Event listener audit: `getEventListeners(node)` — catching listener accumulation

### Debug Skills
- "Why does my page jump on load?" → CLS in Performance panel
- "Why is my page slow?" → flame chart + long task detection
- Memory leak detection → heap snapshot before/after interaction

### Lab
Profile a slow-loading dashboard: find and fix layout thrashing, a detached DOM node leak, and a CLS-causing image without dimensions.

---

## Week 13 — Cross-Browser, Device & Accessibility Debugging

### Topics
- `@supports` for progressive enhancement — query feature support in CSS
- Common cross-browser bugs: `gap` in older Safari flex, `subgrid` support
- Vendor prefix traps — when and how to check `caniuse.com`
- Mobile viewport: `100vh` on iOS, keyboard layout shift, safe area insets
- Remote debugging: Chrome DevTools on Android, Safari Web Inspector on iOS
- Accessibility debugging: screen readers, contrast ratios, keyboard navigation
- `axe` DevTools and WAVE for automated a11y audits
- Lighthouse: Performance, Accessibility, Best Practices — reading and prioritising results

### Labs
- Remote debug a mobile-Safari-only layout bug
- Run axe on a component system; fix all critical violations

---

## Week 14 — Production Debugging: Observability & Advanced Techniques

### Topics
- Source map upload to Sentry/LogRocket so production errors map to TS lines
- Error grouping and noise filtering: real bugs vs browser extensions
- Session replay — watching the exact user journey before a crash
- `window.onerror` and `unhandledrejection` handlers
- `git bisect`: binary search through commits to find regression origin
- Overrides panel: replace a production JS/CSS file with a local edited version
- Minimal Reproducible Example (MRE): isolating bugs to CodePen/StackBlitz
- Post-mortem writing: root cause, timeline, prevention

### Labs
- Use `git bisect` to find the commit that introduced a layout regression
- Use the Overrides panel to patch a third-party script bug without source access

---

## Weeks 15–16 — Capstone: Broken UI Dashboard

**Goal:** Debug a deliberately broken, production-scale UI with no hints given.

### Bugs Embedded in the Dashboard
- Mixed HTML nesting and accessibility failures
- CSS specificity war (`!important` conflicts)
- Z-index stacking context mess
- TypeScript null reference (querySelector called before DOM is ready)
- Event listener memory leak
- Responsive failure (mobile layout broken on Safari)
- CORS-blocked API call silently failing
- Layout thrashing causing 3-second interaction delay
- A component that re-renders infinitely

### Deliverable: Debug Report
For each bug:
1. Reproduction steps
2. Root cause (e.g., "collapsing margin due to adjacent `<p>` elements")
3. Fix and prevention
4. Commit diff

### Final Assessment
- ✅ Timed challenge: fix 5 complex UI bugs in under 90 minutes
- ✅ Lighthouse score ≥ 90 (Performance, Accessibility, Best Practices)
- ✅ axe compliance: 0 critical violations
- ✅ Code review: type safety, CSS architecture, semantic markup
- ✅ Personal debug playbook: your own systematic troubleshooting reference

---

## Progression Map

```
Week 1  ───── Essentials of all three (build something working)
Week 2  ──┐
Week 3  ──┤  HTML & CSS depth
Week 4  ──┤
Week 5  ──┘
Week 6  ──┐
Week 7  ──┤  TypeScript depth
Week 8  ──┤
Week 9  ──┘
Week 10 ─────  CSS advanced (animations + performance)
Week 11 ─────  Integrated debugging across all layers
Week 12 ─────  Performance & memory
Week 13 ─────  Cross-browser, mobile, accessibility
Week 14 ─────  Production observability & advanced techniques
Weeks 15–16 ── Capstone
```
