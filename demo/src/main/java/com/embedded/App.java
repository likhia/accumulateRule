package com.embedded;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;

import com.Reason;

public class App 
{

    private void executeRule() {
        // Load the KIE base:
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        
        ks.getResources().newClassPathResource("AgeDMN.dmn", this.getClass());

        DMNRuntime dmnRuntime = KieRuntimeFactory.of(kContainer.getKieBase()).get(DMNRuntime.class);

        String namespace = "https://kiegroup.org/dmn/_C61C4369-2155-4E9A-A57C-A7AD9404CD12";
        String modelName = "AgeDMN";

        DMNModel dmnModel = dmnRuntime.getModel(namespace, modelName);

        DMNContext dmnContext = dmnRuntime.newContext();  

        for (Integer age : Arrays.asList(1,12,13,64,65,66)) {
            dmnContext.set("Age", age);  
            DMNResult dmnResult =
                dmnRuntime.evaluateAll(dmnModel, dmnContext);  

            for (DMNDecisionResult dr : dmnResult.getDecisionResults()) {  
                System.out.println("Age: " + age + ", " +
                        "Decision: '" + dr.getDecisionName() + "', " +
                        "Result: " + dr.getResult());
            }
        }
    }

    public Reason executeNationalityRule(String nationality, String transportMode) {

        Reason reason = null;

        // Load the KIE base:
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        
        ks.getResources().newClassPathResource("NationalityByTransportMode.dmn", this.getClass());

        DMNRuntime dmnRuntime = KieRuntimeFactory.of(kContainer.getKieBase()).get(DMNRuntime.class);

        String namespace = "https://kiegroup.org/dmn/_82CA1D34-CC84-49BD-8B6B-B52E820790EE";
        String modelName = "NationalityByTransportMode";

        DMNModel dmnModel = dmnRuntime.getModel(namespace, modelName);

        DMNContext dmnContext = dmnRuntime.newContext();  

        
        dmnContext.set("nationality", nationality);
        dmnContext.set("transportMode", transportMode);

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);  

        for (DMNDecisionResult dr : dmnResult.getDecisionResults()) {  
            System.out.println("Decision: '" + dr.getDecisionName() + "', " +
                    "Result: " + dr.getResult());
            
            if ("NationalityByTransportMode".equals(dr.getDecisionName())) {
                reason = new Reason();
                HashMap hm = (HashMap) dr.getResult();
                reason.setReason((String)hm.get("reason"));
                reason.setReasonId((String) hm.get("reasonId"));
                reason.setScore(((BigDecimal) hm.get("score")).intValue());
                break;
            }
            
        }
        
        return reason;
    }
    public static void main( String[] args )
    {
        //System.out.println(new App().executeNationalityRule("US", "S").getReason());        
        new App().executeRule();
    }
}
