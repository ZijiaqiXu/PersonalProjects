package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class InitReducer extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		// key: node id; values: its adjacency lists
		
		// set key: nodeId+1(initial rank)
		key.set(key.toString() + ":" + "1");
		
		// write key: node+rank; value: adjacency list
		for (Text text: values) {
			context.write(key, text);
		}

	}
}
