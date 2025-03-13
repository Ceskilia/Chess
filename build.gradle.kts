import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("maven-publish")
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.ceskilia"
version = "1.0"

application {
    mainClass.set("de.ceskilia.chess.LauncherKt")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    implementation("io.github.oshai:kotlin-logging-jvm:5.0.0-beta-04")
    implementation("ch.qos.logback:logback-classic:1.4.12")

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}

kotlin {
    target.compilations.all {
        kotlinOptions {
            jvmTarget = "21"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}