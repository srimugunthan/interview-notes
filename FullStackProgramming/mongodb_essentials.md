Here is the fixed markdown formatting:

```markdown
Here is the organized breakdown of the key concepts, commands, and workflows from the MongoDB tutorial video.

## 1. Introduction to MongoDB & NoSQL

* **What is MongoDB?** It is a popular NoSQL database management system designed to handle large volumes of data efficiently.
* **NoSQL Format ("Not Only SQL"):** Instead of storing data in fixed tables with rows and columns, MongoDB stores related data together in a single document using **field-value pairs**.
* **Data Format:** Data is fundamentally handled like JSON, but stored internally as **BSON** (Binary JSON) for efficiency.
* **Core Hierarchy:**
  * **Document:** A group of field-value pairs representing an individual object (analogous to a row in SQL).
  * **Collection:** A group of one or more documents (analogous to a table in SQL).
  * **Database:** A container holding one or more collections.

---

## 2. Database Operations (CRUD via Shell)

### Creating & Managing Databases

* **Show all databases:** `show dbs`
* **Switch to / Create a database:** `use <database_name>` *(Note: A new database remains hidden until a collection with data is added to it).*
* **Drop the current database:** `db.dropDatabase()`
* **Show all collections:** `show collections`
* **Create a collection:** `db.createCollection("<collection_name>")`
* **Drop a collection:** `db.<collection_name>.drop()`

### Inserting Documents

* **Insert one document:**
```javascript
db.students.insertOne({ name: "SpongeBob", age: 30, gpa: 3.2 })
```

* **Insert multiple documents:**
```javascript
db.students.insertMany([
  { name: "Patrick", age: 38, gpa: 1.5 },
  { name: "Sandy", age: 27, gpa: 4.0 }
])
```

### Reading & Querying Documents

* **Find all documents:** `db.students.find()`
* **Find with a query filter:** `db.students.find({ name: "SpongeBob" })`
* **Projection (Selecting specific fields):** Pass a second object where fields are toggled using `true`/`1` or `false`/`0`.
```javascript
db.students.find({}, { _id: false, name: true, gpa: true })
```

### Updating Documents

* **Update one document:** Uses the `$set` operator to replace or add values.
```javascript
db.students.updateOne({ name: "SpongeBob" }, { $set: { fullTime: false } })
```

* **Remove a field:** Uses the `$unset` operator with an empty string.
```javascript
db.students.updateOne({ name: "SpongeBob" }, { $unset: { fullTime: "" } })
```

* **Check for existence:** Update documents only if a specific field is missing using `$exists`.
```javascript
db.students.updateMany({ fullTime: { $exists: false } }, { $set: { fullTime: true } })
```

### Deleting Documents

* **Delete one document:** `db.students.deleteOne({ name: "Larry" })`
* **Delete multiple documents:** `db.students.deleteMany({ fullTime: false })`

---

## 3. Data Types Supported

MongoDB documents support a variety of data types, including:

* **String:** Text wrapped in single or double quotes (e.g., `"Larry Lobster"`).
* **Integer:** Whole numbers without decimals (e.g., `32`).
* **Double:** Decimal numbers used for high precision (e.g., `2.8`).
* **Boolean:** True or false flags (`true`/`false`).
* **Date:** Instantiated via `new Date()` (defaults to UTC timezone).
* **Null:** Represents a placeholder or an empty/non-existent value (`null`).
* **Array:** A list of multiple values enclosed in square brackets (e.g., `["biology", "chemistry"]`).
* **Nested Document:** Sub-documents embedded inside a field using curly braces (e.g., `address: { street: "123 Fake St", city: "Bikini Bottom" }`).

---

## 4. Sorting, Limiting, and Operators

### Sorting & Limiting

* **Sorting:** Use `.sort()`. Pass `1` for ascending/alphabetical order, and `-1` for descending/reverse order.
```javascript
db.students.find().sort({ gpa: -1 })
```

* **Limiting Results:** Use `.limit()` to specify the max number of documents returned.
```javascript
db.students.find().limit(3)
```

* **Chaining Methods:** You can combine them to find specific edge cases (e.g., find the student with the highest GPA).
```javascript
db.students.find().sort({ gpa: -1 }).limit(1)
```

### Comparison Operators

* **Not Equal (`$ne`):** `db.students.find({ name: { $ne: "SpongeBob" } })`
* **Less Than (`$lt`) / Less Than or Equal (`$lte`):** `db.students.find({ age: { $lte: 27 } })`
* **Greater Than (`$gt`) / Greater Than or Equal (`$gte`):** `db.students.find({ age: { $gte: 27 } })`
* **Ranges:** Combine multiple operators on a single field.
```javascript
db.students.find({ gpa: { $gte: 3.0, $lte: 4.0 } })
```

* **In Array (`$in`) / Not In Array (`$nin`):** Matches any value present within the specified array.
```javascript
db.students.find({ name: { $in: ["SpongeBob", "Patrick"] } })
```

### Logical Operators

* **`$and`:** Requires all inner expressions to evaluate to true.
```javascript
db.students.find({ $and: [ { fullTime: true }, { age: { $lte: 22 } } ] })
```

* **`$or`:** Requires at least one inner expression to evaluate to true.
```javascript
db.students.find({ $or: [ { fullTime: true }, { age: { $lte: 22 } } ] })
```

* **`$nor`:** Returns documents where all inner expressions evaluate to false.
```javascript
db.students.find({ $nor: [ { fullTime: true }, { age: { $lte: 22 } } ] })
```

* **`$not`:** Inverts the effect of a query expression and includes documents containing `null` values for that field.
```javascript
db.students.find({ age: { $not: { $gte: 30 } } })
```

---

## 5. Indexes Optimization

* **Purpose:** Indexes allow for fast, high-performance lookups by storing field references inside a B-tree structure, turning a slow linear collection scan into a fast index search.
* **Trade-off:** They consume extra memory and slow down write operations (`insert`, `update`, `delete`) because the index tree must be updated along with the data. 
* **Analyze Query Execution:** Append `.explain("executionStats")` to verify how many documents were scanned (`totalDocsExamined`).
* **Create Index:** `db.students.createIndex({ name: 1 })`
* **List Indexes:** `db.students.getIndexes()`
* **Drop Index:** `db.students.dropIndex("name_1")`

---

## 6. Advanced Collection Parameters

When explicitly creating a collection using `db.createCollection()`, you can pass optional configuration settings:

* **Capped Collections:** Fixed-size collections that automatically overwrite older documents when they reach their threshold limit.
* **Size Configurations:**
  * `capped: true` (enables sizing limits)
  * `size: <bytes>` (sets maximum size allocation in bytes)
  * `max: <number>` (sets maximum document count allowed in the collection)
* **Example Initialization:**
```javascript
db.createCollection("teachers", { capped: true, size: 10000000, max: 100 })
```
```

**Issues fixed:**
- Removed extra blank lines and stray backticks
- Fixed indentation for nested list items under "Core Hierarchy"
- Ensured consistent code block formatting with proper language specifiers
- Removed orphaned backticks and stray characters
- Fixed spacing issues in code examples
