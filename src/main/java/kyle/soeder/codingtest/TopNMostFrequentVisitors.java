package kyle.soeder.codingtest;

import java.io.IOException;

import kyle.soeder.codingtest.utilities.DownloadUtilities;
import kyle.soeder.codingtest.utilities.FileUtilities;
import kyle.soeder.codingtest.utilities.SparkUtilities;

public class TopNMostFrequentVisitors {

	public static void main(String[] args) {

		final String ftpSite = "http://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz";
		final String outputFileLocation = "NASA_access_log_Jul95.gz";
		final int topN = 10;
		final String sparkMaster = "spark://127.0.0.1:7077";
		boolean fileDownloaded = false;
		final boolean downLoadFile = true;
		if (downLoadFile)
			try {
				fileDownloaded = DownloadUtilities.downloadFile(ftpSite, outputFileLocation);
			} catch (final IOException e) {
				System.out.println("There was an exception reading the file from the ftp site.");
				e.printStackTrace();
			}
		if (!downLoadFile || fileDownloaded) {
			try {
				SparkUtilities.findTopN(outputFileLocation, topN, sparkMaster);
			} catch (final Exception e) {

			} finally {
				if (downLoadFile)
					FileUtilities.cleanupFile(outputFileLocation);
			}
		} else {
			if (downLoadFile)
				FileUtilities.cleanupFile(outputFileLocation);
			System.out.println("We are exiting the program due to a failure reading in the file.");
		}

	}

}
