# Spring Boot Boilerplate
## Template for spring boot applications. It includes user management via a rest api and connects to a postgresql database and uses JWTs for auth.

### Prerequisites to run
If you want to start the application, then first you have to add a file called `application.properties` to `src/main/java/resources`.
You can use the template below.
#### Template for the file:
```
# CORS configuration
app.security.cors.origins = localhost
app.security.cors.max_age = 3600
app.security.cors.allowed_methods = 'GET', 'POST'

# Logging
logging.file.path=logger
logging.file.name=logger/logfile

# JPA
spring.datasource.url = $URL
spring.datasource.username = $DB_USERNAME
spring.datasource.password = $DB_PASSWORD
spring.datasource.hostname = $DB_HOSTNAME
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQL10Dialect
spring.jpa.properties.hibernate.hbm2ddl.auto = validate
```
---

### Done with: (for more info check `pom.xml`)
* jdk 15 (with preview features turned on)
* maven
* spring boot
* postgresql
* jackson based jjwt
* h2
* mockito
* junit
* slf4j

### License

Copyright (c) 2020 Adam Balski

Licensed under the MIT license.
