package lua.example.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import imgui.ImGui;
import imgui.extension.textedit.Coordinates;
import imgui.extension.textedit.LanguageDefinition;
import imgui.extension.textedit.TextEditor;
import imgui.idl.helper.IDLString;
import lua.Lua;
import lua.LuaFunction;
import lua.LuaState;
import lua.example.basic.imgui.ImGuiRenderer;

public class BasicExample extends ImGuiRenderer {

    private TextEditor editor;
    private OrthographicCamera uiCam;
    private SpriteBatch batch;
    public Lua lua;

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

        lua.registerGlobalFunction("import", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                long cPointer1 = luaState.getCPointer();

                System.out.println("cPointer1: " + cPointer1);


//                int params = luaState.lua_gettop();
//                if(params != 1) {
//                    luaState.luaL_argerror(1, "Only accept String argument");



                    luaState.luaL_error("ERROR");
                    return 1;
//                }
//                return 1;
            }
        });

        Lua.ScriptStatus scriptStatus = lua.runScript(code);

        if(!scriptStatus.isValid()) {
            String error = scriptStatus.getError();
            System.err.println(error);
        }

        lua.dispose();
    }

    @Override
    public void renderImGui() {
        Coordinates coordinates = editor.GetCursorPosition();

        ImGui.Begin("Editor");

        String text = "\t" + (coordinates.get_mLine() + 1) + "/" + (coordinates.get_mColumn() + 1) + " " + editor.GetTotalLines() + " lines | " + (editor.IsOverwrite() ? "Ovr" : "Ins") + " | " + (editor.CanUndo() ? "*" : " ") + " | " + editor.GetLanguageDefinition().get_mName().c_str();
        ImGui.Text(text);

        editor.Render("Title");
        ImGui.End();
    }
}