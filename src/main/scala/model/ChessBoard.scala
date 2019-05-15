package model

import play.api.libs.json._

import scala.collection.immutable.Vector

case class ChessBoard(field: Vector[Vector[Option[ChessPiece]]], currentPlayer: Boolean  = true, check: Option[Boolean] = None,checkmate: Option[Boolean] = None,simulated: Boolean = false) {

  def defaultInit(): ChessBoard ={
    val PieceFactory = new ChessPieceFactory
    var pieces: Vector[Option[ChessPiece]] = Vector()
    var updatedField: Vector[Vector[Option[ChessPiece]]] = Vector()

    pieces = (pieces
      :+ PieceFactory.create("♖",hasMoved = false)
      :+ PieceFactory.create("♘",hasMoved = false)
      :+ PieceFactory.create("♗",hasMoved = false)
      :+ PieceFactory.create("♕",hasMoved = false)
      :+ PieceFactory.create("♔",hasMoved = false)
      :+ PieceFactory.create("♗",hasMoved = false)
      :+ PieceFactory.create("♘",hasMoved = false)
      :+ PieceFactory.create("♖",hasMoved = false))

    updatedField = field.updated(0,pieces)
    pieces = Vector()

    for(i <- 0 to 7){
      pieces = pieces :+ PieceFactory.create("♙",hasMoved = false)
    }

    updatedField = updatedField.updated(1,pieces)
    pieces = Vector()

    pieces = (pieces
      :+ PieceFactory.create("♜",hasMoved = false)
      :+ PieceFactory.create("♞",hasMoved = false)
      :+ PieceFactory.create("♝",hasMoved = false)
      :+ PieceFactory.create("♛",hasMoved = false)
      :+ PieceFactory.create("♚",hasMoved = false)
      :+ PieceFactory.create("♝",hasMoved = false)
      :+ PieceFactory.create("♞",hasMoved = false)
      :+ PieceFactory.create("♜",hasMoved = false))

    updatedField = updatedField.updated(7,pieces)
    pieces = Vector()

    for(i <- 0 to 7){
      pieces = pieces :+ PieceFactory.create("♟",hasMoved = false)
    }

    updatedField = updatedField.updated(6,pieces)

    this.copy(field = updatedField)
  }

  def getAttackMoves(color: Boolean): Vector[(Int,Int)] = {
    var possibleAttacks: Vector[(Int,Int)] = Vector()
      for {
        row <- 0 until this.field.length
        col <- 0 until this.field.length
      } yield {
        this.field(row)(col) match {
          case Some(x: ChessPiece) => {
            if(color == x.color) {
              possibleAttacks = possibleAttacks ++ x.getPossibleAttacks(this)
            }
          }
          case None =>
        }
    }
    possibleAttacks
  }

  def getPossibleMoves(color: Boolean): Vector[(Int,Int)] = {
    var possibleMoves: Vector[(Int,Int)] = Vector()
    for {
      row <- 0 until this.field.length
      col <- 0 until this.field.length
    } yield {
      this.field(row)(col) match {
        case Some(x: ChessPiece) => {
          if(color == x.color) {
            possibleMoves = possibleMoves ++ x.getPossibleMoves(this)
          }
        }
        case None =>
      }
    }
    possibleMoves
  }

  def isWhiteCheck(): Boolean ={
    var possibleAttacks: Vector[(Int,Int)] = getAttackMoves(false)

    for {
      row <- 0 until this.field.length
      col <- 0 until this.field.length
    } yield {
      this.field(row)(col) match {
        case Some(x: ChessPiece) => {
          if(x.color) {
            if(x.toString == "\u2654"){
              if(possibleAttacks.contains((row,col))){
                return true
              }
            }
          }
        }
        case None =>
      }
    }
    false
  }

  def isBlackCheck(): Boolean = {
    var possibleAttacks: Vector[(Int,Int)] = getAttackMoves(true)

    for {
      row <- 0 until this.field.length
      col <- 0 until this.field.length
    } yield {
      this.field(row)(col) match {
        case Some(x: ChessPiece) => {
          if(!x.color) {
            if(x.toString == "\u265A"){
              if(possibleAttacks.contains((row,col))){
                return true
              }
            }
          }
        }
        case None =>
      }
    }
    false
  }

  def changePlayer() : ChessBoard = {
    this.copy(currentPlayer = !currentPlayer)
  }

  def updateField(update: Vector[Vector[Option[ChessPiece]]]): ChessBoard ={
    this.copy(field = update)
  }

  def putPiece(x: Int,y: Int, piece: Option[ChessPiece]): ChessBoard = {
    val updated: Vector[Vector[Option[ChessPiece]]] =  field.updated(y,field(y).updated(x,piece))
    updateField(updated)
  }

  def updatePlayer(update: Boolean): ChessBoard ={
    this.copy(currentPlayer = update)
  }

  def updateCheck(update: Option[Boolean]): ChessBoard ={
    this.copy(check = update)
  }

  def updateCheckmate(update: Option[Boolean]): ChessBoard ={
    this.copy(checkmate = update)
  }

  def updateSim(update: Boolean): ChessBoard ={
    this.copy(simulated = update)
  }

  def sim(x_start: Int,y_start: Int,x_ziel: Int,y_ziel: Int): Option[ChessBoard] ={
    val sim  = updateSim(true)
    sim.move(x_start: Int, y_start: Int, x_ziel: Int, y_ziel: Int)
  }

  def move(x_start: Int,y_start: Int,x_ziel: Int,y_ziel: Int): Option[ChessBoard] ={

    if(field.isEmpty || field(y_start)(x_start).get.color != currentPlayer) {
      return None
    }

    val moves = field(y_start)(x_start).get.getPossibleMoves(this)

    if (moves.contains((y_ziel,x_ziel))) {
      if(!field(y_ziel)(x_ziel).isEmpty){
        val kickedPiece = field(y_ziel)(x_ziel).get
      }

      var updatedField: Vector[Vector[Option[ChessPiece]]] =  field.updated(y_ziel,field(y_ziel).updated(x_ziel,Some(field(y_start)(x_start).get.updateMoved())))
      updatedField = updatedField.updated(y_start,updatedField(y_start).updated(x_start,None))

      var newBoard = updateField(updatedField)
      newBoard = newBoard.updateCheck(None)


      if(!simulated){
        //Is the other Player in check
        if(newBoard.currentPlayer){
          if(newBoard.isBlackCheck()){
            newBoard = newBoard.updateCheck(Option(false))
          }
        } else {
          if(newBoard.isWhiteCheck()){
            newBoard = newBoard.updateCheck(Option(true))
          }
        }
      }

      newBoard = newBoard.changePlayer()

      if(!simulated){
        if(newBoard.getPossibleMoves(newBoard.currentPlayer).length == 0){
          if(newBoard.currentPlayer){
            newBoard = newBoard.updateCheckmate(Option(true))
          } else {
            newBoard = newBoard.updateCheckmate(Option(false))
          }
        }
      }

      Some(newBoard)
    }else {
      None
    }
  }

  override def toString: String = {
    val xaxis = "   A|B| C| D|E| F| G|H" + "\n"
    val line = "|x" * 8 + "|\n"
    var board =  "\n"+ xaxis + ("y" + line) * 8

    for(i <- 0 to 7){
      for(j <- 0 to 7) {
        if(!field(i)(j).isEmpty) {
          board = board.replaceFirst("x",field(i)(j).get.toString)
        } else {
          board = board.replaceFirst("x","＿")
        }
        board = board.replaceFirst("y",(j + 1).toString)
      }
    }
    println(board)
    if(this.currentPlayer) {
      println("Weiß ist am Zug: ")
    }else {
      println("Schwarz ist am Zug: ")
    }
    println("____________________")

    board
  }

  def toJson(): JsObject = {

    var pieces: Vector[(Int,Int,Boolean,String)] = Vector()
    var checkString: String = ""
    var checkMateString: String = ""

    if(check.isEmpty){
      checkString = "None"
    } else {
      checkString = check.get.toString
    }

    if(checkmate.isEmpty){
      checkMateString = "None"
    } else {
      checkMateString = checkmate.get.toString
    }

    for (y <- field.indices) {
      for (x <- field.indices) {
        if (!field(y)(x).isEmpty) {
          pieces = pieces :+ (y,x,field(y)(x).get.hasMoved,field(y)(x).get.toString)
        }
      }
    }

    for (y <- field.indices) {
      for (x <- field.indices) {
        if (!field(y)(x).isEmpty) {
          pieces = pieces :+ (y,x,field(y)(x).get.hasMoved,field(y)(x).get.toString)
        }
      }
    }

    Json.obj(
      "grid" -> Json.obj(
        "size" -> JsNumber(field.length),
        "player" -> JsBoolean(currentPlayer),
        "simulated" -> JsBoolean(simulated),
        "check" -> JsString(checkString),
        "checkmate" -> JsString(checkMateString),
        "cells" -> Json.toJson(
          for {
            p <- pieces
          } yield {
            Json.obj(
              "row" -> p._1,
              "col" -> p._2,
              "hasMoved" -> p._3,
              "piece" -> p._4.toString
            )
          }
        )
      )
    )
  }


}
