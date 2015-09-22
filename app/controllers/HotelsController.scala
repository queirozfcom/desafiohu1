package controllers

import models.Hotel
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import models.Hotel.JsonConverters._

import scala.concurrent.Future

/**
 * Created by felipe on 19/09/15.
 */
class HotelsController extends Controller {

  // create a database handle using Play's configuration for play-slick
  val db = DatabaseConfigProvider.get[JdbcProfile](Play.current).db

  def index = Action {
    Ok(views.html.narrow())
  }

  def list(searchString: String, startDate: Option[String], endDate: Option[String]) =
    Action.async(BodyParsers.parse.empty) { request =>

      Hotel.findAllByFragment(searchString, startDate, endDate).flatMap { hotels =>

        val namesAndCities:Seq[JsValue] = hotels.map(hotel => Json.obj( "value" -> (hotel.name + " - " + hotel.city) ) )

        val asJson = Json.toJson(namesAndCities)

        Future(Ok(Json.prettyPrint(asJson)))
      }
    }
}
