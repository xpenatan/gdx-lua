package lua;

public class LuaLibrary {

    /**
     * Get or create the table and push to stack.
     * If parent exist, it will create or get as a child.
     * If parent don't exist, it will create a global table.
     */
    public static void getOrCreateTable(Lua lua, String table) {
        LuaState luaState = lua.luaState;
        boolean hasParentTable = luaState.lua_istable(-1) != 0;
        if(!hasParentTable) {
            luaState.lua_getglobal(table);
            hasParentTable = luaState.lua_istable(-1) != 0;
            if(!hasParentTable) {
                lua.luaState.lua_pop(1); // remove nill from stack
                luaState.lua_newtable();
                luaState.lua_setglobal(table);
                luaState.lua_getglobal(table);
            }
            return;
        }
        luaState.lua_getfield(-1, table);
        boolean containsTable = luaState.lua_istable(-1) != 0;
        if(!containsTable) {
            lua.luaState.lua_pop(1); // remove nill from stack
            luaState.lua_pushstring(table);
            luaState.lua_newtable();
            luaState.lua_settable(-3);
            luaState.lua_getfield(-1, table);
            //swap tables and pop the parent table so the child is at the top.
            luaState.lua_rotate(-2, 1);
            lua.luaState.lua_pop(1);
        }
    }

    public static void getOrCreateTables(Lua lua, String fullPath) {
        LuaState luaState = lua.luaState;
        String[] split = fullPath.split("\\.");
        for(int i = 0; i < split.length; i++) {
            String name = split[i];
            getOrCreateTable(lua, name);
        }
    }

    public static void addLibrary(Lua lua, String fullPackage, LuaFunction function) {
        LuaState luaState = lua.luaState;
        String[] split = fullPackage.split("\\.");
        String lastItem = split[split.length-1];
        String lastPath = "." + lastItem;
        String fullPath = fullPackage.replace(lastPath, "");
        getOrCreateTables(lua, fullPath);

        {
            // Add function to table on top of the stack
            luaState.lua_pushstring(lastItem);
            luaState.lua_pushcfunction(function);
            luaState.lua_settable(-3);
        }
        luaState.lua_pop(-1); // remove table from stack
    }
}
