# Authorizer App

## Code Design

The design was thought to respect some principles as **SOLID**, **SOC** and **KISS** by using some Design Patterns.

### Structure

 - Main class: the responsibility of the Main class here was just to run the application, setting the Dependency Injection.
 - Controller: API contracts, I/O manager and controller are here.
 - Mappers: classes to transform objects into others to preserve Separation of Concerns.
 - Models: main application POJOs.
 - Services: business logic classes separated by domain (Account and Transaction). The processors calls them.
 - Processors: are classes that have the operation flux. The controller chooses the right processor to the operation 
through `isEligible()` processor method and then calls the `process()` method that return tha operation flux. This 
design is based on Strategy pattern.
 - Validators: based on Chain of Responsibility pattern, Validators are business rules validation classes used by 
services to get any violations about that rules.
 - ActiveAccount class: a Singleton pattern implementation to have only one Account instance to run the application.
 - ViolationType class: an Enum class to catalog all possible violations with their respective output string.     

### Extensibility

The application can be extended for new operations as well as new validations. 
Even existing ones can be removed without much change.  

To add new operations, you only needs to create a new `ProcessorStrategy` implementation and add this to
`OperationProcessor` class, to be accessed by the `AuthorizerController`.  

To add new validations, you will need to create a new `AccountValidator` or `TransactionValidator` implementation with 
the validations. If you need another validator format, you can create another Validator class following the 
pattern.
Make sure to add the new validations to `OperationValidator` class, specifying the chain sequence it belongs, or 
creating a new one.  

## Running instructions

To run the application, first you need to go to the project root folder and please execute:  
`./gradlew clean build`  
This will run the application tests and build it.

Then, with **Docker** installed, run:  
`docker build -t authorizer .`
This creates a Docker Container Image based on the project's Dockerfile.

So, it's time to run that image. Now execute:  
`docker run -i -t --name authorizer authorizer`  
And _voilà_! The application is up and running!

## Input examples

### Account Creation
`{"account":{"active-card":true,"available-limit":100}}`

### Transaction Authorization
`{"transaction":{"merchant":"Restaurant at the End of the Universe","amount":42,"time":"2012-12-21T12:21:00.000Z"}}`