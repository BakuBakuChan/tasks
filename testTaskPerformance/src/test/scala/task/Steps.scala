package testTaskPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Steps {

  /** **********************************************************************************/
  /*HEADERS*/
  val defaltHeaders = Map("Upgrade-Insecure-Requests" -> "1")

  val headersForGetOneTimeTokenForStepFive = Map(
    "Accept" -> "*/*",
    "X-Requested-With" -> "XMLHttpRequest")

  /** **********************************************************************************/

  /*PARAMS MAPS*/
  var paramsInEveryStep = Map(("utf8", "✓")
    , ("commit", "Start")
    , ("authenticity_token", "${token}")
    , ("challenger[step_id]", "${step_id}"))

  var paramMapForFourStep = Map(("${challengerOrdersList1}", "${valuesList1}")
    , ("${challengerOrdersList2}", "${valuesList1}")
    , ("${challengerOrdersList3}", "${valuesList1}")
    , ("${challengerOrdersList4}", "${valuesList1}")
    , ("${challengerOrdersList5}", "${valuesList1}")
    , ("${challengerOrdersList6}", "${valuesList1}")
    , ("${challengerOrdersList7}", "${valuesList1}")
    , ("${challengerOrdersList8}", "${valuesList1}")
    , ("${challengerOrdersList9}", "${valuesList1}")
    , ("${challengerOrdersList10}", "${valuesList1}"))

  /** **********************************************************************************/

  /*RESPONSE BODIES*/
  var requestStartTest =
  http("Open_Start_Page")
    .get("/")
    .headers(defaltHeaders)

  var requestStepOne =
    http("Click_On_Start_Button")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
      .formParam("challenger[step_number]", "1")

  var requestStepTwo =
    http("Step_Two")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
      .formParam("challenger[step_number]", "2")
      .formParam("challenger[age]", "20")

  var requestStepThree =
    http("Step_Three")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
      .formParam("challenger[step_number]", "3")
      .formParam("challenger[largest_order]", "${challenger_largest_order}")
      .formParam("challenger[order_selected]", "${challenger_order_selected}")

  var requestStepFour =
    http("Step_Four")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
      .formParamMap(paramMapForFourStep)
      .formParam("challenger[step_number]", "4")

  var requestGetOneTimeTokenForStepFive =
    http("Get_One_Time_Token")
      .get("/code")
      .headers(headersForGetOneTimeTokenForStepFive)

  var requestStepFive =
    http("Step_Five")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
      .formParam("challenger[step_number]", "5")
      .formParam("challenger[one_time_token]", "${tokkenOnPage}")

  /** **********************************************************************************/

  /*REQUESTS WITH CHECKS*/
  val startTest = exec(
    requestStartTest

      .check(status is (200))

      .check(css("#new_challenger > div > input[type=hidden]:nth-child(2)", "value").saveAs("token"))
      .check(css("#challenger_step_id", "value").saveAs("step_id")))

  val stepOne = exec(
    requestStepOne

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/2"))

      .check(css("#challenger_step_id", "value").saveAs("step_id")))

  val stepTwo = exec(
    requestStepTwo

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/3"))

      .check(css("#challenger_step_id", "value").saveAs("step_id"))
      .check(css("input[type='radio']", "value").findAll.saveAs("listOfIds"))
      .check(regex("<label .*>(\\d{1,3})").findAll.saveAs("listOfNumbers")))

  val stepThree = exec(
    requestStepThree

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/4"))

      .check(css("#challenger_step_id", "value").saveAs("step_id"))
      .check(regex("challenger_order.*name=\"(challenger.+)\" type").findAll.saveAs("challengerOrdersList"))
      .check(regex("challenger_order.*name=\"challenger.*value=\"(\\d+)").findAll.saveAs("valuesList")))

  val stepFour = exec(
    requestStepFour

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/5"))

      .check(css("#challenger_step_id", "value").saveAs("step_id")))

  val getOneTimeTokenForStepFive = exec(
    requestGetOneTimeTokenForStepFive

      .check(status is (200))

      .check(jsonPath("$.code").ofType[String].saveAs("tokkenOnPage")))

  val stepFive = exec(
    requestStepFive

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/done")))

}