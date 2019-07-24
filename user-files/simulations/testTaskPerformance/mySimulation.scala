package rpTest

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class mySimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080/api/v1/test")
    .inferHtmlResources()
    .inferHtmlResources(BlackList(""".*\.js""", """.*css.*""",""".*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")
    .disableFollowRedirect


  /*PAUSES*/
  val lowerPause = 1
  val middlePause = 2
  val higherPause = 3

  /*SESSION AND SCENARIO VARIABLES*/
  val t_numberOUsers = System.getProperty("users", "1").toInt
  val t_duration = System.getProperty("duration", "10").toInt
  val t_rampUp = System.getProperty("rampUp", "60").toInt

  val jsonFileFeederL = jsonFile("levelFile.json").random

  val myScenario = scenario("MyTestSimulation")
    .during(t_duration seconds) {
      randomSwitch(
        85d ->
          exec(myRequests.currentDate)
            .exec(myRequests.postCreateLaunchRequest)
            .repeat(10) {
              exec(myRequests.postCreateSuiteRequest)
                .repeat(10) {
                  exec(myRequests.postCreateTestRequest)
                    .repeat(10) {
                      exec(myRequests.postCreateStepRequest)
                        .repeat(8) {
                          exec(myRequests.currentDate)
                            .exec(myRequests.postCreateLogRequest)
                        }
                        .feed(jsonFileFeederL)
                        .exec(myRequests.postCreateLogWithAttachRequest)
                        .exec(myRequests.currentDate)
                        .exec(myRequests.putFinishStepRequest)
                    }
                    .exec(myRequests.putFinishTestRequest)
                }
                .exec(myRequests.putFinishSuitRequest)
            }
            .exec(myRequests.putFinishLaunchRequest),

        15d -> exec(myRequests.getLaunchesRequest)
          .exec(myRequests.iteratorsInit)
          .doIf(session => session("numberOfLaunches").as[Int] >= 1) {
            exec(myRequests.selectLaunchIdToRead).pause(lowerPause, middlePause)
              .exec(myRequests.getSuitsRequest).pause(lowerPause, middlePause)

              .asLongAs(session => session("suitsIterator").as[Int] < session("suitsToRead").as[Int]) {
                exec(myRequests.selectNumOfSuitsToRead).pause(lowerPause, middlePause)
                  .exec(myRequests.getTestsRequest).pause(lowerPause, middlePause)

                  .asLongAs(session => session("testsIterator").as[Int] < session("testsToRead").as[Int]) {
                    exec(myRequests.selectNumOfTestToRead)
                      .exec(myRequests.getStepsRequest).pause(lowerPause, middlePause)

                      .asLongAs(session => session("stepsIterator").as[Int] < session("stepsToRead").as[Int]) {
                        exec(myRequests.selectNumOfStepsToRead)
                          .exec(myRequests.getLogsRequest).pause(middlePause, higherPause)
                      }
                  }
              }
          }
      )
    }
    .exec(myRequests.currentDate)
    .exec(myRequests.putForceFinishLaunchRequest)

  setUp(myScenario.inject(rampUsers(t_numberOUsers) during (t_rampUp seconds)))
    .protocols(httpProtocol)


}
