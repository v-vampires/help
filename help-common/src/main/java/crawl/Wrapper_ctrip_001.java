package crawl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by fitz.li on 2015/10/21.
 * 抓取携程
 */
public class Wrapper_ctrip_001 {

    private static final Logger log = LoggerFactory.getLogger(Wrapper_ctrip_001.class);

    private static CloseableHttpClient httpClient = null;
    private static final Header USER_AGENT = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");
    private static final String jsUrl = "http://localhost:3000";
    private static final String startJs = "var result;\n" + "simpleLoader = function (a) {\n" + "    result = a;\n"
            + "    return a;\n" + "};\n" + "location = {\n" + "    href: \"%s\"\n" + "};\n";

    private static final Gson gson = new Gson();
    private static Map<String,CityData> cityDataCache = Maps.newHashMap();
    //出发信息
    private CityData depCity;
    private CityData arrCity;
    private String depDate;
    //condition是请求获取首页以及后续页面的报价数据以及booking请求的参数
    private String condition;

    static {
        /*HttpHost proxy = new HttpHost("120.198.236.10",80);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();*/

        httpClient = HttpClients.createDefault();
        //读取城市信息配置文件
    }
    public static void main(String[] args) throws IOException {
        Wrapper_ctrip_001 instance = new Wrapper_ctrip_001();
        instance.init("上海", "香港", "2018-06-01");
        List<String> htmls = instance.getHtml();
        //解析
        Map<String, FlightData> flightDataMap = instance.processHtml(htmls);
        PriceData lowestPriceData = null;
        for(String key : flightDataMap.keySet()){
            FlightData flightData = flightDataMap.get(key);
            PriceData priceData = flightData.getFareList().get(0);
            log.info("航班号："+key+",最低价：overallPrice:"+priceData.getOverallPrice()+",price:"+priceData.getPrice()+",tax:"+priceData.getTax());
            if(lowestPriceData==null || priceData.getOverallPrice() < lowestPriceData.getOverallPrice()){
                lowestPriceData= priceData;
            }
        }

        //用lowestPriceData booking
       /* instance.getBooking(lowestPriceData.getParameter());
        instance.getBooking(null);*/
    }

    private static void readConfig(){
        //读取文件
        FileInputStream fis = null;
        Scanner sc = null;
        try {
            fis = new FileInputStream(Wrapper_ctrip_001.class.getResource("/crawlerconfig/Wrapper_ctrip_001.json").getPath());
            sc = new Scanner(fis);
            StringBuilder citys = new StringBuilder();
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                citys.append(line);
            }
            final Type type = new TypeToken<Map<String, CityData>>() {}.getType();
            cityDataCache = gson.fromJson(citys.toString(), type);
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    private CityData getCityInfo(String cityName){
        //http://flights.ctrip.com/international/tools/poi.ashx?charset=utf-8&key=%E7%81%AB%E5%A5%B4%E9%B2%81%E9%B2%81&channel=1&mode=1&f=1
        long s = System.currentTimeMillis();
        //首先去cache加载，如果cache没有去抓取
        CityData cityData = cityDataCache.get(cityName);
        if(cityData!=null){
            return cityData;
        }
        CloseableHttpResponse response = null;
        try {
            String cityInfoUrl = "http://flights.ctrip.com/international/tools/poi.ashx?charset=utf-8&key="+URLEncoder.encode(cityName,"UTF-8")+"&channel=1&mode=1&f=1";
            HttpGet cityInfoGet = new HttpGet(cityInfoUrl);
            response = httpClient.execute(cityInfoGet);
            response.getEntity();
            String html = EntityUtils.toString(response.getEntity());
            html = html.substring("cQuery.jsonpResponse=".length());
            CityResultData cityResultData = gson.fromJson(html, CityResultData.class);
            cityData = cityResultData.getData().get(0);
            cityDataCache.put(cityName,cityData);
            //异步保存city信息
            //writeConfig();
            return cityData;
        }catch (Exception e){
            throw new IllegalArgumentException("输入的城市信息有误");
        }
        finally {
            log.info("getCityInfo cost time:"+String.valueOf(System.currentTimeMillis()-s));
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init(String depCity,String arrCity,String depDate){
        this.depCity = getCityInfo(depCity);
        this.arrCity = getCityInfo(arrCity);
        this.depDate = depDate;
    }

    public List<String> getHtml() {
        try {
            firstRequest();
            List<String> htmls = secondRequset();
            return htmls;
        } catch (Exception e) {
            log.error("get html error!",e);
        }
        return null;
    }



    /**
     * 第一个请求,获取condition
     * 请求url：http://flights.ctrip.com/international
     * @throws Exception
     */
    private void firstRequest() throws Exception {
        HttpGet firstGet = new HttpGet("http://flights.ctrip.com/international");
        firstGet.removeHeaders("User-Agent");
        firstGet.addHeader(USER_AGENT);
        firstGet.addHeader(new BasicHeader("Host", "flights.ctrip.com"));
        httpClient.execute(firstGet);
        condition = getNewCondition();
    }

    /**
     * 第二个请求，目测每回请求5次
     * url:http://flights.ctrip.com/international/AjaxRequest/SearchFlights/AsyncSearchResultHandler.ashx
     * @return
     * @throws IOException
     */
    private List<String> secondRequset() throws IOException {
        CloseableHttpResponse response = null;
        List<String> htmls = Lists.newArrayList();
        try {
            String firstUrl = buildFirstUrl();
            for(int i=1; i<=5;i++){
                long s = System.currentTimeMillis();
                HttpPost secondPost = new HttpPost("http://flights.ctrip.com/international/AjaxRequest/SearchFlights/AsyncSearchResultHandler.ashx");
                secondPost.setHeader("Referer",firstUrl);
                secondPost.setHeader(USER_AGENT);
                String searchMode = i==1? "Search":"TokenSearch";
                NameValuePair[] names = { new BasicNameValuePair("SearchMode", searchMode),new BasicNameValuePair("condition", condition),new BasicNameValuePair("SearchToken", String.valueOf(i)) };
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(names), Consts.UTF_8);
                secondPost.setEntity(entity);
                response = httpClient.execute(secondPost);
                String html = EntityUtils.toString(response.getEntity());
                log.info("getHtml_"+i+" cost time:"+String.valueOf(System.currentTimeMillis()-s));
                htmls.add(html);
            }
            return htmls;
        }finally {
            if(response!=null){
                response.close();
            }
        }
    }

    private String getNewCondition() throws Exception {
        long s = System.currentTimeMillis();
        String url = buildFirstUrl();
        //设置cookie
        String searchCookieStr = buildSearchCookie();
        HttpClientContext httpClientContext = HttpClientContext.create();
        BasicClientCookie searchCookie = new BasicClientCookie("FlightIntl",searchCookieStr);
        searchCookie.setDomain(".ctrip.com");
        searchCookie.setPath("/");
        CookieStore cookieStore = new BasicCookieStore();
        cookieStore.addCookie(searchCookie);
        httpClientContext.setCookieStore(cookieStore);

        HttpPost httpPost = new HttpPost(url);
        //设置请求header
        httpPost.addHeader("Host", "flights.ctrip.com");
        httpPost.addHeader("Origin", "http://flights.ctrip.com");
        httpPost.addHeader("Referer", "http://flights.ctrip.com/international/");
        httpPost.removeHeaders("User-Agent");
        httpPost.addHeader(USER_AGENT);
        //设置请求参数
        NameValuePair[] pair = new NameValuePair[23];
        pair[0] = new BasicNameValuePair("FlightWay", "S");
        pair[1] = new BasicNameValuePair("homecity_name", depCity.getName()+"(" + depCity.getCode() + ")");
        pair[2] = new BasicNameValuePair("HomeCityID", String.valueOf(depCity.getCityId()));
        pair[3] = new BasicNameValuePair("destcity1_name", arrCity.getName() + "(" + arrCity.getCode() + ")");
        pair[4] = new BasicNameValuePair("destcityID", String.valueOf(arrCity.getCityId()));
        pair[5] = new BasicNameValuePair("DDatePeriod1", depDate);
        pair[6] = new BasicNameValuePair("ADatePeriod1", "");
        pair[7] = new BasicNameValuePair("txtBeginAddress1", "");
        pair[8] = new BasicNameValuePair("txtBeginCityCode1", "");
        pair[9] = new BasicNameValuePair("txtEndAddress1", "");
        pair[10] = new BasicNameValuePair("txtEndCityCode1", "");
        pair[11] = new BasicNameValuePair("txtDatePeriod1", "");
        pair[12] = new BasicNameValuePair("txtBeginAddress2", "");
        pair[13] = new BasicNameValuePair("txtBeginCityCode2", "");
        pair[14] = new BasicNameValuePair("txtEndAddress2", "");
        pair[15] = new BasicNameValuePair("txtEndCityCode2", "");
        pair[16] = new BasicNameValuePair("txtDatePeriod2", "");

        pair[17] = new BasicNameValuePair("txtBeginAddress3", "");
        pair[18] = new BasicNameValuePair("txtBeginCityCode3", "");
        pair[19] = new BasicNameValuePair("txtEndAddress3", "");
        pair[20] = new BasicNameValuePair("txtEndCityCode3", "");
        pair[21] = new BasicNameValuePair("txtDatePeriod3", "");
        pair[22] = new BasicNameValuePair("IsFavFull", "");
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> valuePairs = Arrays.asList(pair);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost,httpClientContext);
            String rs = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            rs = "condition =" + StringUtils.substringBetween(rs, "condition =", "</script>");
            String condition = executeJs(rs, url);
            return condition;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            log.info("getNewCondition cost time:"+String.valueOf(System.currentTimeMillis()-s));
            if(response!=null){
                response.close();
            }
        }
        return null;
    }

    private String buildFirstUrl(){
        String firstUrl = "http://flights.ctrip.com/international/"+depCity.getEName().toLowerCase()+"-"+arrCity.getEName().toLowerCase()+"-"+depCity.getCode().toLowerCase()+"-"+arrCity.getCode().toLowerCase();
        firstUrl = firstUrl.replaceAll(" ", "");
        return firstUrl;
    }

    private String buildSearchCookie() throws UnsupportedEncodingException {
        // Search=["Shanghai|上海(SHA)|2|SHA|480","Hong Kong|香港(HKG)|58|HKG|480","2015-11-01"]
        String base = "Search=";
        String searchCookie = "[\"" +depCity.getEName()+"|"+depCity.getName()+"("+depCity.getCode()+")"+"|"+depCity.getCityId()+"|"+depCity.getCode()+"|"+depCity.getTimeZone()+"\",\""
                +arrCity.getEName()+"|"+arrCity.getName()+"("+arrCity.getCode()+")"+"|"+arrCity.getCityId()+"|"+arrCity.getCode()+"|"+arrCity.getTimeZone()+"\",\""
                +depDate+"\"]";
        searchCookie = base + URLEncoder.encode(searchCookie, "UTF-8");
        searchCookie = searchCookie.replaceAll("%28", "(");
        searchCookie = searchCookie.replaceAll("%29", ")");
        searchCookie = searchCookie.replaceAll("\\+", "%20");
        return searchCookie;
    }

    private String executeJs(String js, String url) throws IOException {
        long s = System.currentTimeMillis();
        CloseableHttpClient nodeClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            StringBuilder sb = new StringBuilder(String.format(startJs, url));
            sb.append(js.replace("window", "global"));
            sb.append(";\n result;");
            HttpPost httpPost = new HttpPost(jsUrl);
            List<NameValuePair> valuePairs = Lists.newArrayList();
            valuePairs.add(new BasicNameValuePair("js",sb.toString()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
            httpPost.setEntity(entity);
            response = nodeClient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            return str;
        } finally {
            log.info("executeJs cost time:"+String.valueOf(System.currentTimeMillis()-s));
            if(response!=null){
                response.close();
            }
        }
    }

    public Map<String,FlightData> processHtml(List<String> htmls){
        long s = System.currentTimeMillis();
        Map<String,FlightData> flightDataMap = Maps.newLinkedHashMap();
        for(String html : htmls){
            FlightPriceResultData resultData = gson.fromJson(html, FlightPriceResultData.class);
            List<FlightData> flightDatas = resultData.getFlightList();
            if(flightDatas==null||flightDatas.isEmpty()){
                continue;
            }
            for(FlightData newFlightData : flightDatas){
                String flightNo = newFlightData.getFlightNoRoute();
                FlightData flightData = flightDataMap.get(flightNo);
                if(flightData!=null){
                    flightData.getFareList().addAll(newFlightData.getFareList());
                }else{
                    flightData = newFlightData;
                    flightDataMap.put(flightNo,flightData);
                }
                Collections.sort(flightData.getFareList(), new Comparator<PriceData>() {
                    public int compare(PriceData o1, PriceData o2) {
                        return o1.getOverallPrice() - o2.getOverallPrice();
                    }
                });
            }
        }
        log.info("processHtml cost time:"+String.valueOf(System.currentTimeMillis()-s));
        return flightDataMap;
    }

    public void getBooking(String parameter) throws IOException {

        //1、POST /international/AjaxRequest/SearchFlights/AsyncSearchResultHandler.ashx
        //CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            /*  HttpGet httpGet = new HttpGet("http://flights.ctrip.com/international/loginprocxy.html?tmp=5341&success=T&errmessage=&buttonId=bookingBtn_1229372760281&ticketStr=uoeOwviAJ6VQEgTNwLuTqSV9j/bS+aOP3Riia1P+kyQbgkQZsD2gid04kim0/i6/rdIknfqvl4wS3oyAH9/xmsbySyLbUyEUe6XM1RwwaAcqi2dsXKE38oyKLa7NxaFCG9jSWzhTk5oKEP/2rDTNP/101SStHEKT3foCWJL1oSz7Tww1TNh4VYMCmTcBDsf2hHPkStAju6SaCXePKr39LG+sR/iQI2vBL2S3yg4blBREmF06aamTOEbVepnetDWwacnpYVDi1rn41YtIb1nqEw==&loginType=3");
            httpGet.addHeader("Host", "flights.ctrip.com");
            httpGet.removeHeaders("User-Agent");
            httpGet.addHeader(USER_AGENT);
            response = httpClient.execute(httpGet);*/


            //1、POST http://flights.ctrip.com/international/AjaxRequest/SearchFlights/AsyncSearchResultHandler.ashx
            HttpPost httpPost = new HttpPost("http://flights.ctrip.com/international/AjaxRequest/SearchFlights/AsyncSearchResultHandler.ashx");
            //设置请求header
            httpPost.addHeader("Host", "flights.ctrip.com");
            httpPost.addHeader("Origin", "http://flights.ctrip.com");
            httpPost.addHeader("Referer", buildFirstUrl());
            httpPost.removeHeaders("User-Agent");
            httpPost.addHeader(USER_AGENT);

            //设置cookie
            HttpClientContext httpClientContext = HttpClientContext.create();
            CookieStore cookieStore = new BasicCookieStore();
            setCookie(cookieStore,"ticket_ctrip","uoeOwviAJ6VQEgTNwLuTqSV9j/bS+aOP3Riia1P+kyQbgkQZsD2giTLs6a/7R/1yNs50FHIdOWlQHJ2+6H3Xfmff8kgWPlHRk5Otu3Qvz6DfZxZ/elq5TvmRcW5UWnEjx2kw+d+4QwbytvHep4kCOYBavjSTt1khyB5/dFsSdvhleTYe5U4mVyoRfqU7D6NXxePf0paVn+rn+QeLDT2PqQrDT6NgaxzV7UIBKnoD8t8DQgtZIvx3TKX0eFKGmlu43JmKrG0kLxB+oktuv6Hrog==");

            httpClientContext.setCookieStore(cookieStore);
            List<NameValuePair> valuePairs = Lists.newArrayList();
            valuePairs.add(new BasicNameValuePair("SearchMode","Book"));
            /*valuePairs.add(new BasicNameValuePair("condition",condition));
            valuePairs.add(new BasicNameValuePair("Parameter",parameter));*/
            valuePairs.add(new BasicNameValuePair("condition","%7B%22FlightWay%22%3A%22S%22%2C%22SegmentList%22%3A%5B%7B%22DCityCode%22%3A%22SHA%22%2C%22ACityCode%22%3A%22HKG%22%2C%22DCity%22%3A%22Shanghai%7C%E4%B8%8A%E6%B5%B7(SHA)%7C2%7CSHA%7C480%22%2C%22ACity%22%3A%22Hong%20Kong%7C%E9%A6%99%E6%B8%AF(HKG)%7C58%7CHKG%7C480%22%2C%22DepartDate%22%3A%222016-1-16%22%7D%5D%2C%22TransferCityID%22%3A0%2C%22Quantity%22%3A1%2C%22TransNo%22%3A%225316010922000015884%22%2C%22SearchRandomKey%22%3A%22%22%2C%22IsAsync%22%3A1%2C%22RecommendedFlightSwitch%22%3A1%2C%22SearchKey%22%3A%227E2DC7E0978CBEDF8EE2B4BBC87BB2EC75B4C84536555E0DDCFDDA3C44EF2865BC763CC19E48F47A%22%2C%22MultiPriceUnitSwitch%22%3A1%2C%22TransferCitySwitch%22%3Afalse%2C%22EngineFlightFltNo%22%3A%22D%22%2C%22EngineFlightRT%22%3A%22D%22%2C%22SearchStrategySwitch%22%3A1%2C%22MaxSearchCount%22%3A5%2C%22RowNum%22%3A%221500%22%7D"));
            valuePairs.add(new BasicNameValuePair("Parameter","%257b%2522ShoppingToken%2522%253a%2522%25257bHKAirlines%25252cHX%25252c0%25252c%25252c%25257d%25257bHX231%25252c1%25252cY%25252cX%25252c%25252c%25257d%25257b406%25252c150%25257cHX%25257c0%25257d%25257bNOR%25257d%2522%257d"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost,httpClientContext);
            String result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            log.info("1111:"+result);
            //OrderURL=NewUI/Booking.aspx?OrderId=1575622848
            /*String orderURL = "";
            //2、GET http://flights.ctrip.com/international/AjaxRequest/UI2_0/GetOrderEachPartInfoesHandler.ashx?Type=GetQTE&OrderId=1575622848&Token=&IsNew=true
            String orderId = orderURL.substring(orderURL.indexOf("OrderId=")+8);
            HttpGet httpGet = new HttpGet("http://flights.ctrip.com/international/AjaxRequest/UI2_0/GetOrderEachPartInfoesHandler.ashx?Type=GetQTE&OrderId="+orderId+"&Token=&IsNew=true");
            httpGet.addHeader("Host", "flights.ctrip.com");
            httpGet.addHeader("Referer","http://flights.ctrip.com/international/"+orderURL);
            response = httpClient.execute(httpGet);
            result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            log.info("222:"+result);*/
        } finally {
            if(response!=null){
                response.close();
            }
        }
    }

    private void setCookie(CookieStore cookieStore,String cookieName,String cookieValue){
        BasicClientCookie cookie = new BasicClientCookie(cookieName,cookieValue);
        cookie.setDomain(".ctrip.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
    }

    class CityResultData{
        private List<CityData> Data;
        private int Status;
        private String Key;

        public List<CityData> getData() {
            return Data;
        }

        public void setData(List<CityData> data) {
            Data = data;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public String getKey() {
            return Key;
        }

        public void setKey(String key) {
            Key = key;
        }
    }
    class CityData{
        private String Name;//火奴鲁鲁
        private String EName;//Honolulu
        private String Code;//HNL
        private int CityId;//757
        private String TimeZone;//-600

        public String getName() {
            return Name;
        }

        public String getEName() {
            return EName;
        }

        public String getCode() {
            return Code;
        }

        public int getCityId() {
            return CityId;
        }

        public String getTimeZone() {
            return TimeZone;
        }
    }

    public class FlightPriceResultData{
        private List<FlightData> FlightList;

        public List<FlightData> getFlightList() {
            return FlightList;
        }
    }
    public class FlightData{
        private String FlightNoRoute;
        private List<PriceData> FareList;

        public String getFlightNoRoute() {
            return FlightNoRoute;
        }

        public List<PriceData> getFareList() {
            return FareList;
        }
    }
    public class PriceData{
        private int Price;
        private int Tax;
        private int OverallPrice;
        private String Parameter;

        public int getPrice() {
            return Price;
        }

        public int getTax() {
            return Tax;
        }

        public int getOverallPrice() {
            return OverallPrice;
        }

        public String getParameter() {
            return Parameter;
        }
    }
}
