package lab7;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class AdminUnitTest {
    @Test
    public void testListWithOffsetAndLimit() {
        String path = "/path/to/lab7/examples/";
        AdminUnitList adminUnitList = new AdminUnitList();
        try {
            adminUnitList.read(path + "admin-units.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        adminUnitList.list(ps, 1, 1);
        String result = os.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("Kolonia Południowa"));
        assertTrue(result.contains("typ: "));
        assertTrue(result.contains("powierzchnia: "));
    }
}
