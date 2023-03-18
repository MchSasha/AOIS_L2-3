import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupingTest {

    @Test
    void getSCNF1() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z) * (!x + !y + !z)";
        Grouping gr = new Grouping();
        gr.getSCNF(formula);
        System.out.println();
    }
    @Test
    void getSCNF2() {
        String formula = "(x + y + z) * (!x + y + z) * (!x + y + !z)";
        Grouping gr = new Grouping();
        gr.getSCNF(formula);
        System.out.println();

    }
    @Test
    void getSCNF3() {
        String formula = "(x + y + !z) * (!x + y + z) * (!x + y + !z) * (!x + !y + z)";
        Grouping gr = new Grouping();
        gr.getSCNF(formula);
        System.out.println();

    }
    @Test
    void getSCNF4() {
        String formula = "(x + y + !z)";
        Grouping gr = new Grouping();
        assertEquals(formula, gr.getSCNF(formula));
        System.out.println();

    }
    @Test
    void getSCNF5() {
        String formula = "(x + y + !z) * (!x + y + z)";
        Grouping gr = new Grouping();
        assertEquals(formula, gr.getSCNF(formula));
        System.out.println();

    }
}