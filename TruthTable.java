import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TruthTable {
    private final List<List<Boolean>> table = new ArrayList<>();
    private Set<String> operands;

    public void addRaw(List<Boolean> value) {
        table.add(value);
    }


    public TruthTable(String formula) {
        getTruthTable(formula);
    }

    public String getPDNF() {
        StringBuilder result = new StringBuilder();
        int numberOfOperands = operands.size();

        for (List<Boolean> raw : table) {
            if (raw.get(numberOfOperands)) {
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
        int counterOfRaws = 0, numberOfOperands = operands.size();

        StringBuilder numericRepresentation = new StringBuilder();
        for (List<Boolean> raw : table) {
            if (raw.get(numberOfOperands)) {
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
        int numberOfOperands = operands.size();
        StringBuilder result = new StringBuilder("f()");
        result.insert(2, numberOfOperands);

        StringBuilder indexStringRepresentation = new StringBuilder();
        for (List<Boolean> raw  : table) {
            indexStringRepresentation.append((raw.get(numberOfOperands) ? '1' : '0'));
        }

        int indexOfFunction = Integer.parseInt(indexStringRepresentation.toString(), 2);

        result.insert(1, indexOfFunction);

        return result.toString();
    }

    public String getNumericPCNF() {
        int numberOfOperands = operands.size();
        StringBuilder result = new StringBuilder("*()");
        int counterOfRaws = 0;

        StringBuilder numericRepresentation = new StringBuilder();
        for (List<Boolean> raw : table) {
            if (!raw.get(numberOfOperands)) {
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
        int numberOfOperands = operands.size();

        for (List<Boolean> raw : table) {
            if (!raw.get(numberOfOperands)) {
                result.append(constructZerosConstituent(raw, operands));
                result.append(" * ");
            }
        }
        if (result.isEmpty()) return "";
        result.delete(result.length() - 3, result.length());
        return result.toString();
    }

    private String constructOnesConstituent(List<Boolean> truthTableRaw) {
        StringBuilder result = new StringBuilder("()");
        int numberOfOperands = operands.size();
        int counterOfElementInRaw = 0;

        for (boolean element : truthTableRaw) {
            if(counterOfElementInRaw == numberOfOperands) break;

            StringBuilder elementConstruction = new StringBuilder();
            elementConstruction.append(getColumnHeader(counterOfElementInRaw, operands));
            if (!element) {
                elementConstruction.insert(0, "!");
            }

            result.insert(result.length() - 1, elementConstruction);
            if(counterOfElementInRaw != numberOfOperands - 1)
                result.insert(result.length() - 1, " * ");

            counterOfElementInRaw++;
        }
        return result.toString();
    }
    private String constructZerosConstituent(List<Boolean> truthTableRaw, Set<String> operands) {
        StringBuilder result = new StringBuilder("()");
        int numberOfOperands = operands.size();
        int counterOfElementInRaw = 0;

        for (boolean element : truthTableRaw) {
            if(counterOfElementInRaw == numberOfOperands) break;

            StringBuilder elementConstruction = new StringBuilder();
            elementConstruction.append(getColumnHeader(counterOfElementInRaw, operands));
            if (element) {
                elementConstruction.insert(0, "!");
            }

            result.insert(result.length() - 1, elementConstruction);
            if(counterOfElementInRaw != numberOfOperands - 1)
                result.insert(result.length() - 1, " + ");

            counterOfElementInRaw++;
        }
        return result.toString();
    }

    private String getColumnHeader(int counter, Set<String> operands){
        return operands.stream().toList().get(counter);
    }

    public void getTruthTable(String initFormula) {
        this.operands = getOperands(initFormula);
        int numberOfOperands = operands.size();
        int numberOfRaws = (int) (Math.pow(2, numberOfOperands));
        int currentRaw = 0;

        while (currentRaw != numberOfRaws) {
            StringBuilder rawValue = new StringBuilder(Integer.toBinaryString(currentRaw));
            while (rawValue.length() != numberOfOperands) {
                rawValue.insert(0, '0');
            }

            List<Boolean> valuesInRaw = new ArrayList<>();
            int positionInRaw = 0;
            while (positionInRaw < numberOfOperands) {
                valuesInRaw.add(rawValue.charAt(positionInRaw++) == '1');
            }

            getTruthTableRawResult(initFormula, valuesInRaw, operands);
            currentRaw++;
        }
    }

    private static Set<String > getOperands(String initFormula) {
        Set<String> operands = new TreeSet<>();
        for (Character operand : initFormula.toCharArray()) {
            if (operand == ' ' || operand == '(' || operand == ')' || operand == '!' || operand == '+' || operand == '*') {
                continue;
            }
            operands.add(operand.toString());
        }
        return operands;
    }

    private void getTruthTableRawResult(String formula, List<Boolean> values, Set<String> operands) {
        StringBuilder processingFormula = new StringBuilder(formula);
        int numberOfUniqueOperands = values.size(), numberOfAllOperandsAppearance = 0;
        int positionInRaw = 0;

        while (positionInRaw < numberOfUniqueOperands) {
            numberOfAllOperandsAppearance += countOperandAppearance(processingFormula.toString(), operands.stream().toList().get(positionInRaw).charAt(0));

            changeCertainOperandInFormula(values.get(positionInRaw), processingFormula, operands.stream().toList().get(positionInRaw++));
        }

        processingFormula = checkFormulaStructure(processingFormula.toString(), numberOfAllOperandsAppearance);

        boolean result = Evaluator.evaluate(processingFormula.toString());
        values.add(result);
        addRaw(values);

        System.out.println(getStringRaw(values));
    }

    @NotNull
    private static StringBuilder getStringRaw(List<Boolean> values) {
        StringBuilder toPrint = new StringBuilder();
        for (Boolean value : values) {
            toPrint.append(value ? "1 " : "0 ");
        }
        return toPrint;
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
