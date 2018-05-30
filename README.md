# wikip
Analyzes data from the Quandl Wiki Prices dataset

# Building
The project is written in Java 8 and built using Apache Maven 3.3.9 (although earlier versions of Maven in the 3.x branch should work). After checking out the project and navigating into the root directly simply execute the following command to build the application:

- `mvn clean package`

# Executing
The projects runs as an executable Java Jar file. After the build process is complete the Jar file can be found inside of the `target` directory as the file `wikip-1.0-uber.jar`. This uber jar contains all classes and dependencies needed for the application to run. Executing the application is done using the following command:

- `java -jar <path to wikip-1.0-uber.jar> `

For example, if you are in the root directory of the Git project and have built the application via Maven, then the following command will run the application:

- `java -jar target/wikip-1.0-uber.jar `
