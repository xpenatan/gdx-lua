package lua.register.gdx.graphics.glutils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lua.register.DefaultNewIndexFunction;
import lua.Lua;
import lua.LuaFunction;
import lua.LuaLibrary;
import lua.LuaState;
import lua.LuaTableType;
import lua.register.LuaCreateClass;

public class LuaShapeRenderer {

    public static void register(Lua lua) {
        LuaState luaState = lua.getLuaState();

        String rendererClassName = "com.badlogic.gdx.graphics.glutils.ShapeRenderer";

        LuaLibrary.registerClass(lua, rendererClassName, true);

        LuaLibrary.registerMetaClassTableNewIndex(lua, LuaTableType.CLASS, rendererClassName, new DefaultNewIndexFunction());
        LuaLibrary.registerMetaClassTableNewIndex(lua, LuaTableType.TEMPLATE, rendererClassName, new DefaultNewIndexFunction());

        LuaLibrary.setClassNewFunction(lua, rendererClassName, new LuaCreateClass() {
            @Override
            public Object onCreateClass(LuaState luaState) {
                return new ShapeRenderer();
            }
        });

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.TEMPLATE, rendererClassName, "Begin", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                System.out.println("BEGIN CALLED");
                return 0;
            }
        });

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.TEMPLATE, rendererClassName, "End", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                System.out.println("END CALLED");
                return 0;
            }
        });

    }
}
