package testTaskPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._


class MySimulation extends Simulation {

  /*PAUSES*/
  val lowerPause = 1
  val middlePause = 2
  val higherPause = 3
  /** **********************************************************************************/

  /*SESSION AND SCENARIO VARIABLES*/
  val t_numberOUsers = System.getProperty("users", "2").toInt
  val t_duration = System.getProperty("duration", "720").toInt
  val t_rampUp = System.getProperty("rampUp", "60").toInt

  /** **********************************************************************************/

  /*FEEDERS*/
  val jsonFileFeederD = jsonFile("descriptionFile.json").random
  val jsonFileFeederM = jsonFile("messagesFile.json").random
  val jsonFileFeederS = jsonFile("statusFile.json").random
  val jsonFileFeederL = jsonFile("levelFile.json").random
  /** **********************************************************************************/


  val httpProtocol = http
    .baseUrl("http://localhost:8080/api/v1")
    .inferHtmlResources()
    .inferHtmlResources(BlackList(""".*\.js""", """.*css.*""",""".*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")
    .disableFollowRedirect

  val myScenario = scenario("MyTestSimulation")
    .during(t_duration seconds) {
      randomSwitch(
        85d -> exec(SimulationRequests.currentDate)
          .feed(jsonFileFeederD)
          .exec(SimulationRequests.postCreateLaunchRequest)
          .exec(SimulationRequests.allToNull)

          .asLongAs(session => session("numberOfSuits").as[Int] < 10) {
          exec(SimulationRequests.currentDate)
            .feed(jsonFileFeederD)
            .exec(SimulationRequests.postCreateSuiteRequest)
            .exec(SimulationRequests.saveSuiteId)

            .asLongAs(session => session("numberOfTests").as[Int] < 10) {
            exec(SimulationRequests.currentDate)
              .exec(SimulationRequests.typeTest)
              .feed(jsonFileFeederD)
              .exec(SimulationRequests.postCreateTestOrStepRequest)
              .exec(SimulationRequests.saveTest)

              .asLongAs(session => session("numberOfSteps").as[Int] < 10) {
              exec(SimulationRequests.currentDate)
                .exec(SimulationRequests.typeStep)
                .feed(jsonFileFeederD)
                .exec(SimulationRequests.postCreateTestOrStepRequest)

                .asLongAs(session => session("numberOfLogs").as[Int] < 10) {
                exec(SimulationRequests.currentDate)
                randomSwitch(
                  80d -> feed(jsonFileFeederM).exec(SimulationRequests.postCreateLogRequest),
                  20d -> feed(jsonFileFeederM).feed(jsonFileFeederL).exec(SimulationRequests.postCreateLogWithAttachRequest))
                  .exec(SimulationRequests.getLogsRequest)
              }
                .feed(jsonFileFeederS)
                .exec(SimulationRequests.putFinishItemRequest)
                .exec(SimulationRequests.useTestId)
                .exec(SimulationRequests.logsToNull)
                .exec(SimulationRequests.getItemssStepRequest)
                .exec(SimulationRequests.useTestId)
            }
              .feed(jsonFileFeederS)
              .exec(SimulationRequests.stepsToNull)
              .exec(SimulationRequests.putFinishItemRequest)
              .exec(SimulationRequests.useSuitId)
              .exec(SimulationRequests.getItemssTestsRequest)
          }
            .feed(jsonFileFeederS)
            .exec(SimulationRequests.useSuitId)
            .exec(SimulationRequests.testToNull)
            .exec(SimulationRequests.getSuitsRequest)
            .exec(SimulationRequests.putFinishItemRequest)
        }
          .feed(jsonFileFeederS)
          .exec(SimulationRequests.putFinishLaunchRequest),

      15d -> exec(SimulationRequests.getLaunchesRequest)
        .exec(SimulationRequests.selectLaunchIdToRead).pause(lowerPause,middlePause)
        .exec(SimulationRequests.iteratorsInit)
        .exec(SimulationRequests.getSuitsRequest).pause(lowerPause,middlePause)

        .asLongAs(session => session("suitsIterator").as[Int] < session("suitsToRead").as[Int]) {
        exec(SimulationRequests.selectNumOfSuitsToRead).pause(lowerPause,middlePause)
          .exec(SimulationRequests.useSuit)
          .exec(SimulationRequests.getItemssTestsRequest).pause(lowerPause,middlePause)

          .asLongAs(session => session("testsIterator").as[Int] < session("testsToRead").as[Int]) {
          exec(SimulationRequests.selectNumOfTestToRead)
            .exec(SimulationRequests.useTest)
            .exec(SimulationRequests.getItemssStepRequest).pause(lowerPause,middlePause)

            .asLongAs(session => session("stepsIterator").as[Int] < session("stepsToRead").as[Int]) {
            exec(SimulationRequests.selectNumOfStepsToRead)
              .exec(SimulationRequests.useStep)
              .exec(SimulationRequests.getLogsRequest).pause(middlePause,higherPause)
          }
            .exec(SimulationRequests.useTest)
        }
          .exec(SimulationRequests.useSuit)
      }
      )

    }

  setUp(myScenario.inject(rampUsers(t_numberOUsers) during (t_rampUp seconds)))
    .protocols(httpProtocol)
}