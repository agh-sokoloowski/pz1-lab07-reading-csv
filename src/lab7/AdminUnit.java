package lab7;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AdminUnit {
    String name;
    int adminLevel;
    double population;
    double area;
    double density;
    AdminUnit parent;
    List<AdminUnit> children;
    BoundingBox bbox = new BoundingBox();

    public String toString() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        ps.printf("%s:\n\ttyp: %d\n\tpowierzchnia: %f\n\n", this.name, this.adminLevel, this.area);
        return os.toString(StandardCharsets.UTF_8);
    }

    void fixMissingValues() {
        // Najbardziej nas interesuje, by parent miał density
        if (this.parent.density == -1) {
            this.parent.fixMissingValues();
        }

        if (this.density == -1) {
            this.density = this.parent.density;
        }

        if (this.population == -1) {
            this.population = this.area * this.density;
        }
    }
}
