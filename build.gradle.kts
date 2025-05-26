plugins {
    kotlin("jvm") version "2.1.10"

    id("com.gradleup.shadow") version "8.3.0"
    id("maven-publish")
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

tasks{
    test {
        useJUnitPlatform()
    }
    build {
        dependsOn("shadowJar")
    }
}

kotlin {
    jvmToolchain(21)
}

//publishing {
//    repositories {
//        maven(project.property("publishing.url") as String) {
//            name = "GitHubPackages"
//            credentials {
//                username = (project.findProperty("gpr.user") ?: System.getenv("GITHUB_USERNAME")) as String
//                password = (project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")) as String
//            }
//        }
//    }
//    publications {
//        register<MavenPublication>("gpr") {
//            artifactId = project.property("publishing.id") as String
//            version = project.version as String
//            from(components["java"])
//        }
//    }
//}