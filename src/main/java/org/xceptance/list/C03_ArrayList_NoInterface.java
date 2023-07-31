package org.xceptance.list;

import java.util.ArrayList;
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
 * Arraylist in a loop with just and add, but reused many times. Different initial sizes
 * for warmup and measurement for a few test cases.
 * Using Arraylist type for declaration, NOT the interface.
 *
    Benchmark                                               Mode  Cnt    Score   Error  Units
    C03_ArrayListGrow_NoInterface.m00_basicReadPerformance  avgt   10  199.413 ± 0.412  ns/op
    C03_ArrayListGrow_NoInterface.m01_small_small           avgt   10  519.559 ± 1.539  ns/op
    C03_ArrayListGrow_NoInterface.m02_large_large           avgt   10  521.699 ± 2.253  ns/op
    C03_ArrayListGrow_NoInterface.m03_small_large           avgt   10  522.134 ± 5.223  ns/op
    C03_ArrayListGrow_NoInterface.m04_large_small           avgt   10  519.343 ± 0.347  ns/op
 *
 *  * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)

public class C03_ArrayList_NoInterface
{
    int iterationCount;
    String src;

    ArrayList<String> result;

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
        result= new ArrayList<>(size);
    }

    /*
# Warmup Iteration   1: (Initial List Size 0) - 206.750 ns/op
# Warmup Iteration   2: (Initial List Size 0) - 205.732 ns/op
# Warmup Iteration   3: (Initial List Size 0) - 199.365 ns/op
# Warmup Iteration   4: (Initial List Size 0) - 199.323 ns/op
# Warmup Iteration   5: (Initial List Size 0) - 199.361 ns/op
Iteration   1: (Initial List Size 0) - 199.360 ns/op
Iteration   2: (Initial List Size 0) - 199.336 ns/op
Iteration   3: (Initial List Size 0) - 200.171 ns/op
Iteration   4: (Initial List Size 0) - 199.262 ns/op
Iteration   5: (Initial List Size 0) - 199.264 ns/op
Iteration   6: (Initial List Size 0) - 199.342 ns/op
Iteration   7: (Initial List Size 0) - 199.440 ns/op
Iteration   8: (Initial List Size 0) - 199.374 ns/op
Iteration   9: (Initial List Size 0) - 199.315 ns/op
Iteration  10: (Initial List Size 0) - 199.260 ns/op
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
# Warmup Iteration   1: (Initial List Size 1) - 563.215 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 559.229 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 519.250 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 519.329 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 519.442 ns/op
Iteration   1: (Initial List Size 1) - 519.678 ns/op
Iteration   2: (Initial List Size 1) - 522.360 ns/op
Iteration   3: (Initial List Size 1) - 519.269 ns/op
Iteration   4: (Initial List Size 1) - 518.827 ns/op
Iteration   5: (Initial List Size 1) - 518.962 ns/op
Iteration   6: (Initial List Size 1) - 518.991 ns/op
Iteration   7: (Initial List Size 1) - 519.323 ns/op
Iteration   8: (Initial List Size 1) - 519.548 ns/op
Iteration   9: (Initial List Size 1) - 519.284 ns/op
Iteration  10: (Initial List Size 1) - 519.348 ns/op
     */
    @Benchmark
    public ArrayList<String> m01_small_small()
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
# Warmup Iteration   1: (Initial List Size 50) - 568.392 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 560.483 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 522.114 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 527.316 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 524.971 ns/op
Iteration   1: (Initial List Size 50) - 520.768 ns/op
Iteration   2: (Initial List Size 50) - 521.676 ns/op
Iteration   3: (Initial List Size 50) - 520.788 ns/op
Iteration   4: (Initial List Size 50) - 521.203 ns/op
Iteration   5: (Initial List Size 50) - 521.720 ns/op
Iteration   6: (Initial List Size 50) - 524.656 ns/op
Iteration   7: (Initial List Size 50) - 520.967 ns/op
Iteration   8: (Initial List Size 50) - 520.309 ns/op
Iteration   9: (Initial List Size 50) - 524.139 ns/op
Iteration  10: (Initial List Size 50) - 520.766 ns/op
     */
    @Benchmark
    public ArrayList<String> m02_large_large()
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
# Warmup Iteration   1: (Initial List Size 1) - 562.908 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 559.218 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 521.247 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 520.486 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 519.128 ns/op
Iteration   1: (Initial List Size 50) - 520.230 ns/op
Iteration   2: (Initial List Size 50) - 521.022 ns/op
Iteration   3: (Initial List Size 50) - 531.615 ns/op
Iteration   4: (Initial List Size 50) - 522.357 ns/op
Iteration   5: (Initial List Size 50) - 522.662 ns/op
Iteration   6: (Initial List Size 50) - 520.759 ns/op
Iteration   7: (Initial List Size 50) - 519.919 ns/op
Iteration   8: (Initial List Size 50) - 521.605 ns/op
Iteration   9: (Initial List Size 50) - 520.093 ns/op
Iteration  10: (Initial List Size 50) - 521.080 ns/op
     */
    @Benchmark
    public ArrayList<String> m03_small_large()
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
# Warmup Iteration   1: (Initial List Size 50) - 568.204 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 560.202 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 519.032 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 518.366 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 519.465 ns/op
Iteration   1: (Initial List Size 1) - 519.484 ns/op
Iteration   2: (Initial List Size 1) - 519.002 ns/op
Iteration   3: (Initial List Size 1) - 519.480 ns/op
Iteration   4: (Initial List Size 1) - 518.901 ns/op
Iteration   5: (Initial List Size 1) - 519.529 ns/op
Iteration   6: (Initial List Size 1) - 519.276 ns/op
Iteration   7: (Initial List Size 1) - 519.438 ns/op
Iteration   8: (Initial List Size 1) - 519.340 ns/op
Iteration   9: (Initial List Size 1) - 519.361 ns/op
Iteration  10: (Initial List Size 1) - 519.616 ns/op
     */
    @Benchmark
    public ArrayList<String> m04_large_small()
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
