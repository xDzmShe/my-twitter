#MyTwitter Social Network
This REST API has been written in SpringBoot framework.<br>
Maven has been used as build tool.

###How to run the app
Navigate to root of the project. Then run following command from command line:

```./mvnw spring-boot:run```

The app will start at http://localhost:8080

###API documentation
[Swagger API](http://localhost:8080/swagger-ui.html)

The API can be tested directly from the swagger documentation.

####To post message
HTTP POST
URL: ```http://localhost:8080/<username>/message```<br>
RequestBody: ```{"text" : "<message with length from 1 to 140 characters>"}```

Note: user is created during posting first message

####To get all messages posted by a given user
HTTP GET
URL: ```http://localhost:8080/<username>/wall```

Note: Messages are sorted in reverse chronological order

####To get list of users followed by a given user
HTTP GET
URL: ```http://localhost:8080/<username>/following```

####To add a use to be followed by a specific user
HTTP POST
URL: ```http://localhost:8080/<username>/following```<br>
RequestBody: ```{"following: <name>}```

####To get all messages posted by following users
HTTP GET
URL: ```http://localhost:8080/<username>/timeline```

Note: Messages are sorted in reverse chronological order
