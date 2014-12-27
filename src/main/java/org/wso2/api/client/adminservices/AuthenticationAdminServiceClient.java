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

package org.wso2.api.client.adminservices;

import org.wso2.api.client.exception.APIStoreInvocationException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;

import java.rmi.RemoteException;


public class AuthenticationAdminServiceClient {

    private static AuthenticationAdminStub authenticationAdminStub;

    public static void init(String backEndServerURL) throws APIStoreInvocationException {

        try {
            authenticationAdminStub = new AuthenticationAdminStub(backEndServerURL);
        } catch (AxisFault axisFault) {
            String errMsg = "Axis2 soap faul occurred in AuthenticationAdminStub";
            throw new APIStoreInvocationException(errMsg, axisFault);
        }
        ServiceClient client = authenticationAdminStub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);
    }

    public static String login( String hostName, String userName, String password) throws APIStoreInvocationException {

        try {
            authenticationAdminStub.login(userName, password, hostName);
        } catch (RemoteException e) {
            String errMsg = "Establishing Connection Failed with back end when log-in";
            throw new APIStoreInvocationException(errMsg, e);
        } catch (LoginAuthenticationExceptionException e) {
            String errMsg = "log-in operation failed";
            throw new APIStoreInvocationException(errMsg, e);
        }
        ServiceContext serviceContext = authenticationAdminStub.
                _getServiceClient().getLastOperationContext().getServiceContext();
        return (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
    }

    public static void setSystemProperties(String keyStorePath,String keyStoreType, String keyStorePassword ){

        System.setProperty("javax.net.ssl.trustStore", keyStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);
        System.setProperty("javax.net.ssl.trustStoreType", keyStoreType);

    }
}
