package service;

import db.Connector;
import domain.ConciergeEntry;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;

/**
 * The controller for functionality related to the hotel rooms, and general hotel management
 * @Author: Brian Guidarini
 */
@RestController
public class Controller {

    Connection jdbc;

    //http://localhost:8080/testPhrase/?phrase="Hello"

    /**
     * Test endpoint, ignore
     * @param phrase
     * @return
     */
    @RequestMapping("/testPhrase")
    public String testWithParam(@RequestParam(value = "phrase") String phrase) {
        return "Phrase received: " + phrase;
    }

    //http://localhost:8080/test

    /**
     * Test endpoint, ignore
     * @return
     */
    @RequestMapping("/test")
    public String getCode() {
        return "Hit the endpoint";
    }















}
