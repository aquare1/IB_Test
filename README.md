Instructions: 
Create a Spring Boot application that satisfies the following requirements. The project should push to GitHub (Or a 
similar repository).
• Database
o Create a database schema that represents the following:
§ 1-1: This relationship is shown by the PersonInfo entity.
§ 1-many: This relationship is shown by Address entity. So one person can have many 
addresses.
§ Many-many: This relationship is shown by Club entity. So same person can join many 
clubs.
o This example is based on a personInfo entity, but you can create your own example that satisfies 
the above relationship requirements
• REST Endpoints
o All endpoints are behind Authentication (Spring Security).
§ JWT for public user which has a scope of READ
§ Basic auth for admin user which has scopes of READ and WRITE
o POST -- /v1/post-person (Needs WRITE Access) for admin user only
§ Allows a person to be inserted into the H2 DB 
o GET -- /v1/get-person/{personId} (Needs either READ or WRITE Access) for admin or public use. 
§ Allows one person to be selected based on ID. We are only searching for a person based 
off ID at this time
§ Path Variable required
o DELETE -- /v1/delete-person/{personId} (Needs WRITE Access) for admin user only. 
§ Allows one person to be deleted based on ID. We are only deleting a person based off ID 
at this time
§ Path Variable required
• Testing
o Classes should have proper test coverage. 
• Swagger
o Swagger documentation for endpoints is preferred.

code path:         ./java/springboot
run command:   mvn spring-boot:run
test command:   mvn clean test org.jacoco:jacoco-maven-plugin:0.8.3:prepare-agent install -Dmaven.test.failure.ignore=true
test report :       ./java/springboot/target/site/jacoco/index.html
