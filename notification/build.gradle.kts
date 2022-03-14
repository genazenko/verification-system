plugins {
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
    id("org.hidetake.swagger.generator") version "2.19.2"
}

group = "com.sunfinance.group"
version = "0.0.1-SNAPSHOT"

val inputSwaggerDir = "$projectDir/src/main/resources/swagger"
val outputSwaggerDir = "$buildDir/generated-sources/swagger"

dependencies {
    implementation(project(":common-lib"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    implementation("org.postgresql:postgresql")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    swaggerCodegen("org.openapitools:openapi-generator-cli:5.4.0")
    implementation("org.openapitools:jackson-databind-nullable:0.2.2")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

swaggerSources {
    register("template") {
        setInputFile(file("$inputSwaggerDir/template-swagger.json"))

        code(delegateClosureOf<org.hidetake.gradle.swagger.generator.GenerateSwaggerCode> {
            language = "spring"
            components = listOf("models")
            additionalProperties = mapOf(
                "modelPackage" to "com.sunfinance.group.swagger.template",
                "dateLibrary" to "java8"
            )
            outputDir = file("$outputSwaggerDir/swagger-template")
        })
    }
    register("gotify") {
        setInputFile(file("$inputSwaggerDir/gotify-swagger.json"))

        code(delegateClosureOf<org.hidetake.gradle.swagger.generator.GenerateSwaggerCode> {
            language = "spring"
            components = listOf("models")
            additionalProperties = mapOf(
                "modelPackage" to "com.sunfinance.group.swagger.gotify",
                "dateLibrary" to "java8"
            )
            outputDir = file("$outputSwaggerDir/swagger-gotify")
        })
    }
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDirs("$outputSwaggerDir/swagger-template/src/main/java")
        java.srcDirs("$outputSwaggerDir/swagger-gotify/src/main/java")
    }
}

tasks {
    compileKotlin {
        dependsOn("generateSwaggerCode")
    }
}
