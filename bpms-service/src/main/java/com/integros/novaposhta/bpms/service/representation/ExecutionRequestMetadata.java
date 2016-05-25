package com.integros.novaposhta.bpms.service.representation;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;

public class ExecutionRequestMetadata extends AbstractSerializableObject {
    private static final long DEFAULT_DURATION = -1;
    private static final long DEFAULT_SIZE = -1;
    
    private final String alias;
    private final String tag;
    private final String format;
    private final String contextId;
    private final String gavInfo;
    private final long requestSize;
    private int requestEntryCount;
    private long responseSize;
    private long responseTime;
    private ExceptionRepresentation error;
    
    public ExecutionRequestMetadata(String alias, String tag, String format, String contextId, IServiceContext context, long size, int requestEntryCount) {
        this(alias, tag, format, contextId, context, size, requestEntryCount, DEFAULT_DURATION, DEFAULT_SIZE, null);
    }
    
    public ExecutionRequestMetadata(
        String alias, 
        String tag,
        String format,
        String contextId, 
        IServiceContext context, 
        long request_size, 
        int request_entry_count,
        long response_size,
        long response_time,
        Throwable error
    ) {
        this.alias = alias;
        this.tag = tag;
        this.format = format;
        this.contextId = contextId;
        this.gavInfo = context.getReleaseId().toString();
        this.requestSize = request_size;
        this.requestEntryCount = request_entry_count;
        this.responseSize = response_time;
        this.error = (error == null ? null : new ExceptionRepresentation(error));
    }

    public String getAlias() {
        return alias;
    }

    public String getTag() {
        return tag;
    }

    public String getContextId() {
        return contextId;
    }

    public String getGavInfo() {
        return gavInfo;
    }

    public void setResponseSize(long response_size) {
        this.responseSize = response_size;
    }

    public void setResponseTime(long response_time) {
        this.responseTime = response_time;
    }

    public String getFormat() {
        return format;
    }

    public long getRequestSize() {
        return requestSize;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public long getResponseTime() {
        return responseTime;
    }
    
    public void setResponseMetadata(long time, long size) {
        this.responseTime = time;
        this.responseSize = size;
    }
    
    public ExceptionRepresentation getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = new ExceptionRepresentation(error);
    }
    
    public int getRequestEntryCount() {
        return requestEntryCount;
    }

    public void setRequestEntryCount(int requestEntryCount) {
        this.requestEntryCount = requestEntryCount;
    }
}
