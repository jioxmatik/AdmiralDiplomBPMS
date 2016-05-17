package com.admiral.diplom.accountmessaging;

/**
 * This class was automatically generated by the data modeler tool.
 * $HASH(79a9f9a7de0957fbfe2827ed09ec791b)
 */
@org.kie.api.definition.type.Label(value = "Customer Stage Agregates")
public class PxCustomerStageAgregates  implements java.io.Serializable {

    static final long serialVersionUID = 1L;
    
    @org.kie.api.definition.type.Label(value = "Account ID")
    @org.kie.api.definition.type.Position(value = 3)
    private java.lang.String account_id;
    
    @org.kie.api.definition.type.Label(value = "Record ID")
    @org.kie.api.definition.type.Position(value = 0)
    private java.lang.String id;
    
    @org.kie.api.definition.type.Label(value = "Stage")
    @org.kie.api.definition.type.Position(value = 1)
    private java.lang.String stage;
    
    @org.kie.api.definition.type.Label(value = "Stage moved Date")
    @org.kie.api.definition.type.Position(value = 2)
    private java.util.Date stage_moved_date;

    public PxCustomerStageAgregates() {
    }

    public PxCustomerStageAgregates(java.lang.String id, java.lang.String stage, java.util.Date stage_moved_date, java.lang.String account_id) {
        this.id = id;
        this.stage = stage;
        this.stage_moved_date = stage_moved_date;
        this.account_id = account_id;
    }


    
    public java.lang.String getAccount_id() {
        return this.account_id;
    }

    public void setAccount_id(java.lang.String account_id) {
        this.account_id = account_id;
    }
    
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }
    
    public java.lang.String getStage() {
        return this.stage;
    }

    public void setStage(java.lang.String stage) {
        this.stage = stage;
    }
    
    public java.util.Date getStage_moved_date() {
        return this.stage_moved_date;
    }

    public void setStage_moved_date(java.util.Date stage_moved_date) {
        this.stage_moved_date = stage_moved_date;
    }
}