package com.spike.giantdataanalysis.spark.example.rdd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class LinkInfo implements Writable, Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> urls;
	private ArrayWritable urlsArrayWritable;

	public LinkInfo() {
		urlsArrayWritable = new ArrayWritable(Text.class);
	}

	public LinkInfo(List<String> urls) {
		this.urls = urls;
		urlsArrayWritable = new ArrayWritable(Text.class);
		if (urls != null) {
			Text[] texts = new Text[urls.size()];
			for (int i = 0, len = urls.size(); i < len; i++) {
				texts[i] = new Text(urls.get(i));
			}
			urlsArrayWritable.set(texts);
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		urlsArrayWritable.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		urlsArrayWritable.write(out);
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
		urlsArrayWritable = new ArrayWritable(Text.class);
		if (urls != null) {
			Text[] texts = new Text[urls.size()];
			for (int i = 0, len = urls.size(); i < len; i++) {
				texts[i] = new Text(urls.get(i));
			}
			urlsArrayWritable.set(texts);
		}
	}

}
