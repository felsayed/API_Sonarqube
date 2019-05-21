package Interface;

import java.util.Map;

public interface Metric {
    void addMetric(Map<String, String> m);
    Map<String,Double> getMetrics(String compName);

}
