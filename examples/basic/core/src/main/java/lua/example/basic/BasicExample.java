package lua.example.basic;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lua.LuaFunction;
import lua.LuaState;

public class BasicExample extends ScreenAdapter {

    private OrthographicCamera uiCam;
    private SpriteBatch batch;
    public LuaState lua;

    @Override
    public void show() {
        lua = new LuaState();
        lua.createContext();

        lua.registerFunction("beep", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                int params = luaState.x_lua_gettop();
                int value = luaState.x_lua_tointeger(1);

                int paramType1 = luaState.x_lua_type(1);
                int paramType2 = luaState.x_lua_type(2);
                int paramType3 = luaState.x_lua_type(3);

                System.out.println("Beep is called: param: " + params);
                System.out.println("paramType1: " + paramType1);
                System.out.println("paramType2: " + paramType2);
                System.out.println("paramType3: " + paramType3);

                return 0;
            }
        });

        lua.script("beep(10, 2.0)");

        lua.destroyContext();
    }

    @Override
    public void render(float delta) {
    }
}