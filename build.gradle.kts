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
    compileOnly("com.github.skipdevelopment:pluto-bungee:1.0")
}
