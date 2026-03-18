package com.tkisor.nekoswc.compiler;

import com.caoccao.javet.swc4j.Swc4j;
import com.caoccao.javet.swc4j.enums.Swc4jMediaType;
import com.caoccao.javet.swc4j.enums.Swc4jParseMode;
import com.caoccao.javet.swc4j.options.Swc4jTranspileOptions;
import com.caoccao.javet.swc4j.outputs.Swc4jTranspileOutput;
import com.tkisor.nekojs.api.compiler.IScriptCompiler;

import java.net.URL;
import java.nio.file.Path;

public class SWCCompiler implements IScriptCompiler {
    private static final Swc4j SWC = new Swc4j();

    @Override
    public boolean canCompile(String extension) {
        return extension.equals(".ts")
                || extension.equals(".tsx")
                || extension.equals(".jsx");
    }

    @Override
    public String compile(Path file, String sourceCode) throws Exception {
        URL specifier = file.toUri().toURL();
        String fileName = file.getFileName().toString();

        Swc4jMediaType mediaType;
        if (fileName.endsWith(".tsx")) {
            mediaType = Swc4jMediaType.Tsx;
        } else if (fileName.endsWith(".jsx")) {
            mediaType = Swc4jMediaType.Jsx;
        } else if (fileName.endsWith(".ts")) {
            mediaType = Swc4jMediaType.TypeScript;
        } else {
            mediaType = Swc4jMediaType.JavaScript;
        }

        Swc4jTranspileOptions options = new Swc4jTranspileOptions()
                .setSpecifier(specifier)
                .setMediaType(mediaType)
                .setParseMode(Swc4jParseMode.Module);

        Swc4jTranspileOutput output = SWC.transpile(sourceCode, options);
        return output.getCode();
    }
}