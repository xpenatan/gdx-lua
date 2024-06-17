package lua.example.basic.imgui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import imgui.ImDrawData;
import imgui.ImGui;
import imgui.ImGuiConfigFlags;
import imgui.ImGuiIO;
import imgui.gdx.ImGuiGdxImpl;
import imgui.gdx.ImGuiGdxInputMultiplexer;

public abstract class ImGuiRenderer extends ScreenAdapter {

    private ImGuiGdxImpl impl;

    protected ImGuiGdxInputMultiplexer input;

    @Override
    public void show() {
        ImGui.CreateContext();

        ImGuiIO io = ImGui.GetIO();
        io.ConfigFlags(ImGuiConfigFlags.ImGuiConfigFlags_DockingEnable);

        input = new ImGuiGdxInputMultiplexer();
        impl = new ImGuiGdxImpl();
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
        impl.update();

        renderImGui();

        ImGui.Render();
        ImDrawData drawData = ImGui.GetDrawData();
        impl.render(drawData);
    }

    public abstract void renderImGui();

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        impl.dispose();
        ImGui.disposeStatic();
        ImGui.DestroyContext();
    }
}