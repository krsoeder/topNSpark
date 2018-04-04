package kyle.soeder.codingtest;

import java.io.IOException;

import org.apache.log4j.Logger;

import kyle.soeder.codingtest.utilities.DownloadUtilities;
import kyle.soeder.codingtest.utilities.FileUtilities;
import kyle.soeder.codingtest.utilities.SparkUtilities;

public class TopNMostFrequentVisitors {
	final static Logger logger = Logger.getLogger(TopNMostFrequentVisitors.class);

	public static void main(String[] args) {
		// Site to download data from e.g.
		// "http://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz"
		final String ftpSite = args[0];
		// Location to save the downloaded file to e.g. "NASA_access_log_Jul95.gz"
		final String outputFileLocation = args[1];
		// topN values to get e.g. 10 for testing
		final int topN = Integer.parseInt(args[2]);
		// Local sparkmaster e.g. "spark://periwinkle.local:7077"
		final String sparkMaster = args[3];
		// Name of the outputfile for the User Data
		final String userResultFile = args[4];
		// Name of the outputfile for the URL data
		final String urlResultFile = args[5];

		logger.debug("ftpSite : " + ftpSite);
		logger.debug("outputFileLocation : " + outputFileLocation);
		logger.debug("topN : " + topN);
		logger.debug("sparkMaster : " + sparkMaster);
		logger.debug("userResultFile : " + userResultFile);
		logger.debug("urlResultFile : " + urlResultFile);

		boolean fileDownloaded = false;
		// For testing this allows me to use pre-downloaded files when flipped to false.
		final boolean downLoadFile = true;
		if (downLoadFile)
			try {
				fileDownloaded = DownloadUtilities.downloadFile(ftpSite, outputFileLocation);
			} catch (final IOException e) {
				logger.error("There was an exception reading the file from the ftp site.");
				logger.error(e);
			}
		if (!downLoadFile || fileDownloaded) {
			try {
				SparkUtilities.findTopN(outputFileLocation, topN, sparkMaster, userResultFile,
						urlResultFile);
			} catch (final Exception e) {

			} finally {
				if (downLoadFile)
					FileUtilities.cleanupFile(outputFileLocation);
			}
		} else {
			if (downLoadFile)
				FileUtilities.cleanupFile(outputFileLocation);
			logger.error(
					"We are exiting the program due to a failure downloading the in the file.");
		}

	}

}
