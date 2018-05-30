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

## Command Line Flags
Various command line flags are available to alter execution of the program and the results displayed. For a full listing (including description) of these flags simply add the `-help` flag. In general, the following options are available to you:

- Adjust the set of Stock Tickers to retrieve data from
- Adjust the `StartDate` of the query
- Adjust the `EndDate` of the query
- Display additional data inside of the report, to include
  - BiggestLoser
  - Busiest Days
  - Max Daily Profit
  - Note: The Average Monthly Open and Close of each security will always be displayed in addition to any additional data requested by the previous flags.
  
## Default Settings
When no additional command line flags are present, the following defaults will be applied:

- Query for the Stock Tickers `GOOGL, MSFT and COF`
- Start and End Date of the query is 01/01/2017 - 06/30/2017
- Display the Average Monthly Open and Close of each security

The above default is equivalent to executing the application with the following flags: 
- `java -jar <path to wikip-1.0-uber.jar> -ticker GOOGL -ticker MSFT -ticker COF -startDate 2017-01-01 -endDate 2017-06-30 `
