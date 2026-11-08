import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

// Baca local.properties untuk mendapatkan base URL environment "local".
// File ini tidak ikut ter-commit (.gitignore), jadi tiap developer mengisi sendiri.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}
val localKiosskuBaseUrl: String = localProperties.getProperty("kiosskuBackendBaseUrl") ?: ""
val localRegionBaseUrl: String = localProperties.getProperty("indonesiaRegionApiBaseUrl") ?: ""

android {
    namespace = "com.itpens.kiossku"
    compileSdk = 33

    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    defaultConfig {
        applicationId = "com.itpens.kiossku"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        create("staging") {
            buildConfigField("String", "KIOSSSKU_BASE_URL", "\"https://dev.kiossku.com/be-api/v1/\"")
            buildConfigField("String", "INDONESIA_REGION_BASE_URL", "\"https://ibnux.github.io/data-indonesia/\"")
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "KIOSSSKU_BASE_URL", "\"https://kiossku.com/be-api/v1/\"")
            buildConfigField("String", "INDONESIA_REGION_BASE_URL", "\"https://ibnux.github.io/data-indonesia/\"")
        }

        getByName("debug") {
            buildConfigField("String", "KIOSSSKU_BASE_URL", localKiosskuBaseUrl)
            buildConfigField("String", "INDONESIA_REGION_BASE_URL", localRegionBaseUrl)
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:31.0.2"))

    // PICASSO
    implementation("com.squareup.picasso:picasso:2.8")

    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.11.0")


    // GSON Converter & Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_ui_version"]}")


    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2022.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // User Interface
    implementation("androidx.compose.ui:ui")
    //implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material")

    // Semua material icon
    implementation("androidx.compose.material:material-icons-extended")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // compose navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Compose viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    // Compose constraint layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation("com.google.accompanist:accompanist-appcompat-theme:0.25.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${rootProject.extra["compose_ui_version"]}")

    // Coil Compose
    implementation("io.coil-kt:coil-compose:2.2.2")

    // DAGGER HILT
    implementation("com.google.dagger:hilt-android:${rootProject.extra["hilt_version"]}")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["hilt_version"]}")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.2")
}

kapt {
    correctErrorTypes = true
}
