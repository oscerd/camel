package org.apache.camel.component.aws.common;

import java.util.Map;

import org.apache.camel.component.extension.verifier.DefaultComponentVerifierExtension;
import org.apache.camel.component.extension.verifier.ResultBuilder;
import org.apache.camel.component.extension.verifier.ResultErrorBuilder;
import org.apache.camel.component.extension.verifier.ResultErrorHelper;

import com.amazonaws.services.logs.model.AWSLogsException;

public class AwsComponentVerifierExtension extends DefaultComponentVerifierExtension {

    public AwsComponentVerifierExtension() {
        this("aws-s3");
    }

    public AwsComponentVerifierExtension(String scheme) {
        super(scheme);
    }

    // *********************************
    // Parameters validation
    // *********************************

    @Override
    protected Result verifyParameters(Map<String, Object> parameters) {
        ResultBuilder builder = ResultBuilder.withStatusAndScope(Result.Status.OK, Scope.PARAMETERS)
            .error(ResultErrorHelper.requiresOption("accessToken", parameters))
            .error(ResultErrorHelper.requiresOption("accessTokenSecret", parameters))
            .error(ResultErrorHelper.requiresOption("consumerKey", parameters))
            .error(ResultErrorHelper.requiresOption("consumerSecret", parameters));

        // Validate using the catalog
        super.verifyParametersAgainstCatalog(builder, parameters);

        return builder.build();
    }

    // *********************************
    // Connectivity validation
    // *********************************

    @Override
    protected Result verifyConnectivity(Map<String, Object> parameters) {
        return ResultBuilder.withStatusAndScope(Result.Status.OK, Scope.CONNECTIVITY)
            .error(parameters, this::verifyCredentials)
            .build();
    }

    private void verifyCredentials(ResultBuilder builder, Map<String, Object> parameters) throws Exception {
        try {
        } catch (AWSLogsException e) {
            // verifyCredentials throws TwitterException when Twitter service or
            // network is unavailable or if supplied credential is wrong
            ResultErrorBuilder errorBuilder = ResultErrorBuilder.withCodeAndDescription(VerificationError.StandardCode.AUTHENTICATION, e.getErrorMessage())
                .detail("twitter_error_code", e.getErrorCode())
                .detail("twitter_status_code", e.getStatusCode())
                .detail("twitter_exception_message", e.getMessage())
                .detail(VerificationError.ExceptionAttribute.EXCEPTION_CLASS, e.getClass().getName())
                .detail(VerificationError.ExceptionAttribute.EXCEPTION_INSTANCE, e);

            builder.error(errorBuilder.build());
        }
    }
}
