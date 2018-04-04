package kyle.soeder.codingtest.utilities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;

import kyle.soeder.codingtest.objects.LogEntry;
import scala.Tuple2;
import scala.Tuple3;

public class SparkUtilities implements Serializable {
	private static Logger logger = Logger.getLogger(SparkUtilities.class);

	/**
	 * Main spark call to perform the calculation for top N User and Urls by day for
	 * given dataset.
	 *
	 * @param fileLocation
	 * @param topN
	 * @param sparkMaster
	 * @throws IOException
	 */
	public final static void findTopN(String fileLocation, int topN, String sparkMaster,
			String userResultFile, String urlResultFile) throws IOException {
		final String appName = "TopNCalculator";
		final SparkSession spark = SparkSession.builder().master(sparkMaster).appName(appName)
				.getOrCreate();

		// Read in data from file system into RDD
		final JavaRDD<String> textFile = spark.read().textFile(fileLocation).javaRDD();
		logger.debug("Number of rows read from file: " + textFile.count());

		// Parse the information out of the logs into LogEntry Objects
		final JavaRDD<LogEntry> parseLogs = parseLogs(textFile);

		// Count user occurrence for each day.
		final JavaPairRDD<Tuple2<String, String>, Integer> userCounts = getUserCountsLogs(
				parseLogs);

		// Change key to row of day, user, count.
		final JavaRDD<Tuple3<String, String, Integer>> dayToUsers = userCounts.map(
				Mappers.dayMapper);

		// Convert data to a map of Day with a list of User and Count
		final JavaPairRDD<String, ArrayList<Tuple2<String,
				Integer>>> dayUserCounts = getListsForEachDay(dayToUsers);

		// Get top n users with the most visits for each day
		final List<Tuple2<String, ArrayList<Tuple2<String, Integer>>>> topNUsers = dayUserCounts
				.map(row -> new Tuple3<>(row._1, row._2, topN)).map(Mappers.topNMapper).collect();

		// Output result data for users
		FileUtilities.writeToFile(userResultFile, topNUsers, "User");

		for (final Tuple2<String, ArrayList<Tuple2<String, Integer>>> user : topNUsers) {
			logger.debug("Day " + user._1());
			for (final Tuple2<String, Integer> userDay : user._2) {
				logger.debug("    User   " + userDay._1() + "   count   " + userDay._2());
			}
		}

		// Retrieve data for number of occurrence of each url for the Day they were
		// viewed
		final JavaPairRDD<Tuple2<String, String>, Integer> urlCounts = getUrlCountsLogs(parseLogs);

		// Change data to row of day, user, count for aggregation in next step.
		final JavaRDD<Tuple3<String, String, Integer>> dayToUrl = urlCounts.map(Mappers.dayMapper);

		// Reduce data to Map of Day to the list of Urls , Counts for that day.
		final JavaPairRDD<String, ArrayList<Tuple2<String,
				Integer>>> dayUrlCounts = getListsForEachDay(dayToUrl);

		// Get top n urls with the most visits for each day
		final List<Tuple2<String, ArrayList<Tuple2<String, Integer>>>> topNUrls = dayUrlCounts.map(
				row -> new Tuple3<>(row._1, row._2, topN)).map(Mappers.topNMapper).collect();

		// Output result data for urls
		FileUtilities.writeToFile(urlResultFile, topNUrls, "URL");

		for (final Tuple2<String, ArrayList<Tuple2<String, Integer>>> user : topNUrls) {
			logger.debug("Day " + user._1());
			for (final Tuple2<String, Integer> userDay : user._2) {
				logger.debug("    URL   " + userDay._1() + "   count   " + userDay._2());
			}
		}

	}

	/**
	 * Maps each string row to LogEntry object in a given JAVA RDD. Returns the new
	 * RDD of Objects
	 *
	 * @param inputStringRows
	 * @return
	 */
	private final static JavaRDD<LogEntry> parseLogs(JavaRDD<String> inputStringRows) {
		return inputStringRows.map(new Function<String, LogEntry>() {
			private static final long serialVersionUID = 1L;

			@Override
			public LogEntry call(String s) throws Exception {
				return new LogEntry(s);
			}
		});
	}

	/**
	 * Maps each string row to LogEntry object in a given JAVA RDD. Returns the new
	 * RDD of Objects
	 *
	 * @param inputStringRows
	 * @return
	 */
	private final static JavaPairRDD<Tuple2<String, String>, Integer> getUserCountsLogs(JavaRDD<
			LogEntry> inputEntries) {
		return inputEntries.map(Mappers.userMapper).mapToPair(s -> new Tuple2<>(new Tuple2<>(s._1(),
				s._2()), s._3())).reduceByKey((i1, i2) -> i1 + i2);
	}

	/**
	 * Maps each string row to LogEntry object in a given JAVA RDD. Returns the new
	 * RDD of Objects
	 *
	 * @param inputStringRows
	 * @return
	 */
	private final static JavaPairRDD<Tuple2<String, String>, Integer> getUrlCountsLogs(JavaRDD<
			LogEntry> inputEntries) {
		return inputEntries.map(Mappers.urlMapper).mapToPair(s -> new Tuple2<>(new Tuple2<>(s._1(),
				s._2()), s._3())).reduceByKey((i1, i2) -> i1 + i2);
	}

	/**
	 * Take in an RDD of (Day, User or URL, Count) and return RDD of (Day,
	 * List(users,counts))
	 *
	 * @param dayToDataCount
	 * @return
	 */
	private final static JavaPairRDD<String, ArrayList<Tuple2<String, Integer>>> getListsForEachDay(
			JavaRDD<Tuple3<String, String, Integer>> dayToDataCount) {
		return dayToDataCount.mapToPair(s -> new Tuple2<>(s._1(), new ArrayList<>(Arrays.asList(
				new Tuple2<>(s._2(), s._3()))))).reduceByKey((i1, i2) -> {
					i1.addAll(i2);
					return i1;
				});
	}
}
