# Initial verification

- Nine numerical test groups pass, including both PDF examples, all 625 matrices with 2x2 entries from -2 to 2, fractions, zero matrices, Unicode minus and unsafe-number rejection.
- Android debug assembly and Android lint passed. Final CSS spacing fix was rebuilt successfully.
- Android 9/API28 emulator checks passed: PDF calculations, input errors, row steps, quiz feedback, full-solution navigation and Back behavior.
- Solve, Practice and Learn fit widths 320, 360 and 412 CSS pixels without page overflow.
- Emulator crash buffer was empty. Visual inspection performed on the Solve screen.
- All bundled APK assets match the final source.
- Calculations use exact safe-integer fractions; oversized intermediate results are rejected. No symbolic algebra parser.

## 0.1.1
Vector arrows replace Unicode arrow glyphs. Added native first-launch welcome state. Android 9 emulator: welcome display, completion, reload persistence, calculator, quiz and responsive-layout checks passed. Build and lint passed; final scroll-focus adjustment rebuilt.

## 0.1.2
- Android debug build, Android test build and lint passed.
- Native OCR instrumentation test passed on Android 9: rendered 2x2 bitmap recognized as rows 1 2 / 3 4.
- Eleven numerical/parser test groups passed.
- Emulator checks passed: custom dimension pickers, all 36 quiz entries/categories, scan review validation and computation, minimum 12px visible text, calm input focus, layouts at 320/360/412px.
- Directional step classes verified; external camera launched and cancellation restored scan controls.
- Manifest inspected: no Internet or Camera permission requested by Pivot. System camera app handles capture.
- Camera recognition of real paper/handwriting varies and awaits user phone feedback. OCR confirmation is required.

## 0.1.3
- Android build, Android test build and lint passed.
- Native tests: printed-matrix OCR and numeric-only row filtering passed (2 tests).
- Eleven numerical/parser checks passed.
- Emulator checks passed for operator alignment at 320/360/412px, vertically centered picker, Learn reveal on two separate visits, scan review, quiz and minimum 12px text.
- New SVG matrix branding and gentle header floating motion; reduced motion remains supported.
- Portrait showcase uses a new media path and explicitly sized 9:16 README preview.
