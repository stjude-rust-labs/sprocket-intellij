import java.net.URI

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.intellij.platform") version "2.11.0"
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity(providers.gradleProperty("platformVersion"))
        bundledPlugin("org.jetbrains.plugins.textmate")
        plugin("com.redhat.devtools.lsp4ij:0.19.1")
        pluginVerifier()
        zipSigner()
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testImplementation("io.mockk:mockk:1.13.16")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(17)
}

intellijPlatform {
    pluginVerification {
        ides {
            recommended()
        }
    }
}

val grammarUrl = "https://raw.githubusercontent.com/stjude-rust-labs/sprocket-vscode/main/syntaxes/wdl.tmGrammar.json"
val generatedSyntaxesDir = layout.buildDirectory.dir("generated/syntaxes")

tasks {
    val updateGrammar by registering {
        group = "build"
        description = "Downloads the latest WDL TextMate grammar from sprocket-vscode"
        outputs.dir(generatedSyntaxesDir)

        doLast {
            val dir = generatedSyntaxesDir.get().asFile
            dir.mkdirs()
            val grammarFile = File(dir, "wdl.tmGrammar.json")
            URI(grammarUrl).toURL().openStream().use { input ->
                grammarFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            println("Downloaded grammar: ${grammarFile.absolutePath}")
        }
    }

    prepareSandbox {
        dependsOn(updateGrammar)
        from(generatedSyntaxesDir) {
            into("${project.name}/syntaxes")
        }
        from("src/main/resources/syntaxes/package.json") {
            into("${project.name}/syntaxes")
        }
    }

    buildPlugin {
        dependsOn(updateGrammar)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(generatedSyntaxesDir) {
            into("syntaxes")
        }
        from("src/main/resources/syntaxes/package.json") {
            into("syntaxes")
        }
    }

    processResources {
        dependsOn(updateGrammar)
    }

    test {
        useJUnitPlatform()
    }

    patchPluginXml {
        sinceBuild.set("242")
        untilBuild.set("253.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
