plugins {
    `java-library`
}

group = "io.rsug.socks5"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains", "annotations", "19+")
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.jar {
    manifest {
        attributes(
                mapOf("Main-Class" to "Main",
                        "Implementation-Title" to project.name,
                        "Implementation-Version" to project.version,
                        "Comment" to "Русские буквы, привет юникод")
        )
    }
}
