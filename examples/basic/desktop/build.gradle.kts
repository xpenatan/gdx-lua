dependencies {
    implementation(project(":examples:basic:core"))

    implementation(project(":lua:desktop"))

    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${LibExt.gdxVersion}")
    implementation("com.badlogicgames.gdx:gdx-platform:${LibExt.gdxVersion}:natives-desktop")
}

val mainClassName = "lua.example.basic.Main"
val assetsDir = File("../assets");

tasks.register<JavaExec>("basic-run-desktop") {
    group = "example-desktop"
    description = "Run desktop app"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = assetsDir
}