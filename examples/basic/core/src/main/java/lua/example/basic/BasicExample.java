package lua.example.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import imgui.ImGui;
import imgui.ImGuiCond;
import imgui.ImVec2;
import imgui.extension.textedit.Coordinates;
import imgui.extension.textedit.LanguageDefinition;
import imgui.extension.textedit.TextEditor;
import imgui.idl.helper.IDLString;
import lua.ErrorStatus;
import lua.Lua;
import lua.LuaState;
import lua.example.basic.imgui.ImGuiRenderer;
import lua.register.gdx.LuaGdx;

public class BasicExample extends ImGuiRenderer {

    private TextEditor editor;
    private SpriteBatch batch;
    public Lua lua;
    boolean luaError;

    ShapeRenderer renderer = new ShapeRenderer();

    @Override
    public void show() {
        super.show();
        editor = new TextEditor();

        LanguageDefinition languageDefinition = LanguageDefinition.Lua();
        editor.SetLanguageDefinition(languageDefinition);

        String code = Gdx.files.internal("data/script.lua").readString();
        IDLString text = new IDLString();
        text.append(code);
        editor.SetText(text);
        lua = new Lua();

        LuaState luaState = lua.getLuaState();

        LuaGdx luaGdx = new LuaGdx();

        luaGdx.register(lua);

        buildScript(code);
    }

    @Override
    public void renderImGui() {
        update();
        renderEditText();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1, 0, 0 ,1);
        renderer.rect(2, 20, 100, 100);
        renderer.end();
    }

    public void update() {
        if(!luaError) {
            LuaState luaState = lua.getLuaState();
            // Call function onRender if exist
            luaState.lua_getglobal("onRender");
            if(luaState.lua_isnil(-1) != 0 || luaState.lua_isfunction(-1) == 0) {
                //Pop because it's not a function
                luaState.lua_pop(1);
                luaError = true;
            }
            else {
                ErrorStatus status = lua.callFunction(0, 0, 0);
                if(!status.isValid()) {
                    String errorText = status.getError();
                    System.err.println(errorText);
                    luaError = true;
                }
            }
        }
    }

    void renderEditText() {
        Coordinates coordinates = editor.GetCursorPosition();

        ImGui.SetNextWindowSize(ImVec2.TMP_1.set(600, 500), ImGuiCond.ImGuiCond_Once);
        ImGui.Begin("Editor");
        String text = "\t" + (coordinates.get_mLine() + 1) + "/" + (coordinates.get_mColumn() + 1) + " " + editor.GetTotalLines() + " lines | " + (editor.IsOverwrite() ? "Ovr" : "Ins") + " | " + (editor.CanUndo() ? "*" : " ") + " | " + editor.GetLanguageDefinition().get_mName().c_str();
        if(ImGui.Button("Build")) {
            String scriptText = editor.GetText().c_str();
            buildScript(scriptText);
        }
        ImGui.SameLine();
        ImGui.Text(text);
        editor.Render("Title");
        ImGui.End();
    }

    public void buildScript(String scriptCode) {
        ErrorStatus status = lua.buildScript(scriptCode);
        long pointer = lua.getLuaState().lua_topointer(-1);
//        String s = lua.dumpTable("ShapeRenderer");
        String s = lua.dumpTable("Vector2");
        luaError = false;
        if(!status.isValid()) {
            String errorText = status.getError();
            System.err.println(errorText);
            luaError = true;
        }
    }
}