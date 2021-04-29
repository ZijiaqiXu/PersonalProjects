FROM payara/micro

COPY --chown=payara:payara activemq-rar-5.16.1.rar ${PAYARA_DIR}/activemq-rar.rar

COPY --chown=payara:payara clinic-rest.war ${PAYARA_DIR}

COPY --chown=payara:payara postgresql-42.2.19.jar ${PAYARA_DIR}/postgresql.jar

COPY --chown=payara:payara config.properties ${PAYARA_DIR}

ENTRYPOINT ["java", "-jar", "/opt/payara/payara-micro.jar", "--contextRoot", "clinic-rest", "--addLibs", "/opt/payara/postgresql.jar", "--systemProperties", "/opt/payara/config.properties", "/opt/payara/activemq-rar.rar", "/opt/payara/clinic-rest.war"]

