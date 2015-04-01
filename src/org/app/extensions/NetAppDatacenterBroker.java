package org.app.extensions;

import java.util.List;

import org.app.models.SimulationRegistry;

public class NetAppDatacenterBroker extends NetBroker{
	 int currentId;
	
    public NetAppDatacenterBroker(SimulationRegistry sr,String name) throws Exception {
        super(sr,name);
        this.currentId=0;
    }
    /** 
     * Gets an id of a datacenter managed by this broker.
     *
     * @return  an id of a datacenter.
     * @since   1.0
     */      
    @Override
    public int getDatacenterId() {
        currentId++;
        int index = currentId%getDatacenterIdsList().size();
        return getDatacenterIdsList().get(index);
    }

    /** 
     * Gets a list of ids from datacenters managed by this broker.
     *
     * @return  a list of datacenter ids.
     * @since   1.0
     */      
    @Override
    public List<Integer> getDatacenterIdList() {
        return getDatacenterIdsList();
    }
}
