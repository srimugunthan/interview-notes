# Chapter 4: Tooling Literacy

Days, not weeks, per the syllabus. The goal isn't to become a build-tools expert — it's to stop being confused by the files a project starts with, and to know where to look when something breaks and AI can't see your screen.

## 4.1 npm and package.json

`package.json` is the manifest for a JavaScript project — its name, scripts, and dependencies.

```json
{
  "name": "my-app",
  "version": "0.0.1",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "react": "^18.3.1",
    "react-dom": "^18.3.1"
  },
  "devDependencies": {
    "vite": "^5.4.0"
  }
}
```

Core commands:

```bash
npm install          # reads package.json, downloads everything into node_modules/
npm install axios    # adds a new package, saves it to "dependencies"
npm install -D vitest # -D = save to "devDependencies" (only needed during development/build, not in production)
npm run dev           # runs the "dev" script defined above
npm uninstall axios
```

What to actually understand:
- `dependencies` ship in your production app (React itself). `devDependencies` are tools used only while building/testing (bundlers, test runners, linters).
- `node_modules/` is regenerated from `package.json` — never edit it by hand, never commit it to git.
- `package-lock.json` pins the *exact* versions installed (including sub-dependencies) so everyone on a team gets identical installs. Commit this file; don't hand-edit it.
- The `^` in `"^18.3.1"` means "any compatible version ≥18.3.1, staying under 19.0.0" (semantic versioning: major.minor.patch — a major bump signals breaking changes).

## 4.2 What a bundler does (conceptually)

Browsers don't natively understand "import this component from that file" the way your source code is organized, and they load JS/CSS from potentially dozens of separate files inefficiently. A bundler:

1. Starts at an entry file (`main.jsx`) and follows every `import` statement to build a dependency graph.
2. Combines everything into a small number of optimized output files (bundles).
3. Transforms syntax your browser might not support yet — JSX, newer JS features, TypeScript — into plain JS via a transpiler (Babel, esbuild, SWC).
4. In development, serves your app with **hot module replacement** — edit a file, see the change in the browser instantly, without a full page reload or losing component state.

Vite (the current standard, and what the syllabus points to) does this by serving your source files directly over native browser ES modules during development — which is why `npm run dev` feels instant compared to older bundlers like Webpack that had to build everything upfront.

```bash
npm run dev     # starts a local dev server, usually http://localhost:5173
npm run build   # produces an optimized dist/ folder ready to deploy
npm run preview # serves that dist/ folder locally, so you can sanity-check the production build
```

You don't need to hand-configure a bundler from scratch (`vite.config.js` usually needs zero changes for a standard React app) — but recognizing "this project uses Vite" vs. "this project uses Create React App / Webpack" from the config files present tells you where to look when a build breaks.

## 4.3 Browser DevTools — where AI can't see for you

This is the one piece of the stack that's fundamentally *yours*. An AI assistant can read your code, but it cannot see your running page, your network requests, or your console errors unless you paste them in. Open DevTools with `F12` or `Cmd+Option+I` (Mac) / `Ctrl+Shift+I` (Windows/Linux).

**Console tab**
- Shows `console.log()` output and runtime errors with stack traces.
- Errors here are your first stop when something's broken — read the actual error message and the file/line it points to before guessing.

**Network tab**
- Every HTTP request your page makes, in order, with status code, timing, and payload.
- Click any request to see request headers, response headers, and the raw response body — this is how you check "is my API actually returning what I think it's returning" independent of what your JS code does with it afterward.
- A red/failed request here, or a 404/500 status, tells you the problem is server-side or network-side, not in your rendering code — saves you from debugging the wrong layer.

**Elements tab**
- The live, current DOM — not your source HTML, but what the browser actually rendered right now, including anything JS added/changed after load.
- Click the inspector icon (top-left of the panel) and click any element on the page to jump straight to its node here — invaluable for "why does this button look wrong," since you can see the exact computed CSS rules applied (and which ones got overridden) in the Styles pane on the right.
- You can edit HTML/CSS live here to experiment — changes don't persist on reload, so it's a safe scratchpad.

**Sources tab** (worth knowing exists)
- Set breakpoints in your actual JS and step through execution line by line, inspecting variable values as you go — more powerful than `console.log`-driven debugging once a bug is subtle enough to need it.

## Try it

1. Open any React app from Chapter 3 in the browser with DevTools open. In the Console, intentionally cause an error (e.g., call `.map()` on `undefined`) and read the stack trace — find the exact line in your source it points to.
2. Open the Network tab, reload `PostList` from Chapter 3, and find the actual request to `jsonplaceholder.typicode.com`. Check its response body matches what you expected, and note the response time.
3. Use the Elements tab's inspector to click on a rendered `<li>` from your post list, and find the Styles pane. Change a CSS property live (e.g., `color`) and watch it update on the page — then reload and confirm it reverted, proving you only touched the live DOM, not your source files.
4. Run `npm run build` on a Vite project and look inside the generated `dist/` folder — compare its file sizes and structure to your `src/` folder to see what bundling actually produced.
