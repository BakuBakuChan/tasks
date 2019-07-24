package reportportalSimulation

import java.text.SimpleDateFormat
import java.util.Calendar

import io.gatling.core.Predef._
import io.gatling.http.Predef._


object SimulationRequests {
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
    .post("/test/launch")
    .headers(mainHeaders)
    .body(ElFileBody("create_launch.json")).asJson

  var postCreateSuite =
    http("POST_Create_Suite")
      .post("/test/item")
      .headers(mainHeaders)
      .body(ElFileBody("create_suite.json")).asJson

  var postCreateTestOrStep =
    http("POST_Create_Test")
      .post("/test/item/${parentId}")
      .headers(mainHeaders)
      .body(ElFileBody("create_test.json")).asJson

  var postCreateLog =
    http("POST_Create_Log")
      .post("/test/log")
      .headers(mainHeaders)
      .body(ElFileBody("create_log.json")).asJson

  var postCreateLogWithAttach =
    http("POST_Create_Log_With_Attach")
      .post("/test/log")
      .header("Content-Type", "multipart/form-data")
      .header("Authorization", "bearer 0899ef96-8939-4bc1-b667-8dd59309ca48")
      .bodyPart(ElFileBodyPart("json_request_part", "create_log_with_attach.json")
        .contentType("application/json")
        .transferEncoding("8bit")).asMultipartForm
      .bodyPart(RawFileBodyPart("file", "${fileName}")
        .contentType("${contentType}")
        .fileName("${fileName}")).asMultipartForm


  /*PUT REQUEST BODIES*/
  var putFinishItem =
    http("PUT_Finish_Item")
      .put("/test/item/${parentId}")
      .headers(mainHeaders)
      .body(ElFileBody("finish_item.json")).asJson


  var putFinishLaunch =
    http("PUT_Finish_Launch")
      .put("/test/launch/${launchId}/finish")
      .headers(mainHeaders)
      .body(ElFileBody("finish_launch.json")).asJson

  /*GET REQUEST BODIES*/
  var getLaunch =
    http("GET_Launch")
      .get("/test/launch/${launchIdToRead}")
      .headers(mainHeaders)

  var getLaunches =
    http("GET_Launches")
      .get("/test/launch")
      .headers(mainHeaders)

  var getSuits =
    http("GET_Suits")
      .get("/test/item?filter.eq.launch=${launchId}&filter.eq.type=SUITE")
      .headers(mainHeaders)

  var getItems =
    http("GET_Items")

      .get("/test/item?filter.eq.parent=${parentId}&filter.eq.launch=${launchId}")
      .headers(mainHeaders)

  var getItemByItemId =
    http("GET_Item_By_Item_Id")
      .get("/test/item/${itemId}")
      .headers(mainHeaders)

  var getLogs =
    http("GET_Logs")
      .get("/test/log?filter.eq.item=${parentId}")
      .headers(mainHeaders)

  /** ***********************************************/

  /** ***********************************************/
  /*REQUEST*/
  var postCreateLaunchRequest = exec(
    postCreateLaunch

      .check(status is (201))
      .check(jsonPath("$.id").ofType[String].saveAs("launchId"))
  )

  var postCreateSuiteRequest = exec(
    postCreateSuite

      .check(status is (201))
      .check(jsonPath("$.id").ofType[String].saveAs("parentId"))
  )


  var postCreateTestOrStepRequest = exec(
    postCreateTestOrStep

      .check(status is (201))
      .check(jsonPath("$.id").ofType[String].saveAs("parentId"))
  )

  val postCreateLogRequest = exec(
    postCreateLog

      .check(status is (201))
  )
  val postCreateLogWithAttachRequest = exec(
    postCreateLogWithAttach

      .check(status is (201))
  )

  val putFinishItemRequest = exec(
    putFinishItem

      .check(status is (200))
  )

  val putFinishLaunchRequest = exec(
    putFinishLaunch

      .check(status is (200))
  )

  var getSuitsRequest = exec(
    getSuits

      .check(status is (200))
      .check(jsonPath("$.page.totalElements").ofType[String].saveAs("numberOfSuits"))
      .check(jsonPath("$.content[*].id").ofType[String].findAll.saveAs("suitsIds"))
  )

  val getLogsRequest = exec(
    getLogs

      .check(status is (200))
      .check(jsonPath("$.page.totalElements").ofType[String].saveAs("numberOfLogs"))
  )

  val getItemssStepRequest = exec(
    getItems

      .check(status is (200))
      .check(jsonPath("$.page.totalElements").ofType[String].saveAs("numberOfSteps"))
      .check(jsonPath("$.content[*].id").ofType[String].findAll.saveAs("stepsIds"))
  )

  val getItemssTestsRequest = exec(
    getItems

      .check(status is (200))
      .check(jsonPath("$.page.totalElements").ofType[String].saveAs("numberOfTests"))
      .check(jsonPath("$.content[*].id").ofType[String].findAll.saveAs("testsIds"))
  )

  var getLaunchesRequest = exec(
    getLaunches

      .check(status is (200))
      .check(jsonPath("$.content[*].id").ofType[String].findAll.saveAs("launchesIds"))
  )
  /** ***********************************************/

  /** ***********************************************/
    /**SESSIONS**/
  val selectLaunchIdToRead = exec { session: Session =>
    val r = scala.util.Random
    val ids = session("launchesIds").as[Seq[String]]
    session.set("launchId", ids(r.nextInt(ids.length - 1)))
  }

  val selectNumOfSuitsToRead = exec { session: Session =>
    val ids = session("suitsIds").as[Seq[String]]
    val halfOfNums = (ids.length / 2).toInt
    val iterator = session("suitsIterator").as[Int] + 1
    session.set("suitsToRead", halfOfNums)
      .set("suitsIterator", iterator)
      .set("suitsIdToRead", ids(iterator))
  }

  val selectNumOfTestToRead = exec { session: Session =>
    val ids = session("testsIds").as[Seq[String]]
    val halfOfNums = (ids.length / 2).toInt
    val iterator = session("testsIterator").as[Int] + 1
    session.set("testsToRead", halfOfNums)
      .set("testsIterator", iterator)
      .set("testsIdToRead", ids(iterator))
  }

  val selectNumOfStepsToRead = exec { session: Session =>
    val ids = session("stepsIds").as[Seq[String]]
    val halfOfNums = (ids.length / 2).toInt
    val iterator = session("stepsIterator").as[Int] + 1
    session.set("stepsToRead", halfOfNums)
      .set("stepsIterator", iterator)
      .set("stepsIdToRead", ids(iterator))
  }

  val useTest = exec { session: Session =>
    val id = session("testsIdToRead").as[String]
    session.set("parentId", id)
  }

  val useSuit = exec { session: Session =>
    val id = session("suitsIdToRead").as[String]
    session.set("parentId", id)
  }

  val useStep = exec { session: Session =>
    val id = session("stepsIdToRead").as[String]
    session.set("parentId", id)
  }

  val iteratorsInit = exec { session: Session =>
    session.set("suitsIterator", "-1")
      .set("testsIterator", "-1")
      .set("stepsIterator", "-1")
      .set("stepsToRead", "0")
      .set("testsToRead", "0")
      .set("suitsToRead", "0")
  }


  val debag = exec { session: Session =>
    session.set("debag", "debag")
  }

  val currentDate = exec { session: Session =>
    val DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    val myCurrentDate = Calendar.getInstance.getTime
    val dateFormat = new SimpleDateFormat(DateFormat)
    val formated = dateFormat.format(myCurrentDate)
    session.set("currentTime", formated)
  }

  val saveSuiteId = exec { session: Session =>
    val id = session("parentId").as[String]
    session.set("suitId", id)
  }

  val saveTest = exec { session: Session =>
    val id = session("parentId").as[String]
    session.set("testId", id)
  }

  val useTestId = exec { session: Session =>
    val id = session("testId").as[String]
    session.set("parentId", id).set("type", "TEST")
  }

  val useSuitId = exec { session: Session =>
    val id = session("suitId").as[String]
    session.set("parentId", id)
  }


  val makeTypeTest = exec { session: Session =>
    session.set("type", "TEST")
  }

  val testToNull = exec { session: Session =>
    session.set("numberOfTests", "0")
  }

  val stepsToNull = exec { session: Session =>
    session.set("numberOfSteps", "0")
  }

  val logsToNull = exec { session: Session =>
    session.set("numberOfLogs", "0")
  }

  val allToNull = exec { session: Session =>
    session.set("numberOfSuits", "0")
      .set("numberOfTests", "0")
      .set("numberOfSteps", "0")
      .set("numberOfLogs", "0")
  }


  val typeTest = exec { session: Session =>
    session.set("type", "TEST").set("name", "Test")
  }

  val typeStep = exec { session: Session =>
    session.set("type", "STEP").set("name", "Step")
  }

  /** ***********************************************/
}