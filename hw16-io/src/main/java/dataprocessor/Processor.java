package dataprocessor;

import java.util.List;
import java.util.Map;
import model.Measurement;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
