plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {

    namespace = "com.github.odaridavid.talkself"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.odaridavid.talkself"
        minSdk = 23
        targetSdk = 34
        versionCode = 4
        versionName = "3.0.1"

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

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.6.0") // Don't update yet
    implementation("androidx.preference:preference-ktx:1.2.1")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")

    //Fragments
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //Hilt
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.dagger:hilt-android:2.48.1")
    "ksp"("com.google.dagger:hilt-compiler:2.48.1")


    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    "ksp"("androidx.room:room-compiler:2.6.1")

    // Kotlin navigation components
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    //livedata
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    implementation("com.github.xabaras:RecyclerViewSwipeDecorator:1.4")

    //Transform into a different view or activity using morphing animations.
    implementation("com.github.skydoves:transformationlayout:1.0.8")

    //Power menu
    implementation("com.github.skydoves:powermenu:2.1.9")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.14.2")
    "ksp"("com.github.bumptech.glide:compiler:4.12.0")

    //color picker
    implementation("codes.side:andcolorpicker:0.6.2")

    //Circle imageview
    implementation("de.hdodenhof:circleimageview:3.1.0")
}