# wikip
Analyzes data from the Quandl Wiki Prices dataset

# Building
The project is written in Java 8 and built using Apache Maven 3.3.9 (although earlier versions of Maven in the 3.x branch should work). After checking out the project and navigating into the root directly simply execute the following command to build the application:

- `mvn clean package`

## Lombok
This application uses [Lombok](https://projectlombok.org/) to auto build data objects, builders, etc. When building from Maven Lombok annotions are automatically recognized and corresponding classes generated. If building from an IDE (i.e Eclipse, Intellij), you may need to configure it to recognize and support Lombok through the use of a plugin.

- [Eclipse Setup](https://projectlombok.org/setup/eclipse)
- [Intellij Setup](https://projectlombok.org/setup/intellij)

Instructions for other IDE's can be found on the Lombok project page.

# Executing
The projects runs as an executable Java Jar file. After the build process is complete the Jar file can be found inside of the `target` directory as the file `wikip-1.0-uber.jar`. This uber jar contains all classes and dependencies needed for the application to run. Executing the application is done using the following command:

- `java -jar <path to wikip-1.0-uber.jar> <optional command line flags>`

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

# Output
Data is written out as a JSON file with the following schema:
- `securityReports` : Contains a section for each Security, identified by its Stock Ticker. Each SecurityReport will list the following:
  - `avgMonthlyOpenCloses` : Lists the average open and close per month
  - `maxDailyProfit` : Optional. Lists the day of max profit
  - `busyDayDetails` : Optional. Lists the days, volume and average volume of busy days
- `biggestLoser` : Optional. Shows the security with the most losing days.

Fields marked as `Optional` can be displayed using the correct command line flag.

An example output (using the flags `-ticker GOOGL  -biggestLoser -busyDays -maxDailyProfit -endDate 2017-01-30`)

```
{
  "securityReports" : {
    "GOOGL" : {
      "avgMonthlyOpenCloses" : [ {
        "month" : "2017-01",
        "averageOpen" : 829.85,
        "averageClose" : 830.24
      }, {
        "month" : "2017-02",
        "averageOpen" : 836.15,
        "averageClose" : 836.75
      } ],
      "maxDailyProfit" : {
        "date" : "2017-01-27",
        "profit" : 25.1
      },
      "busyDayDetails" : {
        "busyDays" : [ {
          "date" : "2017-01-03",
          "volume" : 1959033.0
        }, {
          "date" : "2017-01-06",
          "volume" : 2017097.0
        }, {
          "date" : "2017-01-23",
          "volume" : 2457377.0
        }, {
          "date" : "2017-01-26",
          "volume" : 3493251.0
        }, {
          "date" : "2017-01-27",
          "volume" : 3752497.0
        }, {
          "date" : "2017-01-30",
          "volume" : 3516933.0
        }, {
          "date" : "2017-01-31",
          "volume" : 2020180.0
        }, {
          "date" : "2017-02-01",
          "volume" : 2251047.0
        } ],
        "averageVolume" : 1623909.0256410257
      }
    }
  },
  "biggestLoser" : {
    "ticker" : "GOOGL",
    "numberOfLosingDays" : 17
  }
}
```
