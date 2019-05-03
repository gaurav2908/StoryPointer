cd ./src/main/script/
npm run build
cd ../../..
mvn clean install
java -jar ./target/baseapp-1.0.1.jar