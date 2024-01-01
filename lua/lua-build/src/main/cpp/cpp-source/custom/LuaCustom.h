#pragma once

#define SOL_DEFAULT_PASS_ON_ERROR 1
#include <sol/sol.hpp>
#include <iostream>
#include <string>

#if defined(_MSC_VER) && _MSC_VER <= 1500 // MSVC 2008 or earlier
#include <stddef.h>     // intptr_t
#else
#include <stdint.h>     // intptr_t
#endif

const char GLOBAL_TABLE_FUNCTIONS[] = "__cppfunction__";

class LuaState;

struct ScriptState {
    std::string errorText;
    std::string rawErrorText;
    bool isValid;
};

class LuaFunction
{
    public:
        LuaState* lua;

        virtual ~LuaFunction() {
        }

        virtual int onCall(LuaState* luaState) = 0;
};

static int lua_function(lua_State* L) {
    intptr_t* data = (intptr_t*)lua_touserdata(L, lua_upvalueindex(1));
    LuaFunction* function = reinterpret_cast<LuaFunction*>(*data);
    return function->onCall(function->lua); /* number of results */
}

class LuaState {

private:
    lua_State * L;

public:

    LuaState() {
        L = NULL;
    }

    // #### BEGIN LOW LEVEL LUA METHODS

    void x_lua_absindex(int idx)                               { lua_absindex(L, idx); }
    void x_lua_arith(int op)                                   { lua_arith(L, op); }
    int x_lua_checkstack(int n)                                { return lua_checkstack(L, n); }
    int x_lua_compare(int index1, int index2, int op)          { return lua_compare(L, index1, index2, op); }
    void x_lua_concat(int op, int n)                           { lua_concat(L, n); }
    void x_lua_copy(int fromidx, int toidx)                    { lua_copy(L, fromidx, toidx); }
    void x_lua_createtabble(int narr, int nrec)                { lua_createtable(L, narr, nrec); }
    int x_lua_error()                                          { return lua_error(L); }
    int x_lua_getfield(int index, const char* k)               { return lua_getfield(L, index, k); }

    int x_lua_getglobal(const char* name)                      { return lua_getglobal(L, name); }
    int x_lua_geti(int index, long long i)                     { return lua_geti(L, index, i); }
    int x_lua_getmetatable(int index)                          { return lua_getmetatable(L, index); }
    int x_lua_gettable(int index)                              { return lua_gettable(L, index); }
    int x_lua_gettop()                                         { return lua_gettop(L); }
    int x_lua_getuservalue(int index)                          { return lua_getuservalue(L, index); }
    int x_lua_getiuservalue(int index, int n)                  { return lua_getiuservalue(L, index, n); }
    void x_lua_insert(int index)                               { lua_insert(L, index); }
    int x_lua_isboolean(int index)                             { return lua_isboolean(L, index); }
    int x_lua_iscfunction(int index)                           { return lua_iscfunction(L, index); }
    int x_lua_isfunction(int index)                            { return lua_isfunction(L, index); }
    int x_lua_isinteger(int index)                             { return lua_isinteger(L, index); }
    int x_lua_islightuserdata(int index)                       { return lua_islightuserdata(L, index); }
    int x_lua_isnil(int index)                                 { return lua_isnil(L, index); }
    int x_lua_isnone(int index)                                { return lua_isnone(L, index); }
    int x_lua_isnoneornil(int index)                           { return lua_isnoneornil(L, index); }
    int x_lua_isnumber(int index)                              { return lua_isnumber(L, index); }
    int x_lua_isstring(int index)                              { return lua_isstring(L, index); }
    int x_lua_istable(int index)                               { return lua_istable(L, index); }
    int x_lua_isthread(int index)                              { return lua_isthread(L, index); }
    int x_lua_isuserdata(int index)                            { return lua_isuserdata(L, index); }
    int x_lua_isyieldable()                                    { return lua_isyieldable(L); }
    void x_lua_len(int index)                                  { lua_len(L, index); }

    void x_lua_newtable()                                      { lua_newtable(L); }

    void* x_lua_newuserdata(size_t size)                       { return lua_newuserdata(L, size); }
    void* x_lua_newuserdatauv(size_t size, int nuvalue)        { return lua_newuserdatauv(L, size, nuvalue); }
    int x_lua_next(int index)                                  { return lua_next(L, index); }

    void x_lua_pop(int n)                                      { lua_pop(L, n); }
    void x_lua_pushboolean(int b)                              { lua_pushboolean(L, b); }

    void x_lua_pushfstring(const char* fmt)                    { lua_pushfstring(L, fmt); }
    void x_lua_pushglobaltable()                               { lua_pushglobaltable(L); }
    void x_lua_pushinteger(long long n)                        { lua_pushinteger(L, n); }

    void x_lua_pushlstring(const char* s, int len)             { lua_pushlstring(L, s, len); }
    void x_lua_pushnil()                                       { lua_pushnil(L); }
    void x_lua_pushnumber(double n)                            { lua_pushnumber(L, n); }
    void x_lua_pushstring(const char* s)                       { lua_pushstring(L, s); }
    int x_lua_pushthread()                                     { return lua_pushthread(L); }
    void x_lua_pushvalue(int index)                            { lua_pushvalue(L, index); }

    int x_lua_rawequal(int index1, int index2)                 { return lua_rawequal(L, index1, index2); }
    int x_lua_rawget(int index)                                { return lua_rawget(L, index); }
    int x_lua_rawgeti(int index, long long n)                  { return lua_rawgeti(L, index, n); }

    void x_lua_rawset(int index)                               { lua_rawset(L, index); }
    void x_lua_rawseti(int index, long long i)                 { lua_rawseti(L, index, i); }

    void x_lua_remove(int index)                               { lua_remove(L, index); }
    void x_lua_replace(int index)                              { lua_replace(L, index); }
    int x_lua_resetthread(int index)                           { return lua_resetthread(L); }
    void x_lua_rotatee(int index, int n)                       { lua_rotate(L, index, n); }

    void x_lua_setfield(int index, const char* k)              { lua_setfield(L, index, k); }
    void x_lua_setglobal(int index, const char* name)          { lua_setglobal(L, name); }
    void x_lua_seti(int index, lua_Integer n)                  { lua_seti(L, index, n); }

    int x_lua_setuservalue(int index)                            { return lua_setuservalue(L, index); }
    int x_lua_setiuservalue(int index, int n)                  { return lua_setiuservalue(L, index, n); }
    int x_lua_setmetatable(int index)                          { return lua_setmetatable(L, index); }
    void x_lua_settable(int index)                             { lua_settable(L, index); }
    void x_lua_settop(int index)                               { lua_settop(L, index); }

    int x_lua_status()                                         { return lua_status(L); }
    size_t x_lua_stringtonumber(const char* s)                 { return lua_stringtonumber(L, s); }
    int x_lua_toboolean(int index)                             { return lua_toboolean(L, index); }

    void x_lua_toclose(int index)                              { lua_toclose(L, index); }
    lua_Integer x_lua_tointeger(int index)                     { return lua_tointeger(L, index); }

    const std::string x_lua_tolstring(int index, size_t* len)  { return lua_tolstring(L, index, len); }
    lua_Number x_lua_tonumber(int index)                       { return lua_tonumber(L, index); }

    const std::string x_lua_tostring(int index)                { return lua_tostring(L, index); }

    int x_lua_type(int index)                                  { return lua_type(L, index); }
    const std::string x_lua_typename(int tp)                   { return lua_typename(L, tp); }

    int x_lua_upvalueindex(int i)                              { return lua_upvalueindex(i); }
    lua_Number  x_lua_version()                                { return lua_version(L); }
    void x_lua_warning(const char* msg, int tocont)            { lua_warning(L, msg, tocont); }

    int x_lua_yield(int nresults)                              { return lua_yield(L, nresults); }

    void x_luaL_checktype(int arg, int t)                              { luaL_checktype(L, arg, t); }

    // #### END LOW LEVEL LUA METHODS

    void createContext() {
        L = luaL_newstate();
        luaL_openlibs(L);
    }

    void destroyContext() {
        if (L != NULL) {
            lua_close(L);
            L = NULL;
        }
    }

    void collectGarbage() {
        sol::state_view lua(L);
        lua.collect_garbage();
    }

    const ScriptState script(const char* code) {
        sol::state_view lua(L);
        sol::protected_function_result result = lua.safe_script(code);
        ScriptState scriptState;
        sol::call_status status = result.status();
        scriptState.isValid = status == sol::call_status::ok;
        if (!scriptState.isValid) {
            sol::type t = sol::type_of(L, result.stack_index());

            scriptState.errorText = "Lua: ";
            scriptState.errorText += to_string(status);
            scriptState.errorText += " error";

            if (t == sol::type::string) {
                scriptState.errorText += ": ";
                sol::string_view serr = sol::stack::unqualified_get<sol::string_view>(L, result.stack_index());
                scriptState.rawErrorText.append(serr.data(), serr.size());
                scriptState.errorText = scriptState.rawErrorText;
            }
        }
        return scriptState;
    }

    int getOrInt(const char* key, int otherwise) {
        sol::state_view lua(L);
        return lua.get_or(key, otherwise);
    }
    float getOrFloat(const char* key, float otherwise) {
        sol::state_view lua(L);
        return lua.get_or(key, otherwise);
    }
    bool getOrBool(const char* key, bool otherwise) {
        sol::state_view lua(L);
        return lua.get_or(key, otherwise);
    }
    const std::string getOrString(const char* key, const char* otherwise) {
        sol::state_view lua(L);
        return lua.get_or(key, otherwise);
    }

    void setInt(const char* key, int value) {
        sol::state_view lua(L);
        lua.set(key, value);
    }
    void setFloat(const char* key, float value) {
        sol::state_view lua(L);
        lua.set(key, value);
    }
    void setBool(const char* key, bool value) {
        sol::state_view lua(L);
        lua.set(key, value);
    }
    void setString(const char* key, const char* value) {
        sol::state_view lua(L);
        lua.set(key, value);
    }

    void registerFunction(const char* key, LuaFunction* callback) {
        callback->lua = this;
        intptr_t ptr = reinterpret_cast<intptr_t>(callback);
        intptr_t* data = (intptr_t*)lua_newuserdata(L, sizeof(intptr_t));
        *data = ptr;
        luaL_setmetatable(L, GLOBAL_TABLE_FUNCTIONS);
        lua_getfield(L, LUA_REGISTRYINDEX, GLOBAL_TABLE_FUNCTIONS);
        lua_setmetatable(L, -2);
        lua_pushcclosure(L, &lua_function, 1);
        lua_setglobal((lua_State*)L, (const char*)key);
    }

    void printTable() {
        lua_pushglobaltable(L);       // Get global table
        lua_pushnil(L);               // put a nil key on stack
        while (lua_next(L, -2) != 0) { // key(-1) is replaced by the next key(-1) in table(-2)
            const char* name = lua_tostring(L, -2);  // Get key(-2) name

            std::cout << "name: " << name << std::endl;


            lua_pop(L, 1);               // remove value(-1), now key on top at(-1)
        }
        lua_pop(L, 1);                 // remove global table(-1)
    }

    static void setIntToVoid(void * pointer, int value) {
      int * data = (int *) pointer;
      *data = value;
    }

    static int getIntFromVoid(void * pointer) {
      int * data = (int *) pointer;
      return *data;
    }
};