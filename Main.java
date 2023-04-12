import minimization.Minimization;
import truthtable.TruthTable;

public class Main {
//вариант 17 --- !((!y + !z) * !(!x * !z))   !((!y + !z) + !(!x * z))     !((!y + !z) + !(!x * !z)) -- 0
    public static void main(String[] args) {

        String initFormula = "!((!y + !z) + !(!x * !z))";
        TruthTable table = new TruthTable(initFormula);
        System.out.println("Truth table --- \n" + table);
        String pdnf = table.getPDNF();
        String pcnf = table.getPCNF();
        System.out.println("PDNF --- " + pdnf);
        System.out.println("PCNF --- " + pcnf);

        System.out.println(" Veitch-Karnaugh Method --- ");
        System.out.println(Minimization.getVeitchKarnaughDNF(pdnf));
        System.out.println(Minimization.getVeitchKarnaughCNF(pcnf));
    }
}

/*(a + b + p) * (a +b + !p) * (a + !b + p) * (!a + b + p)*/