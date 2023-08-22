#include <jni.h>
#include <android\log.h>
#include <stdio.h>
#include <sys/stat.h>
#include <unistd.h>

#define LOG_DEBUG(...) __android_log_print(ANDROID_LOG_DEBUG, "jni", __VA_ARGS__);

extern "C"
JNIEXPORT void JNICALL
Java_team_digitalfairy_fileselectortest_TestCpp_testOpenFile(JNIEnv *env, jclass thiz, jint fd) {
    if(fd < 0) {
        LOG_DEBUG("fd %d invalid",fd)
        return;
    }

    int curfd = dup(fd);

    struct stat fb{};
    fstat(curfd,&fb);

    FILE *fp = fdopen(curfd,"rb");
    LOG_DEBUG("%lld blocks (%lld bytes) -> sz %lld",fb.st_blocks,fb.st_blocks*512,fb.st_size);
}