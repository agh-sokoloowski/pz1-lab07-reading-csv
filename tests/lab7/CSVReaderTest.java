package lab7;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CSVReaderTest {
    @Test
    public void testConstructors() {
        assertThrows(IOException.class, () -> {
            CSVReader reader = new CSVReader("I_DO_NOT_EXIST.csv");
        });
    }
}
