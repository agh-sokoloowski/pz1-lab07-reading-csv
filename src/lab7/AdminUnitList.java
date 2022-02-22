package lab7;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminUnitList {
    List<AdminUnit> units = new ArrayList<>();
    Map<Long, AdminUnit> idToAdminUnit = new HashMap<>();
    Map<AdminUnit, Long> adminUnitToParentId = new HashMap<>();
    Map<Long, List<AdminUnit>> parentIdToChildren = new HashMap<>();

    /**
     * Czyta rekordy pliku i dodaje do listy
     *
     * @param filename nazwa pliku
     */
    public void read(String filename) {
        CSVReader reader = null;

        try {
            reader = new CSVReader(filename, ",", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (reader != null && reader.next()) {
            AdminUnit adminUnit = new AdminUnit();

            adminUnit.name = reader.get("name");

            try {
                adminUnit.adminLevel = reader.getInt("admin_level");
            } catch (Exception e) {
                adminUnit.adminLevel = -1;
            }

            try {
                adminUnit.population = reader.getInt("population");
            } catch (Exception e) {
                adminUnit.population = -1;
            }

            try {
                adminUnit.area = reader.getDouble("area");
            } catch (Exception e) {
                adminUnit.area = -1;
            }

            try {
                adminUnit.density = reader.getDouble("density");
            } catch (Exception e) {
                adminUnit.density = -1;
            }

            try {
                adminUnit.bbox.xmin = Math.min(
                        Math.min(
                                reader.getInt("x1"),
                                reader.getInt("x2")),
                        Math.min(
                                reader.getInt("x3"),
                                reader.getInt("x4")));
                adminUnit.bbox.ymin = Math.min(
                        Math.min(
                                reader.getInt("y1"),
                                reader.getInt("y2")),
                        Math.min(
                                reader.getInt("y3"),
                                reader.getInt("y4")));
                adminUnit.bbox.xmax = Math.max(
                        Math.max(
                                reader.getInt("x1"),
                                reader.getInt("x2")),
                        Math.max(
                                reader.getInt("x3"),
                                reader.getInt("x4")));
                adminUnit.bbox.ymax = Math.max(
                        Math.max(
                                reader.getInt("y1"),
                                reader.getInt("y2")),
                        Math.max(
                                reader.getInt("y3"),
                                reader.getInt("y4")));
            } catch (Exception e) {
                // Jeżeli chociaż jedna wartość będzie pusta,
                // to cały bounding box będzie zły
                adminUnit.bbox.xmin = -1;
                adminUnit.bbox.ymin = -1;
                adminUnit.bbox.xmax = -1;
                adminUnit.bbox.ymax = -1;
            }

            long parentId = -1;
            try {
                parentId = reader.getLong("parent");
            } catch (Exception e) {
                // nie ma parenta
            }

            try {
                this.idToAdminUnit.put(reader.getLong("id"), adminUnit);
            } catch (Exception e) {
                // nie no nie ma opcji, że nie będzie ID
            }
            this.adminUnitToParentId.put(adminUnit, parentId);

            if (!this.parentIdToChildren.containsKey(parentId)) {
                this.parentIdToChildren.put(parentId, new ArrayList<>());
            }

            this.parentIdToChildren.get(parentId).add(adminUnit);

            // dodaj do listy
            this.units.add(adminUnit);
        }

        for (AdminUnit unit : this.units) {
            long parentId = this.adminUnitToParentId.get(unit);
            unit.parent = this.idToAdminUnit.getOrDefault(parentId, null);
        }

        for (Map.Entry<Long, AdminUnit> entry : this.idToAdminUnit.entrySet()) {
            entry.getValue().children = this.parentIdToChildren.get(entry.getKey());
        }
    }

    /**
     * Wypisuje zawartość korzystając z AdminUnit.toString()
     *
     * @param out
     */
    public void list(PrintStream out) {
        for (AdminUnit unit : this.units) {
            out.print(unit);
        }
    }

    /**
     * Wypisuje co najwyżej limit elementów począwszy od elementu o indeksie offset
     *
     * @param out    - strumień wyjsciowy
     * @param offset - od którego elementu rozpocząć wypisywanie
     * @param limit  - ile (maksymalnie) elementów wypisać
     */
    public void list(PrintStream out, int offset, int limit) {
        for (int i = 0; i < limit; i++) {
            out.print(this.units.get(i + offset));
        }
    }

    /**
     * Zwraca nową listę zawierającą te obiekty AdminUnit, których nazwa pasuje do wzorca
     *
     * @param pattern - wzorzec dla nazwy
     * @param regex   - jeśli regex=true, użyj funkcji String matches(); jeśli false użyj funkcji contains()
     * @return podzbiór elementów, których nazwy spełniają kryterium wyboru
     */
    public AdminUnitList selectByName(String pattern, boolean regex) {
        AdminUnitList ret = new AdminUnitList();
        // przeiteruj po zawartości units
        for (AdminUnit unit : this.units) {
            if (regex) {
                if (unit.name.matches(pattern)) {
                    // jeżeli nazwa jednostki pasuje do wzorca dodaj do ret
                    ret.units.add(unit);
                }
            } else {
                if (unit.name.contains(pattern)) {
                    // jeżeli nazwa jednostki pasuje do wzorca dodaj do ret
                    ret.units.add(unit);
                }
            }
        }
        return ret;
    }

    private void fixMissingValues() {
        for (AdminUnit unit : this.units) {
            unit.fixMissingValues();
        }
    }
}
