import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    // this plugin is needed to handle some classpaths of
    // dependencies in the Kord library, seems to be the only thing
    // that allows the jar to work properly with Voice Audio in Discord
    id("com.github.johnrengelman.shadow") version "7.1.0"
    application
}

group = "me.callumhoughton"

repositories {
    mavenCentral()
    maven {
        setUrl("https://m2.dv8tion.net/releases")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.kord:kord-core:0.8.0-M7")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

application {
    mainClass.set("MainKt")
}