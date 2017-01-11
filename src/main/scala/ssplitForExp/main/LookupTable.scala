package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2017/01/10.
  */
import scala.collection.mutable

class LookupTable(s: Set[String]) {

  private val key2id:mutable.Map[String, Int] = mutable.Map[String, Int]("UNKNOWN" -> 1,"\n" -> 2)
  private val id2key:mutable.Map[Int, String] = mutable.Map[Int, String](1 -> "UNKNOWN", 2 -> "\n")

  private def maxId(): Int = key2id.values.max

  private def addMember(): Unit = s.map{x =>
    key2id.getOrElse(x, -1) match {
      case id:Int if id == -1 => {
        id2key(maxId + 1) = x
        key2id(x) = maxId + 1
      }
      case _ =>
    }
  }

  addMember()

  def appendMember(member: Set[String]): Unit = member.map{x =>
    key2id.getOrElse(x, -1) match {
      case id:Int if id == -1 => {
        id2key(maxId + 1) = x
        key2id(x) = maxId + 1
      }
      case _ =>
    }
  }

  def getKey2Id:mutable.Map[String, Int] = key2id
  def getId2Key:mutable.Map[Int, String] = id2key

}

object LookupTable{

  def apply(s: Set[String]) = new LookupTable(s)

}
