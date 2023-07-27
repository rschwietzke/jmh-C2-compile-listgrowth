# JIT Compile Oddity for a Simple Lists

## The Problem

As part of XLT, we crunch a lot of data and that requires to hunt for all possible optimizations. Standard lists seem to be a little heavy due to their versatility and a plan array is hard to deal with, because the final size of it is not known.

While benchmarking, we found that our simple list implementation is tremendously faster and almost (at least for JDK 17) as fast as a plain array. So far so good. When we started to investigate an oddity regarding C2 training patterns, we stumbled upon another interesting observation.

When we oversize our list, so the backing array never has to grow, it is about 150% faster (just a simple `add`) than when we undersize it and it has to grow. This would make sense when we always have to grow the array, but in our test, the array has to grow rarely. In addition, the compiler creates fast code initially but over time decides to go with the slower version.

It is interesting that the JDK `ArrayList` does not show that issue but rather is always "slow".

## Summary
Because you might just say TL;DR now, here is a quick summary:

We can get about 200 ns runtime when we right size the list (about 5 million executions per second). If we present the compiler and list that must grow once every 2 seconds and only in the beginning, we get about 530 ns runtime (about 1.7 million executions per second only).

A standard ArrayList's runtime is always on the slower path of 530 ns independent of being right sized or not. 

## Ideas

When playing around and applying certain parameters

## The Code



## Measurements and Results




