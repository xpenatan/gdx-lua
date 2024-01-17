package lua.register.gdx.graphics;

import com.badlogic.gdx.graphics.GL20;
import lua.Lua;
import lua.LuaLibrary;
import lua.LuaState;

public class LuaGL20 {

    public static void registerGL(Lua lua) {
        LuaState luaState = lua.getLuaState();


        Class<GL20> clazz = GL20.class;

        LuaLibrary.registerMetaClass(lua, clazz);
//        LuaLibrary.registerMetaClassNewIndex(lua, clazz, new DefaultNewIndexFunction());




//        LuaLibrary.getOrCreateClass(lua, GL20.class);
//
//        luaState.lua_pushstring("GL_COLOR_BUFFER_BIT");
//        luaState.lua_pushinteger(GL20.GL_COLOR_BUFFER_BIT);
//        luaState.lua_settable(-3);
//        luaState.lua_pop(-1);
//
//        LuaLibrary.addLibrary(lua, "Gdx.gl.glClear", new LuaFunction() {
//
//            @Override
//            public int onCall(LuaState luaState) {
//
//                int params = luaState.lua_gettop();
//                boolean argError = false;
//                if(params != 1) {
//                    argError = true;
//                }
//                else {
//                    if(luaState.lua_isinteger(1) == 0) {
//                        argError = true;
//                    }
//                }
//                if(argError) {
//                    luaState.luaL_error("Only accept a single int argument");
//                    return 0;
//                }
//
//                int mask = luaState.lua_tointeger(1);
//                Gdx.gl.glClear(mask);
//                return 0;
//            }
//        });
//
//        LuaLibrary.addLibrary(lua, "Gdx.gl.glClearColor", new LuaFunction() {
//
//            @Override
//            public int onCall(LuaState luaState) {
//                int params = luaState.lua_gettop();
//                boolean argError = false;
//                if(params != 4) {
//                    argError = true;
//                }
//                else {
//                    if(luaState.lua_isnumber(1) == 0 || luaState.lua_isnumber(2) == 0 ||
//                            luaState.lua_isnumber(3) == 0 || luaState.lua_isnumber(4) == 0) {
//                        argError = true;
//                    }
//                }
//                if(argError) {
//                    luaState.luaL_error("Only accept 4 float arguments");
//                    return 0;
//                }
//
//                float v1 = (float)luaState.lua_tonumber(1);
//                float v2 = (float)luaState.lua_tonumber(2);
//                float v3 = (float)luaState.lua_tonumber(3);
//                float v4 = (float)luaState.lua_tonumber(4);
//                Gdx.gl.glClearColor(v1, v2, v3, v4);
//                return 0;
//            }
//        });

    }
}
