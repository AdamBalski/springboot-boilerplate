# Spring Boot Boilerplate
## Template for spring boot applications. It includes user management via a rest api and connects to a postgresql database and uses JWTs for auth.


### Running as a docker image
To create the image:
```bash
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=spring-boot-boilerplate
```
Then run `docker-compose up` to start the application and the app will listen on port 8081.

### Prerequisites to run separately
If you want to start the application without using docker, then first you have to set up a database and make appropriate changes to a file called `application.properties` in `src/main/java/resources`.

### Done with: (for more info check `pom.xml`)
* jdk 17 (with preview features turned on)
* maven
* spring boot
* jpa with hibernate
* postgresql
* jackson based jjwt
* test containers
* mockito
* junit5
* slf4j
* docker

### License

Copyright (c) 2021 Adam Balski

Licensed under the MIT license.
