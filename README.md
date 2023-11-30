# cognixus_to_do_list_assessment
Cognixus TODO list assessment
1. By build and verify integration test the application, run below mvn command
   - mvn clean install -Djasypt.encryptor.password={jasypt.encryptor.password}
2. By running the application, user can either using option a or b, b is ready for future multiple service purpose
   a. docker-run.bat {jasypt.encryptor.password}
   b. docker-compose-up.bat {jasypt.encryptor.password}
3. After ran the application, you can open url http://localhost:8888/login and login with Google so can exchange the JWT token and use to authorization
   ![Login page](http://ip.klse2u.veryfast.biz.user.fm/cognixus_to_do_list_assessment_google_login.png)



















***jasypt.encryptor.password will provided in the email for better security praticate 
