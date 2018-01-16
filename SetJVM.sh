#!/bin/bash
projectFinalName=$1
limit_in_bytes=$(cat /sys/fs/cgroup/memory/memory.limit_in_bytes)

# If not default limit_in_bytes in cgroup
if [ "$limit_in_bytes" -ne "9223372036854771712" ]
then
    limit_in_megabytes=$(expr $limit_in_bytes \/ 1048576)
    heap_size=$(expr $limit_in_megabytes - 64)
    export JAVA_OPTS="-server -Xms300m -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=128M -Xmx${heap_size}m -XX:-UseCompressedClassPointers -Duser.timezone=Asia/Shanghai -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS"
    echo JAVA_OPTS=$JAVA_OPTS
    echo ${projectFinalName}
fi

ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

java $JAVA_OPTS -jar /${projectFinalName}.jar