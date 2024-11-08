package org.btg.service;

public interface SnsTopicService {
    String addSubscription(String destination, String type);
    String publishMessageToTopic(String mensaje);
}
