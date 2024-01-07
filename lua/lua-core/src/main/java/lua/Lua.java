package lua;

import lua.idl.helper.IDLString;

public class Lua {

    LuaState luaState;
    private ScriptStatus scriptStatus = new ScriptStatus();

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

    public void printTable(String table) {
        printTable(table, luaState);
    }

    public void printStack() {
        System.out.println(dumpStack(luaState));
    }

    public String dumpStack() {
        return dumpStack(luaState);
    }

    public ScriptStatus runScript(String script) {
        luaState.luaL_loadstring(script);
        scriptStatus.code = luaState.lua_pcall(0, 0, 0);
        scriptStatus.error = "";
        if(!scriptStatus.isValid()) {
            IDLString idlString = luaState.lua_tostring(-1);
            scriptStatus.error = idlString.c_str();
        }
        return scriptStatus;
    }

    public static class ScriptStatus {
        private ScriptStatus() {}

        int code;
        String error;

        public boolean isValid() {
            return code == 0;
        }

        public int getCode() {
            return code;
        }

        public String getError() {
            return error;
        }
    }

    public static void printTable(String table, LuaState luaState) {
        luaState.lua_getglobal(table);
        int i = luaState.lua_istable(-1);
        if(i>0) {
            System.err.println("### BEGIN TABLE ###");
            IDLString idlString = luaState.dumpTable();
            String s = idlString.c_str();
            System.err.print(s);
            System.err.println("### END TABLE ###");
        }
        else {
            System.out.println(table + " is not a table");
        }
    }

    public static String dumpStack(LuaState luaState) {
        IDLString idlString = luaState.dumpStack();
        String s = idlString.c_str();
        return "### BEGIN STACK ###\n" + s + "### END STACK ###";
    }
}
