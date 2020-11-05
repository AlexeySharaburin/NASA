package ru.netology;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Arrays;

public class Main {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=dyismfCB0CP0yvF3KEaTIgnutajInFNFyoVec0y4");
        CloseableHttpResponse response = httpClient.execute(request);

        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

        Answer answerNasa = mapper.readValue(response.getEntity().getContent(), Answer.class);

        System.out.println(answerNasa.getUrl());

        HttpGet requestUrl = new HttpGet(answerNasa.getUrl());
        CloseableHttpResponse responseUrl = httpClient.execute(requestUrl);

        Arrays.stream(responseUrl.getAllHeaders()).forEach(System.out::println);

        String stringName = answerNasa.getUrl().split("2011/")[1];

        HttpEntity entity = responseUrl.getEntity();

        byte[] bytes = EntityUtils.toByteArray(entity);

        InputStream stream = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(stream);
        ImageIO.write(image, "jpg", new File("/Users/alexey/Desktop/First_" + stringName));
// URL
        try {
            BufferedImage imageUrl = ImageIO.read(new URL(answerNasa.getUrl()));
            ImageIO.write(imageUrl, "JPEG", new File("/Users/alexey/Desktop/Second_" + stringName));
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }

    }
}
