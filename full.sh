cd ./src/main/script/
npm run build
cd ../../..
mvn clean install
java -jar ./target/baseapp-0.0.1-SNAPSHOT.jar