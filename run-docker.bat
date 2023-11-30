
@echo off

REM First command
echo pull image
docker pull cksiow/cognixus_to_do_list_assessment:1.0 && (
    REM Second command
    echo Running docker...
    docker run -e JASYPT_PASSWORD=%1 -p 8888:8888 cksiow/cognixus_to_do_list_assessment:1.0
)