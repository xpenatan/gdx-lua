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
}
