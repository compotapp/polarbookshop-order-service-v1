# Команда для сборки
# docker build -f docker/Dockerfile -t goppot/k8s-middle:3.0.0 .
# docker push goppot/k8s-middle:3.0.0

# Используем официальный образ OpenJDK 17
FROM eclipse-temurin:17-jdk-jammy as builder

# Рабочая директория внутри контейнера
WORKDIR /app

# Копируем файлы для сборки
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Собираем приложение (создаём JAR)
RUN ./mvnw clean package -DskipTests

# Как будет выглядеть файловая система в контейнере
#/app
#├── .mvn/                # Папка с Maven Wrapper (исполняемые файлы)
#│   └── wrapper/
#│       ├── maven-wrapper.jar
#│       └── maven-wrapper.properties
#├── mvnw                 # Скрипт Maven Wrapper (для Linux)
#├── pom.xml              # Файл зависимостей Maven
#├── src/                 # Исходный код приложения
#│   ├── main/
#│   │   ├── java/        # Java-классы
#│   │   └── resources/   # application.properties, static/ и т.д.
#│   └── test/            # Тесты (если не пропущены)
#└── target/              # Папка с результатами сборки (после RUN ./mvnw package)
#    ├── classes/
#    ├── generated-sources/
#    └── your-app.jar     # Собранный JAR-файл (имя зависит от pom.xml)

# Второй этап - создаём минимальный образ для запуска
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Копируем собранный JAR из первого этапа
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт, который использует приложение
EXPOSE 9001

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]

# Как будет выглядеть файловая система в контейнере
#/app
#└── app.jar              # JAR-файл, скопированный из /app/target/ этапа builder

#docker build -f docker/Dockerfile -t pbs-order-service:1.0.0 .
#docker build -f docker/Dockerfile -t goppot/pbs-order-service:1.0.0 .
#docker tag pbs-order-service:1.0.0 goppot/pbs-config-order:1.0.0
#docker push goppot/pbs-order-service:1.0.0