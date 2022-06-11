plugins {
    java
    eclipse
}

allprojects {
    group = "it.discovery"
}

subprojects {
    apply(plugin = "java")

    java.sourceCompatibility = JavaVersion.VERSION_18
    java.targetCompatibility = JavaVersion.VERSION_18

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }

    dependencies {
        implementation("jakarta.annotation:jakarta.annotation-api:2.1.0")

        compileOnly("org.projectlombok:lombok:1.18.22")
        annotationProcessor("org.projectlombok:lombok:1.18.22")
    }
}

