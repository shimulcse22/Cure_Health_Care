//plugins {
//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
//    id("org.jetbrains.kotlin.kapt")
//    id("com.google.devtools.ksp")
//}
//
//android {
//    namespace = "com.devshawon.curehealthcare"
//    compileSdk = 34
//
//    defaultConfig {
//        applicationId = "com.devshawon.curehealthcare"
//        minSdk = 24
//        targetSdk = 33
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    kapt {
//        correctErrorTypes = true
//        useBuildCache = true
//    }
//
//    buildTypes {
//        debug {
//            isMinifyEnabled = false
//            isZipAlignEnabled = true
//        }
//        release {
//            isMinifyEnabled = false
//            isZipAlignEnabled =  true
//            isDebuggable = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
//            )
//        }
//    }
//
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//
//    buildFeatures {
//        buildConfig = true
//    }
//}
//
//dependencies {
//
//    implementation("androidx.core:core-ktx:1.9.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.10.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//
//    val retrofitVersion = "2.9.0"
//    implementation("com.squareup.retrofit2:retrofit:${retrofitVersion}")
//    implementation("com.squareup.retrofit2:converter-moshi:${retrofitVersion}")
//
//    val coroutineVersion = "1.6.4"
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${coroutineVersion}")
//
//
//    val moshiVersion = "1.13.0"
//    implementation("com.squareup.moshi:moshi-kotlin:${moshiVersion}")
//    kapt("com.squareup.moshi:moshi-kotlin-codegen:${moshiVersion}")
//
//
//
//    val daggerVersion = "2.42"
//    implementation("com.google.dagger:dagger:${daggerVersion}")
//    implementation("com.google.dagger:dagger-android-support:${daggerVersion}")
//    kapt("com.google.dagger:dagger-compiler:${daggerVersion}")
//    implementation("com.google.dagger:dagger-android:$daggerVersion")
//    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")
//
//    val navigationVersion = "2.7.4"
//
//    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
//    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
//
//    val stethoVersion = "1.6.0"
//    implementation("com.facebook.stetho:stetho:${stethoVersion}")
//    implementation("com.facebook.stetho:stetho-okhttp3:${stethoVersion}")
//
//    val roomVersion = "2.5.2"
//    implementation("androidx.room:room-runtime:$roomVersion")
//    kapt("androidx.room:room-compiler:$roomVersion")
//    implementation("androidx.room:room-ktx:$roomVersion")
//
//    //implementation("com.jakewharton.timber:timber:4.7.1")
//
//    val glideVersion = "4.9.0"
//    implementation("com.github.bumptech.glide:glide:${glideVersion}")
//    kapt("com.github.bumptech.glide:compiler:${glideVersion}")
//
//    implementation("com.intuit.sdp:sdp-android:1.0.6")
//    implementation("com.intuit.ssp:ssp-android:1.0.6")
//    implementation("com.crashlytics.sdk.android:crashlytics:2.10.1")
//
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10")
//    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")
//
//    implementation ("dev.b3nedikt.applocale:applocale:2.0.3")
//
//// Needed to intercept view inflation
//    implementation("dev.b3nedikt.viewpump:viewpump:4.0.5")
//
//// Allows to update the text of views at runtime
//    implementation("dev.b3nedikt.reword:reword:4.0.0")
//
//
//
////
//    //implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
//
//
//}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.devshawon.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.devshawon.myapplication"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isZipAlignEnabled = true
        }
        release {
            isMinifyEnabled = false
            isZipAlignEnabled =  true
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:${retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-moshi:${retrofitVersion}")

    val coroutineVersion = "1.6.4"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${coroutineVersion}")

    val moshiVersion = "1.13.0"
    implementation("com.squareup.moshi:moshi-kotlin:${moshiVersion}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${moshiVersion}")

    val daggerVersion = "2.42"
    implementation("com.google.dagger:dagger:${daggerVersion}")
    implementation("com.google.dagger:dagger-android-support:${daggerVersion}")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    //change to ksp to kapt
    kapt("com.google.dagger:dagger-compiler:${daggerVersion}")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")

    val navigationVersion = "2.7.4"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    val stethoVersion = "1.6.0"
    implementation("com.facebook.stetho:stetho:${stethoVersion}")
    implementation("com.facebook.stetho:stetho-okhttp3:${stethoVersion}")

    val roomVersion = "2.5.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    val glideVersion = "4.9.0"
    implementation("com.github.bumptech.glide:glide:${glideVersion}")
    kapt("com.github.bumptech.glide:compiler:${glideVersion}")

    val sdpVersion = "1.0.6"
    implementation("com.intuit.sdp:sdp-android:$sdpVersion")
    implementation("com.intuit.ssp:ssp-android:$sdpVersion")

    val crashlyticsVersion = "2.10.1"
    implementation("com.crashlytics.sdk.android:crashlytics:$crashlyticsVersion")

    val timberVersion = "4.7.1"
    implementation("com.jakewharton.timber:timber:$timberVersion")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")


    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")
}