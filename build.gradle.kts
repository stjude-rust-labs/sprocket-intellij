import java.net.URI

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.20"
    id("org.jetbrains.intellij.platform") version "2.14.0"
    id("org.jetbrains.grammarkit") version "2023.3.0.3"
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
        bundledPlugin("com.jetbrains.sh")
        plugin("com.redhat.devtools.lsp4ij:0.19.1")
        pluginVerifier()
        zipSigner()
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testImplementation("io.mockk:mockk:1.13.16")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

grammarKit {
    tasks.register<org.jetbrains.grammarkit.tasks.GenerateLexerTask>("generateWdlLexer") {
        sourceFile.set(file("src/main/kotlin/org/stjude/sprocket/lang/WDL.flex"))
        targetOutputDir.set(file("src/main/gen/org/stjude/sprocket/lang"))
    }

    tasks.register<org.jetbrains.grammarkit.tasks.GenerateParserTask>("generateWdlParser") {
        dependsOn("generateWdlLexer")
        sourceFile.set(file("src/main/kotlin/org/stjude/sprocket/lang/WDL.bnf"))
        targetRootOutputDir.set(file("src/main/gen"))
        pathToParser.set("org/stjude/sprocket/lang/parser")
        pathToPsiRoot.set("org/stjude/sprocket/lang/psi")
    }
}

kotlin {
    jvmToolchain(17)
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = provider { properties("pluginSinceBuild") }
            untilBuild = provider { properties("pluginUntilBuild") }
        }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

sourceSets["main"].java.srcDirs("src/main/gen")
sourceSets["main"].kotlin.srcDirs("src/main/gen")

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

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        dependsOn("generateWdlParser")
    }

    compileJava {
        dependsOn("generateWdlParser")
    }

    test {
        useJUnitPlatform()
    }

    patchPluginXml {
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))
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
