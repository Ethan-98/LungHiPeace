package com.eeit45team2.lungspringbootversion.backend.order.pay;

import com.eeit45team2.lungspringbootversion.backend.order.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Component
public class PaypalPayment {

    private static Environment env;
    private static ObjectMapper objectMapper;

    @Autowired
    public PaypalPayment(Environment env, ObjectMapper objectMapper) {
        PaypalPayment.env = env;
        PaypalPayment.objectMapper = objectMapper;
    }

    private static String getBearerToken() throws JsonProcessingException {


        String uri = "https://api-m.sandbox.paypal.com/v1/oauth2/token?grant_type=client_credentials";

        String clientId = env.getProperty("paypal.client-Id");
        String secret = env.getProperty("paypal.secret");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, secret);
        headers.add("Accept", "application/json");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type","client_credentials");
        ResponseEntity<String> response = restTemplate.exchange
                (uri, HttpMethod.POST, new HttpEntity<Object>(body, headers), String.class);
        Map map = objectMapper.readValue(response.getBody(), Map.class);
        return (String) map.get("access_token");
    }

    public static String createPayUrl(Order order, String url){

        String json = "{\n" +
                "   \"intent\":\"CAPTURE\",\n" +
                "   \"application_context\":{\n" +
                "     \"brand_name\":\"浪孩和平\",\n" +
                "     \"return_url\": \"" + url + "\",\n" +
                "      \"cancel_url\": \"" + url + "\",\n" +
                "      \"locale\":\"zh-TW\"\n" +
                "     \n" +
                "   },\n" +
                "   \"purchase_units\":[\n" +
                "      {\n" +
                "         \"amount\":{\n" +
                "            \"currency_code\":\"TWD\",\n" +
                "            \"value\":\""+ order.getTotalPrice() +"\"\n" +
                "         },\n" +
                "        \"custom_id\":\"" + order.getOrderNo() + "\"\n" +
                "      }\n" +
                "     \n" +
                "   ]\n" +
                "}";

        String uri = "https://api-m.sandbox.paypal.com/v2/checkout/orders";
        RestTemplate restTemplate = new RestTemplate();
        String approveUri = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getBearerToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> response = restTemplate.exchange
                    (uri, HttpMethod.POST, new HttpEntity<Object>(json, headers), String.class);
            Map map = objectMapper.readValue(response.getBody(), Map.class);

            List<Map<String,String>> links = (List<Map<String, String>>) map.get("links");

            for (Map<String, String> link : links) {
                if(link.get("rel").equals("approve")){
                    approveUri = link.get("href");
                    System.out.println(approveUri);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return approveUri;
    }

    /**
     *
     * @param token
     * @return
     * orderNo 訂單編號
     */
    public static String captureOrder(String token){
        String uri = "https://api-m.sandbox.paypal.com/v2/checkout/orders/"+ token +"/capture";
        RestTemplate restTemplate = new RestTemplate();
        String orderNo = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getBearerToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> response = restTemplate.exchange
                    (uri, HttpMethod.POST, new HttpEntity<>(headers), String.class);
            Map map = objectMapper.readValue(response.getBody(), Map.class);
            List<Object> list;
            Map<Object,Object> maptemp;

            list = (List<Object>) map.get("purchase_units");

            maptemp = (Map<Object, Object>) list.get(0);

            maptemp = (Map<Object, Object>) maptemp.get("payments");

            list = (List<Object>) maptemp.get("captures");

            Map<Object, Object> captures = (Map<Object, Object>) list.get(0);

            if(captures.get("status").equals("COMPLETED")){
                orderNo = (String) captures.get("custom_id");
            }

            System.out.println(captures.get("status") + " " + captures.get("custom_id"));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return orderNo;
    }

}
