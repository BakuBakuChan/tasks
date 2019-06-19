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

  var paramMapForFourStep = Map("${challengerOrdersList1}" -> "${valuesList1}"
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
  var requestGetStartTest =
  http("Open_Start_Page")
    .get("/")
    .headers(defaltHeaders)

  var requestPostStepOne =
    http("Click_On_Start_Button")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
      .formParam("challenger[step_number]", "1")

  var requestPostStepTwo =
    http("Step_Two")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
      .formParam("challenger[step_number]", "2")
      .formParam("challenger[age]", "${age}")

  var requestPostStepThree =
    http("Step_Three")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
      .formParam("challenger[step_number]", "3")
      .formParam("challenger[largest_order]", "${challenger_largest_order}")
      .formParam("challenger[order_selected]", "${challenger_order_selected}")

  var requestPostStepFour =
    http("Step_Four")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
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
      .formParam("challenger[step_number]", "4")

  var requestGetOneTimeTokenForStepFive =
    http("Get_One_Time_Token")
      .get("/code")
      .headers(headersForGetOneTimeTokenForStepFive)

  var requestPostStepFive =
    http("Step_Five")
      .post("/start")
      .headers(defaltHeaders)
      .formParamMap(paramsInEveryStep)
      .formParam("challenger[step_number]", "5")
      .formParam("challenger[one_time_token]", "${tokkenOnPage}")

  var requestGetStepTwo =
    http("Get_Step_Two")
      .get("/step/2")
      .headers(defaltHeaders)

  var requestGetStepThree =
    http("Get_Step_Tree")
      .get("/step/3")
      .headers(defaltHeaders)

  var requestGetStepFour =
    http("Get_Step_Foure")
      .get("/step/4")
      .headers(defaltHeaders)

  var requestGetStepFive =
    http("Get_Step_Five")
      .get("/step/5")
      .headers(defaltHeaders)

  var requestGetDone =
    http("Get_Done_Page")
      .get("/done")
      .headers(defaltHeaders)

  /** **********************************************************************************/
  /*POST REQUESTS*/
  var PostStepOne = exec(requestPostStepOne
    .check(status is (302)))

  var PostStepTwo = exec(requestPostStepTwo
    .check(status is (302)))

  var PostStepThree = exec(requestPostStepThree
    .check(status is (302)))

  var PostStepFour = exec(requestPostStepFour
    .check(status is (302)))

  var PostStepFive = exec(requestPostStepFive
    .check(status not(422))
    .check(status not(404)))

  /** **********************************************************************************/

  /*GET REQUESTS WITH CHECKS*/
  val startTest = exec(
    requestGetStartTest

      .check(status is (200))

      .check(css("#new_challenger > div > input[type=hidden]:nth-child(2)", "value").saveAs("token"))
      .check(css("#challenger_step_id", "value").saveAs("step_id")))


  val stepOne = exec(
    requestGetStepTwo

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/2"))

      .check(css("#challenger_step_id", "value").saveAs("step_id")))

  val stepTwo = exec(
    requestGetStepThree

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/3"))

      .check(css("#challenger_step_id", "value").saveAs("step_id"))
      .check(css("input[type='radio']", "value").findAll.saveAs("listOfIds"))
      .check(regex("<label .*>(\\d{1,3})").findAll.saveAs("listOfNumbers")))

  val stepThree = exec(
    requestGetStepFour

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/4"))

      .check(css("#challenger_step_id", "value").saveAs("step_id"))
      .check(regex("challenger_order.*name=\"(challenger.+)\" type").findAll.saveAs("challengerOrdersList"))
      .check(regex("challenger_order.*name=\"challenger.*value=\"(\\d+)").findAll.saveAs("valuesList")))

  val stepFour = exec(
    requestGetStepFive

      .check(status is (200))
      .check(currentLocation.is("https://challengers.flood.io/step/5"))

      .check(css("#challenger_step_id", "value").saveAs("step_id")))

  val getOneTimeTokenForStepFive = exec(
    requestGetOneTimeTokenForStepFive

      .check(status is (200))

      .check(jsonPath("$.code").ofType[String].saveAs("tokkenOnPage")))

  val stepFive = exec(
    requestGetDone

      .check(status not(404),status not(422))
      .check(currentLocation.is("https://challengers.flood.io/done")))

}