package testTaskPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import io.gatling.core.session._
import io.gatling.core.structure._
import mySessions._
import Steps._

class MySimulation extends Simulation{

  val lowerPause = 1
  val midlePause = 2
  val higherPause = 3

  val httpProtocol = http
    .baseUrl("https://challengers.flood.io")
    .inferHtmlResources()
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")

  val myScenario = scenario("MyTestSimulation")
  .during(2000){
    exec(
      Steps.startTest .pause(lowerPause),
      Steps.stepOne .pause(lowerPause,midlePause),
      Steps.stepTwo .pause(lowerPause,higherPause),
      mySessions.getMaxValueAndRadioForMaxValue .pause(lowerPause,midlePause),
      Steps.stepThree .pause(lowerPause),
      mySessions.getListOfNamesAndValue .pause(lowerPause,midlePause),
      Steps.stepFour .pause(lowerPause,midlePause),
      Steps.getOneTimeTokenForStepFive .pause(lowerPause,higherPause),
      Steps.stepFive .pause(lowerPause,midlePause))}


 //setUp(myScenario.inject(atOnceUsers(1))).protocols(httpProtocol)
  setUp(myScenario.inject(rampUsers(1) during (5 minutes)))
    .protocols(httpProtocol)
//    .maxDuration(6 minutes)
//    .throttle(holdFor(1 minutes))
}