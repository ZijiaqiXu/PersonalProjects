package edu.stevens.cs549.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class EquiJoinMapper extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException,
			IllegalArgumentException {
		String line = value.toString(); // Converts Line to a String

		String[] sections = line.split(": "); // Splits it into two parts. Part 1: id | Part 2: name/"\t"+rank

		if (sections.length > 2) // Checks if the data is in the incorrect format
		{
			throw new IOException("Incorrect data format");
		}
		if (sections.length != 2) {
			return;
		}
		
		// output key: id | value: name/"\t"+rank, including "\t" as a marker
		context.write(new Text(sections[0]), new Text(sections[1]));
	}

}
