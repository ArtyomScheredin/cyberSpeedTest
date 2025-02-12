plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.scheredin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.scheredin.Main"
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.mockito:mockito-inline:4.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}