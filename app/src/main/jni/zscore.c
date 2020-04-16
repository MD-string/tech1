//
// File: zscore.cpp
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//

// Include Files
#include "rt_nonfinite.h"
#include "stableWeightPredict.h"
#include "zscore.h"
#include "stableWeightPredict_emxutil.h"

// Function Definitions

//
// Arguments    : const emxArray_real_T *x
//                emxArray_real_T *z
// Return Type  : void
//
void zscore(const emxArray_real_T *x, emxArray_real_T *z)
{
  double sigma;
  int k;
  double mu;
  int i1;
  int d;
  int ix;
  double xbar;
  int na1;
  double r;
  emxArray_real_T *av;
  emxArray_real_T *cv;
  emxArray_real_T *a;
  int ck;
  if (x->size[0] == 0) {
    sigma = 0.0;
  } else {
    sigma = x->data[0];
    for (k = 2; k <= x->size[0]; k++) {
      sigma += x->data[k - 1];
    }
  }

  mu = sigma / (double)x->size[0];
  i1 = x->size[0];
  if (x->size[0] > 1) {
    d = x->size[0] - 1;
  } else {
    d = x->size[0];
  }

  if (x->size[0] == 0) {
    sigma = 0.0;
  } else {
    ix = 0;
    xbar = x->data[0];
    for (k = 2; k <= i1; k++) {
      ix++;
      xbar += x->data[ix];
    }

    xbar /= (double)x->size[0];
    ix = 0;
    r = x->data[0] - xbar;
    sigma = r * r;
    for (k = 2; k <= i1; k++) {
      ix++;
      r = x->data[ix] - xbar;
      sigma += r * r;
    }

    sigma /= (double)d;
  }

  sigma = sqrt(sigma);
  if (sigma == 0.0) {
    sigma = 1.0;
  }

  na1 = x->size[0];
  d = x->size[0];
  i1 = z->size[0];
  z->size[0] = d;
  emxEnsureCapacity((emxArray__common *)z, i1, (int)sizeof(double));
  emxInit_real_T(&av, 1);
  emxInit_real_T(&cv, 1);
  if (z->size[0] == 0) {
  } else {
    d = x->size[0];
    i1 = av->size[0];
    av->size[0] = d;
    emxEnsureCapacity((emxArray__common *)av, i1, (int)sizeof(double));
    ix = z->size[0];
    for (ck = 0; ck <= 0; ck += ix) {
      for (k = 0; k + 1 <= na1; k++) {
        av->data[k] = x->data[k];
      }

      i1 = cv->size[0];
      cv->size[0] = av->size[0];
      emxEnsureCapacity((emxArray__common *)cv, i1, (int)sizeof(double));
      d = av->size[0];
      for (i1 = 0; i1 < d; i1++) {
        cv->data[i1] = av->data[i1] - mu;
      }

      for (k = 0; k + 1 <= ix; k++) {
        z->data[ck + k] = cv->data[k];
      }
    }
  }

  emxInit_real_T(&a, 1);
  i1 = a->size[0];
  a->size[0] = z->size[0];
  emxEnsureCapacity((emxArray__common *)a, i1, (int)sizeof(double));
  d = z->size[0];
  for (i1 = 0; i1 < d; i1++) {
    a->data[i1] = z->data[i1];
  }

  na1 = z->size[0];
  d = z->size[0];
  i1 = z->size[0];
  z->size[0] = d;
  emxEnsureCapacity((emxArray__common *)z, i1, (int)sizeof(double));
  if (z->size[0] == 0) {
  } else {
    i1 = av->size[0];
    av->size[0] = na1;
    emxEnsureCapacity((emxArray__common *)av, i1, (int)sizeof(double));
    ix = z->size[0];
    for (ck = 0; ck <= 0; ck += ix) {
      for (k = 0; k + 1 <= na1; k++) {
        av->data[k] = a->data[k];
      }

      i1 = cv->size[0];
      cv->size[0] = av->size[0];
      emxEnsureCapacity((emxArray__common *)cv, i1, (int)sizeof(double));
      d = av->size[0];
      for (i1 = 0; i1 < d; i1++) {
        cv->data[i1] = av->data[i1] / sigma;
      }

      for (k = 0; k + 1 <= ix; k++) {
        z->data[ck + k] = cv->data[k];
      }
    }
  }

  emxFree_real_T(&a);
  emxFree_real_T(&cv);
  emxFree_real_T(&av);
}

//
// File trailer for zscore.cpp
//
// [EOF]
//
