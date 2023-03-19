import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinimizationTest {

    @Test
    void getSCNF1() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z) * (!x + !y + !z)";
        Minimization gr = new Minimization();
        assertEquals("(y + z) * (!x + !z)", gr.getSCNF(formula));
    }
    @Test
    void getSCNF2() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z)";
        Minimization gr = new Minimization();
        assertEquals("(y + z) * (!x + y)", gr.getSCNF(formula));

    }
    @Test
    void getSCNF3() {
        String formula = "(x + y + !z) * (!x + y + z) * (!x + y + !z) * (!x + !y + z)";
        Minimization gr = new Minimization();
        assertEquals("(y + !z) * (!x + z)", gr.getSCNF(formula));
    }
    @Test
    void getSCNF4() {
        String formula = "(x + y + !z)";
        Minimization gr = new Minimization();
        assertEquals(formula, gr.getSCNF(formula));

    }
    @Test
    void getSCNF5() {
        String formula = "(x + y + !z) * (!x + y + z)";
        Minimization gr = new Minimization();
        assertEquals(formula, gr.getSCNF(formula));
    }
}