public class Main {
//вариант 17 --- !((!y + !z) * !(!x * !z))
    public static void main(String[] args) {

        String initFormula = "!((!y + !z) * !(!x * !z))";

        TruthTable table = new TruthTable(initFormula);
        System.out.println("PDNF --- " + table.getPDNF());
        System.out.println("PCNF --- " + table.getPCNF());
        System.out.println("Numeric form PDNF --- " + table.getNumericPDNF());
        System.out.println("Numeric form PCNF --- " + table.getNumericPCNF());
        System.out.println("Index form --- " + table.getIndexForm());

        System.out.println();
        TruthTable table5 = new TruthTable("(y + z)");
        System.out.println("PDNF --- " + table5.getPDNF());
        System.out.println("PCNF --- " + table5.getPCNF());
        System.out.println("Numeric form PDNF --- " + table5.getNumericPDNF());
        System.out.println("Numeric form PCNF --- " + table5.getNumericPCNF());
        System.out.println("Index form --- " + table5.getIndexForm());
    }
}