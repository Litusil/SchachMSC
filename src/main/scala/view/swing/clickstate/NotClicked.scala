package view.swing.clickstate

import view.swing.Field

class NotClicked() extends ClickState{
  override def handle(field:Field): Unit = {
    val possibleMoves = field.piece.get.getPossibleMoves(field.controller.chessBoard)
    field.parentGui.showPossibleMoves(possibleMoves)
    Field.selectedPiece = field.piece.get
  }

  override def nextState(): ClickState = {
    new Clicked()
  }

  override def toString: String = "notclicked"

}
