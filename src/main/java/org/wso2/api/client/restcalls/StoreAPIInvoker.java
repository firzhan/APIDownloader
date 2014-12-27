/**
 *  Copyright (c) 2014 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.api.client.restcalls;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.api.client.beans.APIMetaData;
import org.wso2.api.client.constant.APIDownloaderConstant;
import org.wso2.api.client.exception.APIStoreInvocationException;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.*;

public class StoreAPIInvoker {

    private static final Log log = LogFactory.getLog(StoreAPIInvoker.class);

    public static String loginToStore(HttpClient httpClient) throws IOException {
        HttpPost storeLoginRequest = new HttpPost(APIDownloaderConstant.LOGIN_API_CALL);
        HttpResponse storeLoginResponse = httpClient.execute(storeLoginRequest);

        for (Header header : storeLoginResponse.getAllHeaders()) {
            if (header.getName().equals("Set-Cookie")) {
                return header.getValue().split(";", 2)[0];
            }
        }
        return null;
    }

    public static APIMetaData fetchAPIMetaData( HttpClient httpClient, String tenantID,
                                      String cookie) throws APIStoreInvocationException {

        String apiDocInvokeCall;
        String apiDefinition;
        String apiRequest;
        if(APIDownloaderConstant.SUPER_TENANT_ID.equals(tenantID)){
           apiDocInvokeCall = String.format(APIDownloaderConstant.GET_API_DOC_LIST_CALL,
                    APIDownloaderConstant.SUPER_TENANT_ID);
            apiDefinition = APIDownloaderConstant.FETCH_SUPER_TENANT_API_DEFINITION;
        } else {
            apiDocInvokeCall = String.format(APIDownloaderConstant.GET_API_DOC_LIST_CALL, tenantID);
            apiDefinition = APIDownloaderConstant.FETCH_TENANT_API_DEFINITION;
        }

        String apiJsonMetaData = fetchGetAPIResults(httpClient, apiDocInvokeCall, cookie);

        JSONObject jsonObject = new JSONObject(apiJsonMetaData);

        APIMetaData apiMetaData = new APIMetaData();

        apiMetaData.setApiVersion( (String) jsonObject.getJSONArray(APIDownloaderConstant.JSON_ARRAY_APIS).getJSONObject(0).get
                ("version") );
        apiMetaData.setApiName((String) jsonObject.getJSONArray(APIDownloaderConstant.JSON_ARRAY_APIS).getJSONObject(0).get("name"));
        apiMetaData.setApiProvider((String) jsonObject.getJSONArray(APIDownloaderConstant.JSON_ARRAY_APIS).getJSONObject(0).get("provider"));

        if(!APIDownloaderConstant.SUPER_TENANT_ID.equals(tenantID)){
            if (apiMetaData.getApiProvider().contains("@")){
                apiMetaData.setApiProvider(apiMetaData.getApiProvider().replace("@", "-AT-"));
            }
            apiRequest = String.format(apiDefinition, tenantID, apiMetaData.getApiName(),apiMetaData.getApiVersion(),
                    apiMetaData.getApiProvider());
        } else {
            apiRequest = String.format(apiDefinition, apiMetaData.getApiName(),apiMetaData.getApiVersion(),
                    apiMetaData.getApiProvider());
        }

        apiMetaData.setApiDocRequest(apiRequest + APIDownloaderConstant.SWAGGER_FULL_DOC_PATH);
        apiMetaData.setApiIndexRequest(apiRequest + APIDownloaderConstant.SWAGGER_INDEX_DOC_PATH);

        return apiMetaData;
    }

    public static void fetchAPIDefinition( HttpClient httpClient, APIMetaData apiMetaData,
                                             String cookie) throws APIStoreInvocationException {
        String apiDefinition = fetchGetAPIResults(httpClient, apiMetaData.getApiDocRequest(), cookie);
        String apiIndexDocDefinition = fetchGetAPIResults(httpClient, apiMetaData.getApiIndexRequest(), cookie);
        String basePath = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            basePath = stringBuilder.append(APIDownloaderConstant.SWAGGER_API_DOC_PATH).append(apiMetaData
                    .getApiName()).append("-").append(apiMetaData.getApiVersion()).append("-").append(apiMetaData.
                    getApiProvider()).toString();
            if( new File(basePath).mkdir() ) {
                FileUtils.writeStringToFile(new File(basePath + File.separator + "default"), apiDefinition);
                FileUtils.writeStringToFile(new File(basePath +File.separator + "api-doc"), apiIndexDocDefinition);
                log.info("Swagger api-definition successfully created for " + basePath);
            } else  {
                log.error("Swagger api-definition creation failed for " + basePath);
            }
        } catch (IOException e) {
           throw new APIStoreInvocationException("File Writing operation failed " + basePath, e);
        }

    }

    private static String fetchGetAPIResults( HttpClient httpClient, String apiRequest,
                                         String cookie) throws APIStoreInvocationException {

        HttpGet fetchAPIRequest = new HttpGet(apiRequest);
        fetchAPIRequest.setHeader("Cookie", cookie);

        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            HttpResponse fetchAPIResponse = httpClient.execute(fetchAPIRequest);
            HttpEntity entity = fetchAPIResponse.getEntity();
            if(entity != null){
                inputStream = entity.getContent();
                if (inputStream != null){
                    reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
                    return reader.readLine();
                }
            }
        } catch (IOException e) {
            String errMsg = "Error Occured while invoking fetchAPIRequest for query = " + apiRequest;
            throw new APIStoreInvocationException(errMsg, e);
        }finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
               log.error(" Stream/Reader closer operation failed", e);
            }

        }
        return null;
    }
}
