# Medialab-Assistant
**MediaLab Assistant** is a desktop task management application built with **JavaFX** and **Maven**. It enables users to create, organize, and monitor their tasks by grouping them into fully customizable categories, designating them into different priority levels, and assigning the appropriate deadlines and reminders; all through an interactive graphical interface. It supports full task lifecycle management (creation, modification, deletion, and status tracking), advanced search functionality, notification history, and persistent local storage achieved through the usage of JSON files that ensure data consistency across sessions.

This project was developed as part of the "Multimedia Technology" course at the School of Electrical and Computer Engineering (ECE) at the National Technical University of Athens (NTUA). For more information about the assignment's requirements, as well as the final report on the project, please refer to the [documentation](documentation/). Details are provided both in greek and in english.

## Prerequisites
Before tackling on the project setup, make sure you have installed the following:
* [Java JDK 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven (&ge;3.6)](https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/)

## Setup Instructions
The first step is to clone the repository
```
git clone https://github.com/GiannisDimoulas/Medialab-Assistant.git
cd Medialab-Assistant
```

Below, I provide two methods you can use to run and test the project.
### 1. Maven CLI (recommended)
```
rm ./src/main/java/module-info.java
mvn clean install
mvn javafx:run
```
This method uses the configured JavaFX Maven plagin and will automatically lauch the app. However, the deletion of ```module-info.java``` in the beginning is required in this setup, as failure to do so will raise an exception. This is because the JSON dependency (```javax.json```) implementation is not a named module. This project uses the Java Module System (JPMS), which requires explicit module visibility. As a result, runtime fails to locate the provider. By removing ```module-info.java```, the project will not use the modular runtime configuration used by the JavaFX Maven Plagin, and run in classpath mode, instead, resolving the issue.

### 2. Eclipse IDE
This project was developed in [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/packages/) and as such, I provide a method to run the application via it. After downloading and configuring the IDE with the appropriate Maven and JDK versions, install [e(fx)eclipse](https://eclipse.dev/efxclipse/index.html) from the Eclipse Marketplace. Then:
1. Open Eclipse
2. Follow ```File → Import → Maven → Existing Maven Project```
3. Select the project folder
4. Locate ```MediaLab.Assistant.App```
5. ```Right click → Run as → Java Application```
