import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Two {
    public static void main(String[] a) {
        /*HttpClient httpClient = HttpClient.create();//.followRedirect(true);
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        WebClient client = WebClient.builder().clientConnector(connector).build();*/
        WebClient client = WebClient.create();
        client.get().uri("http://oauth.shjsqy.com/oauth/authorize?response_type=code&client_id=jsqy&state=8OIJnIToAAKyQdxnv3Uo3_AOZOwOoPkfQBHzZjBUB9U%3D&redirect_uri=http://gateway.shjsqy.com:80/login/oauth2/code/jsqy")
                .cookie("JSESSIONID", "82CA814A62447F23AB983F5F331C1933")
                .retrieve()
                .onStatus(HttpStatus::is3xxRedirection, response -> {
                    String location = response.headers().header("location").get(0);
                    System.out.println("location==================" + location);
                    try {
                        System.out.println("location==================" + URLDecoder.decode(location, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    return Mono.empty();
                }).bodyToMono(String.class).block();
    }
}
