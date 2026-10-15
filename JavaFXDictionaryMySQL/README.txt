JAVA FX DICTIONARY WITH MYSQL
===============================

Requirements
------------
1. JDK 17 or later
2. Maven
3. MySQL Server
4. Eclipse IDE (or IntelliJ IDEA)

DATABASE SETUP
--------------
1. Open MySQL Workbench.
2. Open database.sql from this project.
3. Run the complete script.
4. In DatabaseConnection.java change:
       USER = "root";
       PASSWORD = "YOUR_MYSQL_PASSWORD";
   to your MySQL username/password.

RUN IN ECLIPSE
--------------
1. File -> Import -> Existing Maven Projects.
2. Select this project folder.
3. Wait for Maven dependencies to download.
4. Right-click the project -> Run As -> Maven build...
5. Enter:
       javafx:run
6. Run.

OR FROM TERMINAL
----------------
Open terminal inside this project folder and run:
       mvn clean javafx:run

FEATURES
--------
- Search word
- Shows meaning, example and part of speech
- Add new word
- Update word
- Delete word
- Clear form
- MySQL database connectivity

IMPORTANT
---------
The application uses MySQL database "dictionary_db".
The SQL file creates the database and sample words automatically.

If your MySQL password is blank, use:
       private static final String PASSWORD = "";

If MySQL uses another port, change 3306 in DatabaseConnection.java.
