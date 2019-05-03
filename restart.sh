cd ./src/main/script/
npm run build
cd ../../..
pkill -9 -f tomcat
rm -rf /opt/apache-tomcat-8.5.4/webapps/ROOT
mkdir /opt/apache-tomcat-8.5.4/webapps/ROOT
mvn clean install
cp target/baseapp-0.0.1-SNAPSHOT.war /opt/apache-tomcat-8.5.4/webapps/ROOT
cd /opt/apache-tomcat-8.5.4/webapps/ROOT
jar xvf baseapp-0.0.1-SNAPSHOT.war
cd ../../bin
export CATALINA_OPTS="-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"
sh ./catalina.sh start