package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BirthdayTest {

    @Test
    public void isValidBirthday() {
        // invalid birthday
        assertFalse(Birthday.isValidBirthday("")); // empty string
        assertFalse(Birthday.isValidBirthday(" ")); // spaces only
        assertFalse(Birthday.isValidBirthday("9188")); // less than 6 numbers
        assertFalse(Birthday.isValidBirthday("birthday")); // non-numeric
        assertFalse(Birthday.isValidBirthday("9011p1")); // alphabets within digits
        assertFalse(Birthday.isValidBirthday("931 534")); // spaces within digits
        assertFalse(Birthday.isValidBirthday("57247542979")); // long birthday numbers

        // valid birthday
        assertTrue(Birthday.isValidBirthday("121287")); // exactly 6 numbers
        assertTrue(Birthday.isValidBirthday("140597"));
    }
}
