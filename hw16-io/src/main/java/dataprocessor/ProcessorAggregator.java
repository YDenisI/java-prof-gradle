package dataprocessor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Measurement;

@SuppressWarnings({"java:S106"})
public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> aggregatedData = new LinkedHashMap<>();
        for (Measurement measurement : data) {
            System.out.println(measurement.value());
            aggregatedData.merge(measurement.name(), measurement.value(), Double::sum);
        }
        System.out.println(aggregatedData);
        return aggregatedData;
    }
}
