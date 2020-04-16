//
// File: stableWeightPredict_types.h
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//
#ifndef STABLEWEIGHTPREDICT_TYPES_H
#define STABLEWEIGHTPREDICT_TYPES_H

// Include Files
#include "rtwtypes.h"

// Type Definitions
#ifndef struct_emxArray__common
#define struct_emxArray__common

typedef struct
{
  void *data;
  int *size;
  int allocatedSize;
  int numDimensions;
  boolean_T canFreeData;
}emxArray__common;

#endif                                 //struct_emxArray__common

#ifndef struct_emxArray_int32_T
#define struct_emxArray_int32_T

typedef struct
{
  int *data;
  int *size;
  int allocatedSize;
  int numDimensions;
  boolean_T canFreeData;
}emxArray_int32_T;

#endif                                 //struct_emxArray_int32_T

#ifndef struct_emxArray_real_T
#define struct_emxArray_real_T

typedef struct
{
  double *data;
  int *size;
  int allocatedSize;
  int numDimensions;
  boolean_T canFreeData;
}emxArray_real_T;

#endif                                 //struct_emxArray_real_T

typedef struct {
  double X_input[1400000];
  double Y_input[100000];
  double Mu[14];
  double Sigma[14];
} struct1_T;

typedef struct {
  double sensor_sel_for_loc[14];
  struct1_T KNN_Model;
  double LR_model_adj_stage_bound_input[8];
  double LR_model_adj_stage_bound[8];
  double LR_model_adj_sensors1[14];
  double LR_model_adj_sensors2[14];
  double LR_model_adj_sensors3[14];
  double LR_model_adj_sensors4[14];
  double LR_model_adj_sensors5[14];
  double LR_model_adj_sensors6[14];
  double LR_model_adj_sensors7[14];
  double LR_model_adj_sensors8[14];
  double LR_model_adj_sensors9[14];
  double LR_model_adj1_input[60];
  double LR_model_adj2_input[60];
  double LR_model_adj3_input[60];
  double LR_model_adj4_input[60];
  double LR_model_adj5_input[60];
  double LR_model_adj6_input[60];
  double LR_model_adj7_input[60];
  double LR_model_adj8_input[60];
  double LR_model_adj9_input[60];
  double weight_model_coeff1[15];
  double weight_model_coeff2[15];
  double weight_model_coeff3[15];
  double weight_model_coeff4[15];
  double weight_model_coeff5[15];
  double weight_model_coeff6[15];
  double weight_model_coeff7[15];
  double weight_model_coeff8[15];
  double weight_model_coeff9[15];
} struct0_T;

#endif

//
// File trailer for stableWeightPredict_types.h
//
// [EOF]
//
