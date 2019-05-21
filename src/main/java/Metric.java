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

public class Metric {

    public Metric() {
    }

    public Metric(Map<String, String> m) {
        this.m = m;
    }

    private String askedMetrics = "bugs,confirmed_issues,code_smells,sqale_index,coverage,duplicated_lines_density,duplicated_blocks";

    public String getAskedMetrics() {
        return askedMetrics;
    }
    private Map<String,String> m=new HashMap<>();
    void addmetric(Map m){
        m.put("nombre de bug","bugs");
        m.put("vulnérabilités","confirmed_issues");
        m.put("code smells","code smells");
        m.put("debt","sqale_index");
        m.put("coverage","coverage");
        m.put("duplications","duplicated_lines_density");
        m.put("duplicated blocks","duplicated_blocks");

    }

    public static String getRealName(Map m,String name)
    {
        for(Object s:m.keySet()){
            if(m.get(s).equals(name)){
                return s.toString();
            }
            else{
                return "Error";
            }
        }
        return null;
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
                name = metric.getAsJsonObject().get("metric").toString();
                value = metric.getAsJsonObject().get("value").getAsDouble();
                map.put(getRealName(this.m,name), value);
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

