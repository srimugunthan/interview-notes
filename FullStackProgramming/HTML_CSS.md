Here's a focused curriculum for **"Essential HTML & CSS for the Experienced Developer"** — designed specifically to close the gap between "I can build backends/ML systems" and "I can read, debug, and fix vibe-coded UI confidently."

---

## Curriculum Design Philosophy

The assumption is you already understand programming deeply. So this skips "what is a tag" and instead focuses on **mental models** that explain *why* layout breaks, *what the browser is actually computing*, and *how to use DevTools as your primary debugging instrument* rather than trial-and-error CSS changes.

---

## Module 1: The Browser's Rendering Pipeline (Foundation)

**Goal:** Build a correct mental model of how the browser turns HTML+CSS into pixels. Everything else follows from this.

- How the browser constructs the DOM and CSSOM
- The render tree: which elements generate boxes, which don't
- Layout → Paint → Composite — where each type of visual bug originates
- **Why this matters for vibe-code debugging:** most mysterious layout failures are caused by incorrect mental models of this pipeline

**Key exercise:** Open any vibe-coded page in DevTools, go to the Rendering tab, enable "Paint flashing" and "Layout Shift Regions" — interpret what you see.

---

## Module 2: The Box Model — The One Thing Everyone Gets Wrong

**Goal:** Understand what `width`, `padding`, `border`, and `margin` actually mean, and why `box-sizing: border-box` is almost always what you want.

- Content box vs border box (the `box-sizing` mental model)
- Margin collapsing: the single biggest source of "why is there extra space here?" bugs
- `display: block` vs `inline` vs `inline-block` — how each participates in layout
- When an element's size comes from its content vs its container

**Key exercise:** Take a vibe-coded component with mysterious spacing. Disable all CSS except `box-sizing` rules and re-add rules one at a time. Locate the exact rule creating the unexpected spacing.

---

## Module 3: Normal Flow and When It Breaks

**Goal:** Understand the default layout behavior so you know precisely what `position`, `float`, or `flex` is *overriding*.

- Block formatting context (BFC): what creates one, why it matters
- How `position: relative/absolute/fixed/sticky` interacts with normal flow
- The `containing block` concept — why `position: absolute` goes to a seemingly wrong ancestor
- `z-index` stacking contexts: why `z-index: 9999` sometimes does nothing

**Key exercise:** Given a vibe-coded modal or dropdown that appears in the wrong position, trace the containing block chain in DevTools to identify why.

---

## Module 4: Flexbox — The Layout Workhorse

**Goal:** Develop a complete, intuitive model of Flexbox so you can read and fix any flex-based layout without guessing.

- The two axes (main axis / cross axis) and how `flex-direction` controls everything
- `flex-grow`, `flex-shrink`, `flex-basis` — the actual algorithm, not just "flex: 1"
- `align-items` vs `align-content` vs `justify-content` — the distinction that trips everyone up
- Flex and overflow: why content overflows even with `overflow: hidden`

**Key exercise:** Take a vibe-coded nav bar or card row that breaks on resize. Identify which flex property is causing the overflow and fix it.

---

## Module 5: CSS Grid — Structural Layout

**Goal:** Read and write CSS Grid for page-level layout; understand when Grid > Flexbox.

- `grid-template-columns/rows`, `fr` unit, `minmax()`, `repeat(auto-fill, ...)`
- Explicit vs implicit grid — why extra items appear in unexpected places
- Grid areas and template-based layout
- Alignment in Grid: the same `justify/align` properties but now in two dimensions

**Key exercise:** Audit a vibe-coded dashboard layout. Identify which elements are using Grid vs Flex. Find where implicit grid is creating unexpected rows.

---

## Module 6: The Cascade, Specificity, and Inheritance

**Goal:** Be able to predict exactly which CSS rule wins, and trace any unexpected style in DevTools.

- The cascade algorithm: origin → specificity → order
- Specificity calculation (0-0-0 to 1-0-0 notation)
- `!important` — why it usually signals a design problem, not a solution
- Inheritance: which properties inherit by default, which don't, and the `inherit/initial/unset` keywords
- CSS custom properties (variables): how they cascade differently from regular properties, and why they're scope-aware

**Key exercise:** In DevTools Styles panel, find a declaration shown as crossed out. Explain exactly why it was overridden.

---

## Module 7: Responsive Design and the Viewport

**Goal:** Understand how the browser maps CSS pixels to physical pixels, and how media queries and fluid units work.

- The viewport meta tag and why it matters for mobile
- CSS pixels vs physical pixels vs device pixel ratio
- `rem` vs `em` vs `%` vs `vw/vh` — when to use each
- Media queries: `min-width` vs `max-width` strategy (mobile-first vs desktop-first)
- `clamp()`, `min()`, `max()` for fluid typography and spacing

**Key exercise:** Resize a vibe-coded page from 320px to 1440px. Document every breakpoint where layout breaks. Identify whether the fix belongs in the media query, flex/grid config, or the typography scale.

---

## Module 8: DevTools as Your Primary Instrument

**Goal:** Be fluent enough in DevTools to diagnose any HTML/CSS problem systematically.

- Elements panel: inspecting and live-editing the DOM + computed styles
- The **Computed** tab: the single source of truth for what the browser actually applied
- **Layout** tab: visualizing flex/grid containers, identifying sizing issues
- Performance tab: identifying layout thrash (forced reflows in JS)
- **Overrides and Snippets**: making persistent edits to any page for testing

**Key exercise:** Debug a broken vibe-coded component using only DevTools (no source file edits). Document the root cause and the minimal CSS change needed.

---

## Module 9: Typography, Color, and Visual Rhythm

**Goal:** Develop enough visual vocabulary to recognize when something "looks off" and know what property to adjust.

- `font-family` stack and how fallback fonts change layout (especially width)
- `line-height`, `letter-spacing`, `text-transform` — the trio that controls text feel
- Color systems: `rgb`, `hsl`, `oklch` — why `hsl` is more maintainable for theming
- CSS custom properties for design tokens — connecting to how Tailwind/shadcn generates variables
- Visual rhythm: spacing scales (4px / 8px base systems), how vibe-coders generate inconsistent spacing

---

## Module 10: Reading and Auditing Vibe-Coded UI

**Goal:** Apply everything above to real vibe-coded output efficiently.

- Common patterns vibe-coders generate: Tailwind utility soup, inline styles, nested flex/grid
- How to identify layout bugs by category: overflow, stacking, alignment, spacing
- A systematic audit checklist: box model → flow → flex/grid → specificity → responsiveness
- Translating "it looks wrong" into a precise CSS diagnosis
- When to refactor vs when to patch

**Key exercise (capstone):** Take a full vibe-coded page (or component library). Write a one-page audit: layout strategy used, 3 bugs found with root cause, and recommended fixes.

---

## Suggested Study Approach

Given your engineering background, you'll move fastest by **reading selectively and debugging actively**. The ratio should be roughly 30% reading, 70% DevTools-based experimentation. The MDN documentation for Flexbox, Grid, and the cascade are the gold standard references — skip tutorial sites that abstract away the underlying model.

For reference material, the MDN guides on [Flexbox](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Flexbox), [Grid](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Grids), and [The Cascade](https://developer.mozilla.org/en-US/docs/Web/CSS/Cascade) are precise and complete. Josh Comeau's CSS courses are also notably ML-engineer-friendly — they explain the underlying model rather than just the syntax.
