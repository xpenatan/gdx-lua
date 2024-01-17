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

    private static final String METATABLE = "com.badlogic.gdx.graphics.glutils.ShapeRenderer";
    private static final String TEMPLATE = "Template";

    public LuaShapeRenderer() {

    }

    public static void register(Lua lua) {
        LuaState luaState = lua.getLuaState();

        Class<ShapeRenderer> clazz = ShapeRenderer.class;

        LuaLibrary.registerMetaClass(lua, clazz);

        LuaLibrary.registerClassFunction(LuaTableType.TEMPLATE, lua, clazz, "begin", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                System.out.println("BEGIN CALLED");
                return 0;
            }
        });

        LuaLibrary.registerClassNewFunction(lua, clazz, new LuaCreateClass() {
            @Override
            public Object onCreateClass(LuaState luaState) {
                return new ShapeRenderer();
            }
        });

        LuaLibrary.registerMetaClassTableNewIndex(LuaTableType.CLASS, lua, clazz, new DefaultNewIndexFunction());
        LuaLibrary.registerMetaClassTableNewIndex(LuaTableType.TEMPLATE, lua, clazz, new DefaultNewIndexFunction());
    }
}