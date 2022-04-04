# Getting Started

This is a basic springboot project with that latest version

- springboot 2.6.x
- java11
- maven build


It can run JBOSS EAP / Tomcat / Jetty

```
deploy with tomcat :

mvn package && java -jar target/finet-api-0.0.5.war

deploy with JbossEAP :

mvn clean install
deploy the war file in target/finet-api-0.0.5.war in JBOSS EAP server


```

#### Test API

REST-DOC-API : 
http://localhost:8080/swagger-ui/index.html

### Health check and version api
```
curl http://localhost:8080/actuator/info

{"build":{"artifact":"finet-api","name":"finet-api","time":"2022-04-04T10:25:50.477Z","version":"0.0.5","group":"com.jtrust"}}


curl http://localhost:8080/actuator/health

{ status: "UP" }

```


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.4/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.4/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

