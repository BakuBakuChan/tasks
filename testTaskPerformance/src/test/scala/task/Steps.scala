package testTaskPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Steps{

  val headers_6 = Map(
    "Accept" -> "*/*",
    "X-Requested-With" -> "XMLHttpRequest")

  val startTest = exec(
    http("Open_Start_Page")
      .get("/")

      .check(status is (200))

      .check(css("#new_challenger > div > input[type=hidden]:nth-child(2)", "value").saveAs("token"))
      .check(css("#challenger_step_id", "value").saveAs("step_id")))

  val stepOne = exec(
    http("Click_On_Start_Button")
      .post("/start")
      .formParam("utf8", "✓")
      .formParam("authenticity_token", "${token}")
      .formParam("challenger[step_id]", "${step_id}")
      .formParam("challenger[step_number]", "1")
      .formParam("commit", "Start")

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/2"))

      .check(css("#challenger_step_id", "value").saveAs("step_id")))



  val stepTwo = exec(
    http("Step_Two")
      .post("/start")
      .formParam("utf8", "✓")
      .formParam("authenticity_token", "${token}")
      .formParam("challenger[step_id]", "${step_id}")
      .formParam("challenger[step_number]", "2")
      .formParam("challenger[age]", "20")
      .formParam("commit", "Next")

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/3"))

      .check(css("#challenger_step_id", "value").saveAs("step_id"))
      .check(css("input[type='radio']", "value").findAll.saveAs("listOfIds"))
      .check(regex("<label .*>(\\d{1,3})").findAll.saveAs("listOfNumbers")))


  val stepThree = exec(
    http("Step_Three")
      .post("/start")
      .formParam("utf8", "✓")
      .formParam("authenticity_token", "${token}")
      .formParam("challenger[step_id]", "${step_id}")
      .formParam("challenger[step_number]", "3")
      .formParam("challenger[largest_order]", "${challenger_largest_order}")
      .formParam("challenger[order_selected]", "${challenger_order_selected}")
      .formParam("commit", "Next")

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/4"))

      .check(css("#challenger_step_id", "value").saveAs("step_id"))
      .check(regex("challenger_order.*name=\"(challenger.+)\" type").findAll.saveAs("challengerOrdersList"))
      .check(regex("challenger_order.*name=\"challenger.*value=\"(\\d+)").findAll.saveAs("valuesList")))



  val stepFour = exec(
    http("Step_Four")
      .post("/start")
      .formParam("utf8", "✓")
      .formParam("authenticity_token", "${token}")
      .formParam("challenger[step_id]", "${step_id}")
      .formParam("challenger[step_number]", "4")
      .formParam("${challengerOrdersList1}", "${valuesList1}")
      .formParam("${challengerOrdersList2}", "${valuesList1}")
      .formParam("${challengerOrdersList3}", "${valuesList1}")
      .formParam("${challengerOrdersList4}", "${valuesList1}")
      .formParam("${challengerOrdersList5}", "${valuesList1}")
      .formParam("${challengerOrdersList6}", "${valuesList1}")
      .formParam("${challengerOrdersList7}", "${valuesList1}")
      .formParam("${challengerOrdersList8}", "${valuesList1}")
      .formParam("${challengerOrdersList9}", "${valuesList1}")
      .formParam("${challengerOrdersList10}", "${valuesList1}")
      .formParam("commit", "Next")

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/5"))

      .check(css("#challenger_step_id", "value").saveAs("step_id")))

  val getOneTimeTokenForStepFive = exec(
    http("Get_One_Time_Token")
      .get("/code")
      .headers(headers_6)

      .check(status is (200))

      .check(jsonPath("$.code").ofType[String].saveAs("tokkenOnPage")))

  val stepFive = exec(
    http("Step_Five")
      .post("/start")
      .formParam("utf8", "✓")
      .formParam("authenticity_token", "${token}")
      .formParam("challenger[step_id]", "${step_id}")
      .formParam("challenger[step_number]", "5")
      .formParam("challenger[one_time_token]", "${tokkenOnPage}")
      .formParam("commit", "Next")
      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/done")))

}