import java.util.ArrayList;
import java.util.Objects;

public class TruthTable {
    private final ArrayList<ArrayList<Boolean>> table = new ArrayList<>();

    public TruthTable(String formula) {
        getTruthTable(formula);
    }

    public void addRaw(Boolean... value) {
        ArrayList<Boolean> temp = new ArrayList<>();
        int i = 0;
        while ( i < value.length) {
            temp.add(value[i++]);
        }
        table.add(temp);
    }

    public String getPDNF() {
        StringBuilder result = new StringBuilder();

        for (ArrayList<Boolean> raw : table) {
            if (raw.get(3)) {
                result.append(constructOnesConstituent(raw));
                result.append(" + ");
            }
        }
        if (result.isEmpty()) return "";

        result.delete(result.length() - 3, result.length());
        return result.toString();
    }

    public String getNumericPDNF() {
        StringBuilder result = new StringBuilder("+()");
        int counterOfRaws = 0;

        StringBuilder numericRepresentation = new StringBuilder();
        for (ArrayList<Boolean> raw : table) {
            if (raw.get(3)) {
                numericRepresentation.append(counterOfRaws).append(", ");
            }
            counterOfRaws++;
        }

        if (numericRepresentation.isEmpty()) return "";
        numericRepresentation.delete(numericRepresentation.length() - 2, numericRepresentation.length());
        result.insert(2, numericRepresentation);

        return result.toString();
    }

    public String getIndexForm() {
        StringBuilder result = new StringBuilder("f(3)");

        StringBuilder indexStringRepresentation = new StringBuilder();
        for (ArrayList<Boolean> raw  : table) {
            indexStringRepresentation.append((raw.get(3) ? '1' : '0'));
        }

        int indexOfFunction = Integer.parseInt(indexStringRepresentation.toString(), 2);

        result.insert(1, indexOfFunction);

        return result.toString();
    }

    public String getNumericPCNF() {
        StringBuilder result = new StringBuilder("*()");
        int counterOfRaws = 0;

        StringBuilder numericRepresentation = new StringBuilder();
        for (ArrayList<Boolean> raw : table) {
            if (!raw.get(3)) {
                numericRepresentation.append(counterOfRaws).append(", ");
            }
            counterOfRaws++;
        }
        if (numericRepresentation.isEmpty()) return "";
        numericRepresentation.delete(numericRepresentation.length() - 2, numericRepresentation.length());
        result.insert(2, numericRepresentation);

        return result.toString();
    }

    public String getPCNF() {
        StringBuilder result = new StringBuilder();

        for (ArrayList<Boolean> raw : table) {
            if (!raw.get(3)) {
                result.append(constructZerosConstituent(raw));
                result.append(" * ");
            }
        }
        if (result.isEmpty()) return "";
        result.delete(result.length() - 3, result.length());
        return result.toString();
    }

    private String constructOnesConstituent(ArrayList<Boolean> truthTableRaw) {
        StringBuilder result = new StringBuilder("()");
        int counterOfElementInRaw = 0;

        for (boolean element : truthTableRaw) {
            if(counterOfElementInRaw == 3) break;

            StringBuilder elementConstruction = new StringBuilder();
            elementConstruction.append(getColumnHeader(counterOfElementInRaw));
            if (!element) {
                elementConstruction.insert(0, "!");
            }

            result.insert(result.length() - 1, elementConstruction);
            if(counterOfElementInRaw != 2)
                result.insert(result.length() - 1, " * ");

            counterOfElementInRaw++;
        }
        return result.toString();
    }
    private String constructZerosConstituent(ArrayList<Boolean> truthTableRaw) {
        StringBuilder result = new StringBuilder("()");
        int counterOfElementInRaw = 0;

        for (boolean element : truthTableRaw) {
            if(counterOfElementInRaw == 3) break;

            StringBuilder elementConstruction = new StringBuilder();
            elementConstruction.append(getColumnHeader(counterOfElementInRaw));
            if (element) {
                elementConstruction.insert(0, "!");
            }

            result.insert(result.length() - 1, elementConstruction);
            if(counterOfElementInRaw != 2)
                result.insert(result.length() - 1, " + ");

            counterOfElementInRaw++;
        }
        return result.toString();
    }

    private String getColumnHeader(int counter){
        return switch (counter) {
            case 0 -> "x";
            case 1 -> "y";
            case 2 -> "z";
            default -> "";
        };
    }

    public void getTruthTable(String initFormula) {
        int numberOfRaws = (int) (Math.pow(2, 3));
        int currentRaw = 0;
        while (currentRaw != numberOfRaws) {
            StringBuilder value = new StringBuilder(Integer.toBinaryString(currentRaw));
            while (value.length() != 3) {
                value.insert(0, '0');
            }

            getTruthTableRawResult(initFormula, value.charAt(0) == '1', value.charAt(1) == '1', value.charAt(2) == '1');
            currentRaw++;
        }
    }

    public boolean getTruthTableRawResult(String formula, boolean xValue, boolean yValue, boolean zValue) {
        StringBuilder processingFormula = new StringBuilder(formula);
        int numberOfOperands = countOperandAppearance(processingFormula.toString(), 'x')
                + countOperandAppearance(processingFormula.toString(), 'y')
                + countOperandAppearance(processingFormula.toString(), 'z');

        changeCertainOperandInFormula(xValue, processingFormula, "x");
        changeCertainOperandInFormula(yValue, processingFormula, "y");
        changeCertainOperandInFormula(zValue, processingFormula, "z");

        processingFormula = checkFormulaStructure(processingFormula.toString(), numberOfOperands);

        boolean result = Evaluator.evaluate(processingFormula.toString());
        System.out.println((xValue ? "1 " : "0 ") + (yValue ? "1 " : "0 ") + (zValue ? "1 " : "0 ") + (result ? " 1" : " 0"));
        addRaw(xValue, yValue, zValue, result);

        return result;
    }

    private static StringBuilder checkFormulaStructure(String formula, int numberOfOperands) {
        StringBuilder processedFormula = new StringBuilder(formula);

        while (numberOfOperands-- > 0 ){
            for (int mainIter = 0; mainIter + 1 < processedFormula.length(); mainIter++) {
                if (removeNegation(processedFormula, mainIter)) break;
            }
        }
        placeMissingParentheses(processedFormula);

        return processedFormula;
    }

    private static boolean removeNegation(StringBuilder newStr, int position) {
        if (newStr.charAt(position) == '!' && (newStr.charAt(position + 1) == 'f' || newStr.charAt(position + 1) == 't')) {
            newStr.replace(position, position + 2, (newStr.charAt(position + 1) == 'f') ? "t" : "f");
            return true;
        }
        return false;
    }

    private static void placeMissingParentheses(StringBuilder newStr) {
        for (int placedParenthesesIter = 1; placedParenthesesIter + 1 < newStr.length(); placedParenthesesIter++) {
            if ((newStr.charAt(placedParenthesesIter-1) != '(' && newStr.charAt(placedParenthesesIter) == '!' && newStr.charAt(placedParenthesesIter + 1) == '(')) {
                newStr.insert(placedParenthesesIter, '(');
                int pairParenthesesIter = placedParenthesesIter + 2;
                int parenthesesCounter = 0;
                searchingForPairParentheses(newStr, pairParenthesesIter, parenthesesCounter);
            }
        }
    }

    private static void searchingForPairParentheses(StringBuilder newStr, int pairParenthesesIter, int parenthesesCounter) {
        while (pairParenthesesIter < newStr.length()) {
            if (newStr.charAt(pairParenthesesIter) == '(') {
                parenthesesCounter++;
                pairParenthesesIter++;
                continue;
            }
            if (newStr.charAt(pairParenthesesIter) == ')') {
                parenthesesCounter--;
                if (parenthesesCounter == 0) {
                    newStr.insert(pairParenthesesIter, ')');
                    break;
                }
                pairParenthesesIter++;
                continue;
            }
            pairParenthesesIter++;
        }
    }

    private static void changeCertainOperandInFormula(boolean value, StringBuilder newStr, String charToFind) {
        int numberOfChangesInFormula = countOperandAppearance(newStr.toString(), charToFind.charAt(0));
        while (numberOfChangesInFormula-- > 0) {
            newStr.setCharAt(newStr.indexOf(charToFind), (value ? 't': 'f'));
        }
    }

    private static int countOperandAppearance(String formula, char operand) {
        int counter = 0;
        for (char character : formula.toCharArray()) {
            if (character == operand) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruthTable table1 = (TruthTable) o;
        return Objects.equals(table, table1.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table);
    }
}
