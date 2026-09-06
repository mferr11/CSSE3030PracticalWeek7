# CSSE3030 Week 7 Practical — Test Oracles

Property-based testing, metamorphic testing, and Daikon invariant discovery,
using `BoundedStack<T>` and `ProductSearch` as the subjects.

Both classes contain a deliberate bug. The point of the exercise is to write 
properties that catch it yourself.

## Requirements

- JDK 17 (Gradle's toolchain will fetch one automatically if needed)
- Internet on your first `daikonDetect` run (one-off ~100MB download)

## Running

| Command | Runs |
|---|---|
| `./gradlew testQ2` | Part 2 — `BoundedStack` properties |
| `./gradlew testQ4Warmup` | Part 4 warm-up — permutation/sum property |
| `./gradlew testQ4` | Part 4 — `ProductSearch` metamorphic properties |
| `./gradlew daikonDetect` | Part 3 — infers invariants into `build/daikon/invariants.txt` |
| `./gradlew test` | everything |


```bash
git clone https://github.com/mferr11/CSSE3030PracticalWeek7.git
```
