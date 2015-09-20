package controllers

import play.api.mvc._

/**
 * Created by felipe on 19/09/15.
 */
class HotelsController extends Controller{

  def index = Action{
    Ok(views.html.narrow())
  }

}
