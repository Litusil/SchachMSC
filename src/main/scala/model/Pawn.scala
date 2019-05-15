package model

case class Pawn(override val color : Boolean, override val hasMoved: Boolean) extends ChessPiece(color,hasMoved) {

  override def getPossibleMoves(chessBoard: ChessBoard): Vector[(Int, Int)] = {
    val pos = this.getPosition(chessBoard.field)
    var possibleMoves: Vector[(Int, Int)] = Vector()
    var yIncrementer = 1

    if (!this.color) {
      yIncrementer = -1
    }

    if (chessBoard.field.length > pos._1 + yIncrementer && chessBoard.field(pos._1 + yIncrementer)(pos._2).isEmpty){
      possibleMoves = possibleMoves :+ (pos._1 + yIncrementer, pos._2)
    }
    
    var xSchlagen = pos._2 + 1
    if (xSchlagen < chessBoard.field.length && (pos._1 + yIncrementer) < chessBoard.field.length){
      if(!chessBoard.field(pos._1 + yIncrementer)(pos._2 + 1).isEmpty) {
        if (chessBoard.field(pos._1 + yIncrementer)(pos._2 + 1).get.color != this.color) {
          possibleMoves = possibleMoves :+ (pos._1 + yIncrementer, pos._2 + 1)
        }
      }
    }
    xSchlagen = pos._2 - 1
    if(xSchlagen >= 0 && pos._1 + yIncrementer < chessBoard.field.length) {
      if(!chessBoard.field(pos._1 + yIncrementer)(pos._2 - 1).isEmpty) {
        if (chessBoard.field(pos._1 + yIncrementer)(pos._2 - 1).get.color != this.color) {
          possibleMoves = possibleMoves :+ (pos._1 + yIncrementer, pos._2 - 1)
        }
      }
    }

    if(!this.hasMoved) {
      if (this.color) {
        if ((pos._1 + 1) < chessBoard.field.length && !chessBoard.field(pos._1 + 1)(pos._2).isEmpty) {
          return possibleMoves
        }
        yIncrementer = 2
      } else {
        if ((pos._1 - 1) < chessBoard.field.length && !chessBoard.field(pos._1 - 1)(pos._2).isEmpty) {
          return possibleMoves
        }
        yIncrementer = -2
      }
        if (pos._1 + yIncrementer < chessBoard.field.length && chessBoard.field(pos._1 + yIncrementer)(pos._2).isEmpty) {
          possibleMoves = possibleMoves :+ (pos._1 + yIncrementer, pos._2)
        }
      }

    restrictMovement(chessBoard,this.color,pos,possibleMoves)
  }

  override def getPossibleAttacks(chessBoard: ChessBoard): Vector[(Int, Int)] = {
    var possibleAttacks: Vector[(Int, Int)] = Vector()
    val pos = this.getPosition(chessBoard.field)
    var yIncrementer = 1

    if(pos == (-1,-1)){
      return possibleAttacks
    }

    if (!this.color) {
      yIncrementer = -1
    }

    if (pos._2 + 1 < chessBoard.field.length && (pos._1 + yIncrementer) < chessBoard.field.length){
      val pos1 = chessBoard.field(pos._1 + yIncrementer)(pos._2 + 1)
      if (pos1.isEmpty || pos1.get.color != color) {
        possibleAttacks = possibleAttacks :+ (pos._1 + yIncrementer, pos._2 + 1)
      }
    }

    if(pos._2 - 1 >= 0 && pos._1 + yIncrementer < chessBoard.field.length) {
      val pos2 = chessBoard.field(pos._1 + yIncrementer)(pos._2 - 1)
      if (pos2.isEmpty || pos2.get.color != color ) {
        possibleAttacks = possibleAttacks :+ (pos._1 + yIncrementer, pos._2 - 1)
      }
    }
    possibleAttacks
  }



  override def updateMoved(): ChessPiece = {
    this.copy(hasMoved = true)
  }

  override def toString: String ={
    if (color) {
      return "\u2659"
    }
    "\u265F"
  }

}
