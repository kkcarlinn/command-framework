import org.gradle.api.publish.maven.*

plugins {
    id("java")
    id("maven-publish")
}

group = "br.com.unidade"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    compileOnly("com.github.skipdevelopment:pluto-spigot:1.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "br.com.unidade"
            artifactId = "command-framework"
            version = "1.0.0"
            from(components["java"])
        }
    }
}

