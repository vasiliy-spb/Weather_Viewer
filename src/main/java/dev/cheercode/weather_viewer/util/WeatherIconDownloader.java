package dev.cheercode.weather_viewer.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherIconDownloader {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите путь для сохранения иконок: ");
        String downloadPath = scanner.nextLine().trim();
        scanner.close();

        File directory = new File(downloadPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Ошибка создания директории!");
                return;
            }
        }

        String[] iconCodes = {"01", "02", "03", "04", "09", "10", "11", "13", "50"};
        String[] timeSuffixes = {"d", "n"};

        for (String code : iconCodes) {
            for (String time : timeSuffixes) {
                downloadIcon(code, time, 1, downloadPath);
                downloadIcon(code, time, 2, downloadPath);
            }
        }
    }

    private static void downloadIcon(String code, String time, int scale, String path) {
        String fileName = code + time + (scale == 2 ? "@2x" : "") + ".png";
        String urlString = "https://openweathermap.org/img/wn/" + fileName;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(new File(path, fileName));

                byte[] buffer = new byte[2048];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
                System.out.println("Успешно скачано: " + fileName);
            } else {
                System.err.println("Ошибка загрузки " + fileName + ". Код ответа: " + responseCode);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке " + fileName + ": " + e.getMessage());
        }
    }
}
