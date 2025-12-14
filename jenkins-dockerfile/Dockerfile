# jenkins-dockerfile/Dockerfile
FROM jenkins/jenkins:lts-jdk21

USER root

# Устанавливаем Docker CLI внутрь образа
RUN apt-get update && \
    apt-get install -y docker.io && \
    rm -rf /var/lib/apt/lists/*

USER jenkins