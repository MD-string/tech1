//
// File: stableWeightPredict.h
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//
#ifndef STABLEWEIGHTPREDICT_H
#define STABLEWEIGHTPREDICT_H

// Include Files
#include <math.h>
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include "rt_nonfinite.h"
#include "rtwtypes.h"
#include "omp.h"
#include "stableWeightPredict_types.h"

// Function Declarations
extern double *stableWeightPredict(const double Sensor_data[42], double
  clear_flag, double last_data_in_out[42], double *status_flag, double
  *locked_weight, double *stable_counter, double *last_weight, struct0_T
  *Parameters,double * desti);

#endif

//
// File trailer for stableWeightPredict.h
//
// [EOF]
//
