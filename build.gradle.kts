import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.8.20"
    id("org.jetbrains.compose") version "1.4.0"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.ceskilia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")

    implementation("io.github.oshai:kotlin-logging-jvm:5.0.0-beta-04")
    implementation("ch.qos.logback:logback-classic:1.4.7")

    implementation(kotlin("reflect"))
    implementation(compose.desktop.currentOs)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}

kotlin {
    jvmToolchain(11)
}

tasks.test {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "LauncherKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Chess"
            packageVersion = "1.0.0"
        }
    }
}