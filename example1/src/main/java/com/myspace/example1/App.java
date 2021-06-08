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

import org.kie.api.KieServices;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.kie.api.runtime.ClassObjectFilter;



public class App {

    private static final String containerName = "example1_1.0.0-SNAPSHOT";
    private static final String sessionName = "kie-session";

    public static void main(String[] args) {
        try {
            // Define KIE services configuration and client:
            Set<Class<?>> allClasses = new HashSet<Class<?>>();
            allClasses.add(Item.class);
            allClasses.add(Result.class);
            allClasses.add(Sample.class);

            String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
            String username = "rhdmAdmin";
            String password = "redhat@123";
            KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(serverUrl, username, password);
            config.setMarshallingFormat(MarshallingFormat.JAXB);
            config.addExtraClasses(allClasses);
            
            KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(config);
         
            // Set up the fact model
            Item item1 = new Item();
            item1.setCategory("cat1");
            item1.setPrice(new BigDecimal("100.00"));
           

            Item item2 = new Item();
            item2.setCategory("cat1");
            item2.setPrice(new BigDecimal("50.00"));
        
            
            Item item3 = new Item();
            item3.setCategory("cat2");
            item3.setPrice(new BigDecimal("20.00"));

            
            Item item4 = new Item();
            item4.setCategory("cat3");
            item4.setPrice(new BigDecimal("150.00"));
           

            Item item5 = new Item();
            item5.setCategory("cat1");
            item5.setPrice(new BigDecimal("20.00"));
        
            
            Result result = new Result(); 
            KieCommands kieCommands = KieServices.Factory.get().getCommands();
            List<Command> commandList = new ArrayList<Command>();
            commandList.add(kieCommands.newInsert(item1, "item"));
            commandList.add(kieCommands.newInsert(item2, "item"));
            commandList.add(kieCommands.newInsert(item3, "item"));
            commandList.add(kieCommands.newInsert(item4, "item"));
            commandList.add(kieCommands.newInsert(item5, "item"));
            commandList.add(kieCommands.newInsert(new Result(), "result"));
            commandList.add(kieCommands.newFireAllRules());
            commandList.add(kieCommands.newGetObjects(new ClassObjectFilter(Result.class),"result"));

            BatchExecutionCommand batch = kieCommands.newBatchExecution(commandList, sessionName);

            // Use rule services client to send request:
            RuleServicesClient ruleClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
            ServiceResponse<ExecutionResults> executeResponse = ruleClient.executeCommandsWithResults(containerName, batch);

            List<Result> resultList = (List<Result>) executeResponse.getResult().getValue("result");
            System.out.println(resultList.size());
            for(int i=0; i < resultList.size(); i++) {
                Result res = (Result) resultList.get(i);
                res.print();
            }

            //commandList = new ArrayList<Command>();
            //commandList.add(kieCommands.newInsert("initial", "test"));
            //commandList.add(kieCommands.newInsert(new Sample(), "sample"));
            //commandList.add(kieCommands.newInsert(new HashMap(), "map"));
            //commandList.add(kieCommands.newFireAllRules());
            //commandList.add(kieCommands.newGetObjects(new ClassObjectFilter(String.class),"test"));
            //commandList.add(kieCommands.newGetObjects(new ClassObjectFilter(Sample.class),"sample"));
            //commandList.add(kieCommands.newGetObjects(new ClassObjectFilter(HashMap.class),"map"));
            //batch = kieCommands.newBatchExecution(commandList, sessionName);
            //executeResponse = ruleClient.executeCommandsWithResults(containerName, batch);

            
            //List<String> testResult = (List<String>) (executeResponse.getResult().getValue("test"));
            //System.out.println(testResult);
            //System.out.println(testResult.get(testResult.size()-1));

            //List<Sample> testResult2 = (List<Sample>) (executeResponse.getResult().getValue("sample"));
            //System.out.println(testResult2);
            //System.out.println(((Sample) testResult2.get(testResult2.size()-1)).getField1());
            //System.out.println(((Sample) testResult2.get(testResult2.size()-1)).getData());

            //List<HashMap> maplist = (List<HashMap>) (executeResponse.getResult().getValue("map"));
            //System.out.println(maplist);
            //System.out.println(((HashMap) maplist.get(maplist.size()-1)).get("key"));
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}