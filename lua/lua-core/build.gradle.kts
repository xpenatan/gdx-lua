val moduleName = "lua-core"

val genPath = File("$projectDir/../lua-cpp/src/main/java")
println("Path " + genPath.canonicalPath)
sourceSets["main"].java {
    srcDir(genPath)
}

dependencies {
    implementation("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
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