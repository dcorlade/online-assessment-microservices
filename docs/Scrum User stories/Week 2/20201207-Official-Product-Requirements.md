## Description

The Department of Software Technology at TU Delft wants to provide as part of its strategy to improve students' performance in some difficult courses, an online self-assessment tool for students (OSATool). This tool will allow students to test their knowledge from multiple topics in a specific course. In this way, students are aware of which subjects need further preparation/self-study before an examination. The content of the assessment is aligned with the course syllabus. Furthermore, lecturers take special care of the validity of the questions and answers.

## Technical Specifications

The system should be built in such a way that it can be extended with extra functionalities later (modular) and allow for easy integration with other systems (API). Individual components of the system need to be scalable so that when the demand for that service increases, the service does not get overloaded (microservices). The system should be written in the Java programming language (version 11). The system should not have a graphical user interface (GUI), but instead, all interactions with the systems are handled through the APIs. The system must be built with Spring Boot (Spring framework) and Gradle. 

## Security Specifications

All users of the system need to authenticate themselves to determine who they are and what they can do on the platform. Users need to have a NetID associated with their account to identify them across the systems of the TU Delft universally. This NetID, together with a password, serves as the credentials for the account. The password needs to be stored safely. For simplicity, the system will not be connected to the existing TU Delft authentication system (Single-Sign-On). The NetID is just a unique string used to identify each user. Security is a difficult aspect of the program to get right; therefore, we strongly advise to use Spring Security.

## Domain Specifications  

The online assessment tool should have one assessment per course. An assessment can have many different topics. Topics can, for example, be architectural patterns, design patterns, and development models for the course SEM (Software Engineering Methods). Each topic has questions and their associated answers. To make use of the online assessment tool, all users have to be logged in. Assessments can only be created and changed by teachers. Students can only take an assessment from the courses they are enrolled in. Each student can take an assessment for the same topic a maximum of 3 times.

An assessment should have a course name and a time of creation. Topics should have a topic name. Questions should have a title (indicating the subject of the question) and a body (containing the actual question). Answers can only be multi-choice.

Each assessment should have a repository of questions and their associated answers. Teachers can add, update, or remove questions to/from this repository. Each assessment has a limit of 10 questions and can only last 20 minutes. During the 20 minutes after starting the assessment, students are allowed to change their answers.

When a student starts an assessment, they get a random selection of questions from the repository of that assessment. However, every assessment should have all topics represented. For every question,  students should select at least one answer from each multiple-choice question.

At the end of the assessment, the student should get a grade that is automatically calculated based on which answer was marked as correct by the teacher. The grade should be calculated according to the following formula (CORRECT QUESTIONS / NUMBER OF QUESTIONS * 9 + 1).

Teachers should have an overview of how many students did the assessment, what their grade is, the two worst made questions, and what the average grade is. Students should be able to see their grade for each assessment, their answers for each assessment, and the average grade of the assessment.

All users of the system need to authenticate themselves to determine who they are and what they can do on the platform. Users need to have a NetID associated with their account to identify them across the systems of the TU Delft universally. This NetID, together with a password, serves as the credentials for the account. The password needs to be stored safely. For simplicity, the system will not be connected to the existing TU Delft authentication system (Single-Sign-On). The NetID is just a unique string used to identify each user. Security is a difficult aspect of the program to get right; therefore, we strongly advise to use Spring Security.

## Final Remarks

Your task is to model and develop the system based on the above summary of the client. Extract the functionality and constraints from the summary and write those down as requirements. Your TA will check these requirements to make sure you are on the right track. If you find requirements that are incomplete or require further information, please use the Brightspace discussion forum to ask your question to the client.