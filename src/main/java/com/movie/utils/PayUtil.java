package com.movie.utils;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("FieldCanBeLocal")
@Component
public class PayUtil {

    //appID
    @SuppressWarnings("FieldCanBeLocal")
    private final String APP_ID = "2021000148661246";

    //应用私钥
    @SuppressWarnings("FieldCanBeLocal")
    private final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDgh8jh3lNhXkZ+PUVQpTYvP1O21aoiKGdJTTDrwUg+Z+FqItKAaTyAE5WmEvU/zP9Vb3J8ZRzmpQ5LhTP3agyzHVhBMcDeFIHOfvmMm9K41m1WZB68vpRphMtfb7dPm9I+A8bZTKyz5uElC8hlLZHKIt7NVLqJBQvPAdGQ8kGZC43kO7YR3LTHKEvdnCOJegyylVqtEMhNEiztsZhQyTBxdJAT8S65SA23RieXlTomKziOXs8EgtllCTuns7oCutMBj+RFOGh6Zm4JHiehllM1cF4+CRBOGLCaPLOppuZH0/LvmM5dcnzr3nixhzHw1TBu9vMeiEDbtQj6byKA2aYfAgMBAAECggEAAywn/HkFuJR6boIzHoaXE2fTqPuv4besbe63SD185dTV7If9bW8POcgND0GpUXN2ECD4/yT2mZOvdgW1zEMlVsqNWq53SSwFAYPtxrzU52OwO85CWOxgcHU94mVTs75B5DptbtiUgB49BCb8d+GC89Y2S7Rsxk7Z8dzWtzOVFbJ7UTjuuEsl+djB31wYjstXhoiH2MVFffIVN9t4mCncETdgiuvZJ8Xofuym605nY06EMoNDJ4Q6PKQ3Au+c3ygSGpx455u+QPD8Z2J72TOfVD5kLod4vqTbkXVx57d2fdqHZ+dU7/DqO8PZi7yOxUjLbmirvBC0q5sAcushVBEVEQKBgQD4eDCJRJcUArY++tsiqH3GMRr60fF24HmMiAyZduHU0l30tujFnCG7jYGYDE7fWHcOErlt/o6UUKUDvOpr+THpLumlJFGg2ROExWnvS6ardee9xI7WP7b+Fe+YdZgJDnWFUUe8U97yW1OqF3GATu/muuwsmwO/8R76z4iqYoNg+wKBgQDnVduj5IfnQ/dhcxLbll6bIun1Vh0Ip+AvENub+vQC24J9+fPueKPCLMSanSfAnrt9sFKIni1J0M+UfSLuK44YFEl9lfDFOrEWgEeB9eAG4v9i2Ky+K8vMapZEWdTAG8g9ZSmUuf9PxH5Hzge7E10SocSD9dlzAeY2jW+GKdWuLQKBgFNPSUbdPqKw/hZD2jHWuMU5NcifmAy520dpXoAeQj4vgt1IbufDH/RYz/kBN3jfmpuXX6En1vqXx5xnPVqSpU+AMUrz0ILUBh1ga9KWVP3sfJU+UH45wsuS41EFLMLsnC7lP94/aONuB7FiRuY0kW+VgRa7qxgyaqbB6xrmCRL9AoGBAMKoIlt0b08Qh9a+J74Sd6T+6fMA9QlViXTRMJm9vxmwb8tlSfb+GH3Jgd3AI7UXXBjVB33h66XTSPhfm8bha32jhHticejkDCMs6f7XUB07YT5hqGmVHLXzcmItXLISLppKx4RYRhdu9fuhyCuIjaah7GRFuSXnEToSJSe1iBI5AoGBAPTrMEw3rGO+UosGbW+CFnGn1uKGPtblkXcSnYlD53GgtpFP2X4+9182UmuprgApLGNfttKbywLw/Gaax5+Ptql2pt+PdsEl43DdpHdLwgWCqT5NlBwgpsfcyOlFvZNL/43dj4DNg3kWdoHWAMexJrU+iUioGswgMmaim/XXSpbq";
    private final String CHARSET = "UTF-8";

    //支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtSoF80DVdjdD1o722fcIp/IXEpVoe6myIEfQ2+LEtd3BpOj5aOQWg0g+9PeJnzvaH/4KH2erS8o8pH+ViNsUZcBePeE8ong+hXYLS7vt50Ic1QIAXrnVR5lV6hYHM/JL7lcMyt6GBGkCrYZG8ayrCD4ege0zwCK+zupn0+bYCIuRk7lssJbq7d+VfabX7tbYhl8FzRKX8SK+/jf2FztaVnCrBBRmXaiHl7Wb8UHPAycj0Zpg8ZF2UbW754I+Ddp6DPxRemOQM6z2GxXLCdNrYzAjDB22fxFzGCDyZ9m826ZRGeynmljn2A1OlhbqK40zCLCZaOaGq0lDUrIR5TESwwIDAQAB";
    //沙箱接口路径 正式路径为https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private final String FORMAT = "json";

    //签名方式
    private final String SIGN_TYPE = "RSA2";

    //支付宝异步支付通知路径,付款完毕后会异步调用本项目的方法，必须为公网地址
    private final String NOTIFY_URL = "http://gfc6d384.natappfree.cc/api/alipay/toSuccess";

    //支付宝同步通知路径,付款完毕后跳转本项目的页面，可以不是公网地址
    private final String RETURN_URL = "http://gfc6d384.natappfree.cc/api/alipay/toSuccess"; /**  此处修改为支付完成后条转转界面  **/
    private AlipayClient alipayClient = null;

    public String sendRequestToAlipay(String outTradeNo, String totalAmount, String subject) throws AlipayApiException {
        alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(RETURN_URL);
        alipayRequest.setNotifyUrl(NOTIFY_URL);

        String amountStr = String.format("%.2f", Double.parseDouble(totalAmount));

        //商品描述（可空）
        String body = "";
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\","
                + "\"total_amount\":\"" + amountStr + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println("返回的结果是："+result );
        return result;
    }

    //    通过订单编号查询
    public String query(String id){
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", id);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = null;
        String body=null;
        try {
            response = alipayClient.execute(request);
            body = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return body;
    }
}
