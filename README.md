# NoteBook

### - **Project Description**

### - **Modules/Functonalities**

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

