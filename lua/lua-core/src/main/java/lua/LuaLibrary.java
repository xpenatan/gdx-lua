package lua;

public class LuaLibrary {

    public static void addLibrary(Lua lua, String fullPackage, LuaFunction function, boolean test) {
        LuaState luaState = lua.luaState;
        String[] split = fullPackage.split("\\.");

        String firstTable = split[0];
        String lastItem = split[split.length-1];
        luaState.lua_getglobal(firstTable);

        boolean tableFound = luaState.lua_istable(-1) != 0;

        if(!tableFound) {
            luaState.lua_newtable();
        }
        for(int i = 1; i < split.length - 1; i++) {
            String name = split[i];

//            if(test) {
//                luaState.lua_getfield(-1, name);
//                lua.printStack();
//            }

            luaState.lua_pushstring(name);
            luaState.lua_newtable();
        }

        luaState.lua_pushstring(lastItem);
        luaState.lua_pushcfunction(function);
        luaState.lua_settable(-3);

        for(int i = 1; i < split.length - 1; i++) {
            String name = split[i];
            luaState.lua_settable(-3);
        }
        if(!tableFound) {
            luaState.lua_setglobal(firstTable);
        }
    }

    public static void test(Lua lua) {
        LuaState luaState = lua.luaState;
        luaState.lua_newtable();
        String dump1;
        String dump2;
        String dump3;
        String dump4;
        String dump5;
        String dump6;
        String dump7;
        String dump8;
        dump1 = lua.dumpStack();
        {
            luaState.lua_pushstring("lib");
            luaState.lua_newtable();
            dump2 = lua.dumpStack();
            {
                luaState.lua_pushstring("test");
                luaState.lua_newtable();
                dump3 = lua.dumpStack();
                {
                    int index = luaState.lua_gettop();
                    luaState.lua_pushstring("import");
                    luaState.lua_pushcfunction(new LuaFunction() {
                        @Override
                        public int onCall(LuaState luaState) {
                            int params = luaState.lua_gettop();
                            boolean argError = false;

                            if(params != 1) {
                                argError = true;
                            }
                            else {
                                int argType = luaState.lua_type(1);
                                if(argType != LuaTypes.LUA_TSTRING) {
                                    argError = true;
                                }
                            }
                            if(argError) {
                                luaState.luaL_error("Only accept a single String argument");
                                return 0;
                            }

                            System.out.println("AAAAA");

                            return 1;
                        }
                    });
                    dump4 = lua.dumpStack();
                    luaState.lua_settable(-3);
                    dump5 = lua.dumpStack();
                }
                luaState.lua_settable(-3);
                dump6 = lua.dumpStack();
            }
            luaState.lua_settable(-3);
            dump7 = lua.dumpStack();
        }
        luaState.lua_setglobal("java");
        dump8 = lua.dumpStack();
        System.out.println();
    }

}
