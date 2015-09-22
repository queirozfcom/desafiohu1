package models

import java.sql.Date

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

case class Availability(hotelIid: Int, date: String, available: Boolean)

class AvailabilitiesTable(tag: Tag) extends Table[Availability](tag, "availabilities") {

  def hotelId = column[Int]("hotel_id")

  def date = column[String]("date")

  def available = column[Boolean]("available")

  def * = (hotelId, date, available) <>((Availability.apply _).tupled, Availability.unapply)

}

object Availability {

  // create a database handle using Play's configuration for play-slick
  val db = DatabaseConfigProvider.get[JdbcProfile](Play.current).db

  // this enables us to treat a sql table as if it were a scala list
  val availabilities = TableQuery[AvailabilitiesTable]


  def probe(hotelId: Int, date: String): Future[Boolean] = {

    val query = availabilities.filter(_.hotelId === hotelId).filter(_.date === date)

    db.run(query.result.headOption).map {
      case Some(availability) => availability.available
      case None => false
    }

  }

}