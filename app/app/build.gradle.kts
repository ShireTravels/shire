plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.shire"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.shire"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation ("androidx.datastore:datastore-preferences:1.1.3")
    implementation(libs.hilt.android)
    "kapt"(libs.hilt.compiler)
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    "kapt"("androidx.room:room-compiler:2.6.1")
    // Room compiler uses sqlite-jdbc at kapt time; pin a newer build for Windows x86_64.
    "kapt"("org.xerial:sqlite-jdbc:3.46.1.3")

    //FIREBASE->

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}

// Allow references to generated code
kapt {
    correctErrorTypes = false
}

val kaptTmpDir = rootProject.layout.projectDirectory.dir(".gradle/tmp").asFile.absolutePath

tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask>().configureEach {
    kaptProcessJvmArgs.add("-Djava.io.tmpdir=$kaptTmpDir")
    kaptProcessJvmArgs.add("-Dorg.sqlite.tmpdir=$kaptTmpDir")
}
