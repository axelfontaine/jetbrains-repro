# jetbrains-repro
Repro repo for various issues with Jetbrains' products (IntelliJ, Kotlin, Ktor, ...)

# [KT-38651](https://youtrack.jetbrains.com/issue/KT-38651)

1. Import project in IntelliJ
2. Navigate to `server/src/main/kotlin/main.kt`
3. Click the <span style="color: green">▶</span> next to `fun main() {`

You will now see the following error in the Run tool window for `MainKt`:

```
Exception in thread "main" java.lang.NoClassDefFoundError: com/racepartyapp/shared/HeartRate
	at com.racepartyapp.server.MainKt.main(main.kt:63)
	at com.racepartyapp.server.MainKt.main(main.kt)
Caused by: java.lang.ClassNotFoundException: com.racepartyapp.shared.HeartRate
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:602)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:522)
	... 2 more
```

This does NOT happen when launching via Gradle.