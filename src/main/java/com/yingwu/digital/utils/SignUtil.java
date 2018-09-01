package com.yingwu.digital.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.*;

/**
 * @author Created by: zhangbingbing
 * @date 2018/8/29
 **/
public class SignUtil {

    @Value("${okex_apiKey}")
    private static String okex_apiKey;

    public static Map<String, Object> signString(Map<String, Object> param) {
        Map<String, Object> signMap = new HashMap<>(param);
        signMap.put("accesskey", okex_apiKey);
//        signMap.put("secretkey", secretkey);
        List<String> keyList = new ArrayList<>(signMap.keySet());
        Collections.sort(keyList);
        StringBuilder sign = new StringBuilder();
        for (String key : keyList) {
            if (signMap.get(key) != null) {
                sign.append(key).append("=").append(signMap.get(key)).append("&");
            }
        }
        if (sign.charAt(sign.length() - 1) == '&') {
            sign.deleteCharAt(sign.length() - 1);
        }
        String signValue = ""/*MD5Util.encrypt(sign.toString()).toLowerCase()*/;
        Map<String, Object> common = new LinkedHashMap<>();
        common.put("accesskey", okex_apiKey);
        common.put("sign", signValue);
        Map<String, Object> outPutMap = new LinkedHashMap<>();
        outPutMap.put("common", common);
        outPutMap.put("data", param);
        return outPutMap;
    }

    /**
     * 质数验签
     *
     * @param param
     * @return
     */
    public Map<String, Object> validateSign(Map<String, Object> param) {
        Map<String, Object> signMap = new HashMap<>(param);
//        //获取 密匙
//        signMap.put("accesskey", accessKeyXf);
//        signMap.put("secretkey", secretkeyXf);
//        List<String> keyList = new ArrayList<>(signMap.keySet());
//        Collections.sort(keyList);
//        StringBuilder sign = new StringBuilder();
//        for (String key : keyList) {
//            if (signMap.get(key) != null) {
//                sign.append(key).append("=").append(signMap.get(key)).append("&");
//            }
//        }
//        if (sign.charAt(sign.length() - 1) == '&') {
//            sign.deleteCharAt(sign.length() - 1);
//        }
//        String signValue = MD5Util.encrypt(sign.toString()).toLowerCase();
//        Map<String, Object> common = new LinkedHashMap<>();
//        common.put("accesskey", accessKeyXf);
//        common.put("sign", signValue);
        Map<String, Object> outPutMap = new LinkedHashMap<>();
//        outPutMap.put("common", common);
//        outPutMap.put("data", param);
        return outPutMap;
    }

    /**
     * 把需要验签的参数封装到TreeMap中,生成加密sign串
     *
     * @param map
     * @return
     */

    public static String generateSignByRule(TreeMap map) {
        String sign = "";
        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> entry = it.next();
            if (it.hasNext()) {
                if (!(entry.getValue() == null || entry.equals(""))) {
                    sign += entry.getKey() + "=" + entry.getValue() + "&";
                }
            } else {
                if (!(entry.getValue() == null || entry.equals(""))) {
                    sign += entry.getKey() + "=" + entry.getValue();
                } else {
                    sign = sign.substring(0, sign.length() - 1);
                }
            }
        }

        return getMD5(sign);
    }

    public static String getMD5(String md5Str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(md5Str.getBytes("utf-8"));
            //byte[] bytes = md5.digest(md5Str.getBytes());
            StringBuffer sb = new StringBuffer();
            String temp = "";
            for (byte b : bytes) {
                temp = Integer.toHexString(b & 0XFF);
                sb.append(temp.length() == 1 ? "0" + temp : temp);
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 密码加密
     */
    public static String getPasswordMd5(String password) {

        String signValue = ""/*MD5Util.encrypt(password)*/;

        return signValue;
    }

}
