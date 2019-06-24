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

> The functionalities of this app are: 

### - **Libraries Used**

> **Volley** is used for communicating with web service. 

> The dependency is included in Gradle build file.

### - **Development Environment Setup**

> As the app is developed using native approach, **Android Studio** and related SDKs are required.

> The project may be directly launched, then be built and deployed once Gradle sync has completed.

> The web service module requires a server with **PHP** and **MySQL** support.

> To deploy the web service, please execute **notebook.sql** in the MySQL server to setup the database.

> Then, edit **model/connection.php** to setup the database host name, user name, password and database name.

> On Windows platform, **XAMPP** is recommended. Please place the entire **notebook** folder into **htdocs** directory.

> Please change the **URL** in the following files: **MainActivity.java**, **LoginActivity.java** and **RegisterActivity.java** to reflect the actual host name that the web service is hosted on.

