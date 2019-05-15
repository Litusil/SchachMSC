package model.fileIOComponent

import model.ChessBoard

trait FileIOInterface {

  def load(): ChessBoard
  def save(chessBoard: ChessBoard): Unit

}