FROM registry.redhat.io/jboss-webserver-5/jws56-openjdk8-openshift-rhel8:5.6.1-6

ARG prometheus_agent=jmx_prometheus_javaagent-0.17.2.jar
ARG prometheus_base_url=https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.17.2

ADD --chown=185:185 ${prometheus_base_url}/${prometheus_agent} /opt/jws-5.6/tomcat/lib/jmx_prometheus_javaagent.jar
COPY jws-prometheus-opts /opt/jboss/container/prometheus/jws-prometheus-opts
COPY config.yaml /opt/jws-5.6/tomcat/conf

COPY target/*.war /opt/jws-5.6/tomcat/webapps
