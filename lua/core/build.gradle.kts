val moduleName = "core"

dependencies {
    implementation("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")
}

tasks.named("clean") {
    doFirst {
        val srcPath = "$projectDir/src/main/java"
        project.delete(files(srcPath))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            from(components["java"])
        }
    }
}