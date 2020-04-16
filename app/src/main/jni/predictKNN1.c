//
// File: predictKNN1.cpp
//
// MATLAB Coder version            : 3.1
// C/C++ source code generated on  : 13-Jun-2018 16:08:38
//

// Include Files
#include "rt_nonfinite.h"
#include "stableWeightPredict.h"
#include "predictKNN1.h"
#include "stableWeightPredict_emxutil.h"
#include "sort1.h"
#include "repmat.h"

// Function Definitions

//
// Arguments    : const emxArray_real_T *mdl_Y
//                const emxArray_real_T *mdl_X
//                const emxArray_real_T *mdl_Mu
//                const emxArray_real_T *mdl_Sigma
//                emxArray_real_T *Xnew
// Return Type  : double
//
double predictKNN1(const emxArray_real_T *mdl_Y, const emxArray_real_T *mdl_X,
                   const emxArray_real_T *mdl_Mu, const emxArray_real_T
                   *mdl_Sigma, emxArray_real_T *Xnew)
{
  double label;
  emxArray_real_T *y;
  int vstride;
  int n;
  emxArray_real_T *b_y;
  emxArray_real_T *X;
  emxArray_real_T *c_y;
  emxArray_real_T *b_Xnew;
  emxArray_real_T *tempXnew;
  unsigned int sz[2];
  int k;
  int b_k;
  emxArray_real_T *d_y;
  double s;
  int c_k;
  emxArray_real_T *dist;
  emxArray_int32_T *iidx;
  emxInit_real_T(&y, 1);
  vstride = y->size[0];
  y->size[0] = mdl_Y->size[0];
  emxEnsureCapacity((emxArray__common *)y, vstride, (int)sizeof(double));
  n = mdl_Y->size[0];
  for (vstride = 0; vstride < n; vstride++) {
    y->data[vstride] = mdl_Y->data[vstride];
  }

  emxInit_real_T1(&b_y, 2);
  repmat(mdl_Mu, b_y);
  vstride = Xnew->size[0] * Xnew->size[1];
  Xnew->size[0] = 1;
  emxEnsureCapacity((emxArray__common *)Xnew, vstride, (int)sizeof(double));
  n = Xnew->size[0];
  vstride = Xnew->size[1];
  n *= vstride;
  for (vstride = 0; vstride < n; vstride++) {
    Xnew->data[vstride] -= b_y->data[vstride];
  }

  repmat(mdl_Sigma, b_y);
  vstride = Xnew->size[0] * Xnew->size[1];
  Xnew->size[0] = 1;
  emxEnsureCapacity((emxArray__common *)Xnew, vstride, (int)sizeof(double));
  n = Xnew->size[0];
  vstride = Xnew->size[1];
  n *= vstride;
  for (vstride = 0; vstride < n; vstride++) {
    Xnew->data[vstride] /= b_y->data[vstride];
  }

  emxFree_real_T(&b_y);
  emxInit_real_T1(&X, 2);
  b_repmat(mdl_Mu, (double)mdl_X->size[0], X);
  vstride = X->size[0] * X->size[1];
  X->size[0] = mdl_X->size[0];
  X->size[1] = mdl_X->size[1];
  emxEnsureCapacity((emxArray__common *)X, vstride, (int)sizeof(double));
  n = mdl_X->size[0] * mdl_X->size[1];
  for (vstride = 0; vstride < n; vstride++) {
    X->data[vstride] = mdl_X->data[vstride] - X->data[vstride];
  }

  emxInit_real_T1(&c_y, 2);
  b_repmat(mdl_Sigma, (double)mdl_X->size[0], c_y);
  vstride = X->size[0] * X->size[1];
  emxEnsureCapacity((emxArray__common *)X, vstride, (int)sizeof(double));
  n = X->size[0];
  vstride = X->size[1];
  n *= vstride;
  for (vstride = 0; vstride < n; vstride++) {
    X->data[vstride] /= c_y->data[vstride];
  }

  emxInit_real_T1(&b_Xnew, 2);
  n = Xnew->size[1];
  vstride = b_Xnew->size[0] * b_Xnew->size[1];
  b_Xnew->size[0] = 1;
  b_Xnew->size[1] = n;
  emxEnsureCapacity((emxArray__common *)b_Xnew, vstride, (int)sizeof(double));
  for (vstride = 0; vstride < n; vstride++) {
    b_Xnew->data[b_Xnew->size[0] * vstride] = Xnew->data[Xnew->size[0] * vstride];
  }

  emxInit_real_T1(&tempXnew, 2);
  b_repmat(b_Xnew, (double)X->size[0], tempXnew);
  vstride = tempXnew->size[0] * tempXnew->size[1];
  emxEnsureCapacity((emxArray__common *)tempXnew, vstride, (int)sizeof(double));
  n = tempXnew->size[0];
  vstride = tempXnew->size[1];
  n *= vstride;
  emxFree_real_T(&b_Xnew);
  for (vstride = 0; vstride < n; vstride++) {
    tempXnew->data[vstride] -= X->data[vstride];
  }

  vstride = X->size[0] * X->size[1];
  X->size[0] = tempXnew->size[0];
  X->size[1] = tempXnew->size[1];
  emxEnsureCapacity((emxArray__common *)X, vstride, (int)sizeof(double));
  n = tempXnew->size[0] * tempXnew->size[1];
  for (vstride = 0; vstride < n; vstride++) {
    X->data[vstride] = tempXnew->data[vstride];
  }

  for (vstride = 0; vstride < 2; vstride++) {
    sz[vstride] = (unsigned int)tempXnew->size[vstride];
  }

  vstride = c_y->size[0] * c_y->size[1];
  c_y->size[0] = (int)sz[0];
  c_y->size[1] = (int)sz[1];
  emxEnsureCapacity((emxArray__common *)c_y, vstride, (int)sizeof(double));
  n = tempXnew->size[0] * tempXnew->size[1];
  emxFree_real_T(&tempXnew);

#pragma omp parallel for \
 num_threads(omp_get_max_threads()) \
 private(b_k)

  for (k = 1; k <= n; k++) {
    b_k = k;
    c_y->data[b_k - 1] = X->data[b_k - 1] * X->data[b_k - 1];
  }

  emxFree_real_T(&X);
  for (vstride = 0; vstride < 2; vstride++) {
    sz[vstride] = (unsigned int)c_y->size[vstride];
  }

  emxInit_real_T(&d_y, 1);
  vstride = d_y->size[0];
  d_y->size[0] = (int)sz[0];
  emxEnsureCapacity((emxArray__common *)d_y, vstride, (int)sizeof(double));
  if ((c_y->size[0] == 0) || (c_y->size[1] == 0)) {
    n = d_y->size[0];
    vstride = d_y->size[0];
    d_y->size[0] = n;
    emxEnsureCapacity((emxArray__common *)d_y, vstride, (int)sizeof(double));
    for (vstride = 0; vstride < n; vstride++) {
      d_y->data[vstride] = 0.0;
    }
  } else {
    vstride = c_y->size[0];
    for (n = 0; n + 1 <= vstride; n++) {
      s = c_y->data[n];
      for (c_k = 2; c_k <= c_y->size[1]; c_k++) {
        s += c_y->data[n + (c_k - 1) * vstride];
      }

      d_y->data[n] = s;
    }
  }

  emxFree_real_T(&c_y);
  emxInit_real_T(&dist, 1);
  vstride = dist->size[0];
  dist->size[0] = d_y->size[0];
  emxEnsureCapacity((emxArray__common *)dist, vstride, (int)sizeof(double));
  n = d_y->size[0];
  for (vstride = 0; vstride < n; vstride++) {
    dist->data[vstride] = d_y->data[vstride];
  }

  for (c_k = 0; c_k + 1 <= d_y->size[0]; c_k++) {
    dist->data[c_k] = sqrt(dist->data[c_k]);
  }

  emxInit_int32_T1(&iidx, 1);
  sort(dist, iidx);
  vstride = d_y->size[0];
  d_y->size[0] = iidx->size[0];
  emxEnsureCapacity((emxArray__common *)d_y, vstride, (int)sizeof(double));
  n = iidx->size[0];
  emxFree_real_T(&dist);
  for (vstride = 0; vstride < n; vstride++) {
    d_y->data[vstride] = iidx->data[vstride];
  }

  emxFree_int32_T(&iidx);
  label = y->data[(int)d_y->data[0] - 1];
  emxFree_real_T(&d_y);
  emxFree_real_T(&y);
  return label;
}

//
// File trailer for predictKNN1.cpp
//
// [EOF]
//
