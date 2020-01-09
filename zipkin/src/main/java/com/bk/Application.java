package com.fw;

/*import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;*/
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableZipkinServer
@EnableEurekaClient
public class Application {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    /*@Bean("zipkinElasticsearchHttp")
    public OkHttpClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        return client.newBuilder().authenticator(new Authenticator() {
            public Request authenticate(Route route, Response response) {
                if (response.request().header("Authorization") != null) {
                    return null; // Give up, we've already failed to authenticate.
                }

                String credential = Credentials.basic("elastic", "NE^z^z#7Mjh^Jd~B=TPs");
                return response.request().newBuilder()
                        .header("Authorization", credential)
                        .build();

            }
        }).build();
    }*/
}
//需要创建手动quet。name=zipkin
