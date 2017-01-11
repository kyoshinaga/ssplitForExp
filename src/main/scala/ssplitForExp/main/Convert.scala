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

  private val wordsAndBorders:List[(String, String)] = words zip borders toList


  /*
   * Tagging BIO class and concatenate key and tag.
   *
   * BIO tag
   *  B : Begin of Sentence.        Value is 0.
   *  I : Continuation of Sentence. Value is 1.
   *  O : Outside of sentence.      Value is 2.
   */
  private val corpus: List[(String, Int)] = wordsAndBorders.flatMap{ x =>
    val w = ListBuffer.fill(x._1.length)(1)
    if (x._2 == "S")
      w(0) = 0
    x._1.map(x => x.toString) zip w
  } ::: List(("\n", 0))

  val fullCorpus: List[(String, Int)] = {
    val lengthOfCorpus = corpus.length
    val l = List.fill(10000 - lengthOfCorpus)("UNKNOWN" -> 2)
    corpus ::: l
  }

}

object Convert {

  def apply(path: String) = new Convert(path)

}
