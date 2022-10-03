package com.calculadora.Calculadora;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
public class Calculator {

  private int result;

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }

  public Integer sum(Integer numero1, Integer numero2){
    result = numero1 + numero2;
    return result;
  }
}
