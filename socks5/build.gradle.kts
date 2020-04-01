plugins {
    java
    `java-library`
}

group = "io.rsug.socks5"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}