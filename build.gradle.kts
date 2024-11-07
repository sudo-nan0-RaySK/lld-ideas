plugins {
    id("java")
}

group = "com.github.sudo.nano.raysk"
version = "1.0-SNAPSHOT"

var rabbitAmqpSdkVersion = "5.18.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.rabbitmq:amqp-client:${rabbitAmqpSdkVersion}")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}