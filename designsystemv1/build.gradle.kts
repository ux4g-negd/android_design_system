import java.util.Properties
import java.security.MessageDigest

fun calculateChecksum(file: File, algorithm: String): String {
    val digest = MessageDigest.getInstance(algorithm)
    file.inputStream().use { input ->
        val buffer = ByteArray(8192)
        var bytesRead = input.read(buffer)
        while (bytesRead != -1) {
            digest.update(buffer, 0, bytesRead)
            bytesRead = input.read(buffer)
        }
    }
    return digest.digest().joinToString("") { "%02x".format(it) }
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
    signing
}

android {
    namespace = "com.ux4g.designsystemv1"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("consumer-rules.pro")
        }
        debug {
            consumerProguardFiles("consumer-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}
for (key in localProperties.stringPropertyNames()) {
    project.extensions.extraProperties.set(key, localProperties.getProperty(key))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "io.github.ux4g-negd"
                artifactId = "designsystemv1"
                version = "1.0.4"

                pom {
                    name.set("Ux4G Design System Compose")
                    description.set("Android Jetpack Compose Design System for Ux4G")
                    url.set("https://github.com/ux4g-negd/ux4g-design-system-compose-mobile")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("ux4g-negd")
                            name.set("Ux4G NeGD")
                            email.set("ux4gofficial@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/ux4g-negd/ux4g-design-system-compose-mobile.git")
                        developerConnection.set("scm:git:ssh://github.com/ux4g-negd/ux4g-design-system-compose-mobile.git")
                        url.set("https://github.com/ux4g-negd/ux4g-design-system-compose-mobile")
                    }
                }
            }
        }

        repositories {
            // Option 1: GitHub Packages
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/ux4g-negd/ux4g-design-system-compose-mobile")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
                }
            }

            // Option 2: Sonatype (Maven Central)
            maven {
                name = "Sonatype"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                    password = project.findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
                }
            }
        }
    }

    signing {
        val signingKeyId = project.findProperty("signing.keyId") as String?
        val signingPassword = project.findProperty("signing.password") as String?
        val signingKey = project.findProperty("signing.key") as String?
        val secretKeyRingFile = project.findProperty("signing.secretKeyRingFile") as String?

        if (signingKeyId != null && signingPassword != null) {
            if (signingKey != null) {
                useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
                sign(publishing.publications)
            } else if (secretKeyRingFile != null) {
                val keyringFile = file(secretKeyRingFile)
                if (keyringFile.exists()) {
                    useInMemoryPgpKeys(signingKeyId, keyringFile.readText(), signingPassword)
                    sign(publishing.publications)
                } else {
                    println("Skipping signing: secretKeyRingFile not found at $secretKeyRingFile")
                }
            } else {
                println("Skipping signing: neither signing.key nor signing.secretKeyRingFile is provided")
            }
        } else {
            println("Skipping signing: missing signing.keyId or signing.password in local.properties")
        }
    }

    tasks.register<Zip>("generateBundle") {
        group = "publishing"
        description = "Generates a ZIP bundle for Sonatype Central Portal with correct structure and checksums"

        dependsOn("publishReleasePublicationToMavenLocal")

        val groupIdPath = "io/github/ux4g-negd"
        val artifactId = "designsystemv1"
        val version = "1.0.4"
        val userHome = System.getProperty("user.home")
        val m2Repo = file("$userHome/.m2/repository/$groupIdPath/$artifactId/$version")

        // Structure the zip to match the Maven coordinates path
        into("$groupIdPath/$artifactId/$version") {
            from(m2Repo)
        }

        archiveFileName.set("bundle_$version.zip")
        destinationDirectory.set(rootProject.projectDir)

        doFirst {
            if (!m2Repo.exists()) {
                throw GradleException("Artifacts not found in Maven Local: ${m2Repo.absolutePath}. Please run publishToMavenLocal first.")
            }
            
            // Generate checksums for all files in m2Repo
            m2Repo.listFiles()?.forEach { file ->
                if (file.isFile && !file.name.endsWith(".md5") && !file.name.endsWith(".sha1")) {
                    val md5 = calculateChecksum(file, "MD5")
                    val sha1 = calculateChecksum(file, "SHA-1")
                    file.resolveSibling("${file.name}.md5").writeText(md5)
                    file.resolveSibling("${file.name}.sha1").writeText(sha1)
                }
            }
        }

        doLast {
            println("Bundle created at: ${rootProject.projectDir.absolutePath}/bundle_$version.zip")
            println("Please upload this file to https://central.sonatype.com/")
        }
    }
}

dependencies {

    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
//    api(platform(libs.androidx.compose.bom))
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    api(libs.androidx.foundation.layout)
    api(libs.androidx.material.icons.core)
    api(libs.androidx.material.icons.extended)
    api(libs.coil.compose)
    api(libs.mlkit.face.detection)
    api(libs.androidx.camera.core)
    api(libs.androidx.camera.camera2)
    api(libs.androidx.camera.lifecycle)
    api(libs.androidx.camera.view)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}