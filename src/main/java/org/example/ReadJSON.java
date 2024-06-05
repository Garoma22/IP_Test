package org.example;
import java.io.*;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ReadJSON {
    public static void main(String[] args) {

        String filePath = "/tickets.json";

        try (InputStream is = ReadJSON.class.getResourceAsStream(filePath);
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            String fileContent = sb.toString();

            // Remove BOM if present
            if (fileContent.startsWith("\uFEFF")) {
                fileContent = fileContent.substring(1);
            }

            JSONTokener tokener = new JSONTokener(fileContent);
            JSONObject jsonObject = new JSONObject(tokener);
            JSONArray tickets = jsonObject.getJSONArray("tickets");

            FlightAnalyzer.analyzeFlights(tickets);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error while parsing JSON: " + e.getMessage());
        }
    }
}

