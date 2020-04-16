//
// File: repmat.cpp
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//

// Include Files
#include "rt_nonfinite.h"
#include "stableWeightPredict.h"
#include "repmat.h"
#include "stableWeightPredict_emxutil.h"

// Function Definitions

//
// Arguments    : const emxArray_real_T *a
//                double varargin_1
//                emxArray_real_T *b
// Return Type  : void
//
void b_repmat(const emxArray_real_T *a, double varargin_1, emxArray_real_T *b)
{
  int outsize_idx_1;
  int ibmat;
  boolean_T p;
  int itilerow;
  outsize_idx_1 = a->size[1];
  ibmat = b->size[0] * b->size[1];
  b->size[0] = (int)varargin_1;
  b->size[1] = outsize_idx_1;
  emxEnsureCapacity((emxArray__common *)b, ibmat, (int)sizeof(double));
  if (!(a->size[1] == 0)) {
    if ((int)varargin_1 == 0) {
      p = true;
    } else if (outsize_idx_1 == 0) {
      p = true;
    } else {
      p = false;
    }

    if (!p) {
      for (outsize_idx_1 = 0; outsize_idx_1 + 1 <= a->size[1]; outsize_idx_1++)
      {
        ibmat = outsize_idx_1 * (int)varargin_1;
        for (itilerow = 1; itilerow <= (int)varargin_1; itilerow++) {
          b->data[(ibmat + itilerow) - 1] = a->data[outsize_idx_1];
        }
      }
    }
  }
}

//
// Arguments    : const emxArray_real_T *a
//                emxArray_real_T *b
// Return Type  : void
//
void repmat(const emxArray_real_T *a, emxArray_real_T *b)
{
  int outsize_idx_1;
  int i2;
  outsize_idx_1 = a->size[1];
  i2 = b->size[0] * b->size[1];
  b->size[0] = 1;
  b->size[1] = outsize_idx_1;
  emxEnsureCapacity((emxArray__common *)b, i2, (int)sizeof(double));
  if ((!(a->size[1] == 0)) && (!(outsize_idx_1 == 0))) {
    for (outsize_idx_1 = 0; outsize_idx_1 + 1 <= a->size[1]; outsize_idx_1++) {
      b->data[outsize_idx_1] = a->data[outsize_idx_1];
    }
  }
}

//
// File trailer for repmat.cpp
//
// [EOF]
//
