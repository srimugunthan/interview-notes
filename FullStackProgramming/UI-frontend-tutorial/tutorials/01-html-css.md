# Chapter 1: HTML & CSS Essentials

Goal: enough HTML/CSS to structure a real page correctly and lay it out without fighting the browser. You will not memorize every CSS property — AI tools are good at generating that. What they're less reliable at is *structure that means something* (semantics, accessibility) and *layout systems you can reason about* (Flexbox, Grid). That's the part worth internalizing.

## 1.1 Semantic HTML

Semantic tags describe what content *is*, not just how it looks. Screen readers, search engines, and browsers' built-in behavior (like keyboard navigation) all rely on this.

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Semantic Page Example</title>
</head>
<body>
  <header>
    <h1>My Site</h1>
    <nav>
      <ul>
        <li><a href="#about">About</a></li>
        <li><a href="#contact">Contact</a></li>
      </ul>
    </nav>
  </header>

  <main>
    <article>
      <h2 id="about">About</h2>
      <p>This is the main content of the page.</p>
    </article>

    <aside>
      <p>Related links or side content go here.</p>
    </aside>
  </main>

  <footer>
    <p>&copy; 2026 My Site</p>
  </footer>
</body>
</html>
```

Why it matters: `<div>` and `<span>` carry no meaning — they're layout-only. `<header>`, `<nav>`, `<main>`, `<article>`, `<aside>`, `<footer>` tell the browser and assistive tech what role each region plays. Prefer the semantic tag whenever one fits; fall back to `<div>`/`<span>` only for pure styling hooks.

## 1.2 Forms and accessibility basics

Every form input needs a **label** that's programmatically tied to it — not just text sitting nearby.

```html
<form>
  <div>
    <label for="email">Email</label>
    <input type="email" id="email" name="email" required />
  </div>

  <div>
    <label for="message">Message</label>
    <textarea id="message" name="message" rows="4"></textarea>
  </div>

  <button type="submit">Send</button>
</form>
```

- `for="email"` on the `<label>` must match `id="email"` on the `<input>`. Clicking the label then focuses the input, and screen readers announce the label when the input gets focus.
- Use the right `type` (`email`, `number`, `tel`, `date`...) — you get free validation and better mobile keyboards.
- `required`, `min`, `max`, `pattern` give you basic validation without JavaScript.

Images need `alt` text describing their content or purpose (empty `alt=""` if the image is purely decorative — that tells screen readers to skip it):

```html
<img src="chart.png" alt="Bar chart showing revenue growth from 2023 to 2026" />
<img src="decorative-swirl.png" alt="" />
```

## 1.3 Flexbox — one-dimensional layout

Flexbox lays children out in a single row or column and handles spacing/alignment for you.

```html
<div class="navbar">
  <div class="logo">Brand</div>
  <nav class="links">
    <a href="#">Home</a>
    <a href="#">Docs</a>
    <a href="#">Pricing</a>
  </nav>
</div>
```

```css
.navbar {
  display: flex;
  justify-content: space-between; /* push logo left, links right */
  align-items: center;            /* vertically center everything */
  padding: 1rem;
}

.links {
  display: flex;
  gap: 1.5rem; /* spacing between flex children, no margin hacks */
}
```

Core mental model:
- `display: flex` turns on flex layout for the **children** of that element.
- `flex-direction: row` (default) or `column` — which axis items flow along.
- `justify-content` — alignment along the main axis (the direction items flow).
- `align-items` — alignment along the cross axis (perpendicular to flow).
- `gap` — spacing between items, no margin math needed.

## 1.4 CSS Grid — two-dimensional layout

Grid is for laying things out in rows *and* columns at once — page layouts, card grids, dashboards.

```html
<div class="dashboard">
  <header class="header">Header</header>
  <nav class="sidebar">Sidebar</nav>
  <main class="content">Main content</main>
  <footer class="footer">Footer</footer>
</div>
```

```css
.dashboard {
  display: grid;
  grid-template-columns: 200px 1fr; /* sidebar fixed, content flexible */
  grid-template-rows: auto 1fr auto;
  grid-template-areas:
    "header  header"
    "sidebar content"
    "footer  footer";
  min-height: 100vh;
  gap: 1rem;
}

.header  { grid-area: header; }
.sidebar { grid-area: sidebar; }
.content { grid-area: content; }
.footer  { grid-area: footer; }
```

- `fr` is a fraction of remaining space — `1fr` means "take whatever's left."
- Named `grid-template-areas` let you literally draw the layout as ASCII art and assign children to slots — extremely readable compared to counting column numbers.

### Flexbox vs. Grid — the rule of thumb

- Laying out a row of nav links, a toolbar, or centering one thing? **Flexbox.**
- Laying out a whole page, a card grid, or anything with both rows and columns that need to line up? **Grid.**
- They compose: a Grid page layout with a Flexbox navbar inside one grid area (as above) is completely normal.

## Try it

1. Save the semantic HTML page above as `index.html` and open it in a browser.
2. Add a contact form to it using the label/input pattern from 1.2. Tab through the fields with your keyboard only — confirm focus order makes sense and each label highlights its field when clicked.
3. Build a 3-column "card" layout (image, title, description repeated 3 times) two ways: once with `display: flex; flex-wrap: wrap`, once with `display: grid; grid-template-columns: repeat(3, 1fr)`. Resize the browser window narrower and see how each behaves differently — that difference is *why* both systems exist.
