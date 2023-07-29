package org.xceptance.list;

import java.util.Arrays;
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

/**
 * Instead of using a list, we manually working with an array and run all
 * check and growth routines directly in our code.
Benchmark                                           Mode  Cnt    Score     Error  Units
C07_HandcraftedInlineList.m00_basicReadPerformance  avgt   10  199.378 ±   0.084  ns/op
C07_HandcraftedInlineList.m01_small_small           avgt   10  488.589 ±   0.575  ns/op
C07_HandcraftedInlineList.m02_large_large           avgt   10  205.383 ±   0.224  ns/op
C07_HandcraftedInlineList.m03_small_large           avgt   10  492.823 ±   0.181  ns/op
C07_HandcraftedInlineList.m04_large_small           avgt   10  304.748 ± 240.291  ns/op

 * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class C07_HandcraftedInlineList
{
    int iterationCount;
    String src;

    String[] result;

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
        System.out.printf("(Initial Array Size %d) - ", size);
        result= new String[size];
    }

    /*
# Warmup Iteration   1: (Initial Array Size 0) - 206.529 ns/op
# Warmup Iteration   2: (Initial Array Size 0) - 205.555 ns/op
# Warmup Iteration   3: (Initial Array Size 0) - 199.447 ns/op
# Warmup Iteration   4: (Initial Array Size 0) - 199.382 ns/op
# Warmup Iteration   5: (Initial Array Size 0) - 199.317 ns/op
Iteration   1: (Initial Array Size 0) - 199.299 ns/op
Iteration   2: (Initial Array Size 0) - 199.407 ns/op
Iteration   3: (Initial Array Size 0) - 199.391 ns/op
Iteration   4: (Initial Array Size 0) - 199.296 ns/op
Iteration   5: (Initial Array Size 0) - 199.396 ns/op
Iteration   6: (Initial Array Size 0) - 199.473 ns/op
Iteration   7: (Initial Array Size 0) - 199.416 ns/op
Iteration   8: (Initial Array Size 0) - 199.374 ns/op
Iteration   9: (Initial Array Size 0) - 199.405 ns/op
Iteration  10: (Initial Array Size 0) - 199.328 ns/op
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
# Warmup Iteration   1: (Initial Array Size 1) - 201.021 ns/op
# Warmup Iteration   2: (Initial Array Size 1) - 637.441 ns/op
# Warmup Iteration   3: (Initial Array Size 1) - 488.746 ns/op
# Warmup Iteration   4: (Initial Array Size 1) - 492.181 ns/op
# Warmup Iteration   5: (Initial Array Size 1) - 488.077 ns/op
Iteration   1: (Initial Array Size 1) - 488.522 ns/op
Iteration   2: (Initial Array Size 1) - 488.337 ns/op
Iteration   3: (Initial Array Size 1) - 489.526 ns/op
Iteration   4: (Initial Array Size 1) - 488.655 ns/op
Iteration   5: (Initial Array Size 1) - 488.594 ns/op
Iteration   6: (Initial Array Size 1) - 488.522 ns/op
Iteration   7: (Initial Array Size 1) - 488.632 ns/op
Iteration   8: (Initial Array Size 1) - 488.661 ns/op
Iteration   9: (Initial Array Size 1) - 488.411 ns/op
Iteration  10: (Initial Array Size 1) - 488.032 ns/op
     */
    @Benchmark
    public String[] m01_small_small()
    {
        Arrays.fill(result, null);
        int lPos = 0;

        final int size = src.length();

        for (int pos = 0; pos < size; pos++)
        {
            char c = src.charAt(pos);

            if (c == ',')
            {
                if (lPos == result.length)
                {
                    result = Arrays.copyOf(result, result.length << 1);
                }
                result[lPos] = src;
                lPos++;
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial Array Size 50) - 206.898 ns/op
# Warmup Iteration   2: (Initial Array Size 50) - 188.229 ns/op
# Warmup Iteration   3: (Initial Array Size 50) - 204.373 ns/op
# Warmup Iteration   4: (Initial Array Size 50) - 205.303 ns/op
# Warmup Iteration   5: (Initial Array Size 50) - 205.411 ns/op
Iteration   1: (Initial Array Size 50) - 205.338 ns/op
Iteration   2: (Initial Array Size 50) - 205.346 ns/op
Iteration   3: (Initial Array Size 50) - 205.424 ns/op
Iteration   4: (Initial Array Size 50) - 205.385 ns/op
Iteration   5: (Initial Array Size 50) - 205.777 ns/op
Iteration   6: (Initial Array Size 50) - 205.330 ns/op
Iteration   7: (Initial Array Size 50) - 205.324 ns/op
Iteration   8: (Initial Array Size 50) - 205.379 ns/op
Iteration   9: (Initial Array Size 50) - 205.263 ns/op
Iteration  10: (Initial Array Size 50) - 205.259 ns/op
     */
    @Benchmark
    public String[] m02_large_large()
    {
        Arrays.fill(result, null);
        int lPos = 0;

        final int size = src.length();

        for (int pos = 0; pos < size; pos++)
        {
            char c = src.charAt(pos);

            if (c == ',')
            {
                if (lPos == result.length)
                {
                    result = Arrays.copyOf(result, result.length << 1);
                }
                result[lPos] = src;
                lPos++;
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial Array Size 1) - 200.619 ns/op
# Warmup Iteration   2: (Initial Array Size 1) - 637.491 ns/op
# Warmup Iteration   3: (Initial Array Size 1) - 488.673 ns/op
# Warmup Iteration   4: (Initial Array Size 1) - 492.478 ns/op
# Warmup Iteration   5: (Initial Array Size 1) - 490.442 ns/op
Iteration   1: (Initial Array Size 50) - 492.780 ns/op
Iteration   2: (Initial Array Size 50) - 492.730 ns/op
Iteration   3: (Initial Array Size 50) - 492.939 ns/op
Iteration   4: (Initial Array Size 50) - 492.839 ns/op
Iteration   5: (Initial Array Size 50) - 493.032 ns/op
Iteration   6: (Initial Array Size 50) - 492.917 ns/op
Iteration   7: (Initial Array Size 50) - 492.602 ns/op
Iteration   8: (Initial Array Size 50) - 492.764 ns/op
Iteration   9: (Initial Array Size 50) - 492.809 ns/op
Iteration  10: (Initial Array Size 50) - 492.817 ns/op
     */
    @Benchmark
    public String[] m03_small_large()
    {
        Arrays.fill(result, null);
        int lPos = 0;

        final int size = src.length();

        for (int pos = 0; pos < size; pos++)
        {
            char c = src.charAt(pos);

            if (c == ',')
            {
                if (lPos == result.length)
                {
                    result = Arrays.copyOf(result, result.length << 1);
                }
                result[lPos] = src;
                lPos++;
            }
        }

        return result;
    }

    /*
# Warmup Iteration   1: (Initial Array Size 50) - 207.654 ns/op
# Warmup Iteration   2: (Initial Array Size 50) - 187.650 ns/op
# Warmup Iteration   3: (Initial Array Size 50) - 204.522 ns/op
# Warmup Iteration   4: (Initial Array Size 50) - 205.385 ns/op
# Warmup Iteration   5: (Initial Array Size 50) - 205.309 ns/op
Iteration   1: (Initial Array Size 1) - 181.128 ns/op
Iteration   2: (Initial Array Size 1) - 182.858 ns/op
Iteration   3: (Initial Array Size 1) - 181.338 ns/op
Iteration   4: (Initial Array Size 1) - 181.972 ns/op
Iteration   5: (Initial Array Size 1) - 181.233 ns/op
Iteration   6: (Initial Array Size 1) - 181.288 ns/op
Iteration   7: (Initial Array Size 1) - 488.728 ns/op
Iteration   8: (Initial Array Size 1) - 490.443 ns/op
Iteration   9: (Initial Array Size 1) - 489.292 ns/op
Iteration  10: (Initial Array Size 1) - 489.199 ns/op
     */
    @Benchmark
    public String[] m04_large_small()
    {
        Arrays.fill(result, null);
        int lPos = 0;

        final int size = src.length();

        for (int pos = 0; pos < size; pos++)
        {
            char c = src.charAt(pos);

            if (c == ',')
            {
                if (lPos == result.length)
                {
                    result = Arrays.copyOf(result, result.length << 1);
                }
                result[lPos] = src;
                lPos++;
            }
        }

        return result;
    }
}
