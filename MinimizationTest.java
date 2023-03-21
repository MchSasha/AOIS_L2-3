import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinimizationTest {

    @Test
    void getCalculationCNF1() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z) * (!x + !y + !z)";
        Minimization gr = new Minimization();
        assertEquals("(y + z) * (!x + !z)", gr.getCalculationCNF(formula));
    }
    @Test
    void getCalculationCNF2() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z)";
        Minimization gr = new Minimization();
        assertEquals("(y + z) * (!x + y)", gr.getCalculationCNF(formula));

    }
    @Test
    void getCalculationCNF3() {
        String formula = "(x + y + !z) * (!x + y + z) * (!x + y + !z) * (!x + !y + z)";
        Minimization gr = new Minimization();
        assertEquals("(y + !z) * (!x + z)", gr.getCalculationCNF(formula));
    }
    @Test
    void getCalculationCNF4() {
        String formula = "(x + y + !z)";
        Minimization gr = new Minimization();
        assertEquals(formula, gr.getCalculationCNF(formula));

    }
    @Test
    void getCalculationCNF5() {
        String formula = "(x + y + !z) * (!x + y + z)";
        Minimization gr = new Minimization();
        assertEquals(formula, gr.getCalculationCNF(formula));
    }
    @Test
    void getCalculationCNF6() {
        String formula = "(!x + y + !z) * (x + !y + !z) * (x + y + !z) * (x + y + z)";
        Minimization gr = new Minimization();
        assertEquals("(y + !z) * (x + !z) * (x + y)", gr.getCalculationCNF(formula));
    }
    @Test
    void getCalculationCNF7() {
        String formula = "(!x + y + !z) * (x + !y + !z) * (x + y + !z) * (x + y + z) * (!x + !y + z)";
        Minimization gr = new Minimization();
        assertEquals("(y + !z) * (x + !z) * (x + y) * (!x + !y + z)", gr.getCalculationCNF(formula));
    }

    @Test
    void getQuineMcCluskeyCNF1(){
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z) * (!x + !y + !z)";
        Minimization gr = new Minimization();
        assertEquals("(y + z) * (!x + !z)", gr.getQuineMcCluskeyCNF(formula));
    }
    @Test
    void getQuineMcCluskeyCNF2() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z)";
        Minimization gr = new Minimization();
        assertEquals("(y + z) * (!x + y)", gr.getQuineMcCluskeyCNF(formula));

    }
    @Test
    void getQuineMcCluskeyCNF3() {
        String formula = "(x + y + !z) * (!x + y + z) * (!x + y + !z) * (!x + !y + z)";
        Minimization gr = new Minimization();
        assertEquals("(y + !z) * (!x + z)", gr.getQuineMcCluskeyCNF(formula));
    }
    @Test
    void getQuineMcCluskeyCNF4() {
        String formula = "(x + y + !z)";
        Minimization gr = new Minimization();
        assertEquals(formula, gr.getQuineMcCluskeyCNF(formula));

    }
    @Test
    void getQuineMcCluskeyCNF5() {
        String formula = "(x + y + !z) * (!x + y + z)";
        Minimization gr = new Minimization();
        assertEquals(formula, gr.getQuineMcCluskeyCNF(formula));
    }
    @Test
    void getQuineMcCluskeyCNF6() {
        String formula = "(!x + y + !z) * (x + !y + !z) * (x + y + !z) * (x + y + z)";
        Minimization gr = new Minimization();
        assertEquals("(y + !z) * (x + !z) * (x + y)", gr.getQuineMcCluskeyCNF(formula));
    }
    @Test
    void getQuineMcCluskeyCNF7() {
        String formula = "(!x + y + !z) * (x + !y + !z) * (x + y + !z) * (x + y + z) * (!x + !y + z)";
        Minimization gr = new Minimization();
        assertEquals("(y + !z) * (x + !z) * (x + y) * (!x + !y + z)", gr.getQuineMcCluskeyCNF(formula));
    }
}