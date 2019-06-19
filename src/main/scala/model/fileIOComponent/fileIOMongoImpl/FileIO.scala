package model.fileIOComponent.fileIOMongoImpl

import com.google.inject.Inject
import com.google.inject.name.Named
import model.{ ChessPiece, ChessPieceFactory}
import model.fileIOComponent.FileIOInterface
import org.mongodb.scala.model.Projections
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.collection.immutable.Vector
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

class FileIO @Inject()(@Named("MongoDBHost") host: String, @Named("MongoDBPort") port: Int) extends FileIOInterface {

  private val mongoClient: MongoClient = MongoClient(s"mongodb://$host:$port")
  private val db: MongoDatabase = mongoClient.getDatabase("schach")
  private val collection = db.getCollection("schach-col")



  override def load: model.ChessBoard = {


    val resultFuture = collection.find().projection(Projections.excludeId()).toFuture()
    val result = Await.result(resultFuture, Duration.Inf)

    Try {
      val PieceFactory = new ChessPieceFactory
      val json: JsValue = Json.parse(result.head.toJson())

      val size = (json \ "grid" \ "size").get.toString.toInt
      var chessBoard = new model.ChessBoard(Vector.fill(size,size)(None: Option[ChessPiece]))
      val currentPlayer = (json \ "grid" \ "player").get.toString.toBoolean
      chessBoard = chessBoard.updatePlayer(currentPlayer)

      val cells = (json \ "grid" \ "cells").as[List[JsObject]]

      for (c <- cells){
        val row = (c \ "row").get.toString().toInt
        val col = (c \ "col").get.toString().toInt
        val hasMoved = (c \ "hasMoved").get.toString().toBoolean
        val piece = (c \ "piece").get.toString().replace("\"","").trim
        val updatedField: Vector[Vector[Option[ChessPiece]]] =  chessBoard.field.updated(row,chessBoard.field(row).updated(col,PieceFactory.create(piece,hasMoved)))

        chessBoard = chessBoard.updateField(updatedField)

      }
      return chessBoard
    }

    new model.ChessBoard(Vector.fill(8,8)(None: Option[ChessPiece])).defaultInit()
  }

  override def save(chessBoard: model.ChessBoard): Unit = {
    Try {
      Await.result(collection.drop().toFuture(), Duration.Inf)
      val gameStateDoc = Document.apply(chessBoard.toJson().toString())
      val resultFuture = collection.insertOne(gameStateDoc).toFuture()
      Await.result(resultFuture, Duration.Inf)
    }
    println("MongoDB saved")
  }

}
