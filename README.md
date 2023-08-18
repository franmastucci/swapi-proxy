<br>

# [swapi-proxy](https://github.com/franmastucci/swapi-proxy)
#### Serves as an intermediary layer for interacting with the SWAPI (Star Wars API), providing a simplified and unified interface to access Star Wars-related data.
#### Author: [Francisco Mastucci Silva](https://www.linkedin.com/in/franmastucci/). 
<br>

### API Quickstart
#### To run tests in a local environment:

```bash
mvn clean install
docker build -t swapi-proxy .
docker run -p 8080:8080 --name trileuco-swapi-proxy swapi-proxy
```

### Swagger UI and Testing
Once you've executed the necessary commands and have the project up and running, you can perform tests using the [Swagger UI](http://localhost:8080/swapi-proxy/swagger-ui/index.html#/).
#### API Endpoints
| Path                                                                                                           | Type  | Description                                                 |
|:---------------------------------------------------------------------------------------------------------------|:------|:------------------------------------------------------------|
| /swapi-proxy/init                                                                                              |  GET  | Initializes data loading of films                           |
| /swapi-proxy/person-info?name={person_name}                                                                    |  GET  | Retrieves information about a person by their name.         |


#### Invoking the init service:
```curl
curl --location 'http://localhost:8080/swapi-proxy/init'
```

#### Invoking the person information service:
```curl
curl --location 'http://localhost:8080/swapi-proxy/person-info?name=Luke%20SkyWalker'
```

### Technology
* **Platform:** Java 17
* **Project type:** Microservice
* **Spring Boot version:** 3.1.2
* **Open Api version:** 3.0.1
<br><br>
