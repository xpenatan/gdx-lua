plugins {
    id("org.gretty") version("3.1.0")
}

gretty {
    contextPath = "/"
    extraResourceBase("build/dist/webapp")
}

dependencies {
    implementation(project(":examples:basic:core"))

    implementation(project(":lua:teavm"))

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation("com.github.xpenatan.gdx-teavm:backend-teavm:${LibExt.gdxTeaVMVersion}")
    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-teavm:${LibExt.gdxImGuiVersion}")
}

val mainClassName = "lua.example.basic.Build"

tasks.register<JavaExec>("basic-build-teavm") {
    group = "example-teavm"
    description = "Build basic example"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("basic-run-teavm") {
    group = "example-teavm"
    description = "Run teavm app"
    val list = listOf("basic-build-teavm", "jettyRun")
    dependsOn(list)

    tasks.findByName("jettyRun")?.mustRunAfter("basic-build-teavm")
}