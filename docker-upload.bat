@echo off

REM First command
echo Running mvn clean install...
mvn clean install -DskipTests && (
    REM Second command
    echo Running docker build...
    docker build -t cksiow/cognixus_to_do_list_assessment:1.0 . && (
        REM Third command
        echo Running docker push...
        docker push cksiow/cognixus_to_do_list_assessment:1.0
    )
)

REM Rest of the script
echo All commands executed.
pause