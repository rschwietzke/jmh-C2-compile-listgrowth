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
 * C02 in a version where we unrolled the loop manually and we also don't use
 * any Arrays.copy of in our code. This is using a plain loop copy instead.
 *
 * Warning: Inline measurement values are not from the Google Cloud machine as seen in C01 to C09!
 *
Benchmark                                                               Mode  Cnt    Score     Error  Units
C11_SimpleArrayList_NoIntrinsics_ManualUnroll.m00_basicReadPerformance  avgt   10  190.172 ±   2.580  ns/op
C11_SimpleArrayList_NoIntrinsics_ManualUnroll.m01_small_small           avgt   10  651.390 ±  18.863  ns/op
C11_SimpleArrayList_NoIntrinsics_ManualUnroll.m02_large_large           avgt   10  205.419 ±   9.178  ns/op
C11_SimpleArrayList_NoIntrinsics_ManualUnroll.m03_small_large           avgt   10  615.684 ±   8.812  ns/op
C11_SimpleArrayList_NoIntrinsics_ManualUnroll.m04_large_small           avgt   10  355.455 ± 347.044  ns/op

 * @author Rene Schwietzke <r.schwietzke@xceptance.com>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class C11_SimpleArrayList_NoIntrinsics_ManualUnroll
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
# Warmup Iteration   1: (Initial List Size 0) - 227.237 ns/op
# Warmup Iteration   2: (Initial List Size 0) - 226.634 ns/op
# Warmup Iteration   3: (Initial List Size 0) - 194.210 ns/op
# Warmup Iteration   4: (Initial List Size 0) - 189.367 ns/op
# Warmup Iteration   5: (Initial List Size 0) - 187.985 ns/op
Iteration   1: (Initial List Size 0) - 192.995 ns/op
Iteration   2: (Initial List Size 0) - 189.628 ns/op
Iteration   3: (Initial List Size 0) - 192.771 ns/op
Iteration   4: (Initial List Size 0) - 188.358 ns/op
Iteration   5: (Initial List Size 0) - 189.649 ns/op
Iteration   6: (Initial List Size 0) - 191.003 ns/op
Iteration   7: (Initial List Size 0) - 187.753 ns/op
Iteration   8: (Initial List Size 0) - 190.228 ns/op
Iteration   9: (Initial List Size 0) - 190.202 ns/op
Iteration  10: (Initial List Size 0) - 189.131 ns/op

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
# Warmup Iteration   1: (Initial List Size 1) - 223.159 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 355.786 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 646.240 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 640.232 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 644.565 ns/op
Iteration   1: (Initial List Size 1) - 643.204 ns/op
Iteration   2: (Initial List Size 1) - 679.551 ns/op
Iteration   3: (Initial List Size 1) - 662.917 ns/op
Iteration   4: (Initial List Size 1) - 651.753 ns/op
Iteration   5: (Initial List Size 1) - 654.341 ns/op
Iteration   6: (Initial List Size 1) - 653.585 ns/op
Iteration   7: (Initial List Size 1) - 641.130 ns/op
Iteration   8: (Initial List Size 1) - 648.076 ns/op
Iteration   9: (Initial List Size 1) - 638.344 ns/op
Iteration  10: (Initial List Size 1) - 641.000 ns/op

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
# Warmup Iteration   1: (Initial List Size 50) - 204.188 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 176.011 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 199.562 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 208.591 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 219.432 ns/op
Iteration   1: (Initial List Size 50) - 205.040 ns/op
Iteration   2: (Initial List Size 50) - 216.537 ns/op
Iteration   3: (Initial List Size 50) - 195.168 ns/op
Iteration   4: (Initial List Size 50) - 203.693 ns/op
Iteration   5: (Initial List Size 50) - 202.206 ns/op
Iteration   6: (Initial List Size 50) - 203.818 ns/op
Iteration   7: (Initial List Size 50) - 210.692 ns/op
Iteration   8: (Initial List Size 50) - 206.399 ns/op
Iteration   9: (Initial List Size 50) - 210.711 ns/op
Iteration  10: (Initial List Size 50) - 199.923 ns/op

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
# Warmup Iteration   1: (Initial List Size 1) - 204.551 ns/op
# Warmup Iteration   2: (Initial List Size 1) - 347.311 ns/op
# Warmup Iteration   3: (Initial List Size 1) - 656.063 ns/op
# Warmup Iteration   4: (Initial List Size 1) - 623.640 ns/op
# Warmup Iteration   5: (Initial List Size 1) - 636.381 ns/op
Iteration   1: (Initial List Size 50) - 618.308 ns/op
Iteration   2: (Initial List Size 50) - 612.823 ns/op
Iteration   3: (Initial List Size 50) - 614.378 ns/op
Iteration   4: (Initial List Size 50) - 612.570 ns/op
Iteration   5: (Initial List Size 50) - 613.406 ns/op
Iteration   6: (Initial List Size 50) - 611.770 ns/op
Iteration   7: (Initial List Size 50) - 614.254 ns/op
Iteration   8: (Initial List Size 50) - 616.721 ns/op
Iteration   9: (Initial List Size 50) - 611.499 ns/op
Iteration  10: (Initial List Size 50) - 631.115 ns/op

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
# Warmup Iteration   1: (Initial List Size 50) - 197.934 ns/op
# Warmup Iteration   2: (Initial List Size 50) - 177.334 ns/op
# Warmup Iteration   3: (Initial List Size 50) - 196.415 ns/op
# Warmup Iteration   4: (Initial List Size 50) - 223.578 ns/op
# Warmup Iteration   5: (Initial List Size 50) - 200.649 ns/op
Iteration   1: (Initial List Size 1) - 178.723 ns/op
Iteration   2: (Initial List Size 1) - 187.862 ns/op
Iteration   3: (Initial List Size 1) - 175.002 ns/op
Iteration   4: (Initial List Size 1) - 178.057 ns/op
Iteration   5: (Initial List Size 1) - 173.140 ns/op
Iteration   6: (Initial List Size 1) - 173.638 ns/op
Iteration   7: (Initial List Size 1) - 616.051 ns/op
Iteration   8: (Initial List Size 1) - 617.964 ns/op
Iteration   9: (Initial List Size 1) - 637.685 ns/op
Iteration  10: (Initial List Size 1) - 616.425 ns/op

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
