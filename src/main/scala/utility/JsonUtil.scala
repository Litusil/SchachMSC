package util

import model.{ChessBoard, ChessPiece, ChessPieceFactory}
import play.api.libs.json._

import scala.collection.immutable.Vector
import scala.io.Source

case class JsonUtil() {

  def createBoardFromJSON (source: String): ChessBoard = {

    val PieceFactory = new ChessPieceFactory
    val json: JsValue = Json.parse(source)

    val size = (json \ "grid" \ "size").get.toString.toInt

    val checkString = (json \ "grid" \ "check").get.as[String]
    var check: Option[Boolean] = None: Option[Boolean]

    val checkmateString = (json \ "grid" \ "checkmate").get.as[String]
    var checkmate: Option[Boolean] = None: Option[Boolean]

    val simulated = (json \ "grid" \ "simulated").get.toString.toBoolean
    val currentPlayer = (json \ "grid" \ "player").get.toString.toBoolean

    if(checkString != "None"){
      checkmate = Some(checkmateString.toBoolean)
    }

    if(checkmateString != "None"){
      check = Some(checkString.toBoolean)
    }

    var chessBoard = ChessBoard(Vector.fill(size,size)(None: Option[ChessPiece]),currentPlayer,check,checkmate,simulated)

    val cells = (json \ "grid" \ "cells").as[List[JsObject]]

    for (c <- cells){
      val row = (c \ "row").get.toString().toInt
      val col = (c \ "col").get.toString().toInt
      val hasMoved = (c \ "hasMoved").get.toString().toBoolean
      val piece = (c \ "piece").get.toString().replace("\"","").trim
      val updatedField: Vector[Vector[Option[ChessPiece]]] =  chessBoard.field.updated(row,chessBoard.field(row).updated(col,PieceFactory.create(piece,hasMoved)))

      chessBoard = chessBoard.updateField(updatedField)
    }

    chessBoard
  }
}

