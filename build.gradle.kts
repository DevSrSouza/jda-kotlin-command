plugins {
    kotlin("jvm") version "1.3.72"
}

group = "br.com.devsrsouza"
version = "1.0.0"

val jda_version = "4.1.1_137"
val coroutines_version = "1.3.5"
val jda_reactor_version = "1.0.0"

repositories {
    jcenter()
}

dependencies {

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutines_version")

    implementation("club.minnced:jda-reactor:$jda_reactor_version")
    implementation("net.dv8tion:JDA:$jda_version") {
        exclude(module = "opus-java")
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}