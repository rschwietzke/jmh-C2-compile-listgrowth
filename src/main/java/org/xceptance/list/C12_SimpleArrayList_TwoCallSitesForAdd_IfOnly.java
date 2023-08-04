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
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.xceptance.common.util.SimpleArrayList;

/**
 * SimpleArraylist in a loop with just and add, but reused many times. Different initial sizes
 * for warmup and measurement for a few test cases.
 * Using List interface for declaration.
 *
    Benchmark                                         Mode  Cnt    Score     Error  Units
    C02_SimpleArrayListGrow.m00_basicReadPerformance  avgt   10  199.506 ±   0.099  ns/op
    C02_SimpleArrayListGrow.m01_small_small           avgt   10  557.397 ±   0.495  ns/op
    C02_SimpleArrayListGrow.m02_large_large           avgt   10  259.252 ±   2.051  ns/op
    C02_SimpleArrayListGrow.m03_small_large           avgt   10  557.713 ±   1.001  ns/op
    C02_SimpleArrayListGrow.m04_large_small           avgt   10  334.477 ± 232.203  ns/op

 * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class C12_SimpleArrayList_TwoCallSitesForAdd_IfOnly
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
# Warmup Iteration   1: (Initial List Size 0) - 206.759 ns/op
# Warmup Iteration   2: (Initial List Size 0) - 205.518 ns/op
# Warmup Iteration   3: (Initial List Size 0) - 199.692 ns/op
# Warmup Iteration   4: (Initial List Size 0) - 199.682 ns/op
# Warmup Iteration   5: (Initial List Size 0) - 199.558 ns/op
Iteration   1: (Initial List Size 0) - 199.626 ns/op
Iteration   2: (Initial List Size 0) - 199.450 ns/op
Iteration   3: (Initial List Size 0) - 199.470 ns/op
Iteration   4: (Initial List Size 0) - 199.505 ns/op
Iteration   5: (Initial List Size 0) - 199.597 ns/op
Iteration   6: (Initial List Size 0) - 199.487 ns/op
Iteration   7: (Initial List Size 0) - 199.460 ns/op
Iteration   8: (Initial List Size 0) - 199.424 ns/op
Iteration   9: (Initial List Size 0) - 199.490 ns/op
Iteration  10: (Initial List Size 0) - 199.550 ns/op
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
# Warmup Iteration   1: (Initial List Size 1) - 225.128 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 638.616 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 557.413 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 557.444 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 557.462 ns/op
Iteration   1: (Initial List Size 1) - 556.773 ns/op
Iteration   2: (Initial List Size 1) - 557.259 ns/op
Iteration   3: (Initial List Size 1) - 557.499 ns/op
Iteration   4: (Initial List Size 1) - 557.603 ns/op
Iteration   5: (Initial List Size 1) - 557.341 ns/op
Iteration   6: (Initial List Size 1) - 557.844 ns/op
Iteration   7: (Initial List Size 1) - 557.414 ns/op
Iteration   8: (Initial List Size 1) - 556.974 ns/op
Iteration   9: (Initial List Size 1) - 557.699 ns/op
Iteration  10: (Initial List Size 1) - 557.564 ns/op
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
                if (result.reachedCapacity())
                {
                    result.add(src);
                }
                else
                {
                    result.addUnsafe(src);
                }
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 224.906 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 239.963 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 258.801 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 259.088 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 258.821 ns/op
Iteration   1: (Initial List Size 50) - 258.868 ns/op
Iteration   2: (Initial List Size 50) - 258.874 ns/op
Iteration   3: (Initial List Size 50) - 258.883 ns/op
Iteration   4: (Initial List Size 50) - 263.107 ns/op
Iteration   5: (Initial List Size 50) - 258.865 ns/op
Iteration   6: (Initial List Size 50) - 258.713 ns/op
Iteration   7: (Initial List Size 50) - 258.683 ns/op
Iteration   8: (Initial List Size 50) - 258.842 ns/op
Iteration   9: (Initial List Size 50) - 258.808 ns/op
Iteration  10: (Initial List Size 50) - 258.875 ns/op
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
                if (result.reachedCapacity())
                {
                    result.add(src);
                }
                else
                {
                    result.addUnsafe(src);
                }            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 1) - 225.056 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 637.712 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 556.971 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 557.009 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 557.212 ns/op
Iteration   1: (Initial List Size 50) - 558.009 ns/op
Iteration   2: (Initial List Size 50) - 557.375 ns/op
Iteration   3: (Initial List Size 50) - 558.144 ns/op
Iteration   4: (Initial List Size 50) - 557.397 ns/op
Iteration   5: (Initial List Size 50) - 559.328 ns/op
Iteration   6: (Initial List Size 50) - 557.475 ns/op
Iteration   7: (Initial List Size 50) - 557.283 ns/op
Iteration   8: (Initial List Size 50) - 557.768 ns/op
Iteration   9: (Initial List Size 50) - 557.286 ns/op
Iteration  10: (Initial List Size 50) - 557.063 ns/op
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
                if (result.reachedCapacity())
                {
                    result.add(src);
                }
                else
                {
                    result.addUnsafe(src);
                }
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 224.897 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 239.547 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 259.248 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 258.979 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 258.816 ns/op
Iteration   1: (Initial List Size 1) - 238.703 ns/op
Iteration   2: (Initial List Size 1) - 239.335 ns/op
Iteration   3: (Initial List Size 1) - 239.469 ns/op
Iteration   4: (Initial List Size 1) - 238.714 ns/op
Iteration   5: (Initial List Size 1) - 238.863 ns/op
Iteration   6: (Initial List Size 1) - 239.850 ns/op
Iteration   7: (Initial List Size 1) - 238.696 ns/op
Iteration   8: (Initial List Size 1) - 557.010 ns/op
Iteration   9: (Initial List Size 1) - 556.943 ns/op
Iteration  10: (Initial List Size 1) - 557.185 ns/op
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
                if (result.reachedCapacity())
                {
                    result.increaseCapacity();
                    result.addUnsafe(src);
                }
                else
                {
                    result.addUnsafe(src);
                }
            }
        }

        return result;
    }

    /**
     * This is just here for debugging purposes. Don't use that to run the benchmark
     * for measurement purposes.
     */
    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder()
                .include("")
                .forks(0)
                .build();

        new Runner(opt).run();
    }
}
