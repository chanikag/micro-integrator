/*
 *Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *WSO2 Inc. licenses this file to you under the Apache License,
 *Version 2.0 (the "License"); you may not use this file except
 *in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 */

package org.wso2.carbon.esb.contenttype.json;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.test.utils.http.client.HttpRequestUtil;
import org.wso2.carbon.automation.test.utils.http.client.HttpResponse;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Test case for transforming XML payload to JSON
 */
public class XMLToJsonTestCase extends ESBIntegrationTest {

    @BeforeClass(alwaysRun = true)
    public void initialize() throws Exception {
        super.init();
    }

    @Test(groups = { "wso2.esb" }, description = "Test XML to JSON conversion")
    public void testXmlToJson() throws Exception {

        String xmlPayload =
                "<location>\n" + "               <name>Bermuda Triangle</name>\n" + "               <n>25.0000</n>\n"
                        + "               <w>71.0000</w>\n" + "            </location>";
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "text/xml");
        HttpResponse response = HttpRequestUtil.
                doPost(new URL(getProxyServiceURLHttp("xmlToJsonTestProxy")), xmlPayload, requestHeader);

        Assert.assertEquals(response.getData(),
                "{\"location\":{\"name\":\"Bermuda Triangle\",\"n\":25.0000,\"w\":71.0000}}",
                "Invalid XML to JSON conversion. " + response.getData());
    }

    @Test(groups = { "wso2.esb" }, description = "Test XML to JSON Array conversion")
    public void testXmlToJsonArray() throws Exception {

        String xmlPayload = "<coordinates>\n" + "    <location>\n" + "        <name>Bermuda Triangle</name>\n"
                + "        <n>25.0000</n>\n" + "        <w>71.0000</w>\n" + "    </location>\n" + "    <location>\n"
                + "        <name>Eiffel Tower</name>\n" + "        <n>48.8582</n>\n" + "        <e>2.2945</e>\n"
                + "    </location>\n" + " </coordinates>";
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "text/xml");
        HttpResponse response = HttpRequestUtil.
                doPost(new URL(getProxyServiceURLHttp("xmlToJsonTestProxy")), xmlPayload, requestHeader);

        Assert.assertTrue(response.getData().contains("{\"coordinates\":{\"location\":[{"),
                "Invalid XML to JSON array conversion . " + response.getData());
    }

    @Test(groups = { "wso2.esb" }, description = "Test XML with attributes to JSON conversion")
    public void testXmlAttributesToJson() throws Exception {

        String xmlPayload = "<location id=\"1\">\n" + "               <name>Bermuda Triangle</name>\n"
                + "               <n>25.0000</n>\n" + "               <w>71.0000</w>\n" + "            </location>";
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "text/xml");
        HttpResponse response = HttpRequestUtil.
                doPost(new URL(getProxyServiceURLHttp("xmlToJsonTestProxy")), xmlPayload, requestHeader);

        Assert.assertEquals(response.getData(),
                "{\"location\":{\"@id\":\"1\",\"name\":\"Bermuda Triangle\",\"n\":25.0000,\"w\":71.0000}}",
                "Invalid XML to JSON attribute conversion . " + response.getData());
    }

    @Test(groups = { "wso2.esb" }, description = "Test XML text nodes with attributes to JSON conversion")
    public void testXmlTextNodesWithAttributesToJson() throws Exception {

        String xmlPayload = "<location id=\"1\">Bermuda Triangle</location>";
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "text/xml");
        HttpResponse response = HttpRequestUtil.
                doPost(new URL(getProxyServiceURLHttp("xmlToJsonTestProxy")), xmlPayload, requestHeader);

        Assert.assertEquals(response.getData(), "{\"location\":{\"@id\":\"1\",\"$\":\"Bermuda Triangle\"}}",
                "Invalid XML to JSON attribute conversion . " + response.getData());
    }

    @Test(groups = { "wso2.esb" }, description = "Test empty XML node to JSON conversion")
    public void testEmptyXmlNodeToJson() throws Exception {

        String xmlPayload =
                "<location>\n" + "<name>Bermuda Triangle</name>\n" + "<description></description>\n" + "</location>";
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "text/xml");
        HttpResponse response = HttpRequestUtil.
                doPost(new URL(getProxyServiceURLHttp("xmlToJsonTestProxy")), xmlPayload, requestHeader);

        Assert.assertEquals(response.getData(), "{\"location\":{\"name\":\"Bermuda Triangle\",\"description\":null}}",
                "Invalid empty XML to JSON conversion . " + response.getData());
    }
}
