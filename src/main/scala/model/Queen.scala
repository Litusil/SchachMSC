package model

import utility.{Direction, MoveSetUtil}

case class Queen(override val color : Boolean, override val hasMoved: Boolean) extends ChessPiece(color, hasMoved) {

  override def getPossibleMoves(chessBoard: ChessBoard): Vector[(Int, Int)] = {
    val pos = this.getPosition(chessBoard.field)
    val possibleMoves: Vector[(Int,Int)] =
      (MoveSetUtil.getSelectableFields(pos._2, pos._1, Direction.LEFT,chessBoard.field)
    ++ MoveSetUtil.getSelectableFields(pos._2, pos._1, Direction.RIGHT,chessBoard.field)
    ++ MoveSetUtil.getSelectableFields(pos._2, pos._1, Direction.UP,chessBoard.field)
    ++ MoveSetUtil.getSelectableFields(pos._2, pos._1, Direction.DOWN,chessBoard.field)
    ++ MoveSetUtil.getSelectableFields(pos._2, pos._1, Direction.LEFT_UP,chessBoard.field)
    ++ MoveSetUtil.getSelectableFields(pos._2, pos._1, Direction.RIGHT_UP,chessBoard.field)
    ++ MoveSetUtil.getSelectableFields(pos._2, pos._1, Direction.LEFT_DOWN,chessBoard.field)
    ++ MoveSetUtil.getSelectableFields(pos._2, pos._1, Direction.RIGHT_DOWN,chessBoard.field))
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
      return "\u2655"
    }
    "\u265B"
  }

}
