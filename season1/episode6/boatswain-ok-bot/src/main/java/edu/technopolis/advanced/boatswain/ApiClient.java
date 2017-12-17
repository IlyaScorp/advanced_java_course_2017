package edu.technopolis.advanced.boatswain;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.technopolis.advanced.boatswain.request.Request;

class ApiClient implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String apiSchema;
    private final String apiHost;
    private final String tokenParam;
    private final CloseableHttpClient client;

    ApiClient(String apiSchema, String apiHost, String tokenParam) {
        this.apiSchema = apiSchema;
        this.apiHost = apiHost;
        this.tokenParam = tokenParam;

        client = HttpClients.custom()
                .setDefaultHeaders(Arrays.asList(
                        new BasicHeader("Host", apiHost),
                        new BasicHeader("Agent", "boatswain-bot"),
                        new BasicHeader("Accept", "*/*")
                        )
                ).build();

    }

    <REQ extends Request, RESP> RESP get(REQ req, Class<RESP> clazz) throws IOException {
        String queryString = getQueryString(req);
        log.info("Sending GET to [{}]", queryString);
        HttpGet request = new HttpGet(queryString);
        return method(clazz, request);
    }

    <REQ extends Request, RESP> RESP post(REQ req, Class<RESP> clazz) throws IOException {
        String queryString = getQueryString(req);
        log.info("Sending POST to [{}] wih following data [{}]", queryString, req.getPayload());
        HttpPost post = new HttpPost(queryString);
        post.setEntity(new ByteArrayEntity(MAPPER.writeValueAsBytes(req.getPayload()), ContentType.APPLICATION_JSON));
        return method(clazz, post);
    }

    <RESP> RESP method(Class<RESP> clazz, HttpUriRequest method) throws IOException {
        try (CloseableHttpResponse response = client.execute(method)){
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            String content = sb.toString();
            log.info("Read response {}", content);
            return MAPPER.readValue(content, clazz);
        }
    }

    private <REQ extends Request> String getQueryString(REQ req) {
        String queryString = req.getQueryStart();
        if (tokenParam != null) {
            if (queryString.indexOf('?') != -1) {
                //... то нужно добавит ещё один параметр
                queryString += '&' + tokenParam;
            } else {
                //... иначе добавить новый единственный параметр
                queryString += '?' + tokenParam;
            }
        }
        return apiSchema + "://" + apiHost + queryString;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
