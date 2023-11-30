# Cognixus To-Do List Assessment

## Project Overview

This repository contains the Cognixus To-Do List Assessment application. Follow the steps below to build, run, and test
the application.

## Build and Integration Test

To build and verify the integration tests, execute the following Maven command:

```
mvn clean install -Djasypt.encryptor.password={jasypt.encryptor.password}
```

## Running the Application

### Choose one of the following options to run the application:

### Option A: Using Docker Run

Run the following command to start the application with Docker:

```
docker-run.bat {jasypt.encryptor.password}
```

### Option B: Using Docker Compose

For future multiple service purposes, use the following command:

```
docker-compose-up.bat {jasypt.encryptor.password}
```

### DevOps Purpose

Use the following command for DevOps purposes (upload latest docker image to docker hub):

```
docker-upload.bat
```

## Accessing the Application

After running the application, open the following URL in your browser:
http://localhost:8888/login
Login with Google to exchange the JWT token for authorization.
![Login page](http://ip.klse2u.veryfast.biz.user.fm/cognixus_to_do_list_assessment_google_login.png)

## Using JWT Token in Swagger

Copy the token obtained from the Google login and use it in the application's Swagger UI at:
http://localhost:8888/swagger-ui/index.html
Replace 'xxxx' in the Swagger UI with the copied token.
This is also consider as interface documentation
![JWT token](http://ip.klse2u.veryfast.biz.user.fm/cognixus_to_do_list_assessment_jwt_token.png)

### Testing with POSTMAN

Due to JavaScript truncation (Long datatype), it's advisable to use POSTMAN for testing to ensure accurate data,
especially when working with Snowflake-generated IDs.

## Checking Database Data

Access the H2 console to check the database data at:
http://localhost:8888/h2-console/login.jsp
Change JDBC URL to jdbc:h2:mem:todolist and login.
since this is for assement purpose we are using H2 in memory database, so the database data will be erased once the
application restarted

![H2 console](http://ip.klse2u.veryfast.biz.user.fm/cognixus_to_do_list_assessment_h2_login.png)

**Note:** The jasypt.encryptor.password will be provided in the email for enhanced security practices.
