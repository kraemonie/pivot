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
