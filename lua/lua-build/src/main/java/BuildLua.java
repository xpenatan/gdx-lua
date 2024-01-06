import com.github.xpenatan.jparser.builder.BuildConfig;
import com.github.xpenatan.jparser.builder.BuildMultiTarget;
import com.github.xpenatan.jparser.builder.BuildTarget;
import com.github.xpenatan.jparser.builder.JBuilder;
import com.github.xpenatan.jparser.builder.targets.EmscriptenTarget;
import com.github.xpenatan.jparser.builder.targets.WindowsTarget;
import com.github.xpenatan.jparser.core.util.FileHelper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BuildLua {

    public static void buildLua() throws Exception {
        String libName = "lua";

        String cppSourceDir = new File("./build/lua/").getCanonicalPath();
        String libsDir = new File("./build/c++/libs/").getCanonicalPath();
        String libBuildPath = new File("./build/c++/").getCanonicalPath();
        String cppDestinationPath = libBuildPath + "/src";
        String libDestinationPath = cppDestinationPath + "/lua";

        FileHelper.copyDir(cppSourceDir, libDestinationPath);

        BuildConfig buildConfig = new BuildConfig(
                cppDestinationPath,
                libBuildPath,
                libsDir,
                libName
        );

        ArrayList<BuildMultiTarget> targets = new ArrayList<>();

        if(BuildTarget.isWindows() || BuildTarget.isUnix()) {
            targets.add(getStaticLuaWindowBuildTarget());
//            targets.add(getEmscriptenStaticBuildTarget());
        }

        JBuilder.build(buildConfig, targets);
    }

    private static BuildMultiTarget getStaticLuaWindowBuildTarget() throws IOException {
        BuildMultiTarget multiTarget = new BuildMultiTarget();

        WindowsTarget windowsTarget = new WindowsTarget();
        windowsTarget.cppCompiler.clear();
        windowsTarget.linkerCompiler.clear();
        windowsTarget.cppCompiler.add("x86_64-w64-mingw32-gcc");
        windowsTarget.linkerCompiler.add("x86_64-w64-mingw32-gcc");
        windowsTarget.cppFlags.remove("-std=c++17");

        windowsTarget.isStatic = true;
        windowsTarget.headerDirs.add("-Isrc/lua/");
        windowsTarget.filterCPPSuffix = ".c";
        windowsTarget.cppInclude.add("**/lua/*.c");
        windowsTarget.cppExclude.add("**/lua/lua.c");
        windowsTarget.cppExclude.add("**/lua/ltests.c");
        windowsTarget.cppExclude.add("**/lua/onelua.c");
        windowsTarget.cppFlags.add("-DLUA_COMPAT_5_3");
        windowsTarget.cppFlags.add("-Dl_noret=void");
//        windowsTarget.cppFlags.add("-DLUA_USE_LONGJMP");
        multiTarget.add(windowsTarget);

        return multiTarget;
    }

    private static BuildMultiTarget getEmscriptenStaticBuildTarget() {
        BuildMultiTarget multiTarget = new BuildMultiTarget();

        // Make a static library
        EmscriptenTarget libTarget = new EmscriptenTarget(null);
//        libTarget.cppCompiler.clear();
//        libTarget.linkerCompiler.clear();
//        String cppCompilerr = EmscriptenTarget.EMSCRIPTEN_ROOT + "emcc";
//        if(BuildTarget.isWindows()) {
//            cppCompilerr += ".bat";
//        }
//        libTarget.cppCompiler.add(cppCompilerr);
//        libTarget.linkerCompiler.add(cppCompilerr);
//        libTarget.cppFlags.remove("-std=c++17");

        libTarget.isStatic = true;
        libTarget.compileGlueCode = false;
        libTarget.headerDirs.add("-Isrc/lua");
        libTarget.filterCPPSuffix = ".c";
        libTarget.cppInclude.add("**/lua/*.c");
        libTarget.cppExclude.add("**/lua/lua.c");
        libTarget.cppExclude.add("**/lua/ltests.c");
        libTarget.cppExclude.add("**/lua/onelua.c");
        libTarget.cppFlags.add("-DLUA_COMPAT_5_3");
        libTarget.cppFlags.add("-Dl_noret=void");
//        libTarget.cppFlags.add("-DLUA_USE_LONGJMP");
        multiTarget.add(libTarget);

        return multiTarget;
    }
}