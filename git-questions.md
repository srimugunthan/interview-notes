what is the difference between merge rebase and fast forward


<img width="438" height="139" alt="image" src="https://github.com/user-attachments/assets/4485a138-d3e7-4036-a141-38b3817cb8c0" />


<img width="502" height="206" alt="image" src="https://github.com/user-attachments/assets/d2d7e8f2-ea60-4fcb-a18c-ffa579322a8b" />



Merge (pull.rebase false) — the default. Git creates a new "merge commit" M that has two parents: the tip of main (C2) and the tip of feature (C4). History is preserved exactly as it happened, but you get an extra commit every time you pull. Good for shared branches where you want auditability.
Rebase (pull.rebase true) — git takes your local commits (C3, C4) and replays them one by one on top of the latest remote commits, producing fresh copies (C3', C4') with new SHAs. The branch history looks linear as if you had started from the latest point. The trade-off: rewrites history, so avoid it on commits already pushed and shared with others.
Fast-forward only (pull.ff only) — git simply slides the main pointer forward to the tip of feature. No new commit is created at all. This only works when main has no commits that feature doesn't already include (i.e., no true divergence). If there is divergence, git refuses and errors out. This is the strictest mode — it keeps history perfectly linear but requires you to rebase first if you're diverged.

12:06Claude responded: | Strategy | Merge commit?StrategyMerge commit?Rewrites history?Works when diverged?Merge✓ Yes✗ No✓ YesRebase✗ No✓ Yes (new SHAs)✓ YesFast-forward only✗ No✗ No✗ No — will fail
