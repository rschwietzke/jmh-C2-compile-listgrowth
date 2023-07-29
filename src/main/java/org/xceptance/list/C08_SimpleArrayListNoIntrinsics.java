package org.xceptance.list;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.BenchmarkParams;

import com.xceptance.common.util.SimpleArrayListNoIntrinsics;

/**
 * Similar to C2 but we use another simple array list impl, SimpleArrayListNoIntrinsics,
 * that does not use Arrays.copy but does that manually using a classic loop.

Benchmark                                                 Mode  Cnt    Score     Error  Units
C08_SimpleArrayListNoIntrinsics.m00_basicReadPerformance  avgt   10  199.507 ±   0.616  ns/op
C08_SimpleArrayListNoIntrinsics.m01_small_small           avgt   10  974.811 ±   1.109  ns/op
C08_SimpleArrayListNoIntrinsics.m02_large_large           avgt   10  258.673 ±   0.116  ns/op
C08_SimpleArrayListNoIntrinsics.m03_small_large           avgt   10  975.743 ±   2.030  ns/op
C08_SimpleArrayListNoIntrinsics.m04_large_small           avgt   10  298.106 ± 115.092  ns/op

 * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class C08_SimpleArrayListNoIntrinsics
{
    int iterationCount;
    String src;

    List<String> result;

    final String LONG = "R,CandleDaySalesPage.2,1666954266805,95,false,1349,429,200,https://production-test.justacmecompany.com/on/dishwasher.store/Sites-justacmecompany-Site/en_US/__Analytics-Start?url=https%3A%2F%2Fproduction-test.justacmecompany.com%2Fs%2Fjustacmecompany%2Fc%2Fhome-smellstuff%2Fworkhelp4life&res=1600x1200&cookie=1&cmpn=&java=0&gears=0&fla=0&ag=0&dir=0&pct=0&pdf=0&qt=0&realp=0&tz=US%2FEastern&wma=1&pcat=new-arrivals&title=3-Wick+Scented+Candles+-+Swim+%26+Swamp+Tier&dwac=0.7629667259452815&r=2905563956785988054&ref=https%3A%2F%2Fproduction-test.justacmecompany.com%2F&data=givemesomedatathatjustfillshere,image/gif,0,0,95,0,95,95,,GET,,,0,,";

    @Setup(Level.Iteration)
    public void setup(BenchmarkParams params)
    {
        src = new String(LONG.toCharArray());

        iterationCount++;

        int size = 0;

        if (params.getBenchmark().endsWith("large_small"))
        {
            size = iterationCount <= params.getWarmup().getCount() ? 50 : 1;
        }
        else if (params.getBenchmark().endsWith("small_large"))
        {
            size = iterationCount <= params.getWarmup().getCount() ? 1 : 50;
        }
        else if (params.getBenchmark().endsWith("small_small"))
        {
            size = 1;
        }
        else if (params.getBenchmark().endsWith("large_large"))
        {
            size = 50;
        }
        System.out.printf("(Initial List Size %d) - ", size);
        result= new SimpleArrayListNoIntrinsics<>(size);
    }

    /*
# Warmup Iteration   1: (Initial List Size 0) - 207.189 ns/op
# Warmup Iteration   2: (Initial List Size 0) - 205.905 ns/op
# Warmup Iteration   3: (Initial List Size 0) - 199.426 ns/op
# Warmup Iteration   4: (Initial List Size 0) - 199.445 ns/op
# Warmup Iteration   5: (Initial List Size 0) - 199.349 ns/op
Iteration   1: (Initial List Size 0) - 199.382 ns/op
Iteration   2: (Initial List Size 0) - 199.440 ns/op
Iteration   3: (Initial List Size 0) - 199.340 ns/op
Iteration   4: (Initial List Size 0) - 199.361 ns/op
Iteration   5: (Initial List Size 0) - 199.431 ns/op
Iteration   6: (Initial List Size 0) - 199.332 ns/op
Iteration   7: (Initial List Size 0) - 200.662 ns/op
Iteration   8: (Initial List Size 0) - 199.344 ns/op
Iteration   9: (Initial List Size 0) - 199.417 ns/op
Iteration  10: (Initial List Size 0) - 199.357 ns/op
     */
    @Benchmark
    public int m00_basicReadPerformance()
    {
        final int size = src.length();
        int count = 0;

        for (int pos = 0; pos < size; pos++)
        {
            char c = src.charAt(pos);

            if (c == ',')
            {
                count++;
            }
        }

        return count;
    }

    /*
# Warmup Iteration   1: (Initial List Size 1) - 225.379 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 551.501 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 974.720 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 974.393 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 974.599 ns/op
Iteration   1: (Initial List Size 1) - 974.540 ns/op
Iteration   2: (Initial List Size 1) - 974.655 ns/op
Iteration   3: (Initial List Size 1) - 976.836 ns/op
Iteration   4: (Initial List Size 1) - 974.794 ns/op
Iteration   5: (Initial List Size 1) - 974.559 ns/op
Iteration   6: (Initial List Size 1) - 974.758 ns/op
Iteration   7: (Initial List Size 1) - 974.270 ns/op
Iteration   8: (Initial List Size 1) - 974.804 ns/op
Iteration   9: (Initial List Size 1) - 974.555 ns/op
Iteration  10: (Initial List Size 1) - 974.342 ns/op
     */
    @Benchmark
    public List<String> m01_small_small()
    {
        result.clear();
        final int size = src.length();

        for (int pos = 0; pos < size; pos++)
        {
            char c = src.charAt(pos);

            if (c == ',')
            {
                result.add(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 224.800 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 239.367 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 258.748 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 258.690 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 258.741 ns/op
Iteration   1: (Initial List Size 50) - 258.610 ns/op
Iteration   2: (Initial List Size 50) - 258.630 ns/op
Iteration   3: (Initial List Size 50) - 258.601 ns/op
Iteration   4: (Initial List Size 50) - 258.724 ns/op
Iteration   5: (Initial List Size 50) - 258.600 ns/op
Iteration   6: (Initial List Size 50) - 258.840 ns/op
Iteration   7: (Initial List Size 50) - 258.650 ns/op
Iteration   8: (Initial List Size 50) - 258.705 ns/op
Iteration   9: (Initial List Size 50) - 258.732 ns/op
Iteration  10: (Initial List Size 50) - 258.634 ns/op
     */
    @Benchmark
    public List<String> m02_large_large()
    {
        result.clear();
        final int size = src.length();

        for (int pos = 0; pos < size; pos++)
        {
            char c = src.charAt(pos);

            if (c == ',')
            {
                result.add(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 1) - 224.606 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 553.979 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 978.301 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 977.366 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 976.735 ns/op
Iteration   1: (Initial List Size 50) - 974.490 ns/op
Iteration   2: (Initial List Size 50) - 976.225 ns/op
Iteration   3: (Initial List Size 50) - 978.336 ns/op
Iteration   4: (Initial List Size 50) - 974.428 ns/op
Iteration   5: (Initial List Size 50) - 974.193 ns/op
Iteration   6: (Initial List Size 50) - 976.676 ns/op
Iteration   7: (Initial List Size 50) - 974.611 ns/op
Iteration   8: (Initial List Size 50) - 976.407 ns/op
Iteration   9: (Initial List Size 50) - 976.671 ns/op
Iteration  10: (Initial List Size 50) - 975.396 ns/op
     */
    @Benchmark
    public List<String> m03_small_large()
    {
        result.clear();
        final int size = src.length();

        for (int pos = 0; pos < size; pos++)
        {
            char c = src.charAt(pos);

            if (c == ',')
            {
                result.add(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 224.776 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 239.676 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 260.398 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 258.617 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 258.731 ns/op
Iteration   1: (Initial List Size 1) - 238.341 ns/op
Iteration   2: (Initial List Size 1) - 240.531 ns/op
Iteration   3: (Initial List Size 1) - 238.751 ns/op
Iteration   4: (Initial List Size 1) - 239.559 ns/op
Iteration   5: (Initial List Size 1) - 238.721 ns/op
Iteration   6: (Initial List Size 1) - 238.942 ns/op
Iteration   7: (Initial List Size 1) - 386.827 ns/op
Iteration   8: (Initial List Size 1) - 386.569 ns/op
Iteration   9: (Initial List Size 1) - 386.516 ns/op
Iteration  10: (Initial List Size 1) - 386.304 ns/op
     */
    @Benchmark
    public List<String> m04_large_small()
    {
        result.clear();
        final int size = src.length();

        for (int pos = 0; pos < size; pos++)
        {
            char c = src.charAt(pos);

            if (c == ',')
            {
                result.add(src);
            }
        }

        return result;
    }
}
