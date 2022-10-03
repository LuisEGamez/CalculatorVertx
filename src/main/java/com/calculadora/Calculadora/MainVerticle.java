package com.calculadora.Calculadora;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  private Calculator calculator = new Calculator();

  @Override
  public void start(Promise<Void> startPromise) throws Exception {



  }



}
