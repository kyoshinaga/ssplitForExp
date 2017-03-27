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
  private val suws = luws.map(x => x \\ "SUW")
  private val borders = luws.map(x => XMLUtil.getAttributionMap(x).getOrElse("B","_"))

  private val wordsAndBorders:List[(NodeSeq, String)] = (suws zip borders).toList

  /*
   * Tagging BIO class and concatenate key and tag.
   *
   * BIO tag
   *  B : Begin of Bunsetsu.                Value is 0.
   *  I : Continuation or end of Bunsetsu.  Value is 1.
   *  O : Outside of sentence.              Value is 2.
   */

  private val BIOcorpus: List[(String, Int)] = List(("\n",2)) :::
    wordsAndBorders.flatMap{x =>
      val w = ListBuffer.fill(x._1.length)(1)
      if(x._2 == "S" || x._2 == "B")
        w(0) = 0
      x._1.map(x => (x \\ "@lemma").toString) zip w
    } ::: List(("\n", 2))

  val BIOFullCorpus: List[(String, Int)] = {
    val lengthOfCorpus = BIOcorpus.length
    val l = List.fill(5000 - lengthOfCorpus)("UNKNOWN" -> 2)
    BIOcorpus ::: l
  }
}

object Convert {

  def apply(path: String) = new Convert(path)

}
