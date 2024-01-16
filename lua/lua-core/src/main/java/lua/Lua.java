package lua;

import com.badlogic.gdx.utils.IntMap;
import lua.idl.helper.IDLString;

public class Lua {

    LuaState luaState;

    LuaImport luaImport;

    public IntMap<Object> luaJavaInstances;

    public Lua() {
        luaState = new LuaState();
        luaState.createContext();
        luaJavaInstances = new IntMap<>();

        luaImport = new LuaImport();
        luaImport.register(this);
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

    /**
     *  Register function to table on top of the stack
     */
    public boolean registerFunction(String name, LuaFunction function) {
        if(luaState.lua_istable(-1) != 0) {
            luaState.lua_pushcfunction(function);
            luaState.lua_setfield(-2, name);
            return true;
        }
        return false;
    }

    public String dumpTable() {
        return dumpTable("", luaState);
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

    public LuaImport getLuaImport() {
        return luaImport;
    }

    void addObjectInstance(int key, Object object) {
        luaJavaInstances.put(object.hashCode(), object);
    }

    boolean removeObjectInstance(int hash) {
        Object remove = luaJavaInstances.remove(hash);
        return remove != null;
    }

    /**
     * Return the table log on top of the stack
     */
    public static String dumpTable(String table, LuaState luaState) {
        String dumpTable = "";
        int top = luaState.lua_gettop();
        boolean containsTable = luaState.lua_istable(-1) != 0;
        if(containsTable) {
            IDLString idlString = luaState.dumpTable();
            String s = idlString.c_str();
            long addr = luaState.lua_topointer(-1);
            dumpTable += "### BEGIN TABLE ###\nTable: " + table + " - Addr: " + addr + "\n"+ s + "### END TABLE ###";
        }
        else {
            dumpTable = table + " is not a table";
        }
        if(top > 0) {
            luaState.lua_pop(1);
        }

        return dumpTable;
    }

    public static String dumpStack(LuaState luaState) {
        IDLString idlString = luaState.dumpStack();
        String s = idlString.c_str();
        return "### BEGIN STACK ###\n" + s + "### END STACK ###";
    }
}
