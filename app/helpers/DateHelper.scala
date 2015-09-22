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

    Try(LocalDate.parse(isoDateString,formatter))

  }

}
