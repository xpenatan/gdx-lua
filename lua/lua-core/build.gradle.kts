val moduleName = "lua-core"

val includePom = configurations.create("includePom")
includePom.extendsFrom(configurations.implementation.get())
val genPath = File("$projectDir/../lua-gen/src/main/java")
println("Path " + genPath.canonicalPath)
sourceSets["main"].java {
    srcDir(genPath)
}

dependencies {
    implementation("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")
}

tasks.named("clean") {
    doFirst {
        val srcPath = "$projectDir/src/main/java"
        project.delete(files(srcPath))
    }
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