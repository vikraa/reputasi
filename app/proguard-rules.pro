# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Source\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn okio.**

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keep interface retrofit.** { *; }

-keepattributes Signature

-keep class com.facebook.** { *; }
-keep interface com.facebook.** { *; }

-dontwarn org.scribe.services.**

-keep class org.apache.** { *; }
-keep class oauth.signpost.** { *; }
-dontwarn org.apache.commons.codec.binary.Base64


