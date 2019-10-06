#grpc-java-demo

A personal demonstration of using gRPC (grpc.io) as a proxy to a public
Azure Blob Store.

## IDE Setup    
This project was built using the IntelliJ IDEA. In order to get IntelliJ to play 
nicely with Gradle builds of gRPC, I had to make the following changes to the IDE
settings:
1. Select the IntelliJ main menue and dropdown and then "Preferences..."
2. Expand the  "Build Execution Deployment" Section
3. Expand the Gradle Section
4. Select Runner and uncheck "Delegate IDE build/run to Gradle" checkbox in the 
panel.

## Generating Code 
Using the project build.gradle file, there should now be a Gradle build task
called generateProto. In IntelliJ, this service is under Tasks -> other. Execute
that task to autogenerate all the client/server boilerplate code for the blob 
storage proxy service.