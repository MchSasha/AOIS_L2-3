package minimization;
import truthtable.TruthTable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinimizationTest {

    @Test
    void getCalculationCNF1() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z) * (!x + !y + !z)";
        assertEquals("(y + z) * (!x + !z)", Minimization.getCalculationCNF(formula));
    }
    @Test
    void getCalculationCNF2() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z)";
        assertEquals("(y + z) * (!x + y)", Minimization.getCalculationCNF(formula));

    }
    @Test
    void getCalculationCNF3() {
        String formula = "(x + y + !z) * (!x + y + z) * (!x + y + !z) * (!x + !y + z)";
        assertEquals("(y + !z) * (!x + z)", Minimization.getCalculationCNF(formula));
    }
    @Test
    void getCalculationCNF4() {
        String formula = "(x + y + !z)";
        assertEquals(formula, Minimization.getCalculationCNF(formula));

    }
    @Test
    void getCalculationCNF5() {
        String formula = "(x + y + !z) * (!x + y + z)";
        assertEquals(formula, Minimization.getCalculationCNF(formula));
    }
    @Test
    void getCalculationCNF6() {
        String formula = "(!x + y + !z) * (x + !y + !z) * (x + y + !z) * (x + y + z)";
        assertEquals("(y + !z) * (x + !z) * (x + y)", Minimization.getCalculationCNF(formula));
    }
    @Test
    void getCalculationCNF7() {
        String formula = "(!x + y + !z) * (x + !y + !z) * (x + y + !z) * (x + y + z) * (!x + !y + z)";
        assertEquals("(y + !z) * (x + !z) * (x + y) * (!x + !y + z)", Minimization.getCalculationCNF(formula));
    }
    @Test
    void getQuineMcCluskeyCNFWrong(){
        String formula = "(x + y + z) * (x + y + !z) * (x + !y + z) * (!x + y + z) * (!x + y + !z) * (!x + !y + z) * (!x + !y + !z)";
        assertEquals("(x + y) * (x + z) * (y + z) * (y + !z) * (!y + z) * (!x + y) * (!x + z) * (!x + !z) * (!x + !y)", Minimization.getQuineMcCluskeyCNF(formula));
    }
    @Test
    void getQuineMcCluskeyCNF2() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z)";
        assertEquals("(y + z) * (!x + y)", Minimization.getQuineMcCluskeyCNF(formula));

    }
    @Test
    void getQuineMcCluskeyCNF4() {
        String formula = "(x + y + !z)";
        assertEquals(formula, Minimization.getQuineMcCluskeyCNF(formula));

    }
    @Test
    void getQuineMcCluskeyCNF5() {
        String formula = "(x + y + !z) * (!x + y + z)";
        assertEquals(formula, Minimization.getQuineMcCluskeyCNF(formula));
    }
    @Test
    void getQuineMcCluskeyCNF6() {
        String formula = "(!x + y + !z) * (x + !y + !z) * (x + y + !z) * (x + y + z)";
        assertEquals("(y + !z) * (x + !z) * (x + y)", Minimization.getQuineMcCluskeyCNF(formula));
    }
    @Test
    void getQuineMcCluskeyCNF7() {
        String formula = "(!x + y + !z) * (x + !y + !z) * (x + y + !z) * (x + y + z) * (!x + !y + z)";
        assertEquals("(y + !z) * (x + !z) * (x + y) * (!x + !y + z)", Minimization.getQuineMcCluskeyCNF(formula));
    }

    @Test
    void getCalculationDNF1() {
        String formula = "(!x * !y * z) + (!x * y * !z) + (!x * y * z) + (x * y * !z)";
        assertEquals("(!x * z) + (y * !z)", Minimization.getCalculationDNF(formula));
    }

    @Test
    void getCalculationDNF2() {
        String formula = "(!x * !y * !z) + (!x * y * !z) + (!x * y * z) + (x * y * z)";
        assertEquals("(!x * !z) + (y * z)", Minimization.getCalculationDNF(formula));

    }
    @Test
    void getCalculationDNF3() {
        String formula = "(!x * !y * !z) + (!x * y * !z) + (x * y * !z) + (x * y * z)";
        assertEquals("(!x * !z) + (x * y)", Minimization.getCalculationDNF(formula));
    }
    @Test
    void getCalculationDNF4() {
        String formula = "(x + y + !z)";
        assertEquals(formula, Minimization.getCalculationDNF(formula));

    }
    @Test
    void getCalculationDNF5() {
        String formula = "(x + y + !z) * (!x + y + z)";
        assertEquals(formula, Minimization.getCalculationDNF(formula));
    }
    @Test
    void getCalculationDNF6() {
        String formula = "(!x * !y * z) + (!x * y * z) + (x * y * !z) + (x * y * z)";
        assertEquals("(!x * z) + (x * y)", Minimization.getCalculationDNF(formula));
    }
    @Test
    void getCalculationDNF7() {
        String formula = "(!x * y * !z) + (x * !y * !z) + (x * y * !z) + (x * y * z) + (!x * !y * z)";
        assertEquals("(y * !z) + (x * !z) + (x * y) + (!x * !y * z)", Minimization.getCalculationDNF(formula));
    }
    @Test
    void getQuineMcCluskeyDNF4() {
        String formula = "(x + y + !z)";
        assertEquals(formula, Minimization.getQuineMcCluskeyDNF(formula));

    }
    @Test
    void getQuineMcCluskeyDNF5() {
        String formula = "(x + y + !z) * (!x + y + z)";
        assertEquals(formula, Minimization.getQuineMcCluskeyDNF(formula));
    }
    @Test
    void getQuineMcCluskeyDNF7() {
        String formula = "(!x * y * !z) + (x * !y * !z) + (x * y * !z) + (x * y * z) + (!x * !y * z)";
        assertEquals("(y * !z) + (x * !z) + (x * y) + (!x * !y * z)", Minimization.getQuineMcCluskeyDNF(formula));
    }

    @Test
    void getVeitchKarnaughCNF1() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z) * (!x + !y + !z)";
        TruthTable table = new TruthTable(formula);
        System.out.println(table);
        assertEquals("(y + z) * (!x + !z)", Minimization.getVeitchKarnaughCNF(formula));
    }
    @Test
    void getVeitchKarnaughCNF33() {
        String formula = "(x + y + !z) * (!x + y + z) * (!x + y + !z) * (!x + !y + z)";
        assertEquals("(y + !z) * (!x + z)", Minimization.getVeitchKarnaughCNF(formula));
    }
    @Test
    void getVeitchKarnaughCNF44() {
        String formula = "(x + y + !z)";
        assertEquals(formula, Minimization.getVeitchKarnaughCNF(formula));

    }
}