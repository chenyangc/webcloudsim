
package org.app.dao;


import org.app.models.DatacenterRegistry;
import org.app.models.HostRegistry;
import org.app.models.SanStorageRegistry;
import org.app.models.Setting;
import org.app.models.SimulationRegistry;
import org.app.utils.LongOperations;

import java.util.List;


/**
 * DatacenterRegistryDAO provides basic CRUD operations related to the 
 * {@link DatacenterRegistry} class.
 * It also provides access to general information about datacenters.
 * 
 * @see         Setting
 * @author      ChenYang
 * @since       1.0
 */
public class DatacenterRegistryDAO {
    
    /** 
     * Inserts a new datacenter registry into the database.
     * The datacenter's name must be unique.
     *
     * @param   dr      the datacenter registry to be inserted.
     * @return          <code>true</code> if the datacenter registry
     *                  has been successfully inserted; 
     *                  <code>false</code> otherwise.
     * @see             DatacenterRegistry
     * @since           1.0
     */
	
    public boolean insertNewDatacenterRegistry(SimulationRegistry sr,DatacenterRegistry dr) {
    	//SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<DatacenterRegistry> datacenters=sr.getDatacenterList();
        for(DatacenterRegistry datacenter:datacenters) {
            if(datacenter.getName().equals(dr.getName()))
            	return false;
        }
        datacenters.add(dr);
    	      
        //Insert the new network entries
        NetworkMapEntryDAO neDAO = new NetworkMapEntryDAO();
        neDAO.insertNewEntityEntries(sr,dr.getName());
        
        return true;
    }
    
    /** 
     * Gets an existing datacenter with the given id.
     *
     * @param   datacenterId    the id of the datacenter to be retrieved.
     * @return                  the datacenter, if it exists; <code>null</code>
     *                          otherwise.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */    
    public DatacenterRegistry getDatacenterRegistry(SimulationRegistry sr,long datacenterId) {
        DatacenterRegistry datacenter = null;
       	//SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<DatacenterRegistry> datacenters=sr.getDatacenterList();
        for(DatacenterRegistry dr:datacenters) {
            if(dr.getId()==datacenterId){
            	datacenter=dr;
            	break;
            }
            	
        }       
        return datacenter;
    }
    
    /** 
     * Gets an existing datacenter with the given name.
     *
     * @param   datacenterName    the name of the datacenter to be retrieved.
     * @return                  the datacenter, if it exists; <code>null</code>
     *                          otherwise.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */       
    public DatacenterRegistry getDatacenterRegistry(SimulationRegistry sr,String datacenterName) {
        DatacenterRegistry datacenter = null;
       	//SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<DatacenterRegistry> datacenters=sr.getDatacenterList();
        for(DatacenterRegistry dr:datacenters) {
            if(dr.getName().equals(datacenterName)){
            	datacenter=dr;
            	break;
            }
            	
        }    
 
        return datacenter;
    }    
    
    /** 
     * Updates an existing datacenter registry.
     *
     * @param   datacenter      the datacenter to be updated.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */       
    public void updateDatacenterRegistry(SimulationRegistry sr,DatacenterRegistry datacenter) {
       	//SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<DatacenterRegistry> datacenters=sr.getDatacenterList();
        for(DatacenterRegistry dr:datacenters) {
            if(dr.getId()==datacenter.getId()){
            	dr=datacenter;
            	break;
            }
            	
        } 
    }
    
    /** 
     * Gets an existing datacenter with the given name.
     *
     * @param   datacenterName  the name of the datacenter to be retrieved.
     * @return                  the datacenter, if it exists; <code>null</code>
     *                          otherwise.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */      
    public boolean removeDatacenterRegistry(SimulationRegistry sr,String datacenterName) {
       	SimulationRegistryDAO srDAO=new SimulationRegistryDAO();
    	List<DatacenterRegistry> datacenters=sr.getDatacenterList();
        try {
			for(DatacenterRegistry dr:datacenters) {
			    if(dr.getName().equals(datacenterName)){
			    	datacenters.remove(dr);
			    	
			        //Remove the related network entries
			        NetworkMapEntryDAO neDAO = new NetworkMapEntryDAO();
			        neDAO.removeEntries(sr,datacenterName); 
		//	        srDAO.updateSimulationRegistry(sr);
			    	return true;
			    }
			    	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
               
        return false;
    }    
    
    /**
     * Gets a list of all existing datacenters.
     *
     * @return          a list containing all the datacenter registries
     *                  in the database; <code>null</code> if no datacenters
     *                  were found.
     * @see             DatacenterRegistry
     * @since           1.0
     */
  
	public List<DatacenterRegistry> getListOfDatacenters(SimulationRegistry sr) {
       
       	//SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<DatacenterRegistry> datacenters=sr.getDatacenterList();

        
        return datacenters;        
    }     
    
    /** 
     * Gets the number of datacenters in the database.
     *
     * @return                  the number of datacenters.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */
    public int getNumOfDatacenters(SimulationRegistry sr) {
        return getListOfDatacenters(sr).size();
    }
    
    /** 
     * Gets all existing datacenters' names.
     *
     * @return                  an array containing the names of all datacenters.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */    
    public String[] getAllDatacentersNames(SimulationRegistry sr) {
        List<DatacenterRegistry> datacenterList = getListOfDatacenters(sr);
        String[] names =  new String[datacenterList.size()];

        for(int i=0; i<datacenterList.size(); i++) {
            names[i] = datacenterList.get(i).getName();
        }

        return names;
    }    
    
    /** 
     * Gets a list of all hosts of a given datacenter.
     *
     * @param   datacenterId    the id of the datacenter of which the hosts
     *                          will be listed.
     * @return                  a list containing all hosts of the
     *                          datacenter; <code>null</code> if the datacenter
     *                          does not exist.
     * @see                     DatacenterRegistry
     * @see                     HostRegistry
     * @since                   1.0
     */    
    public List<HostRegistry> getListOfHosts(SimulationRegistry sr,long datacenterId) {
        DatacenterRegistry datacenter = null;
       	//SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<DatacenterRegistry> datacenters=sr.getDatacenterList();
        for(DatacenterRegistry dr:datacenters) {
            if(dr.getId()==datacenterId){
            	datacenter=dr;
            	break;
            }
            	
        } 
        
        return datacenter.getHostList();        
    }    
    
    /** 
     * Gets a host registry based on its id and the id of the
     * datacenter that owns it.
     * 
     * @param   hostId          the id of the host to be retrieved.
     * @param   datacenterId    the id of the datacenter that owns the host
     *                          to be retrieved.
     * @return                  the host registry, if it exists; 
     *                          <code>null</code> otherwise.
     * @see                     DatacenterRegistry
     * @see                     HostRegistry
     * @since                   1.0
     */    
    public HostRegistry getHostRegistry(SimulationRegistry sr,long hostId, long datacenterId) {
        HostRegistry host = null;
        for(HostRegistry hr : getListOfHosts(sr,datacenterId)) {
            if(hr.getId() == hostId) {
                host = hr;
                break;
            }
        }
        
        return host;          
    }
    
    /** 
     * Gets the number of hosts of a given datacenter.
     * 
     * @param   datacenterId    the id of the datacenter that owns the hosts
     *                          to be counted.
     * @return                  the number of hosts of the datacenter.
     * @see                     DatacenterRegistry
     * @see                     HostRegistry
     * @since                   1.0
     */    
    public int getNumOfHosts(SimulationRegistry sr,long datacenterId) {
        int numOfHosts = 0;
        for(HostRegistry host : getListOfHosts(sr,datacenterId)) {
            numOfHosts += host.getAmount();
        }
        return numOfHosts;
    }    
    
    /** 
     * Gets the names of all hosts of a given datacenter.
     * 
     * @param   datacenterId    the id of the datacenter that owns the hosts.
     * @return                  an array containing the names of all hosts
     *                          of the datacenter.
     * @see                     DatacenterRegistry
     * @see                     HostRegistry
     * @since                   1.0
     */    
    public String[] getHostNames(SimulationRegistry sr,long datacenterId) {
        int n = getNumOfHosts(sr,datacenterId);
        String[] names = new String[n];

        for(int i=0; i<n; i++) {
            names[i]="Host"+i;
        }

        return names;
    }
    
    /** 
     * Gets a list of all storage area networks of a given datacenter.
     *
     * @param   datacenterId    the id of the datacenter of which the SAN
     *                          will be listed.
     * @return                  a list containing all SAN of the
     *                          datacenter; <code>null</code> if the datacenter
     *                          does not exist.
     * @see                     DatacenterRegistry
     * @see                     SanStorageRegistry
     * @since                   1.0
     */      
    public List<SanStorageRegistry> getListOfSans(SimulationRegistry sr,long datacenterId) {
        DatacenterRegistry datacenter = null;
       	//SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<DatacenterRegistry> datacenters=sr.getDatacenterList();
        for(DatacenterRegistry dr:datacenters) {
            if(dr.getId()==datacenterId){
            	datacenter=dr;
            	break;
            }
            	
        } 
        
        return datacenter.getSanList();        
    }    
    
    /** 
     * Gets a SAN registry based on its name and the id of the
     * datacenter that owns it.
     * 
     * @param   sanStorageName  the name of the SAN to be retrieved.
     * @param   datacenterId    the id of the datacenter that owns the SAN
     *                          to be retrieved.
     * @return                  the SAN registry, if it exists; 
     *                          <code>null</code> otherwise.
     * @see                     DatacenterRegistry
     * @see                     SanStorageRegistry
     * @since                   1.0
     */        
    public SanStorageRegistry getSanStorageRegistry(SimulationRegistry sr,String sanStorageName, long datacenterId) {
        DatacenterRegistry datacenter = null;
       	//SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<DatacenterRegistry> datacenters=sr.getDatacenterList();
        for(DatacenterRegistry dr:datacenters) {
            if(dr.getId()==datacenterId){
            	datacenter=dr;
            	break;
            }
            	
        }        
        
        SanStorageRegistry sanStorage = null;
        for(SanStorageRegistry ssr : datacenter.getSanList()) {
            if(ssr.getName().equals(sanStorageName)) {
                sanStorage = ssr;
                break;
            }
        }
        
        return sanStorage;
    }        
    
    /** 
     * Gets the number of processing elements of a given datacenter.
     * 
     * @param   datacenterId    the id of the datacenter that owns the
     *                          processing elements to be counted.
     * @return                  the number of processing elements of the
     *                          datacenter.
     * @see                     DatacenterRegistry
     * @see                     HostRegistry
     * @since                   1.0
     */    
    public int getNumOfPes(SimulationRegistry sr,long datacenterId) {
        int numOfPes=0;

        for(HostRegistry h : getListOfHosts(sr,datacenterId)) {
            numOfPes+=(h.getNumOfPes()*h.getAmount());
        }

        return numOfPes;
    }
    
    /** 
     * Gets the total MIPS capacity of the datacenter.
     * 
     * @param   datacenterId    the id of the datacenter.
     * @return                  the total MIPS capacity of the datacenter.
     * @see                     DatacenterRegistry
     * @see                     HostRegistry
     * @since                   1.0
     */      
    public double getMips(SimulationRegistry sr,long datacenterId) {
        double mips=0;

        for(HostRegistry h : getListOfHosts(sr,datacenterId)) {
            mips+=(h.getNumOfPes()*h.getMipsPerPe()*h.getAmount());
        }
        return mips;
    }
    
    /** 
     * Gets the total storage capacity of the datacenter.
     * 
     * @param   datacenterId    the id of the datacenter.
     * @return                  the total storage capacity of the datacenter.
     * @see                     DatacenterRegistry
     * @see                     HostRegistry
     * @see                     SanStorageRegistry
     * @since                   1.0
     */      
    public long getStorageCapacity(SimulationRegistry sr,long datacenterId) {
        long storage=0;

        for(HostRegistry h : getListOfHosts(sr,datacenterId)) {
            storage=LongOperations.saturatedAdd(LongOperations.saturatedMultiply(h.getStorage(),h.getAmount()),storage);
        }

        for(SanStorageRegistry s : getListOfSans(sr,datacenterId)) {
            storage=LongOperations.saturatedAdd(storage, (long)s.getCapacity());
        }

        return storage;
    }
    
    /** 
     * Gets the total RAM capacity of the datacenter.
     * 
     * @param   datacenterId    the id of the datacenter.
     * @return                  the total RAM capacity of the datacenter.
     * @see                     DatacenterRegistry
     * @see                     HostRegistry
     * @since                   1.0
     */     
    public long getRam(SimulationRegistry sr,long datacenterId) {
        long ram=0;

        for(HostRegistry h : getListOfHosts(sr,datacenterId)) {
            ram=LongOperations.saturatedAdd(LongOperations.saturatedMultiply(h.getRam(),h.getAmount()),ram);
        }
        return ram;
    }
    
    /** 
     * Gets the total bandwidth capacity of the datacenter.
     * 
     * @param   datacenterId    the id of the datacenter.
     * @return                  the total bandwidth capacity of the datacenter.
     * @see                     DatacenterRegistry
     * @see                     HostRegistry
     * @see                     SanStorageRegistry
     * @since                   1.0
     */     
    public long getBandwidth(SimulationRegistry sr,long datacenterId) {
        long bw=0;

        for(HostRegistry h : getListOfHosts(sr,datacenterId)) {
            bw=LongOperations.saturatedAdd(LongOperations.saturatedMultiply(h.getBw(),h.getAmount()),bw);
        }
        return bw;
    }    
    
    /** 
     * Gets the total number of hosts regarding all datacenters.
     * 
     * @return                  the total number of hosts.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */     
    public int getTotalNumOfHosts(SimulationRegistry sr) {
        int numOfHosts=0;

        for(DatacenterRegistry d : getListOfDatacenters(sr)) {
            numOfHosts += getNumOfHosts(sr,d.getId());
        }

        return numOfHosts;
    }    
    
    /** 
     * Gets the total number of processing elements regarding all datacenters.
     * 
     * @return                  the total number of processing elements.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */       
    public int getTotalNumOfPes(SimulationRegistry sr) {
        int numOfPes=0;

        for(DatacenterRegistry d : getListOfDatacenters(sr)) {
            numOfPes += getNumOfPes(sr,d.getId());
        }
        return numOfPes;
    }    
    
    /** 
     * Gets the total MIPS capacity regarding all datacenters.
     * 
     * @return                  the total MIPS capacity.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */       
    public double getTotalMips(SimulationRegistry sr) {
        double mips=0;

        for(DatacenterRegistry d : getListOfDatacenters(sr)) {
            mips += getMips(sr,d.getId());
        }
        return mips;
    }    
    
    /** 
     * Gets the total RAM capacity regarding all datacenters.
     * 
     * @return                  the total RAM capacity.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */      
    public long getTotalRam(SimulationRegistry sr) {
        long ram=0;

        for(DatacenterRegistry d : getListOfDatacenters(sr)) {
            ram += getRam(sr,d.getId());
        }
        return ram;
    }    
   
    /** 
     * Gets the total storage capacity regarding all datacenters.
     * 
     * @return                  the total storage capacity.
     * @see                     DatacenterRegistry
     * @since                   1.0
     */      
    public long getTotalStorageCapacity(SimulationRegistry sr) {
        long storage = 0;

        for(DatacenterRegistry d : getListOfDatacenters(sr)) {
            storage += getStorageCapacity(sr,d.getId());
        }
        return storage;
    }    
    
}
