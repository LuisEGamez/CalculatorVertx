package com.calculator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.ParameterProcessorException;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.jdbcclient.JDBCPool;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    RouterBuilder.create(vertx, "calculator.yaml")
      .onSuccess(routerBuilder -> {
        routerBuilder.operation("sum").handler(this::sum);

        Router router = routerBuilder.createRouter();
        router.errorHandler(400, routingContext -> {
          Throwable failure = routingContext.failure();
          if (failure instanceof ParameterProcessorException)
            routingContext.response().setStatusCode(400).end(failure.getMessage());
        });

        vertx.createHttpServer()
          .requestHandler(router)
          .listen(8080);
      });

  }

  private void sum(RoutingContext routingContext) {

    RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

    Double number1 = requestParameters.queryParameter("number1").getDouble();
    Double number2 = requestParameters.queryParameter ("number2").getDouble();

    double resultado = Calculator.sum(number1, number2);
    ResultDTO resultadoDTO = new ResultDTO(resultado);
    routingContext.json(resultadoDTO);
  }

}
