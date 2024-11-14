import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.javacv.platform)
            implementation(libs.kotlinx.serialization.json)
            implementation("ch.qos.logback:logback-classic:1.2.6")
            implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
        }
    }
}


compose.desktop {
    application {
        mainClass = "org.legalteamwork.silverscreen.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "org.legalteamwork.silverscreen"
            packageVersion = "1.0.0"

            windows {
                iconFile.set(project.file("icon.ico"))
            }
        }
    }
}
