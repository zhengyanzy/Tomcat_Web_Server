package com.zy.server;


import com.zy.servlet.LoginServlet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ParseWebXml{
    public static void parse() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //创建一个SAX解析器工厂对象;
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        //通过工厂对象创建SAX解析器
        SAXParser saxParser = saxParserFactory.newSAXParser();
        //创建一个数据处理器(自己实现)
        PersonHandle personHandle = new PersonHandle();
        //开始解析
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml");
        saxParser.parse(resourceAsStream,personHandle);
        personHandle.mappingEntityList.forEach(new Consumer<MappingEntity>() {
            @Override
            public void accept(MappingEntity mappingEntity) {
                System.out.println(mappingEntity.name);
            }
        });
        //personHandle.mappingEntityList.forEach((MappingEntity mappingEntity)->{System.out.println(mappingEntity);});
        //personHandle.servletEntityList.forEach((ServletEntity servletEntity)->{System.out.println(servletEntity);});
        WebContext.test1(personHandle.mappingEntityList, personHandle.servletEntityList);
        //WebContext webContext = new WebContext(personHandle.mappingEntityList, personHandle.servletEntityList);
    }
}

class PersonHandle extends DefaultHandler {
    public List<MappingEntity> mappingEntityList = null;
    public MappingEntity mappingEntity = null;
    public List<ServletEntity> servletEntityList = null;
    public ServletEntity servletEntity = null;
    private String tag;  //用来存储当前解析的标签名字
    private boolean isMapping = false;

    //开始解析文档时调用,只会执行一次
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mappingEntityList = new ArrayList<>();
        servletEntityList = new ArrayList<>();
        System.out.println("开始解析文档.....");
    }
    //结束解析文档时调用
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("结束解析文档.....");
    }
    //每一个标签开始时调用
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //获取每一个标签的person_id属性，如果没有返回null;
        //System.out.println(attributes.getValue("person_id"));
        if("servlet".equals(qName)){
            servletEntity = new ServletEntity();
        }
        if("servlet-mapping".equals(qName)){
            mappingEntity = new MappingEntity();
            isMapping = true;
        }
        tag = qName;
    }
    //每一个标签结束时调用
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("servlet".equals(qName)){
            servletEntityList.add(servletEntity);
        }
        if("servlet-mapping".equals(qName)){
            mappingEntityList.add(mappingEntity);
            isMapping = false;
        }
        tag=null;
    }
    //当解析到标签中的内容的时候调用(换行也是文本内容)
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(tag!=null){
            if (!isMapping){
                if ("servlet-name".equals(tag)){
                    servletEntity.name = new String(ch,start,length);
                }
                if ("servlet-class".equals(tag)){
                    servletEntity.className = new String(ch,start,length);
                }
            }else {
                if ("servlet-name".equals(tag)){
                    mappingEntity.name = new String(ch,start,length);
                }
                if ("url-pattern".equals(tag)){
                    mappingEntity.pattern.add(new String(ch,start,length));
                }
            }
        }
    }
}