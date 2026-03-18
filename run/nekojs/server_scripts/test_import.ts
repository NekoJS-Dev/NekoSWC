// main_test.ts (真正的执行入口)
// 使用 require 导入，TypeScript 依然能完美识别类型！
const { MOD_NAME, calculateDamage, PlayerDummy } = require('./helper.ts');

console.log(`[Import Test] 当前运行的模组是: ${MOD_NAME}`);

const dmg = calculateDamage(5, 1.5);
console.log(`[Import Test] 计算暴击伤害: 5 * 1.5 = ${dmg}`);

const dummy = new PlayerDummy("Steve");
console.log(`[Import Test] 玩家互动: ${dummy.greet()}`);