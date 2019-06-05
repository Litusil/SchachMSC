package model.fileIOComponent.fileIOSlickImpl


import com.google.inject.{Guice, Inject}
import com.google.inject.name.{Named, Names}
import model.database.ChessBoard
import model.fileIOComponent.FileIOInterface
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try
import slick.jdbc.H2Profile.api._
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global

class FileIO @Inject()(@Named("H2Url") url: String, @Named("H2User") dbUser: String) extends FileIOInterface{

  val db =  Database.forURL(url, user = dbUser)

  // create schema if it doesn't exist
  val tables = List(
    TableQuery[ChessBoard]
  )

  val existingTables = db.run(MTable.getTables)
  val createSchemaFuture = existingTables.flatMap( v => {
    val names = v.map(mt => mt.name.name)
    val createIfNotExist = tables.filter( table =>
      (!names.contains(table.baseTableRow.tableName))).map(_.schema.create)
    db.run(DBIO.sequence(createIfNotExist))
  })
  Await.result(createSchemaFuture, Duration.Inf)

  override def load: Try[Option[GridInterface]] = {
    var gridOption: Option[GridInterface] = None

    Try {
      val injector = Guice.createInjector(new SudokuModule)

      val grids = TableQuery[Grid]
      val cells = TableQuery[Cell]

      val gridQuery = for (
        grid <- grids
      ) yield grid

      val gridF = db.run(gridQuery.result)
      val gridRes = Await.result(gridF, Duration.Inf)
      val gridSize = gridRes.head._2

      gridSize match {
        case 1 =>
          gridOption =  Some(injector.instance[GridInterface](Names.named("tiny")))
        case 4 =>
          gridOption =
            Some(injector.instance[GridInterface](Names.named("small")))
        case 9 =>
          gridOption =
            Some(injector.instance[GridInterface](Names.named("normal")))
        case _ =>
      }

      val cellsQuery = for (
        cell <- cells
      ) yield cell

      val cellsF = db.run(cellsQuery.result)
      val cellsRes = Await.result(cellsF, Duration.Inf)

      gridOption match {
        case Some(grid) => {
          var _grid = grid

          cellsRes.foreach(c => {
            _grid = _grid.set(c._2, c._3, c._4)
            val given = c._5
            val showCandidates = c._6
            if (given) _grid = _grid.setGiven(c._2, c._3, c._4)
            if (showCandidates) _grid = _grid.setShowCandidates(c._2, c._3)
          })

          gridOption = Some(_grid)
        }
        case None =>
      }

      gridOption
    }
  }

  override def save(chessBoard: model.ChessBoard): Unit = {

    Try{
      val chessBoardQuery = TableQuery[ChessBoard]

      Await.result(db.run(chessBoardQuery.delete), Duration.Inf)



      db.run(DBIO.seq(
        chessBoardQuery += (0,chessBoard.fieldToJson().toString(), chessBoard.currentPlayer, chessBoard.check, chessBoard.checkmate, chessBoard.simulated)
      ))
    }
  }


}
