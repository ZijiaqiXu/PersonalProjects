package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DiffRed2 extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double diff_max = 0.0; // sets diff_max to a default value
		/* 
		 * Compute and emit the maximum of the differences
		 */
		double diff_cur = 0d;
		for (Text text: values) {
			diff_cur = Double.parseDouble(text.toString());
			if (diff_max < diff_cur) diff_max = diff_cur;
		}
		
		context.write(new Text(""), new Text(Double.toString(diff_max)));

	}
}
