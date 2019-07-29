jar=`pwd`/target/minio-0.0.1-SNAPSHOT.jar
docker_jar=`pwd`/build/minio-0.0.1-SNAPSHOT.jar

if [[ -e $docker_jar ]]
then
    echo "jar is exist"
    rm -f $docker_jar
    echo "delete the old jar"
fi

cp $jar `pwd`/build

cd `pwd`/build && docker build -t minio:v1 .