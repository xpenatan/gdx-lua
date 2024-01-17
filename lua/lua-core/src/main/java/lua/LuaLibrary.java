package lua;

import lua.register.ImportListener;
import lua.register.LuaCreateClass;

public class LuaLibrary {

    public static final String LUA_J_HASH = "java_hash";
    private static final String TEMPLATE = "Template";
    private static final String CLASS = "Class";

    /**
     * Get or create the table and push to stack.
     * If parent exist, it will create or get as a child.
     * If parent don't exist, it will create a global table.
     */
    private static void getOrCreateTable(Lua lua, String table) {
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

    private static void getOrCreateTables(Lua lua, String fullPath) {
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
            lua.registerFunction(lastItem, function);
        }
        luaState.lua_pop(-1); // remove table from stack
    }

    /**
     * Will create tables and keep the last table into stack
     */
    public static void getOrCreateClass(Lua lua, Class<?> clazz) {
        String name = clazz.getName();
        getOrCreateTables(lua, name);
    }

    /**
     * Get and push meta table to top of stack. Return false if it doesn't exist. <br>
     * It's your responsibility to pop the table from stack.
     */
    public static boolean getMetaClass(Lua lua, String metaTable) {
        LuaState luaState = lua.luaState;
        luaState.luaL_getmetatable(metaTable);
        if(luaState.lua_isnil(-1) != 0) {
            luaState.lua_pop(-1); // Pop nil
            return false;
        }
        return true;
    }

    /**
     * Get and push class table to top of stack. Return false if it doesn't exist. <br>
     * It's your responsibility to pop the table from stack.
     */
    public static boolean getMetaClassTable(LuaTableType type, Lua lua, String metaTable) {
        if(getMetaClass(lua, metaTable)) {
            LuaState luaState = lua.luaState;
            if(type == LuaTableType.CLASS) {
                luaState.lua_getfield(-1, CLASS);
                luaState.lua_remove(-2); // Remove metatable
                return true;
            }
            else if(type == LuaTableType.TEMPLATE) {
                luaState.lua_getfield(-1, TEMPLATE);
                luaState.lua_remove(-2); // Remove metatable
                return true;
            }
        }
        return false;
    }

    /**
     * Add class function. MetaClass must exist
     */
    public static boolean registerClassFunction(LuaTableType type, Lua lua, Class<?> clazz, String functionName, LuaFunction function) {
        LuaState luaState = lua.luaState;
        String name = clazz.getName();
        if(getMetaClassTable(type, lua, name)) {
            if(lua.registerFunction(functionName, function)) {
                luaState.lua_pop(-1);
                return true;
            }
        }
        return false;
    }

    public static boolean setClassString(LuaTableType type, Lua lua, Class<?> clazz, String variableName, String value) {
        LuaState luaState = lua.luaState;
        String name = clazz.getName();
        if(getMetaClassTable(type, lua, name)) {
            luaState.lua_pushstring(value);
            luaState.lua_setfield(-2, variableName);
            luaState.lua_pop(-1);
            return true;
        }
        return false;
    }

    public static boolean setClassInt(LuaTableType type, Lua lua, Class<?> clazz, String variableName, int value) {
        LuaState luaState = lua.luaState;
        String name = clazz.getName();
        if(getMetaClassTable(type, lua, name)) {
            luaState.lua_pushinteger(value);
            luaState.lua_setfield(-2, variableName);
            luaState.lua_pop(-1);
            return true;
        }
        return false;
    }

    public static boolean setClassFloat(LuaTableType type, Lua lua, Class<?> clazz, String variableName, float value) {
        LuaState luaState = lua.luaState;
        String name = clazz.getName();
        if(getMetaClassTable(type, lua, name)) {
            luaState.lua_pushnumber(value);
            luaState.lua_setfield(-2, variableName);
            luaState.lua_pop(-1);
            return true;
        }
        return false;
    }

    public static boolean registerMetaClassTableNewIndex(LuaTableType type, Lua lua, Class<?> clazz, LuaFunction luaFunction) {
        if(getMetaClassTable(type, lua, clazz.getName())) {
            if(lua.registerFunction("__newindex", luaFunction)) {
                lua.luaState.lua_pop(-2);
                return true;
            }
        }
        return false;
    }

    public static void registerClassNewFunction(Lua lua, Class<?> clazz, LuaCreateClass listener) {
        String metaTable = clazz.getName();
        if(getMetaClassTable(LuaTableType.CLASS, lua, metaTable)) {
            // Put new function inside class table
            {
                lua.registerFunction("new", new LuaFunction() {
                    @Override
                    public int onCall(LuaState luaState) {
                        System.out.println("NEW IS CALLED");
                        Object classObject = listener.onCreateClass(luaState);
                        if(classObject == null) {
                            luaState.luaL_error("Class object cannot be null");
                            return 0;
                        }

                        luaState.lua_newtable();

                        // Put hash key
                        {
                            int objectHash = classObject.hashCode();
                            lua.addObjectInstance(objectHash, classObject);
                            luaState.lua_pushinteger(objectHash);
                            luaState.lua_setfield(-2, LUA_J_HASH);
                        }

                        //Put metatable to the new created table
                        {
                            getMetaClassTable(LuaTableType.TEMPLATE, lua, metaTable);
                            luaState.lua_setmetatable(-2); // Set Template metatable to new table
                        }
                        return 1;
                    }
                });
            }
            lua.luaState.lua_pop(-1); // pop metatable
        }
    }

    /**
     * Register MetaClass. Use lua code 'MYCLASS = java.import("class full path")' to obtain lua class object.
     */
    public static void registerMetaClass(Lua lua, Class<?> clazz) {
        String metaTable = clazz.getName();

        createMetaTableAndTemplate(lua, metaTable);

        lua.addImportListener(metaTable, new ImportListener() {
            @Override
            public int onImport(LuaState luaState) {
                // Return a metatable
                if(!getMetaClassTable(LuaTableType.CLASS, lua, metaTable)) {
                    luaState.luaL_error("Metatable don't exist");
                }
                luaState.lua_pushvalue(-1);
                luaState.lua_setmetatable(-2); // Set Template metatable to new table
                return 1;
            }
        });
    }

    private static void createMetaTableAndTemplate(Lua lua, String metaTable) {
        LuaState luaState = lua.luaState;
        luaState.luaL_newmetatable(metaTable);

        {
            luaState.lua_newtable(); // Class table

            // mt.__index = mt
            {
                luaState.lua_pushvalue(-1); // copy MetaTable reference
                luaState.lua_setfield(-2, "__index");
            }

            // Set private metatable
            {
                luaState.lua_pushstring("Private");
                luaState.lua_setfield(-2, "__metatable");
            }

            luaState.lua_setfield(-2, CLASS); // set Class to metatable
        }

        {
            luaState.lua_newtable(); // Template table

            // mt.__index = mt
            {
                luaState.lua_pushvalue(-1); // copy Template reference
                luaState.lua_setfield(-2, "__index");
            }

            // Set private metatable
            {
                luaState.lua_pushstring("Private");
                luaState.lua_setfield(-2, "__metatable");
            }

            // Garbage collector
            {
                lua.registerFunction("__gc", new LuaFunction() {
                    @Override
                    public int onCall(LuaState luaState) {
                        luaState.lua_getfield(-1, LUA_J_HASH);
                        if(luaState.lua_isinteger(-1) != 0) {
                            int hash = luaState.lua_tointeger(-1);
                            lua.removeObjectInstance(hash);
                        }
                        return 0;
                    }
                });
            }
            luaState.lua_setfield(-2, TEMPLATE); // set Template to metatable
        }

        luaState.lua_pop(-1);
    }
}