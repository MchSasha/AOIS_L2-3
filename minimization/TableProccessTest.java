package minimization;
import truthtable.TruthTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableProcessTest {

    @Test
    void getVeitchKarnaughCNF1() {
        String formula = "!((!y + !z) + !(!x * z))";

        assertEquals("(!x) * (y) * (z)", Minimization.getVeitchKarnaughCNF((new TruthTable(formula)).getPCNF()));
    }
    @Test
    void getVeitchKarnaughCNF2() {
        String formula = "!((!y + !z) * !(!x * !z))";

        assertEquals("(y + !z) * (!x + z)", Minimization.getVeitchKarnaughCNF((new TruthTable(formula)).getPCNF()));
    }
    @Test
    void getVeitchKarnaughCNF3() {
        String formula = "((!y * !z) * !(!x * !z))";

        assertEquals("(x) * (!z) * (!y)", Minimization.getVeitchKarnaughCNF((new TruthTable(formula)).getPCNF()));
    }
    @Test
    void getVeitchKarnaughCNF4() {
        String formula = "((!y * z) * !(!x * !z))";

        assertEquals("(!y) * (z)", Minimization.getVeitchKarnaughCNF((new TruthTable(formula)).getPCNF()));
    }
    @Test
    void getVeitchKarnaughCNF5() {
        String formula = "!((y + !z) * !(x * !z))";

        assertEquals("(!y + !z) * (x + z)", Minimization.getVeitchKarnaughCNF((new TruthTable(formula)).getPCNF()));
    }
    @Test
    void getVeitchKarnaughCNF6() {
        String formula = "!((y + !z) * (!x * z))";

        assertEquals("(x + !y + !z)", Minimization.getVeitchKarnaughCNF((new TruthTable(formula)).getPCNF()));
    }
    @Test
    void getVeitchKarnaughCNF7() {
        String formula = "!((y * !z) * (!x + z))";

        assertEquals("(x + !y + z)", Minimization.getVeitchKarnaughCNF((new TruthTable(formula)).getPCNF()));
    }
    @Test
    void getVeitchKarnaughCNF8() {
        String formula = "!((y * z) + (x * z))";

        assertEquals("(!y + !z) * (!x + !z)", Minimization.getVeitchKarnaughCNF((new TruthTable(formula)).getPCNF()));
    }


    @Test
    void getVeitchKarnaughDNF1() {
        String formula = "!((!y + !z) + !(!x * z))";

        assertEquals("(!x * y * z)", Minimization.getVeitchKarnaughDNF((new TruthTable(formula)).getPDNF()));
    }
    @Test
    void getVeitchKarnaughDNF2() {
        String formula = "!((!y + !z) * !(!x * !z))";

        assertEquals("(y * z) + (!x * !z)", Minimization.getVeitchKarnaughDNF((new TruthTable(formula)).getPDNF()));
    }
    @Test
    void getVeitchKarnaughDNF3() {
        String formula = "((!y * !z) * !(!x * !z))";

        assertEquals("(x * !y * !z)", Minimization.getVeitchKarnaughDNF((new TruthTable(formula)).getPDNF()));
    }
    @Test
    void getVeitchKarnaughDNF4() {
        String formula = "((!y * z) * !(!x * !z))";

        assertEquals("(!y * z)", Minimization.getVeitchKarnaughDNF((new TruthTable(formula)).getPDNF()));
    }
    @Test
    void getVeitchKarnaughDNF5() {
        String formula = "!((y + !z) * !(x * !z))";

        assertEquals("(!y * z) + (x * !z)", Minimization.getVeitchKarnaughDNF((new TruthTable(formula)).getPDNF()));
    }
    @Test
    void getVeitchKarnaughDNF6() {
        String formula = "!((y + !z) * (!x * z))";

        assertEquals("(x) + (!y) + (!z)", Minimization.getVeitchKarnaughDNF((new TruthTable(formula)).getPDNF()));
    }
    @Test
    void getVeitchKarnaughDNF7() {
        String formula = "!((y * !z) * (!x + z))";

        assertEquals("(x) + (!y) + (z)", Minimization.getVeitchKarnaughDNF((new TruthTable(formula)).getPDNF()));
    }
    @Test
    void getVeitchKarnaughDNF8() {
        String formula = "!((y * z) + (x * z))";

        assertEquals("(!z) + (!x * !y)", Minimization.getVeitchKarnaughDNF((new TruthTable(formula)).getPDNF()));
    }
}