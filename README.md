# SWE Exercice - Abstraction and declarative programming 

You are a programmer in a software development team of business application for an Austrian retail bank. As a first task the team’s lead developer wants you to extend a connector to a third-party solution, which is managing securities/stocks of the bank.

The system you have to connect to is a very old legacy system and therefore, the request and response objects are formatted as fixed width text (in our example you get files, in reality, you could also request them files via HTTP calls).

A previous member of your new team started to implement a first version of the connector. It is already able to read information about security accounts. The lead developer checked the code and was quite satisfied with it. It fulfills exactly the needs. At this time the business application just needed to read overview data from the legacy system.

Time was passing and 4 months later another developer joined the team. The lead developer knew he has again a business functionality in the backlog which was related to securities. He spared it for the new guy who had to develop the connector for reading meta information from stocks. The lead developer checked the code and was a bit disappointed, as the connector which already existed was just copied and changed to read the information from another connector. Code reuse was not given and the effort for maintenance increased. But because the team was busy at that time and as from a functional point of view, there was nothing bad with the implementation, it was decided to postpone a possible refactoring.

Then you joined the team! To get an overview of the project, you refactored together with the lead developer the two connectors (this part was done during the lecture). On the next day, during the daily standup, you mentioned this refactoring as your current task. The product owner got alerted and mentioning new upcoming business requirements which are all related to securities and maybe also to your connector refactoring. After the standup you and the lead developer recognized that more than 30 files need  to be implemented in the upcoming weeks.

Thus, you needed to keep this in mind and beside a simple refactoring your task became to handle these fixed width text files by a reusable and generic solution. Due to licensing problems all available open source solutions on the market are not usable in your project. So you have to develop your own one. The only requirement the lead developer mentioned: The library should not force the usage of imperative programming. He wants you to implement the solution in a way, that whenever you have to implement a new transaction, you simply describe the data fields of your model and the mapping from the fixed width file format is handled. 

During a discussion about possible implementation strategies, he mentioned that coming up with an own DSL or using annotations could be a possible approach. You could use one of these approaches or find another way to provide this mapping between models and files in a declarative way. For the sake of simplicity, you should refactor the existing connectors to your new approach, but ensure that the existing requirements for the two given data structures. are still fulfilled. Keep in mind, that you only need to read the data from the files -  writing and mapping them back to the legacy system is not required.

See here a description of the data structures:

## SecurityAccountOverview

### Structure

fieldname | length | example | padding char | alignment
--------- | ------ | ------- | ------------ | ---------
Transaction name | 40 | SecurityAccountOverview | blank | left
Security Account Number | 10 | 12345678 | 0 | right
Risk category | 2 | 00 | none | left
Depot Owner Lastname | 30 | MUSTERMANN | blank | right
Depot Owner Firstname | 30 | MAX UND MARIA | blank | right
Currency | 3 | EUR | blank | left
Balance | 17 | 1692.45 | blank | right

## SecurityConfiguration

### Structure

fieldname | length | example | padding char | alignment
--------- | ------ | ------- | ------------ | ---------
Transaction name | 40 | SecurityConfiguration | blank | left
ISIN | 12 | AT0000937503 | none | left
Risk category | 2 | 02 | none | left
Name | 30 | voestalpine Aktie | blank | right
Currency | 3 | EUR | blank | left
52 Weeks Highest | 10 | 54.98 | blank | right
52 Weeks Lowest | 10 | 29.60 | blank | right

## Risk Category

Category | Value
-------- | -----
NON-EXISTING | 00
EXECUTION-ONLY | 01
LOW | 02
MIDDLE | 04
HIGH | 06
SPECULATIVE | 08

## Disclaimer

Have fun and do not hesitate to ask questions. The goal of this lesson is not a perfect implementation, rather finding the right level of abstraction and getting more in touch with declarative programming.

