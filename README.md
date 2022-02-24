

## code path:        
    ./java/springboot
##  run command:   
    mvn spring-boot:run
## swagger-ui
   http://localhost:8083/swagger-ui.html

##  test command:   
   mvn clean test org.jacoco:jacoco-maven-plugin:0.8.3:prepare-agent install -Dmaven.test.failure.ignore=true

##  test report :      
 ./java/springboot/target/site/jacoco/index.html
