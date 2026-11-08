# Pivot showcase media

- pivot-showcase-portrait.mp4: 54-second captioned walkthrough, H.264, 1080 × 1920 (9:16 portrait), 24 fps, silent.
- pivot-preview-portrait.gif: animated excerpt for the repository README, displayed as a tall portrait preview.
- pivot-showcase-portrait.jpg: still cover image.

Recorded from Pivot 0.1.3 on an Android 9 emulator. Captions and background were composed locally with Pillow and FFmpeg. All app screens are actual recordings.

## Re-record

Use an emulator with Pivot's welcome screen visible. Forward its WebView debugging socket to local port 9239. Run scripts/record-showcase.py from the project root, then scripts/render-showcase.py. The recorder targets emulator-5556. Raw footage and scene timing stay under ignored artifacts/showcase. Python with Pillow, Node.js, adb, and FFmpeg are required; adjust local tool paths in the scripts if needed.
