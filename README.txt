This README contains a description of the application, how to run it, how to set up the development environment, and who worked on it. 

# Description of project
The project entails an online assessment application to be used by students and teachers of the TU Delft. With it, teachers will be able to add and manage 
multiple choice exams consisting of 10 questions and a time limit of 20 minutes. Students can take these exams and receive a grade. Students will be able to retrieve 
their own exams with grades after they took them. Teachers will be able to get aggregated information about how the exam was made by all students.
All users will have to authenticate themselves before using the application. 

The project uses a microservices architecture and only contains server-side logic. Client side logic, with for example a graphical user interface, has yet to be
constructed.

# Development team
| Name           	| Email                         	|
| ------------------| ----------------------------------|
| Denis Corlade		| D.Corlade@student.tudelft.nl		|
| Ben Carp			| B.J.Carp@student.tudelft.nl		|
| Daniël Poolman	| D.C.Poolman@student.tudelft.nl	|
| Auke Sonneveld	| A.C.Sonneveld@student.tudelft.nl	|
| Andrejs Karklins	| A.Karklins@student.tudelft.nl		|
| Justin Rademaker	| j.a.rademaker@student.tuddelft.nl	|


# How to run it
The project can be cloned from this link: https://gitlab.ewi.tudelft.nl/cse2115/2020-2010/4-online-assessment/op15-sem29.
For every of the 6 microservices, there is one file called 'Application'. These should all run for the application to have full functionality.
These are the paths to the files:

## Start of path for all
op15-sem29\services\

## Authentication
authentication\src\main\java\app\Application

## Authorisation
authorization\src\main\java\app\Application

## Course-service
course-service\src\main\java\app\Application

## Exam-service
exam-service\src\main\java\app\Application

## Student-service
student-service\src\main\java\app\Application

## Teacher-service
teacher-service\src\main\java\app

Every microservice has its own build.gradle file. There is one build.gradle file directly under the root of the project which can be used to open the project 
with an IDE.

# Database
The microservices make use of two databases. For tests they make use of an in-memory H2 database. For the application itself a MySQL database on the Amazon Web
Services is used. The following urls are used to connect to the database. They can be used for example in a Graphical User Interface like DataGrip:
- jdbc:mysql://op15-sem29.cwkhqhkkl85r.eu-central-1.rds.amazonaws.com:3307/course_service
- jdbc:mysql://op15-sem29.cwkhqhkkl85r.eu-central-1.rds.amazonaws.com:3307/authorization
- jdbc:mysql://op15-sem29.cwkhqhkkl85r.eu-central-1.rds.amazonaws.com:3307/exam_service

# How to use the application
Due to the lack of a client-side using the application is not yet intuitive. The user has to send HTTP-Requests to the different endpoints in every Microservice.
We recommend an application like Postman to simplify this process. 

The user should first send a request to the Authentication microservice to register. Then again to login. After logging in, the response contains a special token
with the key: 'session'. The user should send this same key-value pair combination within the header with every subsequent request to a microservice.
The microservices 'student-service' and 'teacher-service' may be used as user interfaces for all possible requests after authentication.

For easy reference, the ports corresponding to every microservice are:
- Authentication:	8080
- Authorisation:	8081
- Course-service:	8082
- Exam-service:		8083
- Student-service:	8084
- Teacher-service:	8085

## Examples on how to use the application
We provide an example for registering and logging in. A 0-value for role means student, and a 1-value means teacher.

### Register
REQUEST-TYPE: POST
HEADER: Key: Content-Type, Value: application/json
URL: http://localhost:8080/authentication/sign-up
BODY:
{
    "netId": "TUDelftStudent",
    "password": "I_Like_Learning",
    "role": "0",
    "extraTime": "0"
}

### Login
REQUEST-TYPE: POST
HEADER: Key: Content-Type, Value: application/json
URL: http://localhost:8080/login
BODY:
{
    "netId": "TUDelftStudent",
    "password": "I_Like_Learning"
}

#Thanks and acknowledgements 
We would like to give special thanks to our teaching assistants Ilya Grishkov (week 1) and Andrei Geadau (week 2 up until and including week 5).
Both have been very supportive throughout the process and always showed an optimistic and motivating attitude. Thank you for coaching us!
