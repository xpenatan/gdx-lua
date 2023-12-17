#pragma once

#include <sol/sol.hpp>

#if defined(_MSC_VER) && _MSC_VER <= 1500 // MSVC 2008 or earlier
#include <stddef.h>     // intptr_t
#else
#include <stdint.h>     // intptr_t
#endif


class Lua {

    public:
        static void script() {
            sol::state lua;
            lua.open_libraries(sol::lib::base);
            lua.script("print('hello world')");
        }
};