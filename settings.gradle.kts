// Core
include(":lua:lua-base")
include(":lua:lua-build")
include(":lua:lua-cpp")
include(":lua:lua-core")
include(":lua:lua-desktop")
include(":lua:lua-teavm")

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
//        substitute(module("com.github.xpenatan.jParser:jParser-base")).using(project(":jParser:base"))
//        substitute(module("com.github.xpenatan.jParser:jParser-build")).using(project(":jParser:builder"))
//        substitute(module("com.github.xpenatan.jParser:jParser-core")).using(project(":jParser:core"))
//        substitute(module("com.github.xpenatan.jParser:jParser-cpp")).using(project(":jParser:cpp"))
//        substitute(module("com.github.xpenatan.jParser:jParser-idl")).using(project(":jParser:idl"))
//        substitute(module("com.github.xpenatan.jParser:jParser-teavm")).using(project(":jParser:teavm"))
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