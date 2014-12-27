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

package org.wso2.api.client;

import org.wso2.api.client.beans.APIMetaData;
import org.wso2.api.client.constant.APIDownloaderConstant;
import org.wso2.api.client.exception.APIStoreInvocationException;
import org.wso2.api.client.restcalls.StoreAPIInvoker;
import org.apache.http.client.HttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.api.client.adminservices.AuthenticationAdminServiceClient;
import org.wso2.api.client.adminservices.TenantMgtAdminServiceClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;

public class APIDownloader {

    private static final Log log = LogFactory.getLog(APIDownloader.class);
    private static HttpClient httpClient = null;

    private static void invokeApiCall(){

    }

    public static void main(String[] args) throws Exception {

        String adminCookie;
        String authenticationAdminURL = APIDownloaderConstant.SERVICE_URL + APIDownloaderConstant.AUTHENTICATION_ADMIN;
        String tenantAdminURL = APIDownloaderConstant.SERVICE_URL + APIDownloaderConstant.TENANT_ADMIN;

        // setting the system properties for javax.net.ssl
        AuthenticationAdminServiceClient.setSystemProperties(APIDownloaderConstant.CLIENT_TRUST_STORE_PATH,
                APIDownloaderConstant.KEY_STORE_TYPE, APIDownloaderConstant.KEY_STORE_PASSWORD);
        AuthenticationAdminServiceClient.init(authenticationAdminURL);
        log.info("retrieving the admin cookie from the logged in session....");
        adminCookie = AuthenticationAdminServiceClient.login(APIDownloaderConstant.HOST_NAME,
                APIDownloaderConstant.USER_NAME, APIDownloaderConstant.PASSWORD);

        List<String> tenantDomainList = null;
        if(adminCookie != null){
            log.info("logged in to the back-end server successfully....");

            TenantMgtAdminServiceClient.init(tenantAdminURL, adminCookie);
            tenantDomainList = TenantMgtAdminServiceClient.getAllTenants();
        } else {
            throw new APIStoreInvocationException("Login Failed to Admin service");
        }

        httpClient = HttpClientBuilder.create().build();
        String storeCookie = StoreAPIInvoker.loginToStore(httpClient);

        if(storeCookie == null){
            throw new APIStoreInvocationException("Login Failed to API Store");
        }

        for (int i = 0; i < tenantDomainList.size(); i++){
            APIMetaData apiMetaData = StoreAPIInvoker.fetchAPIMetaData(httpClient, tenantDomainList.get(i), storeCookie);
            StoreAPIInvoker.fetchAPIDefinition(httpClient, apiMetaData, storeCookie);
        }
    }
}
