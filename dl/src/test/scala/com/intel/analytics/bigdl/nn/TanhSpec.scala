/*
 * Licensed to Intel Corporation under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * Intel Corporation licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intel.analytics.bigdl.nn

import org.scalatest.{FlatSpec, Matchers}
import com.intel.analytics.bigdl.tensor.Tensor

import scala.math.abs

class TanhSpec extends FlatSpec with Matchers {
  "A Tanh Module " should "generate correct output and grad" in {
    val module = new Tanh[Double]()
    val input = Tensor[Double](2, 2, 2)
    input(Array(1, 1, 1)) = -0.17020166106522
    input(Array(1, 1, 2)) = 0.57785657607019
    input(Array(1, 2, 1)) = -1.3404131438583
    input(Array(1, 2, 2)) = 1.0938102817163
    input(Array(2, 1, 1)) = 1.120370157063
    input(Array(2, 1, 2)) = -1.5014141565189
    input(Array(2, 2, 1)) = 0.3380249235779
    input(Array(2, 2, 2)) = -0.625677742064
    val gradOutput = Tensor[Double](2, 2, 2)
    gradOutput(Array(1, 1, 1)) = 0.79903302760795
    gradOutput(Array(1, 1, 2)) = 0.019753993256018
    gradOutput(Array(1, 2, 1)) = 0.63136631483212
    gradOutput(Array(1, 2, 2)) = 0.29849314852618
    gradOutput(Array(2, 1, 1)) = 0.94380705454387
    gradOutput(Array(2, 1, 2)) = 0.030344664584845
    gradOutput(Array(2, 2, 1)) = 0.33804601291195
    gradOutput(Array(2, 2, 2)) = 0.8807330634445
    val expectedOutput = Tensor[Double](2, 2, 2)
    expectedOutput(Array(1, 1, 1)) = -0.16857698275003
    expectedOutput(Array(1, 1, 2)) = 0.52110579963112
    expectedOutput(Array(1, 2, 1)) = -0.87177144344863
    expectedOutput(Array(1, 2, 2)) = 0.79826462420686
    expectedOutput(Array(2, 1, 1)) = 0.80769763073281
    expectedOutput(Array(2, 1, 2)) = -0.90540347425835
    expectedOutput(Array(2, 2, 1)) = 0.32571298952384
    expectedOutput(Array(2, 2, 2)) = -0.55506882753488
    val expectedGrad = Tensor[Double](2, 2, 2)
    expectedGrad(Array(1, 1, 1)) = 0.77632594793144
    expectedGrad(Array(1, 1, 2)) = 0.014389771607755
    expectedGrad(Array(1, 2, 1)) = 0.15153710218424
    expectedGrad(Array(1, 2, 2)) = 0.1082854310036
    expectedGrad(Array(2, 1, 1)) = 0.32809049064441
    expectedGrad(Array(2, 1, 2)) = 0.0054694603766104
    expectedGrad(Array(2, 2, 1)) = 0.3021830658283
    expectedGrad(Array(2, 2, 2)) = 0.6093779706637
    val inputOrg = input.clone()
    val gradOutputOrg = gradOutput.clone()
    val output = module.forward(input)
    val gradInput = module.backward(input, gradOutput)
    expectedOutput.map(output, (v1, v2) => {
      assert(abs(v1 - v2) < 1e-6);
      v1
    })
    expectedGrad.map(gradInput, (v1, v2) => {
      assert(abs(v1 - v2) < 1e-6);
      v1
    })
    assert(input == inputOrg)
    assert(gradOutput == gradOutputOrg)
  }

  "A Tanh Module " should "be good in gradient check" in {
    val module = new Tanh[Double]()
    val input = Tensor[Double](2, 2, 2).rand()

    val checker = new GradientChecker(1e-2, 1e-2)
    checker.checkLayer(module, input) should be(true)
  }
}
