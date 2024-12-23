-dontwarn org.bytedeco.**
-dontwarn ch.qos.logback.classic.**
-dontwarn ch.qos.logback.core.**
-dontwarn org.opencv.android.**
-dontwarn io.github.oshai.kotlinlogging.**


-keepclasseswithmembers, allowoptimization public class org.legalteamwork.silverscreen.MainKt  {
    public static void main(java.lang.String[]);
}

# На всякий на будущее
#-keep class kotlin.** { *; }
#-keep class kotlinx.** { *; }
-keep class kotlinx.coroutines.** { *; }
#-keep class org.jetbrains.skia.** { *; }
#-keep class org.jetbrains.skiko.** { *; }

-keepclasseswithmembers class androidx.compose.runtime.** { *; }

-keepclasseswithmembers public class **$$serializer {
    private ** descriptor;
}

-keep, includecode public class org.bytedeco.** { *; }

-keep public class ch.qos.logback.classic.** { *; }
-keep public class ch.qos.logback.core.** { *; }

#-printusage
#-printmapping

-optimizationpasses 10

-optimizeaggressively
#-overloadaggressively
#-allowaccessmodification

#-optimizations code/*/*, method/*/*, class/marking/final, library/gson,  class/unboxing/enum
