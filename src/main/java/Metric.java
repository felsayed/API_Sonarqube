import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Metric implements Interface.Metric{

    private Map<String,String> m;

    public Metric() {
        this.m = new HashMap<>();
        addMetric(this.m);
    }

    private String askedMetrics = "bugs,confirmed_issues,code_smells,sqale_index,coverage,duplicated_lines_density,duplicated_blocks";

    public String getAskedMetrics() {
        return askedMetrics;
    }

    public void addMetric(Map<String, String> m) {
        m.put("bugs", "nombre de bug");
        m.put("confirmed_issues", "vulnérabilités");
        m.put("code_smells", "code smells");
        m.put("sqale_index", "debt");
        m.put("coverage","coverage");
        m.put("duplicated_lines_density", "duplications");
        m.put("duplicated_blocks", "duplicated blocks");

    }

    public Map<String, Double> getMetrics(String compName){

        try {
            URL url = new URL("http://localhost:9000/api/measures/component?metricKeys=" + getAskedMetrics() + "&component=" + compName + "");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("hello", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output = br.readLine();
            JsonObject js=new JsonParser().parse(output).getAsJsonObject();
            JsonArray metrics = js.getAsJsonObject("component").getAsJsonArray("measures");

            Map<String, Double> map = new HashMap<>();
            String name;
            Double value;
            for(JsonElement metric : metrics) {
                name = metric.getAsJsonObject().get("metric").getAsString();
                value = metric.getAsJsonObject().get("value").getAsDouble();
                map.put(this.m.get(name), value);
            }
            System.out.println(map);
            conn.disconnect();
            return map;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }
}

