Based on your profile and the interview-focused curriculum above, here's a prioritized reading list — ordered by how much ROI they give you specifically.

---

## PyTorch Books: Prioritized for Your Profile

### Tier 1 — Read These

**1. Deep Learning with PyTorch (2nd Edition)**
Antiga, Stevens, Viehmann, Huang — Manning

The authors include PyTorch core developers, which means the internals coverage is authoritative rather than inferred. This is the only book that explains `torch.Storage`, strides, and the autograd graph construction with the precision you'd need to answer bar-raiser questions. The distributed training sections in the second edition are substantially updated.

Direct curriculum mapping: covers Modules 1 and 2 (memory + autograd) from your interview curriculum almost completely. Read the storage and autograd chapters first, non-linearly.

---

**2. Mastering PyTorch (2nd Edition)**
Ashish Ranjan Jha — Packt

Skips beginner material entirely and goes straight into advanced patterns — custom loss functions, backward hooks, AMP, TorchScript, ONNX export, and profiling. The production and deployment chapters are more practically detailed than anything in the Manning book.

Direct curriculum mapping: covers your Tier 2 interview topics (AMP, compile, deployment) and the hooks-based debugging angle that differentiates Principal-level answers.

---

### Tier 2 — Targeted Reading (Don't Read Cover to Cover)

**3. Machine Learning with PyTorch and Scikit-Learn**
Sebastian Raschka — Packt

Raschka writes with unusual clarity at the algorithmic level. Half the book is Scikit-Learn which you can skip entirely. The PyTorch chapters are worth reading specifically for the `torch.autograd.Function` walkthroughs — he shows the Jacobian-vector product computation more clearly than any other book. Good prep for the "write a custom layer from scratch" coding round.

---

**4. Programming PyTorch for Deep Learning**
Ian Pointer — O'Reilly

Narrower scope than the others but has the best practical coverage of data pipeline engineering — `IterableDataset`, `collate_fn`, `num_workers` tuning. Read only the data loading chapters. Useful if your interview loop includes a take-home or live coding round with a pipeline bottleneck scenario.

---

### Tier 3 — Not Books, But Better Than Books for Specific Topics

These cover things no book keeps current enough to be reliable on:

**Edward Yang's "PyTorch Internals" essay and talks**
Yang is a core PyTorch engineer. His writing on `TensorImpl`, the C++ dispatcher, and ATen is the primary source for understanding what's below the Python API. Directly useful if you get a systems-focused bar-raiser round. Search "ezyang PyTorch internals" — it's freely available.

**PyTorch Dev Podcast (Edward Yang)**
Short episodes, each focused on one architectural component — `torch.compile`, TorchDynamo, AOTAutograd, FSDP internals. More current than any published book by definition. The TorchDynamo and FSDP episodes map directly to your Tier 2 interview prep.

**Official `torch.compile` tutorial + graph break documentation**
For the compilation stack specifically, the official docs are more reliable than any book because this area changed substantially in PyTorch 2.0, 2.1, and 2.2. Books published before 2024 cover an older API.

---

### Reading Strategy Given Your Time Constraints

Don't read linearly. Given the interview curriculum structure:

- **Weeks 1–2:** Manning book chapters on Storage, strides, and autograd graph construction
- **Week 3:** Raschka's `autograd.Function` chapters
- **Week 4:** Jha's AMP and TorchScript chapters + Yang's internals essay
- **Week 5:** PyTorch Dev Podcast episodes on FSDP and `torch.compile` while commuting
- **Week 6:** Official docs for anything that feels stale in the books

One caveat: Packt books in general have uneven editing quality. Both the Jha and Raschka titles are worth it despite that, but don't expect the prose quality of the Manning book.
