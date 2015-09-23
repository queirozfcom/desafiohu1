package models

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{Json, Writes}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

case class Hotel(id: Int, city: String, name: String)

class HotelsTable(tag: Tag) extends Table[Hotel](tag, "hotels") {

  def id = column[Int]("id", O.PrimaryKey)

  def city = column[String]("city")

  def name = column[String]("name")

  def * = (id, city, name) <>((Hotel.apply _).tupled, Hotel.unapply)

}

object Hotel {

  // create a database handle using Play's configuration for play-slick
  val db = DatabaseConfigProvider.get[JdbcProfile](Play.current).db

  // this enables us to treat a sql table as if it were a scala list
  val hotels = TableQuery[HotelsTable]

  def findAllByFragment(searchString: String, limit: Int = 10): Future[Seq[Hotel]] = {

    val searchStringLowerCase = searchString.toLowerCase


    // select both hotels that match the given string for their city and
    // for their name
    val actions = for {
      withName <- hotels.filter(_.name.toLowerCase like s"%$searchStringLowerCase%").sortBy(_.name.asc).take(limit).result
      withCity <- hotels.filter(_.city.toLowerCase like s"%$searchStringLowerCase%").sortBy(_.name.asc).take(limit).result
    } yield (withName, withCity)

    db.run(actions).map { pair =>
      val hotels = pair._1 ++ pair._2

      hotels.distinct
    }


  }


  def findById(id: Int): Future[Option[Hotel]] = {
    val query = hotels.filter(hotel => hotel.id === id)
    db.run(query.result.headOption)
  }


  def mustFindById(id: Int): Future[Hotel] = {
    val query = hotels.filter(hotel => hotel.id === id)
    db.run(query.result.head)
  }

  object JsonConverters {

    implicit val hotelWrites = new Writes[Hotel] {
      def writes(hotel: Hotel) = Json.obj(
        "id" -> hotel.id,
        "city" -> hotel.city,
        "name" -> hotel.name
      )
    }

  }

}