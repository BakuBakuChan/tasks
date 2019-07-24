package rpTest

import java.text.SimpleDateFormat
import java.util.Calendar

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object myRequests {
  /** ***********************************************/
  /*HEADERS*/
  var mainHeaders = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json",
    "Authorization" -> "bearer 0899ef96-8939-4bc1-b667-8dd59309ca48"
  )
  /** ***********************************************/

  /** ***********************************************/
  /*POST REQUEST BODIES*/
  var postCreateLaunch =
  http("POST_Create_Launch")
    .post("/launch")
    .headers(mainHeaders)
    .body(ElFileBody("create_launch.json")).asJson

  var postCreateSuite =
    http("POST_Create_Suite")
      .post("/item")
      .headers(mainHeaders)
      .body(ElFileBody("create_suite.json")).asJson

  var postCreateTest =
    http("POST_Create_Test")
      .post("/item/${suiteId}")
      .headers(mainHeaders)
      .body(ElFileBody("create_test.json")).asJson

  var postCreateStep =
    http("POST_Create_Step")
      .post("/item/${testId}")
      .headers(mainHeaders)
      .body(ElFileBody("create_step.json")).asJson

  var postCreateLog =
    http("POST_Create_Log")
      .post("/log")
      .headers(mainHeaders)
      .body(ElFileBody("create_log.json")).asJson

  var postCreateLogWithAttach =
    http("POST_Create_Log_With_Attach")
      .post("/log")
      .header("Content-Type", "multipart/form-data")
      .header("Authorization", "bearer 0899ef96-8939-4bc1-b667-8dd59309ca48")
      .bodyPart(ElFileBodyPart("json_request_part", "create_log_with_attach.json")
        .contentType("application/json")
        .transferEncoding("8bit")).asMultipartForm
      .bodyPart(RawFileBodyPart("file", "${fileName}")
        .contentType("${contentType}")
        .fileName("${fileName}")).asMultipartForm


  /*PUT REQUEST BODIES*/
  var putFinishStep =
    http("PUT_Finish_Item")
      .put("/item/${stepId}")
      .headers(mainHeaders)
      .body(ElFileBody("finish_item.json")).asJson

  var putFinishTest =
    http("PUT_Finish_Item")
      .put("/item/${testId}")
      .headers(mainHeaders)
      .body(ElFileBody("finish_item.json")).asJson

  var putFinishSuit =
    http("PUT_Finish_Item")
      .put("/item/${suitId}")
      .headers(mainHeaders)
      .body(ElFileBody("finish_item.json")).asJson


  var putFinishLaunch =
    http("PUT_Finish_Launch")
      .put("/launch/${launchId}/finish")
      .headers(mainHeaders)
      .body(ElFileBody("finish_launch.json")).asJson

  var putForceFinishLaunch =
    http("PUT_Force_Finish_Launch")
      .put("/launch/${launchId}/stop")
      .headers(mainHeaders)
      .body(ElFileBody("finish_launch.json")).asJson


  /*GET REQUEST BODIES*/
  var getLaunch =
    http("GET_Launch")
      .get("/launch/${launchIdToRead}")
      .headers(mainHeaders)

  var getLaunches =
    http("GET_Launches")
      .get("/launch")
      .headers(mainHeaders)

  var getUnfinishedLaunches =
    http("GET_Launches")
      .get("/launch?filter.eq.status=IN_PROGRESS")
      .headers(mainHeaders)

  var getSuits =
    http("GET_Suits")
      .get("/item?filter.eq.launch=${launchId}&filter.eq.type=SUITE")
      .headers(mainHeaders)

  var getTest =
    http("GET_Test")
      .get("/item?filter.eq.parent=${suiteId}&filter.eq.launch=${launchId}")
      .headers(mainHeaders)

  var getStep =
    http("GET_Step")
      .get("/item?filter.eq.parent=${testId}&filter.eq.launch=${launchId}")
      .headers(mainHeaders)

  var getLogs =
    http("GET_Logs")
      .get("/log?filter.eq.item=${stepId}")
      .headers(mainHeaders)

  /** ***********************************************/

  /** ***********************************************/
  /*POST REQUEST*/

  var postCreateLaunchRequest = exec(
    postCreateLaunch

      .check(status is (201))
      .check(jsonPath("$.id").ofType[String].saveAs("launchId"))
  )

  var postCreateSuiteRequest = exec(
    postCreateSuite

      .check(status is (201))
      .check(jsonPath("$.id").ofType[String].saveAs("suiteId"))
  )

  var postCreateTestRequest = exec(
    postCreateTest

      .check(status is (201))
      .check(jsonPath("$.id").ofType[String].saveAs("testId"))
  )

  var postCreateStepRequest = exec(
    postCreateStep

      .check(status is (201))
      .check(jsonPath("$.id").ofType[String].saveAs("stepId"))
  )

  var postCreateLogRequest = exec(
    postCreateLog

      .check(status is (201))
  )

  var postCreateLogWithAttachRequest = exec(
    postCreateLogWithAttach

      .check(status is (201))
  )

  /*PUT REQUEST*/
  var putFinishStepRequest = exec(
    putFinishStep

      .check(status is (200))
  )

  var putFinishTestRequest = exec(
    putFinishTest

      .check(status is (200))
  )

  var putFinishSuitRequest = exec(
    putFinishSuit

      .check(status is (200))
  )

  var putFinishLaunchRequest = exec(
    putFinishLaunch

      .check(status is (200))
  )

  var putForceFinishLaunchRequest = exec(
    putForceFinishLaunch

      .check(status is (200))
  )

  /*GET REQUEST*/

  var getLaunchesRequest = exec(
    getLaunches

      .check(status is (200))
      .check(jsonPath("$.page.totalElements").ofType[String].saveAs("numberOfLaunches"))
      .check(jsonPath("$.content[*].id").ofType[String].findAll.saveAs("launchesIds"))
  )

  var getUnfinishedLaunchesRequest = exec(
    getLaunches

      .check(status is (200))
      .check(jsonPath("$.page.totalElements").ofType[String].saveAs("numberOfUnfinishedLaunches"))
      .check(jsonPath("$.content[*].id").ofType[String].findAll.saveAs("unfinishedLaunchesIds"))
  )

  var getSuitsRequest = exec(
    getSuits

      .check(status is (200))
      .check(jsonPath("$.page.totalElements").ofType[String].saveAs("numberOfSuits"))
      .check(jsonPath("$.content[*].id").ofType[String].findAll.saveAs("suitsIds"))
  )

  var getTestsRequest = exec(
    getTest

      .check(status is (200))
      .check(jsonPath("$.page.totalElements").ofType[String].saveAs("numberOfTests"))
      .check(jsonPath("$.content[*].id").ofType[String].findAll.saveAs("testsIds"))
  )

  var getStepsRequest = exec(
    getStep

      .check(status is (200))
      .check(jsonPath("$.page.totalElements").ofType[String].saveAs("numberOfSteps"))
      .check(jsonPath("$.content[*].id").ofType[String].findAll.saveAs("stepsIds"))
  )

  var getLogsRequest = exec(
    getLogs

      .check(status is (200))
  )

  /** ***********************************************/

  /** ***********************************************/
  /*SESSIONS*/
  val debug = exec { session: Session =>
    println(session("numberOfLaunches").as[Int])
    println(session("launchesIds").as[Seq[String]])
    session.set("debag","debag")
  }

  val currentDate = exec { session: Session =>
    val DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    val myCurrentDate = Calendar.getInstance.getTime
    val dateFormat = new SimpleDateFormat(DateFormat)
    val formated = dateFormat.format(myCurrentDate)
    session.set("currentTime", formated)
  }

  val selectLaunchIdToRead = exec { session: Session =>
    val r = scala.util.Random
    val ids = session("launchesIds").as[Seq[String]]
    session.set("launchId", ids(r.nextInt(ids.length)))
  }

  val selectNumOfSuitsToRead = exec { session: Session =>
    val ids = session("suitsIds").as[Seq[String]]
    val halfOfNums = Math.round(ids.length / 2)
    val iterator = session("suitsIterator").as[Int] + 1
    session.set("suitsToRead", halfOfNums)
      .set("suitsIterator", iterator)
      .set("suiteId", ids(iterator))
  }

  val selectNumOfTestToRead = exec { session: Session =>
    val ids = session("testsIds").as[Seq[String]]
    val halfOfNums = Math.round(ids.length / 2)
    val iterator = session("testsIterator").as[Int] + 1
    session.set("testsToRead", halfOfNums)
      .set("testsIterator", iterator)
      .set("testId", ids(iterator))
  }

  val selectNumOfStepsToRead = exec { session: Session =>
    val ids = session("stepsIds").as[Seq[String]]
    val halfOfNums = Math.round(ids.length / 2)
    val iterator = session("stepsIterator").as[Int] + 1
    session.set("stepsToRead", halfOfNums)
      .set("stepsIterator", iterator)
      .set("stepId", ids(iterator))
  }

  val iteratorsInit = exec { session: Session =>
    session.set("suitsIterator", "-1")
      .set("testsIterator", "-1")
      .set("stepsIterator", "-1")
      .set("stepsToRead", "0")
      .set("testsToRead", "0")
      .set("suitsToRead", "0")
  }

  val finishLounches = exec{ session: Session =>
    val ids = session("unfinishedLaunchesIds").as[Seq[String]]
    val iterator = session("numberOfUnfinishedLaunches").as[Int] - 1
    session.set("launchId",ids(iterator))
      .set("numberOfUnfinishedLaunches",iterator)
  }
  /** ***********************************************/

}