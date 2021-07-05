package com.myspace.example1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java.math.BigDecimal;

import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;


import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNContext;

import org.kie.api.KieServices;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.DMNServicesClient;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.kie.api.runtime.ClassObjectFilter;

import com.myspace.dmnexample1.EntityList;

public class DMNApp {

    private static final String containerName = "dmnexample1_1.0.0-SNAPSHOT";
    //private static final String modelNamespace = "https://kiegroup.org/dmn/_CFAB5392-6988-42B9-82D2-137A934CA4DE";
    //private static final String modelName = "SampleDMN1";

    //private static final String modelNamespace = "https://kiegroup.org/dmn/_130EFB28-D8FD-495B-B01E-13B72C1F4907";
    //private static final String modelName = "SampleDMN2";

    //private static final String modelNamespace = "https://kiegroup.org/dmn/_779852A8-1977-4863-8BC0-7D5ED2AEF291";
    //private static final String modelName = "ListDMN";

    private static final String modelNamespace = "https://kiegroup.org/dmn/_16E70A7B-2BBE-4E3F-9F99-B79AEDEB0026";
    private static final String modelName = "ListDMN2";
    
    public static void main(String[] args) {
        try {
           
            String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
            String username = "rhdmAdmin";
            String password = "redhat@123";

            // Define KIE services configuration and client:
            Set<Class<?>> allClasses = new HashSet<Class<?>>();
            allClasses.add(EntityList.class);
           
            KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(serverUrl, username, password);
            config.setMarshallingFormat(MarshallingFormat.JAXB);
            config.addExtraClasses(allClasses);
            
            //https://access.redhat.com/documentation/en-us/red_hat_decision_manager/7.1/html-single/designing_a_decision_service_using_dmn_models/index
            KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(config);
         
            DMNServicesClient dmnClient = kieServicesClient.getServicesClient(DMNServicesClient.class );
         
            EntityList entityList = new EntityList();
            List<String> nameList = new ArrayList();
            nameList.add("IPP");
            nameList.add("Test");
            entityList.setName(nameList);

            DMNContext dmnContext = dmnClient.newContext(); 

            //ListDMN
            dmnContext.set("entityList", entityList );
            
            //ListDMN2
            dmnContext.set("category", "Cat1");
            
           
            ServiceResponse<DMNResult> serverResp =   dmnClient.evaluateAll(containerName, modelNamespace, modelName, dmnContext);

            DMNResult dmnResult = serverResp.getResult();  
            for (DMNDecisionResult dr : dmnResult.getDecisionResults()) {
                System.out.println("Decision: '" + dr.getDecisionName() + "', " +
                        "Result: " + dr.getResult());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
