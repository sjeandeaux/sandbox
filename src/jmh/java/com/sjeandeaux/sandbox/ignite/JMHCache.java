package com.sjeandeaux.sandbox.ignite;

import org.openjdk.jmh.annotations.*;
import scala.Option;

import java.util.concurrent.TimeUnit;

/**
 * sandbox
 */
@State(Scope.Thread)
public class JMHCache {


    private static final Cache cache = new Cache(Option.<String>empty(), "bob");


    @Benchmark
    @Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    public void get() {
        cache.get(3);
    }
}
