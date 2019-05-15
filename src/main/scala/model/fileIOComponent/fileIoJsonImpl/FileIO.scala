package model.fileIOComponent.fileIoJsonImpl

import model.fileIOComponent.FileIOInterface
import model.{ChessBoard, ChessPiece, ChessPieceFactory}
import play.api.libs.json._

import scala.collection.immutable.Vector
import scala.io.Source

class FileIO extends FileIOInterface {
  override def save(chessBoard: ChessBoard): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("board.json"))
    pw.write(Json.prettyPrint(chessBoard.toJson()))
    pw.close()
  }

  override def load: ChessBoard = {

    val PieceFactory = new ChessPieceFactory
    val source: String = Source.fromFile("board.json").getLines.mkString
    val json: JsValue = Json.parse(source)

    val size = (json \ "grid" \ "size").get.toString.toInt
    var chessBoard = new ChessBoard(Vector.fill(size,size)(None: Option[ChessPiece]))
    val currentPlayer = (json \ "grid" \ "player").get.toString.toBoolean
    chessBoard = chessBoard.updatePlayer(currentPlayer)

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



