import com.github.xpenatan.jparser.builder.BuildConfig;
import com.github.xpenatan.jparser.builder.BuildMultiTarget;
import com.github.xpenatan.jparser.builder.BuildTarget;
import com.github.xpenatan.jparser.builder.JBuilder;
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
        }

        JBuilder.build(buildConfig, targets);
    }

    private static BuildMultiTarget getStaticLuaWindowBuildTarget() throws IOException {
        String buildPath = new File("build/c++/").getCanonicalPath().replace("\\", "/");
        BuildMultiTarget multiTarget = new BuildMultiTarget();

        WindowsTarget windowsTarget = new WindowsTarget();
        windowsTarget.isStatic = true;
        windowsTarget.addJNI = false;
        windowsTarget.headerDirs.add("-Isrc/lua/");
        windowsTarget.filterCPPSuffix = ".c";
        windowsTarget.cppInclude.add("**/lua/*.c");
        windowsTarget.cppExclude.add("**/lua/lua.c");
        windowsTarget.cppExclude.add("**/lua/ltests.c");
        windowsTarget.cppExclude.add("**/lua/onelua.c");
        windowsTarget.cppFlags.add("-DLUA_COMPAT_5_3");
        multiTarget.add(windowsTarget);

        WindowsTarget linkTarget = new WindowsTarget();
        linkTarget.headerDirs.add("-Isrc/lua/");
        linkTarget.filterCPPSuffix = ".c";
        linkTarget.cppInclude.add("**/lua/lua.c");
        linkTarget.addJNI = false;
        linkTarget.linkerFlags.add(buildPath + "/libs/windows/lua64.a");
        multiTarget.add(linkTarget);

        return multiTarget;
    }
}