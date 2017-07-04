# Web Engineering 2015

This repository contains samples used during the [Business Informatic Group's](http://www.big.tuwien.ac.at) Web Engineering lecture series 2015 at [Vienna University of Technology](http://www.tuwien.ac.at).

# Web Services talk

In the Web Services talk we have introduced SOAP/WSDL Web Services and RESTful Web Services. The following two projects provide simple Java projects for SOAP/WSDL and RESTful Web Services.

## we-webservice

Contains a simple SOAP/WSDL Arithmetic Service. Simply launch the application's main class `at.ac.tuwien.big.we.webservice.ArithmeticService`and access the WSDL under `http://localhost:8080/arithmeticservice?wsdl`

Use [SOAP-UI](http://www.soapui.org) in order to send sample requests to the Web Service.

## we-restful-service

Contains a simple RESTful Web Service for maintaining student data. For each student the registration number (aka 'Matrikelnummer') and the student's name is stored.

Launch the application inside a servlet container (e.g., [Apache Tomcat](http://tomcat.apache.org) or [Jetty](http://eclipse.org/jetty/)) and access the Web Service under `http://localhost:8080/we-restful-service/rest/students` (note that the URL may change, depending on which context and port you have set in your servlet container)

Use [SOAP-UI](http://www.soapui.org) or a simple HTTP-Client such as [Cocoa](https://github.com/mmattozzi/cocoa-rest-client) in order to test the Web Service. One may also leverage command line's `curl` in order to send HTTP requests, as outlined below.

### Get all students

`curl -i http://localhost:8080/we-restful-service/rest/students`

### Get student with registration number 1

`curl -i http://localhost:8080/we-restful-service/rest/students/1`

Try content negotiation and retrieve the data of student 1 in JSON instead of XML. Set HTTP `Accept` header to `application/json`.

`curl -i --header "Accept:application/json" http://localhost:8080/we-restful-service/rest/students/1`

### Add a student

Prepare a file called `newStudent.xml` with the following data

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<student>
    <name>Greg Focker</name>
    <registerNumber>4</registerNumber>
</student>
```

Push the new student to the RESTful service using

`curl -i -X POST -d @newStudent.xml http://localhost:8080/we-restful-service/rest/students --header "Content-Type:application/xml"`

Execute the command a second time in order to retrieve a HTTP 409 Conflict, since the student has already been added.

Check if everything has been added correctly using the GET again

`curl -i http://localhost:8080/we-restful-service/rest/students`

### Update a student

Rename the previously added Greg Focker with his to-be wife Pamela M. Focker. Prepare a file called `updateStudent.xml` with the following data

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<students>
    <student>
        <name>Pamela M. Focker</name>
        <registerNumber>4</registerNumber>
    </student>
</students>
```

Push the data to the RESTful service using

`curl -i -X PUT -d @updateStudent.xml http://localhost:8080/we-restful-service/rest/students --header "Content-Type:application/xml"`

Check if everything has been updated correctly using the GET again

`curl -i http://localhost:8080/we-restful-service/rest/students`

###D elete a student

Delete the previously added student nr. 4 using

`curl -i -X DELETE http://localhost:8080/we-restful-service/rest/students/4`

Check if the student has been deleted correctly using the GET again

`curl -i http://localhost:8080/we-restful-service/rest/students`

### Delete all students

Delete all student records using

`curl -i -X DELETE http://localhost:8080/we-restful-service/rest/students`


# JPA/Hibernate talk

In the JPA/Hibernate talk we have introduced the basics of Object Relation Mapping (ORM) and the implementation of JPA and Hibernate.

## play-jpa-sample

An example [Play application](http://www.playframework.com) using JPA and Hibernate. The application does not contain any UI logic, but concentrates on how JPA relationships are correctly modeled. A general introduction to JPA and Hibernate is given on the following slide deck: [http://www.slideshare.net/ecosio/introduction-to-jpa-and-hibernate-using](http://www.slideshare.net/ecosio/introduction-to-jpa-and-hibernate-using)

The following DB structure is used for the samples:

![Sample ER diagram](https://github.com/pliegl/we2014/blob/master/img/Sample_ER_Diagram.jpg?raw=true "Sample ER diagram")


