# Account Messaging BPMS project

## Instructions to install

### Install BPMS REST service

First you will need clone this rep:
 ```sh
 git clone https://github.com/jioxmatik/AdmiralDiplomBPMS.git
 ```
Go to the cloned repository dir and run build.sh file. Wait until process is done.

Go to the EAP admin console (For example http://localhost:9990/console/App.html) and go to Deployments page, if we have bpms-service.war on list then choice him and press Replase button else press Add button.

In opened window we need choise file bpms-service.war on cloned repository in bin directory, press Next, turn on Enable checkbox and press save.

### Cloning project in BPMS

Go to the BPMS (For example http://localhost:8080/business-central) and go to page Authoring -> Administration .

If you have oldest repository in BPMS then remove it.

Clone this repository https://github.com/jioxmatik/AdmiralDiplomBPMS.git in to BPMS

Go to page Authoring -> Project authoring select AccountMessaging project, open project editor and press button (Build and Deploy).

### Testing

Open REST client and send in BPMS REST service some request 

### REQUESTS TO BPMS REST service

#### Init context (POST)

http://localhost:8080/bpms-service/rest/resources/contexts?session=ksession1&gav=com.admiral.diplom:AccountMessaging:0.0.1

This request init session and return context_id

#### Insert facts (POST)

http://localhost:8080/bpms-service/rest/resources/contexts/{context_id}/commands/insert-elements.json

This request inserting facts in to BPMS

REQUEST PARAMS EXAMPLE:

```json
{
    "batch-execution": {
        "lookup": "ksession1",
        "commands": {
            "insert-elements": {
                "objects": [
                    {
                         "containedObject": {
                          "@class": "com.admiral.diplom.accountmessaging.Account",
                          "id": "test123test123test123",
                          "date_entered": "2016-05-05 08:22:00 +0000",
                          "px_last_trade_operation_date": "2016-05-05 08:22:00 +0000"
                        }
                    },
		    {
                        "containedObject": {
                          "@class": "com.admiral.diplom.accountmessaging.PxCustomerStageAgregate",
                          "id": "asdfaterfbtbte",
                          "account_id": "test123test123test123",
                          "stage": "1.Lead",
                          "stage_moved_date": "2016-05-20 05:22:00 +0000",
                        }
                    },
 		    {
                        "containedObject": {
                          "@class": "com.admiral.diplom.accountmessaging.PxCustomerStageAgregate",
                          "id": "adgfaserg",
                          "account_id": "test123test123test123",
                          "stage": "2.No accounts",
                          "stage_moved_date": "2016-05-20 05:22:00 +0000",
                        }
                    },
 		    {
                        "containedObject": {
                          "@class": "com.admiral.diplom.accountmessaging.PxCustomerStageAgregate",
                          "id": "tefasfasf",
                          "account_id": "test123test123test123",
                          "stage": "insert-elements",
                          "stage_moved_date": "2016-04-01 05:22:00 +0000",
                        }
                    },

                ]
            }
        }
    }
}
```

#### Fire all rules (POST)

http://localhos:8080/bpms-service/rest/resources/contexts/{context_id}/commands/fire-all-rules.json

This request fire all rules in BPMS

REQUEST PARAMS EXAMPLE:

```json
{
    "batch-execution": {
        "lookup": "ksession1",
        "commands": {
           "fire-all-rules": {}
        }
    }
}
```

#### Get all facts (POST)

http://localhost:8080/bpms-service/rest/resources/contexts/{context_id}/commands/get-objects.json

this request return all facts from BPMS

REQUEST PARAMS EXAMPLE:

```json
{
    "batch-execution": {
        "lookup": "ksession1",
        "commands": {
            "get-objects":{"out-identifier":"objects"}
        }
    }
}
```

#### DELETE context (DELETE)

http://localhost:8080/bpms-service/rest/resources/contexts/{context_id}

This request delete session and clean memory for current context in BPMS (remove facts) 