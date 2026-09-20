# HTML, CSS & TypeScript — Comprehensive Curriculum (Deduplicated)

**Duration:** 16 Weeks | **Mode:** Instructor-led or Self-Paced  
**Prerequisites:** Basic programming familiarity (variables, functions, control flow)  
**Target Outcome:** Systematically identify, isolate, and resolve any UI-related bug using industry-standard tooling, mental models, and debugging workflows.

> **Debugging Mindset (runs throughout):** Reproduce → Isolate → Hypothesize → Fix → Verify

---

## Prerequisites & Setup

- **Tools:** VS Code (TypeScript language server, CSS IntelliSense), Chrome + Firefox + Safari DevTools (Elements, Console, Sources, Network, Performance, Memory, Layers panels), Git
- **References to install/bookmark:** MDN Web Docs, TypeScript Handbook, `caniuse.com`, CodePen/StackBlitz (minimal repro environment)

---

## Phase 1 — HTML Foundations (Weeks 1–2)

**Goal:** Understand how browsers parse and render HTML so you can reason about the DOM before touching CSS or JS.

### Module 1.1 — Document Model
- How browsers parse HTML top-to-bottom (parse order matters for script loading)
- DOM tree structure: nodes, elements, text nodes, attributes
- Block vs inline vs void elements
- `<head>` vs `<body>` — what belongs where and why
- `defer` vs `async` on script tags — race conditions explained
- Critical rendering path: DOM → CSSOM → Render Tree → Layout → Paint → Composite

### Module 1.2 — Semantics & Accessibility
- Landmark elements: `<main>`, `<nav>`, `<aside>`, `<section>`, `<article>`
- Why semantic HTML matters for tab order and screen readers
- ARIA roles, `aria-label`, `aria-describedby` — when to use, when not to
- Form elements: `<label>` association, `fieldset`/`legend`, input types, native validation, `novalidate`, custom error messages
- Common a11y bugs: missing `alt` text, duplicate IDs, broken focus traps

### Module 1.3 — DevTools: Elements Panel
- Inspecting the live DOM vs source HTML (they are different)
- Live-editing HTML attributes and content
- Force element states (`:hover`, `:focus`, `:active`) for testing
- Event listeners tab — finding what's attached to an element
- DOM breakpoints: subtree modifications, attribute changes, node removal
- Accessibility panel — computed ARIA tree

### Debug Skills Unlocked
- "Why is my element not showing?" → check if it exists in the DOM at all
- "Why is my form not submitting?" → inspect label associations and button type
- Fix missing closing tags, invalid nesting
- Debug form submission failures (wrong `name`, missing `action`)

### Lab
- Break a layout by misnesting tags → fix using only DevTools
- Fix broken semantic markup causing accessibility & layout issues

---

## Phase 2 — CSS Mastery (Weeks 3–5)

**Goal:** Predict exactly how any element is styled, and know precisely why a rule is or isn't applied.

### Module 2.1 — The Cascade & Specificity
- Specificity calculation: inline > ID > class > element (with exact scoring)
- Inheritance: which properties inherit and which don't
- The `!important` trap — why it creates unfixable conflicts
- Cascade layers (`@layer`) and how they interact with specificity
- User-agent stylesheet — the baseline every browser starts from
- CSS architecture patterns: BEM, `:is()`, `:where()`, `:has()`

### Module 2.2 — Box Model
- `content-box` vs `border-box` — why `box-sizing: border-box` is universally set
- Margin collapse: adjacent siblings and parent-child cases
- Negative margins and their legitimate uses
- `overflow: hidden/scroll/clip/visible` — impact on layout and stacking
- How `padding` on inline elements behaves differently than on blocks

### Module 2.3 — Layout Systems
- **Flexbox:** main axis vs cross axis, `flex-grow/shrink/basis`, `align-self`, `gap`
- **CSS Grid:** explicit vs implicit tracks, `grid-template-areas`, `auto-fill` vs `auto-fit`, subgrid for aligned nested layouts
- `position: static/relative/absolute/fixed/sticky` — what each is relative to
- Stacking context: what creates one (`z-index`, `opacity`, `transform`, `filter`)

### Module 2.4 — CSS Custom Properties & Responsiveness
- CSS variables: declaration, inheritance, fallback values (`var(--primary, blue)`)
- `@media` queries: `min-width` vs `max-width` strategy, viewport meta tag
- `clamp()`, `min()`, `max()` for fluid sizing
- Container queries — component-level responsiveness
- `dvh`/`svh`/`lvh` — mobile viewport quirks

### Module 2.5 — DevTools: Styles & Layout Panels
- Reading the Styles panel: cascade order, crossed-out rules, specificity winner
- Computed tab: final resolved values after cascade and inheritance
- Flex inspector and Grid overlay — visualizing tracks, gaps, alignment
- Animating CSS properties in the Styles panel to find breakpoints

### Debug Skills Unlocked
- Unapplied styles (specificity, cascade order, invalid property)
- Unexpected overflow: `overflow: auto`, `min-width: 0` in flex
- `z-index` not working → identify stacking context
- Collapsing margins with adjacent siblings or parent-child
- "Why is my layout broken?" → use flex/grid overlay

### Labs
1. Fix a broken card component (margin collapse + z-index bug)
2. Force dark mode with media query → break it → fix using only DevTools
3. Fix overlapping elements caused by stacking context mismanagement
4. Breakpoint isolation: find why a component breaks at `768px`

---

## Phase 3 — TypeScript for UI (Weeks 6–9)

**Goal:** Catch errors before runtime and type DOM interactions precisely.

### Module 3.1 — Type System Fundamentals
- Primitives: `string`, `number`, `boolean`, `null`, `undefined`, `symbol`
- `union` (`|`) types, `intersection` (`&`) types, literal types
- `type` aliases vs `interface` — when to use which
- Generics: writing reusable typed utilities (`Array<T>`, `Promise<T>`)
- `unknown` vs `any` — why `unknown` is the safe default for external data
- Utility types: `Partial<T>`, `Required<T>`, `Pick<T>`, `Omit<T>`, `Record<K,V>`

### Module 3.2 — Narrowing & Type Guards
- `typeof`, `instanceof`, `in` narrowing
- Discriminated unions for state machines (e.g., `loading | success | error`)
- Custom type guard functions (`is` predicates)
- Exhaustiveness checking with `never`

### Module 3.3 — Typing the DOM
- `HTMLElement` vs `HTMLInputElement` vs `HTMLButtonElement` — the hierarchy
- Casting `querySelector` results: `as HTMLInputElement` vs non-null assertion `!`
- Typed event handlers: `MouseEvent`, `KeyboardEvent`, `InputEvent`, `FocusEvent`
- `EventTarget` vs `Element` vs `HTMLElement` — when each is the right type
- Null-safe DOM access patterns; `strictNullChecks`, optional chaining (`?.`), `??`
- `e.target` vs `e.currentTarget` — event handler type mismatch

### Module 3.4 — Async Patterns
- `Promise<T>` typing, `async`/`await` with proper return types
- Typed `fetch` wrappers with validation (Zod or manual)
- Error handling: typed `catch` blocks, `instanceof Error` narrowing, custom error classes
- Race conditions: `AbortController`, cancellation patterns
- Concurrent patterns: `Promise.all`, `Promise.allSettled`, `Promise.race`

### Module 3.5 — TypeScript Toolchain for Debugging
- `tsconfig.json`: `strict`, `noUncheckedIndexedAccess`, `exactOptionalPropertyTypes`, `lib: ["DOM"]`
- Source maps: mapping compiled JS errors back to TS source lines
- Reading compiler errors: decoding complex type mismatch messages
- `ts-node` and `tsx` for running TS directly in debugging sessions
- VS Code: hover types, go-to-definition, "Find all references" for refactor safety
- Build tooling: Vite/Webpack, ESLint, Prettier, HMR

### Debug Skills Unlocked
- `property does not exist on type` → use type guards or correct assertion
- `Cannot read property of null` → trace DOM query timing (onload vs dynamic injection)
- Stale closures, missing cleanup in event listeners (memory leaks)
- "Why is my data undefined at runtime?" → type as `unknown` and narrow
- "How do I trace a runtime error back to TypeScript?" → source maps

### Labs
1. Fetch user data and display it — intentionally cause null reference → fix with optional chaining
2. Add a click handler with wrong event type → let TS catch it, then correct
3. Break a UI by mutating DOM after render → trace using source maps + breakpoints
4. Debug memory leak from unremoved event listeners

---

## Phase 4 — Integrated UI Debugging (Weeks 10–12)

**Goal:** Diagnose any UI bug systematically using a repeatable method across all layers.

### The Systematic Debug Process
1. Reproduce reliably — find the minimum steps
2. Observe — read the error message fully before acting
3. Hypothesize — form one specific theory
4. Isolate — strip the problem to the smallest failing case
5. Test — prove or disprove the hypothesis
6. Fix — one change at a time
7. Verify — confirm fix doesn't break anything else

### Module 4.1 — Rendering Bugs
- Paint vs layout vs composite — which CSS properties trigger which
- Reflow triggers: reading `offsetWidth`, `scrollTop` in a loop (layout thrashing)
- `z-index` conflicts: auditing stacking contexts across a page
- `overflow: hidden` hiding elements you didn't intend
- `transform` creating a new stacking context — breaking `position: fixed` children
- Invisible elements: `display: none` vs `visibility: hidden` vs `opacity: 0` vs `height: 0`
- FOUC (Flash of Unstyled Content): CSS load order, critical CSS, JS-blocking

### Module 4.2 — Network & Data Bugs
- Network panel: filtering by type (XHR, Fetch, WS), timing breakdown
- Reading request/response headers — Content-Type mismatches
- CORS errors: what the browser blocks and why, how to read the error message
- Response parsing bugs: JSON with unexpected `null` fields, date strings
- Waterfall analysis: identifying blocking requests

### Module 4.3 — JavaScript/TypeScript Runtime Debugging
- Setting breakpoints: line, conditional, logpoint, DOM mutation, XHR/fetch
- Watching expressions and inspecting scope variables
- Call stack reading — tracing how you got to the error
- Event listener audit: `getEventListeners()` in console, listener panel
- Memory leak detection: heap snapshots, detached DOM nodes
- `console` discipline: `console.table`, `console.group`, `console.time`

### Module 4.4 — Performance Debugging
- Performance panel: recording, reading the flame chart
- Core Web Vitals: LCP (Largest Contentful Paint), CLS (Layout Shift), INP (Interaction)
- Long task detection — what blocks the main thread
- `requestAnimationFrame` vs `setTimeout` for visual updates
- Expensive repaints: `box-shadow`, `filter`, `will-change` misuse

### Module 4.5 — Cross-Browser & Device Debugging
- CSS feature queries: `@supports` for progressive enhancement
- Common cross-browser bugs: `gap` in older Safari flex, `subgrid` support matrix
- Vendor prefix traps — checking `caniuse.com` systematically
- Mobile viewport bugs: `100vh` on iOS, keyboard pushing layout
- Safe area insets for notched devices
- Remote debugging: Chrome DevTools on Android, Safari Web Inspector on iOS

### Module 4.6 — Accessibility Debugging
- Screen readers, contrast ratios, keyboard navigation
- `axe` DevTools and WAVE for automated a11y auditing
- Accessibility regression tracing

### Debug Skills Unlocked

| Symptom | Debug Technique |
|---|---|
| Element missing | Check `display: none`, `visibility: hidden`, `opacity: 0`, or parent overflow |
| Unexpected scroll | `overflow` on body, fixed elements, `100vw` includes scrollbar |
| `TS2531: Object is possibly null` | Add null check or use `?.` and `??` |
| `element.click is not a function` | Wrong type assertion — log `typeof element` |
| Event fires multiple times | Detach listeners (`getEventListeners(node)` in DevTools) |
| Page jumps on load | CLS in Performance panel |
| Button click does nothing | Breakpoint + event listener audit |
| Page is slow | Flame chart + long task detection |

### Labs
- **Debugging Bootcamp:** 10 intentionally broken UIs (overflow, specificity, async DOM, z-index, CLS, a11y, network fallback, TS type erosion)
- Write reproducible bug reports with steps, environment, and fixes
- Run & interpret Lighthouse + axe audits; prioritize fixes
- Remote debug on mobile via USB/Wi-Fi

---

## Phase 5 — Production-Grade Mastery & Capstone (Weeks 13–16)

**Goal:** Debug without full source access; handle edge cases at scale across browsers and devices.

### Module 5.1 — Error Monitoring & Observability
- Source map upload to Sentry/LogRocket so production errors map to TS lines
- Error grouping and noise filtering — separating real bugs from browser extensions
- Session replay — watching the exact user journey that caused a crash
- Custom error boundaries and structured error reporting
- Setting up `window.onerror` and `unhandledrejection` handlers

### Module 5.2 — Advanced Debugging Techniques
- Bisect methodology: binary search through commits to find regression origin (`git bisect`)
- Minimal reproducible example (MRE): isolating bugs to CodePen/StackBlitz
- Overrides panel: replacing a production JS/CSS file with a local edited version
- Post-mortem writing: documenting root cause, timeline, and prevention
- Framework-agnostic debugging mindset (applies to React/Vue/Angular)

### Module 5.3 — Capstone: Broken UI Dashboard
Build (or receive) a deliberately broken production UI with:
- Mixed HTML nesting / accessibility failures
- CSS specificity wars (`!important` conflicts, stacking context mess)
- TypeScript null reference (querySelector called too early)
- Async race condition / event listener memory leak
- Responsive failure (mobile layout broken on Safari)
- A layout that breaks only on mobile Safari
- A form that submits but never reaches the server
- A page with a 3-second interaction delay (no obvious cause)
- A component that re-renders infinitely

**Deliverable (Debug Report):**
1. Each bug's reproduction steps
2. Root cause (e.g., "collapsing margin due to adjacent `<p>`")
3. Fix & prevention (e.g., "use `display: flow-root`")
4. Commit diff

### Assessment Criteria
- ✅ Timed debugging challenge: fix 5 complex UI bugs in under 90 minutes
- ✅ Lighthouse score ≥ 90 across Performance, Accessibility, Best Practices
- ✅ axe core compliance: 0 critical violations
- ✅ Code review: type safety, CSS architecture, semantic markup
- ✅ Debug playbook submission: personal troubleshooting guide

---

## Debugging Mental Models (Integrated Throughout)

| Concept | Application |
|---|---|
| **Divide & Conquer** | Comment out CSS/TS in halves to isolate breaking code |
| **Render Pipeline Awareness** | DOM → CSSOM → Render Tree → Layout → Paint → Composite |
| **Specificity Calculator** | DevTools → Styles → computed specificity view |
| **Type-Narrowing First** | Fix TS errors before runtime; `strict: true` prevents 60%+ UI bugs |
| **Reproducible Minimal Case** | Strip to bare HTML/CSS/TS; rebuild until bug reappears |
| **Browser as Source of Truth** | DevTools > IDE; always verify computed values, not authored code |

---

## Tools & Resources Reference

| Tool / Resource | Purpose |
|---|---|
| Chrome + Firefox + Safari DevTools | Primary debugging environment (all three required) |
| VS Code + TypeScript plugin | Compiler feedback, source navigation |
| ESLint, Stylelint, Prettier | Code quality and linting |
| axe DevTools, WAVE, Lighthouse | Accessibility and performance audits |
| Sentry / LogRocket | Production error monitoring |
| `caniuse.com` | Browser compatibility research |
| `git bisect` | Regression hunting |
| CodePen / StackBlitz | Minimal repro environment |
| MDN Web Docs | Authoritative HTML/CSS/JS reference |
| TypeScript Handbook | TS reference |
| WebPageTest | Performance profiling |

---

## What "Debug Any UI Issue" Means at the End

After completing this curriculum, given any UI bug you will be able to answer:

- Is this an **HTML structure** problem (wrong DOM, missing element, broken semantics)?
- Is this a **CSS cascade** problem (wrong rule winning, stacking context, box model)?
- Is this a **TypeScript/JS logic** problem (wrong type, null reference, async race)?
- Is this a **network/data** problem (bad response, CORS, parsing error)?
- Is this a **performance** problem (reflow, long task, layout thrashing)?
- Is this a **browser/device** problem (feature support, viewport quirk)?

Each answer maps directly to a tool and a method taught in the curriculum.

> **Remaining frontier:** Framework-specific state/render cycles (React, Vue, Angular) require their own DevTools extensions, but the methodology learned here transfers directly.
