package lua.register.gdx;

import com.badlogic.gdx.Gdx;
import lua.Lua;
import lua.LuaLibrary;
import lua.LuaTableType;
import lua.register.gdx.graphics.LuaGL20;
import lua.register.gdx.graphics.glutils.LuaShapeRenderer;

public class LuaGdx {

    public static void register(Lua lua) {
        LuaGL20.registerGL(lua);
        LuaShapeRenderer.register(lua);

        String gdxClassName = "com.badlogic.gdx.Gdx";
        LuaLibrary.registerClass(lua, gdxClassName, false);

        String glClassName = "com.badlogic.gdx.graphics.GL20";

        if(LuaLibrary.createInstanceObject(lua, glClassName, Gdx.gl)) {
            LuaLibrary.setMetaClassTable(lua, LuaTableType.CLASS, gdxClassName, "gl");
        }

//        LuaLibrary.setMetaClassFloat()
    }
}