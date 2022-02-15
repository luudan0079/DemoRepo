plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")

}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.hbt.demoapplication"
        minSdk = 26
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    dataBinding {
        isEnabled = true
    }

    lint {
        xmlReport = true
    }
}

val ktlint by configurations.creating

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))
val outputFile = "${outputDir}ktlint-results.xml"

val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)
    isIgnoreExitValue = true
    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("src/**/*.kt", "--reporter=checkstyle,output=${outputFile}", "--android")
}

val ktlintPreCommit by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)
    isIgnoreExitValue = false
    description = "Check Kotlin code style."
    main = "com.pinterest.ktlint.Main"
    classpath = ktlint
    args = listOf("src/**/*.kt", "--android")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "src/**/*.kt", "--android")
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.0")
    implementation("junit:junit:4.+")
    implementation("androidx.test.ext:junit:1.1.3")
    implementation("androidx.test.espresso:espresso-core:3.4.0")
    kapt("com.android.databinding:compiler:3.1.4")

    ktlint("com.pinterest:ktlint:0.43.2") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}