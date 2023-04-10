import minimization.Minimization;
import truthtable.TruthTable;

public class Main {
//вариант 17 --- !((!y + !z) * !(!x * !z))   !((!y + !z) + !(!x * z))     !((!y + !z) + !(!x * !z)) -- 0
    public static void main(String[] args) {

        String initFormula = "((y * z) * (x * !z))";

        TruthTable table = new TruthTable(initFormula);
        System.out.println("Truth table --- \n" + table);
        String PDNF = table.getPDNF();
        String PCNF = table.getPCNF();
        System.out.println("PDNF --- " + PDNF);
        System.out.println("PCNF --- " + PCNF);

        System.out.println(" Veitch-Karnaugh Method --- ");
        System.out.println(Minimization.getVeitchKarnaughDNF(PCNF));
        System.out.println(Minimization.getVeitchKarnaughCNF(PDNF));
    }
}