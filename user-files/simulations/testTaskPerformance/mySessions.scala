package testTaskPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object mySessions{
  val getMaxValueAndRadioForMaxValue = exec { session: Session =>
    val mListNumbers = session("listOfNumbers").as[List[String]].map(_.toInt)
    val maxNumber = mListNumbers.max
    val radiosList = session("listOfIds").as[Seq[String]]
    val radioChecked = radiosList(mListNumbers.indexOf(maxNumber))
    session.set("challenger_largest_order", maxNumber).set("challenger_order_selected", radioChecked)
  }

  val getListOfNamesAndValue = exec { session: Session =>
    val ordersList = session("challengerOrdersList").as[Seq[String]]
    val valuesList = session("valuesList").as[Seq[String]]
    session
      .set("challengerOrdersList1", ordersList(0))
      .set("challengerOrdersList2", ordersList(1))
      .set("challengerOrdersList3", ordersList(2))
      .set("challengerOrdersList4", ordersList(3))
      .set("challengerOrdersList5", ordersList(4))
      .set("challengerOrdersList6", ordersList(5))
      .set("challengerOrdersList7", ordersList(6))
      .set("challengerOrdersList8", ordersList(7))
      .set("challengerOrdersList9", ordersList(8))
      .set("challengerOrdersList10", ordersList(9))
      .set("valuesList1", valuesList(0))
  }

  val debugFeeder = exec { session: Session =>
    val age = session("age").as[Int]
    println(age)
    session.set("debug","debug")
  }
}