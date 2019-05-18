import com.google.gson.Gson;
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

    public void getteur(){
 try {

        URL url = new URL("http://localhost:9000/api/measures/component?metricKeys=bugs,confirmed_issues,code_smells,sqale_index,coverage,duplicated_lines_density,duplicated_blocks&component=hello");//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("hello", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output=br.readLine();
        JsonObject js=new JsonParser().parse(output).getAsJsonObject();
        /*for(int i=0;i<7;i++){
            JsonElement j=js.getAsJsonObject("component").getAsJsonArray("measures").get(i);
            System.out.println(""+j+"");
        }*/
        for (int a=0;a<7;a++)
        {
            JsonElement j=js.getAsJsonObject("component").getAsJsonArray("measures").get(a);
            Map<JsonElement ,JsonElement > map=new HashMap<JsonElement, JsonElement>();
            map.put(j.getAsJsonObject().get("metric"),j.getAsJsonObject().get("value"));
            System.out.println(""+map+"");
        }




     conn.disconnect();

    } catch (Exception e) {
        System.out.println("Exception in NetClientGet:- " + e);
    }
}




}

