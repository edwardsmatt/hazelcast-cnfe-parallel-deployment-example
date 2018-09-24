import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
  * Performance test for concurrently accessing the distributed session map
  */
class ConcurrentSessionAccessGatlingTest extends Simulation {

  val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
  // Log all HTTP requests
  // context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
  // Log failed HTTP requests
  // context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))
  // context.getLogger("io.gatling.http.action.sync.HttpTx").setLevel(Level.valueOf("DEBUG"))

  val baseURL = """http://localhost/basic-web-app"""

  val httpConf = http
    .baseURL(baseURL)
    .inferHtmlResources()
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
    .connectionHeader("keep-alive")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")

  val scn = scenario("Create session and then execute Concurrent Requests should not create new session")
    .exec(http("[Sequential] Create Session")
      .get("/")
      .check(substring("Is new session : true").exists)
      .check(status.is(200))).exitHereIfFailed
    .exec(http("[Sequential] Verify existing Session in use")
      .get("/")
      .check(substring("Is new session : true").notExists)
      .check(substring("Is new session : false").exists)).exitHereIfFailed
    .repeat(50) {
    exec(http("[Sequential] Verify existing Session in use")
      .get("/")
      .check(substring("Is new session : true").notExists)
        .check(substring("Is new session : false").exists)
        .check(status.is(200))
        .resources( //Execute two requests concurrently to simulate concurrent AJAX calls.
          http("[Concurrent] Verify existing Session in use")
            .get("/")
            .check(status.is(200))
            .check(substring("Is new session : true").notExists)
            .check(substring("Is new session : false").exists),
          http("[Concurrent] Verify existing Session in use")
            .get("/")
            .check(status.is(200))
            .check(substring("Is new session : true").notExists)
            .check(substring("Is new session : false").exists)
        )
      ).exitHereIfFailed
  }

  val users = scenario("Users").exec(scn)

  setUp(
    users.inject(atOnceUsers(200))
  ).protocols(httpConf)
}
