package com.spike.giantdataanalysis.flink.example.model.streamingledger2;

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.util.SplittableRandom;

import static org.apache.flink.util.Preconditions.checkArgument;

/**
 * A simple random data generator with data rate throttling logic.
 */
public abstract class BaseGenerator<T> extends RichParallelSourceFunction<T> {

    private static final long serialVersionUID = 1L;

    private final int maxRecordsPerSecond;

    private volatile boolean running = true;

    BaseGenerator(int maxRecordsPerSecond) {
        checkArgument(maxRecordsPerSecond == -1 || maxRecordsPerSecond > 0,
                "maxRecordsPerSecond must be positive or -1 (infinite)");
        this.maxRecordsPerSecond = maxRecordsPerSecond;
    }

    @Override
    public final void run(SourceContext<T> ctx) throws Exception {
        final int numberOfParallelSubtasks = getRuntimeContext().getNumberOfParallelSubtasks();
        final Throttler throttler = new Throttler(maxRecordsPerSecond, numberOfParallelSubtasks);
        final SplittableRandom rnd = new SplittableRandom();

        while (running) {
            T event = randomEvent(rnd);
            ctx.collect(event);
            throttler.throttle();
        }
    }

    @Override
    public final void cancel() {
        running = false;
    }

    abstract T randomEvent(SplittableRandom rnd);

}