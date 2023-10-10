plugins {
    java
    application
}

group = "kuro.redpanda.microservice"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("kuro.redpanda.microservice.KafkaApp")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.apache.kafka:kafka-clients:3.3.1")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("org.slf4j:slf4j-simple:1.7.32")



}

tasks.register<Jar>("fatJar") {
    manifest {
        attributes["Main-Class"] = "kuro.redpanda.microservice.KafkaApp"
    }
    archiveBaseName.set("${rootProject.name}")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}


tasks.test {
    useJUnitPlatform()
}
