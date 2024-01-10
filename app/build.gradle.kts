import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.madcamp_2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.madcamp_2"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures  {
        viewBinding = true
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}



dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("io.ktor:ktor-client-core:1.6.5")
    implementation("io.ktor:ktor-client-json:1.6.5")
    implementation("io.ktor:ktor-client-serialization:1.6.5")
    implementation("io.ktor:ktor-client-android:1.6.5")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.kakao.sdk:v2-all:2.19.0") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:2.19.0") // 카카오 로그인
    implementation("com.kakao.sdk:v2-talk:2.19.0") // 친구, 메시지(카카오톡)
    implementation("com.kakao.sdk:v2-share:2.19.0") // 메시지(카카오톡 공유)
    implementation("com.kakao.sdk:v2-friend:2.19.0") // 카카오톡 소셜 피커, 리소스 번들 파일 포함
    implementation("com.kakao.sdk:v2-navi:2.19.0") // 카카오내비
    implementation("com.kakao.sdk:v2-cert:2.19.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // 카카오 인증서비스
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}