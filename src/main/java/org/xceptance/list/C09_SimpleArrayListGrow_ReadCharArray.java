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

import com.xceptance.common.util.SimpleArrayList;

/**
 * Read from an array instead of String to avoid that we inline even more code.
 * This improves the runtime, but not the overall message such as that the JIT
 * goes still witht the slower solution once we see the backing list array grow.
 *
 * Benchmark                                                       Mode  Cnt    Score     Error  Units
 * C09_SimpleArrayListGrow_ReadCharArray.m00_basicReadPerformance  avgt   10  177.824 ±   1.349  ns/op
 * C09_SimpleArrayListGrow_ReadCharArray.m01_small_small           avgt   10  469.303 ±   0.722  ns/op
 * C09_SimpleArrayListGrow_ReadCharArray.m02_large_large           avgt   10  177.606 ±   0.661  ns/op
 * C09_SimpleArrayListGrow_ReadCharArray.m03_small_large           avgt   10  474.316 ±   3.022  ns/op
 * C09_SimpleArrayListGrow_ReadCharArray.m04_large_small           avgt   10  294.260 ± 228.055  ns/op
 *
 * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class C09_SimpleArrayListGrow_ReadCharArray
{
    int iterationCount;
    char[] src;

    List<char[]> result;

    final String LONG = "R,CandleDaySalesPage.2,1666954266805,95,false,1349,429,200,https://production-test.justacmecompany.com/on/dishwasher.store/Sites-justacmecompany-Site/en_US/__Analytics-Start?url=https%3A%2F%2Fproduction-test.justacmecompany.com%2Fs%2Fjustacmecompany%2Fc%2Fhome-smellstuff%2Fworkhelp4life&res=1600x1200&cookie=1&cmpn=&java=0&gears=0&fla=0&ag=0&dir=0&pct=0&pdf=0&qt=0&realp=0&tz=US%2FEastern&wma=1&pcat=new-arrivals&title=3-Wick+Scented+Candles+-+Swim+%26+Swamp+Tier&dwac=0.7629667259452815&r=2905563956785988054&ref=https%3A%2F%2Fproduction-test.justacmecompany.com%2F&data=givemesomedatathatjustfillshere,image/gif,0,0,95,0,95,95,,GET,,,0,,";

    @Setup(Level.Iteration)
    public void setup(BenchmarkParams params)
    {
        // ensure that we are not working on the same array all the time
        src = new String(LONG.toCharArray()).toCharArray();

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
        result= new SimpleArrayList<>(size);
    }

    /*
# Warmup Iteration   1: (Initial List Size 0) - 190.175 ns/op
# Warmup Iteration   2: (Initial List Size 0) - 187.865 ns/op
# Warmup Iteration   3: (Initial List Size 0) - 179.409 ns/op
# Warmup Iteration   4: (Initial List Size 0) - 179.200 ns/op
# Warmup Iteration   5: (Initial List Size 0) - 177.957 ns/op
Iteration   1: (Initial List Size 0) - 178.588 ns/op
Iteration   2: (Initial List Size 0) - 179.777 ns/op
Iteration   3: (Initial List Size 0) - 179.218 ns/op
Iteration   4: (Initial List Size 0) - 179.342 ns/op
Iteration   5: (Initial List Size 0) - 180.937 ns/op
Iteration   6: (Initial List Size 0) - 180.052 ns/op
Iteration   7: (Initial List Size 0) - 179.905 ns/op
Iteration   8: (Initial List Size 0) - 180.239 ns/op
Iteration   9: (Initial List Size 0) - 181.624 ns/op
Iteration  10: (Initial List Size 0) - 179.708 ns/op
     */
    @Benchmark
    public int m00_basicReadPerformance()
    {
        final int size = src.length;
        int count = 0;

        for (int pos = 0; pos < size; pos++)
        {
            char c = src[pos];

            if (c == ',')
            {
                count++;
            }
        }

        return count;
    }

    /*
# Warmup Iteration   1: (Initial List Size 1) - 179.400 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 458.898 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 468.652 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 468.300 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 469.583 ns/op
Iteration   1: (Initial List Size 1) - 468.111 ns/op
Iteration   2: (Initial List Size 1) - 468.550 ns/op
Iteration   3: (Initial List Size 1) - 468.814 ns/op
Iteration   4: (Initial List Size 1) - 468.607 ns/op
Iteration   5: (Initial List Size 1) - 469.020 ns/op
Iteration   6: (Initial List Size 1) - 468.044 ns/op
Iteration   7: (Initial List Size 1) - 469.197 ns/op
Iteration   8: (Initial List Size 1) - 469.589 ns/op
Iteration   9: (Initial List Size 1) - 468.896 ns/op
Iteration  10: (Initial List Size 1) - 468.312 ns/op
    */
    @Benchmark
    public List<char[]> m01_small_small()
    {
        result.clear();
        final int size = src.length;

        for (int pos = 0; pos < size; pos++)
        {
            char c = src[pos];

            if (c == ',')
            {
                result.add(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 179.449 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 176.923 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 176.992 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 177.690 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 177.108 ns/op
Iteration   1: (Initial List Size 50) - 177.235 ns/op
Iteration   2: (Initial List Size 50) - 177.044 ns/op
Iteration   3: (Initial List Size 50) - 177.157 ns/op
Iteration   4: (Initial List Size 50) - 177.255 ns/op
Iteration   5: (Initial List Size 50) - 177.000 ns/op
Iteration   6: (Initial List Size 50) - 177.077 ns/op
Iteration   7: (Initial List Size 50) - 177.156 ns/op
Iteration   8: (Initial List Size 50) - 177.215 ns/op
Iteration   9: (Initial List Size 50) - 177.121 ns/op
Iteration  10: (Initial List Size 50) - 177.144 ns/op
    */
    @Benchmark
    public List<char[]> m02_large_large()
    {
        result.clear();
        final int size = src.length;

        for (int pos = 0; pos < size; pos++)
        {
            char c = src[pos];

            if (c == ',')
            {
                result.add(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 1) - 179.380 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 459.251 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 470.603 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 472.556 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 476.006 ns/op
Iteration   1: (Initial List Size 50) - 473.106 ns/op
Iteration   2: (Initial List Size 50) - 470.335 ns/op
Iteration   3: (Initial List Size 50) - 470.445 ns/op
Iteration   4: (Initial List Size 50) - 471.183 ns/op
Iteration   5: (Initial List Size 50) - 470.932 ns/op
Iteration   6: (Initial List Size 50) - 476.578 ns/op
Iteration   7: (Initial List Size 50) - 475.644 ns/op
Iteration   8: (Initial List Size 50) - 472.497 ns/op
Iteration   9: (Initial List Size 50) - 470.990 ns/op
Iteration  10: (Initial List Size 50) - 471.128 ns/op
    */
    @Benchmark
    public List<char[]> m03_small_large()
    {
        result.clear();
        final int size = src.length;

        for (int pos = 0; pos < size; pos++)
        {
            char c = src[pos];

            if (c == ',')
            {
                result.add(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 179.325 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 177.006 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 177.376 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 177.658 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 177.170 ns/op
Iteration   1: (Initial List Size 1) - 176.839 ns/op
Iteration   2: (Initial List Size 1) - 177.718 ns/op
Iteration   3: (Initial List Size 1) - 176.937 ns/op
Iteration   4: (Initial List Size 1) - 176.910 ns/op
Iteration   5: (Initial List Size 1) - 176.969 ns/op
Iteration   6: (Initial List Size 1) - 177.261 ns/op
Iteration   7: (Initial List Size 1) - 469.320 ns/op
Iteration   8: (Initial List Size 1) - 468.610 ns/op
Iteration   9: (Initial List Size 1) - 468.432 ns/op
Iteration  10: (Initial List Size 1) - 468.291 ns/op
    */
    @Benchmark
    public List<char[]> m04_large_small()
    {
        result.clear();
        final int size = src.length;

        for (int pos = 0; pos < size; pos++)
        {
            char c = src[pos];

            if (c == ',')
            {
                result.add(src);
            }
        }

        return result;
    }
}
