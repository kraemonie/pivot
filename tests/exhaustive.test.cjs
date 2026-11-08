const test=require('node:test'),assert=require('node:assert/strict');
const M=require('../web/math.js');
test('all small 2x2 matrices agree with determinant rank criterion',()=>{for(let a=-2;a<=2;a++)for(let b=-2;b<=2;b++)for(let c=-2;c<=2;c++)for(let d=-2;d<=2;d++){const r=M.solve([[a,b],[c,d]]),det=a*d-b*c;assert.equal(r.determinant,String(det));assert.equal(r.rank,det!==0?2:[a,b,c,d].some(Boolean)?1:0);if(r.relation)for(const row of [[a,b],[c,d]])assert.equal(row.reduce((s,v,j)=>M.add(s,M.mul(String(v),r.relation[j])),'0'),'0');}});
