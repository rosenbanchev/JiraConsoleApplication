package com.jiraConsoleApp.service.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jiraConsoleApp.service.JiraService;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JiraServiceImpl implements JiraService {
    @Override
    public List<JsonNode> getJiraDataList() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget jiraUrl = client.target("https://jira.atlassian.com/rest/api/latest/search?jql=issuetype%20in%20(Bug,%20Documentation,%20Enhancement)%20and%20updated%20%3E%20startOfWeek()&fields=key,summary,iconUrl,issuetype,priority,description,reporter,created,description,displayName,status,creator");

        Invocation.Builder jiraRequest = jiraUrl.request();
        jiraRequest.accept(MediaType.APPLICATION_JSON);

        Response jiraResponseData = jiraRequest.get();
        return transformDataToJsonObjList(jiraResponseData);
    }

    private List<JsonNode> transformDataToJsonObjList(Response response) throws Exception {
        List<JsonNode> jsonNodeList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            String jsonResponse = response.readEntity(String.class);
            JsonNode jsonNode = mapper.readTree(jsonResponse);
            JsonNode issuesNodes = jsonNode.get("issues");

            for (JsonNode issueNode : issuesNodes) {
                ObjectNode transformedNode = mapper.createObjectNode();
                transformedNode.put("summary", issueNode.get("fields").get("summary").asText());
                transformedNode.put("key", issueNode.get("key").asText());
                transformedNode.put("iconUrl", issueNode.get("fields").get("issuetype").get("iconUrl").asText());
                transformedNode.put("issuetype", issueNode.get("fields").get("issuetype").get("name").asText());
                transformedNode.put("priority", issueNode.get("fields").get("priority").get("id").asText());
                transformedNode.put("description", issueNode.get("fields").get("issuetype").get("description").asText());
                transformedNode.put("reporter", issueNode.get("fields").get("reporter").get("displayName").asText());
                transformedNode.put("created", issueNode.get("fields").get("created").asText());

                ObjectNode commentNode = mapper.createObjectNode();
                commentNode.put("id", issueNode.get("fields").get("status").get("description").asText());
                commentNode.put("description", issueNode.get("fields").get("creator").get("displayName").asText());
                transformedNode.set("comments", commentNode);

                jsonNodeList.add(transformedNode);
            }
        } else {
            jsonNodeList = null;
        }
        return jsonNodeList;
    }
    @Override
    public void createXmlFile(List<JsonNode> jiraDataList, Date date, Format dateFormatter) throws IOException {
        FileWriter file = new FileWriter("D:\\JiraFiles/jiraXmlFile_" + dateFormatter.format(date)+ ".xml");
        for (JsonNode jsonNode : jiraDataList) {
            file.write(getXmlString(jsonNode));
            file.write('\n');
            file.flush();
        }
        file.close();
    }

    @Override
    public void createJsonFile(List<JsonNode> jiraDataList, Date date, Format dateFormatter) throws IOException {
        FileWriter file = new FileWriter("D:\\JiraFiles/jiraJsonFile_" + dateFormatter.format(date) + ".json");
        for (JsonNode jsonNode : jiraDataList) {
            file.write(jsonNode.toString());
            file.write('\n');
            file.flush();
        }
        file.close();

    }

    private static String getXmlString(JsonNode jsonNode) throws IOException {
        ObjectMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(jsonNode);
    }
}
