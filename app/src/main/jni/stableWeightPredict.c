//
// File: stableWeightPredict.cpp
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//

// Include Files
#include <stdio.h>
#include <stdlib.h>

#include <math.h>
#include <stddef.h>
#include <string.h>
#include <android/log.h>
#include "rt_nonfinite.h"
#include "stableWeightPredict.h"
#include "weightPredictFunction.h"
#define TAG "jni调试测试重量" // 这个是自定义的LOG的标识
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__) // 定义LOGE类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
// Function Definitions

//
// Arguments    : const double Sensor_data[42]
//                double clear_flag
//                double last_data_in_out[42]
//                double *status_flag
//                double *locked_weight
//                double *stable_counter
//                double *last_weight
//                struct0_T *Parameters
// Return Type  : double
//
double *stableWeightPredict(const double Sensor_data[42], double clear_flag,
  double *last_data_in_out, double *status_flag, double *locked_weight,
  double *stable_counter, double *last_weight, struct0_T *Parameters,double * desti)
{
  double output_weight;
  double y[42];
  double b_Sensor_data[42];
  int i3;
  double b_y[8];
  int i4;
  static const signed char iv0[14] = { 0, 11, 12, 9, 10, 5, 4, 1, 13, 6, 8, 2, 7,
    3 };

  static const signed char iv1[14] = { 7, 2, 1, 3, 4, 6, 5, 0, 8, 9, 10, 11, 12,
    13 };

  double last_data[42];
  double data_diff[14];
  double cur_weight;
  double d0;
  static const signed char iv2[8] = { 0, 1, 2, 3, 4, 5, 6, 7 };

  memcpy(&y[0], &Sensor_data[0], 42U * sizeof(double));
  for (i3 = 0; i3 < 14; i3++) {
    for (i4 = 0; i4 < 3; i4++) {
      b_Sensor_data[i4 + 3 * i3] = y[iv0[iv1[i3]] + 14 * i4];
    }
  }

  memcpy(&b_y[0], &Parameters->LR_model_adj_stage_bound_input[0], sizeof(double)
         << 3);
  for (i3 = 0; i3 < 2; i3++) {
    for (i4 = 0; i4 < 4; i4++) {
      Parameters->LR_model_adj_stage_bound[i4 + (i3 << 2)] = b_y[i3 + (i4 << 1)];
    }
  }

  memcpy(&y[0], &last_data_in_out[0], 42U * sizeof(double));
  for (i3 = 0; i3 < 14; i3++) {
    for (i4 = 0; i4 < 3; i4++) {
      last_data[i4 + 3 * i3] = y[i3 + 14 * i4];
    }
  }

  output_weight = 0.0;
  switch ((int)*status_flag) {
   case 0:
    if (clear_flag == 1.0) {
      *status_flag = 1.0;
    } else {
      for (i3 = 0; i3 < 14; i3++) {
        data_diff[i3] = b_Sensor_data[3 * i3] - last_data[3 * i3];
      }

      d0 = 0.0;
      for (i3 = 0; i3 < 8; i3++) {
        d0 += data_diff[iv2[i3]] * data_diff[i3];
      }

      if (d0 > 215.0) {
        *status_flag = 2.0;
        *stable_counter = 0.0;
      }
    }

    output_weight = *locked_weight;
    *last_weight = *locked_weight;
    break;

   case 1:
    for (i3 = 0; i3 < 14; i3++) {
      data_diff[i3] = b_Sensor_data[3 * i3] - last_data[3 * i3];
    }

    d0 = 0.0;
    for (i3 = 0; i3 < 8; i3++) {
      d0 += data_diff[iv2[i3]] * data_diff[i3];
    }

    if (d0 > 215.0) {
      *status_flag = 2.0;
      *stable_counter = 0.0;
    }

    memcpy(&last_data[0], &b_Sensor_data[0], 42U * sizeof(double));
    *last_weight = 0.0;
    if (clear_flag == 1.0) {
      *status_flag = 0.0;
      *locked_weight = 0.0;
    }
    break;

   case 2:
    cur_weight = weightPredictFunction(b_Sensor_data, Parameters);
    if (fabs(cur_weight - *last_weight) < 2.0) {
      (*stable_counter)++;
      if (*stable_counter >= 3.0) {
        if (cur_weight > 2.0) {
          *status_flag = 0.0;
          *locked_weight = cur_weight;
        } else {
          *status_flag = 1.0;
        }
      }
    } else {
      *stable_counter = 0.0;
      if ((0.0 >= cur_weight) || rtIsNaN(cur_weight)) {
        cur_weight = 0.0;
      }
    }

    output_weight = cur_weight;
    *last_weight = cur_weight;
    if (clear_flag == 1.0) {
      *status_flag = 0.0;
      *locked_weight = 0.0;
    }

    memcpy(&last_data[0], &b_Sensor_data[0], 42U * sizeof(double));
    break;
  }

  for (i3 = 0; i3 < 3; i3++) {
    for (i4 = 0; i4 < 14; i4++) {
      y[i4 + 14 * i3] = last_data[i3 + 3 * i4];
    }
  }
   LOGI("last_weight== %p", *last_weight );
  LOGI("output_weight== %p",output_weight);
  LOGI("cur_weight== %p",cur_weight);
  memcpy(&last_data_in_out[0], &y[0], 42U * sizeof(double));

   //for(int i6=0;i6<42;i6++){
    //    LOGI("y[%d]==%f\r\n",i6,y[i6]);
  // }

   //double arr[46];
    /*= {output_weight,*status_flag,*locked_weight,*stable_counter,y[0],y[1],y[2],y[3],y[4],y[5],y[6],y[7],y[8],y[9],y[10],y[11],y[12],y[13],y[14],y[15],y[16],y[17],y[18],y[19],y[20],y[21],y[22],y[23]
     ,y[24],y[25],y[26],y[27],y[28],y[29],y[30],y[31],y[32],y[33],y[34],y[35],y[36],y[37],y[38],y[39],y[40],y[41]};
     */
     desti[0]=output_weight;
     desti[1]=*status_flag;
     desti[2]=*locked_weight;
     desti[3]=*stable_counter;
     for(int i6=0;i6<42;i6++)
       desti[4+i6]=y[i6];
      LOGI("数组内存地址== %p",desti);

   return desti;
}

//
// File trailer for stableWeightPredict.cpp
//
// [EOF]
//
