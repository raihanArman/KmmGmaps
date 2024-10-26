import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinSerializer)
    alias(libs.plugins.kotlinAndroidParcelize)
    alias(libs.plugins.nativeCocoapods)
    alias(libs.plugins.androidGoogleService)
}

val secretFolder = "$projectDir/build/generatedSecret"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.bumble.appyx.utils.multiplatform.Parcelize",
//                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck"
                )
            }
        }
    }

    cocoapods {
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        podfile = project.file("../iosApp/Podfile")
        name = "ComposeApp"

        ios.deploymentTarget = "17.0"

        framework {
            baseName = "ComposeApp"
            isStatic = false
        }

        pod("netfox") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            version = "1.21.0"
        }

        pod("GoogleMaps") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod("FirebaseCore") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod("FirebaseAuth") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod("GoogleSignIn") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        // Maps custom Xcode configuration to NativeBuildType
//        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
//        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }


    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets.commonMain.configure {
        kotlin.srcDirs(secretFolder)
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)

            implementation(libs.googleMaps.android.core)
            implementation(libs.googleMaps.android.compose)
            implementation(libs.android.play.service.location)
            implementation(libs.android.play.service.auth)

            implementation(libs.androidFirebase.auth)

            implementation(libs.android.credentials)
            implementation(libs.android.credentials.play.service.auth)
            implementation(libs.android.google.id)

        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.logging)

            implementation(libs.compose.viewmodel)
            implementation(libs.jetbrains.navigation)

            //Appyx
            implementation(libs.appyx.navigation)
            implementation(libs.appyx.interactions)
            api(libs.appyx.backstack)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.randev.kmmgmaps"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources", secretFolder)

    defaultConfig {
        applicationId = "com.randev.kmmgmaps"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

fun generatedSecret(file: String) {
    val propContent = file("$rootDir/$file").readText()
    val propData = parseProp(propContent)

    var ktContent = "package com.randev.kmmgmaps\n\nobject SecretConfig {\n"
    propData.forEach { (key, value) ->
        ktContent += "    const val $key = $value\n"
    }
    ktContent += "}"

    val folder = file(secretFolder)
    if (!folder.exists()) {
        folder.mkdirs()
    }

    val fileSecret = file("$secretFolder/SecretConfig.kt")
    if (!fileSecret.exists()) {
        fileSecret.createNewFile()
    }

    fileSecret.writeText(ktContent)
}

fun parseProp(content: String): Map<String, Any> {
    val propertiesData = mutableMapOf<String, Any>()
    content.lines().forEach { line ->
        val key = line.substringBefore("=")
        val rawValue = line.substringAfter("=")
        val value = when {
            rawValue == "true" || rawValue == "false" -> {
                rawValue.toBoolean()
            }
            rawValue.toIntOrNull() != null -> {
                rawValue.toInt()
            }
            rawValue.toLongOrNull() != null -> {
                rawValue.toLong()
            }
            else -> "\"$rawValue\""
        }

        propertiesData[key] = value
    }

    return propertiesData
}

tasks.register("generateSecret") {
    doLast {
        generatedSecret("secret.properties")
    }
}

afterEvaluate {
    tasks.getByName("generateComposeResClass").dependsOn("generateSecret")
}