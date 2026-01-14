plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.mysecureapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mysecureapp"
        minSdk = 24
        targetSdk = 35

        // --- LÓGICA DINÁMICA (DEVSECOPS) ---
        // 1. Intentamos leer el número de Build desde GitHub Actions.
        // Si no existe (ej. compilando en local), usamos '1' por defecto.
        val buildNumber = System.getenv("BUILD_NUMBER")?.toIntOrNull() ?: 1

        // 2. Leemos el Hash del commit para saber qué código exacto es.
        // Tomamos solo los primeros 7 caracteres (ej. "a1b2c3d").
        val commitHash = System.getenv("GITHUB_SHA")?.take(7) ?: "LOCAL-DEV"

        // Asignamos los valores dinámicos al APK
        versionCode = buildNumber
        versionName = "1.0.$buildNumber" // Ej: 1.0.42

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // --- INYECCIÓN DE METADATOS
        // Estos campos se podrán leer desde MainActivity con BuildConfig.NOMBRE_CAMPO
        buildConfigField("String", "BUILD_TIME", "\"${System.currentTimeMillis()}\"")
        buildConfigField("String", "CICD_ORIGIN", "\"GitHub Actions\"")
        buildConfigField("String", "COMMIT_HASH", "\"$commitHash\"")
    }

    // PUNTO 5: Configuración de firmado usando variables de entorno
    signingConfigs {
        create("release") {
            // El pipeline de GitHub Actions creará este archivo físicamente
            storeFile = file("release.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            // PUNTO 5: Aplicamos el firmado a la variante de release
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        // Habilitamos esto para poder usar la clase BuildConfig en el código Kotlin
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}