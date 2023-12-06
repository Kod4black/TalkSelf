buildscript {

    repositories {
        google()
        mavenCentral()
        //maven { url 'https://jitpack.io' }
    }

    dependencies {

        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.android.tools.build:gradle:8.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.21")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.5")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")

    }

}

plugins {
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        //maven { url = "https://jitpack.io" }
    }
}

/*tasks.register(name = "clean", Delete) {
    delete rootProject.buildDir
}*/
