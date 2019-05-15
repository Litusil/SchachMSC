package model

abstract class ChessPiece(val color: Boolean,val hasMoved: Boolean) {

  def getPossibleMoves(chessBoard: ChessBoard): Vector[(Int, Int)]

  def getPossibleAttacks(chessBoard: ChessBoard): Vector[(Int, Int)]

  def updateMoved(): ChessPiece

  //determines the position of the Chesspiece
  def getPosition(chessBoard: Vector[Vector[Option[ChessPiece]]]): (Int,Int) = {
    for (y <- chessBoard.indices) {
      for (x <- chessBoard.indices) {
        if(!chessBoard(y)(x).isEmpty){
          if (chessBoard(y)(x).get eq this) {
          return (y, x)
          }
        }
      }
    }
    (-1, -1)
  }

  def restrictMovement(chessBoard: ChessBoard,color: Boolean, pos: (Int,Int),theoreticalMoves: Vector[(Int,Int)]): Vector[(Int,Int)] ={
    var ret: Vector[(Int,Int)] = Vector()
    if(this.color == chessBoard.currentPlayer && !chessBoard.simulated){
      if(chessBoard.currentPlayer == color){
        for(move <- theoreticalMoves){
          val sim: Option[ChessBoard] = chessBoard.sim(pos._2,pos._1,move._2,move._1)
          sim match{
            case Some(board) =>  {
              if(color){
                if(!board.isWhiteCheck()){
                  ret = ret :+ move
                }
              } else {
                if(!board.isBlackCheck()){
                  ret = ret :+ move
                }
              }
            }
          }
        }
      }
    } else {
      return theoreticalMoves
    }
    ret
  }

}
