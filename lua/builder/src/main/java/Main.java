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
//        buildLua();
        buildGdxLua();
    }

    private static void buildLua() throws Exception {
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
        windowsTarget.cppCompiler.clear();
        windowsTarget.linkerCompiler.clear();
        windowsTarget.cppCompiler.add("x86_64-w64-mingw32-gcc");
        windowsTarget.linkerCompiler.add("x86_64-w64-mingw32-gcc");
        windowsTarget.isStatic = true;
        windowsTarget.addJNI = false;
        windowsTarget.headerDirs.add("-Isrc/lua/");
        windowsTarget.filterCPPSuffix = ".c";
        windowsTarget.cppInclude.add("**/lua/*.c");
        windowsTarget.cppExclude.add("**/lua/lua.c");
        windowsTarget.cppExclude.add("**/lua/ltests.c");
        windowsTarget.cppExclude.add("**/lua/onelua.c");
        windowsTarget.cppFlags.add("-std=gnu99");
        windowsTarget.cppFlags.add("-DLUA_BUILD_AS_DLL");
        windowsTarget.cppFlags.add("-DLUA_COMPAT_5_3");
        windowsTarget.cppFlags.remove("-std=c++17");
        multiTarget.add(windowsTarget);

        WindowsTarget linkTarget = new WindowsTarget();
        linkTarget.cppCompiler.clear();
        linkTarget.linkerCompiler.clear();
        linkTarget.cppCompiler.add("x86_64-w64-mingw32-gcc");
        linkTarget.linkerCompiler.add("x86_64-w64-mingw32-gcc");
        linkTarget.headerDirs.add("-Isrc/lua/");
        linkTarget.filterCPPSuffix = ".c";
        linkTarget.shouldLink = true;
        linkTarget.shouldCompile = true;
        linkTarget.cppInclude.add("**/lua/lua.c");
        linkTarget.addJNI = false;
        linkTarget.linkerFlags.add(buildPath + "/libs/windows/lua64.a");
        linkTarget.cppFlags.remove("-std=c++17");
        multiTarget.add(linkTarget);

        return multiTarget;
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
        String libPath = buildPath + "/libs/windows";

        WindowsTarget windowsTarget = new WindowsTarget();
        windowsTarget.isStatic = true;
        windowsTarget.addJNI = false;
        windowsTarget.headerDirs.add("-Isrc/sol2");
        windowsTarget.headerDirs.add("-Isrc/lua/");
        windowsTarget.cppInclude.add("**/sol2/*.cpp");
        multiTarget.add(windowsTarget);

        WindowsTarget glueTarget = new WindowsTarget();
        glueTarget.headerDirs.add("-Isrc/sol2/");
        glueTarget.headerDirs.add("-Isrc/lua/");
        glueTarget.linkerFlags.add("-L" + libPath);
        glueTarget.linkerFlags.add("-l:lua64.a");
        glueTarget.linkerFlags.add("-l:gdx-lua64.a");
        multiTarget.add(glueTarget);

//        windowsTarget.linkerFlags.add("-L" + libPath);
//        windowsTarget.linkerFlags.add("-l:lua64.a");
//        windowsTarget.linkerFlags.add("-l:liblua-5.4.4.dll");
//        windowsTarget.linkerFlags.add(libPath + "/lua64.a");
//        multiTarget.add(windowsTarget);
        return multiTarget;
    }
}