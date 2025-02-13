package dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import model.Measurement;

@SuppressWarnings({"java:S1068", "java:S1126", "java:S106"})
public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        Gson gson = new Gson();
        Type measurementListType = new TypeToken<List<Measurement>>() {}.getType();
        System.out.println(measurementListType);
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, measurementListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
