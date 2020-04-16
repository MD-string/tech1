//
// File: mean.cpp
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//

// Include Files
#include "rt_nonfinite.h"
#include "stableWeightPredict.h"
#include "mean.h"

// Function Definitions

//
// Arguments    : const double x[42]
//                double y[14]
// Return Type  : void
//
void mean(const double x[42], double y[14])
{
  int i;
  int xoffset;
  double s;
  int k;
  for (i = 0; i < 14; i++) {
    xoffset = i * 3;
    s = x[xoffset];
    for (k = 0; k < 2; k++) {
      s += x[(xoffset + k) + 1];
    }

    y[i] = s;
    y[i] /= 3.0;
  }
}

//
// File trailer for mean.cpp
//
// [EOF]
//
