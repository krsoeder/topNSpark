package kyle.soeder.codingtest.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import scala.Tuple2;

public class FileUtilities {
	private final static Logger logger = Logger.getLogger(FileUtilities.class);

	public final static void cleanupFile(String fileLocation) {
		final File file = new File(fileLocation);
		logger.debug("Cleaning up file");
		if (file.delete()) {
			logger.debug("File deleted successfully");
		} else {
			logger.debug("Failed to delete the file");
		}
	}

	/**
	 * Write the resulting days and their associated lists to outputfile.
	 *
	 *
	 * @return boolean for success or failure.
	 * @throws IOException
	 */
	public final static boolean writeToFile(String outputFileName, List<Tuple2<String, ArrayList<
			Tuple2<String, Integer>>>> data, String dataType) throws IOException {
		BufferedWriter bw = null;
		try {
			final File fout = new File(outputFileName);

			fout.createNewFile();
			final FileOutputStream fos = new FileOutputStream(fout);
			bw = new BufferedWriter(new OutputStreamWriter(fos));

			bw.write("Data for " + dataType);
			bw.newLine();
			bw.newLine();
			bw.newLine();
			for (final Tuple2<String, ArrayList<Tuple2<String, Integer>>> day : data) {
				bw.write("Day " + day._1());
				bw.newLine();
				for (final Tuple2<String, Integer> entryInDay : day._2) {
					bw.write("    " + dataType + "   " + entryInDay._1() + "   count   "
							+ entryInDay._2());
					bw.newLine();
				}
			}
		} catch (final IOException e) {
			logger.error("Failed to write to the output file ");
			logger.error(e);
			throw e;
		} finally {
			if (bw != null)
				bw.close();
		}

		return true;
	}
}
