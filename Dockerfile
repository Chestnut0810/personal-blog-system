# 第一阶段：使用 Maven 编译打包代码
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# 执行打包命令，跳过测试以加快速度
RUN mvn clean package -DskipTests

# 第二阶段：使用轻量级 JDK 运行环境
FROM openjdk:17-jdk-slim
WORKDIR /app
# 将第一阶段打好的 jar 包复制过来
COPY --from=build /app/target/*.jar app.jar
# 暴露 Spring Boot 默认端口 8080
EXPOSE 8080
# 启动项目
ENTRYPOINT ["java","-jar","app.jar"]
