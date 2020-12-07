# Spring Boilerplate
## Boilerplate code for springboot. It contains user management via rest api and connects to a postgresql database.

### Prerequisites to run
If you want to start the server alone then first you have to add in the `resources` directory a file called `application.properties`.
Template for that file:
```
app.security.origins = localhost
app.security.max_age = 3600L
app.security.allowed_methods = ['GET', 'POST']
```
---
### Done with:
* check pom.xml for maven dependencies

### License

Copyright (c) 2020 Adam Balski

Licensed under the MIT license.
