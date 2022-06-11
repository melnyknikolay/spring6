val springBootVersion = "3.0.0-SNAPSHOT"

plugins {
    id("org.springframework.boot") version "3.0.0-SNAPSHOT"
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

