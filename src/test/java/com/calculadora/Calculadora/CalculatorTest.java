package com.calculadora.Calculadora;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CalculatorTest {

  private Calculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new Calculator();
  }

  @Test
  void suma(){

    int expectedResult = calculator.sum(3, 3);
    assertThat(expectedResult).isEqualTo(6);
  }

  @Test
  void sumaFail(){

    int expectedResult = calculator.sum(3, 3);
    assertThat(expectedResult).isNotEqualTo(4);
  }
}
