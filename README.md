# Spring Boot Boilerplate
### Template for spring boot applications. It includes user management via a rest api, uses JWTs for auth(and refresh tokens stored in cookies).


## Running and testing
### Prerequisites
* docker with docker compose
* maven
* JDK >= 17
### Run the app
To create the image, create a `./docker-compose.yml` file. You can source a template with:
```bash
cp docker-compose.yml.template docker-compose.yml
```
Then, compile the web-app into a docker container.

```bash
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=spring-boot-boilerplate
```
Having done that, run `docker compose up` to start the application and the app will listen on port 8080.
### Test
```bash
mvn clean test
```

## Done with: (for more info check `pom.xml`)
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
