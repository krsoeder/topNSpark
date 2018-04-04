package kyle.soeder.codingtest.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import scala.Tuple2;

public class FileUtilitiesTest {

	@Test
	public void testDeleteParse() throws IOException {

		final File testFile = new File("test");
		testFile.createNewFile();
		assertTrue(testFile.exists());

		FileUtilities.cleanupFile("test");

		final File delete = new File("test");

		assertFalse(delete.exists());
	}

	@Test
	public void testWriteToFile() throws IOException {
		List<Tuple2<String, ArrayList<Tuple2<String, Integer>>>> data;
		data = new ArrayList<>();

		final ArrayList<Tuple2<String, Integer>> dataEntries = new ArrayList<>();
		final Tuple2<String, Integer> dataEntry1 = new Tuple2<>("test", 1);
		final Tuple2<String, Integer> dataEntry2 = new Tuple2<>("test2", 2);
		final Tuple2<String, Integer> dataEntry3 = new Tuple2<>("test3", 3);

		dataEntries.add(dataEntry1);
		dataEntries.add(dataEntry2);
		dataEntries.add(dataEntry3);

		final Tuple2<String, ArrayList<Tuple2<String, Integer>>> dayEntry = new Tuple2<>("day",
				dataEntries);
		data.add(dayEntry);

		FileUtilities.writeToFile("test", data, "test");

		final File testFile = new File("test");
		assertTrue(testFile.exists());
		BufferedReader bufferedReader = null;
		try {
			final FileReader fileReader = new FileReader(testFile);
			bufferedReader = new BufferedReader(fileReader);
			String line;

			// 12 lines generated for the above data.
			line = bufferedReader.readLine();
			assertEquals(line, "Data for test");
			line = bufferedReader.readLine();
			assertEquals(line, "");
			line = bufferedReader.readLine();
			assertEquals(line, "");
			line = bufferedReader.readLine();
			assertEquals(line, "Day day");
			line = bufferedReader.readLine();
			assertEquals(line, "    test   test   count   1");
			line = bufferedReader.readLine();
			assertEquals(line, "    test   test2   count   2");
			line = bufferedReader.readLine();
			assertEquals(line, "    test   test3   count   3");
		} catch (final Exception e) {

		} finally {
			if (bufferedReader != null)
				bufferedReader.close();

			FileUtilities.cleanupFile("test");
		}

	}
}
