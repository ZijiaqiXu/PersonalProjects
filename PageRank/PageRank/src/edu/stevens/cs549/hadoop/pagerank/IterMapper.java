package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class IterMapper extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException,
			IllegalArgumentException {
		String line = value.toString(); // Converts Line to a String
		String[] sections = line.split("\t"); // Splits it into two parts. Part 1: node+rank | Part 2: adj list

		if (sections.length > 2) // Checks if the data is in the incorrect format
		{
			throw new IOException("Incorrect data format");
		}
		if (sections.length != 2) {
			return;
		}
		
		// split node and rank
		String[] nodeRank = sections[0].split(":");
		// split adjacency list into String array
		String[] adjList = sections[1].split(" ");
		
		// compute weight for every pointed node
		double rank = Double.parseDouble(nodeRank[1]);
		double weight = rank/adjList.length;
		
		// emit all adjVertex-computedWeight pairs for this node
		for (String adjVertex: adjList) {
			context.write(new Text(adjVertex), new Text(Double.toString(weight)));
		}
		
		// also, emit this node with its adjacency list with marker "L" at the beginning
			context.write(new Text(nodeRank[0]), new Text("L" + sections[1]));

	}

}
