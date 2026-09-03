# CSSE3030 Week 7 Practical — Test Oracles

Starter repository for the Week 7 practical on test oracles: property-based
testing (jqwik), metamorphic testing (jqwik), and invariant discovery
(Daikon). There are two independent exercises:

- **Part 2 — Property-based testing**: `BoundedStack<T>`, a fixed-capacity
  stack with two deliberate bugs that hand-picked example tests would likely
  miss, but that checkable properties over *any* input catch reliably.
- **Part 4 — Metamorphic testing**: `ProductSearch`, where narrowing a search
  filter should only ever shrink the result set — a relation you can check
  without ever computing an expected result by hand.

(Part 3, in between, uses Daikon to dynamically *infer* invariants of
`BoundedStack` from example runs — see below.)

Both `BoundedStack` and `ProductSearch` contain a deliberate, intentional
bug. **Do not "fix" the bug on the `week7` branch** — that's the point of
the exercise. Fixes only live on the `solution` branch (tutors' reference
copy).

---

## 1. Requirements

- **JDK 17** (any distribution — Temurin/Adoptium is a good default). The
  build uses a Gradle Java toolchain pinned to Java 17, so as long as *some*
  JDK is available to run Gradle itself, Gradle will automatically locate or
  download a JDK 17 to compile and run the code with — you do **not** need
  JDK 17 to be your system default.
- Git.
- An internet connection the first time you run `daikonTrace` or
  `daikonDetect` (Part 3) — see Section 4. No Docker is required for this
  practical.

You do **not** need Gradle installed — always use the wrapper (`./gradlew` /
`gradlew.bat`) included in this repo, so everyone uses the same Gradle
version.

### Checking / setting `JAVA_HOME`

**Windows (PowerShell):**
```powershell
echo $env:JAVA_HOME
java -version
```
If `JAVA_HOME` is empty or wrong, set it permanently via *Settings → System →
About → Advanced system settings → Environment Variables*, or from an
elevated PowerShell prompt:
```powershell
setx JAVA_HOME "C:\Path\To\Your\jdk-17"
```
(Open a **new** terminal afterwards — `setx` doesn't affect the current
session.)

**macOS:**
```bash
echo $JAVA_HOME
java -version
/usr/libexec/java_home -V   # lists all installed JDKs
```
If needed, add to `~/.zshrc` (or `~/.bash_profile`):
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

**Linux:**
```bash
echo $JAVA_HOME
java -version
readlink -f "$(which java)"   # helps locate your JDK install path
```
Add to `~/.bashrc` (or your shell's rc file):
```bash
export JAVA_HOME=/path/to/your/jdk-17
export PATH="$JAVA_HOME/bin:$PATH"
```

---

## 2. Running the tests

All commands below are run from the repository root.

| Command | What it runs |
|---|---|
| `./gradlew testQ2` | **Part 2 only** — `BoundedStack` property tests (jqwik) |
| `./gradlew testQ4` | **Part 4 only** — `ProductSearch` metamorphic test (jqwik) |
| `./gradlew daikonTrace` | **Part 3, step 1** — traces `BoundedStackTraceTest` with Daikon's Chicory front end |
| `./gradlew daikonDetect` | **Part 3, step 2** — runs Daikon on that trace and writes inferred invariants |
| `./gradlew test` | Everything (Parts 2 and 4) |
| `./gradlew build` | Full build: compiles, runs all tests, assembles |

On Windows use `gradlew.bat` instead of `./gradlew` (e.g. `gradlew.bat testQ2`).

This mirrors the per-question task pattern from previous weeks: one Gradle
`Test` task per practical question, filtered to that question's test
package, so you can run each part in isolation.

### What to expect when you first run these on the starter (`week7`) branch

- `./gradlew testQ2` — **fails**. `BoundedStackPropertyTest` has three
  `@Property` methods; all three fail, each pointing at one of the two
  seeded bugs in `BoundedStack`. Read the shrunk counterexample jqwik prints
  for each — it will be a tiny, minimal case (e.g. `capacity = 1`).
- `./gradlew testQ4` — **fails**. `ProductSearchMetamorphicTest` checks that
  narrowing a search filter can only ever shrink the result set; it fails
  because of a bug in `ProductSearch.and(...)`.

---

## 3. Where the bugs are (`week7` branch)

### Part 2 — `BoundedStack<T>`

- [`BoundedStack.java`](src/main/java/csse3030/week7/BoundedStack.java) —
  two bugs:
  1. `push()` checks `items.size() == capacity + 1` instead of
     `items.size() == capacity`, so the stack silently accepts **one extra
     element** beyond its stated capacity, breaking the invariant
     `size() <= capacity()`.
  2. `pop()` on an empty stack returns `null` instead of throwing
     `NoSuchElementException` as documented.
- [`BoundedStackPropertyTest.java`](src/test/java/csse3030/week7/BoundedStackPropertyTest.java) —
  jqwik properties that catch both bugs without a single hand-picked example.

### Part 4 — `ProductSearch`

- [`ProductSearch.java`](src/main/java/csse3030/week7/ProductSearch.java) —
  `and(a, b)` is implemented with logical **OR**
  (`a.test(p) || b.test(p)`), so "narrowing" a filter can *expand* the
  result set.
- [`ProductSearchMetamorphicTest.java`](src/test/java/csse3030/week7/ProductSearchMetamorphicTest.java) —
  checks the subset relation between a source query and a narrower
  follow-up query, and fails because of the bug above.

The fixes for both bugs are **only** on the `solution` branch.

---

## 4. Part 3 — Discovering invariants with Daikon

Daikon isn't distributed via Maven Central, so it's wired up with two custom
Gradle tasks that drive it directly:

1. **`./gradlew daikonTrace`** — runs
   [`BoundedStackTraceTest`](src/test/java/csse3030/week7/BoundedStackTraceTest.java)
   (a plain, deterministic test — *not* jqwik, since Daikon needs a fixed,
   repeatable set of calls to trace) under Daikon's **Chicory** front end,
   which instruments `BoundedStack` and records every call in
   `build/daikon/bounded-stack.dtrace.gz`.
2. **`./gradlew daikonDetect`** — runs `daikon.Daikon` over that trace file
   and writes the inferred invariants to `build/daikon/invariants.txt`.

The first time you run either task, Gradle automatically downloads the
official `daikon.jar` distribution (MIT licensed, ~100MB, one-off) into
`lib/daikon.jar` — it is **not** committed to this repository (see
`.gitignore`) to keep clones small. No Docker is needed: Chicory and
`daikon.Daikon` are pure-Java tools, so the JDK you already have is enough.

### The exercise

Run `./gradlew daikonDetect` and open `build/daikon/invariants.txt`. For the
`push`/`pop`/`peek` program points you should see Daikon report invariants
resembling:
```
this.size >= 0
this.size <= this.capacity
this.capacity >= 1
```
from ordinary, in-bounds usage — before you've read a single line of
`BoundedStack.java`. Compare these against the invariants you'd write down
yourself from the documented contract.

Then: add a call sequence to `BoundedStackTraceTest` that pushes **one more
item than the stack's capacity**, re-run `daikonTrace` and `daikonDetect`,
and diff `invariants.txt` against your first run. On the `week7` branch, the
`this.size <= this.capacity` invariant you saw before should disappear —
your "trusted" invariant, broken, because of the same bug Part 2's property
test caught. On the `solution` branch it survives, because `push()` rejects
the extra element instead of accepting it.

---

## 5. Branches / getting the code

- **`week7`** — starter code (this branch). Both bugs are live. This is
  what you work from during the practical.
- **`solution`** — tutor reference copy, with both bugs fixed and all tests
  green. Not for students to work from directly.

Clone the starter branch:
```bash
git clone -b week7 https://github.com/mferr11/CSSE3030PracticalWeek7.git
cd CSSE3030PracticalWeek7
```

Tutors, to check out the reference solution:
```bash
git clone -b solution https://github.com/mferr11/CSSE3030PracticalWeek7.git csse3030-week7-solution
```
or, if you already have the starter cloned:
```bash
git fetch origin
git checkout solution
```
