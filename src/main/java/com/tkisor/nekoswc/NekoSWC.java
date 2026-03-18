package com.tkisor.nekoswc;

import com.mojang.logging.LogUtils;
import com.tkisor.nekojs.api.compiler.ScriptCompilerRegistry;
import com.tkisor.nekoswc.compiler.SWCCompiler;
import com.tkisor.nekoswc.events.NekoJSEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(NekoSWC.MODID)
public class NekoSWC {
    public static final String MODID = "nekoswc";
    private static final Logger LOGGER = LogUtils.getLogger();

    public NekoSWC(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("[NekoSWC] 正在初始化 TypeScript / JSX 编译引擎...");

        ScriptCompilerRegistry.register(new SWCCompiler());

        NeoForge.EVENT_BUS.addListener(NekoJSEventHandler::onModifyWorkspaceConfig);
    }
}