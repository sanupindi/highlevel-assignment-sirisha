# Highlevel App -  Assignment - Scheduling Module
#### Assignee - Sirisha Anupindi
This repository contains the code/artifacts that were created as part of the Highlevel Scheduling module assignment (testcase development along with automation) .

#### Tech stack:
-----
- Java 1.8
- Maven 3.3.x
- TestNG 7.x

The folder layout with appropriate comments is given below:
- **src**: This folder contains the source code for the automation test case.
- **docs**: This folder contains the excel file (`HighLevelCalender_TestCases_Sirisha.xlsx`) that lists the manual test cases for the scheduling module.
  The excel file has 3 tabs:
    - TestCases : This tab lists all the documented test cases.
    - Observations: This tabs lists notes/observations from my end when I was exploring the app for a better use case understanding.
    - Additional Test Cases: While the `TestCases` tab is not necessarily exhaustive in listing all the test cases for this module, given the time constraint, I have listed a gist of other possible test cases that can be documented/used as a brief gist.

### Running the code
The code base assumes the following environment variables are setup prior to executing the code. I have used [dotenv](https://github.com/cdimascio/dotenv-java) library to enable custom environment variables injection. So either please setup the following environment variables at system level or create a `.env` file at the root of the project and add the entries as listed below:

- HIGHLEVEL_APP_USERNAME=a valid login to the app that has access to the calendars s etc.
- HIGHLEVEL_APP_PASSWORD=password of the account mentioned above
- CALENDAR_SUT_NAME=/automation1/autotest

I have hardcoded the calendar to use for the automation test case as short cut for this submission, ideally a team/calendar would be setup in the `BeforeMethod` and the same would be deleted in the `AfterMethod` methods.
