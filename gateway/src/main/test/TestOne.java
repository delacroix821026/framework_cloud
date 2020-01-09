import com.fw.Application;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.cookie.Cookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientRequest;
import reactor.netty.http.client.HttpClientResponse;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestOne {
    @Test
    public void test() {
        System.out.println("webClient===============================");
    }

    //@Test
    public static void main(String[] a) {
        //放入参数
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "superadmin");
        formData.add("password", "admin");

        //调用登陆url
        TestOne one = new TestOne();
        Mono<String> mono = one.call("http://oauth.shjsqy.com/login", HttpMethod.POST, formData);
        System.out.println("=============" + mono.block());

        //调用门户
        mono = one.call("http://gateway.shjsqy.com/oauth2/authorization/jsqy", HttpMethod.GET, new LinkedMultiValueMap<>());
        System.out.println("=============" + mono.block());
    }

    //cookies存放
    private MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();

    //Api接口访问器，最原始的
    private WebClient client = WebClient.create();

    //递归调用方法
    public Mono<String> call(String url, HttpMethod method, MultiValueMap<String, String> formData) {
        /*StringBuffer bufferCookie = new StringBuffer();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            StringBuffer buff = new StringBuffer();
            buff.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
            bufferCookie.insert(0, buff.toString());
        }*/

        //GET或者POST
        Mono<String> mono = client.method(method)
                //访问地址
                .uri(url)
                //接受参数类型，POST使用的，form表单
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                //接受的参数username password
                .body(BodyInserters.fromFormData(formData))
                //cookies里有东西就设置进去
                .cookies(new Consumer<MultiValueMap<String, String>>() {
                    @Override
                    public void accept(MultiValueMap<String, String> stringStringMultiValueMap) {
                        stringStringMultiValueMap.addAll(cookies);
                    }
                })
                //调用后，获取Response
                .exchange().flatMap(response -> {
                    //是不是3xx的status
                    if (response.statusCode().is3xxRedirection()) {
                        //是的话。把cookie统统拿出来放进cookies内部变量里
                        for (List<ResponseCookie> cookieSet : response.cookies().values()) {
                            for (ResponseCookie cookie : cookieSet) {
                                //因为第五部会交换SESSIONID，所以要先删除
                                cookies.remove(cookie.getName());
                                cookies.add(cookie.getName(), cookie.getValue());
                            }
                        }
                        try {
                            //获取Head里的location并 解码
                            String location = URLDecoder.decode(response.headers().header("location").get(0), "utf-8");
                            //这里有个坑点。登陆最后步跳转 返回的是 /，待优化
                            if("/".equals(location))
                                location = "http://gateway.shjsqy.com/";
                            //再次发起请求，get访问location地址
                            return call(location, HttpMethod.GET, new LinkedMultiValueMap<>());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    //没有3xx里像上层开始返回
                    return response.bodyToMono(String.class);
                });
        return mono;
    }
}
