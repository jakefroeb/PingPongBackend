package controllers

import java.util.Calendar

import models._
import play.api._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current
import play.api.mvc.BodyParsers._
import play.api.libs.json.Json
import play.api.libs.json.Json._

object Application extends Controller{

  //create an instance of the table]
  val games = TableQuery[GamesTable]


  //JSON read/write macro
  implicit val playersFormat = Json.format[Players]
  implicit val gameFormat = Json.format[Game]

  def jsonFindAll = DBAction { implicit rs =>
    Ok(Json.obj("game" -> toJson(games.list))).withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
      "Access-Control-Allow-Credentials" -> "true",
      "Access-Control-Max-Age" -> (60 * 60 * 24).toString
    )
  }

  def jsonInsert = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[Players].map { players =>
      val game = Game(players.player_1,players.player_2,players.score_1,players.score_2, Calendar.getInstance().getTime.toString,None)
      games.insert(game)
        Ok(Json.obj("game" -> toJson(game))).withHeaders(
          "Access-Control-Allow-Origin" -> "*",
          "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
          "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
          "Access-Control-Allow-Credentials" -> "true",
          "Access-Control-Max-Age" -> (60 * 60 * 24).toString
        )
    }.getOrElse(BadRequest("invalid json"))    
  }

}
