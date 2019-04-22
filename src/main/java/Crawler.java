import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    public static void main(String[] args) {
        for(int i=10;i<44;++i)
            getPictures("http://comic.sfacg.com/HTML/LOVEL/0"+i+"/", "F:\\百度云同步盘\\计划\\人生学习\\love理论\\all\\");
        //getPictures("http://comic.sfacg.com/HTML/LOVEL/001j/", "F:\\百度云同步盘\\计划\\人生学习\\love理论\\all\\");
    }

    private static void download(String urlString, String filename, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        System.out.println("输出文件："+sf.getPath() + "\\" + filename);
    }

    private static void getPictures(String urlString, String savePath) {
        try {
            Connection cFirst = Jsoup.connect(urlString);
            Connection.Response rFirst = cFirst
                    .userAgent(
                            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; chromeframe/31.0.1650.63; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; CMDTDFJS; f9J; InfoPath.3; .NET4.0C; .NET4.0E; Tablet PC 2.0)")
                    .execute();
            Document doc = rFirst.parse();
            String sJS = doc.select("script[language]").get(1).attr("src");
            String sPrefix = "http://comic.sfacg.com";

            // 构造URL
            URL url = new URL(sPrefix + sJS);
            // 打开连接
            URLConnection con = url.openConnection();
            // 设置请求超时为5s
            con.setConnectTimeout(5 * 1000);
            // 输入流
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String content = "";
            String line = br.readLine();
            while (line != null) {
                content += line + "/n";
                line = br.readLine();
            }
            String sMatch = "picAy.*?;";
            Pattern pattern = Pattern.compile(sMatch);
            Matcher m = pattern.matcher(content);
            int iCount = 0;
            while (m.find()) {
                String sValue = m.group();
                if (iCount > 0) {
                    int i = sValue.indexOf('"');
                    String sUrl = sPrefix + sValue.substring(i+1, sValue.length() - 2);
                    download(sUrl, (iTemp++) + ".jpg", savePath);
                }
                ++iCount;
            }
        } catch (Exception e) {

        }
    }
    private static int iTemp=100;
}
