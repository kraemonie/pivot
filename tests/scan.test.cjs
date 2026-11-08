const test=require('node:test'),assert=require('node:assert/strict');const P=require('../web/scan-parser.js');
test('reviewed scan preserves rows, negatives and fractions',()=>{assert.deepEqual(P.parse('[ 1  -2  1/2 ]\n[ 0  3  4 ]'),[['1','-2','1/2'],['0','3','4']]);});
test('scan requires a rectangular matrix and valid values',()=>{for(const s of ['','1 2\n3','1 O\n0 1','1/0','1 2 3 4 5 6'])assert.throws(()=>P.parse(s));});
