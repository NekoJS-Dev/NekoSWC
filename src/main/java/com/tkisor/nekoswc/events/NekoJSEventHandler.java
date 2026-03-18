package com.tkisor.nekoswc.events;

import com.tkisor.nekojs.bindings.event.ModifyWorkspaceConfigEvent;

public class NekoJSEventHandler {
    public static void onModifyWorkspaceConfig(ModifyWorkspaceConfigEvent event) {
        event.setFileName("tsconfig.json");
        event.getModel().compilerOptions.jsx = "preserve";
        event.getModel().compilerOptions.moduleResolution = "Node";
    }
}