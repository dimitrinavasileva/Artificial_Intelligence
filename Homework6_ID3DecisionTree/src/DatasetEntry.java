import java.util.ArrayList;
import java.util.List;

class DatasetEntry {
    private final List<String> attributes;
    private final String response;

    DatasetEntry(List<String> attributes, String response) {
        this.attributes = attributes;
        this.response = response;
    }

    DatasetEntry(DatasetEntry other) {
        this.response = other.response;
        attributes = new ArrayList<>(other.attributes);
    }

    List<String> getAttributes() {
        return attributes;
    }

    String getResponse() {
        return response;
    }
}
