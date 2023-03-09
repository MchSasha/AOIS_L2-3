public class Main {
//вариант 17 --- !((!y | !z) & !(!x & !z))
    public static void main(String[] args) {

        String initFormula = "!(!(x + !y) * !(!x * !z))";

        TruthTable table = new TruthTable(initFormula);
        System.out.println("PDNF --- " + table.getPDNF());
        System.out.println("PCNF --- " + table.getPCNF());
        System.out.println("Numeric form PDNF --- " + table.getNumericPDNF());
        System.out.println("Numeric form PCNF --- " + table.getNumericPCNF());
        System.out.println("Index form --- " + table.getIndexForm());
    }
}