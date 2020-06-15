plugins {
    kotlin("jvm") version "1.3.72"
    id("maven-publish")
}

group = "br.com.devsrsouza"
version = "1.0.1"

val jda_version = "4.1.1_137"
val coroutines_version = "1.3.5"
val jda_reactor_version = "1.0.0"

repositories {
    jcenter()
}

dependencies {

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
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

val sources by tasks.registering(Jar::class) {
    baseName = project.name
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name.toLowerCase()

            from(components["java"])
            artifact(sources.get())

            pom {
                description.set("A small Kotlin library to create Guild commands for Discord using JDA and Kotlin Coroutines.")
                url.set("https://github.com/DevSrSouza/jda-kotlin-command")
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://raw.githubusercontent.com/DevSrSouza/jda-kotlin-command/master/LICENSE")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("DevSrSouza")
                        name.set("Gabriel Souza")
                        email.set("devsrsouza@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/DevSrSouza/jda-kotlin-command/tree/master/")
                }
            }
        }
    }
}