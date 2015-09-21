package controllers

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import slick.driver.JdbcProfile

class Application extends Controller {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }




}
