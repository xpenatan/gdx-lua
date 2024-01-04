val moduleName = "lua-desktop"

val windowsFile = "$projectDir/../lua-build/build/c++/libs/windows/gdx_lua64.dll"

tasks.jar {
    from(windowsFile)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            from(components["java"])
        }
    }
}