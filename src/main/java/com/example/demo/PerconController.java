package com.example.demo;

import com.example.demo.tools.XMLHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/per")
public class PerconController {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/addPerson")
    public void addPerson(PersonExtend person) {
        personRepository.save(person);
    }

    @DeleteMapping("/deletePerson")
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    @RequestMapping("/findone")
    public String findone(){
        PersonExtend pe = personRepository.getOne(444L);
        return pe.toString();
    }

    /**
     * 展示数据层对xml的解析   xmltable,extractvalue等
     * 跳转路径如： http://localhost:9090/per/getxmldemo?id=444
     * @param id
     * @return
     */

    @RequestMapping("/getxmldemo")
    public String getxmldemo(String id){
        List<String> list = personRepository.getxmldemo(Long.parseLong(id));
        StringBuffer sb = new StringBuffer();
        for(String s : list){
            sb.append(s).append("   ");
        }
        return sb.toString();
    }

    /**
     *   展示java层对xml的解析
     *   跳转路径如：http://localhost:9090/per/findonebyid?id=444
     * @param id
     * @return
     */
    @RequestMapping("/findonebyid")
    public String findonebyid(String id){
        PersonExtend pe = personRepository.getOne(Long.parseLong(id));
        String info = XMLHelper.parseDoc(pe.getPsnXml()).asXML();
        StringBuffer sb = new StringBuffer();
        sb.append("姓名：").append(XMLHelper.getNodeValueFromXML(info,"data/basic_info/zh_name")).append("<br/>");
        sb.append("单位：").append(XMLHelper.getNodeValueFromXML(info,"data/basic_info/org_name")).append("<br/>");
        sb.append("学历：").append(XMLHelper.getNodeValueFromXML(info,"data/basic_info/educate_name")).append("<br/>");

        return sb.toString();
    }

    /**
     * 跳转路径如：http://localhost:9090/per/findonebyid?path=data/basic_info/zh_name
     * @param path
     * @return
     */
    @RequestMapping("/findonepro")
    public String findonepro(String path){
        PersonExtend pe = personRepository.getOne(444L);
        return XMLHelper.getNodeValueFromXML(XMLHelper.parseDoc(pe.getPsnXml()).asXML(),path);
    }

    @RequestMapping("/hello2")
    public String getString2(){
        return "3332";
    }

    @RequestMapping()
    @ResponseBody
    public String test(){
        return "test";
    }

}
