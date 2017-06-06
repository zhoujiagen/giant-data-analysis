package com.spike.giantdataanalysis.opentsdb.example;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.opentsdb.core.TSDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.opentsdb.supports.Metrics;
import com.spike.giantdataanalysis.opentsdb.supports.OpenTSDBs;
import com.spike.giantdataanalysis.opentsdb.supports.OpenTSDBs.AbstractMetricPoint;

public class OpenTSDBBenchmark {

    private static final Logger LOG = LoggerFactory.getLogger(OpenTSDBBenchmark.class);

    public static final int DEFAULT_BATCH_SIZE = 1000;

    public static void main(String[] args) {

        AbstractMetricPoint point = Metrics.RND_METRICPOINT();
        System.out.println(point);


        try {
            int workerSize = 100;

            if (args.length != 2) {
                LOG.error("\nArguments: <Worker Size> <Config File Path>");
                return;
            }

            workerSize = Integer.valueOf(args[0]);
            LOG.info("Worker Size: {}", String.valueOf(workerSize));
            String configFile = args[1]; // example: src/main/resources/opentsdb.conf
            Preconditions.checkState(new File(configFile).exists(), "Config file doesnot exist.");

            ExecutorService es = Executors.newFixedThreadPool(workerSize);

            for (int i = 0; i < workerSize; i++) {
                es.submit(new Emitter((i + 1), DEFAULT_BATCH_SIZE, configFile));
            }

            System.in.read();// MOCK BLOCKING

        } catch (Exception e) {
            LOG.error("OpenTSDBBenchmark fail", e);
        }
    }

    public static class Emitter implements Runnable {

        public int index;
        public int batchSize = DEFAULT_BATCH_SIZE;
        public String configFile;

        public Emitter(int index, int batchSize, String configFile) {
            this.index = index;
            this.batchSize = batchSize;
            this.configFile = configFile;
        }

        @Override
        public void run() {
            TSDB tsdb = OpenTSDBs.CLIENT(configFile);

            while (true) {
                try {
                    List<AbstractMetricPoint> points = Lists.newArrayList();
                    for (int i = 0; i < batchSize; i++) {
                        AbstractMetricPoint point = Metrics.RND_METRICPOINT();
                        points.add(point);
                    }
                    OpenTSDBs.SYNC_ADDPOINT(tsdb, points);
                } catch (Exception e) {
                    LOG.error("emit failed", e);
                    break;
                } finally {
                    OpenTSDBs.SHUTDOWN(tsdb);
                }

            }
        }
    }


}
