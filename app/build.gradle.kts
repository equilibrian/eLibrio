import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.mikepenz.aboutlibraries)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

enum class Stage(private val suffix: String) {
    ALPHA("A"),
    BETA("B"),
    RELEASE("R");

    override fun toString(): String {
        return suffix
    }
}

val major = 0
val minor = 10
val patch = 0
val stage = Stage.ALPHA

val version = "$major.$minor.$patch$stage"

android {
    namespace = "su.elibrio.mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "su.elibrio.mobile"
        minSdk = 29
        targetSdk = 34
        versionCode = major * 1000 + minor * 100 + patch
        versionName = version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            applicationVariants.all {
                val variant = this
                variant.outputs
                    .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                    .forEach { output ->
                        output.outputFileName = "eLibrio-${version}.apk"
                    }
            }
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
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.accompanist.permissions)

    implementation(libs.coil.compose)

    implementation(libs.timber)
    implementation(libs.slf4j.api)
    implementation(libs.logback.android)

    implementation(libs.simple.xml)

    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose.m3)

    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.room.runtime)
    kapt("androidx.room:room-compiler:2.6.1")

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}

aboutLibraries {
    registerAndroidTasks = false
    outputFileName = "aboutlibraries.json"
    configPath = "config"
    offlineMode = false
    fetchRemoteLicense = true
    fetchRemoteFunding = true
    gitHubApiToken = gradleLocalProperties(rootDir, providers).getProperty("github.pat")
    additionalLicenses = arrayOf("mit", "mpl_2_0")
    excludeFields = arrayOf("developers", "funding")
    includePlatform = true
    strictMode = com.mikepenz.aboutlibraries.plugin.StrictMode.WARN
    allowedLicenses = arrayOf("Apache-2.0", "asdkl")
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.LINK
    duplicationRule = com.mikepenz.aboutlibraries.plugin.DuplicateRule.SIMPLE
    prettyPrint = false
    filterVariants = arrayOf("debug", "release")
}