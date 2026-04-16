package com.tkisor.nekoswc.compiler;

import java.util.regex.Pattern;

public class ModuleTransformer {

    private static final String ID = "([\\p{L}\\p{N}_$]+)";

    // import "lib";
    private static final Pattern IMPORT_SIDE_EFFECT = Pattern.compile("(?m)^\\s*import\\s+(['\"][^'\"]+['\"])\\s*;?");

    // import { 变量a, 变量b } from "lib";
    private static final Pattern IMPORT_DEFAULT_AND_NAMED = Pattern.compile("(?m)^\\s*import\\s+" + ID + "\\s*,\\s*\\{([\\s\\S]*?)\\}\\s+from\\s+(['\"][^'\"]+['\"])\\s*;?");

    // import { a, 变量 as 别名 } from "lib";
    private static final Pattern IMPORT_NAMED = Pattern.compile("(?m)^\\s*import\\s+\\{([\\s\\S]*?)\\}\\s+from\\s+(['\"][^'\"]+['\"])\\s*;?");

    // import 核心类 from "lib";
    private static final Pattern IMPORT_DEFAULT = Pattern.compile("(?m)^\\s*import\\s+" + ID + "\\s+from\\s+(['\"][^'\"]+['\"])\\s*;?");

    // import * as 核心模块 from "lib";
    private static final Pattern IMPORT_NAMESPACE = Pattern.compile("(?m)^\\s*import\\s+\\*\\s+as\\s+" + ID + "\\s+from\\s+(['\"][^'\"]+['\"])\\s*;?");

    // export const 映射 = {};
    private static final Pattern EXPORT_VAR = Pattern.compile("(?m)^\\s*export\\s+(const|let|var)\\s+" + ID + "\\s*=");

    // export function 打印() / export class 玩家类
    private static final Pattern EXPORT_FUNC_CLASS = Pattern.compile("(?m)^\\s*export\\s+(async\\s+function|function|class)\\s+" + ID);

    // export default ...
    private static final Pattern EXPORT_DEFAULT = Pattern.compile("(?m)^\\s*export\\s+default\\s+");

    // export { 变量a, 变量b as 别名c };
    private static final Pattern EXPORT_BLOCK = Pattern.compile("(?m)^\\s*export\\s+\\{([\\s\\S]*?)\\}\\s*;?");

    public static String transform(String code) {
        if (code == null || code.isEmpty()) return code;
        String result = code;

        result = IMPORT_SIDE_EFFECT.matcher(result).replaceAll("require($1);");

        result = IMPORT_DEFAULT_AND_NAMED.matcher(result).replaceAll(match -> {
            String def = match.group(1);
            String names = match.group(2).replaceAll("\\s+as\\s+", ":");
            String module = match.group(3);
            return "const " + def + " = require(" + module + ").default || require(" + module + "); const {" + names + "} = require(" + module + ");";
        });

        result = IMPORT_NAMED.matcher(result).replaceAll(match -> {
            String names = match.group(1).replaceAll("\\s+as\\s+", ":");
            String module = match.group(2);
            return "const {" + names + "} = require(" + module + ");";
        });

        result = IMPORT_DEFAULT.matcher(result).replaceAll("const $1 = require($2).default || require($2);");
        result = IMPORT_NAMESPACE.matcher(result).replaceAll("const $1 = require($2);");

        result = EXPORT_VAR.matcher(result).replaceAll("$1 $2 = exports.$2 =");

        result = EXPORT_FUNC_CLASS.matcher(result).replaceAll("exports.$2 = $1 $2");

        result = EXPORT_DEFAULT.matcher(result).replaceAll("exports.default = ");

        result = EXPORT_BLOCK.matcher(result).replaceAll(match -> {
            String fullMatch = match.group(0);
            String block = match.group(1);
            String[] exports = block.split(",");

            StringBuilder sb = new StringBuilder();
            for (String exp : exports) {
                exp = exp.trim();
                if (exp.isEmpty()) continue;

                String[] parts = exp.split("\\s+as\\s+");
                String local = parts[0];
                String exported = parts.length > 1 ? parts[1] : parts[0];
                sb.append("exports.").append(exported).append(" = ").append(local).append("; ");
            }

            long newlines = fullMatch.chars().filter(ch -> ch == '\n').count();
            for (int i = 0; i < newlines; i++) {
                sb.append("\n");
            }
            return sb.toString();
        });

        return result;
    }
}