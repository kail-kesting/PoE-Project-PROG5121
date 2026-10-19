package poe.part1;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class MessageNGTest {

    @Test
    public void testCheckMessageIDSuccess() {
        Message message = new Message();
        // Valid ID <= 10 characters
        boolean result = message.checkMessageID("1234567890");
        assertTrue(result);
    }

    @Test
    public void testCheckRecipientCellSuccess() {
        Message message = new Message();
        // Valid SA number with international code
        String expected = "Cell phone number successfully captured.";
        String actual = message.checkRecipientCell("+27718693002");
        assertEquals(actual, expected);
    }

    @Test
    public void testCheckRecipientCellFailure() {
        Message message = new Message();
        // Invalid number format
        String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        String actual = message.checkRecipientCell("08575975889");
        assertEquals(actual, expected);
    }

    @Test
    public void testCreateMessageHash() {
        Message message = new Message();
        // Test Case 1 Data from Assignment Rubric
        String messageID = "0012345678";
        int messageNumber = 0;
        String messageText = "Hi Thanks";

        String expectedHash = "00:0:HITHANKS";
        String actualHash = message.createMessageHash(messageID, messageNumber, messageText);

        assertEquals(actualHash, expectedHash);
    }
}
