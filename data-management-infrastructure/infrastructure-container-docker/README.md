
# getstarted

Samples from https://docs.docker.com/get-started


create image:

    docker build -t friendlyhello .

view the images:

    $ docker image ls
    REPOSITORY                                               TAG                 IMAGE ID            CREATED             SIZE
    friendlyhello                                            latest              f8e7428d285a        6 seconds ago       151MB
    python                                                   2.7-slim            46ba956c5967        3 days ago          140MB

run & access the app:

    docker run -p 4000:80 friendlyhello
    http://localhost:4000/
    docker run -d -p 4000:80 friendlyhello

view the containers:

    $ docker container ls
    CONTAINER ID        IMAGE               COMMAND             CREATED              STATUS              PORTS                  NAMES
    f11c79f8ef8e        friendlyhello       "python app.py"     About a minute ago   Up About a minute   0.0.0.0:4000->80/tcp   gallant_leavitt

stop container:

    docker container stop f11c79f8ef8e

tag & publish the image:

    docker tag <image> <username>/<repository>:<tag>
    docker push <username>/<repository>:<tag>

pull & run:

    docker run -p 4000:80 <username>/<repository>:<tag>


# MySQL

REF：
http://www.runoob.com/docker/docker-install-mysql.html
https://stackoverflow.com/questions/49194719/authentication-plugin-caching-sha2-password-cannot-be-loaded
    docker run -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql --default-authentication-plugin=mysql_native_password



    # https://github.com/docker-library/mysql/blob/master/5.7/Dockerfile
    docker run -p 3306:3306 --name mysql5.7 -v $PWD/conf:/etc/mysql/conf.d -v $PWD/logs:/logs -v $PWD/data:/mysql_data -e MYSQL_ROOT_PASSWORD=root -d mysql --default-authentication-plugin=mysql_native_password


# Apache Kafka
TODO(zhoujiagen)
https://hub.docker.com/r/wurstmeister/kafka/
https://wurstmeister.github.io/kafka-docker/
