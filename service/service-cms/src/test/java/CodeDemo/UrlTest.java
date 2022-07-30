package CodeDemo;

import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlTest {

    @Test
    public void test1(){
        //对URL地址进行编码，解决一些字符失效的问题

        String url = "https://edu-guli-project-oss.oss-cn-shenzhen.aliyuncs.com/2022/04/08/1ca23bc660e24249b6ce702fd2ca3e5c315982.jpg";

        url = url.replace("https://edu-guli-project-oss.oss-cn-shenzhen.aliyuncs.com/", "");
        try {
            url = URLEncoder.encode(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("*****url**编码****"+url);

        //解码
        try {
            url = URLDecoder.decode(url);
        } catch (Exception e){
            e.printStackTrace();
        }


        System.out.println("=====解码====="+url);

    }
}
