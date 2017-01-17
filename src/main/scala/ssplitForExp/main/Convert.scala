package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2017/01/10.
  */

import scala.collection.mutable.ListBuffer
import scala.xml._

/*
 * BIO tag
 *  B : Begin of Sentence. Value is 0.
 *  I : Continuation of Sentence. Value is 1.
 *  O : Outside of sentence. Value is 2.
 */
class Convert(path: String) {

  private val xml = XML.load(path)
  private val articles = XMLUtil.findAll(xml, "article")

  private val sentences = articles.map(x => XMLUtil.findAll(x, "sentence"))

  if (sentences.isEmpty)
    throw new IllegalAccessError("cannot construct " + getClass.getSimpleName + " without sentence node.")

  private val luws = sentences.flatMap(x => x.flatMap(n => XMLUtil.findAll(n, "LUW").toList))

  private val words = luws.map(x => x.text)
  private val borders = luws.map(x => XMLUtil.getAttributionMap(x).getOrElse("B","_"))

  private val wordsAndBorders:List[(String, String)] = (words zip borders).toList

  /*
   * Tagging BIO class and concatenate key and tag.
   *
   * BIO tag
   *  B : Begin of Sentence.                Value is 0.
   *  I : Continuation or end of sentence.  Value is 1.
   *  O : Outside of sentence.              Value is 2.
   */
  private val BIOcorpus: List[(String, Int)] = List(("\n",2)) :::
    wordsAndBorders.flatMap{x =>
    val w = ListBuffer.fill(x._1.length)(1)
    if(x._2 == "S")
      w(0) = 0
    x._1.map(x => x.toString) zip w
  } ::: List(("\n", 2))

  val BIOFullCorpus: List[(String, Int)] = {
    val lengthOfCorpus = BIOcorpus.length
    val l = List.fill(10000 - lengthOfCorpus)("UNKNOWN" -> 2)
    BIOcorpus ::: l
  }

  /*
   * Tagging IOE class and concatenate key and tag.
   *
   * IOE tag
   *  I : Begin or continuation of Sentence.  Value is 0.
   *  O : Outside of sentence.                Value is 1.
   *  E : End of Sentence.                    Value is 2.
   */
  private val wordsAndTag:List[(String, String)] = wordsAndBorders.scanRight(("\n", "S", "O")) {(x, z) =>
    z._2 match {
      case "S" => (x._1, x._2,  "E")
      case _ => (x._1, x._2,  "I")
    }
  }.map{x => (x._1, x._3)}
  private val IOEcorpus: List[(String, Int)] = List(("\n",1)) :::
    wordsAndTag.flatMap{ x =>
    val l = x._1.length
    val w = ListBuffer.fill(l)(x._2 match {
      case "O" => 1
      case _ => 0
    })
    if (x._2 == "E")
      w(l - 1) = 2
    x._1.map(x => x.toString) zip w
  }

  val IOEFullCorpus: List[(String, Int)] = {
    val lengthOfCorpus = IOEcorpus.length
    val l = List.fill(10000 - lengthOfCorpus)("UNKNOWN" -> 1)
    IOEcorpus ::: l
  }
}

object Convert {

  def apply(path: String) = new Convert(path)

}
