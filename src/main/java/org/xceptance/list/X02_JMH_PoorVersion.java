package org.xceptance.list;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.xceptance.common.util.SimpleArrayList;

public class X02_JMH_PoorVersion
{
    int iterationCount;
    String src;

    List<String> result;

    final String LONG = "R,CandleDaySalesPage.2,1666954266805,95,false,1349,429,200,https://production-test.justacmecompany.com/on/dishwasher.store/Sites-justacmecompany-Site/en_US/__Analytics-Start?url=https%3A%2F%2Fproduction-test.justacmecompany.com%2Fs%2Fjustacmecompany%2Fc%2Fhome-smellstuff%2Fworkhelp4life&res=1600x1200&cookie=1&cmpn=&java=0&gears=0&fla=0&ag=0&dir=0&pct=0&pdf=0&qt=0&realp=0&tz=US%2FEastern&wma=1&pcat=new-arrivals&title=3-Wick+Scented+Candles+-+Swim+%26+Swamp+Tier&dwac=0.7629667259452815&r=2905563956785988054&ref=https%3A%2F%2Fproduction-test.justacmecompany.com%2F&data=givemesomedatathatjustfillshere,image/gif,0,0,95,0,95,95,,GET,,,0,,";

    public void setup(boolean isWarmup)
    {
        src = new String(LONG.toCharArray());

        iterationCount++;

        int size = 0;

        size = isWarmup ? 50 : 1;

        System.out.printf("(Initial List Size %d) - ", size);
        result= new SimpleArrayList<>(size);
    }

    public List<String> m04_large_small()
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

    public void measure(long iteration, boolean isWarmup, long iterationRuntime) throws InterruptedException, BrokenBarrierException
    {
        int count = 0;
        var flag = new Flag();
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final Thread t = new Thread(new Timer(barrier, flag, iterationRuntime));

        System.out.format("%s Iteration %3d - ",isWarmup ? "WarmUp" : "Measurement", iteration);

        var x = new X02_JMH_PoorVersion();
        x.setup(isWarmup);

        t.start();
        barrier.await();

        long s = System.nanoTime();
        while (!flag.done)
        {
            x.m04_large_small();
            count++;
        }
        long e = System.nanoTime();

        double result = (e - s) / count;
        System.out.format(": %10.3f%n", result);
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException
    {
        int warmupCount = 3;
        int iterationCount = 10;
        long iterationRuntime_MS = 2 * 1000;

        var x = new X02_JMH_PoorVersion();

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
