package testTaskPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.Predef.RampBuilder
import mySessions._
import Steps._

class MySimulation extends Simulation{

  val httpProtocol = http
    .baseUrl("https://challengers.flood.io")
    .inferHtmlResources()
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")

  val myScenario = scenario("MyTestSimulation")
    .exec(
      Steps.startTest .pause(1),
      Steps.stepOne .pause(1,2),
      Steps.stepTwo .pause(1,3),
      mySessions.getMaxValueAndRadioForMaxValue .pause(1,2),
      Steps.stepThree .pause(1,3),
      mySessions.getListOfNamesAndValue .pause(1,2),
      Steps.stepFour .pause(1,2),
      Steps.getOneTimeTokenForStepFive .pause(1,3),
      Steps.stepFive .pause(1,2)
    )

 setUp(myScenario.inject(atOnceUsers(1))).protocols(httpProtocol)
  //setUp(myScenario.inject(rampUsers(20) over (5 minutes))).protocols(httpProtocol).maxDuration(6)

}