# How To Add Stuff To The Cache

The goal of this repo is simple:

> If you found something useful for CSE213, put it here so the rest of us don't have to spend 40 minutes searching for it later.

---

## Folder Rules

### Topic doesn't exist?

Create a new folder in the root directory.

Then create a subfolder using your student ID and put your files there.

Example:

```
/Design-Patterns
    /2305164
        code.cpp
        notes.pdf
```

---

### Topic already exists?

Create a subfolder using your student ID inside that topic and put your files there.

Example:

```
/UML
    /2305164
        diagrams.pdf
```

The idea is simple:

> Your stuff goes in your folder.

---

## About Other People's Folders

It is suggested **not to directly modify files inside someone else's folder**.

Instead:

### Option 1: Copy it to your own folder

If you want to improve, modify, or experiment with someone's code, copy it into your own ID folder and work there.

```
/Requirements-Engineering
    /2205001
        solution.cpp

    /2305164
        solution-improved.cpp
```

---

### Option 2: Create a local workspace

You can create another subfolder with any name you want and add it to `.gitignore`.

Example:

```
/UML
    /2305164
    /playground
```

Then add:

```
playground/
```

to `.gitignore`.

Perfect for testing ideas, writing cursed code, debugging for 3 hours, or pretending you'll clean things up later.

---

## Before You Push

Quick checklist:

- Is it useful?
- Is it in the correct topic folder?
- Is it inside your own folder?
- Did you accidentally upload your entire Downloads directory?
- Did you accidentally upload `node_modules`?

If the answer to the last two is "no", you're probably good.

---

## Reminder

This repository exists so that:

- one person finds the resource,
- everyone benefits from it.

Let's keep things organized so Future Us doesn't suffer.

---

## Credits

Maintained by students.

Inspired by deadlines.

Powered by panic.

---

### Motto

"Cache today, cry less tomorrow."
