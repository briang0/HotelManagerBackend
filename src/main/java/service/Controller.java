package service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    //http://localhost:8080/testPhrase/?phrase="Hello"
    @RequestMapping("/testPhrase")
    public String testWithParam(@RequestParam(value = "phrase") String phrase) {
        return "Phrase received: " + phrase;
    }

    //http://localhost:8080/test
    @RequestMapping("/test")
    public String getCode() {
        return "Hit the endpoint";
    }

}
