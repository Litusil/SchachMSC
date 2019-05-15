package view.swing.clickstate

import view.swing.Field

trait ClickState {
  def handle(field:Field)
  def nextState():ClickState
}
