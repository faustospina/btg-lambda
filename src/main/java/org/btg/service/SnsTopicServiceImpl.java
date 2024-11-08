package org.btg.service;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnsTopicServiceImpl implements SnsTopicService{

    private final AmazonSNSClient snsClient;
    String TOPIC_ARN="" ;

    @Override
    public String addSubscription(String destination, String type) {
        String protocol = determineProtocol(type);
        if (protocol == null) {
            return "Invalid subscription type. Please use 'sms' or 'email'.";
        }
        SubscribeRequest request = new SubscribeRequest(TOPIC_ARN, protocol, destination);
        snsClient.subscribe(request);
        return "Subscription request is pending. To confirm the subscription, check the " + type + ": " + destination;
    }

    @Override
    public String publishMessageToTopic(String mensaje) {
        PublishRequest publishRequest=new PublishRequest(TOPIC_ARN,buildEmailBody(mensaje),"Notification: Network connectivity issue");
        snsClient.publish(publishRequest);
        return "Notification send successfully !!";
    }

    private String buildEmailBody(String mensaje){
        return "Estimado cliente ,\n" +
                "\n" +
                "\n" +
                mensaje;
    }

    private String determineProtocol(String type) {
        switch (type.toLowerCase()) {
            case "sms":
                return "sms";
            case "email":
                return "email";
            default:
                return null;
        }
    }
}
