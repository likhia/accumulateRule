package com.embedded;

import java.util.Arrays;

import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;

public class App 
{

    public void executeRule() {
        // Load the KIE base:
        KieServices ks = KieServices.Factory.get();
        ReleaseId releaseId = ks.newReleaseId( "com.bala", "li-dmn", "1.0.0" );
        KieContainer kContainer = ks.newKieContainer( releaseId );
        //KieContainer kContainer = ks.getKieClasspathContainer();
        
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
    public static void main( String[] args )
    {
        new App().executeRule();        
    }
}
