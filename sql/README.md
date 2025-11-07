# Introduction

This project focuses on developing and applying both fundamental and advanced SQL skills through practical exercises and data modeling tasks. The primary objective was to design and interact with a relational database schema, define data relationships using DDL (Data Definition Language), and perform CRUD operations, joins, aggregations, and subqueries to gain hands-on experience with PostgreSQL.

I worked extensively on writing SQL queries to extract, manipulate, and analyze data from the club membership database (clubdata.sql). I also studied the basics of data modeling and data normalization to ensure efficient and well-structured database design. Additionally, I installed and configured DBeaver on Rocky Linux, connected it to a PostgreSQL instance running in a Docker container, and used it to manage and query the database efficiently.

Through this project, I gained practical experience in setting up a database environment, designing normalized schemas, performing analytical queries, and simulating real-world database workflows—from schema design and data loading to complex querying—strengthening both my database design understanding and SQL proficiency.

# SQL Queries

###### Table Setup (DDL)

-- Table: cd.members
CREATE TABLE cd.members (
    memid SERIAL PRIMARY KEY,
    surname VARCHAR(50) NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    address VARCHAR(100),
    zipcode VARCHAR(10),
    telephone VARCHAR(20),
    recommendedby INT REFERENCES cd.members(memid),
    joindate DATE NOT NULL
);

-- Table: cd.facilities
CREATE TABLE cd.facilities (
    facid SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    membercost NUMERIC(10,2) DEFAULT 0,
    guestcost NUMERIC(10,2) DEFAULT 0,
    initialoutlay NUMERIC(10,2) DEFAULT 0,
    monthlymaintenance NUMERIC(10,2) DEFAULT 0
);

-- Table: cd.bookings
CREATE TABLE cd.bookings (
    bookingid SERIAL PRIMARY KEY,
    facid INT NOT NULL REFERENCES cd.facilities(facid),
    memid INT NOT NULL REFERENCES cd.members(memid),
    starttime TIMESTAMP NOT NULL,
    slots INT NOT NULL
);


SQL Query Practice

--Question 1

INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES  (9, 'Spa', 20, 30, 100000, 800);


--Question 2

INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT MAX(facid) + 1, 'Spa', 20, 30, 100000, 800
FROM cd.facilities;


--Question 3

UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';

--Question 4

UPDATE cd.facilities AS f2
SET membercost = f1.membercost * 1.1,
    guestcost  = f1.guestcost  * 1.1
FROM cd.facilities AS f1
WHERE f2.name = 'Tennis Court 2'
  AND f1.name = 'Tennis Court 1';

--Question 5

DELETE FROM cd.bookings;

--Question 6

DELETE FROM cd.members
WHERE memid = 37

--Question 7

SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0
  AND membercost < (monthlymaintenance / 50);

--Question 8

SELECT *
FROM cd.facilities
WHERE name LIKE '%Tennis%';


--Question 9

SELECT *
FROM cd.facilities
WHERE facid IN (1, 5);

--Question 10

SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate > '2012-09-01';

--Question 11

SELECT surname AS name
FROM cd.members
UNION
SELECT name
FROM cd.facilities;

--Question 12

SELECT starttime
FROM cd.bookings
JOIN cd.members ON cd.bookings.memid = cd.members.memid
WHERE cd.members.firstname = 'David'
  AND cd.members.surname = 'Farrell';


--Question 13

SELECT b.starttime, f.name
FROM cd.bookings b
JOIN cd.facilities f ON b.facid = f.facid
WHERE f.name IN ('Tennis Court 1','Tennis Court 2')
  AND DATE(b.starttime) = '2012-09-21'
ORDER BY b.starttime;

--Question 14

SELECT
    m.firstname AS memsname,
    m.surname AS memfname,
    r.firstname AS recsname,
    r.surname AS recfname
FROM cd.members m
LEFT JOIN cd.members r ON m.recommendedby = r.memid
ORDER BY m.surname, m.firstname;

--Question 15

SELECT DISTINCT m.firstname, m.surname
FROM cd.members m
JOIN cd.members r ON m.memid = r.recommendedby
ORDER BY m.surname, m.firstname;

--Question 16

SELECT DISTINCT
    m.firstname || ' ' || m.surname AS member,
    r.firstname || ' ' || r.surname AS recommender
FROM cd.members m
LEFT JOIN cd.members r ON m.recommendedby = r.memid
ORDER BY member;

--Question 17

SELECT recommendedby, COUNT(*) AS count
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;

--Question 18

SELECT facid, SUM(slots) AS total_slots
FROM cd.bookings
GROUP BY facid
ORDER BY facid;


--Question 19

SELECT facid, SUM(slots) AS total_slots
FROM cd.bookings
WHERE starttime >= '2012-09-01' 
  AND starttime < '2012-10-01'
GROUP BY facid
ORDER BY total_slots;

--Question 20

SELECT 
    facid,
    EXTRACT(MONTH FROM starttime) AS month,
    SUM(slots) AS total_slots
FROM cd.bookings
WHERE EXTRACT(YEAR FROM starttime) = 2012
GROUP BY facid, month
ORDER BY facid, month;

--Question 21

SELECT COUNT(DISTINCT memid) AS total_members
FROM cd.bookings;

--Question 22

SELECT
    m.surname,
    m.firstname,
    m.memid,
    MIN(b.starttime)
FROM cd.members m
JOIN cd.bookings b ON m.memid = b.memid
WHERE b.starttime > '2012-09-01'
GROUP BY m.memid, m.surname, m.firstname
ORDER BY m.memid;

--Question 23

SELECT 
    COUNT(*) over(),
    firstname,
    surname
FROM cd.members
ORDER BY joindate;

--Question 24

SELECT
    ROW_NUMBER() OVER () AS row_number,
    firstname,
    surname
FROM cd.members
ORDER BY joindate;

--Question 25

WITH total_slots_per_facility AS (
    SELECT facid, SUM(slots) AS total_slots
    FROM cd.bookings
    GROUP BY facid
)
SELECT facid, total_slots AS total
FROM total_slots_per_facility
WHERE total_slots = (
    SELECT MAX(total_slots)
    FROM total_slots_per_facility
);

--Question 26

SELECT surname || ', ' || firstname AS member_name
FROM cd.members;

--Question 27

SELECT memid, telephone
FROM cd.members
WHERE telephone LIKE '%(%' OR telephone LIKE '%)%'
ORDER BY memid;

--Question 28

SELECT LEFT(surname, 1) AS initial, COUNT(*) AS member_count
FROM cd.members
GROUP BY LEFT(surname, 1)
ORDER BY initial
