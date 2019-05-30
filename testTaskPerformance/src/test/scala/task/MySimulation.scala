package testTaskPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._


class MySimulation extends Simulation {

  /*PAUSES*/
  val lowerPause = 1
  val midlePause = 2
  val higherPause = 3
  /** **********************************************************************************/

  /*SECCION AND SCENARIO VARIABLES*/
  val numberOUsers = 1
  val durationOfAllTest = 1
  val durationForScenario = 200
  /** **********************************************************************************/

  /*FEEDERS*/
  val jsonFileFeeder = jsonFile("ageFile.json").random
  /** **********************************************************************************/


  val httpProtocol = http
    .baseUrl("https://challengers.flood.io")
    .inferHtmlResources()
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")

  val myScenario = scenario("MyTestSimulation")
    .during(durationForScenario) {
      exec(Steps.startTest).pause(lowerPause)
        .exec(Steps.stepOne).pause(lowerPause, midlePause)
        .feed(jsonFileFeeder)
        //.exec(mySessions.debugFeeder)
        .exec(Steps.stepTwo).pause(lowerPause, higherPause)
        .exec(mySessions.getMaxValueAndRadioForMaxValue).pause(lowerPause, midlePause)
        .exec(Steps.stepThree).pause(lowerPause)
        .exec(mySessions.getListOfNamesAndValue).pause(lowerPause, midlePause)
        .exec(Steps.stepFour).pause(lowerPause, midlePause)
        .exec(Steps.getOneTimeTokenForStepFive).pause(lowerPause, higherPause)
        .exec(Steps.stepFive).pause(lowerPause, midlePause)
    }

  setUp(myScenario.inject(rampUsers(numberOUsers) during (durationOfAllTest minutes)))
    .protocols(httpProtocol)
}