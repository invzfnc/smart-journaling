package main.java.com.journalapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Retrieves current weather.
 */
public class Weather {
    /**
     * Retrieve current weather in Kuala Lumpur from
     * api.data.gov.my based on current period of the day.
     * @return string representing weather,
     * either "Sunny", "Hazy", "Rainy", "Thunderstorms",
     * or empty string if something goes wrong
     */
    public static String getCurrentWeather() {
        API api = new API();
        String url = "https://api.data.gov.my/weather/forecast/?contains=WP%20Kuala%20Lumpur@location__location_name&sort=date&limit=1";

        try {
            // send GET request and retrieve weather json
            String response = api.get(url);

            // determine period of day and extract key
            Pattern pattern = Pattern.compile("\"summary_forecast\":\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(response);

            if (matcher.find()) {
                String weather = matcher.group(1);
                // translate response into simple English word
                return translateWeather(weather);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Take weather from API call response, turn it into simple English phrase.
     * All possible <a href="https://developer.data.gov.my/realtime-api/weather#possible-values-for-_forecast-fields">responses</a>
     * @param weather response value from weather API call
     * @return "Hazy", "Sunny", "Rainy", "Thunderstorms", or "" if matching fails
     */
    private static String translateWeather(String weather) {
        if (weather.equals("Berjerebu"))
            return "Hazy";
        else if (weather.equals("Tiada Hujan"))
            return "Sunny";
        else if (weather.startsWith("Hujan"))
            return "Rainy";
        else if (weather.startsWith("Ribut"))
            return "Thunderstorms";
        else
            return "Unknown";  // in case weird stuff happen
    }

    /**
     * Demonstrate the usage of getCurrentWeather method.
     */
    private static void main(String[] args) {
        System.out.println("The current weather now is: " + getCurrentWeather());
    }
}