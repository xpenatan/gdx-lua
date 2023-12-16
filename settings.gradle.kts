// Core
include(":lua:base")
include(":lua:builder")
include(":lua:core")
include(":lua:desktop")


//includeBuild("E:\\Dev\\Projects\\java\\gdx-teavm") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.gdx-teavm:backend-teavm")).using(project(":backends:backend-teavm"))
//    }
//}

//includeBuild("E:\\Dev\\Projects\\java\\jParser") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.jParser:base")).using(project(":jParser:base"))
//        substitute(module("com.github.xpenatan.jParser:builder")).using(project(":jParser:builder"))
//        substitute(module("com.github.xpenatan.jParser:core")).using(project(":jParser:core"))
//        substitute(module("com.github.xpenatan.jParser:cpp")).using(project(":jParser:cpp"))
//        substitute(module("com.github.xpenatan.jParser:idl")).using(project(":jParser:idl"))
//        substitute(module("com.github.xpenatan.jParser:teavm")).using(project(":jParser:teavm"))
//        substitute(module("com.github.xpenatan.jParser:loader-core")).using(project(":jParser:loader:loader-core"))
//        substitute(module("com.github.xpenatan.jParser:loader-teavm")).using(project(":jParser:loader:loader-teavm"))
//    }
//}