package com.sjeandeaux.sandbox

import org.apache.ignite.Ignite
import org.apache.ignite.binary.{BinaryObject, BinaryObjectBuilder}

package object ignite {

  implicit class IntegerIgnite(i: Int) {
    def toKey()(implicit ignite: Ignite): org.apache.ignite.binary.BinaryObject = {
      val builder: BinaryObjectBuilder = getBuilder[BinaryObjectBuilder](ignite)
      builder.setField("key1", i)
      builder.setField("key2", i)
      builder.setField("key3", i)
      return builder.build()
    }

    def toValue()(implicit ignite: Ignite): org.apache.ignite.binary.BinaryObject = {
      val builder: BinaryObjectBuilder = getBuilder[BinaryObjectBuilder](ignite)
      builder.setField("value1", i)
      builder.setField("value2", i)
      builder.setField("value3", i)
      return builder.build()
    }
  }

  private def getBuilder[T](ignite: Ignite): BinaryObjectBuilder = {
    return ignite.binary().builder(classOf[BinaryObject].getName)
  }
}
