to debug:
mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"


java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar baseapp-0.0.1-SNAPSHOT.jar


debug tomcat:
export CATALINA_OPTS="-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"
sh ./catalina.sh start

url:
http://localhost:5000

debug port: 8000