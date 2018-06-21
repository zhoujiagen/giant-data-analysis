package com.spike.giantdataanalysis.flink.example.model;

import com.spike.giantdataanalysis.flink.example.model.PerformanceIndex.OperationSystem.CPU;
import com.spike.giantdataanalysis.flink.example.model.PerformanceIndex.OperationSystem.Disk;
import com.spike.giantdataanalysis.flink.example.model.PerformanceIndex.OperationSystem.DiskIO;
import com.spike.giantdataanalysis.flink.example.model.PerformanceIndex.OperationSystem.Kernel;
import com.spike.giantdataanalysis.flink.example.model.PerformanceIndex.OperationSystem.Network;
import com.spike.giantdataanalysis.flink.example.model.PerformanceIndex.OperationSystem.Process;
import com.spike.giantdataanalysis.flink.example.model.PerformanceIndex.OperationSystem.Sys;

public class Metrics {
	/**
	 * <pre>
	 * 展示metric
	 * 
	 * CPU
	 * Disk
	 * DiskIO
	 * Network
	 * Kernel
	 * Sys
	 * Process
	 * </pre>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		for (CPU e : CPU.values()) {
			System.out.println("tsdb mkmetric " + e.getMetric());
		}
		for (Disk e : Disk.values()) {
			System.out.println("tsdb mkmetric " + e.getMetric());
		}
		for (DiskIO e : DiskIO.values()) {
			System.out.println("tsdb mkmetric " + e.getMetric());
		}
		for (Network e : Network.values()) {
			System.out.println("tsdb mkmetric " + e.getMetric());
		}
		for (Kernel e : Kernel.values()) {
			System.out.println("tsdb mkmetric " + e.getMetric());
		}
		for (Sys e : Sys.values()) {
			System.out.println("tsdb mkmetric " + e.getMetric());
		}
		for (Process e : Process.values()) {
			System.out.println("tsdb mkmetric " + e.getMetric());
		}
	}

	public static enum MetricType {
		INT, PERCENT, DOUBLE
	}

	public static final String TAG_DATACENTER = "dc";
	public static final String[] TAG_DATACENTER_VALUES = //
			new String[] { "dc001", "dc002", "dc003", "dc004" };

	public static final String TAG_MACHINE_GROUP = "mg";
	public static String[] TAG_MACHINE_GROUP_VALUES = //
			new String[] { "mg0001", "mg0002", "mg0003", "mg0004", "mg0005", //
					"mg0006", "mg0007", "mg0008", "mg009", "mg0010" };

	public static final String TAG_MACHINE = "m";
	public static final String[] TAG_MACHINE_VALUES = //
			new String[] { "m00001", "m00002", "m00003", "m00004", "m00005", //
					"m00006", "m00007", "m00008", "m00009", "m00010", //
					"m00011", "m00012", "m00013", "m00014", "m00015", //
					"m00016", "m00017", "m00018", "m00019", "m00020" };

	public static double DEFAULT_MAX_DOUBLE = 1000d;
	public static int DEFAULT_MAX_INT = 1000;
	public static float DEFAULT_MAX_FLOAT = 1000f;

	public static int METRIC_SIZE() {
		return 7;
	}

}
