package com.test.jm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.jm.domain.tree.ApiCase;
import com.test.jm.domain.tree.ApiTree;
import com.test.jm.domain.tree.Case;
import com.test.jm.domain.tree.CaseTree;
import com.test.jm.dto.CaseExtend;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.dto.test.CaseDTO;
import com.test.jm.keys.CommonType;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    //json提取

    //正则提取需传入参数
    public static List<String> getMatchList(String source) {
        List<String> list = new ArrayList<>();
        if(StringUtils.isNotBlank(source) && !"{}".equalsIgnoreCase(source)){
            Pattern regex = Pattern.compile("\\$\\{([^}]*)\\}");
            Matcher matcher = regex.matcher(source);
            //循环，字符串中有多少个符合的，就循环多少次
            while(matcher.find()) {
                //每一个符合正则的字符串
                String e = matcher.group();
                list.add(e);
            }
        }
        return list;
    }

    //正则提取所需参数值
    public static String regMatch(String pre, String post, String source){
        String result = null;
        if(StringUtils.isNotBlank(pre) && StringUtils.isNotBlank(post)){
            String rgex = pre + "(.*?)" + post;
            Pattern pattern = Pattern.compile(rgex);
            Matcher matcher = pattern.matcher(source);
            while (matcher.find()) {
                result = matcher.group(1);
            }
        }
        return result;
    }


    //str特殊字符替换
    public static String replaceStr(String special_str){
        if(StringUtils.isNotBlank(special_str)){
            String final_str = special_str
                    .replaceAll("\\$\\{",Matcher.quoteReplacement("\\$\\{"))
                    .replaceAll("\\}",Matcher.quoteReplacement("\\}"));
            System.out.println("final_str: "+ final_str);
            return final_str;
        }
        return null;
    }

    //常亮字符替换
    public static void replaceCommon(ApiDTO apiDTO, String special_str){
        if(null != special_str){
            switch (special_str){
                case CommonType.TELNO:
                    setCommon(apiDTO, replaceStr(special_str), get_tel_no());
                    break;
                case CommonType.IDCARD:
                    setCommon(apiDTO, replaceStr(special_str), get_id_card());
                    break;
                case CommonType.BANKCARD:
                    setCommon(apiDTO, replaceStr(special_str), get_bank_card());
                    break;
                default:
                    break;
            }
        }
    }

    //前置字符替换
    public static void replacePre(ApiDTO apiDTO, String special_str){
        if(null != special_str){
            String special = special_str.split("\\{")[1].split("\\}")[0];
            Object getPre = RequestThreadLocal.getInfo().getOrDefault( special, "replacePre");
            if(!"replacePre".equals(getPre)){
                apiDTO.setBody(apiDTO.getBody().replaceAll(replaceStr(special_str), String.valueOf(getPre)));
            }
        }
    }

    public static void setCommon(ApiDTO apiDTO, String special_str, String str){
        apiDTO.setBody(apiDTO.getBody().replaceAll(special_str, str));
    }

    public static String ObjectToJsonStr(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static boolean isJson(String content) {
        if(StringUtils.isBlank(content)){
            return false;
        }
        try {
            JSONObject jsonObj = JSON.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Map<String, Object> strToMap(String str){
        if(StringUtils.isNotBlank(str)){
            Map<String, Object> map = JSONObject.parseObject(str);
            return map;
        }
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    public static String HeaderListToMap(List<Header> lists){
        Map<String, Object> m = new HashMap<>();
        try {
            for (Header header: lists) {
                m.put(header.getName(), header.getValue());
            }
            JSONObject json = new JSONObject(m);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return lists.toString();
        }

    }

    public static String CookieListToMap(List<Cookie> lists){
        Map<String, Object> m = new HashMap<>();
        try {
            for (Cookie cookie: lists) {
                m.put(cookie.getName(), cookie.getValue());
            }
            JSONObject json = new JSONObject(m);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return lists.toString();
        }

    }

    public static List caseTree(List<CaseExtend> caseExtends){
        List all = new LinkedList();
        for (int i = 0; i < caseExtends.size(); i++) {
            CaseExtend cc = caseExtends.get(i);
            CaseTree caseTree = new CaseTree();
            caseTree.setId(cc.getPid());
            caseTree.setLabel(cc.getProject_name());
            List cd = new ArrayList<>();
            for (CaseDTO caseDTO:cc.getCaseExtends()) {
                Case c = new Case();
                c.setId(caseDTO.getId());
                c.setLabel(caseDTO.getName());
                cd.add(c);
            }
            caseTree.setChildren(cd);
            all.add(caseTree);
        }

        return all;
    }

    public static List apiTree(List<CaseExtend> caseExtends){
        List all = new LinkedList();
        for (int i = 0; i < caseExtends.size(); i++) {
            CaseExtend cc = caseExtends.get(i);
            ApiTree apiTree = new ApiTree();
            apiTree.setValue(cc.getPid());
            apiTree.setLabel(cc.getProject_name());
            List cd = new ArrayList<>();
            for (CaseDTO caseDTO:cc.getCaseExtends()) {
                ApiCase c = new ApiCase();
                c.setValue(caseDTO.getId());
                c.setLabel(caseDTO.getName());
                cd.add(c);
            }
            apiTree.setChildren(cd);
            all.add(apiTree);
        }

        return all;
    }

    //生成手机号
    public static String get_tel_no(){
        String[] telFirst="13,14,15,16,17,18,19".split(",");
        String first=telFirst[new Random().nextInt(7)];
        String second= new Random().nextInt(89999999)+100000000+"";
        String telno = first + second;
        return telno;
    }

    //生成身份证
    public static String get_id_card(){
        String[] IDCard = getIDCard();
        return IDCard[0];
    }

    //生成银行卡
    public static String get_bank_card(){
        String[] pre="95588,62220".split(",");
        String first=pre[new Random().nextInt(2)] + "0120291";
        String cardCode1 = new Random().nextInt(899999) + 1000000 + "";
        String bankCard = first + cardCode1;
        return bankCard;
    }

    private static String[] getIDCard() {
        String id = "";
        String[] provinces = { "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37",
                "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71",
                "81", "82" };
        String province = provinces[new Random().nextInt(provinces.length - 1)];
        String[] citys = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "21", "22", "23", "24", "25",
                "26", "27", "28" };
        String city = citys[new Random().nextInt(citys.length - 1)];
        String[] countys = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "21", "22", "23", "24", "25",
                "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38" };
        String county = countys[new Random().nextInt(countys.length - 1)];
        // 随机生成出生年月 7-14
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dft1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - new Random().nextInt(365 * 100));
        //获取生日日期
        String birthday=dft1.format(date.getTime());
        String birth = LocalDateTime.now().minusYears(new Random().nextInt(52)+18).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String no = new Random().nextInt(899)+100 + "";
        String sex=no.substring(no.length()-1);
        if(Integer.parseInt(sex)%2==0){
            sex="2";
        }else{
            sex="1";
        }
        id = province + city + county + birth + no;
        String check = getCheckCode(id);
        // 拼接身份证号码
        id = province + city + county + birth + no + check;
        String[] cardInfo={id,sex,birthday};

        return cardInfo;
    }

    private static String getCheckCode(String number) {
        /*
         * 第十八位数字的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2
         * 1 6 3 7 9 10 5 8 4 2 2.将这17位数字和系数相乘的结果相加。 3.用加出来和除以11，看余数是多少？
         * 4余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5
         * 4 3 2。 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
         *
         */
//        System.out.println(number);
        final int[] coef = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        final int[] mapping = { 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 };
        int[] store = new int[18];
        String str = null;
        if (number.length() == 17) {

            // 取出17位的每一个值
            for (int i = 0; i < number.length(); i++) {
                String s = number.substring(i, i + 1);
                store[i] = Integer.parseInt(s);
            }

            // 乘以不同的系数，将这17位数字和系数相乘的结果相加
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum = sum + store[i] * coef[i];
            }

            // 用加出来和除以11，看余数是多少？
            int remaining = sum % 11;
            // remaining = 2:'X'?
            if (remaining == 2) {
                str = "X";
            } else {
                str = Integer.toString(mapping[remaining]);
            }
        } else {
            System.out.println("位数不对，出错！");
        }
        return str;
    }




}
