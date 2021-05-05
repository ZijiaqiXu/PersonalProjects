package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DiffRed1 extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double[] ranks = new double[2];
		
		Iterator<Text> iterator = values.iterator();
		try {
			ranks[0] = Double.parseDouble(iterator.next().toString());
			ranks[1] = Double.parseDouble(iterator.next().toString());
		} catch (NoSuchElementException ne) {
			ne.printStackTrace();
		}
		
		// compute rank difference
		double diff = Math.abs(ranks[0] - ranks[1]);
		
		// emit key:node; value: rank difference
		context.write(key, new Text(Double.toString(diff)));
	}
}
