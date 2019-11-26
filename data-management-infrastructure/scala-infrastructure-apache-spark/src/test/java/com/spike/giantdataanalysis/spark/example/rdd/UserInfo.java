package com.spike.giantdataanalysis.spark.example.rdd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class UserInfo implements Writable, Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private IntWritable idWritable;
	private List<String> subscribeTopics;
	private ArrayWritable subscribeTopicsArrayWritable;

	public UserInfo() {
		idWritable = new IntWritable();
		subscribeTopicsArrayWritable = new ArrayWritable(Text.class);
	}

	public UserInfo(Integer id, List<String> subscribeTopics) {
		this.id = id;
		this.subscribeTopics = subscribeTopics;

		idWritable = new IntWritable(id);
		subscribeTopicsArrayWritable = new ArrayWritable(Text.class);
		if (subscribeTopics != null) {
			Text[] texts = new Text[subscribeTopics.size()];
			for (int i = 0, len = subscribeTopics.size(); i < len; i++) {
				texts[i] = new Text(subscribeTopics.get(i));
			}
			subscribeTopicsArrayWritable.set(texts);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		idWritable = new IntWritable(id);
	}

	public List<String> getSubscribeTopics() {
		return subscribeTopics;
	}

	public void setSubscribeTopics(List<String> subscribeTopics) {
		this.subscribeTopics = subscribeTopics;
		subscribeTopicsArrayWritable = new ArrayWritable(Text.class);
		if (subscribeTopics != null) {
			Text[] texts = new Text[subscribeTopics.size()];
			for (int i = 0, len = subscribeTopics.size(); i < len; i++) {
				texts[i] = new Text(subscribeTopics.get(i));
			}
			subscribeTopicsArrayWritable.set(texts);
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		idWritable.readFields(in);
		subscribeTopicsArrayWritable.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		idWritable.write(out);
		subscribeTopicsArrayWritable.write(out);
	}

}
