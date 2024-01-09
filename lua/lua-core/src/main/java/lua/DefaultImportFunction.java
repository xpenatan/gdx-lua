package lua;

/**
 * Function that can only contains 1 string parameter.
 */
public abstract class DefaultImportFunction extends LuaFunction {

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
        return onLibrary(luaState);
    }

    protected abstract int onLibrary(LuaState luaState);
}
