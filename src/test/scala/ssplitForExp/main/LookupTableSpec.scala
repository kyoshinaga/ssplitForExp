package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2017/01/11.
  */
import org.scalatest.{FlatSpec, Matchers}

class LookupTableSpec extends FlatSpec with Matchers {

  "element of key2id" should "equal id2key" in {

    val lookup = LookupTable(Set("あ","い"))

    val key2id = lookup.getKey2Id
    val id2key = lookup.getId2Key

    key2id("あ") should be (key2id(id2key(key2id("あ"))))
    key2id("い") should not be (key2id(id2key(key2id("あ"))))
  }

}
