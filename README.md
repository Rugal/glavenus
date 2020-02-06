# Rugal Spring Boot Skeleton Project

## Code Status
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![CircleCI](https://circleci.com/gh/Rugal/spring-boot-template.svg?style=svg)](https://circleci.com/gh/Rugal/spring-boot-template)
[![codecov](https://codecov.io/gh/Rugal/spring-boot-template/branch/master/graph/badge.svg)](https://codecov.io/gh/Rugal/spring-boot-template)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0b88e1b2aa394d80a3e321154844b07d)](https://www.codacy.com/app/ryujinwrath/spring-boot-template?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Rugal/spring-boot-template&amp;utm_campaign=Badge_Grade)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FRugal%2Fglavenus.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FRugal%2Fglavenus?ref=badge_shield)

[![Heroku](http://heroku-badge.herokuapp.com/?app=spring-boot-developmen&svg=1&root=/actuator/info)](https://spring-boot-development.herokuapp.com/actuator/info)
[![Heroku](http://heroku-badge.herokuapp.com/?app=spring-boot-staging&svg=1&root=/actuator/info)](https://spring-boot-staging.herokuapp.com/actuator/info)


## Start Local Environment

### Database
Please install `docker` and `docker-compose`.  

At the root directory run the following command:  

```bash
docker-compose up
```

The default user is `postgres`, password is `123`


### Service

Please install `maven` and `JDK 11`.  

You should also initialize the database by flyway, if you haven't done so:

```bash
mvn flyway:migrate
```

At the root directory run the following command:  
```bash
mvn spring-boot:run
```

By default it will connect to the docker database.  


## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FRugal%2Fglavenus.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FRugal%2Fglavenus?ref=badge_large)