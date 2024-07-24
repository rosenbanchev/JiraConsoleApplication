package com.jiraConsoleApp;

import com.jiraConsoleApp.model.JiraIssueModel;
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

    private static final String XML_FORMAT = "1";
    private static final String GSON_FORMAT = "2";

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
        System.out.println("Choose one of the options:");
        System.out.println("1 to create a XML file!");
        System.out.println("2 to create a JSON file!");
        System.out.print("Please choose the wanted format: ");
        String chosenFormat = scanner.next();

        List<JiraIssueModel> jiraIssueModelList = jiraService.getJiraDataModelList();
        Date date = new Date();
        Format dateFormatter = new SimpleDateFormat("MM-dd-yyyy_hh-mm-ss");

        switch (chosenFormat) {
            case XML_FORMAT:
                System.out.println("Chosen format is XML!");
                jiraService.createXmlFile(jiraIssueModelList, date, dateFormatter);
                System.out.println("The XML file is created!");
                break;
            case GSON_FORMAT:
                System.out.println("Chosen format is JSON!");
                jiraService.createJsonFile(jiraIssueModelList, date, dateFormatter);
                System.out.println("The JSON file is created!");
                break;
            default:
                System.out.println("The format is incorrect!");
        }
    }
}
