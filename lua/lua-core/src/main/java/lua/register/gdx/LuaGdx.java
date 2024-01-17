package lua.register.gdx;

import lua.Lua;
import lua.register.gdx.graphics.LuaGL20;
import lua.register.gdx.graphics.glutils.LuaShapeRenderer;

public class LuaGdx {

    public static void register(Lua lua) {
        LuaGL20.registerGL(lua);
        LuaShapeRenderer.register(lua);
    }
}