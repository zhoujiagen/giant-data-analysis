package com.spike.giantdataanalysis.spark.example.rdd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class UserID implements Writable, Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private IntWritable idWritable;

	public UserID() {
		idWritable = new IntWritable();
	}

	public UserID(Integer id) {
		this.id = id;
		idWritable = new IntWritable(id);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		idWritable = new IntWritable(id);
		this.id = id;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		idWritable.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		idWritable.write(out);
	}
}
