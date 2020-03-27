object ApplicationId {
    val id = "com.codingwithmitch.cleannotes"
}

object Modules {
    val app = ":app"
    val notes = ":notes"
}

object Versions {
    val gradle = "3.5.3"

    val compilesdk = 29
    val minsdk = 21
    val targetsdk = 29

    val kotlin = "1.3.61"
    val ktx = "1.2.0"
    val dagger = "2.25.4"
    val nav_components = "2.3.0-alpha04"
    val recyclerview = "1.2.0-alpha01"
    val material_dialogs = "3.1.0"
    val room = "2.1.0"

    val appcompat = "1.1.0"
    val constraintlayout = "1.1.3"
    val material_design = "1.2.0-alpha05"

    val play_core = "1.7.1"

}

object Releases {
    val version_code = 1
    val version_name = "1.0"
}

object Dependencies {
    val kotlin_standard_library = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.nav_components}"
    val navigation_runtime = "androidx.navigation:navigation-runtime:${Versions.nav_components}"
    val material_dialogs = "com.afollestad.material-dialogs:core:${Versions.material_dialogs}"
    val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    val room_ktx = "androidx.room:room-ktx:${Versions.room}"

    val play_core = "com.google.android.play:core:${Versions.play_core}"
}

object SupportDependencies {

    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    val material_design = "com.google.android.material:material:${Versions.material_design}"
}


