package com.integros.novaposhta.bpms.service.contexts;

class UnknownAliasException extends Exception {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Unknown alias \"%s\"";
    
    public UnknownAliasException(String alias) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, alias));
    }

    public UnknownAliasException(String alias, String message) {
        super(message);
    }
    
    public UnknownAliasException(String alias, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, alias), cause);
    }
    
    public UnknownAliasException(String alias, String message, Throwable cause) {
        super(message, cause);
    }
}

interface IAliasMap {
    void setAlias(String original, String alias);
    void unsetAlias(String alias);
    Iterable<String> getAliases(String original);
    String getOriginal(String alias) throws UnknownAliasException;
    /**
     * 
     * @param string String to translate
     * @return string itself, if it's not a known alias, corresponding original string otherwise.
     */
    String translate(String string);
}
