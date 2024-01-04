package lua.example.basic;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import imgui.ImGui;
import imgui.extension.textedit.Coordinates;
import imgui.extension.textedit.LanguageDefinition;
import imgui.extension.textedit.TextEditor;
import imgui.idl.helper.IDLString;
import lua.LuaFunction;
import lua.LuaState;
import lua.ScriptState;
import lua.example.basic.imgui.ImGuiRenderer;

public class BasicExample extends ImGuiRenderer {

    private TextEditor editor;
    private OrthographicCamera uiCam;
    private SpriteBatch batch;
    public LuaState lua;

    @Override
    public void show() {
        super.show();
        editor = new TextEditor();

        LanguageDefinition languageDefinition = LanguageDefinition.Lua();
        editor.SetLanguageDefinition(languageDefinition);
        IDLString text = new IDLString();
        String code = "\n" +
                "function onCreate()\n" +
                "\n" +
                "end\n" +
                "\n\n" +
                "function onRender(delta)\n" +
                "\n" +
                "end\n";
        text.append(code);
        editor.SetText(text);
        lua = new LuaState();
        lua.createContext();

        lua.x_lua_pushinteger(20);
        lua.x_lua_pushcclosure(new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                int value = lua.x_lua_tointeger(lua.x_lua_upvalueindex(2));

                int params = luaState.x_lua_gettop();
                lua.idl.helper.IDLString idlString = luaState.x_lua_tostring(1);
//                int value = luaState.x_lua_tointeger(1);

                int paramType1 = luaState.x_lua_type(1);
                int paramType2 = luaState.x_lua_type(2);
                int paramType3 = luaState.x_lua_type(3);

                System.out.println("value: " + value);
                System.out.println("Beep is called: param: " + params);
                System.out.println("Beep is called: value: " + idlString.c_str());
                System.out.println("paramType1: " + paramType1);
                System.out.println("paramType2: " + paramType2);
                System.out.println("paramType3: " + paramType3);
                return 0;
            }
        }, 1);
        lua.x_lua_setglobal("beep");

//        lua.registerFunction("beep", new LuaFunction() {
//            @Override
//            public int onCall(LuaState luaState) {
//                int params = luaState.x_lua_gettop();
//                lua.idl.helper.IDLString idlString = luaState.x_lua_tostring(1);
////                int value = luaState.x_lua_tointeger(1);
//
//                int paramType1 = luaState.x_lua_type(1);
//                int paramType2 = luaState.x_lua_type(2);
//                int paramType3 = luaState.x_lua_type(3);
//
//                System.out.println("Beep is called: param: " + params);
//                System.out.println("Beep is called: value: " + idlString.c_str());
//                System.out.println("paramType1: " + paramType1);
//                System.out.println("paramType2: " + paramType2);
//                System.out.println("paramType3: " + paramType3);
//                return 0;
//            }
//        });

        lua.setInt("myVar", 10);

        lua.stackDump();

        ScriptState state = lua.script("beep(\"HELLO\")");

        if(!state.get_isValid()) {
            String error = state.get_rawErrorText().c_str();
            System.out.println("test: " + error);
        }

        lua.destroyContext();
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