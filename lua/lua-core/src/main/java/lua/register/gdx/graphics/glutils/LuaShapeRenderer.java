package lua.register.gdx.graphics.glutils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.HashMap;
import lua.Lua;
import lua.LuaFunction;
import lua.LuaImport;
import lua.LuaState;
import lua.register.ImportListener;

public class LuaShapeRenderer {

    HashMap<Integer, ShapeRenderer> shapeRenderMap;

    public LuaShapeRenderer() {

    }

    public void register(Lua lua) {
        LuaImport luaImport = lua.getLuaImport();

        luaImport.addImportListener("com.badlogic.gdx.graphics.glutils", new ImportListener() {
            @Override
            public int onImport(LuaState luaState) {
                // Return a table template to create our shape renderer
                createTableTemplate(luaState);
                return 1;
            }
        });
    }

    private void createTableTemplate(LuaState luaState) {
        luaState.lua_newtable();
        luaState.lua_pushstring("new");
        luaState.lua_pushcfunction(new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                ShapeRenderer renderer = new ShapeRenderer();
                int hash = renderer.hashCode();
                shapeRenderMap.put(hash, renderer);

                luaState.lua_newtable();
                luaState.lua_pushstring("java_address");
                luaState.lua_pushinteger(hash);
                luaState.lua_settable(-3);

                System.out.println("NEW IS CALLED");

                return 1;
            }
        });
        luaState.lua_settable(-3);


        luaState.lua_newtable();
        luaState.lua_pushstring("begin");
        luaState.lua_pushcfunction(new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {

                System.out.println("begin is CALLED");

                return 0;
            }
        });
        luaState.lua_settable(-3);
    }
}
