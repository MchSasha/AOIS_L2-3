import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Grouping {
    public String getSCNF(String PCNF) {
        List<List<String>> constituentOperands;
        List<List<String>> simplifiedZeroConstituents = new ArrayList<>();
        List<List<List<String>>> allGroupingVariants;
        List<String> extraConstituents;
        StringBuilder result = new StringBuilder();

        String operator = " \\+ ";
        constituentOperands = getOperands(PCNF, operator);
        System.out.println(constituentOperands);            //////////////////

        allGroupingVariants = getAllGroupingVariants(constituentOperands);

        simplifiedZeroConstituents = findDuplicateLists(allGroupingVariants, constituentOperands);
        System.out.println(simplifiedZeroConstituents);         /////////////////

        if (simplifiedZeroConstituents.isEmpty()) {
            return PCNF;
        }


        for (List<String> processingConstituent : simplifiedZeroConstituents) {

        }






        return "";

    }

    private List<List<String>> findDuplicateLists(List<List<List<String>>> allGroupingVariants, List<List<String>> constituentOperands) {                 ///////ref
        List<List<String>> duplicates = new ArrayList<>();
        int size = allGroupingVariants.size();
//
        for (int i = 0; i < size - 1; i++) {
            List<List<String>> current = allGroupingVariants.get(i);

            for (int j = i + 1; j < size; j++) {
                List<List<String>> other = allGroupingVariants.get(j);
//
                for (List<String> temp : current) {
                    if (other.contains(temp)) {
//
                        int iter1 = current.indexOf(temp);
                        int iter2 = other.indexOf(temp);
                        if (!Objects.equals(constituentOperands.get(i).get(iter1), constituentOperands.get(j).get(iter2))) {
                            duplicates.add(temp);

                        }
                    }
                }
            }
        }
        return duplicates;
    }


    private List<List<List<String>>> getAllGroupingVariants(List<List<String>> constituentOperands) {
        List<List<List<String>>> allGroupingVariants = new ArrayList<>();

        for (var constituent : constituentOperands) {
            List<List<String>> groupingVariants;
            groupingVariants = getGroupingVariants(constituent);

            allGroupingVariants.add(groupingVariants);
            System.out.println(groupingVariants);           /////////////////
        }
        return allGroupingVariants;
    }

    private List<List<String>> getGroupingVariants(List<String> constituent) {
        List<List<String>> variants = new ArrayList<>();

        for (int i = 0; i < constituent.size(); i++) {
            List<String> variant = new ArrayList<>(constituent);
            variant.remove(i);
            variants.add(variant);
        }
        return variants;
    }

    private List<List<String>> getOperands(String formula, String operator) {
        int groupCounter = 0;
        List<List<String>> constituentOperands = new ArrayList<>();

        while(groupCounter < formula.length()) {
            String constituent = getFromParentheses(formula, groupCounter);

            if (endOfTheString(formula, groupCounter, constituent)) break;

            groupCounter += constituent.length() + 2;

            constituent = constituent.trim();
            constituent = constituent.replaceAll(operator, " ");
            String[] operands = constituent.split(" ");
            constituentOperands.add(Arrays.stream(operands).toList());
        }
        return constituentOperands;
    }

    private static boolean endOfTheString(String formula, int groupCounter, String constituent) {
        return groupCounter + constituent.length() + 2 >= formula.length();
    }

    @NotNull
    private static String getFromParentheses(String formula, int groupCounter) {
        return formula.substring(formula.indexOf('(', groupCounter) + 1, formula.indexOf(')', formula.indexOf('(', groupCounter) + 1));
    }

}
