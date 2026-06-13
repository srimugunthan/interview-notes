# SQL Syntax
Here is a structured reference guide to the SQL syntax covered in the tutorial, complete with timestamps mapping to each concept:

https://www.youtube.com/watch?v=roGOgrPvNNY 

### 1. Data Retrieval Basics

* **SELECT & FROM:** Every query begins with `SELECT` to target specific columns and `FROM` to specify the table name. Use `*` as a wildcard to fetch all columns `[00:00:19]`.
* **WHERE:** Filters rows based on standard conditions (`=`, `!=`, `>`, `<`, `>=`, `<=`) `[00:00:33]`.
* **Logical Operators:** Combine conditions using `AND`, `OR`, and `NOT`. Use parentheses to dictate evaluation priority `[00:00:41]`.
* **IN & BETWEEN:** `IN` evaluates if a value matches any item in a provided list. `BETWEEN` checks inclusive ranges `[00:00:49]`.
* **LIKE (Pattern Matching):** Uses `%` to represent zero or more characters and `_` to represent exactly one character `[00:01:04]`.
* **ORDER BY:** Sorts query results. Defaults to ascending (`ASC`), but can be set to descending (`DESC`). You can sort by multiple columns consecutively `[00:01:21]`.
* **LIMIT & OFFSET:** Restricts the volume of output rows returned (`LIMIT`) and skips a designated number of rows (`OFFSET`) for pagination `[00:01:32]`.
* **DISTINCT:** Evaluates rows and strips duplicate combinations from the final output `[00:01:49]`.
* **AS (Aliases):** Grants a column or table a temporary, more readable name `[00:01:58]`.

### 2. Aggregation & Grouping

* **Aggregate Functions:** Calculations computed across rows: `COUNT()`, `SUM()`, `AVG()`, `MIN()`, and `MAX()`. Note that `COUNT(*)` includes NULL values, while `COUNT(column_name)` ignores them `[00:02:06]`.
* **GROUP BY:** Collects matching rows into summary groups. Any non-aggregated column in your `SELECT` must be included in your `GROUP BY` clause `[00:02:32]`.
* **HAVING:** Acts exactly like a `WHERE` clause, but evaluates aggregated row groups *after* grouping has taken place `[00:02:47]`.

### 3. Data Modification Language (DML)

* **INSERT:** Injects brand new rows into specified tables. Multiple records can be appended simultaneously by separating row groupings with commas `[00:02:56]`.
* **UPDATE:** Alters existing row data. Always specify a `WHERE` condition to avoid accidentally updating every record in the table `[00:03:13]`.
* **DELETE & TRUNCATE:** `DELETE` removes individual rows according to a `WHERE` filter. `TRUNCATE TABLE` completely clears all table contents quickly without logging individual row deletions, leaving the base structural schema intact `[00:03:20]`.

### 4. Data Definition Language (DDL)

* **Data Types:** Common allocations include `INT` (integers), `DECIMAL`/`NUMERIC` (precise numbers), `VARCHAR` (variable strings), `TEXT` (long text), `BOOLEAN`, `DATE`, `TIMESTAMP`, and `BLOB` (binary data) `[00:03:46]`.
* **Constraints:** Rules implemented on columns, including `PRIMARY KEY` (unique & not null identity), `NOT NULL`, `UNIQUE`, `DEFAULT`, `CHECK`, and `FOREIGN KEY` (creates relationships to other tables) `[00:04:16]`.
* **ALTER TABLE:** Modifies active schemas by adding, dropping, or rewriting columns and architectural parameters `[00:05:08]`.
* **DROP TABLE:** Irreversibly deletes an entire table architecture alongside all nested records `[00:05:17]`.

### 5. Table Joins

* **INNER JOIN:** Extracts only the matched records present across both linked tables `[00:05:34]`.
* **LEFT JOIN:** Fetches all records from the left table alongside matching rows from the right table (unmatched right rows generate `NULL` entries) `[00:05:43]`.
* **RIGHT JOIN:** The inverse of a Left Join; returns all entries from the right table with `NULL` defaults on unmatched left inputs `[00:05:50]`.
* **FULL OUTER JOIN:** Pulls all rows present in both systems, inserting `NULL` markers whenever structural relations fail to line up `[00:05:57]`.
* **CROSS JOIN:** Creates a full Cartesian product, matching every single row from the left table against every available row on the right `[00:06:04]`.
* **SELF JOIN:** Links a table to itself using explicit aliases to distinguish between the primary and virtual secondary instances `[00:06:21]`.

### 6. Set Operations & Subqueries

* **UNION vs UNION ALL:** `UNION` stacks query datasets while actively filtering out duplicates; `UNION ALL` retains all duplicated entries `[00:06:28]`.
* **INTERSECT & EXCEPT:** `INTERSECT` outputs only rows shared by both queries. `EXCEPT` isolates records from the first query that don't appear in the second `[00:06:36]`.
* **Subqueries:** Queries nested inside `WHERE`, `FROM`, or `SELECT` structures. Subqueries in the `FROM` block act as temporary virtual tables `[00:06:52]`.
* **EXISTS, ANY, & ALL:** `EXISTS` returns true if a subquery extracts at least one matching record. `ANY` matches conditions against any single array item, while `ALL` requires a condition to pass against every option `[00:07:16]`.

### 7. Logical Expressions & Functions

* **CASE:** Operates as standard inline `IF-THEN-ELSE` logic to yield output values based on conditional criteria `[00:07:50]`.
* **COALESCE & NULLIF:** `COALESCE()` extracts the very first non-null argument from a checklist. `NULLIF(x, y)` evaluates two variables, outputting `NULL` if they are identical `[00:08:05]`.
* **String Functions:** Formats strings using methods like `UPPER()`, `LOWER()`, `LENGTH()`, `CONCAT()`, `SUBSTRING()`, `TRIM()`, and `REPLACE()` `[00:08:22]`.
* **Date & Cast Functions:** Commands like `NOW()`, `CURRENT_DATE`, `EXTRACT()`, and `DATEDIFF()` manipulate date elements. Use `CAST()` to change an asset's data type on the fly `[00:08:35]`.

### 8. Views, CTEs, & Window Functions

* **Views:** Saved queries acting as virtual tables that run in real-time without storing independent underlying data `[00:09:06]`.
* **CTEs (Common Table Expressions):** Declared with the `WITH` keyword to break down complex code blocks into legible, temporary named outputs. *Recursive CTEs* reference themselves to navigate hierarchical data structures like organizational trees `[00:09:42]`.
* **Window Functions:** Compute calculations across sets of rows related to the current row without compressing them into single lines. Defined via the `OVER()` clause, often alongside a `PARTITION BY` and `ORDER BY` statement `[00:10:11]`.
* *Ranking Functions:* `ROW_NUMBER()` loops integers sequentially. `RANK()` skips numbers in case of a tie, whereas `DENSE_RANK()` advances values sequentially regardless of ties `[00:10:45]`.
* *Positional Functions:* `LAG()` and `LEAD()` access values from preceding or succeeding records `[00:11:00]`.



### 9. Administration & Optimization

* **Transactions:** Bundle commands into atomic blocks using `COMMIT` or `ROLLBACK` to preserve ACID criteria. Use `SAVEPOINT` to mark rollback checkpoints within a transaction `[00:11:16]`.
* **Stored Procedures & Triggers:** Reusable blocks of code saved on the server are executed with `CALL`. `TRIGGERS` execute automatically in response to events like `INSERT`, `UPDATE`, or `DELETE` `[00:11:43]`.
* **Indexes:** Structure columns to speed up query reading, though they add processing overhead to data writes (`INSERT`/`UPDATE`) `[00:09:28]`.
* **EXPLAIN & EXPLAIN ANALYZE:** `EXPLAIN` parses query execution blueprints (like index usages or scan paths). Adding `ANALYZE` forces the engine to run the code and output real-time processing metrics `[00:13:16]`.
