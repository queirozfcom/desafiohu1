package controllers

import models.{Hotel, Availability}
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import slick.driver.JdbcProfile
import helpers.{AvailabilityHelper => Helper}


/**
 * Created by felipe on 19/09/15.
 */
class BaseController extends Controller {

  def JsonResponse(jsval:JsValue):Result = {

    Ok(Json.prettyPrint(jsval))

  }

}
