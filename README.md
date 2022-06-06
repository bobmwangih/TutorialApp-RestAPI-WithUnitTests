# TutorialApp-RestAPI-WithUnitTests

This REPO holds a REST backend service powered by SpringBoot and H2 database,
<br/>
Also the services has the following unit tests - : 
<br/>
- RestController unit test (@WebMvcTest)
<br/>
- JpaRepository unit test (@DataJpaTest)

## Tech Stack:
SpringBoot | H2 database | spring JPA | Mockito | Junit5

## The application exports the following APIs
```java
POST	/api/tutorials	create new Tutorial
GET	/api/tutorials	retrieve all Tutorials
GET	/api/tutorials/:id	retrieve a Tutorial by :id
PUT	/api/tutorials/:id	update a Tutorial by :id
DELETE	/api/tutorials/:id	delete a Tutorial by :id
DELETE	/api/tutorials	delete all Tutorials
GET	/api/tutorials?title=[keyword]	find all Tutorials which title contains keyword

```

[![Site preview](/public/rename.png)](https://github.com/bobmwangih/TutorialApp-RestAPI-WithUnitTests)

<br/>

[![Site preview](/public/repoTest.png)](https://github.com/bobmwangih/TutorialApp-RestAPI-WithUnitTests)
