package dataprocessor;

import java.util.List;
import model.Measurement;

public interface Loader {

    List<Measurement> load();
}
