# Private Tracker Service

## Code Status

[![CircleCI](https://circleci.com/gh/Rugal/glavenus.svg?style=svg)](https://circleci.com/gh/Rugal/glavenus)
[![codecov](https://codecov.io/gh/Rugal/glavenus/branch/master/graph/badge.svg)](https://codecov.io/gh/Rugal/glavenus)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FRugal%2Fglavenus.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2FRugal%2Fglavenus?ref=badge_shield)

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
