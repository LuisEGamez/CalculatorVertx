package com.calculator;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
public class ResultDTO {
  private Double result;

  public ResultDTO(Double result) {
    this.result = result;
  }

  public Double getResult() {
    return result;
  }
}
