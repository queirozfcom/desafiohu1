package helpers

import java.time.LocalDate
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.lifted.TableQuery
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import models.{AvailabilitiesTable, HotelsTable, Availability, Hotel}

/**
 * Created by felipe on 22/09/15.
 */
object AvailabilityHelper {

  // create a database handle using Play's configuration for play-slick
  val db = DatabaseConfigProvider.get[JdbcProfile](Play.current).db

  val hotels = TableQuery[HotelsTable]
  val availabilities = TableQuery[AvailabilitiesTable]

  def hotelAvailability(hotel: Hotel, start: Option[LocalDate] = None, end: Option[LocalDate] = None): Future[Seq[(String, Boolean)]] = {

    val query = if (start.isEmpty || end.isEmpty)
      availabilities.filter(_.hotelId === hotel.id).result
    else {

      // neither are None
      // we need to convert to ISO date in order to perform
      // date comparison
      val startISODate = DateHelper.localDateToISODate(start.get)
      val endISODate = DateHelper.localDateToISODate(end.get)

      availabilities.filter(_.hotelId === hotel.id)
        .filter(_.date >= startISODate)
        .filter(_.date <= endISODate)
        .result
    }

    db.run(query).map { results =>
      results.map { availability =>
        val localDate = DateHelper.stringToLocalDate(availability.date).get
        val brazilianDate = DateHelper.localDateToBrazilianDateFormat(localDate)

        (brazilianDate, availability.available)
      }
    }

  }

}
