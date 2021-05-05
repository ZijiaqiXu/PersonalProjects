package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IterReducer extends Reducer<Text, Text, Text, Text> {
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double d = PageRankDriver.DECAY; // Decay factor

		double rank = 0d;
		
		String t;
		
		// initialize adjList with random String
		Text adjList = new Text("E");
		// input values: node-weight pairs (one special node+adjList pair)
		for (Text text: values) {
			t = text.toString();
			// check the beginning mark
			if (t.charAt(0) == 'L') adjList.set(t.substring(1));// if this node has adjList, set the adjList
			else rank += Double.parseDouble(t);
		}
		
		// decaying
		rank = 1 - d + d*rank;
		
		String nodeRank = key.toString() + ":" + Double.toString(rank);
		
		// if this node has adjList, emit key: node+rank; value: adjList
		if (adjList.toString() != "E") context.write(new Text(nodeRank), adjList);
	}
}
