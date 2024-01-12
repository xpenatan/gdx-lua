package lua.register.gdx;

import lua.Lua;
import lua.register.gdx.graphics.glutils.LuaShapeRenderer;

public class GdxRegister {

    LuaShapeRenderer luaShapeRenderer;

    public GdxRegister() {
        luaShapeRenderer = new LuaShapeRenderer();
    }

    public void register(Lua lua) {
        LuaGL.registerGL(lua);
        luaShapeRenderer.register(lua);
    }
}
