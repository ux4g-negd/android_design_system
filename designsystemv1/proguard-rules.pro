# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep all Composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
-keep @androidx.compose.runtime.Composable fun **()

# Keep all Ux4g Design System classes
-keep class com.ux4g.designsystemv1.** { *; }
-keep interface com.ux4g.designsystemv1.** { *; }
-keep enum com.ux4g.designsystemv1.** { *; }

# Keep Kotlin metadata for reflection
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes Exceptions

# Keep all R8 metadata for Compose
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations

# Keep companion objects
-keep class **$Companion { *; }

# Prevent stripping of classes used by reflection
-dontwarn com.ux4g.**
-dontnote com.ux4g.**

# Keep Coil classes
-keep class coil.** { *; }
-keep interface coil.** { *; }

# Keep Jetpack Compose classes
-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile
