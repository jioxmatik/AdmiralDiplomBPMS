package com.integros.novaposhta.bpms.service.contexts.persistence;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import org.kie.api.KieServices;
import org.kie.api.marshalling.Marshaller;
import org.kie.api.runtime.KieSession;

public class SimpleFileBasedPersistenceEngine implements IServiceContextPersistenceEngine {
    private final String persistencePath;
    
    public SimpleFileBasedPersistenceEngine(String persistencePath)
    {
        this.persistencePath = persistencePath;
    }

    @Override
    public void persist(String key, IServiceContext value) throws ServiceContextPersistenceFailedException {
        ByteArrayOutputStream baos = null;
        OutputStream os = null;
        try {
            KieSession kSession = value.getKieSession();
            baos = new ByteArrayOutputStream();
            os = new FileOutputStream(this.getFilename(key));
            Marshaller marshaller = KieServices.Factory.get().getMarshallers().newMarshaller(kSession.getKieBase());
            marshaller.marshall(baos, kSession);
            baos.writeTo(os);
        } catch(Exception e) {
            throw new ServiceContextPersistenceFailedException(e);
        } finally {
            if(baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public IServiceContext restore(String key) throws ServiceContextRestorationFailedException {
        try {
            throw new UnsupportedOperationException("Not yet implemented.");
        } catch(Exception e) {
            throw new ServiceContextRestorationFailedException(e);
        }
    }

    @Override
    public IServiceContext restoreToContext(String key, IServiceContext target) throws ServiceContextRestorationFailedException {
        InputStream is = null;
        try{
            Marshaller marshaller = KieServices.Factory.get().getMarshallers().newMarshaller(target.getKieSession().getKieBase());
            is = new FileInputStream(this.getFilename(key));
            target.reset(marshaller.unmarshall(is));
            return target;
        } catch(Exception e) {
            throw new ServiceContextRestorationFailedException(e);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean containsKey(String key) {
        File f = new File(this.getFilename(key));
        return f.exists() && !f.isDirectory();
    }
    
    protected final String getFilename(String key) {
        return this.persistencePath + "/" + key;
    }

    @Override
    public Set<String> keySet() throws PersistenceNotAvailableException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
