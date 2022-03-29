FROM registry.redhat.io/jboss-webserver-5/jws56-openjdk8-openshift-rhel8:5.6.1-6

COPY db/driver/*.jar    /opt/jws-5.6/tomcat/lib
COPY target/*.war       /opt/jws-5.6/tomcat/webapps

