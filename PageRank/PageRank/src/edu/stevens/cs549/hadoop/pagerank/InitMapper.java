package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class InitMapper extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException,
			IllegalArgumentException {
		String line = value.toString(); // Converts Line to a String
		
		// split the input value into two parts: 1. node id; 2. its adjacency list
		String[] output = line.split(": ");
		
		// output them as Text intermediate format
		// exclude nodes with no out-edge (empty adjList)
		if (output.length == 2) context.write(new Text(output[0]), new Text(output[1]));
	}

}
