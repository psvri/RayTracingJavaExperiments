package vectorbench;

import org.openjdk.jmh.annotations.*;
import vector.Vec;

import java.util.concurrent.TimeUnit;

public class MyBenchmark {
    @State(Scope.Benchmark)
    public static class VecState {
        public Vec v1 = new Vec();
        public Vec v2 = new Vec(1, 2, 3);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Vec addVec(VecState input) {
        return input.v1.add(input.v2);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Vec mulVec(VecState input) {
        return input.v1.mul(input.v2);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Vec createVec() {
        return new Vec();
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public double accessVec(VecState input) {
        return input.v2.y();
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public double lengthVec(VecState input) {
        return input.v2.length();
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public double dotVec(VecState input) {
        return input.v2.dot(input.v2);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Vec reflectVec(VecState input) {
        return input.v2.reflect(new Vec(0, 1, 0));
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Vec refractVec(VecState input) {
        return input.v2.refract(new Vec(0, 1, 0), 1.40);
    }
}
