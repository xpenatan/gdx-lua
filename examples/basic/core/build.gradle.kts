dependencies {
    implementation(project(":lua:core"))

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-core:${LibExt.gdxImGuiVersion}")
    implementation("com.github.xpenatan.gdx-imgui:gdx-impl:${LibExt.gdxImGuiVersion}")
}