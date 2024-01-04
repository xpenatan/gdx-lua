package lua;

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

    public ScriptState script(String script) {
        return luaState.script(script);
    }
}
