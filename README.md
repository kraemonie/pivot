
<div align="center">

# PIVOT

### Understand the Pivots. Discover the Rank. Test Independence.

**An Offline Android Learning Application for Linear Independence and Matrix Rank**

<p>
  <a href="https://github.com/kraemonie/pivot/releases/latest">
    <img src="https://img.shields.io/badge/Download-Android%20APK-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Download Android APK">
  </a>
  <a href="https://github.com/kraemonie/pivot/blob/main/docs/demo/pivot-showcase-v0.1.5.mp4">
    <img src="https://img.shields.io/badge/Watch-App%20Demo-FF5252?style=for-the-badge&logo=youtube&logoColor=white" alt="Watch app demo">
  </a>
</p>

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Android SDK](https://img.shields.io/badge/Android%20SDK-35-3DDC84?logo=android&logoColor=white)
![Offline](https://img.shields.io/badge/Works-Offline-3977D5)
![Project](https://img.shields.io/badge/Project-Linear%20Algebra-8067D8)

</div>

---

## About Pivot

**Pivot** is an offline Android application developed as a learning and problem-solving tool for two fundamental Linear Algebra topics: **Linear Independence** and **Rank of a Matrix**.

The app allows students to enter matrices, perform row operations, identify pivot columns, calculate matrix rank, and determine whether the columns of a matrix are linearly independent or dependent.

Pivot goes beyond simply displaying answers. It supports learning through step-by-step row reduction, concept lessons, practice quizzes, and camera-assisted matrix input—all in a mobile application that can be used offline.

---

## See Pivot in Action

<div align="center">
  <a href="docs/demo/pivot-showcase-v0.1.5.mp4">
    <img src="docs/demo/pivot-preview-v0.1.5.gif" width="300" height="533" alt="Pivot app demo preview">
  </a>
  <p><em>Tap the preview to watch the full app demonstration.</em></p>
</div>

---

## Features at a Glance

| Feature | What it does |
| --- | --- |
| **Matrix Solver** | Enter matrices with dimensions from 1×1 up to 5×5. |
| **Linear Independence Checker** | Determine whether the columns of a matrix are linearly independent or dependent. |
| **Matrix Rank Calculator** | Calculate rank by identifying pivot positions after row reduction. |
| **Step-by-Step Row Reduction** | Follow the elementary row operations used to simplify a matrix. |
| **Pivot Identification** | Identify pivot columns and understand their role in determining rank. |
| **Dependence Relation** | Display a nontrivial linear dependence relation when the columns are dependent. |
| **Determinant Calculator** | Calculate the determinant of square matrices. |
| **Practice Quizzes** | Answer shuffled questions and receive immediate feedback with explanations. |
| **Learn Module** | Review the concepts behind matrix rank and linear independence. |
| **Matrix Scanner** | Capture or import a matrix image and recognize its entries using on-device text recognition. |

---

## Explore the App

### 1. Solve

Use the matrix solver to calculate and examine the properties of a matrix.

- Choose the matrix dimensions.
- Enter integer, decimal, or fraction values.
- Calculate the matrix rank.
- Check the linear independence or dependence of the columns.
- View pivot columns and dependence relations.
- Calculate determinants for square matrices.
- Follow the row-reduction process step by step.

### 2. Learn

Review the mathematical ideas behind the app's calculations. The learning section introduces the concepts of matrix rank, pivots, linear independence, and linear dependence.

### 3. Practice

Test your understanding through interactive practice questions.

- Shuffled practice questions.
- Questions covering matrix rank and linear independence.
- Immediate answer feedback.
- Explanations that reinforce the correct concepts.
- Locally saved practice scores.

### 4. Scan

Use the device camera or import an image to help enter a matrix.

- Capture a printed matrix.
- Import an existing image.
- Recognize text on the device using Google ML Kit.
- Review and correct the recognized entries before solving.

> **Scanning tip:** Printed integers generally produce the most reliable results. Handwriting, stacked fractions, and complex brackets may require manual correction.

---

## The Mathematics Behind Pivot

Pivot connects two important ideas in Linear Algebra: the number of pivot positions in a matrix and the linear independence of its columns.

### Matrix Rank

The **rank** of a matrix is the number of pivot positions in its row-echelon form or reduced row-echelon form.

\[
\operatorname{rank}(A)=\text{number of pivots}
\]

### Linear Independence

A set of vectors is **linearly independent** when the equation

\[
c_1\mathbf{v}_1+c_2\mathbf{v}_2+\cdots+c_n\mathbf{v}_n=\mathbf{0}
\]

has only the trivial solution:

\[
c_1=c_2=\cdots=c_n=0
\]

If a nontrivial solution exists, the vectors are linearly dependent.

### How Rank Determines Independence

For a matrix \(A\) with \(n\) columns:

| Condition | Result |
| --- | --- |
| \(\operatorname{rank}(A)=n\) | The columns are linearly independent. |
| \(\operatorname{rank}(A)<n\) | The columns are linearly dependent. |

### Worked Example

Consider the matrix:

\[
A=
\begin{bmatrix}
1&0&1\\
0&1&1
\end{bmatrix}
\]

Its reduced row-echelon form is:

\[
\operatorname{RREF}(A)=
\begin{bmatrix}
1&0&1\\
0&1&1
\end{bmatrix}
\]

There are two pivot positions, so:

\[
\operatorname{rank}(A)=2
\]

The matrix has three columns. Since its rank is less than its number of columns, the columns are linearly dependent.

A dependence relation is:

\[
\mathbf{v}_1+\mathbf{v}_2-\mathbf{v}_3=\mathbf{0}
\]

This example demonstrates how row reduction and matrix rank can be used to determine linear independence.

---

## How to Use Pivot

1. Open the **Solve** section.
2. Select the number of rows and columns.
3. Enter the matrix values.
4. Run the calculation.
5. Review the matrix rank and independence result.
6. Examine the pivot columns and dependence relation, if applicable.
7. Follow the row operations to understand how the result was obtained.

You can also visit **Learn** to review the concepts or **Practice** to test your understanding.

---

## Supported Inputs and Limitations

| Category | Support |
|:---|:---|
| Matrix dimensions | 1×1 to 5×5 |
| Integer entries | Supported |
| Decimal entries | Supported |
| Fraction entries | Supported |
| Determinants | Square matrices |
| Symbolic expressions and variables | Not supported |
| Matrix scanning | Printed integers recommended; manual correction may be needed |

**Calculation note:** Decimal inputs are represented as exact fractions during calculation. Intermediate integer calculations are checked against JavaScript safe-integer limits so the app can report values that exceed its supported exact range instead of silently returning rounded results.

---

## Installation

### Download the Android APK

Get the latest available build from the GitHub Releases page:

**[Download Pivot APK](https://github.com/kraemonie/pivot/releases/latest)**

1. Open the Releases page.
2. Download the available APK file.
3. Open the downloaded file on your Android device.
4. If prompted, allow installation from that source in Android settings.
5. Finish installing and launch Pivot.

---

## Build from Source

Developers who want to inspect or modify the application can build it locally.

### Requirements

- Android Studio
- Java Development Kit (JDK) 17
- Android SDK 35
- Node.js (for the mathematical test suite)

### Clone the Repository

```bash
git clone https://github.com/kraemonie/pivot.git
cd pivot
```

Open the project in Android Studio and allow Gradle to synchronize.

### Run the Mathematical Tests

```bash
node --test tests/math.test.cjs
```

### Build and Lint the Android App

**Windows**

```bat
gradlew.bat assembleDebug lintDebug
```

**macOS / Linux**

```bash
./gradlew assembleDebug lintDebug
```

After a successful debug build, the APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

---

## Offline Use and Privacy

Pivot is designed to provide its core learning and calculation features without requiring an internet connection.

- Matrix calculations run locally.
- Core learning and practice features are available offline.
- Matrix text recognition is performed on-device using Google ML Kit.
- Practice progress is stored locally.

---

## Technology Stack

| Component | Technology |
|:---|:---|
| Mobile platform | Android |
| Programming language | Java 17 |
| Build system | Gradle |
| Development environment | Android Studio |
| Android SDK | API 35 |
| Matrix text recognition | Google ML Kit Text Recognition |
| Mathematical tests | Node.js Test Runner |

**Text recognition reference:** [Google ML Kit – Text Recognition for Android](https://developers.google.com/ml-kit/vision/text-recognition/v2/android)

---

## Project Information

| Item | Details |
|:---|:---|
| Application name | Pivot |
| Project topic | Linear Independence and Rank of a Matrix |
| Project type | Android educational application |
| Subject | Linear Algebra |
| Primary purpose | To help students explore, calculate, and understand matrix rank and linear independence. |

---

<div align="center">

### Learn the Process. Understand the Mathematics.

**PIVOT — Make Every Row Operation Count.**

[Download APK](https://github.com/kraemonie/pivot/releases/latest) · [View Source Code](https://github.com/kraemonie/pivot) · [Watch Demo](https://github.com/kraemonie/pivot/blob/main/docs/demo/pivot-showcase-v0.1.5.mp4)

</div>
