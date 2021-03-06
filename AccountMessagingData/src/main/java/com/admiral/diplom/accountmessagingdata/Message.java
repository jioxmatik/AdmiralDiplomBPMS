package com.admiral.diplom.accountmessagingdata;

/**
 * This class was automatically generated by the data modeler tool.
 * $HASH(3921da62698dc081944d7446428ef310)
 */
@org.kie.api.definition.type.Label(value = "Message")
public class Message  implements java.io.Serializable {

    static final long serialVersionUID = 1L;
    
    @org.kie.api.definition.type.Label(value = "Account ID")
    @org.kie.api.definition.type.Position(value = 1)
    private java.lang.String account_id;
    
    @org.kie.api.definition.type.Label(value = "Descrioption")
    @org.kie.api.definition.type.Position(value = 2)
    private java.lang.String description;
    
    @org.kie.api.definition.type.Label(value = "ID")
    @org.kie.api.definition.type.Position(value = 0)
    private java.lang.String id;

    public Message() {
    }

    public Message(java.lang.String id, java.lang.String account_id, java.lang.String description) {
        this.id = id;
        this.account_id = account_id;
        this.description = description;
    }


    
    public java.lang.String getAccount_id() {
        return this.account_id;
    }

    public void setAccount_id(java.lang.String account_id) {
        this.account_id = account_id;
    }
    
    public java.lang.String getDescription() {
        return this.description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }
}