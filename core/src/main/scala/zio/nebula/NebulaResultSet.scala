package zio.nebula

import java.util
import java.util.Spliterator
import java.util.function.Consumer

import scala.jdk.CollectionConverters.*

import com.vesoft.nebula.Row
import com.vesoft.nebula.client.graph.data.*
import com.vesoft.nebula.client.graph.data.ResultSet.Record
import com.vesoft.nebula.graph.PlanDescription

/**
 * @author
 *   梦境迷离
 * @version 1.0,2023/8/29
 */
final class NebulaResultSet(resultSet: ResultSet) {

  import NebulaResultSet.*

  def isSucceeded: Boolean = resultSet.isSucceeded

  def isEmpty: Boolean = resultSet.isEmpty

  def getErrorCode: Int = resultSet.getErrorCode

  def getSpaceName: String = resultSet.getSpaceName

  def getErrorMessage: String = resultSet.getErrorMessage

  def getComment: String = resultSet.getComment

  def getLatency: Long = resultSet.getLatency

  def getPlanDesc: PlanDescription = resultSet.getPlanDesc

  def keys: List[String] = resultSet.getColumnNames.asScala.toList

  def getColumnNames: List[String] = resultSet.getColumnNames.asScala.toList

  def rowsSize: Int = resultSet.rowsSize()

  def rowValues(index: Int): NebulaRecord = new NebulaRecord(resultSet.rowValues(index))

  def colValues(columnName: String): List[ValueWrapper] = resultSet.colValues(columnName).asScala.toList

  def getRows: List[Row] = resultSet.getRows.asScala.toList

  override def toString: String = resultSet.toString
}

object NebulaResultSet {

  final val decodeType = "utf-8"

  final class NebulaRecord(record: Record) extends Iterable[ValueWrapper] {
    override def iterator: Iterator[ValueWrapper] = record.iterator().asScala

    override def foreach[U](f: ValueWrapper => U): Unit = record.forEach((t: ValueWrapper) => f.apply(t))

    override def toString(): String = record.toString

    def spliterator: Spliterator[ValueWrapper] = record.spliterator

    def get(index: Int): ValueWrapper = record.get(index)

    def get(columnName: String): ValueWrapper = record.get(columnName)

    def values: util.List[ValueWrapper] = record.values()
    override def size: Int              = record.size()

    def contains(columnName: String): Boolean = record.contains(columnName)
  }
}