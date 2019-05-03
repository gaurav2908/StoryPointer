mvn clean install
java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar ./target/baseapp-1.0.1.jar