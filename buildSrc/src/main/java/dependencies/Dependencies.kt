package dependencies

object Versions {
    val gradle = "3.5.3"
    val compilesdk = 29
    val minsdk = 21
    val targetsdk = 29
    val kotlin = "1.3.61"
    val ktx = "1.2.0"
    val dagger = "2.25.4"
    val nav_components = "2.3.0-alpha04"
    val material_dialogs = "3.2.1"
    val room = "2.1.0"
    val appcompat = "1.1.0-rc01"
    val constraintlayout = "1.1.3"
    val material_design = "1.2.0-alpha05"
    val play_core = "1.7.1"
    val play_services = "4.3.3"
    val leak_canary = "2.0-alpha-3"
    val swipe_refresh_layout = "1.1.0-alpha03"
    val firestore = "21.4.2"
    val espresso_core = "3.1.1"
    val espresso_idling_resource = "3.2.0"
    val mockk_version = "1.9.2"
    val test_runner = "1.2.0"
    val test_core = "1.2.0"
    val coroutines_version = "1.3.0"
    val lifecycle_version = "2.2.0-alpha03"
    val retrofit2_version = "2.6.0"
    val markdown_processor = "0.1.3"
    val junit_jupiter_version = "5.6.0"
    val junit_4_version = "4.12"
}

object Dependencies {
    val kotlin_standard_library = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    val kotlin_coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines_version}"
    val kotlin_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines_version}"
    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.nav_components}"
    val navigation_runtime = "androidx.navigation:navigation-runtime:${Versions.nav_components}"
    val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Versions.nav_components}"
    val navigation_dynamic = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.nav_components}"
    val material_dialogs = "com.afollestad.material-dialogs:core:${Versions.material_dialogs}"
    val material_dialogs_input = "com.afollestad.material-dialogs:input:${Versions.material_dialogs}"
    val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    val room_ktx = "androidx.room:room-ktx:${Versions.room}"
    val play_core = "com.google.android.play:core:${Versions.play_core}"
    val leak_canary = "com.squareup.leakcanary:leakcanary-android:${Versions.leak_canary}"
    val firestore = "com.google.firebase:firebase-firestore-ktx:${Versions.firestore}"
    val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle_version}"
    val lifecycle_coroutines = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle_version}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit2_version}"
    val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit2_version}"
    val markdown_processor = "com.yydcdut:markdown-processor:${Versions.markdown_processor}"
}

object AndroidTestDependencies{

    val kotlin_test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
    val idling_resource = "androidx.test.espresso:espresso-idling-resource:${Versions.espresso_idling_resource}"
    val test_runner = "androidx.test:runner:${Versions.test_runner}"
    val test_rules = "androidx.test:rules:${Versions.test_runner}"
    val text_core_ktx = "androidx.test:core-ktx:${Versions.test_core}"
    val mockk_android = "io.mockk:mockk-android:${Versions.mockk_version}"
}

object TestDependencies {

    val jupiter_api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit_jupiter_version}"
    val jupiter_params = "org.junit.jupiter:junit-jupiter-params:${Versions.junit_jupiter_version}"
    val jupiter_engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit_jupiter_version}"
    val mockk = "io.mockk:mockk:${Versions.mockk_version}"
    val junit4 = "junit:junit:${Versions.junit_4_version}"
}

object ApplicationId {
    val id = "com.codingwithmitch.cleannotes"
}


object Build {
    val build_tools = "com.android.tools.build:gradle:${Versions.gradle}"
    val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val google_services = "com.google.gms:google-services:${Versions.play_services}"
    val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.3.2.0"
}

object JavaVersion {
    val version_1_8 = "1.8"
}

object Releases {
    val version_code = 1
    val version_name = "1.0"
}


object AnnotationProcessorsDependencies {
    val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle_version}"
}

object SupportDependencies {

    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    val material_design = "com.google.android.material:material:${Versions.material_design}"
    val swipe_refresh_layout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipe_refresh_layout}"
}




