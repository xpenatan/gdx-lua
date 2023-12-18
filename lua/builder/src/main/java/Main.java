import com.github.xpenatan.jparser.builder.BuildConfig;
import com.github.xpenatan.jparser.builder.BuildMultiTarget;
import com.github.xpenatan.jparser.builder.BuildTarget;
import com.github.xpenatan.jparser.builder.JBuilder;
import com.github.xpenatan.jparser.builder.targets.WindowsTarget;
import com.github.xpenatan.jparser.core.JParser;
import com.github.xpenatan.jparser.core.util.FileHelper;
import com.github.xpenatan.jparser.cpp.CppCodeParser;
import com.github.xpenatan.jparser.cpp.CppGenerator;
import com.github.xpenatan.jparser.cpp.NativeCPPGenerator;
import com.github.xpenatan.jparser.idl.IDLReader;
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
        String libName = "gdx-lua";
        String basePackage = "lua";

        String idlPath = new File("src/main/cpp/lua.idl").getCanonicalPath();
        IDLReader idlReader = IDLReader.readIDL(idlPath);
        String baseJavaDir = new File(".").getAbsolutePath() + "./base/src/main/java";
        String genDir = "../core/src/main/java";

        String cppSourceDir = new File("./build/sol2/").getCanonicalPath();
        String libsDir = new File("./build/c++/libs/").getCanonicalPath();
        String libBuildPath = new File("./build/c++/").getCanonicalPath();
        String cppDestinationPath = libBuildPath + "/src";
        String libDestinationPath = cppDestinationPath + "/sol2";

        FileHelper.copyDir(cppSourceDir, libDestinationPath);

        CppGenerator cppGenerator = new NativeCPPGenerator(libDestinationPath, false);
        CppCodeParser cppParser = new CppCodeParser(cppGenerator, idlReader, basePackage, cppSourceDir);
        cppParser.generateClass = true;
        JParser.generate(cppParser, baseJavaDir, genDir);

        Path copyOut = new File(libDestinationPath).toPath();
        FileHelper.copyDir(new File("src/main/cpp/cpp-source/custom").toPath(), copyOut);

        BuildConfig buildConfig = new BuildConfig(
                cppDestinationPath,
                libBuildPath,
                libsDir,
                libName
        );

        ArrayList<BuildMultiTarget> targets = new ArrayList<>();

        if(BuildTarget.isWindows() || BuildTarget.isUnix()) {
            targets.add(getWindowBuildTarget());
        }

        JBuilder.build(buildConfig, targets);
    }

    private static BuildMultiTarget getWindowBuildTarget() throws IOException {
        BuildMultiTarget multiTarget = new BuildMultiTarget();

        String buildPath = new File("build/c++/").getCanonicalPath().replace("\\", "/");
        WindowsTarget windowsTarget = new WindowsTarget();
        windowsTarget.addJNI = true;
        windowsTarget.headerDirs.add("-Isrc/sol2");
        windowsTarget.headerDirs.add("-Isrc/lua");
        windowsTarget.linkerFlags.add(buildPath + "/libs/windows/lua64.a");
        windowsTarget.cppFlags.add("-DSOL_API_LINKAGE");
        windowsTarget.cppFlags.add("-DSOL_C_FUNCTION_LINKAGE");
        windowsTarget.cppFlags.add("-DSOL_USE_LUA_HPP");
        multiTarget.add(windowsTarget);
        return multiTarget;
    }
}