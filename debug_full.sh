cd ./src/main/script/
npm run build
cd ../../..
mvn clean install
java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar ./target/baseapp-0.0.1-SNAPSHOT.jar