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

package org.wso2.api.client.constant;

public class APIDownloaderConstant {

    public static final String CLIENT_TRUST_STORE_PATH = "/home/firzhan/api-definition/key-store/wso2carbon.jks";

    public static final String HOST_NAME = "localhost";
    public static final String HTTPS_PORT = "9443";
    public static final String SERVICE_URL = "https://" + HOST_NAME + ":" + HTTPS_PORT + "/services/";
    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "admin";
    public static final String KEY_STORE_PASSWORD = "wso2carbon";
    public static final String KEY_STORE_TYPE = "jks";

    public static final String AUTHENTICATION_ADMIN = "AuthenticationAdmin";
    public static final String TENANT_ADMIN = "TenantMgtAdminService";

    public static final String GET_API_DOC_LIST_CALL= "https://" + HOST_NAME + ":" + HTTPS_PORT +
            "/store/site/blocks/api/listing/ajax/list.jag?action=getAllPaginatedPublishedAPIs&tenant=%s&start=1&end=5";

    public static final String LOGIN_API_CALL = "https://" + HOST_NAME + ":" + HTTPS_PORT +
                    "/store/site/blocks/user/login/ajax/login.jag?action=login&username=" +
                    USER_NAME + "&password=" + PASSWORD;

    public static final String FETCH_TENANT_API_DEFINITION = "https://"+ HOST_NAME + ":" + HTTPS_PORT +
            "/t/%s/registry/resource/_system/governance/apimgt/applicationdata/api-docs/%s-%s-%s";

    public static final String FETCH_SUPER_TENANT_API_DEFINITION = "https://"+ HOST_NAME + ":" + HTTPS_PORT +
            "/registry/resource/_system/governance/apimgt/applicationdata/api-docs/%s-%s-%s";

    public static final String SWAGGER_FULL_DOC_PATH = "/1.2/default";
    public static final String SWAGGER_INDEX_DOC_PATH = "/1.2/api-doc";

    public static final String SWAGGER_API_DOC_PATH = "/home/firzhan/api-definition/swagger-api/";

    public static final String SUPER_TENANT_ID = "carbon.super";

    // ########## JSON defined values ##########################

    public static final String JSON_ARRAY_APIS = "apis";


}
