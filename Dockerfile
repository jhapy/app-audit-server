FROM jhapy/base-image-slim:1.16

ENV JAVA_OPTS=""
ENV APP_OPTS=""

ADD target/app-audit-server.jar /app/

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Xverify:none -Djava.security.egd=file:/dev/./urandom -Dpinpoint.agentId=$(date | md5sum | head -c 24) -jar /app/app-audit-server.jar $APP_OPTS"]

HEALTHCHECK --interval=30s --timeout=30s --retries=10 CMD curl -f http://localhost:9004/management/health || exit 1

EXPOSE 9004