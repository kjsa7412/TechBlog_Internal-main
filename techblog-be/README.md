## 1. Local Tomcat Server Setting
VM System parametor에 "spring.profiles.active"를 추가해야 한다

- [InteliJ]: VM Optin => -Dspring.profiles.active=local 추가
- [Eclipse]: VM Arguments => -Dspring.profiles.active="local" 추가

## 2. Project Config
- SpringBoot, Maven, Jar
- Spring Security
- JPA
- JWT
- OPEN JDK 11