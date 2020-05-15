#include <jni.h>
#include <string>

void sendDataToJava(JNIEnv *env, jclass obj, jstring text) {
    jclass activityClass = env->GetObjectClass(obj);

    jmethodID setData = env->GetMethodID(activityClass,
                                                         "setDataFromJNI", "(Ljava/lang/String;)V");
    env->CallVoidMethod(obj, setData, text);
}

extern "C" JNIEXPORT void JNICALL
Java_com_olegel_androidndkqt_MainActivity_setStr(JNIEnv *env, jclass type, jstring text){
sendDataToJava(env,type,text);
}

