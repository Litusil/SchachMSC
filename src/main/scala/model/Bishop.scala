package model

import scala.collection.immutable.Vector
import utility.MoveSetUtil.getSelectableFields
import utility.Direction

case class Bishop(override val color : Boolean, override val hasMoved : Boolean) extends ChessPiece(color, hasMoved) {

  override def getPossibleMoves(chessBoard: ChessBoard): Vector[(Int, Int)] = {
    val pos = this.getPosition(chessBoard.field)
    var possibleMoves: Vector[(Int, Int)] = (getSelectableFields(pos._2, pos._1, Direction.LEFT_UP,chessBoard.field)
    ++ getSelectableFields(pos._2, pos._1, Direction.RIGHT_UP,chessBoard.field)
    ++ getSelectableFields(pos._2, pos._1, Direction.LEFT_DOWN,chessBoard.field)
    ++ getSelectableFields(pos._2, pos._1, Direction.RIGHT_DOWN,chessBoard.field))
    restrictMovement(chessBoard,this.color,pos,possibleMoves)
  }

  override def getPossibleAttacks(chessBoard: ChessBoard): Vector[(Int, Int)] = {
    getPossibleMoves(chessBoard)
  }

  override def updateMoved(): ChessPiece = {
    this.copy(hasMoved = true)
  }

  override def toString: String ={
    if (color) {
      return "\u2657"
    }
    "\u265D"
  }
}
