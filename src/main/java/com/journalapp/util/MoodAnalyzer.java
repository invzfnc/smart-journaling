package main.java.com.journalapp.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyze the mood of user given the content of journal entry.
 */
public class MoodAnalyzer {
    /**
     * Analyze mood of user given the content of journal entry.
     * @return string representing mood. valid responses: "Very Positive", "Positive", "Neutral", "Negative", "Very Negative" (either one)
     * <p>
     * This method uses <a href="https://huggingface.co/tabularisai/multilingual-sentiment-analysis">tabularisai/multilingual-sentiment-analysis model</a>
     * to analyze the user's mood. Token is needed to send requests to the HuggingFace Inference API. Setup your token before calling this method. */
    public static String analyze(String text) {
        String response = "";
        try {
            // load token
            Map<String, String> env = EnvLoader.loadEnv(".env");
            String token = env.get("BEARER_TOKEN");

            if (token == null || token.isEmpty()) {
                System.err.println("ERROR: BEARER_TOKEN is not set in the environment. Aborting operation.");
                throw new Exception("Token is not set in the environment.");
            }

            // send post request
            String url = "https://router.huggingface.co/hf-inference/models/tabularisai/multilingual-sentiment-analysis";
            String jsonBody = "{\"inputs\": \"" + text + "\"}";
            response = new API().post(url, token, jsonBody);

            Pattern pattern = Pattern.compile("\\[\\[\\{\"label\":\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                response = matcher.group(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Demonstrate the use of this class.
     */
    private static void main(String[] args) {
        System.out.println(analyze("my hair loss issue is getting worse..."));  // Negative
    }
}
