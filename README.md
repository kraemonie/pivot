 <div align="center">

# PIVOT

### Understand the Pivots. Discover the Rank. Test Independence.

**An Offline Android Learning Application for Linear Independence and Matrix Rank**

<p>
  <a href="https://github.com/kraemonie/pivot/releases/latest">
    <img src="https://img.shields.io/badge/DOWNLOAD-ANDROID%20APK-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Download APK">
  </a>
  <a href="https://github.com/kraemonie/pivot/blob/main/docs/demo/pivot-showcase-v0.1.5.mp4">
    <img src="https://img.shields.io/badge/WATCH-APP%20DEMO-FF5252?style=for-the-badge&logo=youtube&logoColor=white" alt="Watch Demo">
  </a>
</p>

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![SDK](https://img.shields.io/badge/Android%20SDK-35-green?logo=android&logoColor=white)
![Offline](https://img.shields.io/badge/Internet-Not%20Required-blue)
![Project](https://img.shields.io/badge/Type-Academic%20Project-8B7CF6)

</div>

---

## Overview

**Pivot** is an offline Android application developed as a learning tool for the Linear Algebra topics **Linear Independence and Rank of a Matrix**.

It provides students with an interactive environment where they can enter matrices, perform row operations, identify pivot columns, calculate matrix rank, and determine whether a set of vectors is linearly independent or dependent.

Beyond solving mathematical problems, Pivot encourages learning through step-by-step solutions, interactive lessons, practice quizzes, and camera-assisted matrix input.

The application is designed to make abstract Linear Algebra concepts easier to understand through practical interaction and immediate feedback.

## Features

| Feature | Description |
|:---|:---|
| **Matrix Solver** | Enter and solve matrices with customizable dimensions. |
| **Linear Independence Checker** | Determine whether matrix columns are linearly independent or dependent. |
| **Matrix Rank Calculator** | Calculate the rank by identifying pivot positions. |
| **Step-by-Step Row Reduction** | Follow the elementary row operations used to simplify a matrix. |
| **Pivot Identification** | Identify pivot columns and understand their role in determining rank. |
| **Dependence Relation** | Display a nontrivial linear dependence relation when applicable. |
| **Determinant Calculator** | Calculate the determinant of square matrices. |
| **Practice Quizzes** | Test understanding through randomized questions and instant explanations. |
| **Learn Module** | Review essential concepts through concise learning materials. |
| **Matrix Scanner** | Scan printed matrices using the device camera or import an existing image. |

---

## Explore the Application

### 01 — Solve

Calculate and analyze matrices through an interactive matrix-solving interface.

- Select matrix dimensions from 1×1 up to 5×5.
- Enter integers, decimals, or fractions.
- Calculate the exact matrix rank.
- Determine linear independence or dependence.
- Identify pivot columns.
- Display a nontrivial dependence relation when applicable.
- Calculate determinants for square matrices.
- Review row reduction one step at a time.

### 02 — Learn

Understand the mathematical concepts behind the calculations.

The Learn module provides concise reference materials covering the essential ideas of linear independence and matrix rank.

### 03 — Practice

Strengthen mathematical understanding through interactive exercises.

- 36 shuffled practice questions.
- Questions about matrix rank and linear independence.
- Immediate feedback after answering.
- Explanations to help understand correct answers.
- Locally saved practice scores.

### 04 — Scan

Enter matrices more conveniently using the phone camera.

- Capture a matrix using the device camera.
- Import an existing image from the gallery.
- Recognize text directly on the device using Google ML Kit.
- Review and correct recognized matrix entries before solving.

**Note:** Printed integers generally produce the best recognition results. Handwriting, stacked fractions, and complex brackets may require manual corrections.

---

## Mathematical Foundation

Pivot is built around two closely related Linear Algebra concepts.

### Matrix Rank

The rank of a matrix is the number of pivot positions obtained after reducing the matrix to row echelon form.

\[
\operatorname{rank}(A)=\text{Number of pivots}
\]

### Linear Independence

A set of vectors is linearly independent if the only solution to their linear combination equaling the zero vector is the trivial solution.

For a matrix with \(n\) columns:

| Condition | Interpretation |
|:---|:---|
| \(\operatorname{rank}(A)=n\) | Linearly Independent |
| \(\operatorname{rank}(A)<n\) | Linearly Dependent |

### Sample Matrix

Consider the following matrix:

\[
A=
\begin{bmatrix}
1&0&1\\
0&1&1
\end{bmatrix}
\]

After row reduction, the matrix has two pivot positions.

Therefore:

- Matrix rank = 2
- Number of columns = 3
- Rank is less than the number of columns.
- The column vectors are linearly dependent.

Their dependence relation is:

\[
\mathbf{v}_1+\mathbf{v}_2-\mathbf{v}_3=\mathbf{0}
\]

This illustrates how matrix rank can be used to determine linear independence.

---

## How to Use Pivot

1. Open the **Solve** section.
2. Select the desired number of rows and columns.
3. Enter the matrix values.
4. Tap the calculate button.
5. Review the matrix rank and independence result.
6. Explore the identified pivot columns.
7. Follow the row operations to understand how the result was obtained.

You can also explore the Learn and Practice sections to reinforce your understanding.

---

## Supported Inputs and Limitations

| Category | Support |
|:---|:---|
| Matrix dimensions | 1×1 to 5×5 |
| Integer values | Supported |
| Decimal values | Supported |
| Fraction values | Supported |
| Determinants | Square matrices |
| Symbolic expressions | Not supported |
| Matrix scanning | Printed integers recommended |

**Calculation accuracy:** Decimal inputs are interpreted as exact fractions. The application checks intermediate integer calculations against JavaScript safe-integer limits. Calculations that exceed these limits display an error rather than returning a potentially rounded result.

---

## Installation

### Download the APK

The latest available Android APK can be obtained from the project's GitHub Releases page.

**[Download Pivot – Latest APK](https://github.com/kraemonie/pivot/releases/latest)**

1. Open the release page.
2. Download the available APK.
3. Open the downloaded file on your Android device.
4. Allow installation from the source if prompted by Android.
5. Complete the installation and launch Pivot.

---

## Build from Source

Want to explore or modify the application?

### Prerequisites

- Android Studio
- Java Development Kit (JDK) 17
- Android SDK 35
- Node.js (for running the mathematical tests)

### Clone the Repository

```bash
git clone https://github.com/kraemonie/pivot.git
```

Navigate to the project directory:

```bash
cd pivot
```

Open the project in Android Studio and synchronize the Gradle files.

### Run Tests

```bash
node --test tests/math.test.cjs
```

### Build the Android APK

**Windows:**

```bash
gradlew.bat assembleDebug lintDebug
```

**macOS / Linux:**

```bash
./gradlew assembleDebug lintDebug
```

### APK Output

After a successful build, the debug APK will be generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

---

## Privacy and Offline Functionality

Pivot is designed to work without requiring an internet connection.

- No Internet permission is required by the application.
- No external online services are needed for its core functionality.
- Matrix scanning uses on-device text recognition.
- Camera photos are stored in the application's private cache.
- Practice scores are saved locally.

This allows students to use the application's learning and calculation features without depending on an active internet connection.

---

## Design and Accessibility

Pivot features a mobile-focused interface designed to keep mathematical content readable and easy to navigate.

- Montserrat typography for the interface.
- Space Grotesk typography for matrix numbers.
- Minimum 12px interface text size.
- Custom matrix dimension selectors.
- Animated interface elements.
- Reduced-motion support.

Both bundled fonts are licensed under the SIL Open Font License (OFL).

---

## Technologies and References

| Component | Technology |
|:---|:---|
| Mobile Platform | Android |
| Build System | Gradle |
| Development Environment | Android Studio |
| Java Runtime | Java 17 |
| Android SDK | API 35 |
| Matrix Recognition | Google ML Kit Text Recognition |
| Mathematical Testing | Node.js Test Runner |

**Recognition documentation:** [Google ML Kit – Text Recognition for Android](https://developers.google.com/ml-kit/vision/text-recognition/v2/android)

---

## Project Information

**Project Title:** Linear Independence and Rank of a Matrix

**Application Name:** Pivot

**Project Type:** Android Educational Application

**Subject:** Linear Algebra

**Purpose:** To provide an interactive learning and calculation tool that demonstrates the concepts of linear independence, linear dependence, matrix rank, and elementary row operations.

---

<div align="center">

### Learn the Process. Understand the Mathematics.

**PIVOT — Make Every Row Operation Count.**

[Download APK](https://github.com/kraemonie/pivot/releases/latest) · [View Source Code](https://github.com/kraemonie/pivot) · [Watch Demo](https://github.com/kraemonie/pivot/blob/main/docs/demo/pivot-showcase-v0.1.5.mp4)

</div>
