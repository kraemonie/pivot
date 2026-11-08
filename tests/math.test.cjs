const test=require('node:test'),assert=require('node:assert/strict');
const M=require('../web/math.js');
test('PDF square example has rank three and determinant -24',()=>{const r=M.solve([[2,2,-1],[4,0,2],[0,6,-3]]);assert.equal(r.rank,3);assert.equal(r.determinant,'-24');assert.equal(r.independent,true)});
test('PDF rectangular example has three pivots and dependent columns',()=>{const r=M.solve([[1,0,2,1],[0,2,4,2],[0,2,2,1]]);assert.equal(r.rank,3);assert.deepEqual(r.pivots,[0,1,2]);assert.equal(r.independent,false);assert.equal(r.determinant,null)});
test('dependent columns return a verifiable nonzero relation',()=>{const a=[[1,2,3],[2,4,6]];const r=M.solve(a);assert.equal(r.rank,1);assert(r.relation.some(x=>x!=='0'));for(const row of a)assert.equal(row.reduce((s,v,j)=>M.add(s,M.mul(String(v),r.relation[j])),'0'),'0')});
test('zero matrix and rectangular full column rank',()=>{assert.equal(M.solve([[0,0],[0,0]]).rank,0);assert.equal(M.solve([[1,0],[0,1],[1,1]]).independent,true)});
test('exact decimal fractions do not create spurious pivots',()=>{assert.equal(M.solve([['0.1','0.3'],['1/5','3/5']]).rank,1);assert.equal(M.solve([['1/2',0],[0,'-2/3']]).determinant,'-1/3')});
test('invalid and oversized values rejected',()=>{for(const x of ['1/0','x','1+2','Infinity',''])assert.throws(()=>M.solve([[x]]));assert.throws(()=>M.solve([[1],[1,2]]))});
test('copied mathematical minus is accepted',()=>{assert.equal(M.solve([['−2/3']]).determinant,'-2/3')});

test('unsafe integers and cancellation intermediates rejected',()=>{assert.throws(()=>M.solve([['9007199254740993']]));assert.throws(()=>M.add('9007199254740991/2','-9007199254740991/3'));});
