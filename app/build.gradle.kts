@file:Suppress("UnstableApiUsage")

plugins {
    java
    jacoco
    alias(libs.plugins.quarkus)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform(libs.quarkus.bom))
    implementation("io.quarkus:quarkus-qute")
    implementation("io.quarkus:quarkus-cache")
    implementation ("io.quarkus:quarkus-rest")
    implementation( "io.quarkus:quarkus-rest-jackson")
    implementation ("io.quarkus:quarkus-arc")
    implementation(libs.j2html)
    implementation(libs.google.api.client)
    implementation(libs.google.api.sheets)
    implementation(platform(libs.google.auth.bom))
    implementation(libs.google.auth.library)

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
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
