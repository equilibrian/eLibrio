plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val major = 0
val minor = 1
val patch = 0

val latestCommitId: String = try {
    val process = ProcessBuilder("git", "rev-parse", "HEAD")
        .start()
    process.waitFor(5, TimeUnit.SECONDS)
    val fullCommitId = process.inputStream.bufferedReader().readText().trim()
    fullCommitId.substring(0, 7)
} catch (e: Exception) {
    "Failed to fetch commit id"
}


android {
    namespace = "ru.equilibrian.elibrio"
    compileSdk = 33

    defaultConfig {
        applicationId = "ru.equilibrian.elibrio"
        minSdk = 26
        targetSdk = 33
        versionCode = major * 1000 + minor * 100 + patch
        versionName = "$major.$minor.$patch ($latestCommitId)"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
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
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // logging lib
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("androidx.compose.foundation:foundation-layout:1.4.3")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-runtime-ktx:2.6.0")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.core:core-ktx:1.10.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
