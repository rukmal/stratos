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
package org.apache.stratos.rest.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.core.multitenancy.persistence.TenantPersistor;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.tenant.TenantManager;

/**
 * Some of the admin services needs objects with states inside the runtime. There are
 * two mechanisms to get those kind of objects. Either with singleton with or via OSGi
 * services. OSGi services mechanism is preferred. This is a helper class for doing that.
 */
public class ServiceHolder {
    private static Log log = LogFactory.getLog(ServiceHolder.class);

    public static TenantManager getTenantManager() {
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        RealmService realmService = (RealmService)carbonContext.getOSGiService(RealmService.class);
        return realmService.getTenantManager();
    }

    public static RealmService getRealmService(){
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        RealmService realmService = (RealmService)carbonContext.getOSGiService(RealmService.class);
        return realmService;
    }

    public static RegistryService getRegistryService() {
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        RegistryService registryService = (RegistryService) carbonContext.getOSGiService(RegistryService.class);
        return registryService;
    }

    public static TenantPersistor getTenantPersistor() {
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        TenantPersistor tenantPersistor = (TenantPersistor) carbonContext.getOSGiService(TenantPersistor.class);
        return tenantPersistor;
    }

}