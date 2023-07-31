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
 * Same as C4 BUT uses another list add method that has the size check and growth
 * pull out of the add method.

    Benchmark                                                      Mode  Cnt    Score     Error  Units
    C05_SimpleArrayListGrow_CheckPullOut.m00_basicReadPerformance  avgt   10  199.727 ±   0.934  ns/op
    C05_SimpleArrayListGrow_CheckPullOut.m01_small_small           avgt   10  565.942 ±   0.802  ns/op
    C05_SimpleArrayListGrow_CheckPullOut.m02_large_large           avgt   10  204.461 ±   0.878  ns/op
    C05_SimpleArrayListGrow_CheckPullOut.m03_small_large           avgt   10  567.556 ±   2.758  ns/op
    C05_SimpleArrayListGrow_CheckPullOut.m04_large_small           avgt   10  386.832 ± 234.884  ns/op
 *
 * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class C05_SimpleArrayList_CheckPullOut
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
# Warmup Iteration   1: (Initial List Size 0) - 206.843 ns/op
# Warmup Iteration   2: (Initial List Size 0) - 205.985 ns/op
# Warmup Iteration   3: (Initial List Size 0) - 199.465 ns/op
# Warmup Iteration   4: (Initial List Size 0) - 199.491 ns/op
# Warmup Iteration   5: (Initial List Size 0) - 199.496 ns/op
Iteration   1: (Initial List Size 0) - 201.416 ns/op
Iteration   2: (Initial List Size 0) - 199.405 ns/op
Iteration   3: (Initial List Size 0) - 199.492 ns/op
Iteration   4: (Initial List Size 0) - 199.543 ns/op
Iteration   5: (Initial List Size 0) - 199.504 ns/op
Iteration   6: (Initial List Size 0) - 199.440 ns/op
Iteration   7: (Initial List Size 0) - 199.552 ns/op
Iteration   8: (Initial List Size 0) - 199.475 ns/op
Iteration   9: (Initial List Size 0) - 200.008 ns/op
Iteration  10: (Initial List Size 0) - 199.437 ns/op
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
# Warmup Iteration   1: (Initial List Size 1) - 201.495 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 638.834 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 566.006 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 565.523 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 565.592 ns/op
Iteration   1: (Initial List Size 1) - 565.764 ns/op
Iteration   2: (Initial List Size 1) - 566.270 ns/op
Iteration   3: (Initial List Size 1) - 565.270 ns/op
Iteration   4: (Initial List Size 1) - 566.175 ns/op
Iteration   5: (Initial List Size 1) - 566.532 ns/op
Iteration   6: (Initial List Size 1) - 565.855 ns/op
Iteration   7: (Initial List Size 1) - 565.963 ns/op
Iteration   8: (Initial List Size 1) - 564.982 ns/op
Iteration   9: (Initial List Size 1) - 566.720 ns/op
Iteration  10: (Initial List Size 1) - 565.886 ns/op
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
                result.add2(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 200.495 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 264.930 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 204.116 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 204.065 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 204.595 ns/op
Iteration   1: (Initial List Size 50) - 204.093 ns/op
Iteration   2: (Initial List Size 50) - 204.601 ns/op
Iteration   3: (Initial List Size 50) - 204.554 ns/op
Iteration   4: (Initial List Size 50) - 205.999 ns/op
Iteration   5: (Initial List Size 50) - 204.192 ns/op
Iteration   6: (Initial List Size 50) - 204.196 ns/op
Iteration   7: (Initial List Size 50) - 204.456 ns/op
Iteration   8: (Initial List Size 50) - 204.444 ns/op
Iteration   9: (Initial List Size 50) - 204.095 ns/op
Iteration  10: (Initial List Size 50) - 203.981 ns/op
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
                result.add2(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 1) - 200.292 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 638.916 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 566.220 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 568.456 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 572.988 ns/op
Iteration   1: (Initial List Size 50) - 566.671 ns/op
Iteration   2: (Initial List Size 50) - 566.763 ns/op
Iteration   3: (Initial List Size 50) - 566.963 ns/op
Iteration   4: (Initial List Size 50) - 571.390 ns/op
Iteration   5: (Initial List Size 50) - 567.795 ns/op
Iteration   6: (Initial List Size 50) - 568.210 ns/op
Iteration   7: (Initial List Size 50) - 565.409 ns/op
Iteration   8: (Initial List Size 50) - 567.116 ns/op
Iteration   9: (Initial List Size 50) - 569.646 ns/op
Iteration  10: (Initial List Size 50) - 565.599 ns/op
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
                result.add2(src);
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial List Size 50) - 200.474 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 265.329 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 204.628 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 204.266 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 204.244 ns/op
Iteration   1: (Initial List Size 1) - 266.033 ns/op
Iteration   2: (Initial List Size 1) - 267.153 ns/op
Iteration   3: (Initial List Size 1) - 266.731 ns/op
Iteration   4: (Initial List Size 1) - 266.395 ns/op
Iteration   5: (Initial List Size 1) - 266.425 ns/op
Iteration   6: (Initial List Size 1) - 266.210 ns/op
Iteration   7: (Initial List Size 1) - 569.167 ns/op
Iteration   8: (Initial List Size 1) - 567.081 ns/op
Iteration   9: (Initial List Size 1) - 566.861 ns/op
Iteration  10: (Initial List Size 1) - 566.261 ns/op
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
                result.add2(src);
            }
        }

        return result;
    }

}
