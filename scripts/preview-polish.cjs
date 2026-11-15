const fs = require('node:fs');
const path = require('node:path');
const http = require('node:http');
const { spawn } = require('node:child_process');

(async () => {
  const base = path.resolve('web');
  const output = path.resolve(process.argv[2] || 'artifacts/ui-polish');
  fs.mkdirSync(output, { recursive: true });
  const { pathToFileURL } = require('node:url');
  const cache = path.join(process.env.LOCALAPPDATA,'npm-cache','_npx');
  const modulePaths = fs.readdirSync(cache).map(dir => path.join(cache,dir,'node_modules'));
  const puppeteer = await import(pathToFileURL(require.resolve('puppeteer-core',{paths:modulePaths})).href);
  const types = { '.html':'text/html', '.js':'text/javascript', '.css':'text/css', '.ttf':'font/ttf', '.svg':'image/svg+xml' };
  const server = http.createServer((req,res) => {
    const filename = path.resolve(base, '.' + decodeURIComponent(req.url.split('?')[0]).replace(/^\/assets/, ''));
    if (!filename.startsWith(base + path.sep)) { res.writeHead(403).end(); return; }
    fs.readFile(filename, (err, data) => {
      if (err) { res.writeHead(404).end(); return; }
      res.writeHead(200, { 'Content-Type':types[path.extname(filename)] || 'application/octet-stream' });res.end(data);
    });
  });
  await new Promise(resolve => server.listen(8137,'127.0.0.1',resolve));
  const browser = await puppeteer.default.launch({ executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe', headless:true, args:['--remote-debugging-port=9239'], userDataDir:path.join(output,'chrome-profile') });
  try {
    const page = await browser.newPage();
    const errors = [];
    page.on('pageerror', error => errors.push(error.message));
    await page.setViewport({ width:412,height:915,deviceScaleFactor:2,isMobile:true });
    await page.goto('http://127.0.0.1:8137/assets/index.html');
    await page.evaluate(() => document.fonts.ready);
    await page.waitForFunction(() => Array.from(document.images).every(img => img.complete && img.naturalWidth > 0));
    await new Promise(resolve => setTimeout(resolve, 500));
    await page.screenshot({path:path.join(output,'welcome.png')});
    if (await page.$eval('#welcome', el => !el.hidden)) await page.click('#beginPivot');
    await new Promise(r => setTimeout(r,400));
    await page.screenshot({path:path.join(output,'mobile.png'),fullPage:true});
    await page.$eval('.input-card', el => el.scrollIntoView({block:'start'}));
    await page.$eval('.input-card', el => el.style.scrollMarginTop='0px');
    await (await page.$('.input-card')).screenshot({path:path.join(output,'card-input.png')});
    await page.screenshot({path:path.join(output,'input.png')});
    await page.click('#calculate');
    await page.$eval('.result-card', el => el.scrollIntoView({block:'start'}));
    await new Promise(r => setTimeout(r,400));
    await (await page.$('.result-card')).screenshot({path:path.join(output,'card-result.png')});
    await page.screenshot({path:path.join(output,'result.png')});
    await page.$eval('#stepsCard', el => el.scrollIntoView({block:'start'}));
    await page.click('#nextStep');
    await new Promise(r => setTimeout(r,400));
    await (await page.$('#stepsCard')).screenshot({path:path.join(output,'card-steps.png')});
    await page.click('#nextStep');
    await new Promise(r => setTimeout(r,400));
    await (await page.$('#stepsCard')).screenshot({path:path.join(output,'card-steps-next.png')});
    await page.screenshot({path:path.join(output,'steps.png')});
    await page.click('[data-page="practice"]');
    await page.$eval('.quiz-card', el => el.scrollIntoView({block:'start'}));
    await page.click('#answers button');
    await new Promise(r => setTimeout(r,400));
    await page.screenshot({path:path.join(output,'practice.png')});
    await page.click('[data-page="learn"]');
    await page.$eval('#learn', el => el.scrollIntoView({block:'start'}));
    await new Promise(r => setTimeout(r,800));
    await page.screenshot({path:path.join(output,'learn.png')});
    await page.setViewport({width:1440,height:1100,deviceScaleFactor:1,isMobile:false});
    await page.click('[data-page="solve"]');
    await new Promise(r => setTimeout(r,450));
    await page.screenshot({path:path.join(output,'desktop.png'),fullPage:true});
    // Reuse the project's full existing browser checks against the same app.
    if (!process.argv.includes('--screenshots-only')) {
      await page.emulateMediaFeatures([{name:'prefers-reduced-motion',value:'reduce'}]);
      await new Promise((resolve,reject) => {
        const check = spawn(process.execPath,['scripts/qa-webview.cjs'],{stdio:'inherit'});
        check.on('exit', code => code === 0 ? resolve() : reject(Error('Browser QA failed: '+code)));
      });
    }
    if (errors.length) throw Error('Browser errors: ' + errors.join('\n'));
    console.log('Screenshots saved to ' + output);
  } finally { await browser.close(); server.close(); }
})().catch(err => { console.error(err);process.exitCode=1; });
