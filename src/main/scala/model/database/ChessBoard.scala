package model.database


import slick.jdbc.H2Profile.api._

class ChessBoard (tag: Tag) extends Table[(Int, String, Boolean, Option[Boolean], Option[Boolean], Boolean)](tag, "CHESSBOARD") {

  def id = column[Int]("CHESSBOARD_ID", O.PrimaryKey, O.AutoInc)
  def field= column[String]("FIELD")
  def currentPlayer = column[Boolean]("CURRENTPLAYER")
  def check = column[Option[Boolean]]("CHECK")
  def checkMate = column[Option[Boolean]]("CHECKMATE")
  def simulated = column[Boolean]("SIMULATED")

  def * = (id, field, currentPlayer, check, checkMate, simulated)
}
