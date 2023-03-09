import static org.junit.jupiter.api.Assertions.*;

class TruthTableTest {
    @org.junit.jupiter.api.Test
    void getTruthTableRaw1() {
        String formula = "!((!y + !z) * !(!x * !z))";
        TruthTable table = new TruthTable(formula);
        boolean result = table.getTruthTableRawResult(formula, false, false, false);
        assertTrue(result);
    }
    @org.junit.jupiter.api.Test
    void getTruthTableRaw2() {
        String formula = "!((!y + !z) * !(!x * !z))";
        TruthTable table = new TruthTable(formula);
        boolean result = table.getTruthTableRawResult(formula, true, false, true);
        assertFalse(result);
    }
    @org.junit.jupiter.api.Test
    void getTruthTableRaw3() {
        String formula = "!((!y + !z) * !(!x * !z))";
        TruthTable table = new TruthTable(formula);
        boolean result = table.getTruthTableRawResult(formula, true, true, false);
        assertFalse(result);
    }
    @org.junit.jupiter.api.Test
    void getTruthTableRaw4() {
        String formula = "!((!y + !z) * !(!x * !z))";
        TruthTable table = new TruthTable(formula);
        boolean result = table.getTruthTableRawResult(formula, false, false, false);
        assertTrue(result);
    }
    @org.junit.jupiter.api.Test
    void getTruthTableRawWholeVariant1() {
        String formula = "!((!y + !z) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        assertTrue(table1.getTruthTableRawResult(formula, false, false, false));
        assertFalse(table1.getTruthTableRawResult(formula, false, false, true));
        assertTrue(table1.getTruthTableRawResult(formula, false, true, false));
        assertTrue(table1.getTruthTableRawResult(formula, false, true, true));
        assertFalse(table1.getTruthTableRawResult(formula, true, false, false));
        assertFalse(table1.getTruthTableRawResult(formula, true, false, true));
        assertFalse(table1.getTruthTableRawResult(formula, true, true, false));
        assertTrue(table1.getTruthTableRawResult(formula, true, true, true));
    }

    @org.junit.jupiter.api.Test
    void getPDNF() {
        String formula = "!((!y + !z) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        assertEquals("(!x * !y * !z) + (!x * y * !z) + (!x * y * z) + (x * y * z)", table1.getPDNF());
    }
    @org.junit.jupiter.api.Test
    void getPCNF() {
        String formula = "!((!y + !z) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        assertEquals("(x + y + !z) * (!x + y + z) * (!x + y + !z) * (!x + !y + z)", table1.getPCNF());
    }
    @org.junit.jupiter.api.Test
    void getNumericPDNF() {
        String formula = "!((!x + y) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        assertEquals("+(0, 2, 4, 5)", table1.getNumericPDNF());
    }
    @org.junit.jupiter.api.Test
    void getNumericPCNF() {
        String formula = "!((!x + y) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        assertEquals("*(1, 3, 6, 7)", table1.getNumericPCNF());
    }
    @org.junit.jupiter.api.Test
    void getPerfectForms1() {
        String formula = "!((!x + !y) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        assertEquals("(!x * !y * !z) + (!x * y * !z) + (x * y * !z) + (x * y * z)", table1.getPDNF());
        assertEquals("(x + y + !z) * (x + !y + !z) * (!x + y + z) * (!x + y + !z)", table1.getPCNF());
    }
    @org.junit.jupiter.api.Test
    void getPerfectForms2() {
        String formula = "!((!x + !y) * !(!x * z))";
        TruthTable table1 = new TruthTable(formula);
        assertEquals("(!x * !y * z) + (!x * y * z) + (x * y * !z) + (x * y * z)", table1.getPDNF());
        assertEquals("(x + y + z) * (x + !y + z) * (!x + y + z) * (!x + y + !z)", table1.getPCNF());
    }
    @org.junit.jupiter.api.Test
    void getPerfectForms3() {
        String formula = "!((!x + y) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        assertEquals("(!x * !y * !z) + (!x * y * !z) + (x * !y * !z) + (x * !y * z)", table1.getPDNF());
        assertEquals("(x + y + !z) * (x + !y + !z) * (!x + !y + z) * (!x + !y + !z)", table1.getPCNF());
    }

}
