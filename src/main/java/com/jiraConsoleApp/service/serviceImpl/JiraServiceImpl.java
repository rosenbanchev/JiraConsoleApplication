package com.jiraConsoleApp.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jiraConsoleApp.model.JiraCommentModel;
import com.jiraConsoleApp.model.JiraIssueModel;
import com.jiraConsoleApp.service.JiraService;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JiraServiceImpl implements JiraService {
    @Override
    public List<JiraIssueModel> getJiraDataModelList() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget jiraUrl = client.target("https://jira.atlassian.com/rest/api/latest/search?jql=issuetype%20in%20(Bug,%20Documentation,%20Enhancement)%20and%20updated%20%3E%20startOfWeek()&fields=key,summary,iconUrl,issuetype,priority,description,reporter,created,description,displayName,status,creator");

        Invocation.Builder jiraRequest = jiraUrl.request();
        jiraRequest.accept(MediaType.APPLICATION_JSON);

        Response jiraResponseData = jiraRequest.get();
        return setResponseToJiraList(jiraResponseData);
    }
    private List<JiraIssueModel> setResponseToJiraList(Response jiraResponseData) throws JsonProcessingException {
        List<JiraIssueModel> jiraIssueModelList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        if (jiraResponseData.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            String jsonResponse = jiraResponseData.readEntity(String.class);
            JsonNode jsonNode = mapper.readTree(jsonResponse);
            JsonNode issuesNodes = jsonNode.get("issues");

            for (JsonNode issueNode : issuesNodes) {
                JiraIssueModel jiraIssueModel = new JiraIssueModel();
                jiraIssueModel.setSummary(issueNode.get("fields").get("summary").asText());
                jiraIssueModel.setKey(issueNode.get("key").asText());
                jiraIssueModel.setIconUrl(issueNode.get("fields").get("issuetype").get("iconUrl").asText());
                jiraIssueModel.setIssueType(issueNode.get("fields").get("issuetype").get("name").asText());
                jiraIssueModel.setPriority(issueNode.get("fields").get("priority").get("id").asText());
                jiraIssueModel.setDescription(issueNode.get("fields").get("issuetype").get("description").asText());
                jiraIssueModel.setReporter(issueNode.get("fields").get("reporter").get("displayName").asText());
                jiraIssueModel.setCreatedDate(issueNode.get("fields").get("created").asText());

                // Can't find the comments for any task!!!
                List<JiraCommentModel> jiraCommentModelList = new ArrayList<>();
                JiraCommentModel jiraCommentModel = new JiraCommentModel();
                jiraCommentModel.setAuthor(issueNode.get("fields").get("creator").get("displayName").asText());
                jiraCommentModel.setText(issueNode.get("fields").get("status").get("description").asText());

                jiraCommentModelList.add(jiraCommentModel);
                jiraIssueModel.setComments(jiraCommentModelList);
                jiraIssueModelList.add(jiraIssueModel);
            }
            return jiraIssueModelList;
        }else {
            return null;
        }
    }
    @Override
    public void createXmlFile(List<JiraIssueModel> jiraIssueModelList, Date date, Format dateFormatter) throws IOException {
        FileWriter file = new FileWriter("D:\\JiraFiles/jiraXmlFile_" + dateFormatter.format(date)+ ".xml");
        for (JiraIssueModel jiraIssueModel : jiraIssueModelList) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLEncoder xmlEncoder = new XMLEncoder(baos);
            xmlEncoder.writeObject(jiraIssueModel);
            xmlEncoder.close();

            String xml = baos.toString();
            System.out.println(xml);
            file.write(xml);
            file.write('\n');
            file.flush();
        }
        file.close();
    }

    @Override
    public void createJsonFile(List<JiraIssueModel> jiraIssueModelList, Date date, Format dateFormatter) throws IOException {
        FileWriter file = new FileWriter("D:\\JiraFiles/jiraJsonFile_" + dateFormatter.format(date) + ".json");
        for (JiraIssueModel jiraIssueModel : jiraIssueModelList) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(jiraIssueModel);
            file.write(json);
            file.write('\n');
            file.flush();
        }
        file.close();
    }
}
