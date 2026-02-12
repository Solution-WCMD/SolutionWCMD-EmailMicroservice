plugins {
    id("java")
    id("io.freefair.lombok") version "9.2.0"
    id("com.gradleup.shadow") version "9.3.1"
}

group = "com.solutiongameofficial.email"
version = "v0.0.1-alpha"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val javaLinVersion = "6.7.0"
val angusMailVersion = "2.1.5"
val jacksonVersion = "2.21.0"
val simpleSlf4jVersion = "2.0.17"

dependencies {
    implementation("io.javalin:javalin:$javaLinVersion")
    implementation("org.eclipse.angus:angus-mail:$angusMailVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    runtimeOnly("org.slf4j:slf4j-simple:$simpleSlf4jVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    enabled = false
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier.set("")
    manifest {
        attributes("Main-Class" to "com.solutiongameofficial.email.Main")
    }
}