val moduleName = "desktop"

val windowsFile = "$projectDir/../builder/build/c++/libs/windows/gdx_lua64.dll"

tasks.jar {
    from(windowsFile)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            from(components["java"])
        }
    }
}