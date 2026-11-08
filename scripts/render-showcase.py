from pathlib import Path
from PIL import Image,ImageDraw,ImageFont
import json,subprocess
root=Path.cwd();tmp=root/'artifacts/showcase';out=root/'docs/demo';out.mkdir(exist_ok=True)
font_path=str(root/'web/fonts/SpaceGrotesk.ttf')
def font(size):return ImageFont.truetype(font_path,size)
base=Image.new('RGB',(1080,1920),'#121C30');d=ImageDraw.Draw(base)
for x in range(18,1080,32):
 for y in range(16,1920,32):d.ellipse((x,y,x+1,y+1),fill='#243344')
d.rounded_rectangle((164,514,916,1826),radius=38,fill='#27384B',outline='#536A55',width=3)
d.line((80,64,72,64,72,140,80,140),fill='#D5F875',width=4)
d.line((154,64,162,64,162,140,154,140),fill='#D5F875',width=4)
for row in range(2):
 for col in range(2):
  x,y=88+col*34,72+row*34
  d.rounded_rectangle((x,y,x+26,y+26),radius=6,fill='#D5F875' if row==col else '#547348')
d.text((190,38),'pivot',font=font(80),fill='#D5F875')
d.text((82,145),'EVERY COLUMN COUNTS',font=font(25),fill='#AFCAA0')
d.line((80,198,1000,198),fill='#314631',width=2)
d.text((180,1855),'ACTUAL ANDROID APP · PIVOT v0.1.2',font=font(24),fill='#AFC3A7')
base.save(tmp/'backdrop.png')
def wrap(text,f,width):
 lines=[]
 for paragraph in text.replace('\\n','\n').split('\n'):
  line=''
  for word in paragraph.split():
   test=(line+' '+word).strip()
   if f.getlength(test)>width and line:lines.append(line);line=word
   else:line=test
  lines.append(line)
 return lines
info=json.loads((tmp/'scenes.json').read_text(encoding='utf-8'))
for i,s in enumerate(info['scenes']):
 im=Image.new('RGBA',(920,300),(0,0,0,0));draw=ImageDraw.Draw(im)
 draw.text((0,0),f'{i+1:02} / {len(info["scenes"]):02}',font=font(24),fill='#D5F875')
 y=42
 for line in wrap(s['title'],font(60),900):draw.text((0,y),line,font=font(60),fill='#F2F4ED');y+=74
 y+=10
 for line in wrap(s['subtitle'],font(30),900):draw.text((0,y),line,font=font(30),fill='#BFCEB9');y+=41
 if y>300:raise ValueError('Caption exceeds portrait layout: '+s['title'])
 im.save(tmp/f'caption-{i}.png')
ffmpeg=r'C:\ffmpeg\ffmpeg-8.1.2-essentials_build\bin\ffmpeg.exe'
args=[ffmpeg,'-hide_banner','-y','-i',str(tmp/'raw.mp4'),'-loop','1','-i',str(tmp/'backdrop.png')]
for i in range(len(info['scenes'])):args+=['-loop','1','-i',str(tmp/f'caption-{i}.png')]
filters=['[0:v]scale=720:1280,setsar=1[phone]','[1:v][phone]overlay=180:530:shortest=1[base]']
prev='base'
for i,s in enumerate(info['scenes']):
 start=0 if i==0 else s['at']+1
 end=info['scenes'][i+1]['at']+1 if i+1<len(info['scenes']) else info['duration']+2
 name=f's{i}';filters.append(f"[{prev}][{i+2}:v]overlay=80:218:enable='between(t,{start:.3f},{end:.3f})'[{name}]");prev=name
args+=['-filter_complex_threads','2','-filter_complex',';'.join(filters),'-map',f'[{prev}]','-an','-t',str(info['duration']+1),'-r','24','-c:v','libx264','-threads','2','-preset','fast','-crf','23','-pix_fmt','yuv420p','-movflags','+faststart',str(out/'pivot-showcase.mp4')]
subprocess.run(args,check=True)
subprocess.run([ffmpeg,'-hide_banner','-y','-ss','17','-i',str(out/'pivot-showcase.mp4'),'-frames:v','1',str(out/'pivot-showcase.jpg')],check=True)
subprocess.run([ffmpeg,'-hide_banner','-y','-ss','14','-t','8','-i',str(out/'pivot-showcase.mp4'),'-filter_complex','fps=8,scale=360:-1:flags=lanczos,split[a][b];[a]palettegen=max_colors=64[p];[b][p]paletteuse=dither=bayer','-loop','0',str(out/'pivot-preview.gif')],check=True)
