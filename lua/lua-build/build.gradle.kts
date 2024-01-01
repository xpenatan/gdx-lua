import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.support.unzipTo

plugins {
    id("de.undercouch.download") version("5.5.0")
}

val mainClassName = "Main"

dependencies {
    implementation(project(":lua:lua-base"))
    implementation("com.github.xpenatan.jParser:core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:builder:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:teavm:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:cpp:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:idl:${LibExt.jParserVersion}")
}

tasks.register<JavaExec>("build_project") {
    dependsOn("classes")
    group = "lua"
    description = "Generate and build native project"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

val zippedPath = "$buildDir/lua-source.zip"
val sourcePath = "$buildDir/lua-source"
val sourceDestination = "$buildDir/lua/"

tasks.register<Download>("download_source_lua") {
    group = "lua"
    description = "Download lua source"
    src("https://github.com/lua/lua/archive/refs/tags/v5.4.4.zip")
    dest(File(zippedPath))
    doLast {
        unzipTo(File(sourcePath), dest)
        copy{
            from("$sourcePath/lua-5.4.4")
            into(sourceDestination)
        }
        delete(sourcePath)
        delete(zippedPath)
    }
}

val solZippedPath = "$buildDir/sol2/sol/"

tasks.register("download_source_sol") {
    group = "lua"
    description = "Download sol2 source"
    doLast {
        download.run {
            src("https://github.com/ThePhD/sol2/releases/download/v3.3.0/sol.hpp")
            dest("$solZippedPath/sol.hpp")
        }
        download.run {
            src("https://github.com/ThePhD/sol2/releases/download/v3.3.0/config.hpp")
            dest("$solZippedPath/config.hpp")
        }
        download.run {
            src("https://github.com/ThePhD/sol2/releases/download/v3.3.0/forward.hpp")
            dest("$solZippedPath/forward.hpp")
        }
    }
}

tasks.register("download_source") {
    group = "lua"
    description = "Download source"
    dependsOn(arrayOf("download_source_sol", "download_source_lua"))
}