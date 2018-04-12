#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_app_band_runawaynation_matth_setlistmaker_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
