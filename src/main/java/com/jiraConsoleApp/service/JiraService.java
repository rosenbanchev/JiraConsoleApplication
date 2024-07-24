package com.jiraConsoleApp.service;

import com.jiraConsoleApp.model.JiraIssueModel;

import java.io.IOException;
import java.text.Format;
import java.util.Date;
import java.util.List;

public interface JiraService {

    List<JiraIssueModel> getJiraDataModelList() throws Exception;

    void createXmlFile(List<JiraIssueModel> jiraIssueModelList, Date date, Format dateFormatter) throws IOException;

    void createJsonFile(List<JiraIssueModel> jiraIssueModelList, Date date, Format dateFormatter) throws IOException;
}
