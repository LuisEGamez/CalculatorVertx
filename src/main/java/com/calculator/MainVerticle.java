package com.calculator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.ParameterProcessorException;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.*;

import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {

    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
      .setPort(3308)
      .setHost("127.0.0.1")
      .setDatabase("calculator")
      .setUser("root")
      .setPassword("root");

    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);


    SqlClient client = MySQLPool.client(vertx, connectOptions, poolOptions);


    RouterBuilder.create(vertx, "calculator.yaml")
      .onSuccess(routerBuilder -> {
        routerBuilder.operation("sum").handler(routingContext1 -> sum(routingContext1, client));
        routerBuilder.operation("getResults").handler(routerBuilder1 -> getAllResult(routerBuilder1, client));

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

  private void getAllResult(RoutingContext routerBuilder, SqlClient client) {
    client
      .preparedQuery("SELECT * FROM results;")
      .execute( ar -> {
        if (ar.succeeded()) {
          List<ResultDTO> resultDTOS = new ArrayList<>();
          RowSet<Row> rows = ar.result();
          rows.forEach(row -> resultDTOS.add(new ResultDTO(row.getInteger(0), row.getDouble(1), row.getLocalDateTime(2).toString())));
          routerBuilder.response().end(Json.encode(resultDTOS));
        } else {
          System.out.println("Failure: " + ar.cause().getMessage());
        }
      });


  }

  private void sum(RoutingContext routingContext, SqlClient client) {

    RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

    Double number1 = requestParameters.queryParameter("number1").getDouble();
    Double number2 = requestParameters.queryParameter ("number2").getDouble();

    double resultado = Calculator.sum(number1, number2);
    ResultDTO resultadoDTO = new ResultDTO(resultado);
    routingContext.response().end("Result: " + resultadoDTO.getResult());

    client
      .preparedQuery("CREATE TABLE IF NOT EXISTS results (`id` INT NOT NULL AUTO_INCREMENT, `result` DOUBLE NULL, `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`id`));")
      .execute(ar -> {
        if(ar.succeeded()){
          RowSet<Row> rows = ar.result();
          if(rows.rowCount()==0){
            System.out.println("Table already exist");
          }else {
            System.out.println("Table created");
          }
          client
            .preparedQuery("INSERT INTO results (result) VALUES (?)")
            .execute(Tuple.of(resultadoDTO.getResult()), ar1 -> {
              if (ar1.succeeded()) {
                System.out.println("Result inserted");
              } else {
                System.out.println("Failure: " + ar1.cause().getMessage());
              }
            });
        }else {
          System.out.println(ar.cause().getMessage());
        }
      });


  }


}
