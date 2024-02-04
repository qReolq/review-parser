# Review-parser

This Java application is designed to parse and save viewer reviews for a movie from the Kinopoisk website.
The application utilizes Jsoup for web scraping, and the parsed data is stored in CSV format using OpenCSV.

## Technologies
Java, Jsoup, Lombok, OpenCSV

## Usage
To run the application, provide the following command-line arguments:
```Bash
java -jar jar <csvFilePath> <prePages>
```
* csvFilePath: The path where the CSV file will be saved.
* prePages: Number of reviews processe by one thread(50,100,200). Affects the number of threads created in the executor service

## Quick start
1. Clone this repo into folder.

```Bash
git clone https://github.com/qReolq/review-parser.git
cd review-parser\target
```
2 Then, run the Main class with the appropriate command-line arguments.
```Bash
java -jar .\ReviewParser-1.0-SNAPSHOT.jar \path\to\reviews.csv 100
```

## Examle
```Bash
java -jar .\ReviewParser-1.0-SNAPSHOT.jar C:\Users\admin\Desktop 100
```
