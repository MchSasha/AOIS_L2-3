public class Main {
//вариант 17 --- !((!y | !z) & !(!x & !z))
    public static void main(String[] args) {

        String initFormula = "!((!x + !y) * !(!x * !z))";

        TruthTable table = new TruthTable(initFormula);
        System.out.println(table.getPDNF());
        System.out.println(table.getPCNF());
    }
}