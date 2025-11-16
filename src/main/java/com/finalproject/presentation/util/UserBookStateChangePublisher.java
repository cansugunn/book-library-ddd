package com.finalproject.presentation.util;

import java.util.LinkedList;
import java.util.List;

public class UserBookStateChangePublisher {
    private List<UserBookStateChangeSubscriber> subscriberList;

    public UserBookStateChangePublisher() {
        subscriberList = new LinkedList<>();
    }

    public void subscribe(UserBookStateChangeSubscriber... subscribers) {
        subscriberList.addAll(List.of(subscribers));
    }

    public void unsubscribe(UserBookStateChangeSubscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    public void notifySubscribers() {
        for (UserBookStateChangeSubscriber subscriber : subscriberList) {
            subscriber.refresh();
        }
    }
}
