package view


import controller.ChessController
import util.Observer



case class Tui(controller: ChessController) extends Observer {
  controller.add(this)
  print()

  override def update(): Unit ={
    print()
  }

  def print(): String ={
    controller.chessBoard.toString
  }

  def processInputLine(eingabe: Option[String]): Unit ={
    if (eingabe.isEmpty){
      println("falsche Eingabe!\n")
      return
    }

    if (eingabe.get == "save"){
      controller.save()
      return
    }

    if (eingabe.get == "load"){
      controller.load()
      return
    }

    if (eingabe.get.trim().toUpperCase.matches("[a-hA-H][1-8]( |-)[a-hA-H][1-8]")){
      var command = eingabe.get.trim().toUpperCase
      command = command.replaceAll("A","0")
      command = command.replaceAll("B","1")
      command = command.replaceAll("C","2")
      command = command.replaceAll("D","3")
      command = command.replaceAll("E","4")
      command = command.replaceAll("F","5")
      command = command.replaceAll("G","6")
      command = command.replaceAll("H","7")
      controller.move(command(0).toInt-48,command(1).toInt-49,command(3).toInt-48,command(4).toInt-49)
    }else {
      println("falsche Eingabe!\n")
    }
  }

}
