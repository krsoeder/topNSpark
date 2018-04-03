package kyle.soeder.codingtest.utilities;

import java.io.File;

public class FileUtilities {
	public static void cleanupFile(String fileLocation) {
		final File file = new File(fileLocation);
		System.out.println("Cleaning up file");
		if (file.delete()) {
			System.out.println("File deleted successfully");
		} else {
			System.out.println("Failed to delete the file");
		}
	}
}
