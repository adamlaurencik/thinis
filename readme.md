#ThinIs
ThinIs is an application for android cell phones.

It serves as client application for school webpage to acquire news, grades and substitutions.

Requires android v19.

This application was developed in gymdb.sk

##Project structure

1. Server
  * the purpose of server is to push notifications to client android application on event.
  * proxy trafic for login
2. Client
  * android client application with access to substitutions and evaluation in school.

------
##How to build the project
<b>Server - ThinIs-GCM-Server</b>
In order to run server you need to setup application at google cloud console (https://cloud.google.com/console). Create application and gain api key for android access to Google Cloud Messaging services. After that build and deploy application to your own apache tomcat instance.

<b>Client - ThinIs</b>
Use gradle to build the project. After build you will find android *.apk archive to be deployed in build directory. You can deploy the application to your own mobile phone or test it within AVD.
To run this application properly you need to run and set server information in client application. Go to /src/main/assets/application.properties and put the server IP address with web-app endpoint.

