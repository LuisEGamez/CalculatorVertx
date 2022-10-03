package com.calculadora.Calculadora;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

public class MainVerticle extends AbstractVerticle {

  private final Calculator calculator = new Calculator();

  @Override
  public void start() throws Exception {

    RouterBuilder.create(vertx, "calculator.yaml")
      .onSuccess(routerBuilder -> {
        routerBuilder.operation("sum").handler(this::sum);

        Router router = routerBuilder.createRouter();
        vertx.createHttpServer()
          .requestHandler(router)
          .listen(8080);
      });

  }

  private void sum(RoutingContext routingContext) {

    RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

    Integer number1 = requestParameters.queryParameter("number1").getInteger();
    Integer number2 = requestParameters.queryParameter("number2").getInteger();
    if(number1 == null || number2 == null){
      routingContext.response().setStatusCode(400).end();
    }else {
      calculator.sum(number1, number2);
      routingContext.json(calculator);
    }

  }



}
