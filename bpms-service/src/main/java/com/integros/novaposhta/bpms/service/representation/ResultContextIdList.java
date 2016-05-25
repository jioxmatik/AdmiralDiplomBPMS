package com.integros.novaposhta.bpms.service.representation;

public class ResultContextIdList extends ResultOK {
    
        private final Iterable<String> contexts;
        
        public ResultContextIdList(Iterable<String> contextIds) {
		super();
                this.contexts = contextIds;
	}

        public Iterable<String> getContexts() {
            return contexts;
        }
}