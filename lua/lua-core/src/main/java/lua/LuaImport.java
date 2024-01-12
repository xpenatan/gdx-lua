package lua;

import java.util.HashMap;
import lua.DefaultImportFunction;
import lua.Lua;
import lua.LuaFunction;
import lua.LuaLibrary;
import lua.LuaState;
import lua.register.ImportListener;

public class LuaImport {

    private HashMap<String, ImportListener> importMap;

    public LuaImport() {
        importMap = new HashMap<>();
    }

    public void addImportListener(String key, ImportListener importListener) {
        importMap.put(key, importListener);
    }

    public void removeImportListener(String key) {
        importMap.remove(key);
    }

    void register(Lua lua) {
        LuaFunction function = new DefaultImportFunction() {
            @Override
            public int onImport(LuaState luaState) {
                String importParam = luaState.lua_tostring(-1).c_str();
                ImportListener importListener = importMap.get(importParam);
                if(importListener != null) {
                    return importListener.onImport(luaState);
                }
                else {
                    luaState.luaL_error("Import param is not registered: " + importParam);
                }
                return 0;
            }
        };
        LuaLibrary.addLibrary(lua, "java.import", function);
    }
}
