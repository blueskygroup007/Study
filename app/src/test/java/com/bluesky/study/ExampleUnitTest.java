package com.bluesky.study;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testJson() throws JSONException {
        JSONObject object = new JSONObject();
        //string
        object.put("string","string");
        //int
        object.put("int",2);
        //boolean
        object.put("boolean",true);
        //array
        List<Integer> integers = Arrays.asList(1,2,3);
        object.put("list",integers);
        //null
        object.put("null",null);
        System.out.println(object);


    }
    /*public void testJson2() {
        JSONObject object = JSONObject.parseObject("{\"boolean\":true,\"string\":\"string\",\"list\":[1,2,3],\"int\":2}");
        //string
        String s = object.getString("string");
        System.out.println(s);
        //int
        int i = object.getIntValue("int");
        System.out.println(i);
        //boolean
        boolean b = object.getBooleanValue("boolean");
        System.out.println(b);
        //list
        List<Integer> integers = JSON.parseArray(object.getJSONArray("list").toJSONString(),Integer.class);
        integers.forEach(System.out::println);
        //null
        System.out.println(object.getString("null"));
    }

    public void testJson3(){
        JSONParser parser=new JSONParser();
        String s = "[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
        try{
            Object obj = parser.parse(s);
            JSONArray array = (JSONArray)obj;
            System.out.println("The 2nd element of array");
            System.out.println(array.get(1));
            System.out.println();
            JSONObject obj2 = (JSONObject)array.get(1);
            System.out.println("Field \"1\"");
            System.out.println(obj2.get("1"));

            s = "{}";
            obj = parser.parse(s);
            System.out.println(obj);

            s= "[5,]";
            obj = parser.parse(s);
            System.out.println(obj);

            s= "[5,,2]";
            obj = parser.parse(s);
            System.out.println(obj);
        }catch(ParseException pe){
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }
    }*/
}