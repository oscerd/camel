package org.apache.camel.component.aws.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Component;
import org.apache.camel.component.extension.ComponentVerifierExtension;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Test;

public class AWSs3ComponentVerifierTest extends CamelTestSupport{

    // *************************************************
    // Tests (parameters)
    // *************************************************
    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }
    
    @Test
    public void testParameters() throws Exception {
        Component component = context().getComponent("aws-s3");
        System.err.println(component.getSupportedExtensions().toString());
        ComponentVerifierExtension verifier = component.getExtension(ComponentVerifierExtension.class).orElseThrow(IllegalStateException::new);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("a", "l");

        ComponentVerifierExtension.Result result = verifier.verify(ComponentVerifierExtension.Scope.PARAMETERS, parameters);

        Assert.assertEquals(ComponentVerifierExtension.Result.Status.OK, result.getStatus());
    }
    
}
