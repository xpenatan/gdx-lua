import com.github.xpenatan.jparser.builder.BuildConfig;
import com.github.xpenatan.jparser.builder.BuildMultiTarget;
import com.github.xpenatan.jparser.builder.BuildTarget;
import com.github.xpenatan.jparser.builder.JBuilder;
import com.github.xpenatan.jparser.builder.targets.EmscriptenTarget;
import com.github.xpenatan.jparser.builder.targets.WindowsTarget;
import com.github.xpenatan.jparser.core.JParser;
import com.github.xpenatan.jparser.core.util.FileHelper;
import com.github.xpenatan.jparser.cpp.CppCodeParser;
import com.github.xpenatan.jparser.cpp.CppGenerator;
import com.github.xpenatan.jparser.cpp.NativeCPPGenerator;
import com.github.xpenatan.jparser.idl.IDLReader;
import com.github.xpenatan.jparser.teavm.TeaVMCodeParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        BuildLua.buildLua();
        buildGdxLua();
    }

    private static void buildGdxLua() throws Exception {
        String libName = "gdx_lua";
        String basePackage = "lua";

        String luaPath = new File("./../").getCanonicalPath().replace("\\", "/");
        String luaBasePath = luaPath + "/lua-base";
        String luaBuildPath = luaPath + "/lua-build";
        String luaCorePath = luaPath + "/lua-gen";
        String luaTeavmPath = luaPath + "/lua-teavm";

        String idlPath = luaBuildPath + "/src/main/cpp/lua.idl";
        IDLReader idlReader = IDLReader.readIDL(idlPath);
        String baseJavaDir = luaBasePath + "/src/main/java";

        String cppSourceDir = luaBuildPath + "/build/sol2/";
        String cppBuildPath = luaBuildPath + "/build/c++";
        String libsDir = cppBuildPath + "/libs/";
        String cppDestinationPath = cppBuildPath + "/src";
        String libDestinationPath = cppDestinationPath + "/lib";

        Path copyOut = new File(libDestinationPath).toPath();
        FileHelper.copyDir(new File("src/main/cpp/cpp-source/custom").toPath(), copyOut);

        {
            CppGenerator cppGenerator = new NativeCPPGenerator(libDestinationPath, false);
            CppCodeParser cppParser = new CppCodeParser(cppGenerator, idlReader, basePackage, null) {
                @Override
                public String getIDLMethodName(String name) {
                    return getMethod(name);
                }
            };
            cppParser.generateClass = true;
            JParser.generate(cppParser, baseJavaDir, luaCorePath + "/src/main/java");
        }
        {
            TeaVMCodeParser teavmParser = new TeaVMCodeParser(idlReader, libName, basePackage, null) {
                @Override
                public String getIDLMethodName(String name) {
                    return getMethod(name);
                }
            };
            JParser.generate(teavmParser, baseJavaDir, luaTeavmPath + "/src/main/java/");
        }

        ArrayList<BuildMultiTarget> targets = new ArrayList<>();
        if(BuildTarget.isWindows() || BuildTarget.isUnix()) {
            targets.add(getWindowBuildTarget(cppBuildPath));
            targets.add(getEmscriptenStaticBuildTarget(idlReader));
        }

        BuildConfig buildConfig = new BuildConfig(cppDestinationPath, cppBuildPath, libsDir, libName);
        JBuilder.build(buildConfig, targets);
    }

    private static String getMethod(String name) {
        if(name.startsWith("x_")) {
            name = name.replace("x_", "");
        }
        return name;
    }

    private static BuildMultiTarget getWindowBuildTarget(String cppBuildPath) throws IOException {
        BuildMultiTarget multiTarget = new BuildMultiTarget();

        WindowsTarget windowsTarget = new WindowsTarget();
        windowsTarget.addJNIHeaders();
        windowsTarget.headerDirs.add("-Isrc/lib");
        windowsTarget.headerDirs.add("-Isrc/lua");
        windowsTarget.cppInclude.add(cppBuildPath + "/src/jniglue/JNIGlue.cpp");
        windowsTarget.linkerFlags.add(cppBuildPath + "/libs/windows/lua64.a");
        multiTarget.add(windowsTarget);
        return multiTarget;
    }

    private static BuildMultiTarget getEmscriptenStaticBuildTarget(IDLReader idlReader) {
        BuildMultiTarget multiTarget = new BuildMultiTarget();

        EmscriptenTarget linkTarget = new EmscriptenTarget(idlReader);
        linkTarget.headerDirs.add("-Isrc/lib");
        linkTarget.headerDirs.add("-Isrc/lua");
        linkTarget.headerDirs.add("-includesrc/lib/LuaCustom.h");
        linkTarget.linkerFlags.add("../../libs/emscripten/lua.a");
        multiTarget.add(linkTarget);

        return multiTarget;
    }
}