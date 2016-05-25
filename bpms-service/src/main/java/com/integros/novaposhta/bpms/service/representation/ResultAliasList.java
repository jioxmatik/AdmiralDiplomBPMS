package com.integros.novaposhta.bpms.service.representation;

public class ResultAliasList extends ResultOK {
    
        private final Iterable<String> aliases;
        
        public ResultAliasList(Iterable<String> aliasIds) {
		super();
                this.aliases = aliasIds;
	}

        public Iterable<String> getAliases() {
            return aliases;
        }
}