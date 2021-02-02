import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    private static final int VALIDATION_SUBSETS_COUNT = 10;

    public static void main(String[] args) {
        System.out.println();

        Dataset dataset = Dataset.fromFile();
        Collections.shuffle(dataset.getEntries());
        List<Integer> attributes =
                IntStream.range(0, dataset.getAttributesCount()).boxed().collect(Collectors.toList());

        double accuraciesSum = 0;
        int setSize = dataset.size() / VALIDATION_SUBSETS_COUNT;
        for (int i = 0; i < setSize * VALIDATION_SUBSETS_COUNT; i += setSize) {
            int endIdx = (i + setSize >= dataset.size()) ? dataset.size() - 1 : i + setSize;

            List<DatasetEntry> testingSet = new ArrayList<>(dataset.getEntries().subList(i, endIdx));
            List<DatasetEntry> trainingSet = new ArrayList<>(dataset.getEntries().subList(0, i));

            trainingSet.addAll(dataset.getEntries().subList(endIdx, dataset.size()));

            TreeNode tree = ID3DecisionTree.createTree(new Dataset(trainingSet,
                    "recurrence-events", "no-recurrence-events"), attributes);

            int accurateCount = 0;
            for (DatasetEntry entry : testingSet) {
                String response = ID3DecisionTree.classify(tree, entry);

                if (entry.getResponse().equals(response)) {
                    accurateCount++;
                }
            }

            double accuracy = (double) accurateCount / testingSet.size() * 100;
            accuraciesSum += accuracy;
            System.out.println("Accuracy: " + accuracy + "%");
        }
        System.out.println();
        System.out.println("Mean accuracy: " + accuraciesSum / VALIDATION_SUBSETS_COUNT + "%");

    }

}
