# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class ir.jatlin.topmarket.core.network.model.** { *; }
-keep class ir.jatlin.topmarket.core.network.api.** { *; }
-keep class ir.jatlin.topmarket.core.network.response.** { *; }

-keep class ir.jatlin.topmarket.core.data.di.** { *; }
-keep class ir.jatlin.topmarket.core.data.repository.** { *; }
-keep class ir.jatlin.topmarket.core.data.repository.order.** { *; }
-keep class ir.jatlin.topmarket.core.data.repository.customer.** { *; }

-keep class ir.jatlin.topmarket.core.database.di.** { *; }
-keep class ir.jatlin.topmarket.core.database.converter.** { *; }
-keep class ir.jatlin.topmarket.core.database.dao.** { *; }
-keep class ir.jatlin.topmarket.core.database.entity.** { *; }

-keep class ir.jatlin.topmarket.core.domain.** { *; }
