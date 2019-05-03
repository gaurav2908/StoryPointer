cd /opt/apache-tomcat-8.5.4/bin
export CATALINA_OPTS="-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"
sh ./catalina.sh start