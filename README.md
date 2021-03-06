# Medical Quality Informatics

###Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites & Local Development
* Java JDK
* Database:
    - https://www.postgresql.org/
    - A database named `mqi`
    - A user named `mqi` with the password `mqi`
    ```
    create database mqi;
    create user mqi with encrypted password 'mqi';
    alter user mqi with superuser;
    ```
* Application Server:
    - Install maven: https://maven.apache.org/
    - Navigate to the project root directory.
    - To run the application server: ```mvn spring-boot:run -Dspring-boot.run.profiles=local```
    - To run server unit tests: ```mvn test```
    - To run server contract tests: ```mvn test -Pcontract-tests```
* Client:
    - Install Node Package Manager: https://www.npmjs.com/ 
    = Navigate to the client directory.
    - To run the client: ```npm install```
    - To start the client: ```npm start```
    - To run client unit tests: ```npm test```
    - To run client contract tests: ```npm run contracts``` (application server will need to be running)

## Deployment
The application is currently being deployed to Heroku with no use of CI/CD
Use the ```build.sh``` or ```build.bat``` script then commit to git


###### Egia Software Solutions, Inc
