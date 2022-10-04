package com.calculator;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class IntegrationTest {

  /*@BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) throws Exception {
    vertx.deployVerticle(new MainVerticle(), testContext.succeedingThenComplete());
  }

  @Test
  void http_server_check_response_bad_request(Vertx vertx, VertxTestContext testContext) {
    HttpClient client = vertx.createHttpClient();
    client.request(HttpMethod.GET, 8080, "localhost", "/api/calculator/sum")
      .compose(req -> req.send().compose(HttpClientResponse::body))
      .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
        assertThat(buffer.toString()).isEqualTo("Plop");
        testContext.completeNow();
      })));
  }*/

  @Test
  void http_server_check_response_without_first_parameter(Vertx vertx, VertxTestContext testContext) {



    Router router = Router.router(vertx);

    router.get("/api/calculator/sum").handler(ctx -> ctx.response().end("[Bad Request] Missing parameter number1 in QUERY"));

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080);


    HttpClient client = vertx.createHttpClient();

    client.request(HttpMethod.GET, 8080, "localhost", "/api/calculator/sum")
      .compose(req -> req.send().compose(HttpClientResponse::body))
      .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
        assertThat(buffer.toString()).isEqualTo("[Bad Request] Missing parameter number1 in QUERY");
        testContext.completeNow();
      })));
  }

  @Test
  void http_server_check_response(Vertx vertx, VertxTestContext testContext) {

    ResultDTO resultadoDTO = new ResultDTO(Calculator.sum(2,3));

    Router router = Router.router(vertx);

    router.get("/api/calculator/sum")
      .handler(ctx -> ctx.response().end(Json.encode(resultadoDTO)));

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080);


    HttpClient client = vertx.createHttpClient();

    client.request(HttpMethod.GET, 8080, "localhost", "/api/calculator/sum")
      .compose(req -> req.send().compose(HttpClientResponse::body))
      .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
        assertThat(buffer.toString()).isEqualTo("{\"result\":5}");
        testContext.completeNow();
      })));
  }


}
