import com.google.protobuf.gradle.protoc

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.google.protobuf") version "0.8.18"
}

group = "com.sunfinance.group"
version = "0.0.1-SNAPSHOT"

dependencies {
    api("com.google.protobuf:protobuf-kotlin:3.19.4")
}

// this makes it so IntelliJ picks up the sources but then ktlint complains
sourceSets {
    val main by getting { }
    main.java.srcDirs("build/generated/source/proto/main/java")
    main.java.srcDirs("build/generated/source/proto/main/grpc")
    main.java.srcDirs("build/generated/source/proto/main/kotlin")
    main.java.srcDirs("build/generated/source/proto/main/grpckt")
}

protobuf {
    protobuf.protoc {
        artifact = "com.google.protobuf:protoc:3.19.2"
    }
}
