plugins {
    id("java")
    id("io.freefair.lombok") version "8.0.1"
}

repositories {
    mavenCentral()
}

val timefoldSolverVersion = "1.15.0"
val logbackVersion = "1.5.8"

group = "org.acme"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation(platform("ai.timefold.solver:timefold-solver-bom:$timefoldSolverVersion"))
    implementation("ai.timefold.solver:timefold-solver-core")
    testImplementation("ai.timefold.solver:timefold-solver-test")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")


    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.glassfish:jakarta.el:4.0.2")
}

tasks.test {
    useJUnitPlatform()
}