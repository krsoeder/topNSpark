package kyle.soeder.codingtest.objects;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LogEntryTest {

	@Test
	public void testParse() {
		final LogEntry test = new LogEntry(
				"burger.letters.com - - [01/Jul/1995:00:00:12 -0400] \"GET /shuttle/countdown/video/livevideo.gif HTTP/1.0\" 200 0");
		assertEquals(test.getUser(), "burger.letters.com");
		assertEquals(test.getTimestamp(), "01/Jul/1995");
		assertEquals(test.getUrl(), "/shuttle/countdown/video/livevideo.gif");
		assertEquals(test.getByteReply(), 0);
		assertEquals(test.getHttpReply(), 200);
	}

	@Test
	public void testEmptyParse() {
		final LogEntry test = new LogEntry("");
		assertEquals(test.getUser(), null);
		assertEquals(test.getTimestamp(), null);
		assertEquals(test.getUrl(), null);
		assertEquals(test.getByteReply(), 0);
		assertEquals(test.getHttpReply(), 0);
	}

	@Test
	public void testPartialLineParse() {
		final LogEntry test = new LogEntry("stuff");
		assertEquals(test.getUser(), null);
		assertEquals(test.getTimestamp(), null);
		assertEquals(test.getUrl(), null);
		assertEquals(test.getByteReply(), 0);
		assertEquals(test.getHttpReply(), 0);
	}

}
