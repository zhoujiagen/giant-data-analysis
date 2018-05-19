package com.spike.giantdataanalysis.flink.example.model;

import java.util.Random;
import java.util.UUID;

public final class Randoms {

	public static boolean BOOL() {
		Random rnd = new Random(ID().hashCode());
		return rnd.nextBoolean();
	}

	public static String ID() {
		return UUID.randomUUID().toString().replaceAll("\\-", "");
	}

	public static long LONG() {
		Random rnd = new Random(ID().hashCode());
		return rnd.nextLong();
	}

	/**
	 * @return [0.0d, 1.0d)
	 */
	public static double DOUBLE() {
		Random rnd = new Random(ID().hashCode());
		return rnd.nextDouble();
	}

	/**
	 * @return [0.0d, max)
	 */
	public static double DOUBLE(double max) {
		Random rnd = new Random(ID().hashCode());
		double result = rnd.nextDouble() * max;
		return result;
	}

	/**
	 * @return [0.0f, 1.0f)
	 */
	public static float FLOAT() {
		Random rnd = new Random(ID().hashCode());
		return rnd.nextFloat();
	}

	/**
	 * @return [0.0f, max)
	 */
	public static float FLOAT(float max) {
		Random rnd = new Random(ID().hashCode());
		return rnd.nextFloat();
	}

	public static int INT() {
		Random rnd = new Random(ID().hashCode());
		return rnd.nextInt();
	}

	/**
	 * @return [0, max)
	 */
	public static int INT(int max) {
		Random rnd = new Random(ID().hashCode());
		return rnd.nextInt(max);
	}

	/**
	 * @return 例50.2
	 */
	public static float PRECENT() {
		Random rnd = new Random(ID().hashCode());
		float result = rnd.nextFloat() * 101f;
		if (result > 100f)
			result = 100f;
		return result;
	}

	/**
	 * @return [0.0f, max)
	 */
	public static float PERCENT(float max) {
		Random rnd = new Random(ID().hashCode());
		return rnd.nextFloat() * max;
	}
}
