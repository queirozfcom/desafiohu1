package controllers

import models.{Hotel, Availability}
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{JsBoolean, JsString, JsValue, Json}
import play.api.mvc._
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import helpers.{AvailabilityHelper => Helper, DateHelper}


import scala.concurrent.Future

/**
 * Created by felipe on 19/09/15.
 */
class AvailabilitiesController extends BaseController {

  // create a database handle using Play's configuration for play-slick
  val db = DatabaseConfigProvider.get[JdbcProfile](Play.current).db

  def index = Action {
    Ok(views.html.narrow())
  }

  def list(hotelId: Int, startDate: Option[String], endDate: Option[String]) = {
    Action.async(BodyParsers.parse.empty) { request =>

      Hotel.findById(hotelId).flatMap{
        case Some(hotel)=>{
          if(startDate.isEmpty || endDate.isEmpty) {


            Helper.hotelAvailability(hotel).map { lst =>
              val asJson = lst.map(pair => Json.obj("date" -> pair._1, "avail" -> pair._2)).toArray
              JsonResponse(Json.toJson(asJson))
            }
          }else{

            // neither are None
            val tryStartDate = DateHelper.brazilianDateStringToLocalDate(startDate.get)
            val tryEndDate = DateHelper.brazilianDateStringToLocalDate(endDate.get)

            //if both are valid dates, proceed, othwerwise return an empty array
            if(tryStartDate.isSuccess && tryEndDate.isSuccess ){

              val startLD = Some(tryStartDate.get)
              val endLD = Some(tryEndDate.get)

              Helper.hotelAvailability(hotel,startLD,endLD).map{ lst=>
                val asJson = lst.map( pair => Json.obj( "date"->pair._1, "avail"->pair._2 )).toArray
                JsonResponse(Json.toJson(asJson))
              }
            }else{
              Future(JsonResponse(Json.arr()))
            }

          }
        }
        case None => Future(BadRequest("Invalid hotelId"))
      }
    }
  }



}
