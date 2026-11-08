const fs=require('node:fs');
(async()=>{
 const port=9239,pages=await(await fetch('http://127.0.0.1:'+port+'/json')).json(),page=pages.find(p=>p.url.includes('/assets/index.html'));if(!page)throw Error('Pivot is not open');
 const ws=new WebSocket(page.webSocketDebuggerUrl),pending=new Map();let id=0;await new Promise((r,j)=>{ws.onopen=r;ws.onerror=j});ws.onmessage=e=>{const m=JSON.parse(e.data);const h=pending.get(m.id);if(h){pending.delete(m.id);h(m)}};
 const started=Date.now(),scenes=[];const delay=ms=>new Promise(r=>setTimeout(r,ms));
 async function run(expression){const n=++id;const p=new Promise((resolve,reject)=>{const timeout=setTimeout(()=>reject(Error('CDP timeout')),10000);pending.set(n,m=>{clearTimeout(timeout);resolve(m)})});ws.send(JSON.stringify({id:n,method:'Runtime.evaluate',params:{expression,returnByValue:true,userGesture:true}}));const m=await p;if(m.error||m.result.exceptionDetails)throw Error(JSON.stringify(m));return m.result.result.value;}
 function scene(title,subtitle){const at=(Date.now()-started)/1000;scenes.push({at,title,subtitle});console.log(title);}
 try{
 scene('Meet Pivot.','Linear algebra, one clear step at a time.');await delay(4500);
 await run("document.getElementById('beginPivot').click()");scene('Start with your numbers.','Enter integers, decimals or fractions. Shape the matrix to fit your problem.');await delay(2000);
 await run("document.querySelector('.input-card').scrollIntoView({behavior:'smooth',block:'start'})");await delay(2800);
 await run("document.getElementById('rowsPicker').click()");await delay(2400);await run("document.querySelectorAll('#dimensionOptions button')[2].click()");await delay(1000);
 await run("document.getElementById('calculate').click();document.querySelector('.result-card').scrollIntoView({behavior:'smooth',block:'start'})");scene('Every column counts.','Find the rank, identify pivot columns and check linear independence.');await delay(4600);
 await run("document.getElementById('stepsCard').scrollIntoView({behavior:'smooth',block:'start'})");scene('Follow the logic.','Step through row operations. Move forward or back at your own pace.');await delay(2000);
 for(let i=0;i<3;i++){await run("document.getElementById('nextStep').click()");await delay(1400);}await run("document.getElementById('prevStep').click()");await delay(1800);
 await run("document.querySelector('[data-example=\"2\"]').click();document.getElementById('calculate').click();document.querySelector('.result-card').scrollIntoView({behavior:'smooth',block:'start'})");scene('See what dependence means.','A nonzero combination of columns reveals why they are dependent.');await delay(5000);
 await run("document.querySelector('[data-page=\"practice\"]').click()");scene('Build your confidence.','36 shuffled practice questions. Get an explanation after every answer.');await delay(1800);await run("document.querySelector('.quiz-card').scrollIntoView({behavior:'smooth',block:'start'})");await delay(2200);await run("document.querySelector('#answers button').click()");await delay(1600);await run("document.getElementById('feedback').scrollIntoView({behavior:'smooth',block:'center'})");await delay(2400);
 await run("document.querySelector('[data-page=\"learn\"]').click();document.querySelector('#learn .card').scrollIntoView({behavior:'smooth',block:'center'})");scene('Make the ideas click.','Independence, rank and pivots—explained in a pocket learning guide.');await delay(2600);
 await run("document.querySelectorAll('#learn .card')[1].scrollIntoView({behavior:'smooth',block:'center'})");await delay(2200);await run("document.querySelector('#learn .callout').scrollIntoView({behavior:'smooth',block:'center'})");await delay(2600);
 await run("document.querySelector('.brand').click()");scene('Your next move starts here.','Solve. Practice. Learn.\nPivot for Android · v0.1.3');await delay(5000);
 fs.writeFileSync('artifacts/showcase/scenes.json',JSON.stringify({scenes,duration:(Date.now()-started)/1000},null,2));
 }finally{ws.close();}
})().catch(e=>{console.error(e);process.exit(1)});
