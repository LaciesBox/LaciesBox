**GUIDE**

Server side (Backend): `Java`

Client side (Frontend): `Vue`

**What does the program do?**

`This is a demo on how Vue can connect to a Spring server.`

`Basically, what happens is that Vue makes an AJAX call to ask the server (Spring)
to perform a CRUD (Create, Read, Update, Delete) operation.`

`After the call is made, Vue will handle the collected data or in case of call failure, the errors.`

`By handling I mean it'll respectively updated information on the screen. Voila!`

**Setup**
1. Install Maven 
    
    https://maven.apache.org/install.html
    
    (if Windows) https://docs.wso2.com/display/IS323/Installing+Apache+Maven+on+Windows

**How to run the program**
 
1. Open terminal (if on Linux) / Git Bash (if on Windows) on project's root directory.

2. Run "sh run-dev.sh" command to run Spring Boot application.

3. Run "cd client" command to go to client directory

4. Run "npm install" to install related node modules.

5. Run "npm run serve" command to run Vue application.
