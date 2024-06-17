package cz.luck.ditto.model;

import lombok.Data;

@Data
public class CreateSubscriptionNotification {

    private String type;
    private Attr attr;

}
