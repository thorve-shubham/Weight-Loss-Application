// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

//extra addition
buildscript {
    extra.apply {
        set("room_version", "2.5.2")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}