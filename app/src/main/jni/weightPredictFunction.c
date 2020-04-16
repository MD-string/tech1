//
// File: weightPredictFunction.cpp
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//

// Include Files
#include "rt_nonfinite.h"
#include "stableWeightPredict.h"
#include "weightPredictFunction.h"
#include "stableWeightPredict_emxutil.h"
#include "mean.h"
#include "predictKNN1.h"
#include "zscore.h"

// Function Definitions

//
// sensor_data       8通道传感器mvv值
//  predict_weight    预测重量
// Arguments    : const double sensor_data[42]
//                const struct0_T *Parameters
// Return Type  : double
//
double weightPredictFunction(const double sensor_data[42], const struct0_T
  *Parameters)
{
  double final_predict;
  boolean_T bv0[14];
  int i0;
  emxArray_int32_T *r0;
  int trueCount;
  int i;
  int varargin_2;
  emxArray_real_T *b_sensor_data;
  int loop_ub;
  emxArray_real_T *Scaled_sensor_data;
  boolean_T bv1[100000];
  emxArray_int32_T *r1;
  int k;
  emxArray_real_T *KNN_Model_Y;
  double y;
  emxArray_real_T *b_y;
  emxArray_real_T *KNN_Model_X;
  int b_loop_ub;
  emxArray_real_T *KNN_Model_Mu;
  emxArray_real_T *KNN_Model_Sigma;
  emxArray_int32_T *r2;
  int b_varargin_2;
  emxArray_int32_T *r3;
  int c_varargin_2;
  emxArray_int32_T *r4;
  int d_varargin_2;
  emxArray_int32_T *r5;
  int e_varargin_2;
  emxArray_int32_T *r6;
  int f_varargin_2;
  emxArray_int32_T *r7;
  int g_varargin_2;
  emxArray_int32_T *r8;
  int h_varargin_2;
  emxArray_int32_T *r9;
  emxArray_real_T *c_y;
  emxArray_real_T *d_y;
  emxArray_real_T *e_y;
  emxArray_real_T *f_y;
  emxArray_real_T *g_y;
  emxArray_real_T *h_y;
  emxArray_real_T *i_y;
  emxArray_real_T *LR_model_adj7;
  emxArray_real_T *j_y;
  emxArray_real_T *weight_model_coeff7;
  int c_loop_ub;
  int d_loop_ub;
  emxArray_real_T *b_Scaled_sensor_data;
  double estimated_loc;
  emxArray_real_T *LR_model_adj_sensors;
  emxArray_real_T *c_LR_model_weight_model_sensor_;
  emxArray_real_T *LR_model_weight_model_coeff;
  double c_sensor_data[14];
  double predict_weight_LR;
  int mm;
  int zone;
  emxArray_real_T *b_LR_model_adj7;
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->sensor_sel_for_loc[i0] != 0.0);
  }

  emxInit_int32_T(&r0, 2);
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r0->size[0] * r0->size[1];
  r0->size[0] = 1;
  r0->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r0->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  varargin_2 = r0->size[1];
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->sensor_sel_for_loc[i0] != 0.0);
  }

  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r0->size[0] * r0->size[1];
  r0->size[0] = 1;
  r0->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r0->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  emxInit_real_T(&b_sensor_data, 1);
  i0 = b_sensor_data->size[0];
  b_sensor_data->size[0] = r0->size[1];
  emxEnsureCapacity((emxArray__common *)b_sensor_data, i0, (int)sizeof(double));
  loop_ub = r0->size[1];
  for (i0 = 0; i0 < loop_ub; i0++) {
    b_sensor_data->data[i0] = sensor_data[3 * ((int)
      Parameters->sensor_sel_for_loc[r0->data[r0->size[0] * i0] - 1] - 1)];
  }

  emxInit_real_T(&Scaled_sensor_data, 1);
  zscore(b_sensor_data, Scaled_sensor_data);

  // %%
  emxFree_real_T(&b_sensor_data);
  for (i0 = 0; i0 < 100000; i0++) {
    bv1[i0] = (Parameters->KNN_Model.Y_input[i0] != 0.0);
  }

  trueCount = 0;
  for (i = 0; i < 100000; i++) {
    if (bv1[i]) {
      trueCount++;
    }
  }

  i0 = r0->size[0] * r0->size[1];
  r0->size[0] = 1;
  r0->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 100000; i++) {
    if (bv1[i]) {
      r0->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  emxInit_int32_T1(&r1, 1);
  i0 = r1->size[0];
  r1->size[0] = r0->size[1];
  emxEnsureCapacity((emxArray__common *)r1, i0, (int)sizeof(int));
  loop_ub = r0->size[1];
  for (i0 = 0; i0 < loop_ub; i0++) {
    r1->data[i0] = r0->data[r0->size[0] * i0];
  }

  k = r1->size[0];
  emxFree_int32_T(&r1);
  for (i0 = 0; i0 < 100000; i0++) {
    bv1[i0] = (Parameters->KNN_Model.Y_input[i0] != 0.0);
  }

  trueCount = 0;
  for (i = 0; i < 100000; i++) {
    if (bv1[i]) {
      trueCount++;
    }
  }

  i0 = r0->size[0] * r0->size[1];
  r0->size[0] = 1;
  r0->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 100000; i++) {
    if (bv1[i]) {
      r0->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  emxInit_real_T(&KNN_Model_Y, 1);
  i0 = KNN_Model_Y->size[0];
  KNN_Model_Y->size[0] = r0->size[1];
  emxEnsureCapacity((emxArray__common *)KNN_Model_Y, i0, (int)sizeof(double));
  loop_ub = r0->size[1];
  for (i0 = 0; i0 < loop_ub; i0++) {
    KNN_Model_Y->data[i0] = Parameters->KNN_Model.Y_input[r0->data[r0->size[0] *
      i0] - 1];
  }

  y = (double)k * (double)varargin_2;
  if (1.0 > y) {
    i0 = 0;
  } else {
    i0 = (int)y;
  }

  emxInit_real_T1(&b_y, 2);
  i = b_y->size[0] * b_y->size[1];
  b_y->size[0] = varargin_2;
  b_y->size[1] = k;
  emxEnsureCapacity((emxArray__common *)b_y, i, (int)sizeof(double));
  for (k = 1; k <= i0; k++) {
    b_y->data[k - 1] = Parameters->KNN_Model.X_input[k - 1];
  }

  emxInit_real_T1(&KNN_Model_X, 2);
  i0 = KNN_Model_X->size[0] * KNN_Model_X->size[1];
  KNN_Model_X->size[0] = b_y->size[1];
  KNN_Model_X->size[1] = b_y->size[0];
  emxEnsureCapacity((emxArray__common *)KNN_Model_X, i0, (int)sizeof(double));
  loop_ub = b_y->size[0];
  for (i0 = 0; i0 < loop_ub; i0++) {
    b_loop_ub = b_y->size[1];
    for (i = 0; i < b_loop_ub; i++) {
      KNN_Model_X->data[i + KNN_Model_X->size[0] * i0] = b_y->data[i0 +
        b_y->size[0] * i];
    }
  }

  emxFree_real_T(&b_y);
  if (1 > varargin_2) {
    loop_ub = 0;
  } else {
    loop_ub = varargin_2;
  }

  emxInit_real_T1(&KNN_Model_Mu, 2);
  i0 = KNN_Model_Mu->size[0] * KNN_Model_Mu->size[1];
  KNN_Model_Mu->size[0] = 1;
  KNN_Model_Mu->size[1] = loop_ub;
  emxEnsureCapacity((emxArray__common *)KNN_Model_Mu, i0, (int)sizeof(double));
  for (i0 = 0; i0 < loop_ub; i0++) {
    KNN_Model_Mu->data[KNN_Model_Mu->size[0] * i0] = Parameters->KNN_Model.Mu[i0];
  }

  if (1 > varargin_2) {
    loop_ub = 0;
  } else {
    loop_ub = varargin_2;
  }

  emxInit_real_T1(&KNN_Model_Sigma, 2);
  i0 = KNN_Model_Sigma->size[0] * KNN_Model_Sigma->size[1];
  KNN_Model_Sigma->size[0] = 1;
  KNN_Model_Sigma->size[1] = loop_ub;
  emxEnsureCapacity((emxArray__common *)KNN_Model_Sigma, i0, (int)sizeof(double));
  for (i0 = 0; i0 < loop_ub; i0++) {
    KNN_Model_Sigma->data[KNN_Model_Sigma->size[0] * i0] =
      Parameters->KNN_Model.Sigma[i0];
  }

  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors1[i0] != 0.0);
  }

  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r0->size[0] * r0->size[1];
  r0->size[0] = 1;
  r0->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r0->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  varargin_2 = r0->size[1];
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors2[i0] != 0.0);
  }

  emxInit_int32_T(&r2, 2);
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r2->size[0] * r2->size[1];
  r2->size[0] = 1;
  r2->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r2, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r2->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  b_varargin_2 = r2->size[1];
  emxFree_int32_T(&r2);
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors3[i0] != 0.0);
  }

  emxInit_int32_T(&r3, 2);
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r3->size[0] * r3->size[1];
  r3->size[0] = 1;
  r3->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r3, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r3->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  c_varargin_2 = r3->size[1];
  emxFree_int32_T(&r3);
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors4[i0] != 0.0);
  }

  emxInit_int32_T(&r4, 2);
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r4->size[0] * r4->size[1];
  r4->size[0] = 1;
  r4->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r4, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r4->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  d_varargin_2 = r4->size[1];
  emxFree_int32_T(&r4);
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors5[i0] != 0.0);
  }

  emxInit_int32_T(&r5, 2);
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r5->size[0] * r5->size[1];
  r5->size[0] = 1;
  r5->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r5, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r5->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  e_varargin_2 = r5->size[1];
  emxFree_int32_T(&r5);
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors6[i0] != 0.0);
  }

  emxInit_int32_T(&r6, 2);
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r6->size[0] * r6->size[1];
  r6->size[0] = 1;
  r6->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r6, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r6->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  f_varargin_2 = r6->size[1];
  emxFree_int32_T(&r6);
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors7[i0] != 0.0);
  }

  emxInit_int32_T(&r7, 2);
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r7->size[0] * r7->size[1];
  r7->size[0] = 1;
  r7->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r7, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r7->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  g_varargin_2 = r7->size[1];
  emxFree_int32_T(&r7);
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors8[i0] != 0.0);
  }

  emxInit_int32_T(&r8, 2);
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r8->size[0] * r8->size[1];
  r8->size[0] = 1;
  r8->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r8, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r8->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  h_varargin_2 = r8->size[1];
  emxFree_int32_T(&r8);
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors9[i0] != 0.0);
  }

  emxInit_int32_T(&r9, 2);
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r9->size[0] * r9->size[1];
  r9->size[0] = 1;
  r9->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r9, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r9->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  emxInit_real_T1(&c_y, 2);
  trueCount = r9->size[1];
  y = 4.0 * ((double)varargin_2 + 1.0);
  i0 = c_y->size[0] * c_y->size[1];
  c_y->size[0] = varargin_2 + 1;
  c_y->size[1] = 4;
  emxEnsureCapacity((emxArray__common *)c_y, i0, (int)sizeof(double));
  k = 1;
  emxFree_int32_T(&r9);
  while (k <= (int)y) {
    c_y->data[k - 1] = Parameters->LR_model_adj1_input[k - 1];
    k++;
  }

  emxInit_real_T1(&d_y, 2);
  y = 4.0 * ((double)b_varargin_2 + 1.0);
  i0 = d_y->size[0] * d_y->size[1];
  d_y->size[0] = b_varargin_2 + 1;
  d_y->size[1] = 4;
  emxEnsureCapacity((emxArray__common *)d_y, i0, (int)sizeof(double));
  for (k = 1; k <= (int)y; k++) {
    d_y->data[k - 1] = Parameters->LR_model_adj2_input[k - 1];
  }

  emxInit_real_T1(&e_y, 2);
  y = 4.0 * ((double)c_varargin_2 + 1.0);
  i0 = e_y->size[0] * e_y->size[1];
  e_y->size[0] = c_varargin_2 + 1;
  e_y->size[1] = 4;
  emxEnsureCapacity((emxArray__common *)e_y, i0, (int)sizeof(double));
  for (k = 1; k <= (int)y; k++) {
    e_y->data[k - 1] = Parameters->LR_model_adj3_input[k - 1];
  }

  emxInit_real_T1(&f_y, 2);
  y = 4.0 * ((double)d_varargin_2 + 1.0);
  i0 = f_y->size[0] * f_y->size[1];
  f_y->size[0] = d_varargin_2 + 1;
  f_y->size[1] = 4;
  emxEnsureCapacity((emxArray__common *)f_y, i0, (int)sizeof(double));
  for (k = 1; k <= (int)y; k++) {
    f_y->data[k - 1] = Parameters->LR_model_adj4_input[k - 1];
  }

  emxInit_real_T1(&g_y, 2);
  y = 4.0 * ((double)e_varargin_2 + 1.0);
  i0 = g_y->size[0] * g_y->size[1];
  g_y->size[0] = e_varargin_2 + 1;
  g_y->size[1] = 4;
  emxEnsureCapacity((emxArray__common *)g_y, i0, (int)sizeof(double));
  for (k = 1; k <= (int)y; k++) {
    g_y->data[k - 1] = Parameters->LR_model_adj5_input[k - 1];
  }

  emxInit_real_T1(&h_y, 2);
  y = 4.0 * ((double)f_varargin_2 + 1.0);
  i0 = h_y->size[0] * h_y->size[1];
  h_y->size[0] = f_varargin_2 + 1;
  h_y->size[1] = 4;
  emxEnsureCapacity((emxArray__common *)h_y, i0, (int)sizeof(double));
  for (k = 1; k <= (int)y; k++) {
    h_y->data[k - 1] = Parameters->LR_model_adj6_input[k - 1];
  }

  emxInit_real_T1(&i_y, 2);
  y = 4.0 * ((double)g_varargin_2 + 1.0);
  i0 = i_y->size[0] * i_y->size[1];
  i_y->size[0] = g_varargin_2 + 1;
  i_y->size[1] = 4;
  emxEnsureCapacity((emxArray__common *)i_y, i0, (int)sizeof(double));
  for (k = 1; k <= (int)y; k++) {
    i_y->data[k - 1] = Parameters->LR_model_adj7_input[k - 1];
  }

  emxInit_real_T1(&LR_model_adj7, 2);
  i0 = LR_model_adj7->size[0] * LR_model_adj7->size[1];
  LR_model_adj7->size[0] = 4;
  LR_model_adj7->size[1] = i_y->size[0];
  emxEnsureCapacity((emxArray__common *)LR_model_adj7, i0, (int)sizeof(double));
  loop_ub = i_y->size[0];
  for (i0 = 0; i0 < loop_ub; i0++) {
    for (i = 0; i < 4; i++) {
      LR_model_adj7->data[i + LR_model_adj7->size[0] * i0] = i_y->data[i0 +
        i_y->size[0] * i];
    }
  }

  y = 4.0 * ((double)h_varargin_2 + 1.0);
  i0 = i_y->size[0] * i_y->size[1];
  i_y->size[0] = h_varargin_2 + 1;
  i_y->size[1] = 4;
  emxEnsureCapacity((emxArray__common *)i_y, i0, (int)sizeof(double));
  for (k = 1; k <= (int)y; k++) {
    i_y->data[k - 1] = Parameters->LR_model_adj8_input[k - 1];
  }

  emxInit_real_T1(&j_y, 2);
  y = 4.0 * ((double)trueCount + 1.0);
  i0 = j_y->size[0] * j_y->size[1];
  j_y->size[0] = trueCount + 1;
  j_y->size[1] = 4;
  emxEnsureCapacity((emxArray__common *)j_y, i0, (int)sizeof(double));
  for (k = 1; k <= (int)y; k++) {
    j_y->data[k - 1] = Parameters->LR_model_adj9_input[k - 1];
  }

  emxInit_real_T1(&weight_model_coeff7, 2);
  loop_ub = (int)(varargin_2 + 1U);
  b_loop_ub = (int)(b_varargin_2 + 1U);
  c_loop_ub = (int)(c_varargin_2 + 1U);
  d_loop_ub = (int)(d_varargin_2 + 1U);
  d_varargin_2 = (int)(e_varargin_2 + 1U);
  varargin_2 = (int)(f_varargin_2 + 1U);
  c_varargin_2 = (int)(g_varargin_2 + 1U);
  i0 = weight_model_coeff7->size[0] * weight_model_coeff7->size[1];
  weight_model_coeff7->size[0] = 1;
  weight_model_coeff7->size[1] = c_varargin_2;
  emxEnsureCapacity((emxArray__common *)weight_model_coeff7, i0, (int)sizeof
                    (double));
  for (i0 = 0; i0 < c_varargin_2; i0++) {
    weight_model_coeff7->data[weight_model_coeff7->size[0] * i0] =
      Parameters->weight_model_coeff7[i0];
  }

  emxInit_real_T1(&b_Scaled_sensor_data, 2);
  c_varargin_2 = (int)(h_varargin_2 + 1U);
  b_varargin_2 = (int)(trueCount + 1U);
  i0 = b_Scaled_sensor_data->size[0] * b_Scaled_sensor_data->size[1];
  b_Scaled_sensor_data->size[0] = 1;
  b_Scaled_sensor_data->size[1] = Scaled_sensor_data->size[0];
  emxEnsureCapacity((emxArray__common *)b_Scaled_sensor_data, i0, (int)sizeof
                    (double));
  k = Scaled_sensor_data->size[0];
  for (i0 = 0; i0 < k; i0++) {
    b_Scaled_sensor_data->data[b_Scaled_sensor_data->size[0] * i0] =
      Scaled_sensor_data->data[i0];
  }

  estimated_loc = predictKNN1(KNN_Model_Y, KNN_Model_X, KNN_Model_Mu,
    KNN_Model_Sigma, b_Scaled_sensor_data);
  emxFree_real_T(&b_Scaled_sensor_data);
  emxFree_real_T(&KNN_Model_Sigma);
  emxFree_real_T(&KNN_Model_Mu);
  emxFree_real_T(&KNN_Model_X);
  emxFree_real_T(&KNN_Model_Y);
  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors7[i0] != 0.0);
  }

  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r0->size[0] * r0->size[1];
  r0->size[0] = 1;
  r0->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r0->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  emxInit_real_T1(&LR_model_adj_sensors, 2);
  i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
  LR_model_adj_sensors->size[0] = 1;
  LR_model_adj_sensors->size[1] = r0->size[1];
  emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                    (double));
  k = r0->size[0] * r0->size[1];
  for (i0 = 0; i0 < k; i0++) {
    LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors7[r0->
      data[i0] - 1];
  }

  for (i0 = 0; i0 < 14; i0++) {
    bv0[i0] = (Parameters->LR_model_adj_sensors7[i0] != 0.0);
  }

  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      trueCount++;
    }
  }

  i0 = r0->size[0] * r0->size[1];
  r0->size[0] = 1;
  r0->size[1] = trueCount;
  emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
  trueCount = 0;
  for (i = 0; i < 14; i++) {
    if (bv0[i]) {
      r0->data[trueCount] = i + 1;
      trueCount++;
    }
  }

  emxInit_real_T1(&c_LR_model_weight_model_sensor_, 2);
  i0 = c_LR_model_weight_model_sensor_->size[0] *
    c_LR_model_weight_model_sensor_->size[1];
  c_LR_model_weight_model_sensor_->size[0] = 1;
  c_LR_model_weight_model_sensor_->size[1] = r0->size[1];
  emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                    (int)sizeof(double));
  k = r0->size[0] * r0->size[1];
  for (i0 = 0; i0 < k; i0++) {
    c_LR_model_weight_model_sensor_->data[i0] =
      Parameters->LR_model_adj_sensors7[r0->data[i0] - 1];
  }

  emxInit_real_T1(&LR_model_weight_model_coeff, 2);
  i0 = LR_model_weight_model_coeff->size[0] * LR_model_weight_model_coeff->size
    [1];
  LR_model_weight_model_coeff->size[0] = 1;
  LR_model_weight_model_coeff->size[1] = weight_model_coeff7->size[1];
  emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                    sizeof(double));
  k = weight_model_coeff7->size[0] * weight_model_coeff7->size[1];
  for (i0 = 0; i0 < k; i0++) {
    LR_model_weight_model_coeff->data[i0] = weight_model_coeff7->data[i0];
  }

  switch ((int)estimated_loc) {
   case 1:
    i0 = LR_model_adj7->size[0] * LR_model_adj7->size[1];
    LR_model_adj7->size[0] = 4;
    LR_model_adj7->size[1] = c_y->size[0];
    emxEnsureCapacity((emxArray__common *)LR_model_adj7, i0, (int)sizeof(double));
    b_loop_ub = c_y->size[0];
    for (i0 = 0; i0 < b_loop_ub; i0++) {
      for (i = 0; i < 4; i++) {
        LR_model_adj7->data[i + LR_model_adj7->size[0] * i0] = c_y->data[i0 +
          c_y->size[0] * i];
      }
    }

    for (i0 = 0; i0 < 14; i0++) {
      bv0[i0] = (Parameters->LR_model_adj_sensors1[i0] != 0.0);
    }

    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        trueCount++;
      }
    }

    i0 = r0->size[0] * r0->size[1];
    r0->size[0] = 1;
    r0->size[1] = trueCount;
    emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        r0->data[trueCount] = i + 1;
        trueCount++;
      }
    }

    i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    LR_model_adj_sensors->size[0] = 1;
    LR_model_adj_sensors->size[1] = r0->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                      (double));
    b_loop_ub = r0->size[0] * r0->size[1];
    for (i0 = 0; i0 < b_loop_ub; i0++) {
      LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors1
        [r0->data[i0] - 1];
    }

    i0 = c_LR_model_weight_model_sensor_->size[0] *
      c_LR_model_weight_model_sensor_->size[1];
    c_LR_model_weight_model_sensor_->size[0] = 1;
    c_LR_model_weight_model_sensor_->size[1] = LR_model_adj_sensors->size[1];
    emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                      (int)sizeof(double));
    b_loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    for (i0 = 0; i0 < b_loop_ub; i0++) {
      c_LR_model_weight_model_sensor_->data[i0] = LR_model_adj_sensors->data[i0];
    }

    i0 = LR_model_weight_model_coeff->size[0] *
      LR_model_weight_model_coeff->size[1];
    LR_model_weight_model_coeff->size[0] = 1;
    LR_model_weight_model_coeff->size[1] = loop_ub;
    emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                      sizeof(double));
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_weight_model_coeff->data[LR_model_weight_model_coeff->size[0] *
        i0] = Parameters->weight_model_coeff1[i0];
    }
    break;

   case 2:
    i0 = LR_model_adj7->size[0] * LR_model_adj7->size[1];
    LR_model_adj7->size[0] = 4;
    LR_model_adj7->size[1] = d_y->size[0];
    emxEnsureCapacity((emxArray__common *)LR_model_adj7, i0, (int)sizeof(double));
    loop_ub = d_y->size[0];
    for (i0 = 0; i0 < loop_ub; i0++) {
      for (i = 0; i < 4; i++) {
        LR_model_adj7->data[i + LR_model_adj7->size[0] * i0] = d_y->data[i0 +
          d_y->size[0] * i];
      }
    }

    for (i0 = 0; i0 < 14; i0++) {
      bv0[i0] = (Parameters->LR_model_adj_sensors2[i0] != 0.0);
    }

    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        trueCount++;
      }
    }

    i0 = r0->size[0] * r0->size[1];
    r0->size[0] = 1;
    r0->size[1] = trueCount;
    emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        r0->data[trueCount] = i + 1;
        trueCount++;
      }
    }

    i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    LR_model_adj_sensors->size[0] = 1;
    LR_model_adj_sensors->size[1] = r0->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                      (double));
    loop_ub = r0->size[0] * r0->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors2
        [r0->data[i0] - 1];
    }

    i0 = c_LR_model_weight_model_sensor_->size[0] *
      c_LR_model_weight_model_sensor_->size[1];
    c_LR_model_weight_model_sensor_->size[0] = 1;
    c_LR_model_weight_model_sensor_->size[1] = LR_model_adj_sensors->size[1];
    emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                      (int)sizeof(double));
    loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      c_LR_model_weight_model_sensor_->data[i0] = LR_model_adj_sensors->data[i0];
    }

    i0 = LR_model_weight_model_coeff->size[0] *
      LR_model_weight_model_coeff->size[1];
    LR_model_weight_model_coeff->size[0] = 1;
    LR_model_weight_model_coeff->size[1] = b_loop_ub;
    emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                      sizeof(double));
    for (i0 = 0; i0 < b_loop_ub; i0++) {
      LR_model_weight_model_coeff->data[LR_model_weight_model_coeff->size[0] *
        i0] = Parameters->weight_model_coeff2[i0];
    }
    break;

   case 3:
    i0 = LR_model_adj7->size[0] * LR_model_adj7->size[1];
    LR_model_adj7->size[0] = 4;
    LR_model_adj7->size[1] = e_y->size[0];
    emxEnsureCapacity((emxArray__common *)LR_model_adj7, i0, (int)sizeof(double));
    loop_ub = e_y->size[0];
    for (i0 = 0; i0 < loop_ub; i0++) {
      for (i = 0; i < 4; i++) {
        LR_model_adj7->data[i + LR_model_adj7->size[0] * i0] = e_y->data[i0 +
          e_y->size[0] * i];
      }
    }

    for (i0 = 0; i0 < 14; i0++) {
      bv0[i0] = (Parameters->LR_model_adj_sensors3[i0] != 0.0);
    }

    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        trueCount++;
      }
    }

    i0 = r0->size[0] * r0->size[1];
    r0->size[0] = 1;
    r0->size[1] = trueCount;
    emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        r0->data[trueCount] = i + 1;
        trueCount++;
      }
    }

    i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    LR_model_adj_sensors->size[0] = 1;
    LR_model_adj_sensors->size[1] = r0->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                      (double));
    loop_ub = r0->size[0] * r0->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors3
        [r0->data[i0] - 1];
    }

    i0 = c_LR_model_weight_model_sensor_->size[0] *
      c_LR_model_weight_model_sensor_->size[1];
    c_LR_model_weight_model_sensor_->size[0] = 1;
    c_LR_model_weight_model_sensor_->size[1] = LR_model_adj_sensors->size[1];
    emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                      (int)sizeof(double));
    loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      c_LR_model_weight_model_sensor_->data[i0] = LR_model_adj_sensors->data[i0];
    }

    i0 = LR_model_weight_model_coeff->size[0] *
      LR_model_weight_model_coeff->size[1];
    LR_model_weight_model_coeff->size[0] = 1;
    LR_model_weight_model_coeff->size[1] = c_loop_ub;
    emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                      sizeof(double));
    for (i0 = 0; i0 < c_loop_ub; i0++) {
      LR_model_weight_model_coeff->data[LR_model_weight_model_coeff->size[0] *
        i0] = Parameters->weight_model_coeff3[i0];
    }
    break;

   case 4:
    i0 = LR_model_adj7->size[0] * LR_model_adj7->size[1];
    LR_model_adj7->size[0] = 4;
    LR_model_adj7->size[1] = f_y->size[0];
    emxEnsureCapacity((emxArray__common *)LR_model_adj7, i0, (int)sizeof(double));
    loop_ub = f_y->size[0];
    for (i0 = 0; i0 < loop_ub; i0++) {
      for (i = 0; i < 4; i++) {
        LR_model_adj7->data[i + LR_model_adj7->size[0] * i0] = f_y->data[i0 +
          f_y->size[0] * i];
      }
    }

    for (i0 = 0; i0 < 14; i0++) {
      bv0[i0] = (Parameters->LR_model_adj_sensors4[i0] != 0.0);
    }

    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        trueCount++;
      }
    }

    i0 = r0->size[0] * r0->size[1];
    r0->size[0] = 1;
    r0->size[1] = trueCount;
    emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        r0->data[trueCount] = i + 1;
        trueCount++;
      }
    }

    i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    LR_model_adj_sensors->size[0] = 1;
    LR_model_adj_sensors->size[1] = r0->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                      (double));
    loop_ub = r0->size[0] * r0->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors4
        [r0->data[i0] - 1];
    }

    i0 = c_LR_model_weight_model_sensor_->size[0] *
      c_LR_model_weight_model_sensor_->size[1];
    c_LR_model_weight_model_sensor_->size[0] = 1;
    c_LR_model_weight_model_sensor_->size[1] = LR_model_adj_sensors->size[1];
    emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                      (int)sizeof(double));
    loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      c_LR_model_weight_model_sensor_->data[i0] = LR_model_adj_sensors->data[i0];
    }

    i0 = LR_model_weight_model_coeff->size[0] *
      LR_model_weight_model_coeff->size[1];
    LR_model_weight_model_coeff->size[0] = 1;
    LR_model_weight_model_coeff->size[1] = d_loop_ub;
    emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                      sizeof(double));
    for (i0 = 0; i0 < d_loop_ub; i0++) {
      LR_model_weight_model_coeff->data[LR_model_weight_model_coeff->size[0] *
        i0] = Parameters->weight_model_coeff4[i0];
    }
    break;

   case 5:
    i0 = LR_model_adj7->size[0] * LR_model_adj7->size[1];
    LR_model_adj7->size[0] = 4;
    LR_model_adj7->size[1] = g_y->size[0];
    emxEnsureCapacity((emxArray__common *)LR_model_adj7, i0, (int)sizeof(double));
    loop_ub = g_y->size[0];
    for (i0 = 0; i0 < loop_ub; i0++) {
      for (i = 0; i < 4; i++) {
        LR_model_adj7->data[i + LR_model_adj7->size[0] * i0] = g_y->data[i0 +
          g_y->size[0] * i];
      }
    }

    for (i0 = 0; i0 < 14; i0++) {
      bv0[i0] = (Parameters->LR_model_adj_sensors5[i0] != 0.0);
    }

    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        trueCount++;
      }
    }

    i0 = r0->size[0] * r0->size[1];
    r0->size[0] = 1;
    r0->size[1] = trueCount;
    emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        r0->data[trueCount] = i + 1;
        trueCount++;
      }
    }

    i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    LR_model_adj_sensors->size[0] = 1;
    LR_model_adj_sensors->size[1] = r0->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                      (double));
    loop_ub = r0->size[0] * r0->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors5
        [r0->data[i0] - 1];
    }

    i0 = c_LR_model_weight_model_sensor_->size[0] *
      c_LR_model_weight_model_sensor_->size[1];
    c_LR_model_weight_model_sensor_->size[0] = 1;
    c_LR_model_weight_model_sensor_->size[1] = LR_model_adj_sensors->size[1];
    emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                      (int)sizeof(double));
    loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      c_LR_model_weight_model_sensor_->data[i0] = LR_model_adj_sensors->data[i0];
    }

    i0 = LR_model_weight_model_coeff->size[0] *
      LR_model_weight_model_coeff->size[1];
    LR_model_weight_model_coeff->size[0] = 1;
    LR_model_weight_model_coeff->size[1] = d_varargin_2;
    emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                      sizeof(double));
    for (i0 = 0; i0 < d_varargin_2; i0++) {
      LR_model_weight_model_coeff->data[LR_model_weight_model_coeff->size[0] *
        i0] = Parameters->weight_model_coeff5[i0];
    }
    break;

   case 6:
    i0 = LR_model_adj7->size[0] * LR_model_adj7->size[1];
    LR_model_adj7->size[0] = 4;
    LR_model_adj7->size[1] = h_y->size[0];
    emxEnsureCapacity((emxArray__common *)LR_model_adj7, i0, (int)sizeof(double));
    loop_ub = h_y->size[0];
    for (i0 = 0; i0 < loop_ub; i0++) {
      for (i = 0; i < 4; i++) {
        LR_model_adj7->data[i + LR_model_adj7->size[0] * i0] = h_y->data[i0 +
          h_y->size[0] * i];
      }
    }

    for (i0 = 0; i0 < 14; i0++) {
      bv0[i0] = (Parameters->LR_model_adj_sensors6[i0] != 0.0);
    }

    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        trueCount++;
      }
    }

    i0 = r0->size[0] * r0->size[1];
    r0->size[0] = 1;
    r0->size[1] = trueCount;
    emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        r0->data[trueCount] = i + 1;
        trueCount++;
      }
    }

    i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    LR_model_adj_sensors->size[0] = 1;
    LR_model_adj_sensors->size[1] = r0->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                      (double));
    loop_ub = r0->size[0] * r0->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors6
        [r0->data[i0] - 1];
    }

    i0 = c_LR_model_weight_model_sensor_->size[0] *
      c_LR_model_weight_model_sensor_->size[1];
    c_LR_model_weight_model_sensor_->size[0] = 1;
    c_LR_model_weight_model_sensor_->size[1] = LR_model_adj_sensors->size[1];
    emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                      (int)sizeof(double));
    loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      c_LR_model_weight_model_sensor_->data[i0] = LR_model_adj_sensors->data[i0];
    }

    i0 = LR_model_weight_model_coeff->size[0] *
      LR_model_weight_model_coeff->size[1];
    LR_model_weight_model_coeff->size[0] = 1;
    LR_model_weight_model_coeff->size[1] = varargin_2;
    emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                      sizeof(double));
    for (i0 = 0; i0 < varargin_2; i0++) {
      LR_model_weight_model_coeff->data[LR_model_weight_model_coeff->size[0] *
        i0] = Parameters->weight_model_coeff6[i0];
    }
    break;

   case 7:
    for (i0 = 0; i0 < 14; i0++) {
      bv0[i0] = (Parameters->LR_model_adj_sensors7[i0] != 0.0);
    }

    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        trueCount++;
      }
    }

    i0 = r0->size[0] * r0->size[1];
    r0->size[0] = 1;
    r0->size[1] = trueCount;
    emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        r0->data[trueCount] = i + 1;
        trueCount++;
      }
    }

    i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    LR_model_adj_sensors->size[0] = 1;
    LR_model_adj_sensors->size[1] = r0->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                      (double));
    loop_ub = r0->size[0] * r0->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors7
        [r0->data[i0] - 1];
    }

    i0 = c_LR_model_weight_model_sensor_->size[0] *
      c_LR_model_weight_model_sensor_->size[1];
    c_LR_model_weight_model_sensor_->size[0] = 1;
    c_LR_model_weight_model_sensor_->size[1] = LR_model_adj_sensors->size[1];
    emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                      (int)sizeof(double));
    loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      c_LR_model_weight_model_sensor_->data[i0] = LR_model_adj_sensors->data[i0];
    }

    i0 = LR_model_weight_model_coeff->size[0] *
      LR_model_weight_model_coeff->size[1];
    LR_model_weight_model_coeff->size[0] = 1;
    LR_model_weight_model_coeff->size[1] = weight_model_coeff7->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                      sizeof(double));
    loop_ub = weight_model_coeff7->size[0] * weight_model_coeff7->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_weight_model_coeff->data[i0] = weight_model_coeff7->data[i0];
    }
    break;

   case 8:
    i0 = LR_model_adj7->size[0] * LR_model_adj7->size[1];
    LR_model_adj7->size[0] = 4;
    LR_model_adj7->size[1] = i_y->size[0];
    emxEnsureCapacity((emxArray__common *)LR_model_adj7, i0, (int)sizeof(double));
    loop_ub = i_y->size[0];
    for (i0 = 0; i0 < loop_ub; i0++) {
      for (i = 0; i < 4; i++) {
        LR_model_adj7->data[i + LR_model_adj7->size[0] * i0] = i_y->data[i0 +
          i_y->size[0] * i];
      }
    }

    for (i0 = 0; i0 < 14; i0++) {
      bv0[i0] = (Parameters->LR_model_adj_sensors8[i0] != 0.0);
    }

    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        trueCount++;
      }
    }

    i0 = r0->size[0] * r0->size[1];
    r0->size[0] = 1;
    r0->size[1] = trueCount;
    emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        r0->data[trueCount] = i + 1;
        trueCount++;
      }
    }

    i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    LR_model_adj_sensors->size[0] = 1;
    LR_model_adj_sensors->size[1] = r0->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                      (double));
    loop_ub = r0->size[0] * r0->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors8
        [r0->data[i0] - 1];
    }

    i0 = c_LR_model_weight_model_sensor_->size[0] *
      c_LR_model_weight_model_sensor_->size[1];
    c_LR_model_weight_model_sensor_->size[0] = 1;
    c_LR_model_weight_model_sensor_->size[1] = LR_model_adj_sensors->size[1];
    emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                      (int)sizeof(double));
    loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      c_LR_model_weight_model_sensor_->data[i0] = LR_model_adj_sensors->data[i0];
    }

    i0 = LR_model_weight_model_coeff->size[0] *
      LR_model_weight_model_coeff->size[1];
    LR_model_weight_model_coeff->size[0] = 1;
    LR_model_weight_model_coeff->size[1] = c_varargin_2;
    emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                      sizeof(double));
    for (i0 = 0; i0 < c_varargin_2; i0++) {
      LR_model_weight_model_coeff->data[LR_model_weight_model_coeff->size[0] *
        i0] = Parameters->weight_model_coeff8[i0];
    }
    break;

   case 9:
    i0 = LR_model_adj7->size[0] * LR_model_adj7->size[1];
    LR_model_adj7->size[0] = 4;
    LR_model_adj7->size[1] = j_y->size[0];
    emxEnsureCapacity((emxArray__common *)LR_model_adj7, i0, (int)sizeof(double));
    loop_ub = j_y->size[0];
    for (i0 = 0; i0 < loop_ub; i0++) {
      for (i = 0; i < 4; i++) {
        LR_model_adj7->data[i + LR_model_adj7->size[0] * i0] = j_y->data[i0 +
          j_y->size[0] * i];
      }
    }

    for (i0 = 0; i0 < 14; i0++) {
      bv0[i0] = (Parameters->LR_model_adj_sensors9[i0] != 0.0);
    }

    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        trueCount++;
      }
    }

    i0 = r0->size[0] * r0->size[1];
    r0->size[0] = 1;
    r0->size[1] = trueCount;
    emxEnsureCapacity((emxArray__common *)r0, i0, (int)sizeof(int));
    trueCount = 0;
    for (i = 0; i < 14; i++) {
      if (bv0[i]) {
        r0->data[trueCount] = i + 1;
        trueCount++;
      }
    }

    i0 = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    LR_model_adj_sensors->size[0] = 1;
    LR_model_adj_sensors->size[1] = r0->size[1];
    emxEnsureCapacity((emxArray__common *)LR_model_adj_sensors, i0, (int)sizeof
                      (double));
    loop_ub = r0->size[0] * r0->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      LR_model_adj_sensors->data[i0] = Parameters->LR_model_adj_sensors9
        [r0->data[i0] - 1];
    }

    i0 = c_LR_model_weight_model_sensor_->size[0] *
      c_LR_model_weight_model_sensor_->size[1];
    c_LR_model_weight_model_sensor_->size[0] = 1;
    c_LR_model_weight_model_sensor_->size[1] = LR_model_adj_sensors->size[1];
    emxEnsureCapacity((emxArray__common *)c_LR_model_weight_model_sensor_, i0,
                      (int)sizeof(double));
    loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
    for (i0 = 0; i0 < loop_ub; i0++) {
      c_LR_model_weight_model_sensor_->data[i0] = LR_model_adj_sensors->data[i0];
    }

    i0 = LR_model_weight_model_coeff->size[0] *
      LR_model_weight_model_coeff->size[1];
    LR_model_weight_model_coeff->size[0] = 1;
    LR_model_weight_model_coeff->size[1] = b_varargin_2;
    emxEnsureCapacity((emxArray__common *)LR_model_weight_model_coeff, i0, (int)
                      sizeof(double));
    for (i0 = 0; i0 < b_varargin_2; i0++) {
      LR_model_weight_model_coeff->data[LR_model_weight_model_coeff->size[0] *
        i0] = Parameters->weight_model_coeff9[i0];
    }
    break;
  }

  emxFree_real_T(&j_y);
  emxFree_real_T(&i_y);
  emxFree_real_T(&h_y);
  emxFree_real_T(&g_y);
  emxFree_real_T(&f_y);
  emxFree_real_T(&e_y);
  emxFree_real_T(&d_y);
  emxFree_real_T(&c_y);
  emxFree_int32_T(&r0);
  mean(sensor_data, c_sensor_data);
  i0 = weight_model_coeff7->size[0] * weight_model_coeff7->size[1];
  weight_model_coeff7->size[0] = 1;
  weight_model_coeff7->size[1] = c_LR_model_weight_model_sensor_->size[1];
  emxEnsureCapacity((emxArray__common *)weight_model_coeff7, i0, (int)sizeof
                    (double));
  loop_ub = c_LR_model_weight_model_sensor_->size[0] *
    c_LR_model_weight_model_sensor_->size[1];
  for (i0 = 0; i0 < loop_ub; i0++) {
    weight_model_coeff7->data[i0] = c_sensor_data[(int)
      c_LR_model_weight_model_sensor_->data[i0] - 1];
  }

  if (2 > LR_model_weight_model_coeff->size[1]) {
    i0 = 1;
    i = 1;
  } else {
    i0 = 2;
    i = LR_model_weight_model_coeff->size[1] + 1;
  }

  k = Scaled_sensor_data->size[0];
  Scaled_sensor_data->size[0] = i - i0;
  emxEnsureCapacity((emxArray__common *)Scaled_sensor_data, k, (int)sizeof
                    (double));
  loop_ub = i - i0;
  for (k = 0; k < loop_ub; k++) {
    Scaled_sensor_data->data[k] = LR_model_weight_model_coeff->data[(i0 + k) - 1];
  }

  if ((c_LR_model_weight_model_sensor_->size[1] == 1) || (i - i0 == 1)) {
    y = 0.0;
    for (i0 = 0; i0 < weight_model_coeff7->size[1]; i0++) {
      y += weight_model_coeff7->data[weight_model_coeff7->size[0] * i0] *
        Scaled_sensor_data->data[i0];
    }
  } else {
    y = 0.0;
    for (i0 = 0; i0 < weight_model_coeff7->size[1]; i0++) {
      y += weight_model_coeff7->data[weight_model_coeff7->size[0] * i0] *
        Scaled_sensor_data->data[i0];
    }
  }

  emxFree_real_T(&c_LR_model_weight_model_sensor_);
  predict_weight_LR = LR_model_weight_model_coeff->data[0] + y;
  i0 = weight_model_coeff7->size[0] * weight_model_coeff7->size[1];
  weight_model_coeff7->size[0] = 1;
  weight_model_coeff7->size[1] = LR_model_adj_sensors->size[1];
  emxEnsureCapacity((emxArray__common *)weight_model_coeff7, i0, (int)sizeof
                    (double));
  loop_ub = LR_model_adj_sensors->size[0] * LR_model_adj_sensors->size[1];
  emxFree_real_T(&LR_model_weight_model_coeff);
  for (i0 = 0; i0 < loop_ub; i0++) {
    weight_model_coeff7->data[i0] = c_sensor_data[(int)
      LR_model_adj_sensors->data[i0] - 1];
  }

  if (predict_weight_LR < 0.0) {
    zone = 0;
  } else {
    for (mm = 0; mm < 4; mm++) {
      if ((predict_weight_LR >= Parameters->LR_model_adj_stage_bound[mm]) &&
          (predict_weight_LR < Parameters->LR_model_adj_stage_bound[4 + mm])) {
        zone = mm;
      }
    }
  }

  i0 = LR_model_adj7->size[1];
  if (2 > i0) {
    i0 = 1;
    i = 1;
  } else {
    i0 = 2;
    i = LR_model_adj7->size[1] + 1;
  }

  emxInit_real_T1(&b_LR_model_adj7, 2);
  loop_ub = LR_model_adj7->size[1];
  k = b_LR_model_adj7->size[0] * b_LR_model_adj7->size[1];
  b_LR_model_adj7->size[0] = 1;
  b_LR_model_adj7->size[1] = loop_ub;
  emxEnsureCapacity((emxArray__common *)b_LR_model_adj7, k, (int)sizeof(double));
  for (k = 0; k < loop_ub; k++) {
    b_LR_model_adj7->data[b_LR_model_adj7->size[0] * k] = LR_model_adj7->
      data[zone + LR_model_adj7->size[0] * k];
  }

  k = Scaled_sensor_data->size[0];
  Scaled_sensor_data->size[0] = i - i0;
  emxEnsureCapacity((emxArray__common *)Scaled_sensor_data, k, (int)sizeof
                    (double));
  loop_ub = i - i0;
  for (k = 0; k < loop_ub; k++) {
    Scaled_sensor_data->data[k] = b_LR_model_adj7->data[(i0 + k) - 1];
  }

  emxFree_real_T(&b_LR_model_adj7);
  if ((LR_model_adj_sensors->size[1] == 1) || (i - i0 == 1)) {
    y = 0.0;
    for (i0 = 0; i0 < weight_model_coeff7->size[1]; i0++) {
      y += weight_model_coeff7->data[weight_model_coeff7->size[0] * i0] *
        Scaled_sensor_data->data[i0];
    }
  } else {
    y = 0.0;
    for (i0 = 0; i0 < weight_model_coeff7->size[1]; i0++) {
      y += weight_model_coeff7->data[weight_model_coeff7->size[0] * i0] *
        Scaled_sensor_data->data[i0];
    }
  }

  emxFree_real_T(&LR_model_adj_sensors);
  emxFree_real_T(&weight_model_coeff7);
  emxFree_real_T(&Scaled_sensor_data);
  final_predict = LR_model_adj7->data[zone] + y;
  emxFree_real_T(&LR_model_adj7);
  return final_predict;
}

//
// File trailer for weightPredictFunction.cpp
//
// [EOF]
//
