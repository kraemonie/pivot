const assert=require('node:assert/strict');
(async()=>{const pages=await(await fetch('http://127.0.0.1:9239/json')).json(),page=pages.find(p=>p.url.includes('/assets/index.html'));assert(page,'Pivot page missing');const ws=new WebSocket(page.webSocketDebuggerUrl),pending=new Map();let id=0;await new Promise((r,j)=>{ws.onopen=r;ws.onerror=j});ws.onmessage=e=>{const m=JSON.parse(e.data);if(pending.has(m.id)){pending.get(m.id)(m);pending.delete(m.id)}};
async function send(method,params){const n=++id,p=new Promise(r=>pending.set(n,r));ws.send(JSON.stringify({id:n,method,params}));const m=await p;if(m.error)throw Error(JSON.stringify(m.error));return m.result;}
async function run(expression){const r=await send('Runtime.evaluate',{expression,returnByValue:true});if(r.exceptionDetails)throw Error(JSON.stringify(r.exceptionDetails));return r.result.value;}
try{
assert.equal(await run("!!window.PivotMath"),true);
if(await run("window.PivotBridge&&PivotBridge.needsWelcome()")){
assert.equal(await run("document.getElementById('welcome').hidden"),false);assert(await run("document.getElementById('welcome').getBoundingClientRect().height>100"));
await run("document.getElementById('beginPivot').click()");
assert.equal(await run("PivotBridge.needsWelcome()"),false);
await send('Page.reload',{});
for(let i=0;i<40;i++){await new Promise(r=>setTimeout(r,100));if(await run("!!document.getElementById('welcome')"))break;}
assert.equal(await run("document.getElementById('welcome').hidden"),true);
console.log('PASS: first-launch welcome, completion and reload persistence');
}
await run("document.getElementById('calculate').click()");assert.equal(await run("document.getElementById('rank').textContent"),'3');assert.equal(await run("document.getElementById('determinant').textContent"),'-24');
await run("document.querySelector('[data-example=\"1\"]').click();document.getElementById('calculate').click()");assert.equal(await run("document.getElementById('verdict').textContent"),'Linearly dependent.');
await run("document.getElementById('nextStep').click()");assert.notEqual(await run("document.getElementById('stepProgress').textContent"),'1 / 1');
await run("document.querySelector('#matrixInput input').value='x';document.getElementById('calculate').click()");assert(await run("document.getElementById('error').textContent.length>0"));
await run("document.querySelector('[data-example=\"0\"]').click();document.getElementById('calculate').click()");
for(const width of [320,360,412]){await send('Emulation.setDeviceMetricsOverride',{width,height:800,deviceScaleFactor:1,mobile:true});for(const section of ['solve','practice','learn']){await run("document.querySelector('[data-page=\""+section+"\"]').click()");assert(await run('document.documentElement.scrollWidth<=innerWidth+1'),'Overflow '+section+' '+width);}}
await run("document.querySelector('[data-page=\"practice\"]').click();document.querySelector('#answers button').click()");assert.equal(await run("document.getElementById('feedback').hidden"),false);assert.equal(await run("Array.from(document.querySelectorAll('#answers button')).every(function(x){return x.disabled})"),true);
await run("document.getElementById('quizExplain').click()");assert.equal(await run("document.getElementById('solve').hidden"),false);
await run("document.querySelector('[data-page=\"practice\"]').click();window.pivotBack()");assert.equal(await run("document.getElementById('solve').hidden"),false);
await send('Emulation.clearDeviceMetricsOverride',{});await run("document.querySelector('[data-example=\"0\"]').click();document.getElementById('calculate').click();window.scrollTo(0,0)");
console.log('PASS: PDF examples, invalid input, row steps, quiz feedback, full solution, back navigation, 320/360/412px layouts');
}finally{ws.close()}})().catch(e=>{console.error(e);process.exit(1)});
