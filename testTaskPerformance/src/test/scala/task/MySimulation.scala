package testTaskPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class MySimulation extends Simulation {

  val lowerPause = 1
  val midlePause = 2
  val higherPause = 3
  val numberOUsers = 1
  val durationOfAllTest = 5
  val durationForScenario = 2000


  val httpProtocol = http
    .baseUrl("https://challengers.flood.io")
    .inferHtmlResources()
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")

  val myScenario = scenario("MyTestSimulation")
    .during(durationForScenario) {
      //      exec(
      //        Steps.startTest.pause(lowerPause),
      //        Steps.stepOne.pause(lowerPause, midlePause),
      //        Steps.stepTwo.pause(lowerPause, higherPause),
      //        mySessions.getMaxValueAndRadioForMaxValue.pause(lowerPause, midlePause),
      //        Steps.stepThree.pause(lowerPause),
      //        mySessions.getListOfNamesAndValue.pause(lowerPause, midlePause),
      //        Steps.stepFour.pause(lowerPause, midlePause),
      //        Steps.getOneTimeTokenForStepFive.pause(lowerPause, higherPause),
      //        Steps.stepFive.pause(lowerPause, midlePause))
      exec(Steps.startTest).pause(lowerPause)
        .exec(Steps.stepOne).pause(lowerPause, midlePause)
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