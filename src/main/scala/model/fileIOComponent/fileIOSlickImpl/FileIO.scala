package model.fileIOComponent.fileIOSlickImpl


import com.google.inject.{Guice, Inject}
import com.google.inject.name.{Named, Names}
import controller.ChessController
import model.database.ChessBoard
import model.fileIOComponent.FileIOInterface
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.Try
import slick.jdbc.H2Profile.api._
import slick.jdbc.meta.MTable
import util.JsonUtil

import scala.concurrent.ExecutionContext.Implicits.global

class FileIO @Inject()(@Named("H2Url") url: String, @Named("H2User") dbUser: String) extends FileIOInterface{

  val db =  Database.forURL(url, user = dbUser)

  // create schema if it doesn't exist
  val tables = List(
    TableQuery[ChessBoard]
  )
  val chessBoardQuery = TableQuery[ChessBoard]

  /*
  db.run(DBIO.seq(
    chessBoardQuery +=  (0, "",true, Some(true), Some(true), true)
  ))
  */

  val existingTables = db.run(MTable.getTables)
  val createSchemaFuture = existingTables.flatMap( v => {
    val names = v.map(mt => mt.name.name)
    val createIfNotExist = tables.filter( table =>
      (!names.contains(table.baseTableRow.tableName))).map(_.schema.create)
    db.run(DBIO.sequence(createIfNotExist))
  })
  Await.result(createSchemaFuture, Duration.Inf)

  override def load: model.ChessBoard = {


    val query = chessBoardQuery.map(_.*)
    val action = query.result
    val runresult = db.run( action)
    val result = Await.result(runresult, Duration.Inf)

    val field = result.head._2
    val json = JsonUtil()
    val chessVec = json.createFieldFromJSON(result.head._2)
    model.ChessBoard(chessVec, result.head._3,result.head._4,result.head._5,result.head._6)

  }

  override def save(chessBoard: model.ChessBoard): Unit = {


      val chessBoardQuery = TableQuery[ChessBoard]

      Await.result(db.run(chessBoardQuery.delete), Duration.Inf)

    db.run(DBIO.seq(
      chessBoardQuery += (0,chessBoard.fieldToJson().toString(), chessBoard.currentPlayer, chessBoard.check, chessBoard.checkmate, chessBoard.simulated)
    ))

    val query = chessBoardQuery.map(_.*)
    val action = query.result
    val results = db.run( action)
    results.foreach( println )

  }


}
