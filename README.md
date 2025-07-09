#  Car Rent Management System

The Car Rent Management System automates the workflow of a car rental agency, including customer handling, vehicle tracking, booking management, payments, and maintenance logging. It improves efficiency, supports scalable data growth, and provides clear reporting for strategic planning.


![Car Rent System Screenshot](https://raw.githubusercontent.com/AmjadAzward/Car-Rent-System/main/Extra/Images/Screenshot%202025-06-19%20152745.png)

![Car Rent System Screenshot](https://raw.githubusercontent.com/AmjadAzward/Car-Rent-System/main/Extra/Images/Screenshot%202025-06-19%20152813.png)

![Car Rent System Screenshot](https://raw.githubusercontent.com/AmjadAzward/Car-Rent-System/main/Extra/Images/Screenshot%202025-06-19%20152908.png)

---

##  Objectives

* Automate car rental workflows and reduce manual operations.
* Manage scalable customer, vehicle, and booking data.
* Track and update car availability, rental status, and maintenance.
* Generate detailed reports for business analysis and decision-making.

---

##  Development Tools and Setup Details

| Tool/Library               | Version  | Notes/Links                                                                                   |
| -------------------------- | -------- | --------------------------------------------------------------------------------------------- |
| Java SE Development Kit    | 11.0.2   | [Download JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)          |
| Apache Ant (Build Tool)    | 1.10.13  | [Download Ant](https://ant.apache.org/bindownload.cgi)                                        |
| Java Swing (GUI Framework) | Built-in | Comes with JDK – no separate install                                                          |
| MySQL Database             | 8.0.23   | [Download MySQL](https://dev.mysql.com/downloads/mysql/8.0.html)                              |
| MySQL JDBC Driver          | 8.0.23   | [Download Connector/J](https://dev.mysql.com/downloads/connector/j/)                          |
| JasperReports              | 6.21.3   | [Download JasperReports](https://sourceforge.net/projects/jasperreports/files/jasperreports/) |
| Apache NetBeans IDE        | 23       | [Download NetBeans](https://netbeans.apache.org/download/nb23/)                               |

---

##  Setup Instructions

### 1. Install Java JDK 11.0.2

* Download and install from Oracle
* Set `JAVA_HOME` and update your system `PATH`

### 2. Install Apache Ant 1.10.13

* Extract and add `ant/bin` to your system `PATH`

### 3. Install MySQL Server 8.0.23

* Create a new database, e.g., `car_rental_db`
* Create a user and grant necessary privileges

### 4. Add MySQL Connector/J to Project

* Download and include `mysql-connector-java-8.0.23.jar` in your classpath

### 5. Include JasperReports Libraries

* Add all necessary `.jar` files from JasperReports to your project's libraries

### 6. Import Project in NetBeans IDE

* Open Apache NetBeans IDE
* Import or create a new Java project
* Configure required libraries (JDBC, JasperReports)

### 7. Configure Database Connection in Code

Example JDBC settings in your Java source:

```java
String url = "jdbc:mysql://localhost:3306/car_rental_db";
String user = "your_db_username";
String password = "your_db_password";
```

### 8. Build and Run the Application

* Use Apache Ant or NetBeans' build options
* Run the `Main` class or the main entry point of the system

---

##  Additional Notes

* Ensure MySQL server is running before launching the system
* UI built using Java Swing (no separate installation needed)
* Place JasperReports templates (`.jrxml` or `.jasper`) correctly in the project folder
* Project supports modular tab-based navigation (Cars, Customers, Rentals, Payments, etc.)

---

##  Useful Links

| Tool/Library        | Version | Download Link                                                                                 |
| ------------------- | ------- | --------------------------------------------------------------------------------------------- |
| Oracle JDK          | 11.0.2  | [JDK Download](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)          |
| Apache Ant          | 1.10.13 | [Ant Download](https://ant.apache.org/bindownload.cgi)                                        |
| MySQL Server        | 8.0.23  | [MySQL Download](https://dev.mysql.com/downloads/mysql/8.0.html)                              |
| MySQL Connector/J   | 8.0.23  | [Connector/J Download](https://dev.mysql.com/downloads/connector/j/)                          |
| JasperReports       | 6.21.3  | [JasperReports Download](https://sourceforge.net/projects/jasperreports/files/jasperreports/) |
| Apache NetBeans IDE | 23      | [NetBeans Download](https://netbeans.apache.org/download/nb23/)                               |
