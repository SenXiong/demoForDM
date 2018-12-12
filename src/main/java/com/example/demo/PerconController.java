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
        PersonExtend pe = personRepository.getOne(333L);
        return pe.toString();
    }

    /*跳转路径如： http://localhost:9090/per/getxmldemo?id=333*/
    @RequestMapping("/getxmldemo")
    public String getxmldemo(String id){
        List<String> list = personRepository.getxmldemo(Long.parseLong(id));
        StringBuffer sb = new StringBuffer();
        for(String s : list){
            sb.append(s).append("   ");
        }
        return sb.toString();
    }

    @RequestMapping("/findonebyid")
    public String findonebyid(String id){
        PersonExtend pe = personRepository.getOne(Long.parseLong(id));
        return pe.toString();
    }

    /*跳转路径如：http://localhost:9090/per/findonepro?path=data/basic_info/zh_name   */
    @RequestMapping("/findonepro")
    public String findonepro(String path){
        PersonExtend pe = personRepository.getOne(333L);
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
