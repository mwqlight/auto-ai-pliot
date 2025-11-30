# 多阶段构建：构建阶段
FROM maven:3.8.6-openjdk-17-slim AS builder

# 设置工作目录
WORKDIR /app

# 复制pom.xml文件
COPY pom.xml .

# 下载依赖（利用Docker缓存）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-jdk-slim

# 安装必要的工具
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 创建应用用户
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 设置工作目录
WORKDIR /app

# 从构建阶段复制jar包
COPY --from=builder /app/target/*.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs && chown appuser:appuser /app/logs

# 切换用户
USER appuser

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM参数
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]