package view

import view.swing.Gui
import akka.actor.Actor
import view.UiMessage.{CreateGui, CreateTui}

case class UiFactory() extends Actor {

  override def receive: Receive = {

    case CreateGui(controller) => Gui(controller)
    case CreateTui(controller) => Tui(controller)
      val tui = Tui(controller)
      sender ! tui
  }
}