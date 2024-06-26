@file:Suppress("UnstableApiUsage")

/*
* This file was generated by the Gradle 'init' task.
*
* This generated file contains a sample Java application project to get you started.
* For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.3/userguide/building_java_projects.html in the Gradle documentation.
* This project uses @Incubating APIs which are subject to change.
*/

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    java
    id("io.quarkus")

}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-qute")
    implementation("io.quarkus:quarkus-cache")

    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation ("io.quarkus:quarkus-resteasy-reactive")
    implementation( "io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation ("io.quarkus:quarkus-arc")
    implementation(libs.poi)
    implementation(libs.poi.ooxml)
    implementation(libs.log4j.core)
    implementation(libs.h2)
    implementation(libs.j2html)

    testImplementation(libs.hamcrest)
    testImplementation ("io.quarkus:quarkus-junit5")
    testImplementation ("io.rest-assured:rest-assured")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.9.3")
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}