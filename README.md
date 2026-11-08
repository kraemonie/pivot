# Pivot
Offline Android Studio app for linear independence and matrix rank. Open this directory in Android Studio and sync Gradle. Java 17 and Android SDK 35 are required.

## Use
Solve: select 1-5 rows and columns, enter numbers or fractions, and calculate. Each column is one vector. Results include exact rank, determinant for square matrices, pivot columns and a nontrivial dependence relation. Step through row reduction.
Practice: answer rank and independence questions with immediate explanations. Scores persist locally. Learn: concise reference based on the supplied course PDF pages 1-3.

## Build and checks
`node --test tests/math.test.cjs`
`gradlew.bat assembleDebug lintDebug`

The APK is generated at app/build/outputs/apk/debug/app-debug.apk. No Internet permission or external services. Reduced motion supported. Decimal inputs are interpreted as exact fractions; expressions with variables are not supported. Font: Space Grotesk (OFL).

Exact arithmetic checks every intermediate integer against JavaScript safe-integer limits. Calculations exceeding those limits show an error instead of a rounded result.

## Camera and interface (0.1.2)
Scan a matrix opens the phone camera; Choose photo imports an existing image. Bundled ML Kit text recognition reads it on-device. Review and correct the numbers, with one row per line, before computing. Printed integers work best; handwriting, stacked fractions and complex brackets may need correction. Scans support the same 1–5 row/column limit. The manifest removes Internet permission. Camera photos are stored in private app cache.

The UI uses a minimum 12px text size, calm matrix-entry focus, custom size pickers, 36 shuffled practice questions, a subtle animated background and reduced-motion-aware popup, Learn and row-step animations.

Recognition reference: https://developers.google.com/ml-kit/vision/text-recognition/v2/android
