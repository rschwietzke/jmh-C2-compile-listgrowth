package org.xceptance.list;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

import com.xceptance.common.util.SimpleArrayList;

public class X03_JMH_PoorVersion_charArray
{
    int iterationCount;
    char[] src;

    String[] result;

    final String LONG = "R,CandleDaySalesPage.2,1666954266805,95,false,1349,429,200,https://production-test.justacmecompany.com/on/dishwasher.store/Sites-justacmecompany-Site/en_US/__Analytics-Start?url=https%3A%2F%2Fproduction-test.justacmecompany.com%2Fs%2Fjustacmecompany%2Fc%2Fhome-smellstuff%2Fworkhelp4life&res=1600x1200&cookie=1&cmpn=&java=0&gears=0&fla=0&ag=0&dir=0&pct=0&pdf=0&qt=0&realp=0&tz=US%2FEastern&wma=1&pcat=new-arrivals&title=3-Wick+Scented+Candles+-+Swim+%26+Swamp+Tier&dwac=0.7629667259452815&r=2905563956785988054&ref=https%3A%2F%2Fproduction-test.justacmecompany.com%2F&data=givemesomedatathatjustfillshere,image/gif,0,0,95,0,95,95,,GET,,,0,,";

    final AtomicLong start = new AtomicLong();
    final AtomicLong end = new AtomicLong();

    public void setup(boolean isWarmup)
    {
        src = LONG.toCharArray();

        iterationCount++;

        int size = 0;

        size = isWarmup ? 50 : 1;

        System.out.printf("(Initial List Size %d) - ", size);
        result = new String[size];
    }

    public String[] m04_large_small()
    {
        final int size = src.length;

        int pos = 0;
        int targetPos = 0;
        while (pos < size)
        {
            final char c = src[pos];
            if (c == ',')
            {
                if (targetPos == result.length)
                {
                    result = Arrays.copyOf(result, (result.length << 1) + 2);
                }
                result[targetPos++] = LONG;
            }
            pos++;
        }

        return result;
    }

    public double measureLoop(X03_JMH_PoorVersion_charArray x, Flag flag)
    {
        int count = 0;

        // atomic to prevent reordering aka the cpu should not look ahead
        start.set(System.nanoTime());

        while (!flag.done)
        {
            x.m04_large_small();
            count++;
        }

        // atomic to prevent reordering aka the cpu should not look ahead
        end.set(System.nanoTime());

        final double runtime = (double)(end.get() - start.get()) / (double)count;
        return runtime;
    }

    public void measure(long iteration, boolean isWarmup, long iterationRuntime) throws InterruptedException, BrokenBarrierException
    {
        final Flag flag = new Flag();
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final Thread t = new Thread(new Timer(barrier, flag, iterationRuntime));

        System.out.format("%s Iteration %3d - ",isWarmup ? "WarmUp" : "Measurement", iteration);

        var x = new X03_JMH_PoorVersion_charArray();
        x.setup(isWarmup);

        t.start();
        barrier.await();

        // measure loop
        final double runtime = measureLoop(x, flag);
        System.out.format(": %10.3f%n", runtime);

    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException
    {
        int warmupCount = 3;
        int iterationCount = 10;
        long iterationRuntime_MS = 2 * 1000;

        var x = new X03_JMH_PoorVersion_charArray();

        // warmup
        for (long i = 1; i <= warmupCount; i++)
        {
            x.measure(i, true, iterationRuntime_MS);
        }
        // measure
        for (long i = 1; i <= iterationCount; i++)
        {
            x.measure(i, false, iterationRuntime_MS);
        }
    }

    private class Flag
    {
        public volatile boolean done = false;
    }

    class Timer implements Runnable
    {
        final CyclicBarrier barrier;
        final Flag flag;
        final long waitingTime;

        public Timer(CyclicBarrier barrier, Flag flag, long waitingTime)
        {
            this.barrier = barrier;
            this.flag = flag;
            this.waitingTime = waitingTime;
        }

        public void run()
        {
            try
            {
                barrier.await();
                Thread.sleep(waitingTime);
                flag.done = true;
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (BrokenBarrierException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
