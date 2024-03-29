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
import lua.LuaErrorStatus;
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

    int moveState = 1;
    // 1 = RIGHT
    // 2 = TOP
    // 3 = LEFT
    // 4 = BOTTOM

    float recX = 50;
    float recY = 50;

    @Override
    public void renderImGui() {
        update();
        renderEditText();
    }

    public void update() {

        if(!luaError) {
            LuaState luaState = lua.getLuaState();
            // Call function onRender if exist
            luaState.lua_getglobal("render");
            if(luaState.lua_isnil(-1) != 0 || luaState.lua_isfunction(-1) == 0) {
                //Pop because it's not a function
                luaState.lua_pop(1);
                luaError = true;
            }
            else {
                LuaErrorStatus status = lua.callFunction(0, 0, 0);
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
        String text = "\t" + (coordinates.mLine() + 1) + "/" + (coordinates.mColumn() + 1) + " " + editor.GetTotalLines() + " lines | " + (editor.IsOverwrite() ? "Ovr" : "Ins") + " | " + (editor.CanUndo() ? "*" : " ") + " | " + editor.GetLanguageDefinition().mName().c_str();
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
        LuaErrorStatus status = lua.buildScript(scriptCode);
        long pointer = lua.getLuaState().lua_topointer(-1);
//        String s = lua.dumpTable("ShapeRenderer");
        String ShapeRenderer = lua.dumpTable("ShapeRenderer");
        String renderer = lua.dumpTable("renderer");
        String GL20 = lua.dumpTable("GL20");
        String Gdx = lua.dumpTable("Gdx");
        luaError = false;
        if(!status.isValid()) {
            String errorText = status.getError();
            System.err.println(errorText);
            luaError = true;
        }
    }
}