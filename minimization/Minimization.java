package minimization;
import truthtable.TruthTable;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Minimization {

    public static String getVeitchKarnaughCNF(String pcnf) {
        if (pcnf.isEmpty()) return "";
        TruthTable formulaTruthTable = new TruthTable(pcnf);
        if (Objects.equals(formulaTruthTable.getIndexForm(), TruthTable.ZERO_INDEX)) return String.valueOf(0);
        if (Objects.equals(formulaTruthTable.getIndexForm(), TruthTable.ONE_INDEX)) return String.valueOf(1);

        int numberOfOperands = formulaTruthTable.getOperandsNumber();
        int columnOperandsNumber = numberOfOperands / 2;
        int rowOperandsNumber = numberOfOperands - columnOperandsNumber;
        String groupOperator = " \\+ ";

        List<List<Boolean>> veitchKarnaughTable = getVeitchKarnaughTable(formulaTruthTable, numberOfOperands, columnOperandsNumber, rowOperandsNumber);

        List<List<String>> shortenedForm = new ArrayList<>();
        List<List<String>> combinations = new ArrayList<>();
        Set<List<Integer>> visitedFields = new HashSet<>();

        findAllShapesInTable(numberOfOperands, veitchKarnaughTable, combinations, visitedFields, false);

        getShortenedForm(formulaTruthTable, shortenedForm, combinations, false);

        if (shortenedForm.isEmpty()) {
            return pcnf;
        }

        return constructFormula(shortenedForm, groupOperator);
    }
    public static String getVeitchKarnaughDNF(String pdnf) {
        if (pdnf.isEmpty()) return "";
        TruthTable formulaTruthTable = new TruthTable(pdnf);
        if (Objects.equals(formulaTruthTable.getIndexForm(), TruthTable.ZERO_INDEX)) return String.valueOf(0);
        if (Objects.equals(formulaTruthTable.getIndexForm(), TruthTable.ONE_INDEX)) return String.valueOf(1);

        int numberOfOperands = formulaTruthTable.getOperandsNumber();
        int columnOperandsNumber = numberOfOperands / 2;
        int rowOperandsNumber = numberOfOperands - columnOperandsNumber;
        String groupOperator = " * ";

        List<List<Boolean>> veitchKarnaughTable = getVeitchKarnaughTable(formulaTruthTable, numberOfOperands, columnOperandsNumber, rowOperandsNumber);

        List<List<String>> shortenedForm = new ArrayList<>();
        List<List<String>> combinations = new ArrayList<>();
        Set<List<Integer>> visitedFields = new HashSet<>();

        findAllShapesInTable(numberOfOperands, veitchKarnaughTable, combinations, visitedFields, true);

        getShortenedForm(formulaTruthTable, shortenedForm, combinations, true);

        if (shortenedForm.isEmpty()) {
            return pdnf;
        }

        return constructFormula(shortenedForm, groupOperator);
    }

    private static void getShortenedForm(TruthTable formulaTruthTable, List<List<String>> shortenedForm, List<List<String>> allCombinationsOfShapes,
                                         boolean value) {
        char valueToConvert = (value) ? '0' : '1';

        for (var combinationsInShape : allCombinationsOfShapes) {
            List<String> implicant = new ArrayList<>();

            for (int index = 0; index < combinationsInShape.get(0).length(); index++) {
                boolean equality = isSameValueInAllCombinations(combinationsInShape, index);
                if (equality) {
                    String variable = constructVariable(formulaTruthTable, valueToConvert, combinationsInShape, index);
                    implicant.add(variable);
                }
            }
            shortenedForm.add(implicant);
        }
    }

    private static boolean isSameValueInAllCombinations(List<String> combinationsInShape, int index) {
        boolean equality = true;
        for (int indexInGroup = 1; indexInGroup < combinationsInShape.size(); indexInGroup++) {
            if (combinationsInShape.get(indexInGroup).charAt(index) != combinationsInShape.get(0).charAt(index)) {
                equality = false;
                break;
            }
        }
        return equality;
    }

    private static String constructVariable(TruthTable formulaTruthTable, char toConvert, List<String> combinationsInShape, int index) {
        StringBuilder variable = new StringBuilder(formulaTruthTable.getOperands().stream().toList().get(index));

        if (combinationsInShape.get(0).charAt(index) == toConvert) {
            variable.insert(0, '!');
        }
        return String.valueOf(variable);
    }

    private static void findAllShapesInTable(int numberOfOperands, List<List<Boolean>> veitchKarnaughTable, List<List<String>> combinations,
                                             Set<List<Integer>> visitedFields, boolean value) {
        int maxPower = (int) Math.pow(2, numberOfOperands - 1);
        int minPower = maxPower / 2;
        int numberOfCertainValueInTable = getNumberOfCertainValue(veitchKarnaughTable, value);

        while (visitedFields.size() != numberOfCertainValueInTable) {
            findHorizontalRectangles(maxPower, veitchKarnaughTable, combinations, visitedFields, value);

            findSquares(minPower, veitchKarnaughTable, combinations, visitedFields, value);

            findVerticalRectangles(minPower, veitchKarnaughTable, combinations, visitedFields, value);
            findHorizontalRectangles(minPower, veitchKarnaughTable, combinations, visitedFields, value);

            findFurtherField(veitchKarnaughTable, combinations, visitedFields, value);

            maxPower /= 2;
        }
    }

    private static int getNumberOfCertainValue(List<List<Boolean>> veitchKarnaughTable, boolean value) {
        int valueCounter = 0;

        for (List<Boolean> row : veitchKarnaughTable) {
            for (boolean currentValue : row) {
                if (currentValue == value) {
                    valueCounter++;
                }
            }
        }

        return valueCounter;
    }

    public static void findFurtherField(List<List<Boolean>> veitchKarnaughTable, List<List<String>> combinationsToGlue,
                                        Set<List<Integer>> visitedFields, boolean value) {
        int numOfRows = veitchKarnaughTable.size();
        int numOfColumns = veitchKarnaughTable.get(0).size();

        for (int row = 0; row < numOfRows; row++) {
            for (int column = 0; column < numOfColumns; column++) {
                if (veitchKarnaughTable.get(row).get(column) != value) continue;

                List<Integer> field = List.of(row, column);

                if (visitedFields.contains(field))  continue;

                visitedFields.add(field);

                String combination = getGreyCode(row, 1) + getGreyCode(column, 2);
                combinationsToGlue.add(Collections.singletonList(combination));
            }
        }
    }

    public static void findVerticalRectangles(int degree, List<List<Boolean>> veitchKarnaughTable, List<List<String>> combinationsToGlue,
                                              Set<List<Integer>> visitedFields, boolean value) {
        int numOfRows = veitchKarnaughTable.size();
        int numOfColumns = veitchKarnaughTable.get(0).size();

        for (int row = 0; row < numOfRows; row++) {
            for (int column = 0; column < numOfColumns; column++) {
                if (veitchKarnaughTable.get(row).get(column) != value) continue;

                List<List<Integer>> rectangle = isVerticalRectangle(degree, row, column, numOfRows, numOfColumns, veitchKarnaughTable, value);

                if (rectangle.isEmpty()) continue;
                if (visitedFields.containsAll(rectangle)) continue;

                visitedFields.addAll(rectangle);

                List<String> combinationsOfCertainShape = new ArrayList<>();
                for(var rec : rectangle){
                    String combination = getGreyCode(rec.get(0), 1) + getGreyCode(rec.get(1), 2);
                    combinationsOfCertainShape.add(combination);
                }
                combinationsToGlue.add(combinationsOfCertainShape);
            }
        }
        removeDuplicates(combinationsToGlue);
    }

    private static List<List<Integer>> isVerticalRectangle(int degree, int startRow, int startCol, int numRows, int numCols,
                                                           List<List<Boolean>> veitchKarnaughTable, boolean value) {
        if (startRow + degree > numRows || startCol > numCols) return new ArrayList<>();

        List<List<Integer>> rectangle = new ArrayList<>();
        for (int row = startRow; row < startRow + degree; row++) {
            for (int col = startCol; col <= startCol; col++) {
                if (veitchKarnaughTable.get(row).get(col) != value) return new ArrayList<>();

                int colInExtendedTable = col;
                if (colInExtendedTable >= veitchKarnaughTable.get(0).size()) {
                    colInExtendedTable -= veitchKarnaughTable.get(0).size();
                }

                rectangle.add(List.of(row, colInExtendedTable));
            }
        }
        return rectangle;
    }


    public static void findHorizontalRectangles(int degree, List<List<Boolean>> veitchKarnaughTable, List<List<String>> combinationsOfCertainShape,
                                                Set<List<Integer>> visitedFields, boolean value) {
        List<List<Boolean>> extendedTable = createExtendedTable(veitchKarnaughTable);

        int numRows = extendedTable.size();
        int numCols = extendedTable.get(0).size();

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (extendedTable.get(row).get(col) != value) continue;

                List<List<Integer>> rectangle = isHorizontalRectangle(degree, row, col, numRows, numCols, veitchKarnaughTable, value);

                if (rectangle.isEmpty()) continue;
                if (visitedFields.containsAll(rectangle)) continue;

                visitedFields.addAll(rectangle);
                List<String> combination = new ArrayList<>();
                for(var rec : rectangle){
                    combination.add(getGreyCode(rec.get(0), 1) + getGreyCode(rec.get(1), 2));
                }
                combinationsOfCertainShape.add(combination);

            }
        }
        removeDuplicates(combinationsOfCertainShape);
    }

    private static List<List<Boolean>> createExtendedTable(List<List<Boolean>> veitchKarnaughTable) {
        List<List<Boolean>> table = new ArrayList<>();
        for (List<Boolean> innerList : veitchKarnaughTable) {
            List<Boolean> copiedInnerList = new ArrayList<>(innerList);
            table.add(copiedInnerList);
        }

        for (var row : table) {
            row.add(row.get(0));
        }
        return table;
    }

    private static void removeDuplicates(List<List<String>> combinationsOfCertainShape) {
        List<List<String>> toRemove = new ArrayList<>();
        for (int iter = 0; iter < combinationsOfCertainShape.size(); iter++) {
            List<List<String>> incompleteCombinations = new ArrayList<>(combinationsOfCertainShape);
            incompleteCombinations.remove(iter);

            Set<String> combinations = new HashSet<>();
            for (var combination : incompleteCombinations) {
                combinations.addAll(combination);
            }

            if (combinations.containsAll(combinationsOfCertainShape.get(iter))) {
                toRemove.add(combinationsOfCertainShape.get(iter));
            }
        }
        for (var extra : toRemove) {
            combinationsOfCertainShape.remove(extra);
        }
    }

    public static void findSquares(int degree, List<List<Boolean>> veitchKarnaughTable, List<List<String>> combinationsOfCertainShape,
                                   Set<List<Integer>> visitedFields, boolean value) {
        List<List<Boolean>> extendedTable = createExtendedTable(veitchKarnaughTable);

        int numRows = extendedTable.size();
        int numCols = extendedTable.get(0).size();

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (extendedTable.get(row).get(col) != value) continue;
                List<List<Integer>> rectangle = isSquare(degree, row, col, numRows, numCols, veitchKarnaughTable, value);

                if (rectangle.isEmpty())  continue;

                visitedFields.addAll(rectangle);
                List<String> combination = new ArrayList<>();
                for(var rec : rectangle){
                    combination.add(getGreyCode(rec.get(0), 1) + getGreyCode(rec.get(1), 2));
                }
                combinationsOfCertainShape.add(combination);
            }
        }
        removeDuplicates(combinationsOfCertainShape);
    }

    private static List<List<Integer>> isHorizontalRectangle(int degree, int startRow, int startCol, int numRows, int numCols,
                                                             List<List<Boolean>> veitchKarnaughTable, boolean value) {
        if (startRow > numRows || startCol + degree > numCols) return new ArrayList<>();
        List<List<Boolean>> extendedTable = createExtendedTable(veitchKarnaughTable);
        List<List<Integer>> rectangle = new ArrayList<>();

        for (int row = startRow; row <= startRow; row++) {
            for (int col = startCol; col < startCol + degree; col++) {
                if (extendedTable.get(row).get(col) != value) return new ArrayList<>();

                int rowInExtendedTable = row;
                int colInExtendedTable = col;
                if (rowInExtendedTable >= veitchKarnaughTable.size()) {
                    rowInExtendedTable -= veitchKarnaughTable.size();
                }
                if (colInExtendedTable >= veitchKarnaughTable.get(0).size()) {
                    colInExtendedTable -= veitchKarnaughTable.get(0).size();
                }

                rectangle.add(List.of(rowInExtendedTable, colInExtendedTable));
            }
        }
        return rectangle;
    }

    private static List<List<Integer>> isSquare(int degree, int startRow, int startCol, int numRows, int numCols, List<List<Boolean>> veitchKarnaughTable,
                                                boolean value) {
        if (startRow + degree > numRows || startCol + degree > numCols) return new ArrayList<>();

        List<List<Boolean>> extendedTable = createExtendedTable(veitchKarnaughTable);
        List<List<Integer>> rectangle = new ArrayList<>();

        for (int row = startRow; row < startRow + degree; row++) {
            for (int col = startCol; col < startCol + degree; col++) {
                if (extendedTable.get(row).get(col) != value) return new ArrayList<>();

                int rowInExtendedTable = row;
                int colInExtendedTable = col;
                if (rowInExtendedTable >= veitchKarnaughTable.size()) {
                    rowInExtendedTable -= veitchKarnaughTable.size();
                }
                if (colInExtendedTable >= veitchKarnaughTable.get(0).size()) {
                    colInExtendedTable -= veitchKarnaughTable.get(0).size();
                }

                rectangle.add(List.of(rowInExtendedTable, colInExtendedTable));
            }
        }
        return rectangle;
    }

    private static List<List<Boolean>> getVeitchKarnaughTable(TruthTable formulaTruthTable, int numberOfOperands, int columnOperandsNumber,
                                                              int rowOperandsNumber) {
        List<List<Boolean>> veitchKarnaughTable = new ArrayList<>();
        int columnSize = (int) Math.pow(2, columnOperandsNumber);
        int rowSize = (int) Math.pow(2, rowOperandsNumber);

        for (int columnIter = 0; columnIter < columnSize; columnIter++) {
            String rowHeaderInGreyCode = getGreyCode(columnIter, columnOperandsNumber);
            List<Boolean> rowInVeitchKarnaughTable = new ArrayList<>();

            for (int rowIter = 0; rowIter < rowSize; rowIter++) {
                String columnHeaderInGreyCode = getGreyCode(rowIter, rowOperandsNumber);
                String valuesCombination = rowHeaderInGreyCode + columnHeaderInGreyCode;
                int indexOfCombinationInTruthTable = Integer.parseInt(valuesCombination, 2);

                boolean valuesCombinationResult = formulaTruthTable.getRow(indexOfCombinationInTruthTable).get(numberOfOperands);

                rowInVeitchKarnaughTable.add(valuesCombinationResult);
            }
            veitchKarnaughTable.add(rowInVeitchKarnaughTable);
        }
        return veitchKarnaughTable;
    }

    public static String getGreyCode(int myNum, int numOfBits) {
        if (numOfBits <= 1) {
            return String.valueOf(myNum);
        }

        if (myNum >= Math.pow(2, (numOfBits - 1))) {
            return "1" + getGreyCode((int)(Math.pow(2, (numOfBits))) - myNum - 1, numOfBits - 1);
        } else {
            return "0" + getGreyCode(myNum, numOfBits - 1);
        }
    }


    public static String getQuineMcCluskeyCNF(String pcnf) {
        String groupOperator = " \\+ ";
        List<List<String>> constituentOperands = getOperands(pcnf, groupOperator);
        List<List<List<String>>> allGroupingVariants = getAllGroupingVariants(constituentOperands);
        List<List<String>> shortenedForm = getShortenedForm(allGroupingVariants, constituentOperands);

        if (shortenedForm.isEmpty()) {
            return pcnf;
        }

        List<List<Boolean>> quineMcCluskeyTable = getQuineMcCluskeyTable(constituentOperands, shortenedForm);

        calculateDeadEndForm(shortenedForm, quineMcCluskeyTable);

        shortenedForm.addAll(getIrreducibleConstituents(constituentOperands, shortenedForm));

        return constructFormula(shortenedForm, groupOperator);
    }

    private static void calculateDeadEndForm(List<List<String>> shortenedForm, List<List<Boolean>> quineMcCluskeyTable) {
        List<Integer> indexes = new ArrayList<>();
        for (int rowIter = 0; rowIter < quineMcCluskeyTable.size(); rowIter++) {
            List<List<Boolean>> incompleteVariantOfTable = new ArrayList<>(quineMcCluskeyTable);
            incompleteVariantOfTable.remove(rowIter);

            if (hasEmptyColumns(incompleteVariantOfTable)) {
                continue;
            }
            indexes.add(rowIter);
        }

        for (int i = indexes.size() - 1; i >= 0; i--) {
            shortenedForm.remove(indexes.get(i));
        }
    }

    private static Boolean hasEmptyColumns(List<List<Boolean>> incompleteVariantOfTable) {
        for (int columnIter = 0; columnIter < incompleteVariantOfTable.get(0).size(); columnIter++) {

            int emptyFieldCounter = 0;
            for (List<Boolean> row : incompleteVariantOfTable) {
                if (row.get(columnIter)) {
                    break;
                }
                emptyFieldCounter++;
            }
            if (emptyFieldCounter == incompleteVariantOfTable.size()) {
                return true;
            }
        }
        return false;
    }

    private static List<List<Boolean>> getQuineMcCluskeyTable(List<List<String>> constituentOperands, List<List<String>> shortenedForm) {
        List<List<Boolean>> quineMcCluskeyTable = new ArrayList<>();

        for (List<String> implicant : shortenedForm) {
            List<Boolean> row = new ArrayList<>();

            for (List<String> constituent : constituentOperands) {
                boolean isContained = new HashSet<>(constituent).containsAll(implicant);
                row.add(isContained);
            }
            quineMcCluskeyTable.add(row);
        }
        return quineMcCluskeyTable;
    }


    public static String getCalculationCNF(String pcnf) {
        String groupOperator = " \\+ ";
        List<List<String>> constituentOperands = getOperands(pcnf, groupOperator);
        List<List<List<String>>> allGroupingVariants = getAllGroupingVariants(constituentOperands);
        List<List<String>> shortenedForm = getShortenedForm(allGroupingVariants, constituentOperands);

        if (shortenedForm.isEmpty()) {
            return pcnf;
        }

        String referenceFormulaResult = new TruthTable(pcnf).getIndexForm();
        calculateDeadEndForm(shortenedForm, groupOperator, referenceFormulaResult);

        shortenedForm.addAll(getIrreducibleConstituents(constituentOperands, shortenedForm));

        return constructFormula(shortenedForm, groupOperator);
    }

    private static List<List<String>> getIrreducibleConstituents(List<List<String>> constituentOperands, List<List<String>> shortenedForm) {
        List<List<String>> irreducibleConstituents = new ArrayList<>();

        for (List<String> constituent : constituentOperands) {
            for (int iter = 0; iter < shortenedForm.size(); iter++) {
                List<String> implicant = shortenedForm.get(iter);

                if (new HashSet<>(constituent).containsAll(implicant)) break;

                if(iter+1 == shortenedForm.size()) irreducibleConstituents.add(constituent);
            }
        }

        return irreducibleConstituents;
    }

    private static void calculateDeadEndForm(List<List<String>> shortenedForm, String groupOperator, String referenceFormulaResult) {
        for (int iter = 0; iter < shortenedForm.size(); iter++) {
            List<List<String>> incompleteVariant = new ArrayList<>(shortenedForm);
            incompleteVariant.remove(iter);

            String incompleteFormula = constructFormula(incompleteVariant, groupOperator);
            String incompleteFormulaResult = new TruthTable(incompleteFormula).getIndexForm();

            if (Objects.equals(incompleteFormulaResult, referenceFormulaResult)) {
                shortenedForm.remove(iter);
            }
        }
    }

    private static String constructFormula(List<List<String>> operands, String groupOperator) {
        if(operands.isEmpty()) return "";
        groupOperator = (groupOperator.equals(" \\+ ")) ? " + " : " * ";
        String outerOperator = (Objects.equals(groupOperator, " + ")) ? " * " : " + ";
        StringBuilder result = new StringBuilder();

        for (List<String> constituent : operands) {
            StringBuilder constituentResult = new StringBuilder();

            for (String operand : constituent) {
                constituentResult.append(operand).append(groupOperator);
            }
            constituentResult.delete(constituentResult.length() - groupOperator.length(), constituentResult.length());
            constituentResult.insert(0, '(').insert(constituentResult.length(), ')');

            result.append(constituentResult).append(outerOperator);
        }
        result.delete(result.length() - outerOperator.length(), result.length());

        return result.toString();
    }


    private static List<List<String>> getShortenedForm(List<List<List<String>>> allGroupingVariants, List<List<String>> constituentOperands) {
        List<List<String>> duplicates = new ArrayList<>();
        int size = allGroupingVariants.size();

        for (int currentIter = 0; currentIter < size - 1; currentIter++) {
            List<List<String>> currentConstituentVariations = allGroupingVariants.get(currentIter);
            List<String> currentGroup = constituentOperands.get(currentIter);

            for (int otherIter = currentIter + 1; otherIter < size; otherIter++) {
                List<List<String>> otherConstituentVariations = allGroupingVariants.get(otherIter);
                List<String> otherGroup = constituentOperands.get(otherIter);

                findDuplicates(currentConstituentVariations, otherConstituentVariations, currentGroup, otherGroup, duplicates);
            }
        }
        return new ArrayList<>(new LinkedHashSet<>(duplicates));
    }

    private static void findDuplicates(List<List<String>> currentConstituent, List<List<String>> otherConstituent,
                                       List<String> currentGroup, List<String> otherGroup, List<List<String>> duplicates) {
        for (List<String> potentialDuplicate : currentConstituent) {
            if (otherConstituent.contains(potentialDuplicate)) {
                int indexOfCurrentRemainingOperand = currentConstituent.indexOf(potentialDuplicate);
                int indexOfOtherRemainingOperand = otherConstituent.indexOf(potentialDuplicate);
                String currentRemainingOperand = currentGroup.get(indexOfCurrentRemainingOperand);
                String otherRemainingOperand = otherGroup.get(indexOfOtherRemainingOperand);

                if (!Objects.equals(currentRemainingOperand, otherRemainingOperand)) {
                    duplicates.add(potentialDuplicate);
                }
            }
        }
    }

    private static List<List<List<String>>> getAllGroupingVariants(List<List<String>> constituentOperands) {
        List<List<List<String>>> allGroupingVariants = new ArrayList<>();

        for (var constituent : constituentOperands) {
            List<List<String>> groupingVariants;
            groupingVariants = getGroupingVariants(constituent);

            allGroupingVariants.add(groupingVariants);
        }
        return allGroupingVariants;
    }

    private static List<List<String>> getGroupingVariants(List<String> constituent) {
        List<List<String>> variants = new ArrayList<>();

        for (int iter = 0; iter < constituent.size(); iter++) {
            List<String> variant = new ArrayList<>(constituent);
            variant.remove(iter);
            variants.add(variant);
        }
        return variants;
    }

    private static List<List<String>> getOperands(String formula, String operator) {
        int groupCounter = 0;
        List<List<String>> constituentOperands = new ArrayList<>();

        while(groupCounter < formula.length()) {
            String constituent = removeParentheses(formula, groupCounter);

            if (isEndOfTheString(formula, groupCounter, constituent)) break;

            groupCounter += constituent.length() + 2;

            constituent = constituent.trim();
            constituent = constituent.replaceAll(operator, " ");
            String[] operands = constituent.split(" ");
            constituentOperands.add(Arrays.stream(operands).toList());
        }
        return constituentOperands;
    }

    private static boolean isEndOfTheString(String formula, int groupCounter, String constituent) {
        return groupCounter + constituent.length() + 2 >= formula.length();
    }

    @NotNull
    private static String removeParentheses(String formula, int groupCounter) {
        return formula.substring(formula.indexOf('(', groupCounter) + 1, formula.indexOf(')', formula.indexOf('(', groupCounter) + 1));
    }

    public static String getCalculationDNF(String pdnf) {
        String groupOperator = " \\* ";
        List<List<String>> constituentOperands = getOperands(pdnf, groupOperator);
        List<List<List<String>>> allGroupingVariants = getAllGroupingVariants(constituentOperands);
        List<List<String>> shortenedForm = getShortenedForm(allGroupingVariants, constituentOperands);

        if (shortenedForm.isEmpty()) {
            return pdnf;
        }

        String referenceFormulaResult = new TruthTable(pdnf).getIndexForm();
        calculateDeadEndForm(shortenedForm, groupOperator, referenceFormulaResult);

        shortenedForm.addAll(getIrreducibleConstituents(constituentOperands, shortenedForm));

        return constructFormula(shortenedForm, groupOperator);
    }
    public static String getQuineMcCluskeyDNF(String pdnf) {
        String groupOperator = " \\* ";
        List<List<String>> constituentOperands = getOperands(pdnf, groupOperator);
        List<List<List<String>>> allGroupingVariants = getAllGroupingVariants(constituentOperands);
        List<List<String>> shortenedForm = getShortenedForm(allGroupingVariants, constituentOperands);

        if (shortenedForm.isEmpty()) {
            return pdnf;
        }

        List<List<Boolean>> quineMcCluskeyTable = getQuineMcCluskeyTable(constituentOperands, shortenedForm);

        calculateDeadEndForm(shortenedForm, quineMcCluskeyTable);

        shortenedForm.addAll(getIrreducibleConstituents(constituentOperands, shortenedForm));

        return constructFormula(shortenedForm, groupOperator);
    }
}
