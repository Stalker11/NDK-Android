#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_olegel_androidndkqt_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_olegel_androidndkqt_MainActivity_myCustomString(JNIEnv* env,jobject, jstring str){
    return str;
}
