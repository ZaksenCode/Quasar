plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradleup.shadow") version "8.3.0"
}

val minestomVersion = project.property("minestom.version") as String
val slf4jSimpleVersion: String = project.property("slf4j_simple.version") as String

group = "me.zaksen.quasar"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io" )
}

dependencies {
    implementation("net.minestom:minestom-snapshots:$minestomVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jSimpleVersion")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}