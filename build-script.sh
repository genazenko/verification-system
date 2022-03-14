if ./gradlew clean build; then
  cp notification/build/libs/notification-0.0.1-SNAPSHOT.jar docker/notification.jar
  cp template/build/libs/template-0.0.1-SNAPSHOT.jar docker/template.jar
  cp verification/build/libs/verification-0.0.1-SNAPSHOT.jar docker/verification.jar
  docker-compose --project-directory docker up -d --build
fi
