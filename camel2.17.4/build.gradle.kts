plugins {
    application
}

group = "io.rsug"
version = "0.0.1-PREVIEW"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains", "annotations", "19+")
    implementation("org.apache.camel", "camel-core", "2.17.4")
    implementation("org.apache.camel", "camel-main", "2.17.4")
    implementation("org.apache.camel", "camel-log", "2.17.4")
    implementation("org.apache.camel", "camel-core-xml", "2.17.4")
    implementation("org.apache.camel", "camel-xmlsecurity", "2.17.4")
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}