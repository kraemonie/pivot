import subprocess,time
from pathlib import Path
adb=r'C:\Users\Kris\AppData\Local\Android\Sdk\platform-tools\adb.exe'
cmd=[adb,'-s','emulator-5556']
proc=subprocess.Popen(cmd+['shell','screenrecord','--bit-rate','4000000','--time-limit','80','/sdcard/Download/pivot-showcase-raw.mp4'],creationflags=subprocess.CREATE_NO_WINDOW)
time.sleep(1)
try:
 subprocess.run(['node','scripts/record-showcase.cjs'],check=True)
finally:
 p=subprocess.run(cmd+['shell','pidof','screenrecord'],capture_output=True,text=True).stdout.strip()
 for pid in p.split():
  if pid.isdigit():subprocess.run(cmd+['shell','kill','-2',pid],check=False)
 proc.wait(timeout=20)
subprocess.run(cmd+['pull','/sdcard/Download/pivot-showcase-raw.mp4','artifacts/showcase/raw.mp4'],check=True)
