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

## 0.1.4
- Applied the supplied video's purple, lime, pink, cyan and yellow palette, bold typography, outlined controls and solid offset shadows. Existing page structure and all three Learn lessons remain intact.
- Added matching app branding, adaptive launcher artwork and an Android 13+ monochrome icon. Android system bars follow the active section and welcome screen.
- Ten existing math and scan-parser tests passed.
- Browser checks passed for matrix dimensions, scan review, PDF examples, invalid input, row operations, quiz feedback, solution navigation, Learn re-entry and 320/360/412px layouts. Visible text remains at least 12px, and reduced motion remains supported.
- Reviewed fresh welcome, Solve, Practice, Learn, row-step and desktop screenshots. Verified that the SVG brand image decodes successfully.
- Android debug assembly and lint passed. All 11 bundled web assets match the final source; adaptive and themed icon resources are present in the APK.
- Installed the update on the connected Android 15 phone. On-device checks passed for the brand image, section backgrounds, viewport fit, original Learn content, rank 3 and determinant −24 for the PDF square example. Practice score was preserved.

## 0.1.5
- Bundled open-licensed Montserrat, using weight 900 for the reference video's heavy geometric headings and wordmark. Matrix numbers keep Space Grotesk.
- Repeated browser checks passed at 320/360/412px with minimum 12px text, quiz and scan feedback, row steps, Learn replay and solution navigation. Captured the actual rank-3 result, row steps, successful independence quiz and original Learn lesson for the launch video.
- Android debug build and lint passed with the new font; the final APK includes 13 web assets.
- Installed versionCode 6 / v0.1.5 on the phone and repeated the icon, theme, lesson, calculator, viewport and score-preservation smoke checks successfully.
