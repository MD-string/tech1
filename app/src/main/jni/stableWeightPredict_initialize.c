//
// File: stableWeightPredict_initialize.cpp
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//

// Include Files
#include "rt_nonfinite.h"
#include "stableWeightPredict.h"
#include "stableWeightPredict_initialize.h"
#include "stableWeightPredict_data.h"

// Function Definitions

//
// Arguments    : void
// Return Type  : void
//
void stableWeightPredict_initialize()
{
  rt_InitInfAndNaN(8U);
  //omp_init_nest_lock(&emlrtNestLockGlobal);
}

//
// File trailer for stableWeightPredict_initialize.cpp
//
// [EOF]
//
