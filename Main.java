import minimization.Minimization;
import truthtable.TruthTable;
//Д8421+6

public class Main {
    public static void main(String[] args) {

        String inputForTable = "x+y+z+m";

        buildInitialTable(inputForTable);

        getY4minimization(inputForTable);

        getY3minimization(inputForTable);

        getY2minimization(inputForTable);

        getY1Minimization(inputForTable);
    }

    private static void buildInitialTable(String initFormula) {
        int tableIter1 = 6;
        int tableIter = 0;
        TruthTable table = new TruthTable(initFormula);

        for (var row : table.getTable()) {
            row.remove(4);
        }

        while (tableIter < 10) {
            String yRow = Integer.toBinaryString(tableIter1++);

            while (yRow.length() != 4) {
                yRow = String.valueOf(new StringBuilder(yRow).insert(0, "0"));
            }

            int bitsCounter = 0;
            while (bitsCounter < 4) {
                table.getTable().get(tableIter).add(yRow.charAt(bitsCounter++) == '1');
            }
            tableIter++;
        }
        System.out.println("x y z m 4 3 2 1");
        System.out.println(table);
    }

    private static void getY1Minimization(String initFormula) {
        int tableIter1;
        int tableIter2 = 10;
        TruthTable table = new TruthTable(initFormula);
        while (tableIter2 > 0) {
            table.getTable().get(tableIter2--).set(4, false);
            table.getTable().get(tableIter2--).set(4, true);
        }
        tableIter1 = 16;
        while (tableIter1-- > 10) {
            table.getTable().get(tableIter1).set(4, null);
        }
        table.getTable().get(11).set(4, true);
        table.getTable().get(13).set(4, true);
        table.getTable().get(15).set(4, true);

        System.out.println("x y z m Y1");
        System.out.println(table);
        System.out.println("Minimization: " +  Minimization.getCalculationDNF(Minimization.getQuineMcCluskeyDNF(
                Minimization.getVeitchKarnaughDNF(initFormula, table))) + "\n");
    }

    private static void getY2minimization(String initFormula) {
        int tableIter1 = 2;
        int tableIter2 = 2;
        int tableIter4 = 2;
        TruthTable table = new TruthTable(initFormula);
        while (tableIter1-- > 0) table.getTable().get(tableIter1).set(4, true);
        while (tableIter4-- > 0) table.getTable().get(2 + tableIter4).set(4, false);
        while (tableIter2-- > 0) table.getTable().get(tableIter2 + 4).set(4, true);
        tableIter2 = 2;
        while (tableIter2-- > 0) table.getTable().get(tableIter2 + 6).set(4, false);
        tableIter2 = 2;
        while (tableIter2-- > 0) table.getTable().get(tableIter2 + 8).set(4, true);
        tableIter4 = 16;
        while (tableIter4-- > 10) table.getTable().get(tableIter4).set(4, null);
        table.getTable().get(12).set(4, true);
        table.getTable().get(13).set(4, true);
        System.out.println("x y z m Y2");
        System.out.println(table);
        System.out.println("Minimization: " +  Minimization.getCalculationDNF(Minimization.getQuineMcCluskeyDNF(
                Minimization.getVeitchKarnaughDNF(initFormula, table))) + "\n");
    }

    private static void getY3minimization(String initFormula) {
        int tableIter1;
        int tableIter2 = 2;
        int tableIter3 = 4;
        TruthTable table1 = new TruthTable(initFormula);
        while (tableIter2-- > 0) table1.getTable().get(tableIter2).set(4, true);
        while (tableIter3-- > 0) table1.getTable().get(2 + tableIter3).set(4, false);
        int tableIter4 = 4;
        while (tableIter4-- > 0) table1.getTable().get(tableIter4 + 6).set(4, true);
        tableIter1 = 16;
        while (tableIter1-- > 10) table1.getTable().get(tableIter1).set(4, null);

        table1.getTable().get(10).set(4, false);
        table1.getTable().get(11).set(4, false);
        table1.getTable().get(12).set(4, false);
        table1.getTable().get(13).set(4, false);
        System.out.println("x y z m Y3");
        System.out.println(table1);
        System.out.println("Minimization: " +  Minimization.getCalculationCNF(Minimization.getQuineMcCluskeyCNF(
                Minimization.getVeitchKarnaughCNF(initFormula, table1))) + "\n");
    }

    private static void getY4minimization(String initFormula) {
        TruthTable table = new TruthTable(initFormula);

        int tableIter1 = 2, tableIter2 = 7;
        while (tableIter1-- > 0) table.getTable().get(tableIter1).set(4, false);
        while (tableIter2-- > 0) table.getTable().get(2 + tableIter2).set(4, true);
        int tableIter3 = 16;
        while (tableIter3-- > 10) table.getTable().get(tableIter3).set(4, null);

        System.out.println("x y z m Y4");
        System.out.println(table);
        System.out.println("Minimization: " +  Minimization.getCalculationCNF(Minimization.getVeitchKarnaughCNF(initFormula, table)) + "\n");
    }
}
