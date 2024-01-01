package lua;

import idl.IDLBase;

public class LuaFunction extends IDLBase {

    /*[-C++;-NATIVE]
        static jclass luaFunctionClass = 0;
        static jmethodID onCallID = 0;
        class CustomLuaFunction : public LuaFunction
        {
        private:
            JNIEnv* env;
            jobject obj;
        public:
            CustomLuaFunction( JNIEnv* env, jobject obj )
            {
                this->env = env;
                this->obj = obj;
            }
            virtual int onCall(LuaState* luaState)
            {
                return env->CallIntMethod(obj, onCallID, (jlong)luaState);
            }
        };
    */

    /*[-teaVM;-ADD]
        @org.teavm.jso.JSFunctor
        public interface OnCall extends org.teavm.jso.JSObject {
            int onCallJS(int luaState);
        }
    */

    /*[-teaVM;-REPLACE]
        public LuaFunction() {
            OnCall onCall = new OnCall() {
                @Override
                public int onCallJS (int luaState) {
                    return onCall(luaState);
                }
            };
            int pointer = createNative(onCall);
            initObject(pointer, true);
        }
    */
    public LuaFunction() {
        long addr = createNATIVE();
        initObject(addr, true);
    }

    /*[-C++;-NATIVE]
        if(luaFunctionClass == 0) {
            luaFunctionClass = (jclass)env->NewGlobalRef(env->GetObjectClass(object));
            onCallID = env->GetMethodID(luaFunctionClass, "onCall", "(J)I");
        }
        return (jlong)new CustomLuaFunction(env, env->NewGlobalRef(object));
    */
    /*[-teaVM;-REPLACE]
        @org.teavm.jso.JSBody(params = { "onCall" }, script = "var jsLuaFunction = new [MODULE].LuaFunctionImpl(); jsLuaFunction.onCall = onCall; return [MODULE].getPointer(jsLuaFunction);")
        private static native int createNative(OnCall onCall);
    */
    private native long createNATIVE();

    private int onCall(long luaState) {
        LuaState.TMP.setPointer(luaState);
        return onCall(LuaState.TMP);
    }

    public int onCall(LuaState luaState) {
        return 0;
    }

}