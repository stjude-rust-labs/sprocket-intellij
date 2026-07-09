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
        bundledPlugin("com.jetbrains.sh")
        plugin("com.redhat.devtools.lsp4ij:0.19.1")
        pluginVerifier()
        zipSigner()
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testImplementation("io.mockk:mockk:1.13.16")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // `WdlParserTest` extends the platform's JUnit 3 `ParsingTestCase`; the vintage
    // engine lets `useJUnitPlatform()` discover and run it.
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.11.4")
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

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        dependsOn("generateWdlParser")
    }

    compileJava {
        dependsOn("generateWdlParser")
    }

    test {
        useJUnitPlatform()
        // `ParsingTestCase` boots and disposes a platform `Application`; isolate each test
        // class in its own JVM so that lifecycle doesn't leak into the plain unit tests.
        setForkEvery(1)
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

    wrapper {
        gradleVersion = "9.4.1"
        distributionSha256Sum = "708d2c6ecc97ca9a11838ef64a6c2301151b8dd10387e22dc1a12c30557cab5b"
        distributionType = Wrapper.DistributionType.ALL
    }
}
