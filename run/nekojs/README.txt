=== NekoJS 脚本目录说明 ===
- startup_scripts: 游戏启动时加载，用于注册物品、方块。修改后需要重启游戏。
- server_scripts: 存档/服务器加载时运行，用于配方、事件监听。可使用 /reload 重载。
- client_scripts: 仅在客户端运行，用于 GUI、按键绑定等。
- probe: 存放 NekoJS 自动生成的类型声明文件 (.d.ts)，请勿手动修改。