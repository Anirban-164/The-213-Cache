# SOLID Principles — Exam Preparation Guide

> Based on your lecture material (Lec 1, Slides 10–31). Covers the exact question formats you mentioned: *which principle does code follow/violate?*, *what changes are needed?*, *what happens if we make a certain change?*

---

## Quick Reference — The Five Principles

| Principle | One Sentence | The "Smell" That Indicates Violation |
|---|---|---|
| **S** — Single Responsibility | A class should have **one reason to change** | A class does formatting AND saving AND emailing |
| **O** — Open/Closed | Open for **extension**, closed for **modification** | Adding a new type requires editing an `if/else` chain |
| **L** — Liskov Substitution | A subtype must **honor its parent's contract** | A subclass throws `UnsupportedOperationException` on an inherited method |
| **I** — Interface Segregation | Don't force clients to depend on **methods they don't use** | A class implements an interface but leaves half the methods as empty stubs |
| **D** — Dependency Inversion | Depend on **abstractions**, not concretions | A high-level class does `new MySQLDatabase()` directly inside its logic |

### How They Interlock (exam loves this!)

- **SRP violation → usually OCP violation too** — a bloated class has no clean seam for extension
- **OCP fix → often creates DIP compliance** — introducing an abstraction satisfies both
- **LSP keeps OCP honest** — extending via subclassing only works if the subclass truly honors the parent's promises
- **ISP is SRP applied to interfaces** — one interface, one role

---

## Category 1: "Which SOLID Principle Does This Code Violate?"

### Q1 — The God Class
```java
public class StudentManager {
    public void addStudent(Student s) { /* save to DB */ }
    public void deleteStudent(int id) { /* delete from DB */ }
    public String generateTranscript(Student s) { /* format PDF */ }
    public void emailTranscript(Student s, String email) { /* send email */ }
    public void printTranscript(Student s) { /* send to printer */ }
}
```

<details>
<summary>Answer</summary>

**SRP violation.** `StudentManager` has at least three reasons to change:
1. Database schema changes (CRUD methods)
2. Transcript formatting changes (PDF layout)
3. Email/printing infrastructure changes (delivery)

**Fix:** Split into `StudentRepository` (DB), `TranscriptFormatter` (PDF), and `TranscriptDelivery` (email/print).
</details>

---

### Q2 — The if/else Shape Problem
```java
public class ShippingCalculator {
    public double calculate(String type, double weight) {
        if ("ground".equals(type))       return weight * 1.5;
        else if ("air".equals(type))     return weight * 3.0;
        else if ("express".equals(type)) return weight * 5.0;
        else throw new IllegalArgumentException("Unknown type");
    }
}
```

<details>
<summary>Answer</summary>

**OCP violation.** Adding a new shipping type (e.g., "drone") requires **editing** the existing, tested method — adding another `else if` branch.

**Fix:** Create a `ShippingStrategy` interface with `double calculate(double weight)`, then one class per type:
```java
interface ShippingStrategy {
    double calculate(double weight);
}
class GroundShipping implements ShippingStrategy {
    public double calculate(double weight) { return weight * 1.5; }
}
// AirShipping, ExpressShipping, DroneShipping — all added WITHOUT editing ShippingCalculator
```
</details>

---

### Q3 — The Square/Rectangle Trap
```java
class Rectangle {
    protected int width, height;
    public void setWidth(int w)  { this.width = w; }
    public void setHeight(int h) { this.height = h; }
    public int getArea() { return width * height; }
}

class Square extends Rectangle {
    @Override
    public void setWidth(int w)  { this.width = w; this.height = w; }
    @Override
    public void setHeight(int h) { this.width = h; this.height = h; }
}
```

A client writes:
```java
void resize(Rectangle r) {
    r.setWidth(5);
    r.setHeight(4);
    assert r.getArea() == 20;  // What happens if r is a Square?
}
```

<details>
<summary>Answer</summary>

**LSP violation.** `Rectangle`'s contract says width and height are **independent**. `Square` silently breaks this — setting width also changes height. Any code written against `Rectangle`'s contract **fails** when given a `Square` (area becomes 16, not 20).

LSP is about **behavioral compatibility**, not real-world taxonomy. Mathematically a square *is* a rectangle, but in OOP the subclass doesn't honor the parent's behavioral promises.
</details>

---

### Q4 — The Fat Interface
```java
interface Worker {
    void work();
    void eat();
    void sleep();
}

class Robot implements Worker {
    public void work() { /* works fine */ }
    public void eat()  { /* ??? robots don't eat */ throw new UnsupportedOperationException(); }
    public void sleep() { /* ??? robots don't sleep */ throw new UnsupportedOperationException(); }
}
```

<details>
<summary>Answer</summary>

**ISP violation.** `Robot` is forced to implement `eat()` and `sleep()` — methods it can never honestly fulfill. The interface is too broad.

**Fix:** Split into `Workable`, `Eatable`, `Sleepable`:
```java
interface Workable { void work(); }
interface Eatable  { void eat(); }
interface Sleepable { void sleep(); }

class Robot implements Workable { /* only work() */ }
class Human implements Workable, Eatable, Sleepable { /* all three */ }
```

> **Bonus:** This also violates **LSP** — `Robot` throws exceptions where the `Worker` interface promised working methods.
</details>

---

### Q5 — Direct Dependency on Concrete Class
```java
class NotificationService {
    private EmailSender sender = new EmailSender();

    public void notify(String message) {
        sender.send(message);
    }
}
```

<details>
<summary>Answer</summary>

**DIP violation.** The high-level module (`NotificationService`) directly depends on a low-level module (`EmailSender`). To switch to SMS or push notifications, you must **edit** `NotificationService`.

**Fix:** Depend on an abstraction:
```java
interface MessageSender { void send(String msg); }

class NotificationService {
    private MessageSender sender;
    public NotificationService(MessageSender sender) { this.sender = sender; }
    public void notify(String msg) { sender.send(msg); }
}
// EmailSender, SmsSender, PushSender all implement MessageSender
```

> This fix also restores **OCP** — new senders can be added without editing `NotificationService`.
</details>

---

## Category 2: "What Changes Are Needed to Follow a Certain Principle?"

### Q6 — Make This OCP-Compliant
```java
class TaxCalculator {
    public double getTax(String country, double amount) {
        if ("US".equals(country))       return amount * 0.07;
        else if ("EU".equals(country))  return amount * 0.20;
        else if ("BD".equals(country))  return amount * 0.15;
        return 0;
    }
}
```

<details>
<summary>Answer</summary>

Extract a `TaxPolicy` interface and one implementation per country:
```java
interface TaxPolicy { double calculate(double amount); }
class USTaxPolicy  implements TaxPolicy { public double calculate(double a) { return a * 0.07; } }
class EUTaxPolicy  implements TaxPolicy { public double calculate(double a) { return a * 0.20; } }
class BDTaxPolicy  implements TaxPolicy { public double calculate(double a) { return a * 0.15; } }
```
Now `TaxCalculator` receives a `TaxPolicy` and calls `policy.calculate(amount)`. Adding Japan's tax means adding `JPTaxPolicy` — **no editing** of existing code.
</details>

---

### Q7 — Make This SRP-Compliant
```java
class UserAccount {
    public boolean validateEmail(String email) { /* regex check */ }
    public String hashPassword(String pw)      { /* bcrypt hash */ }
    public void saveToDatabase()               { /* INSERT INTO users */ }
    public void sendWelcomeEmail()             { /* SMTP send */ }
}
```

<details>
<summary>Answer</summary>

Four reasons to change → four classes:

| Class | Responsibility |
|---|---|
| `EmailValidator` | Validation rules |
| `PasswordHasher` | Hashing algorithm |
| `UserRepository` | Database access |
| `WelcomeMailer` | Email delivery |

`UserAccount` becomes a thin data class, and a `UserRegistrationService` coordinates all four.
</details>

---

### Q8 — Make This DIP-Compliant
```java
class OrderProcessor {
    private MySQLDatabase db = new MySQLDatabase();
    private StripePayment payment = new StripePayment();

    public void process(Order order) {
        payment.charge(order.getTotal());
        db.save(order);
    }
}
```

<details>
<summary>Answer</summary>

Introduce abstractions and inject them:
```java
interface PaymentGateway { void charge(double amount); }
interface OrderRepository { void save(Order order); }

class OrderProcessor {
    private final PaymentGateway payment;
    private final OrderRepository repo;

    public OrderProcessor(PaymentGateway payment, OrderRepository repo) {
        this.payment = payment;
        this.repo = repo;
    }

    public void process(Order order) {
        payment.charge(order.getTotal());
        repo.save(order);
    }
}
```
Now `MySQLDatabase implements OrderRepository` and `StripePayment implements PaymentGateway` — but `OrderProcessor` never mentions them by name.
</details>

---

### Q9 — Fix the ISP Violation
```java
interface MultiFunctionDevice {
    void print(Document d);
    void scan(Document d);
    void fax(Document d);
}

class BasicPrinter implements MultiFunctionDevice {
    public void print(Document d) { /* works */ }
    public void scan(Document d)  { throw new UnsupportedOperationException(); }
    public void fax(Document d)   { throw new UnsupportedOperationException(); }
}
```

<details>
<summary>Answer</summary>

Split the fat interface into role-specific ones:
```java
interface Printer  { void print(Document d); }
interface Scanner  { void scan(Document d); }
interface FaxMachine { void fax(Document d); }

class BasicPrinter implements Printer { /* only print */ }
class AllInOnePrinter implements Printer, Scanner, FaxMachine { /* all three */ }
```
Each class implements only what it honestly supports.
</details>

---

### Q10 — Fix the LSP Violation
```java
class Bird {
    public void fly() { System.out.println("Flying"); }
}

class Ostrich extends Bird {
    @Override
    public void fly() { throw new UnsupportedOperationException("Can't fly"); }
}
```
Client code: `void migrate(Bird b) { b.fly(); }` — crashes for Ostrich.

<details>
<summary>Answer</summary>

Restructure the hierarchy so `fly()` isn't promised by all birds:
```java
class Bird { /* common bird behavior */ }

interface Flyable { void fly(); }

class Sparrow extends Bird implements Flyable {
    public void fly() { System.out.println("Flying"); }
}

class Ostrich extends Bird { /* no fly() — never promises it */ }
```
Now `migrate()` accepts `Flyable`, not `Bird`, so you can't accidentally pass an `Ostrich`.
</details>

---

## Category 3: "If We Make This Change, Which Principle Is Maintained or Violated?"

### Q11 — Adding a New Payment Type
**Original (follows OCP):**
```java
interface PaymentMethod { void pay(double amount); }
class CreditCard implements PaymentMethod { ... }
class PayPal implements PaymentMethod { ... }
```
**Proposed change:** Add `class CryptoPayment implements PaymentMethod { ... }`.

<details>
<summary>Answer</summary>

**OCP is maintained.** New behavior is added by creating a new class, not editing existing ones. `CreditCard` and `PayPal` are untouched.

**DIP is maintained.** Any client code that depends on `PaymentMethod` (the abstraction) works with `CryptoPayment` without changes.
</details>

---

### Q12 — Adding a Field to an Interface
**Original:**
```java
interface CloudStorageProvider {
    void storeFile(String name);
    String getFile(String name);
}
```
**Proposed change:** Add `void createServer(String region)` to this interface.

<details>
<summary>Answer</summary>

**ISP is violated.** Implementations like `Dropbox` (storage-only) are now forced to implement `createServer()` — a method they have no use for. 

**SRP is ALSO violated.** The interface itself now has two distinct responsibilities (file storage and server management), meaning it has two reasons to change. Any class implementing this full interface (like an `AmazonWebServices` class) would also be violating SRP. As the study guide notes, "ISP is SRP applied to interfaces." 

**Fix:** Keep `CloudStorageProvider` narrow. Put `createServer()` in a separate `CloudHostingProvider` interface.
</details>

---

### Q13 — Overriding to Restrict Behavior
**Original:**
```java
class Document {
    public void open() { ... }
    public void save() { ... }
}
```
**Proposed change:** Create `ReadOnlyDocument extends Document` that overrides `save()` to throw `UnsupportedOperationException`.

<details>
<summary>Answer</summary>

**LSP is violated.** `Document` promises that `save()` works. Code like `project.saveAll()` iterating over `List<Document>` will **crash** when it hits a `ReadOnlyDocument`.

**ISP is ALSO violated.** While ISP is usually talked about with Java `interface` types, the underlying principle applies to base classes too: the `ReadOnlyDocument` subclass is being forced to inherit a `save()` method it has no use for, leading directly to the exception-throwing hack. The base contract is too fat.

**Better fix:** Make `Document` only have `open()`. Create `WritableDocument extends Document` with `save()`. Code that needs to save receives a `List<WritableDocument>`.
</details>

---

### Q14 — Extracting Tax Logic into Its Own Class
**Original:** `Order` class contains `getTaxRate()`, `getOrderTotal()`, line item data, customer info.

**Proposed change:** Extract `TaxCalculator` as a separate class, have `Order` delegate to it.

<details>
<summary>Answer</summary>

This change **maintains/improves**:
- **SRP** ✅ — `Order` now changes only when order structure changes, not when tax law changes
- **OCP** ✅ — Different tax strategies can now be swapped without editing `Order`
- **DIP** ✅ — If `Order` depends on a `TaxCalculator` interface (not concrete class), high-level module doesn't depend on low-level detail
</details>

---

### Q15 — Making a Singleton
**Original:** A `Logger` with a public constructor.

**Proposed change:** Make constructor private, add `static Logger getInstance()`.

<details>
<summary>Answer</summary>

**SRP is (mildly) violated.** The class now manages two concerns: logging AND its own lifecycle/access control. This is a well-known trade-off of the Singleton pattern — it deliberately bends SRP to guarantee a single instance.

This is a common exam trap: recognizing that Singleton **intentionally** violates SRP.
</details>

---

## Category 4: Multi-Principle Analysis & Design Pattern Questions

### Q16 — Which Principles Does This Code Violate? (Multi-answer)
```java
class ReportService {
    public void generateReport(String type, List<Data> data) {
        String content;
        if ("PDF".equals(type))       content = formatAsPDF(data);
        else if ("CSV".equals(type))  content = formatAsCSV(data);
        else if ("HTML".equals(type)) content = formatAsHTML(data);
        else throw new IllegalArgumentException();

        new EmailClient().send("boss@company.com", content);
        new MySQLDatabase().save(content);
    }
}
```

<details>
<summary>Answer</summary>

**Four violations:**

| Principle | Why |
|---|---|
| **SRP** | Three responsibilities: formatting, emailing, persisting |
| **OCP** | Adding XML format requires editing the `if/else` chain |
| **DIP** | Directly instantiates `new EmailClient()` and `new MySQLDatabase()` (concretions, not abstractions) |
| **ISP** | Not directly, but if any client uses `ReportService`, they're coupled to formatting, email, AND database |
</details>

---

### Q17 — Builder Pattern and SRP
**Original:** An `Order` class with a 14-parameter constructor that handles validation, defaults, normalization, and business logic.

**Proposed change:** Extract an `Order.Builder` inner class that handles construction, validation, and defaults. `Order` only holds data and business methods.

<details>
<summary>Answer</summary>

**SRP is improved.** Before: `Order`'s reasons to change included both "order data structure changed" AND "construction/validation rules changed." After: construction logic lives in `Builder`, business logic lives in `Order`. Two separate concerns, two separate code paths.

This is exactly why the Builder pattern is said to "lean on SRP" (from the lecture's Principle → Pattern mapping table).
</details>

---

### Q18 — Factory Method and OCP/DIP
```java
abstract class NotificationCreator {
    public void sendNotification(String msg) {
        Notification n = createNotification();
        n.send(msg);
    }
    protected abstract Notification createNotification();
}
class EmailNotificationCreator extends NotificationCreator {
    protected Notification createNotification() { return new EmailNotification(); }
}
```
**Question:** Which principles does this design follow? What happens if we add `PushNotificationCreator`?

<details>
<summary>Answer</summary>

**Follows OCP and DIP:**
- **OCP:** Adding `PushNotificationCreator extends NotificationCreator` + `PushNotification implements Notification` requires **zero edits** to existing classes
- **DIP:** `NotificationCreator.sendNotification()` depends on the abstract `Notification`, not any concrete class

Adding `PushNotificationCreator` **maintains** both principles — pure extension, no modification.
</details>

---

### Q19 — Abstract Factory and ISP
An `AbstractFactory` interface has:
```java
interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
    Slider createSlider();
    DatePicker createDatePicker();
}
```
A `MinimalUIFactory` only needs buttons and checkboxes.

<details>
<summary>Answer</summary>

**ISP violation.** `MinimalUIFactory` is forced to implement `createSlider()` and `createDatePicker()` it doesn't support.

**Fix:** Split into `BasicUIFactory` (button + checkbox) and `AdvancedUIFactory extends BasicUIFactory` (slider + datepicker). Or use separate interfaces per widget family.
</details>

---

### Q20 — Singleton and DIP
```java
class OrderService {
    public void process(Order order) {
        DatabaseConnection db = DatabaseConnection.getInstance();
        db.save(order);
    }
}
```

<details>
<summary>Answer</summary>

**DIP violation.** `OrderService` (high-level) depends directly on the concrete `DatabaseConnection` class. You cannot test `OrderService` with a mock database — `getInstance()` always returns the real Singleton.

**Fix:** Inject a `Database` interface:
```java
class OrderService {
    private final Database db;
    public OrderService(Database db) { this.db = db; }
    public void process(Order order) { db.save(order); }
}
```
The Singleton can still exist internally, but `OrderService` doesn't know or care.
</details>

---

## Category 5: Tricky Edge Cases & Scenario Questions

### Q21 — "Does This Violate SRP?"
```java
class Calculator {
    public int add(int a, int b)      { return a + b; }
    public int subtract(int a, int b) { return a - b; }
    public int multiply(int a, int b) { return a * b; }
    public int divide(int a, int b)   { return a / b; }
}
```

<details>
<summary>Answer</summary>

**No violation.** All four methods serve **one purpose**: arithmetic computation. SRP says "one reason to change" — the only reason this class changes is if arithmetic rules change. Having many methods is fine if they're all cohesive.

**Trap:** Students often confuse "one method" with "one responsibility." A class with 10 methods can follow SRP if all methods serve the same purpose.
</details>

---

### Q22 — "Is This OCP Compliant?"
```java
class Logger {
    private List<LogHandler> handlers = new ArrayList<>();
    
    public void addHandler(LogHandler h) { handlers.add(h); }
    
    public void log(String msg) {
        for (LogHandler h : handlers) {
            h.handle(msg);
        }
    }
}
```

<details>
<summary>Answer</summary>

**Yes — OCP compliant.** New log destinations (file, console, remote server) are added by implementing `LogHandler` and calling `addHandler()`. The `Logger` class itself never needs to be modified.
</details>

---

### Q23 — "Does Composition Over Inheritance Fix LSP?"
A `Stack` is implemented as `class Stack extends ArrayList`. Client code receives a `List` and calls `.add(index, element)` to insert at arbitrary positions — which breaks the LIFO contract of a stack.

<details>
<summary>Answer</summary>

**LSP violation via inheritance.** `Stack extends ArrayList` promises it can do everything `ArrayList` can, but a stack shouldn't allow arbitrary-position insertion.

**Fix with composition:**
```java
class Stack<T> {
    private final List<T> data = new ArrayList<>();
    public void push(T item) { data.add(item); }
    public T pop()           { return data.remove(data.size() - 1); }
    public T peek()          { return data.get(data.size() - 1); }
    // NOT a List — can't be treated as one
}
```
By using composition instead of inheritance, `Stack` **doesn't inherit** the problematic `List` interface, so there's no LSP issue.
</details>

---

### Q24 — "Two Changes, Two Different Answers"
```java
interface Animal {
    void eat();
    void makeSound();
}
```

**Change A:** Add `void swim()` to the interface.  
**Change B:** Create `interface Swimmable { void swim(); }` separately.

<details>
<summary>Answer</summary>

**Change A violates ISP.** `Dog`, `Cat`, and every non-swimming `Animal` must now implement `swim()` with empty/exception bodies.

**Change B maintains ISP.** Only animals that can swim implement `Swimmable`. `Duck implements Animal, Swimmable` — `Cat implements Animal` only. No forced empty methods.
</details>

---

### Q25 — "Which Principle Is This Not Violating?"
```java
class ElectricPowerSwitch {
    private Switchable device;
    
    public ElectricPowerSwitch(Switchable device) {
        this.device = device;
    }
    
    public void press() {
        if (isOn()) device.turnOff();
        else        device.turnOn();
    }
}
```

<details>
<summary>Answer</summary>

This code **follows DIP correctly.** The switch depends on `Switchable` (an abstraction), not on `LightBulb` or `Fan` (concretions). Any `Switchable` device can be passed in without changing the switch code.

It also follows **OCP** — new devices (`Heater`, `GarageDoor`) are added by implementing `Switchable`, not by editing `ElectricPowerSwitch`.
</details>

---

### Q26 — The Subtle SRP + OCP Combo
```java
class AreaCalculator {
    public double totalArea(Object[] shapes) {
        double total = 0;
        for (Object shape : shapes) {
            if (shape instanceof Circle)
                total += Math.PI * ((Circle) shape).radius * ((Circle) shape).radius;
            else if (shape instanceof Rectangle)
                total += ((Rectangle) shape).width * ((Rectangle) shape).height;
        }
        return total;
    }
}
```

<details>
<summary>Answer</summary>

**Both SRP and OCP violated simultaneously** (this is the lecture's exact example!):

- **OCP:** Adding `Triangle` requires editing the `if/else` chain
- **SRP:** `AreaCalculator` knows the area formula for **every** shape — it changes whenever any shape's formula changes

**Fix:** Each shape provides its own `area()` method via a `Shape` interface. `AreaCalculator` just calls `shape.area()` — oblivious to which shape it is.

> **Key insight from the study guide:** "OCP and SRP usually get violated together and fixed together. That's not a coincidence — they're two views of the same underlying idea: *push variation into the parts of the code built to hold it.*"
</details>

---

### Q27 — DIP vs. "Just Using an Interface"
```java
class PaymentService {
    public void process(PaymentMethod method, double amount) {
        method.charge(amount);
    }
}
```
But in `main()`:
```java
PaymentService service = new PaymentService();
service.process(new CreditCardPayment(), 100.0);  // concretion in main()
```

**Is DIP violated?**

<details>
<summary>Answer</summary>

**No — DIP is satisfied.** `PaymentService` depends on `PaymentMethod` (abstraction), not `CreditCardPayment`. The concretion in `main()` is expected — *something* must create the concrete objects. DIP says **high-level modules** shouldn't depend on low-level modules. The composition root (`main()`) is the one place where concrete wiring is acceptable.
</details>

---

### Q28 — "Does Adding a Default Method Fix ISP?"
```java
interface CloudProvider {
    void createServer(String region);
    void storeFile(String name);
    default void getCDNAddress() { throw new UnsupportedOperationException(); }
}
```

<details>
<summary>Answer</summary>

**No — ISP is still violated.** A `default` method that throws an exception is just a more convenient way to write a stub. `Dropbox` (storage-only) still "has" a `getCDNAddress()` method it can't honestly fulfill. The interface is still too fat.

Default methods fix **backward compatibility** (adding to an existing interface without breaking implementors). They do NOT fix ISP — the correct fix is splitting the interface.
</details>

---

### Q29 — Recognizing Multiple Principles in One Fix
**Before:**
```java
class LightSwitch {
    private LightBulb bulb = new LightBulb();
    public void press() {
        if (on) bulb.turnOff();
        else    bulb.turnOn();
    }
}
```

**After:**
```java
interface Switchable { void turnOn(); void turnOff(); }
class LightSwitch {
    private Switchable device;
    public LightSwitch(Switchable device) { this.device = device; }
    public void press() {
        if (on) device.turnOff();
        else    device.turnOn();
    }
}
```

**Which principles did this fix address?**

<details>
<summary>Answer</summary>

Three principles improved in one change:

| Principle | How |
|---|---|
| **DIP** | `LightSwitch` now depends on `Switchable` (abstraction), not `LightBulb` (concretion) |
| **OCP** | New devices (`Fan`, `Heater`) can be added without editing `LightSwitch` |
| **SRP** | `LightSwitch` is no longer coupled to bulb-specific details — its only job is toggling |

This is the lecture's own example (Slides 23–31). It's a perfect demonstration of how fixing DIP tends to fix OCP and SRP simultaneously.
</details>

---

### Q30 — "Is This SRP or ISP?"
A `UserService` interface:
```java
interface UserService {
    User findById(int id);
    List<User> findAll();
    void save(User u);
    void delete(int id);
    void sendPasswordResetEmail(User u);
    void generateLoginReport();
}
```

<details>
<summary>Answer</summary>

**Both:**
- **ISP violation** — a component that only needs to query users is forced to depend on email and reporting methods
- **SRP violation** — if this were a class, it would have three reasons to change: user CRUD, email infrastructure, and reporting format

> From the study guide: *"ISP is SRP applied to interfaces."* This is a textbook case of that relationship.

**Fix:** Split into `UserRepository` (CRUD), `PasswordResetService` (email), and `LoginReportService` (reporting).
</details>

---

## Exam Strategy — Quick Decision Framework

When you see code and need to identify the principle:

```
Is a class doing too many unrelated things?          → SRP
Is adding a new type forcing edits to existing code? → OCP
Does a subclass break behavior the parent promised?  → LSP
Is a class forced to implement useless methods?      → ISP
Does high-level code directly create/name low-level? → DIP
```

> [!TIP]
> **Multiple violations often co-exist.** If you spot one, always scan for others. The most common combo is **SRP + OCP** (fat class with an `if/else` chain). The exam may ask you to identify ALL violations, not just one.

> [!IMPORTANT]
> **"Which pattern violates which principle?"** is a common trick question. The answer they expect: **Singleton violates SRP** — it manages both its business logic and its own lifecycle. Know this one cold.
