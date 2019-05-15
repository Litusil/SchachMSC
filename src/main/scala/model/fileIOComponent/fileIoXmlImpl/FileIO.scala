package model.fileIOComponent.fileIoXmlImpl

import model.fileIOComponent.FileIOInterface
import model.{ChessBoard, ChessPiece, ChessPieceFactory}

import scala.collection.immutable.Vector
import scala.xml.PrettyPrinter


class FileIO extends FileIOInterface {

  override def save(chessBoard: ChessBoard): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("board.xml" ))
    val prettyPrinter = new PrettyPrinter(120,4)
    val xml = prettyPrinter.format(gridToXML(chessBoard))
    pw.write(xml)
    pw.close()
  }

  def gridToXML(chessBoard: ChessBoard) = {
    <grid size ={chessBoard.field.length.toString} player = {chessBoard.currentPlayer.toString}>
      {
      for {
        row <- chessBoard.field.indices
        col <- chessBoard.field.indices
      } yield cellToXml(chessBoard.field, row, col)
      }
    </grid>
  }

  def cellToXml(board: Vector[Vector[Option[ChessPiece]]], row: Int, col:Int) = {
    if (!board(row)(col).isEmpty) {
      <cell row={row.toString} col={col.toString} hasMoved={board(row)(col).get.hasMoved.toString}>
        {board(row)(col)}
      </cell>
    }
  }


  override def load() : ChessBoard= {

    val PieceFactory = new ChessPieceFactory
    val file = scala.xml.XML.loadFile("board.xml")
    val size = (file \\ "grid" \ "@size").text.toInt
    var chessBoard = new ChessBoard(Vector.fill(size,size)(None: Option[ChessPiece]))
    val currentPlayer = (file \\ "grid" \ "@player").text.toBoolean
    chessBoard = chessBoard.updatePlayer(currentPlayer)

    val cellNodes= (file \\ "cell")

    for (cell <- cellNodes) {
      val row: Int = (cell \ "@row").text.toInt
      val col: Int = (cell \ "@col").text.toInt
      val hasMoved = (cell \ "@hasMoved").text.toBoolean
      val piece = (cell).text.trim
      val updatedField: Vector[Vector[Option[ChessPiece]]] =  chessBoard.field.updated(row,chessBoard.field(row).updated(col,PieceFactory.create(piece,hasMoved)))
      chessBoard = chessBoard.updateField(updatedField)
    }
    chessBoard
  }

}
