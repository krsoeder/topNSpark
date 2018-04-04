package kyle.soeder.codingtest.utilities;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class DownloadUtilities {
	final private static Logger logger = Logger.getLogger(DownloadUtilities.class);

	/**
	 * This is the helper method used to download the zip file from an external ftp
	 * site.
	 *
	 * @param ftpSite
	 *            String version of the ftp site and file name to download for this
	 *            test it is ftp://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz
	 * @return
	 * @throws IOException
	 *             - if there are any issues reading the external file or writing
	 *             the new file this error will get thrown up to the main class.
	 */
	public static boolean downloadFile(String ftpSite, String outputFileLocation)
			throws IOException {
		boolean success = false;
		BufferedInputStream in = null;
		FileOutputStream out = null;
		try {
			final URL url = new URL(ftpSite);
			final URLConnection con = url.openConnection();

			in = new BufferedInputStream(con.getInputStream());

			out = new FileOutputStream(outputFileLocation);

			logger.debug("Downloading file located at " + ftpSite);
			final byte[] readBytes = new byte[1024];
			int bytesRead = -1;
			while ((bytesRead = (in.read(readBytes))) >= 0) {
				out.write(readBytes, 0, bytesRead);
			}
			logger.debug("Completed file download, file is located at " + outputFileLocation);
			success = true;
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
		return success;
	}
}
