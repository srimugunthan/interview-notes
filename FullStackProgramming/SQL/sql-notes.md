Here is the reorganized transcript from the video titled **"15 SQL Interview Questions TO GET YOU HIRED in 2026"**, broken down question-by-question into structured points:

### 1. Difference Between DBMS and RDBMS [[00:54](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=54)]

* **DBMS (Database Management System):**
* Stores data in individual files with no connections or linkages between them.
* Searching for specific data requires checking files one by one, making it tedious and time-consuming.


* **RDBMS (Relational Database Management System):**
* Maintains relationships between data using interconnected tables (e.g., MySQL).
* Data is retrieved quickly through queries, reducing redundancy.
* Designed to support multiple users with high security and is optimized for large datasets.



### 2. Primary Key vs. Foreign Key [[02:18](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=138)]

* **Primary Key:**
* Uniquely identifies each row/record in a table (e.g., a student Roll Number).
* Ensures data is distinct, cannot be duplicated, and strictly prohibits empty or `NULL` values.


* **Foreign Key:**
* Connects and links data across different tables.
* Points back to the primary key of another table to maintain relational integrity.



### 3. Constraints and Their Types [[03:27](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=207)]

Constraints are rules applied to data columns to keep data accurate and reliable:

* **NOT NULL:** Prevents a column from accepting blank or empty values.
* **UNIQUE:** Ensures all values in a column are distinct (e.g., email IDs).
* **PRIMARY KEY:** A combination of `NOT NULL` and `UNIQUE`. Uniquely identifies a record.
* **FOREIGN KEY:** Links tables together by referencing a primary key in another table.
* **CHECK:** Enforces specific conditional rules on data values (e.g., age must be `> 18`).
* **DEFAULT:** Automatically fills a column with a predefined value if no value is manually provided.

### 4. DDL vs. DML Commands [[05:21](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=321)]

* **DDL (Data Definition Language):**
* Deals with the foundational **structure** of the database, not the actual data inside.
* *Key Commands:* `CREATE` (makes a new table), `ALTER` (modifies structural columns), and `DROP` (deletes an entire table structure).


* **DML (Data Manipulation Language):**
* Focuses on managing and interacting with the **actual data entries** within the tables.
* *Key Commands:* `INSERT` (adds rows), `UPDATE` (changes data), and `DELETE` (removes rows).



### 5. Difference Between DELETE, TRUNCATE, and DROP [[07:13](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=433)]

* **DELETE:**
* Removes specific rows based on a conditional `WHERE` clause.
* It is slower but can be rolled back (undone) if needed.


* **TRUNCATE:**
* Quickly clears out **all rows** from a table at once.
* Keeps the table structure intact for future use, but the data deletion cannot be rolled back.


* **DROP:**
* Permanently removes the **entire table structure** and all its data from the database.
* Cannot be undone.



### 6. GROUP BY vs. ORDER BY Clause [[08:44](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=524)]

* **GROUP BY:**
* Groups data into "buckets" to summarize rows based on shared values.
* Frequently used with aggregate functions (`SUM`, `AVG`, `COUNT`) to get summarized views (e.g., average salary per department).


* **ORDER BY:**
* Sorts the final resulting rows in a specified presentation sequence.
* Can organize records in ascending (`ASC`) or descending (`DESC`) order based on one or more columns.



### 7. WHERE vs. HAVING Clause [[10:44](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=644)]

* **WHERE Clause:**
* Acts as a filter **before** any grouping of data occurs.
* Filters individual rows based on specific conditions.


* **HAVING Clause:**
* Acts as a filter **after** grouping has taken place.
* Filters grouped data based on aggregate results (e.g., showing only departments with a `COUNT` of employees greater than 1).



### 8. Aggregate Functions in SQL [[12:19](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=739)]

Mathematical tools that perform calculations on a set of values and return a single value:

* **COUNT:** Counts the total number of rows or non-null values.
* **SUM:** Adds up numeric values in a column.
* **AVG:** Calculates the mathematical average of a numeric column.
* **MIN:** Finds the lowest value in a column.
* **MAX:** Finds the highest value in a column.

### 9. Indexing and Clustered Indexes [[13:39](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=819)]

* **Indexing:** Accelerates data retrieval so the database engine doesn't have to scan every single page or row linearly to find a record.
* **Clustered Index:** Physically organizes and sorts the actual data rows of the table on disk based on the index key (e.g., arranging records sequentially by Employee ID).

### 10. Database Normalization and Forms [[14:47](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=887)]

Normalization organizes data efficiently to minimize duplicate information (redundancy) and prevent anomalies during inserts, updates, or deletions:

* **1NF (First Normal Form):** Every cell must contain a single (atomic) value, and columns must have unique names.
* **2NF (Second Normal Form):** Must be in 1NF, and all non-key attributes must fully depend directly on the primary key.
* **3NF (Third Normal Form):** Must be in 2NF, and non-key attributes must be independent of other non-key attributes (no transitive dependencies).
* **BCNF (Boyce-Codd Normal Form):** A stricter version of 3NF where every determinant must be a candidate key.

### 11. UNION vs. UNION ALL Operator [[17:10](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=1030)]

* **UNION:** Combines the results of two `SELECT` queries into a single list and **removes all duplicate** rows.
* **UNION ALL:** Combines the results of two queries but **keeps every row**, including duplicates. It runs faster because it skips the sorting/deduplication step.

### 12. Finding the Second Highest Salary [[18:36](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=1116)]

* Solved using nested subqueries:
1. The innermost subquery finds the absolute maximum salary: `SELECT MAX(salary) FROM employee`.
2. The next level filters out this maximum to find the highest salary left over: `WHERE salary < (innermost query)`.
3. The outer query extracts the name and final matching salary. If multiple employees share this second-highest salary, it accurately returns all of them.



### 13. Views in SQL [[20:05](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=1205)]

* A view is a **virtual table** that doesn't physically store data on disk; it simply saves a query configuration.
* It dynamically draws data from base tables when queried.
* Excellent for security because it can hide sensitive database columns and restrict user permissions to specific sections of data.

### 14. Converting Text to Date Format [[21:18](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=1278)]

* Handled using the **`STR_TO_DATE()`** function.
* Takes a text string and a format mask (e.g., `%d` for day, `%m` for month, `%Y` for year) to translate text strings (like `'27-10-2024'`) into a recognized system date object.

### 15. Triggers in SQL [[21:50](http://www.youtube.com/watch?v=oX5Y26O5dBE&t=1310)]

* Triggers are automatic, reflex-like actions that execute in response to specified events in the database.
* They run on three distinct data events: `INSERT`, `UPDATE`, or `DELETE`.
* *Example given:* A library system can use a trigger to automatically decrease the available book count in a `books` table by one whenever a new checkout entry is added to a `borrowed_books` log table.
