# JIT Compiler Challenge - Growing List

## The Problem

As part of XLT, we crunch a lot of data and that requires to hunt for all possible optimizations. Standard lists seem to be a little heavy due to their versatility and a plain array is hard to deal with, because the final size it is not known upfront.

While benchmarking, we found that our simple list implementation is tremendously faster and almost (at least for JDK 17) close to a plain array. So far so good. When we started to investigate an oddity regarding [C2 training patterns](https://github.com/rschwietzke/jmh-C2-compile), we stumbled upon another interesting observation.

When we oversize our SimpleArrayList, so the backing array never has to grow, it is about twice as fast (just using a simple `add`) as when we undersize it and it has to grow. This would make sense when we always have to grow the array, but in our test, the array has to grow rarely. We setup the list before each iteration, we gives us one setup and about 2-5 million executions. The compiler creates fast code initially but over time decides to go with the slower version.

It is interesting that the JDK `ArrayList` does not show that issue but rather is always "slow".

While investigating all possible reasons, we finally arrived at the conclusion that the benchmark harness might play tricks with us here. But that cannot be proved nor disproved it. The only thing we know is, that almost independent of the warmup time (if not too short), the 7th or 8th measurement iterations sees a runtime increase by 100% (C02 test case). Even when we run longer or shorter measurement iterations, it is almost most certainly number 7 or 8 that starts to see the increased runtime.

```
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
```

### Runtimes

![C02 Standard Runtime](/assets/c02-table-standard.png)

The outlier test case is marked in yellow.

Here you can see the varying runtimes of the testcase dependent on the initial data presented to the test code (warmup) and the later data during measurement. This is in parts similar to [Hotspot Compile Issue for While-Loops](https://github.com/rschwietzke/jmh-C2-compile). In contrast to that test setup, we see a late change of the compiled code here which is not explainable. It is also not sure when and how this is triggered.

* 1) When we train our list with a small list and later present a small list again, we see 520 ns runtime
* 2) When we train with large and present large later, we see 260 ns runtime
* 3) When we train with small and go to large, we get a runtime of 520 ns
* 4) When we train with large and go small, we get for a while 260 ns but suddenly the runtime change later to 560 ns.

We never get an optimized runtime for 3) despite that 2) should be possible but the opposite happens (see 4).

If we warmup way longer, this measurement does not change. If we run longer measurement iterations, the change still happens at about the same iteration count.

Small means, the list has to grow from 1 element to about 40 to satisfy our space requirements, but the once grown list will be reused a millions times before we restart. Large means, we come with slightly oversized list and never need any size changes.

## Summary
Because you might just say TL;DR now, here is a quick summary:

We can get about 260 ns runtime when we right size the list (about 5 million executions per second). If we present the compiler a list that must grow from 1 to 50 (by doubling every time) every 2 seconds and only in the beginning of an benchmark iteration, we get about 560 ns runtime (about 1.7 million executions per second).

When digging into the compiled code and running experiments, we found out that this is likely related to loop unrolling and inlining. When the inline code has to change due to the list growth and is not longer inlineable and we also have to stop unrolling the loop, because the triggers (direct array access) are not longer there.

A standard ArrayList's runtime is always on the slower path of 530 ns independent of being right sized or not.

We might not want to call it a bug, because there are good reasons for all of that but we seem to leave quite some performance potential untapped. Overall, this is a rather common programming pattern I guess, so maybe it is good for an investigation why we discard the fast code despite the relative rare list growth occurrences.

![Measurement Data](/assets/c02-detailed-list.png)

Note: While writing this README and evaluating all the data again, by either measuring or varying, we arrived at the conclusion that something within JMH triggers a recompile due to a code path change and that causes the entire measurement method to be recompiled. Which gives us the slower runtime when the benchmark runs longer. Never the opposite, by the way.

When we add `-XX:-TieredCompilation`, the issues disappears which might suggest that a simple C2 compile run (no additional data from level 3 profiling added) is the best strategy.

## The Questions

Why do we give up our fast code after a while? Why so late and why at all? Each iteration is about 4 million invocations and only 10 of these see the list grow. The list growth itself occurs about 10 x 5 times = 50 (1, 4, 10, 22, 46). This feels like a heavily penalty.

Why is a no tiered-compilation resulting in almost perfect code that stays in place? What JMH code is triggering the later recompile when running with classic JDK JIT settings?

## The Code

### Base Test Case
Our test case in question is `org.xceptance.list.C02_SimpleArrayListGrow`. All other C0X test cases are deviations from that  to verify or test out theories.

#### Setup
During setup, we check what stage we are in (warmup or measurement) and what test case we run. Depending on that, we define the size of the list and allocate it. Later on, we will reuse the list until the iteration is over. `iterationCount` will help us to make the call between warmup and measurement (not sure if there is any functionality in JMH for that).

```java
int iterationCount;
String src;

List<String> result;

final String LONG = "R,CandleDaySalesPage.2,1666954266805,95,false,1349,429,200,https://production-test.justacmecompany.com/on/dishwasher.store/Sites-justacmecompany-Site/en_US/__Analytics-Start?url=https%3A%2F%2Fproduction-test.justacmecompany.com%2Fs%2Fjustacmecompany%2Fc%2Fhome-smellstuff%2Fworkhelp4life&res=1600x1200&cookie=1&cmpn=&java=0&gears=0&fla=0&ag=0&dir=0&pct=0&pdf=0&qt=0&realp=0&tz=US%2FEastern&wma=1&pcat=new-arrivals&title=7-Bloc+Abcdef+Dnadsel+-+Swim+%26+Swamp+Tier&dwac=0.7629667259452815&r=2905563956785988054&ref=https%3A%2F%2Fproduction-test.justacmecompany.com%2F&data=givemesomedatathatjustfillshere,image/gif,0,0,95,0,95,95,,GET,,,0,,";


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
```

#### Test Code

The measurement code consists of a loop that checks on a delimiter character and if this matches, adds the source to the a list. The real code, this is coming from, is a parser for CSV-like data, where we have to crunch millions of lines of data points deliver in CSV form. Each data line varies in length and delimiter count. Yes, there is a max amount of delimiter occurrences and that is what we use at the moment, preallocate more space and later reuse the array/list.

```java
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
            result.add(src);
        }
    }

    return result;
}
```
In addition to our list example, we always include a more simple iteration (m00) and counting version that shows us how much time the pure iteration and comparison of characters needs. It sets the expectation, kind of.

### Variations

* C01: C02 using an Java ArrayList instead of the handwritten list
* C02: Our original test case
* C03: C01 but not using the List interface for variable declaration
* C04: C02 but not using the List interface for variable declaration
* C05: C04, but pulled out the check and growth into its own method
* C06: Simialr to C05, but always uses a local reference that is populated using a `getData` call that, if needed, growth the backing array
* C07: "Mini list" implementation directly in the test code, not other class referenced
* C08: Uses a version of SimpleArrayList which does not use Arrays.copy to growth the list, rather a plain simple loop to reduce the inlineable code size
* C09: Reads from an array of char instead from String to avoid inlining even more code
* C10: Manually unrolled loop
* C11: C10 but also using the no intrinsics version of the SimpleArrayList (replace Arrays.copyOf by a naive loop)

None of the variations, but the `ArrayList`, shows a different runtime behavior. The `ArrayList` version does not attempt to optimize the code as much as SimpleArrayList, hence we don't see any difference at all.

## Measurements and Results

All measurements in the code are just examples to illustrate the base measurements. These have been taken using a Google Cloud c2, 8 core, 32 GB memory machine.

```
# JMH version: 1.36
# VM version: JDK 17.0.8, OpenJDK 64-Bit Server VM, 17.0.8+7
# VM invoker: /home/r_schwietzke/.sdkman/candidates/java/17.0.8-tem/bin/java
# VM options: -Xms2g -Xmx2g -XX:+AlwaysPreTouch -XX:+UseSerialGC`
```

* Each iteration is 2 sec long
* 5 warmup rounds
* 10 measurement rounds to see if the compiler makes any late changes
* Setup before each iteration(!), important: this is different from other JMH tests where you setup before the entire benchmark just once, this is not as wasteful as a setup before each invocation (see [setup per invocation](https://github.com/openjdk/jmh/blob/master/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_38_PerInvokeSetup.java))

```
mvn clean verify; java -Xms2g -Xmx2g -XX:+AlwaysPreTouch -XX:+UseSerialGC  -jar target/benchmarks.jar
```

When testing our theories, we used `-jvmArgsAppends` to keep the original parameters and add to them.

All data points can be found here: [Measurement Data in a shared Google spreadsheet](https://docs.google.com/spreadsheets/d/1t9zJeR9zS7q5mlkZNjLktGVgFCBbk6zurzzIy4Rers8/edit?usp=sharing). Tab "List".

## Ideas
To validate our measurements and try out theories, we used the following OpenJDK parameters to change the JIT behavior. The ideas for that have been derived from a manual inspection of the generated machine code for the fast path.

The inspection reveled that the JIT unrolls the loop and inlines some parts of the list. Both behaviors are expected but when viewed closer, the unrolling happens only because of the inlining as it seems (just guessed by me).

The extra test runs have been only executed the C02 test case. You find all data in the sheet: [Measurement Data in a shared Google spreadsheet](https://docs.google.com/spreadsheets/d/1t9zJeR9zS7q5mlkZNjLktGVgFCBbk6zurzzIy4Rers8/edit?usp=sharing). The header of the table carries the parameters and some plain naming.

### No Loop Unrolling

When we prevent loop unrolling by setting `-XX:LoopUnrollLimit=0`, we see, as expected, that the plain m00 loop runtime increases from 200 ns to 350 ns. So, stopping unrolling works.

The overall runtime behavior of the remaining methods stays the same, only the difference of our Large_Small test case between the fast and the slow path changed.

Conclusion: So, loop unrolling is not why the code behaves this way.

### Inlining

Because we saw that code is inlined, let's stop inlining of the `add` method code: `-XX:CompileCommand=dontinline,com/xceptance/common/util/SimpleArrayList.add`.

Our m00 runtime does not change, which is expected, because we don't use `add` there. All the other methods runs at the same speed now and the max runtime is about just 30 ns slower. All previously "fast" methods are now slow and behave like the previously "slow" ones.

Conclusion: Inlining makes a difference but the runtime difference is small. The runtime difference for unrolling was high. So let's assume that the speed advantage comes from unrolling but the runtime behavior change is connected to inlining.

### No inlining and no unrolling

Based on our previous two measurements, let's combine both.

We see m00 behave as expected without unrolling. In addition, there is no other runtime difference to our no inlining version, hence the inlining seems to allow unrolling in the first place. No inlining, no unrolling.

### Manual unrolling

When the JIT does not inline, it cannot directly access/see the list's backing array, hence it also does not unroll the loop.
So, let's unroll the loop manually with test case C10.

Measurements are faster for manually unrolled loops (that is a surprise) but we still see our runtime change. When we disable loop unrolling by the JIT now, we don't see any measurement difference, which proves the manual unroll success. Still, the runtime change occurs.

So, it is not the unrolling at the end of the day that causes the change, rather just some recompile of the method which also occurs when we manually unroll. So it is likely the inlining again.

### Disabled TieredCompilation

When we disable tiered compilation the code runs as expected. There is not runtime change anymore for our Large_Small method and it is always running fast. For everything trained with list growth, we are on the slow path, all trained with a not growing list, we are on the fast path.

## Misc

### JDK 21 Warmup
Please see also a related report which follows up on a finding related to comparing JDK 17 and JDK 21. Soon to be published.

### JDK 21
The overall runtimes and behavior are the same, except for the issue above.

### GraalVM 17.0.8-ce
This version of Graal is always slower than the OpenJDK. It also does not optimize the fast path at all. Even the plain m00 example is slower.

### GraalVM 17.0.8
The former Enterprise version of Graal is significantly faster then its CE counterpart and OpenJDK. Amazingly, it even pushed the limits of m00 even further.

But it also bails out of the fast path similarly to OpenJDK.