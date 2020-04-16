//
// File: predictKNN1.h
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//
#ifndef PREDICTKNN1_H
#define PREDICTKNN1_H

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
extern double predictKNN1(const emxArray_real_T *mdl_Y, const emxArray_real_T
  *mdl_X, const emxArray_real_T *mdl_Mu, const emxArray_real_T *mdl_Sigma,
  emxArray_real_T *Xnew);

#endif

//
// File trailer for predictKNN1.h
//
// [EOF]
//
