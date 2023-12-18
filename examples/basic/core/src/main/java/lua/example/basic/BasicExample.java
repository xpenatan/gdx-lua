package lua.example.basic;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lua.Lua;

public class BasicExample extends ScreenAdapter {

    private OrthographicCamera uiCam;
    private SpriteBatch batch;

    @Override
    public void show() {
        Lua.script();
    }

    @Override
    public void render(float delta) {
    }
}