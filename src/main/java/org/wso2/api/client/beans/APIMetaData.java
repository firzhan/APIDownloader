package org.wso2.api.client.beans;

public class APIMetaData {

    private String apiVersion;
    private String apiName;
    private String apiProvider;
    private String apiDocRequest;
    private String apiIndexRequest;

    public APIMetaData(){

    }

    public String getApiIndexRequest() {
        return apiIndexRequest;
    }

    public void setApiIndexRequest(String apiIndexRequest) {
        this.apiIndexRequest = apiIndexRequest;
    }

    public String getApiDocRequest() {
        return apiDocRequest;
    }

    public void setApiDocRequest(String apiDocRequest) {
        this.apiDocRequest = apiDocRequest;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiProvider() {
        return apiProvider;
    }

    public void setApiProvider(String apiProvider) {
        this.apiProvider = apiProvider;
    }
}
