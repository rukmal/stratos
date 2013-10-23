/*
 * Licensed to the Apache Software Foundation (ASF) under one 
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY 
 * KIND, either express or implied.  See the License for the 
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.stratos.cloud.controller.util;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * We keep information regarding a service (i.e. a cartridge instance)
 * in this object.
 */
public class ServiceContext implements Serializable{

    private static final long serialVersionUID = -6740964802890082678L;
    private File file;
	private String clusterId;
    private String tenantRange;
    private String hostName;
    private String payloadFilePath;
    private String cartridgeType;
    private Cartridge cartridge;
    private byte[] payload;
    private String autoScalerPolicyName;
    /**
     * Key - Value pair.
     */
    private Map<String, String> properties = new HashMap<String, String>();
    /**
     * Key - IaaS Type
     * Value - {@link IaasContext} object
     */
    private Map<String, IaasContext> iaasCtxts = new HashMap<String, IaasContext>();
	
    public Map<String, IaasContext> getIaasCtxts() {
    	return iaasCtxts;
    }

	public String getClusterId() {
        return clusterId;
    }
    
    public boolean setClusterId(String domainName) {
        if (!"".equals(domainName)) {
            this.clusterId = domainName;
            return true;
        }
        
        return false;
    }
    
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
    
    public String getProperty(String key) {
        
        if(properties.containsKey(key)){
            return properties.get(key);
        }
        
        return "";
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
    

    public Cartridge getCartridge() {
        return cartridge;
    }

    public void setCartridge(Cartridge cartridge) {
        this.cartridge = cartridge;
    }

	public String getTenantRange() {
	    return tenantRange;
    }

	public void setTenantRange(String tenantRange) {
	    this.tenantRange = tenantRange;
    }
	
	public IaasContext addIaasContext(String iaasType){
		IaasContext ctxt = new IaasContext(iaasType);
		iaasCtxts.put(iaasType, ctxt);
		return ctxt;
	}
	
	public IaasContext getIaasContext(String type){
		return iaasCtxts.get(type);
	}
	
	public void setIaasContextMap(Map<String, IaasContext> map){
		iaasCtxts = map;
	}

	public String getPayloadFile() {
	    return payloadFilePath;
    }

	public void setPayloadFile(String payloadFile) {
	    this.payloadFilePath = payloadFile;
    }

	public String getHostName() {
		if(cartridge != null && (hostName == null || hostName.isEmpty())){
			return cartridge.getHostName();
		}
	    return hostName;
    }

	public void setHostName(String hostName) {
	    this.hostName = hostName;
    }

	public String getCartridgeType() {
	    return cartridgeType;
    }

	public void setCartridgeType(String cartridgeType) {
	    this.cartridgeType = cartridgeType;
    }

	public byte[] getPayload() {
	    return payload;
    }

	public void setPayload(byte[] payload) {
	    this.payload = payload;
    }

	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}

    public String toXml() {
		String str =
				payloadFilePath == null ? "<service domain=\"" + clusterId +
				                        "\" tenantRange=\"" + tenantRange + "\">\n" +
				                        "\t<cartridge type=\"" + cartridgeType +
				                        "\"/>\n" + "\t<host>" + hostName +
				                        "</host>\n" + "</service>"

		                                    : "<service domain=\"" + clusterId +
		                                    "\" tenantRange=\"" + tenantRange + "\">\n" +
		                                    "\t<cartridge type=\"" + cartridgeType +
		                                    "\"/>\n"  + "\t<host>" + hostName +
		                                    "</host>\n" + "\t<payload>" + payloadFilePath +
		                                    "</payload>\n" +
		                                    propertiesToXml() +
		                                    "</service>";
		return str;
	}

    public String propertiesToXml() {
		StringBuilder builder = new StringBuilder("");
		for (Iterator<Map.Entry<String, String>> iterator = getProperties().entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, String> prop = iterator.next();

			String key = prop.getKey();
			String value = prop.getValue();
			if (key != null) {
				builder.append("\t<property name=\""+key +"\" value=\"" + (value == null ? "" : value) +"\"/>\n");
			}

		}

		return builder.toString();
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof ServiceContext) {
			return this.clusterId.equals(((ServiceContext) obj).getClusterId());
		}
		return false;
	}
    
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(clusterId).
                toHashCode();
    }

    public String getAutoScalerPolicyName() {
        return autoScalerPolicyName;
    }

    public void setAutoScalerPolicyName(String autoScalerPolicyName) {
        this.autoScalerPolicyName = autoScalerPolicyName;
    }
}
