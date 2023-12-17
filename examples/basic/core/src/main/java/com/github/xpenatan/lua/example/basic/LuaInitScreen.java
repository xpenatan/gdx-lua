package com.github.xpenatan.lua.example.basic;

import com.badlogic.gdx.ScreenAdapter;
import lua.LuaLoader;

public class LuaInitScreen extends ScreenAdapter {

    private LuaGame game;

    private boolean init = false;

    public LuaInitScreen(LuaGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        LuaLoader.init(() -> init = true);
    }

    @Override
    public void render(float delta) {
        if(init) {
            init = false;
            game.setScreen(new BasicExample());
        }
    }
}
