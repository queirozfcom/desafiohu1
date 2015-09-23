package helpers

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.util.Try

/**
 * Created by felipe on 22/09/15.
 */
object DateHelper {

  def stringToLocalDate(isoDateString: String): Try[LocalDate] = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    Try(LocalDate.parse(isoDateString, formatter))
  }

  def brazilianDateStringToLocalDate(dateString: String): Try[LocalDate] = {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    Try(LocalDate.parse(dateString, formatter))
  }

  def localDateToISODate(ld: LocalDate): String = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    ld.format(formatter)
  }

  def localDateToBrazilianDateFormat(ld: LocalDate): String = {

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    ld.format(formatter)

  }


}
