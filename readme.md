**example1** project contains the source code to trigger the rule in Kie Server. 
Use this to compile and run your application in vs. 

```
mvn compile exec:java 
```

Use this to build the fat jar with dependenciesi in vs.

```sh
mvn clean compile assembly:single
```

Use this to run application. 
```
java -cp ./target/example1-1.0-SNAPSHOT-jar-with-dependencies.jar com.myspace.example1.DMNApp
```

**dm-project** folder contains the decision manager projects for accumulateRule and DMN.  

