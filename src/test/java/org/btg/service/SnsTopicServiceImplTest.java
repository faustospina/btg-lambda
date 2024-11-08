package org.btg.service;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SnsTopicServiceImplTest {
    @Mock
    private AmazonSNSClient snsClient;

    @InjectMocks
    private SnsTopicServiceImpl snsTopicService;

    private final String TOPIC_ARN = "arn:aws:sns:us-east-1:849292:BtgTopic";

    @BeforeEach
    void setUp() {
        snsTopicService = new SnsTopicServiceImpl(snsClient);
    }

    @Test
    void testAddSubscriptionWithInvalidType() {
        String destination = "test@example.com";
        String type = "invalidType";
        String expectedResponse = "Invalid subscription type. Please use 'sms' or 'email'.";

        String actualResponse = snsTopicService.addSubscription(destination, type);
        assertEquals(expectedResponse, actualResponse);
        verify(snsClient, never()).subscribe(any(SubscribeRequest.class));
    }

}