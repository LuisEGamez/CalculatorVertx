package com.calculator;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CalculatorTest {

  @Test
  void suma(){

    int expectedResult = Calculator.sum(3, 3);
    assertThat(expectedResult).isEqualTo(6);
  }

  @Test
  void sumaFail(){

    int expectedResult = Calculator.sum(3, 3);
    assertThat(expectedResult).isNotEqualTo(4);
  }
}
