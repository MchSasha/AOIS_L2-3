package truthtable;
import static org.junit.jupiter.api.Assertions.*;

class TruthTableTest {
    @org.junit.jupiter.api.Test
    void getPDNF() {
        String formula = "!((!y + !z) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        System.out.println(table1);
        assertEquals("(!x * !y * !z) + (!x * y * !z) + (!x * y * z) + (x * y * z)", table1.getPDNF());
    }

    @org.junit.jupiter.api.Test
    void getPCNF() {
        String formula = "!((!y + !z) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        System.out.println(table1);
        assertEquals("(x + y + !z) * (!x + y + z) * (!x + y + !z) * (!x + !y + z)", table1.getPCNF());
    }
    @org.junit.jupiter.api.Test
    void getNumericPDNF() {
        String formula = "!((!x + y) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        System.out.println(table1);
        assertEquals("+(0, 2, 4, 5)", table1.getNumericPDNF());
    }
    @org.junit.jupiter.api.Test
    void getNumericPCNF() {
        String formula = "!((!x + y) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        System.out.println(table1);
        assertEquals("*(1, 3, 6, 7)", table1.getNumericPCNF());
    }
    @org.junit.jupiter.api.Test
    void getPerfectForms1() {
        String formula = "!((!x + !y) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        System.out.println(table1);
        assertEquals("(!x * !y * !z) + (!x * y * !z) + (x * y * !z) + (x * y * z)", table1.getPDNF());
        assertEquals("(x + y + !z) * (x + !y + !z) * (!x + y + z) * (!x + y + !z)", table1.getPCNF());
    }
    @org.junit.jupiter.api.Test
    void getPerfectForms2() {
        String formula = "!((!x + !y) * !(!x * z))";
        TruthTable table1 = new TruthTable(formula);
        System.out.println(table1);
        assertEquals("(!x * !y * z) + (!x * y * z) + (x * y * !z) + (x * y * z)", table1.getPDNF());
        assertEquals("(x + y + z) * (x + !y + z) * (!x + y + z) * (!x + y + !z)", table1.getPCNF());
    }
    @org.junit.jupiter.api.Test
    void getPerfectForms3() {
        String formula = "!((!x + y) * !(!x * !z))";
        TruthTable table1 = new TruthTable(formula);
        System.out.println(table1);
        assertEquals("(!x * !y * !z) + (!x * y * !z) + (x * !y * !z) + (x * !y * z)", table1.getPDNF());
        assertEquals("(x + y + !z) * (x + !y + !z) * (!x + !y + z) * (!x + !y + !z)", table1.getPCNF());
    }

    @org.junit.jupiter.api.Test
    void otherLength() {
        TruthTable table5 = new TruthTable("(y + z)");
        System.out.println(table5);
        assertEquals("(!y * z) + (y * !z) + (y * z)", table5.getPDNF());
        assertEquals("(y + z)", table5.getPCNF());
        assertEquals("+(1, 2, 3)", table5.getNumericPDNF());
        assertEquals("*(0)", table5.getNumericPCNF());
        assertEquals("f7(2)", table5.getIndexForm());
    }


}
