import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Dataset {
    private List<DatasetEntry> entries = new ArrayList<>();

    private final String positiveResponse;
    private final String negativeResponse;

    private Dataset(String positive, String negative) {
        this.positiveResponse = positive;
        this.negativeResponse = negative;
    }

    Dataset(List<DatasetEntry> entries, String positiveResponse, String negativeResponse) {
        this(positiveResponse, negativeResponse);
        this.entries = entries;
    }

    static Dataset fromFile() {
        Dataset dataset = new Dataset("recurrence-events", "no-recurrence-events");

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("breast-cancer.arff")))) {
            String line = bufferedReader.readLine();

            while (line != null) {
                if (line.isEmpty() || line.charAt(0) == '@' || line.charAt(0) == '%') {

                    line = bufferedReader.readLine();
                    continue;
                }

                line = line.replaceAll("'", "");

                List<String> entry = Arrays.asList(line.split(","));
                String response = entry.get(entry.size() - 1);
                dataset.addEntry(new DatasetEntry(entry.subList(0, entry.size() - 1), response));

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    private void addEntry(DatasetEntry entry) {
        entries.add(entry);
    }

    int size() {
        return entries.size();
    }

    int getAttributesCount() {
        if (entries.isEmpty()) {
            return 0;
        }

        return entries.get(0).getAttributes().size();
    }

    String getPositiveResponse() {
        return positiveResponse;
    }

    String getNegativeResponse() {
        return negativeResponse;
    }

    List<DatasetEntry> getEntries() {
        return entries;
    }

    int getTotalPositiveResponses() {
        return (int) entries.stream().filter(x -> x.getResponse().equals(positiveResponse)).count();
    }

    int getTotalNegativeResponses() {
        return (int) entries.stream().filter(x -> x.getResponse().equals(negativeResponse)).count();
    }

    List<String> getAttributeAllPossibleValues(int attribute) {
        if (attribute < 0 || attribute >= getAttributesCount()) {
            throw new IllegalArgumentException();
        }

        List<String> possibleValues = new ArrayList<>();

        for (DatasetEntry entry : entries) {
            List<String> attributes = entry.getAttributes();

            if (!possibleValues.contains(attributes.get(attribute))) {
                possibleValues.add(attributes.get(attribute));
            }
        }

        return possibleValues;
    }

    Dataset getSubsetWithAttributeValue(int selectedAttribute, String value) {
        Dataset dataset = new Dataset(positiveResponse, negativeResponse);

        for (DatasetEntry entry : entries) {
            List<String> attributes = entry.getAttributes();

            if (attributes.get(selectedAttribute).equals(value)) {
                dataset.addEntry(new DatasetEntry(entry));
            }
        }

        return dataset;
    }
}
