package org.xceptance.list;

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
 * SimpleArraylist in a loop with just and add, but reused many times. Different initial sizes
 * for warmup and measurement for a few test cases.
 * Using SimpleArraylist type for declaration, NOT the interface.
 *
    Benchmark                                                     Mode  Cnt    Score     Error  Units
    C04_SimpleArrayListGrow_NoInterface.m00_basicReadPerformance  avgt   10  199.454 ±   0.415  ns/op
    C04_SimpleArrayListGrow_NoInterface.m01_small_small           avgt   10  591.424 ±   0.762  ns/op
    C04_SimpleArrayListGrow_NoInterface.m02_large_large           avgt   10  208.154 ±   1.273  ns/op
    C04_SimpleArrayListGrow_NoInterface.m03_small_large           avgt   10  591.200 ±   0.512  ns/op
    C04_SimpleArrayListGrow_NoInterface.m04_large_small           avgt   10  315.367 ± 288.144  ns/op
 *
 *  * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class C04_SimpleArrayList_NoInterface
{
    int iterationCount;
    String src;

    SimpleArrayList<String> result;

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
        result= new SimpleArrayList<>(size);
    }

    /*
# Warmup Iteration   1: (Initial List Size 0) - 206.702 ns/op
# Warmup Iteration   2: (Initial List Size 0) - 206.920 ns/op
# Warmup Iteration   3: (Initial List Size 0) - 199.338 ns/op
# Warmup Iteration   4: (Initial List Size 0) - 199.350 ns/op
# Warmup Iteration   5: (Initial List Size 0) - 200.122 ns/op
Iteration   1: (Initial List Size 0) - 199.359 ns/op
Iteration   2: (Initial List Size 0) - 199.443 ns/op
Iteration   3: (Initial List Size 0) - 199.400 ns/op
Iteration   4: (Initial List Size 0) - 199.348 ns/op
Iteration   5: (Initial List Size 0) - 199.425 ns/op
Iteration   6: (Initial List Size 0) - 199.329 ns/op
Iteration   7: (Initial List Size 0) - 199.369 ns/op
Iteration   8: (Initial List Size 0) - 199.413 ns/op
Iteration   9: (Initial List Size 0) - 200.218 ns/op
Iteration  10: (Initial List Size 0) - 199.238 ns/op
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
# Warmup Iteration   1: (Initial List Size 1) - 258.553 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 640.220 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 591.103 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 591.310 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 590.793 ns/op
Iteration   1: (Initial List Size 1) - 590.790 ns/op
Iteration   2: (Initial List Size 1) - 591.472 ns/op
Iteration   3: (Initial List Size 1) - 591.748 ns/op
Iteration   4: (Initial List Size 1) - 591.306 ns/op
Iteration   5: (Initial List Size 1) - 590.743 ns/op
Iteration   6: (Initial List Size 1) - 592.408 ns/op
Iteration   7: (Initial List Size 1) - 591.874 ns/op
Iteration   8: (Initial List Size 1) - 591.072 ns/op
Iteration   9: (Initial List Size 1) - 591.465 ns/op
Iteration  10: (Initial List Size 1) - 591.367 ns/op
     */
    @Benchmark
    public SimpleArrayList<String> m01_small_small()
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
# Warmup Iteration   1: (Initial List Size 50) - 258.547 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 196.530 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 207.844 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 207.676 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 207.740 ns/op
Iteration   1: (Initial List Size 50) - 207.794 ns/op
Iteration   2: (Initial List Size 50) - 207.836 ns/op
Iteration   3: (Initial List Size 50) - 207.998 ns/op
Iteration   4: (Initial List Size 50) - 210.509 ns/op
Iteration   5: (Initial List Size 50) - 207.938 ns/op
Iteration   6: (Initial List Size 50) - 207.676 ns/op
Iteration   7: (Initial List Size 50) - 207.849 ns/op
Iteration   8: (Initial List Size 50) - 207.868 ns/op
Iteration   9: (Initial List Size 50) - 207.810 ns/op
Iteration  10: (Initial List Size 50) - 208.259 ns/op
     */
    @Benchmark
    public SimpleArrayList<String> m02_large_large()
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
# Warmup Iteration   1: (Initial List Size 1) - 266.371 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 640.155 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 591.028 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 590.892 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 590.902 ns/op
Iteration   1: (Initial List Size 50) - 590.985 ns/op
Iteration   2: (Initial List Size 50) - 591.611 ns/op
Iteration   3: (Initial List Size 50) - 591.113 ns/op
Iteration   4: (Initial List Size 50) - 591.069 ns/op
Iteration   5: (Initial List Size 50) - 591.958 ns/op
Iteration   6: (Initial List Size 50) - 590.825 ns/op
Iteration   7: (Initial List Size 50) - 591.050 ns/op
Iteration   8: (Initial List Size 50) - 591.294 ns/op
Iteration   9: (Initial List Size 50) - 591.063 ns/op
Iteration  10: (Initial List Size 50) - 591.029 ns/op
     */
    @Benchmark
    public SimpleArrayList<String> m03_small_large()
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
# Warmup Iteration   1: (Initial List Size 50) - 265.976 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 196.510 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 207.938 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 207.883 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 207.859 ns/op
Iteration   1: (Initial List Size 1) - 196.379 ns/op
Iteration   2: (Initial List Size 1) - 196.795 ns/op
Iteration   3: (Initial List Size 1) - 196.880 ns/op
Iteration   4: (Initial List Size 1) - 196.748 ns/op
Iteration   5: (Initial List Size 1) - 198.308 ns/op
Iteration   6: (Initial List Size 1) - 196.779 ns/op
Iteration   7: (Initial List Size 1) - 197.109 ns/op
Iteration   8: (Initial List Size 1) - 592.236 ns/op
Iteration   9: (Initial List Size 1) - 591.512 ns/op
Iteration  10: (Initial List Size 1) - 590.920 ns/op
     */
    @Benchmark
    public SimpleArrayList<String> m04_large_small()
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
