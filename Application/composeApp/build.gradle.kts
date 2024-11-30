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
        val desktopTest by getting {
            dependencies {
                implementation(kotlin("test")) // Базовые функции тестирования
                implementation(kotlin("test-junit")) // Для запуска JUnit-тестов
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlin.logging)
            implementation(libs.logback.classic)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.javacv.platform)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.logback.classic)
            implementation(libs.kotlin.logging)
            implementation(kotlin("test"))
        }
    }
}

compose.desktop {
    application {
        mainClass = "org.legalteamwork.silverscreen.MainKt"

        nativeDistributions {
            includeAllModules = true
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "org.legalteamwork.silverscreen"
            packageVersion = "1.0.0"

            windows {
                iconFile.set(project.file("icon.ico"))
            }
        }
    }
}

// Убедитесь, что все тесты запускаются
//tasks.test {
//    useJUnitPlatform()
//}
