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
                System.out.println("Beep is called");

                return 0;
            }
        });

        lua.script("beep()");

        lua.destroyContext();
    }

    @Override
    public void render(float delta) {
    }
}