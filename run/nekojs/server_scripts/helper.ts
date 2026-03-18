// helper.ts (只负责定义和导出，绝对不要写 console.log 或执行逻辑)
const MOD_NAME: string = "NekoJS";

function calculateDamage(base: number, multiplier: number): number {
    return base * multiplier;
}

class PlayerDummy {
    private playerName: string;

    constructor(name: string) {
        this.playerName = name;
    }

    public greet(): string {
        return `Hello, ${this.playerName}! 欢迎来到现代化的 JS 魔改世界！`;
    }
}

// 使用 CommonJS 导出
module.exports = {
    MOD_NAME,
    calculateDamage,
    PlayerDummy
};