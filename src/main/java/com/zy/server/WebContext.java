package com.zy.server;

import java.util.HashMap;
import java.util.List;

//首先只需要执行一次解析web.xml
//执行方式在服务器启动的时候,返回一个webContext(将其设置静态对象)
//每一次连接请求都需要将webContext传递进去吗(不需要,传递需要耗费空间)

public class WebContext {
    public static List<MappingEntity> mappingEntityList;
    public static HashMap<String,String> mappingEntity_map = new HashMap<>();
    public static List<ServletEntity> servletEntityList;
    public static HashMap<String,String> servletEntity_map = new HashMap<>();

    public static void test1(List<MappingEntity> mappingEntityList, List<ServletEntity> servletEntityList){
        servletEntityList.forEach((ServletEntity servletEntity)->{servletEntity_map.put(servletEntity.name,servletEntity.className);});
        for(MappingEntity m:mappingEntityList){
            for (String str:m.pattern){
                mappingEntity_map.put(str,m.name);
            }
        }
    }

    public static String getClz(String string){
        String s = mappingEntity_map.get(string);
        String s1 = servletEntity_map.get(s);
        return s1;
    }
}
