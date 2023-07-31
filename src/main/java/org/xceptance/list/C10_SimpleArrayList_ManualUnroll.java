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
 * C02 in a version where we unrolled the loop manually.
 *
 * The measurements in this source file are not comparable to C09 and lower due to the use of a different machine. I
 * destroyed my cloud measurement machine and did not want to set it up again for just a few lines. This is a Lenovo T14s laptop
 * which is a little shakier in regards to stable runtimes.
Benchmark                                                  Mode  Cnt    Score     Error  Units
C10_SimpleArrayList_ManualUnroll.m00_basicReadPerformance  avgt   10  185.758 ±   3.518  ns/op
C10_SimpleArrayList_ManualUnroll.m01_small_small           avgt   10  432.788 ±   7.987  ns/op
C10_SimpleArrayList_ManualUnroll.m02_large_large           avgt   10  202.107 ±  17.017  ns/op
C10_SimpleArrayList_ManualUnroll.m03_small_large           avgt   10  443.477 ±  12.650  ns/op
C10_SimpleArrayList_ManualUnroll.m04_large_small           avgt   10  286.323 ± 202.915  ns/op

 * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class C10_SimpleArrayList_ManualUnroll
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
        result= new SimpleArrayList<>(size);
    }

    /*
# Warmup Iteration   1: (Initial List Size 0) - 219.206 ns/op
# Warmup Iteration   2: (Initial List Size 0) - 222.024 ns/op
# Warmup Iteration   3: (Initial List Size 0) - 187.067 ns/op
# Warmup Iteration   4: (Initial List Size 0) - 186.294 ns/op
# Warmup Iteration   5: (Initial List Size 0) - 184.020 ns/op
Iteration   1: (Initial List Size 0) - 182.325 ns/op
Iteration   2: (Initial List Size 0) - 182.654 ns/op
Iteration   3: (Initial List Size 0) - 185.035 ns/op
Iteration   4: (Initial List Size 0) - 186.210 ns/op
Iteration   5: (Initial List Size 0) - 183.516 ns/op
Iteration   6: (Initial List Size 0) - 185.877 ns/op
Iteration   7: (Initial List Size 0) - 188.136 ns/op
Iteration   8: (Initial List Size 0) - 188.411 ns/op
Iteration   9: (Initial List Size 0) - 186.923 ns/op
Iteration  10: (Initial List Size 0) - 188.495 ns/op

     */
    @Benchmark
    public int m00_basicReadPerformance()
    {
        final int size = src.length();
        int count = 0;

        int pos = 0;
        for (; pos < size; pos += 4)
        {
            char c1 = src.charAt(pos);
            if (c1 == ',')
            {
                count++;
            }
            char c2 = src.charAt(pos + 1);
            if (c2 == ',')
            {
                count++;
            }
            char c3 = src.charAt(pos + 2);
            if (c3 == ',')
            {
                count++;
            }
            char c4 = src.charAt(pos + 3);
            if (c4 == ',')
            {
                count++;
            }
        }
        for (; pos < size; pos++)
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
# Warmup Iteration   1: (Initial List Size 1) - 193.294 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 737.901 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 417.969 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 449.211 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 437.554 ns/op
Iteration   1: (Initial List Size 1) - 438.007 ns/op
Iteration   2: (Initial List Size 1) - 440.768 ns/op
Iteration   3: (Initial List Size 1) - 430.509 ns/op
Iteration   4: (Initial List Size 1) - 426.990 ns/op
Iteration   5: (Initial List Size 1) - 433.147 ns/op
Iteration   6: (Initial List Size 1) - 428.177 ns/op
Iteration   7: (Initial List Size 1) - 434.386 ns/op
Iteration   8: (Initial List Size 1) - 439.114 ns/op
Iteration   9: (Initial List Size 1) - 431.464 ns/op
Iteration  10: (Initial List Size 1) - 425.321 ns/op

     */
    @Benchmark
    public List<String> m01_small_small()
    {
        result.clear();
        final int size = src.length();

        int pos = 0;
        for (; pos < size; pos += 4)
        {
            char c = src.charAt(pos);
            if (c == ',')
            {
                result.add(src);
            }
            char c2 = src.charAt(pos + 1);
            if (c2 == ',')
            {
                result.add(src);
            }
            char c3 = src.charAt(pos + 2);
            if (c3 == ',')
            {
                result.add(src);
            }
            char c4 = src.charAt(pos + 3);
            if (c4 == ',')
            {
                result.add(src);
            }
        }
        for (; pos < size; pos++)
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
# Warmup Iteration   1: (Initial List Size 50) - 189.486 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 170.938 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 194.358 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 218.294 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 229.033 ns/op
Iteration   1: (Initial List Size 50) - 217.615 ns/op
Iteration   2: (Initial List Size 50) - 201.633 ns/op
Iteration   3: (Initial List Size 50) - 198.239 ns/op
Iteration   4: (Initial List Size 50) - 196.400 ns/op
Iteration   5: (Initial List Size 50) - 192.906 ns/op
Iteration   6: (Initial List Size 50) - 209.570 ns/op
Iteration   7: (Initial List Size 50) - 195.608 ns/op
Iteration   8: (Initial List Size 50) - 224.069 ns/op
Iteration   9: (Initial List Size 50) - 193.771 ns/op
Iteration  10: (Initial List Size 50) - 191.262 ns/op

     */
    @Benchmark
    public List<String> m02_large_large()
    {
        result.clear();
        final int size = src.length();

        int pos = 0;
        for (; pos < size; pos += 4)
        {
            char c = src.charAt(pos);
            if (c == ',')
            {
                result.add(src);
            }
            char c2 = src.charAt(pos + 1);
            if (c2 == ',')
            {
                result.add(src);
            }
            char c3 = src.charAt(pos + 2);
            if (c3 == ',')
            {
                result.add(src);
            }
            char c4 = src.charAt(pos + 3);
            if (c4 == ',')
            {
                result.add(src);
            }
        }
        for (; pos < size; pos++)
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
# Warmup Iteration   1: (Initial List Size 1) - 197.071 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 799.535 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 443.723 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 460.090 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 454.672 ns/op
Iteration   1: (Initial List Size 50) - 451.312 ns/op
Iteration   2: (Initial List Size 50) - 452.619 ns/op
Iteration   3: (Initial List Size 50) - 456.924 ns/op
Iteration   4: (Initial List Size 50) - 443.651 ns/op
Iteration   5: (Initial List Size 50) - 434.086 ns/op
Iteration   6: (Initial List Size 50) - 445.033 ns/op
Iteration   7: (Initial List Size 50) - 438.920 ns/op
Iteration   8: (Initial List Size 50) - 442.343 ns/op
Iteration   9: (Initial List Size 50) - 439.725 ns/op
Iteration  10: (Initial List Size 50) - 430.154 ns/op

     */
    @Benchmark
    public List<String> m03_small_large()
    {
        result.clear();
        final int size = src.length();

        int pos = 0;
        for (; pos < size; pos += 4)
        {
            char c = src.charAt(pos);
            if (c == ',')
            {
                result.add(src);
            }
            char c2 = src.charAt(pos + 1);
            if (c2 == ',')
            {
                result.add(src);
            }
            char c3 = src.charAt(pos + 2);
            if (c3 == ',')
            {
                result.add(src);
            }
            char c4 = src.charAt(pos + 3);
            if (c4 == ',')
            {
                result.add(src);
            }
        }
        for (; pos < size; pos++)
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
# Warmup Iteration   1: (Initial List Size 50) - 207.804 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 185.491 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 209.100 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 243.994 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 230.514 ns/op
Iteration   1: (Initial List Size 1) - 188.641 ns/op
Iteration   2: (Initial List Size 1) - 188.236 ns/op
Iteration   3: (Initial List Size 1) - 177.280 ns/op
Iteration   4: (Initial List Size 1) - 186.743 ns/op
Iteration   5: (Initial List Size 1) - 177.298 ns/op
Iteration   6: (Initial List Size 1) - 176.809 ns/op
Iteration   7: (Initial List Size 1) - 430.022 ns/op
Iteration   8: (Initial List Size 1) - 452.668 ns/op
Iteration   9: (Initial List Size 1) - 441.241 ns/op
Iteration  10: (Initial List Size 1) - 444.294 ns/op

     */
    @Benchmark
    public List<String> m04_large_small()
    {
        result.clear();
        final int size = src.length();

        int pos = 0;
        for (; pos < size; pos += 4)
        {
            char c = src.charAt(pos);
            if (c == ',')
            {
                result.add(src);
            }
            char c2 = src.charAt(pos + 1);
            if (c2 == ',')
            {
                result.add(src);
            }
            char c3 = src.charAt(pos + 2);
            if (c3 == ',')
            {
                result.add(src);
            }
            char c4 = src.charAt(pos + 3);
            if (c4 == ',')
            {
                result.add(src);
            }
        }
        for (; pos < size; pos++)
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
