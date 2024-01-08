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
import lua.LuaLibrary;
import lua.LuaState;
import lua.LuaTypes;
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

        LuaState luaState = lua.getLuaState();

        LuaFunction function = new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                int params = luaState.lua_gettop();
                boolean argError = false;

                if(params != 1) {
                    argError = true;
                }
                else {
                    int argType = luaState.lua_type(1);
                    if(argType != LuaTypes.LUA_TSTRING) {
                        argError = true;
                    }
                }
                if(argError) {
                    luaState.luaL_error("Only accept a single String argument");
                    return 0;
                }

                System.out.println("import");

                return 1;
            }
        };
        LuaLibrary.addLibrary(lua, "java.import", function);

        LuaFunction functionIn = new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                int params = luaState.lua_gettop();
                boolean argError = false;

                if(params != 1) {
                    argError = true;
                }
                else {
                    int argType = luaState.lua_type(1);
                    if(argType != LuaTypes.LUA_TSTRING) {
                        argError = true;
                    }
                }
                if(argError) {
                    luaState.luaL_error("Only accept a single String argument");
                    return 0;
                }

                System.out.println("include");

                return 1;
            }
        };
        LuaLibrary.addLibrary(lua, "java.include", functionIn);

        Lua.ScriptStatus scriptStatus = lua.runScript(code);

        String table = lua.dumpTable("java");
        System.out.println(table);

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