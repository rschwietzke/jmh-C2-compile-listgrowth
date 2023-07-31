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
 * Same as C4 BUT uses another list add method that uses a get concept for the
 * backing array access including growth if needed.
 *
    Benchmark                                                           Mode  Cnt    Score     Error  Units
    C06_SimpleArrayListGrow_GetDataIntroduced.m00_basicReadPerformance  avgt   10  199.390 ±   0.083  ns/op
    C06_SimpleArrayListGrow_GetDataIntroduced.m01_small_small           avgt   10  567.171 ±   2.379  ns/op
    C06_SimpleArrayListGrow_GetDataIntroduced.m02_large_large           avgt   10  204.324 ±   0.772  ns/op
    C06_SimpleArrayListGrow_GetDataIntroduced.m03_small_large           avgt   10  567.334 ±   1.854  ns/op
    C06_SimpleArrayListGrow_GetDataIntroduced.m04_large_small           avgt   10  350.729 ± 225.458  ns/op

 * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class C06_SimpleArrayList_GetDataIntroduced
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
# Warmup Iteration   1: (Initial List Size 0) - 207.356 ns/op
# Warmup Iteration   2: (Initial List Size 0) - 206.477 ns/op
# Warmup Iteration   3: (Initial List Size 0) - 199.388 ns/op
# Warmup Iteration   4: (Initial List Size 0) - 199.392 ns/op
# Warmup Iteration   5: (Initial List Size 0) - 199.395 ns/op
Iteration   1: (Initial List Size 0) - 199.394 ns/op
Iteration   2: (Initial List Size 0) - 199.464 ns/op
Iteration   3: (Initial List Size 0) - 199.414 ns/op
Iteration   4: (Initial List Size 0) - 199.420 ns/op
Iteration   5: (Initial List Size 0) - 199.373 ns/op
Iteration   6: (Initial List Size 0) - 199.446 ns/op
Iteration   7: (Initial List Size 0) - 199.362 ns/op
Iteration   8: (Initial List Size 0) - 199.273 ns/op
Iteration   9: (Initial List Size 0) - 199.345 ns/op
Iteration  10: (Initial List Size 0) - 199.413 ns/op
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
# Warmup Iteration   1: (Initial List Size 1) - 200.212 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 638.103 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 566.804 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 566.012 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 565.487 ns/op
Iteration   1: (Initial List Size 1) - 567.223 ns/op
Iteration   2: (Initial List Size 1) - 566.075 ns/op
Iteration   3: (Initial List Size 1) - 567.665 ns/op
Iteration   4: (Initial List Size 1) - 565.231 ns/op
Iteration   5: (Initial List Size 1) - 567.777 ns/op
Iteration   6: (Initial List Size 1) - 570.022 ns/op
Iteration   7: (Initial List Size 1) - 567.592 ns/op
Iteration   8: (Initial List Size 1) - 565.278 ns/op
Iteration   9: (Initial List Size 1) - 568.953 ns/op
Iteration  10: (Initial List Size 1) - 565.890 ns/op
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
                result.add3(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 200.370 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 264.861 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 204.145 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 204.128 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 204.057 ns/op
Iteration   1: (Initial List Size 50) - 204.221 ns/op
Iteration   2: (Initial List Size 50) - 204.141 ns/op
Iteration   3: (Initial List Size 50) - 204.046 ns/op
Iteration   4: (Initial List Size 50) - 205.731 ns/op
Iteration   5: (Initial List Size 50) - 204.462 ns/op
Iteration   6: (Initial List Size 50) - 204.097 ns/op
Iteration   7: (Initial List Size 50) - 204.201 ns/op
Iteration   8: (Initial List Size 50) - 204.243 ns/op
Iteration   9: (Initial List Size 50) - 204.033 ns/op
Iteration  10: (Initial List Size 50) - 204.062 ns/op
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
                result.add3(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 1) - 200.349 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 637.643 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 566.878 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 565.467 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 565.224 ns/op
Iteration   1: (Initial List Size 50) - 566.990 ns/op
Iteration   2: (Initial List Size 50) - 566.969 ns/op
Iteration   3: (Initial List Size 50) - 568.259 ns/op
Iteration   4: (Initial List Size 50) - 565.252 ns/op
Iteration   5: (Initial List Size 50) - 569.170 ns/op
Iteration   6: (Initial List Size 50) - 566.177 ns/op
Iteration   7: (Initial List Size 50) - 567.420 ns/op
Iteration   8: (Initial List Size 50) - 568.646 ns/op
Iteration   9: (Initial List Size 50) - 566.324 ns/op
Iteration  10: (Initial List Size 50) - 568.132 ns/op
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
                result.add3(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 200.265 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 257.139 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 203.951 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 203.896 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 203.988 ns/op
Iteration   1: (Initial List Size 1) - 257.587 ns/op
Iteration   2: (Initial List Size 1) - 258.145 ns/op
Iteration   3: (Initial List Size 1) - 259.027 ns/op
Iteration   4: (Initial List Size 1) - 257.989 ns/op
Iteration   5: (Initial List Size 1) - 258.063 ns/op
Iteration   6: (Initial List Size 1) - 257.925 ns/op
Iteration   7: (Initial List Size 1) - 258.065 ns/op
Iteration   8: (Initial List Size 1) - 569.082 ns/op
Iteration   9: (Initial List Size 1) - 565.729 ns/op
Iteration  10: (Initial List Size 1) - 565.677 ns/op
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
                result.add3(src);
            }
        }

        return result;
    }

}
