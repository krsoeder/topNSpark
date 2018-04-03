package kyle.soeder.codingtest.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.spark.api.java.function.Function;

import kyle.soeder.codingtest.objects.LogEntry;
import scala.Tuple2;
import scala.Tuple3;

public class Mappers implements Serializable {

	/**
	 * This comparator allows us to compare a Tuple2<String,Integer> with itself
	 * where the comparison is strictly taking into account the Integer value. this
	 * is used for ordering our user and url tuple lists where the Integer value is
	 * the number of occurrences.
	 */
	public final static Comparator<Tuple2<String, Integer>> tupleComparator = new Comparator<Tuple2<
			String, Integer>>() {

		@Override
		public int compare(Tuple2<String, Integer> o1, Tuple2<String, Integer> o2) {

			return o1._2().compareTo(o2._2());
		}
	};

	/**
	 * This mapper function takes a Log Entry and converts it into a Tuple3 of
	 * Date,User, and the value 1 for use in a reduce by key counter later.
	 */
	public final static Function<LogEntry, Tuple3<String, String,
			Integer>> userMapper = new Function<LogEntry, Tuple3<String, String, Integer>>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Tuple3<String, String, Integer> call(LogEntry entry) throws Exception {
					return new Tuple3<>(entry.getTimestamp(), entry.getUser(), 1);
				}

			};

	/**
	 * This mapper function takes a Tuple2<Tuple2<String,String>, Integer> which is
	 * the result of a reduce by key where the key is a Tuple2 of Date and Either
	 * user or url visited and converts it to a Tuple3 to allow for the final
	 * transformation of the data. The Tuple3 thats results is (Date, User or Url,
	 * Count of Occurrences). From here we can transform the data to create ordered
	 * lists by day.
	 */
	public final static Function<Tuple2<Tuple2<String, String>, Integer>, Tuple3<String, String,
			Integer>> dayMapper = new Function<Tuple2<Tuple2<String, String>, Integer>, Tuple3<
					String, String, Integer>>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Tuple3<String, String, Integer> call(Tuple2<Tuple2<String, String>,
						Integer> entry) throws Exception {
					return new Tuple3<>(entry._1()._1(), entry._1()._2(), entry._2());
				}

			};

	/**
	 * This mapper takes in the Tuple3 of (Day, List of ( Users, Counts ), topN
	 * where topN is the number of top values we want to return from the list). To
	 * do this I sort that days data and return the top last valeus of the list
	 * since natural order is ascending.
	 */
	public final static Function<Tuple3<String, ArrayList<Tuple2<String, Integer>>, Integer>,
			Tuple2<String, ArrayList<Tuple2<String, Integer>>>> topNMapper = new Function<Tuple3<
					String, ArrayList<Tuple2<String, Integer>>, Integer>, Tuple2<String, ArrayList<
							Tuple2<String, Integer>>>>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Tuple2<String, ArrayList<Tuple2<String, Integer>>> call(Tuple3<String,
						ArrayList<Tuple2<String, Integer>>, Integer> entry) throws Exception {
					final ArrayList<Tuple2<String, Integer>> dayData = entry._2();
					dayData.sort(tupleComparator);
					final int size = dayData.size();
					if (size <= entry._3())
						return new Tuple2<>(entry._1(), dayData);
					else
						return new Tuple2<>(entry._1(), new ArrayList<>(dayData.subList(size - 1
								- entry._3(), size - 1)));
				}

			};

	/**
	 * This mapper converts a Log entry into a tuple3 of Day, URL, 1 for use in
	 * counting later.
	 */
	public final static Function<LogEntry, Tuple3<String, String,
			Integer>> urlMapper = new Function<LogEntry, Tuple3<String, String, Integer>>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Tuple3<String, String, Integer> call(LogEntry entry) throws Exception {
					return new Tuple3<>(entry.getTimestamp(), entry.getUrl(), 1);
				}

			};
}
