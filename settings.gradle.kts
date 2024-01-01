// Core
include(":lua:base")
include(":lua:builder")
include(":lua:core")
include(":lua:desktop")
include(":lua:teavm")

// Examples
include(":examples:basic:core")
include(":examples:basic:desktop")
include(":examples:basic:teavm")

// #### Use include build to use other project source directly. Just update the source path ####

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

//includeBuild("E:\\Dev\\Projects\\java\\gdx-imgui") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.gdx-imgui:imgui-ext-core")).using(project(":imgui-ext:ext-core"))
//        substitute(module("com.github.xpenatan.gdx-imgui:imgui-ext-desktop")).using(project(":imgui-ext:ext-desktop"))
//        substitute(module("com.github.xpenatan.gdx-imgui:imgui-ext-teavm")).using(project(":imgui-ext:ext-teavm"))
//        substitute(module("com.github.xpenatan.gdx-imgui:gdx-impl")).using(project(":gdx:gdx-impl"))
//    }
//}