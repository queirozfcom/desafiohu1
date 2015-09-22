package helpers

import java.time.LocalDate
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import models.{Availability, Hotel}

/**
 * Created by felipe on 22/09/15.
 */
object AvailabilityHelper {


  def hotelIsAvailableOn(hotel:Hotel, start: Option[LocalDate], end:Option[LocalDate] ):Future[Boolean] = ???

}
