package lua;

import lua.idl.helper.IDLString;

public class Lua {

    LuaState luaState;

    public Lua() {
        luaState = new LuaState();
        luaState.createContext();
    }

    public void dispose() {
        luaState.destroyContext();
    }

    public LuaState getLuaState() {
        return luaState;
    }

    public void registerGlobalFunction(String name, LuaFunction function) {
        luaState.lua_pushcfunction(function);
        luaState.lua_setglobal(name);
    }

    public String dumpTable(String table) {
        luaState.lua_getglobal(table);
        return dumpTable(table, luaState);
    }

    public String dumpStack() {
        return dumpStack(luaState);
    }

    public ErrorStatus buildScript(String script) {
        ErrorStatus status = ErrorStatus.get();
        luaState.luaL_loadstring(script);
        status.code = luaState.lua_pcall(0, 0, 0);
        status.error = "";
        if(!status.isValid()) {
            IDLString idlString = luaState.lua_tostring(-1);
            status.error = idlString.c_str();
        }
        return status;
    }

    /**
     * Call function
     */
    public ErrorStatus callFunction(int nargs, int nresults, int msgh) {
        ErrorStatus status = ErrorStatus.get();
        status.code = luaState.lua_pcall(nargs, nresults, msgh);
        status.error = "";
        if(!status.isValid()) {
            IDLString idlString = luaState.lua_tostring(-1);
            status.error = idlString.c_str();
        }
        return status;
    }

    /**
     * Return the table log on top of the stack
     */
    public static String dumpTable(String table, LuaState luaState) {
        String dumpTable = "";
        boolean containsTable = luaState.lua_istable(-1) != 0;
        if(containsTable) {
            IDLString idlString = luaState.dumpTable();
            String s = idlString.c_str();
            dumpTable += "### BEGIN TABLE ###\nTable: " + table + "\n"+ s + "### END TABLE ###";
        }
        else {
            dumpTable = table + " is not a table";
        }
        luaState.lua_pop(1);

        return dumpTable;
    }

    public static String dumpStack(LuaState luaState) {
        IDLString idlString = luaState.dumpStack();
        String s = idlString.c_str();
        return "### BEGIN STACK ###\n" + s + "### END STACK ###";
    }
}
