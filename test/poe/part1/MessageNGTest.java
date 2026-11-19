package poe.part1;

import java.io.File;
import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MessageNGTest {

    private static final String TEST_JSON_FILE = "test_messages_part3.json";
    private File testFile;

    @BeforeMethod
    public void setUpTestFile() {
        testFile = new File(TEST_JSON_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @AfterMethod
    public void tearDownTestFile() {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    // --- Part 2 Tests ---

    @Test
    public void testCheckMessageIDSuccess() {
        Message message = new Message();
        assertTrue(message.checkMessageID("1234567890"));
    }

    @Test
    public void testCheckRecipientCellSuccess() {
        Message message = new Message();
        String expected = "Cell phone number successfully captured.";
        String actual = message.checkRecipientCell("+27718693002");
        assertEquals(actual, expected);
    }

    @Test
    public void testCheckRecipientCellFailure() {
        Message message = new Message();
        String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        String actual = message.checkRecipientCell("08575975889");
        assertEquals(actual, expected);
    }

    @Test
    public void testCreateMessageHash() {
        Message message = new Message();
        String actualHash = message.createMessageHash("0012345678", 0, "Hi Thanks");
        assertEquals(actualHash, "00:0:HITHANKS");
    }

    // --- Part 3 Tests ---

    @Test
    public void testSentMessagesArrayPopulated() {
        Message message = new Message();
        message.recordPart3Outcome("Sent", "1000000001", "+27834557896",
                "Did you get the cake?", "10:1:DIDCAKE?", "Developer");
        message.recordPart3Outcome("Stored", "1000000002", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.", "10:2:WHERETIME.", "Developer");
        message.recordPart3Outcome("Disregarded", "1000000003", "+27834484567",
                "Yohoooo, I am at your gate.", "10:3:YOHOOOOGATE.", "Developer");
        message.recordPart3Outcome("Sent", "0838884567", "+27838884567",
                "It is dinner time!", "08:4:ITTIME!", "Developer");

        String[] expectedSent = {"Did you get the cake?", "It is dinner time!"};
        assertEquals(message.getSentMessagesArray(), expectedSent);
        assertEquals(message.returnTotalMessages(), 2);
    }

    @Test
    public void testDisplayLongestStoredMessage() {
        Message message = new Message();
        Message.StoredMessageRecord[] records = {
            new Message.StoredMessageRecord("1000000001", "+27834557896",
                    "Did you get the cake?", "10:1:DIDCAKE?", "Developer"),
            new Message.StoredMessageRecord("1000000002", "+27838884567",
                    "Where are you? You are late! I have asked you to be on time.", "10:2:WHERETIME.", "Developer"),
            new Message.StoredMessageRecord("1000000005", "+27838884567",
                    "Ok, I am leaving without you.", "10:5:OKYOU.", "Developer")
        };

        String result = message.displayLongestStoredMessage(records);
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    public void testSearchByMessageID() {
        Message message = new Message();
        Message.StoredMessageRecord[] records = {
            new Message.StoredMessageRecord("0838884567", "+27838884567",
                    "It is dinner time!", "08:4:ITTIME!", "Developer")
        };

        String result = message.searchByMessageID(records, "0838884567");
        assertTrue(result.contains("It is dinner time!"));
    }

    @Test
    public void testSearchByRecipient() {
        Message message = new Message();
        Message.StoredMessageRecord[] records = {
            new Message.StoredMessageRecord("1000000002", "+27838884567",
                    "Where are you? You are late! I have asked you to be on time.", "10:2:WHERETIME.", "Developer"),
            new Message.StoredMessageRecord("1000000005", "+27838884567",
                    "Ok, I am leaving without you.", "10:5:OKYOU.", "Developer")
        };

        String result = message.searchByRecipient(records, "+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        Message message = new Message();
        String targetHash = "10:2:WHERETIME.";
        String longestMsg = "Where are you? You are late! I have asked you to be on time.";

        message.storeMessage(TEST_JSON_FILE, "1000000002", "+27838884567", longestMsg, targetHash, "Developer");
        message.storeMessage(TEST_JSON_FILE, "1000000005", "+27838884567", "Ok, I am leaving without you.", "10:5:OKYOU.", "Developer");

        String deleteResponse = message.deleteMessageByHash(TEST_JSON_FILE, targetHash);
        assertTrue(deleteResponse.contains("deleted successfully"));

        Message.StoredMessageRecord[] remaining = message.loadStoredMessagesArray(TEST_JSON_FILE);
        assertEquals(remaining.length, 1);
        assertEquals(remaining[0].messageHash, "10:5:OKYOU.");
    }

    @Test
    public void testDisplayStoredMessagesReport() {
        Message message = new Message();
        Message.StoredMessageRecord[] records = {
            new Message.StoredMessageRecord("1000000002", "+27838884567",
                    "Where are you? You are late! I have asked you to be on time.", "10:2:WHERETIME.", "Developer")
        };

        String report = message.displayStoredMessagesReport(records);
        assertTrue(report.contains("10:2:WHERETIME."));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains("Where are you? You are late! I have asked you to be on time."));
    }
}