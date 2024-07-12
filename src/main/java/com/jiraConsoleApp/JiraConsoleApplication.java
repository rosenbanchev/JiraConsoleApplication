package com.jiraConsoleApp;

import com.fasterxml.jackson.databind.JsonNode;
import com.jiraConsoleApp.service.JiraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class JiraConsoleApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(JiraConsoleApplication.class);

    @Autowired
    private JiraService jiraService;


    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(JiraConsoleApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("EXECUTING : command line runner");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please choose the wanted format (1 - XML, 2 - JSON): ");
        String chosenFormat = scanner.next();
        System.out.println(chosenFormat);

        List<JsonNode> jiraDataList = jiraService.getJiraDataList();
        System.out.println(jiraDataList);
        Date date = new Date();
        Format dateFormatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm-ss");

        if ("1".equals(chosenFormat)) {
            System.out.println("Chosen format is XML!");
            jiraService.createXmlFile(jiraDataList, date, dateFormatter);
            System.out.println("The XML file is created!");
        } else if ("2".equals(chosenFormat)) {
            System.out.println("Chosen format is JSON!");
            jiraService.createJsonFile(jiraDataList, date, dateFormatter);
            System.out.println("The JSON file is created!");
        } else {
            System.out.println("The format is incorrect!");
        }


    }

}
