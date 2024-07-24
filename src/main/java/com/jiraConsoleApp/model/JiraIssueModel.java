package com.jiraConsoleApp.model;

import java.util.List;

public class JiraIssueModel {
    private String summary;
    private String key;
    private String iconUrl;
    private String issueType;
    private String priority;
    private String description;
    private String reporter;
    private String createdDate;
    private List<JiraCommentModel> comments;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<JiraCommentModel> getComments() {
        return comments;
    }

    public void setComments(List<JiraCommentModel> comments) {
        this.comments = comments;
    }
}
