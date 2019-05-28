package testTaskPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
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
      Steps.startTest,
      Steps.stepOne,
      Steps.stepTwo,
      mySessions.getMaxValueAndRadioForMaxValue,
      Steps.stepThree,
      mySessions.getListOfNamesAndValue,
      Steps.stepFour,
      Steps.getOneTimeTokenForStepFive,
      Steps.stepFive
    )
  
  setUp(myScenario.inject(atOnceUsers(1))).protocols(httpProtocol)
}