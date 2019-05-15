package view

import controller.ChessController

object UiMessage {
  case class CreateGui(controller: ChessController)
  case class CreateTui(controller: ChessController)
}