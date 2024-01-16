package lua.register.gdx.graphics.glutils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.HashMap;
import lua.Lua;
import lua.LuaFunction;
import lua.LuaImport;
import lua.LuaState;
import lua.register.ImportListener;

public class LuaShapeRenderer {

    private static final String METATABLE = "com.badlogic.gdx.graphics.glutils.ShapeRenderer";
    private static final String TEMPLATE = "Template";

    HashMap<Integer, ShapeRenderer> shapeRenderMap;

    public LuaShapeRenderer() {

    }

    public void register(Lua lua) {
        LuaImport luaImport = lua.getLuaImport();
        LuaState luaState = lua.getLuaState();

        createTableTemplate(lua, luaState);

        luaImport.addImportListener(METATABLE, new ImportListener() {
            @Override
            public int onImport(LuaState luaState) {
                // Return a table template to create our shape renderer

                luaState.luaL_getmetatable(METATABLE);

                return 1;
            }
        });
    }

    private void createTableTemplate(Lua lua, LuaState luaState) {
        luaState.luaL_newmetatable(METATABLE);

        luaState.lua_pushcfunction(new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                System.out.println("NEW IS CALLED");
//                ShapeRenderer renderer = new ShapeRenderer();
//                int hash = renderer.hashCode();
//                shapeRenderMap.put(hash, renderer);
////                luaState.lua_pushstring("java_address");
////                luaState.lua_pushinteger(hash);
////                luaState.lua_settable(-3);
                luaState.lua_newtable();
                luaState.luaL_getmetatable(METATABLE);
                luaState.lua_getfield(-1, TEMPLATE);
                luaState.lua_remove(-2); // Remove metatable
                luaState.lua_setmetatable(-2); // Set mt to table
                return 1;
            }
        });
        luaState.lua_setfield(-2, "new");

        luaState.lua_newtable(); // TEMPLATE
        luaState.lua_pushvalue(-1); // copy TEMPLATE reference
        luaState.lua_setfield(-2, "__index"); // mt.__index = mt

        luaState.lua_pushstring("Private"); // copy TEMPLATE reference
        luaState.lua_setfield(-2, "__metatable"); // mt.__index = mt

        luaState.lua_pushcfunction(new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {

                //Check if key is string
                if(luaState.lua_isstring(-2) != 0) {
                    String key = luaState.lua_tostring(-2).c_str();
                    if(key.equals("x")) {
                        // allow only x
                        luaState.lua_rawset(-3);
                    }
                }
                return 0;
            }
        });
        luaState.lua_setfield(-2, "__newindex"); // mt.__index = mt

        luaState.lua_pushcfunction(new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                int top = luaState.lua_gettop();
                if(top == 2) {
                    if(luaState.lua_istable(1) == 0) {
                        luaState.luaL_error("begin is not a static method");
                    }

                }
                else {
                    luaState.luaL_error("Wrong arguments");
                }
                return 0;
            }
        });
        luaState.lua_setfield(-2, "begin");

        luaState.lua_setfield(-2, TEMPLATE); // set Template to metatable
        luaState.lua_pop(-1);
    }
}
