//
// File: sort1.cpp
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//

// Include Files
#include "rt_nonfinite.h"
#include "stableWeightPredict.h"
#include "sort1.h"
#include "sortIdx.h"
#include "stableWeightPredict_emxutil.h"

// Function Declarations
static void b_sort(emxArray_real_T *x, int dim, emxArray_int32_T *idx);

// Function Definitions

//
// Arguments    : emxArray_real_T *x
//                int dim
//                emxArray_int32_T *idx
// Return Type  : void
//
static void b_sort(emxArray_real_T *x, int dim, emxArray_int32_T *idx)
{
  int i5;
  emxArray_real_T *vwork;
  int k;
  unsigned int unnamed_idx_0;
  int vstride;
  emxArray_int32_T *iidx;
  int j;
  if (dim <= 1) {
    i5 = x->size[0];
  } else {
    i5 = 1;
  }

  emxInit_real_T(&vwork, 1);
  k = vwork->size[0];
  vwork->size[0] = i5;
  emxEnsureCapacity((emxArray__common *)vwork, k, (int)sizeof(double));
  unnamed_idx_0 = (unsigned int)x->size[0];
  k = idx->size[0];
  idx->size[0] = (int)unnamed_idx_0;
  emxEnsureCapacity((emxArray__common *)idx, k, (int)sizeof(int));
  if (dim > 2) {
    vstride = x->size[0];
  } else {
    vstride = 1;
    k = 1;
    while (k <= dim - 1) {
      k = x->size[0];
      vstride *= k;
      k = 2;
    }
  }

  emxInit_int32_T1(&iidx, 1);
  for (j = 0; j + 1 <= vstride; j++) {
    for (k = 0; k + 1 <= i5; k++) {
      vwork->data[k] = x->data[j + k * vstride];
    }

    sortIdx(vwork, iidx);
    for (k = 0; k + 1 <= i5; k++) {
      x->data[j + k * vstride] = vwork->data[k];
      idx->data[j + k * vstride] = iidx->data[k];
    }
  }

  emxFree_int32_T(&iidx);
  emxFree_real_T(&vwork);
}

//
// Arguments    : emxArray_real_T *x
//                emxArray_int32_T *idx
// Return Type  : void
//
void sort(emxArray_real_T *x, emxArray_int32_T *idx)
{
  int dim;
  dim = 2;
  if (x->size[0] != 1) {
    dim = 1;
  }

  b_sort(x, dim, idx);
}

//
// File trailer for sort1.cpp
//
// [EOF]
//
