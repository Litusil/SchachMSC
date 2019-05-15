package model

case class King(override val color: Boolean,override val hasMoved: Boolean) extends ChessPiece(color,hasMoved) {

  override def getPossibleMoves(chessBoard: ChessBoard): Vector[(Int, Int)] = {
    val pos = this.getPosition(chessBoard.field)
    val kingMoves: Vector[(Int,Int)] = Vector((-1,0),(1,0),(0,1),(0,-1),(-1,1),(1,1),(-1,-1),(1,-1))
    var possibleMoves: Vector[(Int,Int)] = Vector()

    for (e <- kingMoves){
      val x = pos._2 + e._2
      val y = pos._1 + e._1
      if (x  >= 0 && x < chessBoard.field.length){
        if (y  >= 0 && y < chessBoard.field.length){
          if (chessBoard.field(y)(x).isEmpty|| chessBoard.field(y)(x).get.color != chessBoard.field(pos._1)(pos._2).get.color) {
            possibleMoves = possibleMoves :+ (y, x)
          }
        }
      }
    }
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
      return "\u2654"
    }
    "\u265A"
  }
}
