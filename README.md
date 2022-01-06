# Quickstart in Couchbase with Java and Spring Boot
#### Build a REST API with Couchbase's Java SDK 3 and Spring Boot

> This repo is designed to teach you how to connect to a Couchbase cluster to create, read, update, and delete documents and how to write simple parametrized N1QL queries.

[![Try it now!](https://da-demo-images.s3.amazonaws.com/runItNow_outline.png?couchbase-example=java-springboot-quickstart-repo&source=github)](https://gitpod.io/#https://github.com/couchbase-examples/java-springboot-quickstart)

Full documentation can be found on the [Couchbase Developer Portal](https://developer.couchbase.com/tutorial-quickstart-java-springboot/).
## Prerequisites
To run this prebuilt project, you will need:

- Couchbase Server 7 Installed (version 7.0.0-5247 or higher)
- Java SDK v1.8 or higher installed 
- Code Editor installed (IntelliJ IDEA, Eclipse, or Visual Studio Code)
- Maven command line

## Install Dependencies 


```sh
mvn package
```
> Note: Maven packages auto restore when building the project in IntelliJ IDEA or Eclipse depending on IDE configuration.

### Database Server Configuration

All configuration for communication with the database is stored in the `/src/main/resources/application.properties` file.  This includes the connection string, username, and password.  The default username is assumed to be `Administrator` and the default password is assumed to be `password`.  If these are different in your environment you will need to change them before running the application.

### Dependency Injection via DBSetupRunner class 

The quickstart code provides a CommandLineRunner called DBSetupRunner in the runners folder that wires up the Bucket and Cluster objects for dependency injection.  This runner also creates the bucket, collection, scope, and indexes for the tutorial to run properly automatically when the application 

## Running The Application

At this point the application is ready and you can run it via your IDE or from the terminal:

```sh
mvn spring-boot:run -e -X
```

You can launch your browser and go to the [Swagger start page](https://localhost:8080/swagger-ui/index.html).

## Running The Tests

To run the standard integration tests (which requires a running Couchbase Server), use the following command:

```sh
mvn verify
```

## Project Setup Notes

This project was based on the standard [Spring Boot project](https://spring.io/guides/gs/rest-service/).  The HealthCheckController is provided as a sanity check and is used in integration tests.  

A full list of packages are referenced in the pom.xml file

## Conclusion

Setting up a basic REST API in Spring Boot with Couchbase is fairly simple.  This project when run with Couchbase Server 7 installed creates a bucket in Couchbase, an index for our parameterized [N1QL query](https://docs.couchbase.com/java-sdk/current/howtos/n1ql-queries-with-sdk.html), and showcases basic CRUD operations needed in most applications.
