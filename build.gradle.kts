plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("kapt") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-velocity") version "2.3.0"
}

group = "dev.rafi"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    kapt("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")

    implementation("dev.dejvokep:boosted-yaml:1.3.5")

    implementation("org.bstats:bstats-velocity:3.0.2")

    implementation("org.jetbrains.exposed:exposed-core:0.51.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.51.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.51.0")

    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.xerial:sqlite-jdbc:3.45.2.0")

    implementation("dev.rollczi:litecommands-velocity:3.4.1")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks {
    shadowJar {
        relocate("dev.dejvokep.boostedyaml", "dev.rafi.ezwhitelist.common.libs.bosstedyaml")
        relocate("org.bstats", "dev.rafi.ezwhitelist.common.libs.bstats")
    }

    runVelocity {
        velocityVersion("3.3.0-SNAPSHOT")
    }
}