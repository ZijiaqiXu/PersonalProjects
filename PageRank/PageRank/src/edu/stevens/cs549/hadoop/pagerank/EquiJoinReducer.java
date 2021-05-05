package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class EquiJoinReducer extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		// key: id; values: name/"\t"+rank
		
		double rank = 0d;
		String name = "";

		for (Text text: values) {
			if (text.toString().charAt(0) == '\t') rank = Double.parseDouble(text.toString().substring(1));
			else name = text.toString();
		}
		
		// output key:name | value: rank
		context.write(new Text(name), new Text(Double.toString(rank)));

	}
}