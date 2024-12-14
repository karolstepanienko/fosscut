FROM ghcr.io/graalvm/native-image-community:21 AS build

RUN microdnf install findutils

COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
RUN ./gradlew wrapper
COPY src src

RUN ./gradlew nativeCompile


FROM alpine:3.21.0 as final

RUN apk add libc6-compat

COPY --from=build /app/build/native/nativeCompile/fosscut-api .

ENTRYPOINT /fosscut-api
