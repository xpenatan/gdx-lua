package lua.register.gdx;

import lua.Lua;
import lua.register.gdx.graphics.glutils.LuaShapeRenderer;

public class LuaGdx {

    LuaShapeRenderer luaShapeRenderer;

    public LuaGdx() {
        luaShapeRenderer = new LuaShapeRenderer();
    }

    public void register(Lua lua) {
        LuaGL.registerGL(lua);
        luaShapeRenderer.register(lua);
    }
}
