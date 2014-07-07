MFDatabase
==========
Project Overview

1. Problem: Build a query processing engine for the MF queries because traditional optimizers lead to poor performance.

2. Solution: The project introduces the Φ operator, and scan each tuple for each grouping attributes instead of using joins

3. Technology We Are Using

- Language: Java
- DBMS: PostgreSQL
- Library: PostgreSQL Java Library

4. HighLights

- Check if select clause is valid? “‘: selectAttr
- Generate Minimum Loop using revised topological sort! Can check whether a particular condition is valid
(ex. if avg_1_quant is not created for some group, then this group should not ignored for 2.quant > avg_1_quant)

Please see project.pdf for more details.
