package com.jiraConsoleApp.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.text.Format;
import java.util.Date;
import java.util.List;

public interface JiraService {
    List<JsonNode> getJiraDataList() throws Exception;

    void createXmlFile(List<JsonNode> jiraDataList, Date date, Format dateFormatter) throws IOException;

    void createJsonFile(List<JsonNode> jiraDataList, Date date, Format dateFormatter) throws IOException;
}
