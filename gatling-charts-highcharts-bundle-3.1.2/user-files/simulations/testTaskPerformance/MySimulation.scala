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
  val t_numberOUsers = System.getProperty("users", "1").toInt
  val t_duration = System.getProperty("duration", "60").toInt
  val t_rampUp = System.getProperty("rampUp", "1").toInt

  /** **********************************************************************************/

  /*FEEDERS*/
  val jsonFileFeeder = jsonFile("ageFile.json").random
  /** **********************************************************************************/


  val httpProtocol = http
    .baseUrl("https://challengers.flood.io")
    .inferHtmlResources()
    .inferHtmlResources(BlackList(""".*\.js""", """.*css.*""",""".*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")
    .disableFollowRedirect

  val myScenario = scenario("MyTestSimulation")
    .during(t_duration seconds) {
      exec(Steps.startTest).pause(lowerPause)

        .exec(Steps.PostStepOne).pause(lowerPause, midlePause)
        .exec(Steps.stepOne).pause(lowerPause, midlePause)

        .feed(jsonFileFeeder)
        .exec(Steps.PostStepTwo).pause(lowerPause, midlePause)
        .exec(Steps.stepTwo).pause(lowerPause, higherPause)

        .exec(mySessions.getMaxValueAndRadioForMaxValue).pause(lowerPause, midlePause)

        .exec(Steps.PostStepThree).pause(lowerPause, midlePause)
        .exec(Steps.stepThree).pause(lowerPause)

        .exec(mySessions.getListOfNamesAndValue).pause(lowerPause, midlePause)

        .exec(Steps.PostStepFour).pause(lowerPause, midlePause)
        .exec(Steps.stepFour).pause(lowerPause, midlePause)
        .exec(Steps.getOneTimeTokenForStepFive).pause(lowerPause, higherPause)
		.randomSwitch(70d -> exec(Steps.PostStepFive).pause(lowerPause, midlePause)
        .exec(Steps.stepFive).pause(lowerPause, midlePause),
		 30d -> exec(http("Step_Five")
               .post("/start")
               .formParam("challenger[step_number]", "5")
               .formParam("challenger[one_time_token]", "0")
			   .check(status not(422))
               .check(status not(404))))
    }

  setUp(myScenario.inject(rampUsers(t_numberOUsers) during (t_rampUp seconds)))
    .protocols(httpProtocol)
}
