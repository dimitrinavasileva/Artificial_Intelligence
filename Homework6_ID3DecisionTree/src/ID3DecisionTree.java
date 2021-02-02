import java.util.ArrayList;
import java.util.List;

class ID3DecisionTree {

    static TreeNode createTree(Dataset dataset, List<Integer> attributes) {
        if (!areAllEntriesPositive(dataset)) {
            if (areAllEntriesNegative(dataset)) {
                return new LeafNode(dataset.getNegativeResponse());
            }

            if (attributes.isEmpty()) {
                return new LeafNode(getMostCommonResponse(dataset));
            }

            TreeNode currentNode = new TreeNode();

            int selectedAttribute = getBestAttribute(dataset, attributes);
            currentNode.setValue(selectedAttribute);

            for (String value : dataset.getAttributeAllPossibleValues(selectedAttribute)) {
                Dataset subset = dataset.getSubsetWithAttributeValue(selectedAttribute, value);
                if (subset.size() != 0) {
                    attributes.remove((Integer) selectedAttribute); //Cast to Integer in order to remove by value, not by idx
                    TreeNode child = currentNode.addChild(createTree(subset, attributes));
                    child.setEdge(value);
                }
            }

            return currentNode;
        } else {
            return new LeafNode(dataset.getPositiveResponse());
        }
    }

    static String classify(TreeNode tree, DatasetEntry entry) {
        if (tree instanceof LeafNode) {
            return tree.toString();
        }

        int attributeIndex = tree.getValue();

        for (TreeNode child : tree.getChildren()) {
            if (child.getEdge().equals(entry.getAttributes().get(attributeIndex))) {
                return classify(child, entry);
            }
        }

        return "UNKNOWN";
    }

    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    private static double calculateEntropy(int x, int y) {
        if (x == 0 || y == 0) {
            return 0;
        }

        double pX = (double) x / (x + y);
        double pY = (double) y / (x + y);

        return -(pX * log2(pX)) - (pY * log2(pY)); //entropy
    }

    // This method calculates the Entropy of E(Response, attributeIndex)
    private static double calculateEntropy(Dataset dataset, int attributeIndex) {

        List<String> possibleValues = new ArrayList<>(); // Possible values for this attribute
        List<Integer> positiveCount = new ArrayList<>(); // Positive responses for a particular value of the attribute
        List<Integer> negativeCount = new ArrayList<>(); // Negative responses for a particular value of the attribute

        for (DatasetEntry entry : dataset.getEntries()) {
            String attributeValue = entry.getAttributes().get(attributeIndex);
            int valueIndex = possibleValues.indexOf(attributeValue);

            if (valueIndex == -1) {
                possibleValues.add(attributeValue);
                positiveCount.add(0);
                negativeCount.add(0);
                valueIndex = (possibleValues.indexOf(attributeValue));
            }

            if (entry.getResponse().equals(dataset.getPositiveResponse())) {
                positiveCount.set(valueIndex, positiveCount.get(valueIndex) + 1);
            } else {
                negativeCount.set(valueIndex, negativeCount.get(valueIndex) + 1);
            }
        }

        double entropy = 0;
        int totalExamples = dataset.size();

        //Iterate all possible values
        for (int i = 0; i < possibleValues.size(); i++) {
            int positive = positiveCount.get(i);
            int negative = negativeCount.get(i);
            entropy += (double) (positive + negative) / totalExamples * calculateEntropy(positive, negative);
        }

        return entropy;
    }

    // This method calculates the Gain of G(Response, attributeIndex)
    private static double calculateGain(Dataset dataset, int attributeIndex) {
        int totalPositiveResponses = dataset.getTotalPositiveResponses();
        int totalNegativeResponses = dataset.getTotalNegativeResponses();

        return calculateEntropy(totalNegativeResponses, totalPositiveResponses) - calculateEntropy(dataset, attributeIndex);
    }

    private static String getMostCommonResponse(Dataset dataset) {
        int totalPositiveResponses = dataset.getTotalPositiveResponses();
        int totalNegativeResponses = dataset.getTotalNegativeResponses();

        return (totalPositiveResponses > totalNegativeResponses) ? dataset.getPositiveResponse() : dataset.getNegativeResponse();
    }

    private static int getBestAttribute(Dataset dataset, List<Integer> attributes) {
        if (dataset.size() == 0 || attributes.isEmpty()) {
            return -1;
        }

        int bestAttributeIndex = attributes.get(0);
        double bestGain = 0;

        for (int index : attributes) {
            double currentGain = calculateGain(dataset, index);
            if (currentGain > bestGain) {
                bestAttributeIndex = index;
                bestGain = currentGain;
            }
        }

        return bestAttributeIndex;
    }

    private static boolean areAllEntriesPositive(Dataset dataset) {
        return dataset.getTotalPositiveResponses() == dataset.size();
    }

    private static boolean areAllEntriesNegative(Dataset dataset) {
        return dataset.getTotalNegativeResponses() == dataset.size();
    }
}
