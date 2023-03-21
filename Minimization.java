import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Minimization {
    public String getQuineMcCluskeyCNF(String PCNF) {
        String groupOperator = " \\+ ";
        List<List<String>> constituentOperands = getOperands(PCNF, groupOperator);
        List<List<List<String>>> allGroupingVariants = getAllGroupingVariants(constituentOperands);
        List<List<String>> shortenedForm = getShortenedForm(allGroupingVariants, constituentOperands);

        if (shortenedForm.isEmpty()) {
            return PCNF;
        }

        List<List<Boolean>> quineMcCluskeyTable = getQuineMcCluskeyTable(constituentOperands, shortenedForm);

        calculateDeadEndForm(shortenedForm, quineMcCluskeyTable);

        shortenedForm.addAll(getIrreducibleConstituents(constituentOperands, shortenedForm));

        return constructFormula(shortenedForm, groupOperator);
    }

    private void calculateDeadEndForm(List<List<String>> shortenedForm, List<List<Boolean>> quineMcCluskeyTable) {
        for (int rowIter = 0; rowIter < quineMcCluskeyTable.size(); rowIter++) {
            List<List<Boolean>> incompleteVariantOfTable = new ArrayList<>(quineMcCluskeyTable);
            incompleteVariantOfTable.remove(rowIter);

            if (hasEmptyColumns(incompleteVariantOfTable)) {
                continue;
            }
            shortenedForm.remove(rowIter);
        }
    }

    private Boolean hasEmptyColumns(List<List<Boolean>> incompleteVariantOfTable) {
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

    private List<List<Boolean>> getQuineMcCluskeyTable(List<List<String>> constituentOperands, List<List<String>> shortenedForm) {
        List<List<Boolean>> quineMcCluskeyTable = new ArrayList<>();

        for (List<String> implicant : shortenedForm) {
            quineMcCluskeyTable.add(new ArrayList<>(constituentOperands.size()));

            for (List<String> constituent : constituentOperands) {
                int implicantIndex = shortenedForm.indexOf(implicant);

                if (new HashSet<>(constituent).containsAll(implicant)) {
                    quineMcCluskeyTable.get(implicantIndex).add(true);
                } else quineMcCluskeyTable.get(implicantIndex).add(false);
            }
        }
        return quineMcCluskeyTable;
    }


    /////////////////////////////////////constituent and implicant
    public String getCalculationCNF(String PCNF) {                 /////////////////////////name
        String groupOperator = " \\+ ";
        List<List<String>> constituentOperands = getOperands(PCNF, groupOperator);
        List<List<List<String>>> allGroupingVariants = getAllGroupingVariants(constituentOperands);
        List<List<String>> shortenedForm = getShortenedForm(allGroupingVariants, constituentOperands);

        if (shortenedForm.isEmpty()) {
            return PCNF;
        }

        String referenceFormulaResult = new TruthTable(PCNF).getIndexForm();
        calculateDeadEndForm(shortenedForm, groupOperator, referenceFormulaResult);

        shortenedForm.addAll(getIrreducibleConstituents(constituentOperands, shortenedForm));

        printInfo(constituentOperands, allGroupingVariants, shortenedForm);                                             //////////////

        return constructFormula(shortenedForm, groupOperator);
    }

    private List<List<String>> getIrreducibleConstituents(List<List<String>> constituentOperands, List<List<String>> shortenedForm) {
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

    private void calculateDeadEndForm(List<List<String>> shortenedForm, String groupOperator, String referenceFormulaResult) {
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

    private String constructFormula(List<List<String>> operands, String groupOperator) {
        if(groupOperator.equals(" \\+ ")) groupOperator = " + ";
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


    private List<List<String>> getShortenedForm(List<List<List<String>>> allGroupingVariants, List<List<String>> constituentOperands) {
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
        return duplicates;
    }

    private void findDuplicates(List<List<String>> currentConstituent, List<List<String>> otherConstituent, List<String> currentGroup, List<String> otherGroup, List<List<String>> duplicates) {
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

    private List<List<List<String>>> getAllGroupingVariants(List<List<String>> constituentOperands) {
        List<List<List<String>>> allGroupingVariants = new ArrayList<>();

        for (var constituent : constituentOperands) {
            List<List<String>> groupingVariants;
            groupingVariants = getGroupingVariants(constituent);

            allGroupingVariants.add(groupingVariants);
        }
        return allGroupingVariants;
    }

    private List<List<String>> getGroupingVariants(List<String> constituent) {
        List<List<String>> variants = new ArrayList<>();

        for (int iter = 0; iter < constituent.size(); iter++) {
            List<String> variant = new ArrayList<>(constituent);
            variant.remove(iter);
            variants.add(variant);
        }
        return variants;
    }

    private List<List<String>> getOperands(String formula, String operator) {
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

    private boolean isEndOfTheString(String formula, int groupCounter, String constituent) {
        return groupCounter + constituent.length() + 2 >= formula.length();
    }

    @NotNull
    private String removeParentheses(String formula, int groupCounter) {
        return formula.substring(formula.indexOf('(', groupCounter) + 1, formula.indexOf(')', formula.indexOf('(', groupCounter) + 1));
    }

    private void printInfo(List<List<String>> constituentOperands, List<List<List<String>>> allGroupingVariants, List<List<String>> shortenedForm) {
        System.out.println(constituentOperands);
        System.out.println(allGroupingVariants);
        System.out.println(shortenedForm);
    }
}
