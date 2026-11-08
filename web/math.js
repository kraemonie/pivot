(function(root){
'use strict';
const abs=x=>x<0?-x:x;
function gcd(a,b){a=abs(a);b=abs(b);while(b){const t=a%b;a=b;b=t;}return a||1;}
function q(n,d=1){if(!Number.isSafeInteger(n)||!Number.isSafeInteger(d))throw Error('These values exceed exact arithmetic limits. Try smaller numbers or simpler fractions.');if(!d)throw Error('A denominator cannot be zero.');if(d<0){n=-n;d=-d;}const g=gcd(n,d);return [n/g,d/g];}
function parse(value){const s=String(value).trim().replace(/−/g,'-');if(!s||s.length>24)throw Error('Enter a number or fraction, up to 24 characters.');if(!/^[+-]?(?:\d+(?:\.\d+)?|\.\d+)(?:\/[+-]?\d+)?$/.test(s))throw Error('Use numbers, decimals or fractions such as -2/3.');const parts=s.split('/'),v=parts[0],digits=(v.split('.')[1]||'').length;return q(Number(v.replace('.','')),10**digits*(parts[1]?Number(parts[1]):1));}
const safe=x=>{if(!Number.isSafeInteger(x))throw Error('These values exceed exact arithmetic limits. Try smaller numbers or simpler fractions.');return x;};
const add=(a,b)=>q(safe(a[0]*b[1])+safe(b[0]*a[1]),a[1]*b[1]),mul=(a,b)=>q(a[0]*b[0],a[1]*b[1]),neg=a=>[-a[0],a[1]],div=(a,b)=>q(a[0]*b[1],a[1]*b[0]);
const str=a=>a[1]===1?String(a[0]):a[0]+'/'+a[1];
function solve(input){if(!Array.isArray(input)||!input.length||input.length>5||!input[0].length||input[0].length>5||input.some(r=>r.length!==input[0].length))throw Error('Choose a rectangular matrix with 1 to 5 rows and columns.');
 const a=input.map(row=>row.map(parse)),m=a.length,n=a[0].length,pivots=[],steps=[];let row=0,det=q(1);
 const snapshot=label=>steps.push({label,matrix:a.map(r=>r.map(str))});snapshot('Original matrix');
 for(let col=0;col<n&&row<m;col++){
  let k=row;while(k<m&&!a[k][col][0])k++;if(k===m)continue;
  if(k!==row){[a[row],a[k]]=[a[k],a[row]];det=neg(det);snapshot('Swap R'+(row+1)+' and R'+(k+1));}
  const pivot=a[row][col];det=mul(det,pivot);
  if(str(pivot)!=='1'){a[row]=a[row].map(x=>div(x,pivot));snapshot('R'+(row+1)+' ÷ ('+str(pivot)+')');}
  for(let i=0;i<m;i++){if(i===row||!a[i][col][0])continue;const factor=a[i][col];a[i]=a[i].map((x,j)=>add(x,neg(mul(factor,a[row][j]))));snapshot('R'+(i+1)+' − ('+str(factor)+') × R'+(row+1));}
  pivots.push(col);row++;
 }
 let relation=null;if(row<n){const free=Array.from({length:n},(_,i)=>i).find(i=>!pivots.includes(i));const x=Array.from({length:n},()=>q(0));x[free]=q(1);pivots.forEach((c,i)=>x[c]=neg(a[i][free]));relation=x.map(str);}
 return {rank:row,pivots,independent:row===n,determinant:m===n?(row===n?str(det):'0'):null,relation,steps,rref:a.map(r=>r.map(str)),rows:m,columns:n};
}
const api={solve,add:(a,b)=>str(add(parse(a),parse(b))),mul:(a,b)=>str(mul(parse(a),parse(b)))};
if(typeof module!=='undefined')module.exports=api;root.PivotMath=api;
})(typeof window==='undefined'?globalThis:window);
