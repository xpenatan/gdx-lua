val moduleName = "lua-core"

val genPath = File("$projectDir/../lua-gen/src/main/java")
println("Path " + genPath.canonicalPath)
sourceSets["main"].java {
    srcDir(genPath)
}

dependencies {
    implementation("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")
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