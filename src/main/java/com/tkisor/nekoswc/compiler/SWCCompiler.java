package com.tkisor.nekoswc.compiler;

import com.caoccao.javet.swc4j.Swc4j;
import com.caoccao.javet.swc4j.enums.Swc4jMediaType;
import com.caoccao.javet.swc4j.enums.Swc4jParseMode;
import com.caoccao.javet.swc4j.enums.Swc4jSourceMapOption;
import com.caoccao.javet.swc4j.options.Swc4jTranspileOptions;
import com.caoccao.javet.swc4j.outputs.Swc4jTranspileOutput;
import com.tkisor.nekojs.api.compiler.IScriptCompiler;
import com.tkisor.nekojs.core.error.SourceMapRegistry;
import com.tkisor.nekojs.core.fs.NekoJSPaths;

import java.net.URL;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SWCCompiler implements IScriptCompiler {
    private static final Swc4j SWC = new Swc4j();
    private static final Pattern MAPPINGS_PATTERN = Pattern.compile("\"mappings\"\\s*:\\s*\"([^\"]+)\"");

    @Override
    public boolean canCompile(String extension) {
        return extension.equals(".ts") || extension.equals(".tsx") || extension.equals(".jsx") || extension.equals(".js");
    }

    @Override
    public String compile(Path file, String sourceCode) throws Exception {
        URL specifier = file.toUri().toURL();
        String fileName = file.getFileName().toString();

        Swc4jMediaType mediaType;
        if (fileName.endsWith(".tsx")) mediaType = Swc4jMediaType.Tsx;
        else if (fileName.endsWith(".jsx")) mediaType = Swc4jMediaType.Jsx;
        else if (fileName.endsWith(".ts")) mediaType = Swc4jMediaType.TypeScript;
        else mediaType = Swc4jMediaType.JavaScript;

        Swc4jTranspileOptions options = new Swc4jTranspileOptions()
                .setSpecifier(specifier)
                .setMediaType(mediaType)
                .setParseMode(Swc4jParseMode.Module)
                .setSourceMap(Swc4jSourceMapOption.Separate)
                .setInlineSources(true);

        Swc4jTranspileOutput output = SWC.transpile(sourceCode, options);

        String jsCode = ModuleTransformer.transform(output.getCode());

        String sourceMapJson = output.getSourceMap();
        if (sourceMapJson != null) {
            Matcher m = MAPPINGS_PATTERN.matcher(sourceMapJson);
            if (m.find()) {
                String mappings = m.group(1);
                String relativePath = NekoJSPaths.ROOT.relativize(file).toString().replace('\\', '/');
                SourceMapRegistry.register(relativePath, mappings);
            }
        }

        return jsCode;
    }
}