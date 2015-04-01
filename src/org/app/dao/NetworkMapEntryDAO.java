/* 
 * Copyright (c) 2010-2012 Thiago T. Sá
 * 
 * This file is part of CloudReports.
 *
 * CloudReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CloudReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * For more information about your rights as a user of CloudReports,
 * refer to the LICENSE file or see <http://www.gnu.org/licenses/>.
 */

package org.app.dao;


import org.app.models.CustomerRegistry;
import org.app.models.DatacenterRegistry;
import org.app.models.NetworkMapEntry;
import org.app.models.Setting;
import org.app.models.SimulationRegistry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * NetworkMapEntryDAO provides basic CRUD operations related to the 
 * {@link NetworkMapEntry} class.
 * 
 * @see         Setting
 * @author      ChenYang
 * @since       1.0
 */
public class NetworkMapEntryDAO {
    
    /** 
     * Inserts a new customer registry into the database.
     *
     * @param   entry   the network entry to be inserted.
     * @see             NetworkMapEntry
     * @since           1.0
     */    
	
	
    public void insertNetworkMapEntry(SimulationRegistry sr,NetworkMapEntry entry) {

    	List<NetworkMapEntry> networkMapEntry=sr.getNetworkMapEntrys();
    	if(!networkMapEntry.contains(entry)){
    		networkMapEntry.add(entry);
    	}
    	
    }
    
    /** 
     * Creates initial network entries for new entities.
     *
     * @param   entityName  the name of the new entity.
     * @return              <code>true</code> if the initial entries have been
     *                      successfully created; <code>false</code> otherwise.
     * @see                 NetworkMapEntry
     * @since               1.0
     */     
    public boolean insertNewEntityEntries(SimulationRegistry sr,String entityName) {
       
    	List<NetworkMapEntry> networkMapEntry=sr.getNetworkMapEntrys();
      	for(NetworkMapEntry nm:networkMapEntry){
    		if(nm.getSource().equals(entityName)){
    			return false;
    		}
    	}

 
        DatacenterRegistryDAO drDAO = new DatacenterRegistryDAO();
        List<DatacenterRegistry> datacenterList = new LinkedList<DatacenterRegistry>();
        datacenterList.addAll(sr.getDatacenterList());
        datacenterList.remove(drDAO.getDatacenterRegistry(sr,entityName));
        for(DatacenterRegistry dr : datacenterList) {
            /*
            * Include all other datacenters as a destinations to
            * this new entity
            */            
            NetworkMapEntry newEntry = new NetworkMapEntry(entityName, dr.getName(), 1.0, 1.0);
            insertNetworkMapEntry(sr,newEntry);

            /*
            * Include this new entity as a destination to all other
            * datacenters.
            */
            newEntry = new NetworkMapEntry(dr.getName(), entityName, 1.0, 1.0);
            insertNetworkMapEntry(sr,newEntry);
        }

        CustomerRegistryDAO crDAO = new CustomerRegistryDAO();
        List<CustomerRegistry> customerList = new LinkedList<CustomerRegistry>();
        customerList.addAll(sr.getCustomerList());
        customerList.remove(crDAO.getCustomerRegistry(sr,entityName));
        for(CustomerRegistry cr : customerList) {
            /*
            * Include all other customers as a destination to
            * this new entity
            */
            NetworkMapEntry newEntry = new NetworkMapEntry(entityName, cr.getName(), 1.0, 1.0);
            insertNetworkMapEntry(sr,newEntry);

            /*
            * Include this new entity as a destination to all other
            * customers.
            */
            newEntry = new NetworkMapEntry(cr.getName(), entityName, 1.0, 1.0);
            insertNetworkMapEntry(sr,newEntry);
        }
        
        return true;
    }    
    
    /** 
     * Gets an existing network entry based on a source and a destination.
     *
     * @param   source          the name of a source entity.
     * @param   destination     the name of a destination entity.
     * @return                  the network entry, if it exists; 
     *                          <code>null</code> otherwise.
     * @see                     NetworkMapEntry
     * @since                   1.0
     */    
    public NetworkMapEntry getNetworkMapEntry(SimulationRegistry sr,String source, String destination) {
    	List<NetworkMapEntry> networkMapEntry=sr.getNetworkMapEntrys();
    	NetworkMapEntry entry = null;
    	for(NetworkMapEntry nm:networkMapEntry){
    		if(nm.getSource().equals(source)&&nm.getDestination().equals(destination)){
    			entry=nm;
    			return entry;
    		}
    	}
    	return entry;
    }
    
    /** 
     * Updates an existing network entry.
     *
     * @param   entry   the network entry to be updated.
     * @see             NetworkMapEntry
     * @since           1.0
     */    
    public void updateNetworkMapEntry(SimulationRegistry sr,NetworkMapEntry entry) {
    	List<NetworkMapEntry> networkMapEntry=sr.getNetworkMapEntrys();
    	for(NetworkMapEntry nm:networkMapEntry){
    		if(nm.getSource().equals(entry.getSource())&&nm.getDestination().equals(entry.getDestination())){
    			nm=entry;
    		}
    	}

    }
    
    /** 
     * Removes all network entries of a given entity.
     *
     * @param   entityName      the name of the entity of which all network
     *                          entries will be removed.
     * @see                     NetworkMapEntry
     * @since                   1.0
     */    
    public void removeEntries(SimulationRegistry sr,String entityName) {
    	List<NetworkMapEntry> networkMapEntry=sr.getNetworkMapEntrys();      

    	List<NetworkMapEntry> removedtmp=new ArrayList<NetworkMapEntry>();
        for(NetworkMapEntry nm : networkMapEntry) {
                if(nm.getSource().equals(entityName)||nm.getDestination().equals(entityName)){
                	removedtmp.add(nm);
                }
        }
        networkMapEntry.removeAll(removedtmp);

      
    }
    
    /** 
     * Gets a list of all existing network entries.
     *
     * @return  a list containing all existing network entries
     *          in the database; <code>null</code> if no
     *          entries were found.
     * @see     NetworkMapEntry
     * @since   1.0
     */    
  
	public List<NetworkMapEntry> getAllEntries(SimulationRegistry sr) {
       
    	List<NetworkMapEntry> networkMapEntry=sr.getNetworkMapEntrys();

       
        
        return networkMapEntry;       
    }    

    /** 
     * Gets a list of network entries of a given entity.
     * The resulting list contains entries on which the given entity is either
     * the source or the destination.
     * 
     * @param   entityName      the name of the entity of which all related 
     *                          network entries will be retrieved.
     * @return                  a list containing all existing network entries
     *                          in the database; <code>null</code> if no
     *                          entries were found.
     * @see                     NetworkMapEntry
     * @since                   1.0
     */       

	public List<NetworkMapEntry> getListOfEntries(SimulationRegistry sr,String entityName) {
       
        List<NetworkMapEntry> networkList =  new LinkedList<NetworkMapEntry>() ;
    	List<NetworkMapEntry> networkMapEntry=sr.getNetworkMapEntrys();
    	for(NetworkMapEntry nm:networkMapEntry){
    		if(nm.getSource().equals(entityName)||nm.getDestination().equals(entityName)){
    			networkList.add(nm);
    		}
    	}
       
        
        return networkList;
    }       
    
    /** 
     * Gets a list of network entries on which the given entity is the source.
     *
     * @param   entityName      the name of the entity of which all 
     *                          destinations will be retrieved.
     * @return                  a list containing all network entries on which
     *                          the given entity is the source; 
     *                          <code>null</code> if no entries were found.
     * @see                     NetworkMapEntry
     * @since                   1.0
     */         

	public List<NetworkMapEntry> getListOfDestinations(SimulationRegistry sr,String entityName) {
        List<NetworkMapEntry> networkList = new LinkedList<NetworkMapEntry>() ;
    	List<NetworkMapEntry> networkMapEntry=sr.getNetworkMapEntrys();
    	 
    	
    	for(NetworkMapEntry nm:networkMapEntry){
    		if(nm.getSource().equals(entityName)){
    		
    			networkList.add(nm);
    		
    		}
    	}
       
        
        return networkList;      
    }     

}
