package com.calculator;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
public class ResultDTO {
  private Integer result;

  public ResultDTO(Integer result) {
    this.result = result;
  }

  public Integer getResult() {
    return result;
  }
}
