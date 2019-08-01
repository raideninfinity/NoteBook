# NoteBook

### - **Project Description**

> This project's aim is to create a note-taking native Android mobile app with online synchronization across devices.

> The app enables users to quickly write down their thoughts, draft out memos, or store any text that they want to keep around for any length of time. 

> The app provides users the functionality of creating new notes, editing notes, or delete notes that are not needed anymore.

> Users are able to create an account to store their notes online, and retrieve/synchronize notes on other devices. 

> The mobile app part of this project is developed using Java and uses SQLite database and SharedPreferences to store internal data.

> The web service part of this project is developed using PHP and uses MySQL database.

### - **Modules/Functonalities**

> The modules of this app are:

> - Note List Module

> - Note Edit Module

> - SQLite Database Module

> - User Account Module

> - Online Synchronization Module

> The functionalities of this app are: 

> - New Note - Users are able to create new plain text notes.

> - View/Edit Note - Users are able to view and edit existing notes.

> - Delete Note - Users are able to delete unwanted notes.

> - Sign Up - Users are able to sign up for an account for online sync-ing

> - Sign In/Sign Out - Users are able to sign in with and sign out of existing accounts

> - Online Backup - Notes are backed up in a web server.

> - Online Synchronization - Notes can be retrieved from the web server and be synchronized across devices.

### - **Libraries Used**

> **Volley** is used for communicating with web service. 

> The dependency is included in Gradle build file.

### - **Development Environment Setup**

> The NoteBook folder contains project source codes for the mobile app while the WebService folder contains the SQL file for the database as well as PHP files for the web service. 

> As the app is developed using native approach, **Android Studio** and related SDKs are required.

> The project may be directly launched, then be built and deployed once Gradle sync has completed.

> The web service module requires a server with **PHP** and **MySQL** support.

> To deploy the web service, please execute **notebook.sql** in the MySQL server to setup the database.

> Then, edit **model/connection.php** to setup the database host name, user name, password and database name.

> On Windows platform, **XAMPP** is recommended. Please place the entire **notebook** folder into **htdocs** directory.

> Please change the **URL** in the following files: **MainActivity.java**, **LoginActivity.java** and **RegisterActivity.java** to reflect the actual host name that the web service is hosted on.

### - **Screenshots**

https://i.imgur.com/bdTrYNv.png

https://i.imgur.com/QUbPzsO.png
