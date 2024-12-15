plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("jacoco")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true // Habilita la cobertura para tests instrumentados
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "META-INF/LICENSE.md",
                    "META-INF/LICENSE",
                    "META-INF/LICENSE.txt",
                    "META-INF/NOTICE",
                    "META-INF/NOTICE.txt",
                    "META-INF/{AL2.0,LGPL2.1}",
                    "META-INF/LICENSE-notice.md"
                )
            )
        }
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.espresso.intents)
    implementation(libs.androidx.games.text.input)
    implementation(libs.androidx.uiautomator)
    implementation(libs.junit.jupiter)
    implementation(libs.androidx.junit.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.inline)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.google.code.gson:gson:2.8.9") // Aquí se agregan las dependencias
    implementation("com.google.zxing:core:3.5.1")
    implementation("androidx.appcompat:appcompat:1.6.1")


    implementation("com.google.code.gson:gson:2.8.9")
    //Dependencias para lector de qr
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.4.1")// Aquí se agregan las dependencias

    //Dependencias envio por SMTP
    implementation("com.sun.mail:android-mail:1.6.0")
    implementation("com.sun.mail:android-activation:1.6.0")


}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest", "connectedDebugAndroidTest") // Ejecuta ambos tipos de tests antes del reporte

    reports {
        xml.required.set(true)  // Generar reporte XML
        html.required.set(true) // Generar reporte HTML
    }

    // Ruta de las clases compiladas en debug
    val debugTree = fileTree(
        mapOf(
            "dir" to "$buildDir/tmp/kotlin-classes/debug",
            "excludes" to listOf(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*", // Excluir clases de test
                "**/databinding/**",
                "**/androidx/**",
                "**/kotlin/**"
            )
        )
    )

    // Ruta de los archivos fuente
    val mainSrc = files("$projectDir/src/main/java")

    // Configuración de directorios
    sourceDirectories.setFrom(mainSrc)
    classDirectories.setFrom(debugTree)
    executionData.setFrom(
        fileTree(
            mapOf(
                "dir" to "$buildDir",
                "includes" to listOf(
                    "outputs/code_coverage/debugAndroidTest/connected/SM-A226B - 11/coverage.ec" // Cobertura de instrumented tests
                )
            )
        )
    )
}
