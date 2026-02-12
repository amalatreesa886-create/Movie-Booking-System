

# Movie Ticket Booking System (Java + MySQL)

A desktop-based Movie Ticket Booking System developed using **Java Swing (GUI)** and **MySQL (Database)**.
This application allows users to book movie tickets with real-time price calculation and store booking details in a MySQL database.

---

## Features

* Book movie tickets
* Customer details (Name & Phone)
* Movie selection
* Show time selection
* Seat selection
* Number of tickets
* Automatic total price calculation
* Data stored in MySQL database
* JDBC integration with MySQL Connector

---

## Technologies Used

* Java (Swing GUI)
* MySQL Database
* JDBC (MySQL Connector J 9.6.0)
* Eclipse / IntelliJ IDEA

---

## Project Structure

```
MovieBookingSystem.java
```

The project follows a simple layered structure:

* **DBConfig** → Database configuration
* **Booking (Model)** → Data structure
* **BookingDAO (DAO Layer)** → Database operations
* **MovieBookingSystem (UI Layer)** → Swing GUI

---

## Database Setup

### Create Database

Run this in MySQL:

```sql
CREATE DATABASE moviebooking;
USE moviebooking;
```

### Create Table

```sql
CREATE TABLE bookings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100),
    phone VARCHAR(20),
    movie VARCHAR(100),
    show_time VARCHAR(50),
    seat VARCHAR(10),
    tickets INT,
    total_amount INT
);
```

---

## MySQL Configuration

Open `DBConfig` class and update password:

```java
static final String PASS = "your_password";
```

If you don’t have a password, leave it as:

```java
static final String PASS = "";
```

---

## Add MySQL Connector

Download:

MySQL Connector J 9.6.0

Then:

**In Eclipse**

```
Right Click Project → Build Path → Configure Build Path → Add External JAR
```

Add `mysql-connector-j-9.6.0.jar`

---

## How to Run

1. Start MySQL (XAMPP / Workbench)
2. Make sure database & table are created
3. Add MySQL Connector JAR
4. Run `MovieBookingSystem.java`
5. Fill details and click **Book Ticket**

---

## Application Preview (Features)

* Dark themed modern UI
* Real-time ticket price calculation
* Popup confirmation on successful booking
* Exception handling for invalid input
* Database error handling

---

## Concepts Used

* Object-Oriented Programming (OOP)
* MVC-like architecture
* JDBC Connectivity
* PreparedStatement
* Exception Handling
* Event Handling in Swing

---

## Future Improvements

* Admin panel to view bookings
* Seat availability tracking
* Ticket cancellation feature
* Payment gateway integration
* Print ticket functionality

---
