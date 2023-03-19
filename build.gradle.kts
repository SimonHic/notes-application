import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "ie.setu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Dependencies for Logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("org.slf4j:slf4j-simple:2.0.5")
    // Dependencies for Persistence
    // For Streaming to XML and JSON
    implementation("com.thoughtworks.xstream:xstream:1.4.20")
    implementation("org.codehaus.jettison:jettison:1.5.4")
    //Color for the UX
    implementation("org.fusesource.jansi:jansi:2.4.0")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}