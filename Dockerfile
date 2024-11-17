FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY app app
COPY model model

RUN sed -i 's/\r$//' mvnw
RUN ./mvnw install -DskipTests
RUN mkdir -p model/target/dependency && (cd model/target/dependency; jar -xf ../*.jar)
RUN cd ..
RUN mkdir -p app/target/dependency && (cd app/target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","org.prison.app.Application"]