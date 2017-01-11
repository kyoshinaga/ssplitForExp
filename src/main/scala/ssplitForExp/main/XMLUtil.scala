package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2017/01/11.
  */
import scala.xml._

object XMLUtil {
  def getNonEmptyChild(nodes: NodeSeq): NodeSeq = nodes.map(_.child filterNot (_.isAtom)).flatten

  def text(node: Node): String = node.child.collect {
    case t: Atom[_] => t.data
  }.mkString.trim

  def removeText(node: Elem) = node.copy(child = (node.child map {
    case t: Atom[_] => Text("")
    case other => other
  }))

  def find(node: Node, that: String): Node = (node \ that)(0)
  def findAll(node: Node, that: String): NodeSeq = node \ that
  def findSub(node: Node, that: String): Node = (node \\ that)(0)
  def findAllSub(node: Node, that: String): NodeSeq = node \\ that

  def hasChild(nodes: NodeSeq): Boolean = getNonEmptyChild(nodes).length > 0

  def getAttributionMap(node: Node): Map[String, String] = {
    for{
      elem <- node.attributes.seq
      n = elem.key -> elem.value.toString
    } yield  n
  }.toMap[String, String]

}
