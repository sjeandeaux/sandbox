package com.sjeandeaux.sandbox.ignite

import org.apache.ignite.binary.BinaryObject
import org.apache.ignite.cache.CacheMode
import org.apache.ignite.configuration.{CacheConfiguration, IgniteConfiguration}
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.sharedfs.TcpDiscoverySharedFsIpFinder

/**
  * server <path of ip finder> <name of cache> <inject the cache here true false> <key in cache>
  *
  */
object Cache {

  def main(args: Array[String]): Unit = {
    println("Go.........")
    val (cache: Cache, inject: Boolean, keyInCache: Int) = args match {
      case Array(path, nameCache, inject, keyInCache) => (Cache(Some(path), nameCache), inject.toBoolean, keyInCache.toInt)
      case _ => {
        println("server <path of ip finder> <name of cache> <inject the cache here true false> <key in cache>")
        sys.exit(-1)
      }
    }

    if (inject) {
      for (i <- 1 to 5000) {
        cache.put(i, i)
      }
    }
    println(cache.get(keyInCache))
  }
}


case class Cache(path: Option[String] = None, nameCache: String) {

  import org.apache.ignite.Ignition

  implicit val ignite = Ignition.start(getConfiguration(path))

  val cache = ignite.getOrCreateCache(getCacheConfiguration(nameCache)).withKeepBinary[BinaryObject, BinaryObject]()

  def put(key: Int, value: Int): Unit = {
    cache.put(key.toKey(), value.toKey())
  }

  def get(keyInCache: Int): BinaryObject = {
    return cache.get(keyInCache.toKey())
  }

  def getConfiguration(path: Option[String] = None): IgniteConfiguration = {
    val cfg = new IgniteConfiguration
    cfg.setClientMode(false)
    val discoverySpi = new TcpDiscoverySpi
    val finder = new TcpDiscoverySharedFsIpFinder()

    path.foreach(finder.setPath(_))

    discoverySpi.setIpFinder(finder)
    cfg.setDiscoverySpi(discoverySpi)
  }

  def getCacheConfiguration(name: String): CacheConfiguration[BinaryObject, BinaryObject] = {
    val cacheConfiguration = new CacheConfiguration[BinaryObject, BinaryObject]()
    cacheConfiguration.setName(name)
    cacheConfiguration.setCacheMode(CacheMode.REPLICATED)
    cacheConfiguration.setReadThrough(false)
    cacheConfiguration.setWriteThrough(false)
    return cacheConfiguration
  }

}