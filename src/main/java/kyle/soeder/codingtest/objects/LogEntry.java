package kyle.soeder.codingtest.objects;

import java.io.Serializable;

public class LogEntry implements Serializable {

	String user;
	String timestamp;
	String url;
	int httpReply;
	long byteReply;

	/**
	 * This will parse a string log entry and turn it into a usable log entry
	 * object. The parsing is performed with 2 splits based on the data looking as
	 * such "user - - [DD/mon/YYYY:HH:MM:SS offset] "CALL url METHOD" httpReply
	 * bytesReply" We are just nulling out bad data for this exercise.
	 *
	 * @param logEntry
	 */
	public LogEntry(String logEntry) {
		try {
			// Get the user from the the data before " - - "
			final String[] firstSplit = logEntry.split(" - - ");
			// Get the data after the " - - " broken apart by spaces.
			final String[] secondSplit = firstSplit[1].split(" ");

			this.user = firstSplit[0];
			this.timestamp = secondSplit[0].replace("[", "").substring(0, 11);
			this.url = secondSplit[3];
			this.httpReply = Integer.parseInt(secondSplit[5]);
			this.byteReply = Long.parseLong(secondSplit[6]);
		} catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
			this.httpReply = 0;
			this.byteReply = 0;
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getHttpReply() {
		return httpReply;
	}

	public void setHttpReply(int httpReply) {
		this.httpReply = httpReply;
	}

	public long getByteReply() {
		return byteReply;
	}

	public void setByteReply(long byteReply) {
		this.byteReply = byteReply;
	}

	@Override
	public String toString() {
		return "LogEntry [user=" + user + ", timestamp=" + timestamp + ", url=" + url
				+ ", httpReply=" + httpReply + ", byteReply=" + byteReply + "]";
	}

}
