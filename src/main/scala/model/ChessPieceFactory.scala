package model

class ChessPieceFactory {
  def create(piece: String, hasMoved: Boolean): Option[ChessPiece] = {
    piece match {
      case "♖" => Option(Rook(color = true,hasMoved))
      case "♘" => Option(Knight(color =true,hasMoved))
      case "♗" => Option(Bishop(color =true,hasMoved))
      case "♕" => Option(Queen(color =true,hasMoved))
      case "♔" => Option(King(color =true,hasMoved))
      case "♙" => Option(Pawn(color =true,hasMoved))
      case "♜" => Option(Rook(color =false,hasMoved))
      case "♞" => Option(Knight(color =false,hasMoved))
      case "♝" => Option(Bishop(color =false,hasMoved))
      case "♛" => Option(Queen(color =false,hasMoved))
      case "♚" => Option(King(color =false,hasMoved))
      case "♟" => Option(Pawn(color =false,hasMoved))
    }
  }
}
