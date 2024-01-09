package lua.example.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import imgui.ImGui;
import imgui.ImGuiCond;
import imgui.ImVec2;
import imgui.extension.textedit.Coordinates;
import imgui.extension.textedit.LanguageDefinition;
import imgui.extension.textedit.TextEditor;
import imgui.idl.helper.IDLString;
import lua.DefaultImportFunction;
import lua.ErrorStatus;
import lua.Lua;
import lua.LuaFunction;
import lua.LuaLibrary;
import lua.LuaState;
import lua.example.basic.imgui.ImGuiRenderer;
import lua.gdx.register.GL;

public class BasicExample extends ImGuiRenderer {

    private TextEditor editor;
    private OrthographicCamera uiCam;
    private SpriteBatch batch;
    public Lua lua;
    boolean luaError;

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

        LuaFunction function = new DefaultImportFunction() {
            @Override
            public int onLibrary(LuaState luaState) {
                System.out.println("import");
                return 1;
            }
        };
        LuaLibrary.addLibrary(lua, "java.import", function);

        LuaFunction functionIn = new DefaultImportFunction() {
            @Override
            public int onLibrary(LuaState luaState) {
                System.out.println("include");
                return 1;
            }
        };
        LuaLibrary.addLibrary(lua, "java.include", functionIn);
        GL.registerGL(lua);




        buildScript(code);
    }

    @Override
    public void renderImGui() {
        update();
        renderEditText();
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
        luaError = false;
        if(!status.isValid()) {
            String errorText = status.getError();
            System.err.println(errorText);
            luaError = true;
        }
    }
}