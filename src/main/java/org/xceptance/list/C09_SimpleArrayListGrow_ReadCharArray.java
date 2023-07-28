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
 * SimpleList Performance over time
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
    public void setup(BenchmarkParams params) throws InterruptedException
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
}
