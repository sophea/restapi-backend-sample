#!/bin/sh -l

JAVA_OPTS="-Dfile.encoding=UTF-8 -Duser.timezone=UTC -XX:NativeMemoryTracking=summary -XX:+HeapDumpOnOutOfMemoryError"
PORT="8443"

cd /srv/"$APP" || exit
#/opt/java/bin/java $JAVA_OPTS org.springframework.boot.loader.JarLauncher --bind 0.0.0.0:$PORT

/opt/java/bin/java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar application.jar
