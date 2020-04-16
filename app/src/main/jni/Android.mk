LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := JniLibrary

APP_PLATFORM:=android-15
LOCAL_SRC_FILES :=  cn_hand_tech_loadJNI_loadJNI.c  mean.c predictKNN1.c repmat.c rt_nonfinite.c \
rtGetInf.c rtGetNaN.c sort1.c sortIdx.c  stableWeightPredict.c stableWeightPredict_data.c stableWeightPredict_emxutil.c stableWeightPredict_initialize.c \
stableWeightPredict_terminate.c  weightPredictFunction.c zscore.c \
LOCAL_LDLIBS    += -llog
LOCAL_LDLIBS    += -landroid -lstdc++
LOCAL_ALLOW_UNDEFINED_SYMBOLS := true


include $(BUILD_SHARED_LIBRARY)