package org.app.dao;

import java.util.List;

import org.app.models.CustomerRegistry;
import org.app.models.Setting;
import org.app.models.SimulationRegistry;
import org.app.models.UtilizationProfile;
import org.app.models.VirtualMachineRegistry;
import org.app.utils.LongOperations;

/**
 * CustomerRegistryDAO provides basic CRUD operations related to the 
 * {@link CustomerRegistry} class.
 * It also provides access to general information about virtual machines
 * deployed by a given customer.
 * 
 * @see         Setting
 * @author      ChenYang
 * @since       1.0
 */
public class CustomerRegistryDAO {
    
    /** 
     * Inserts a new customer registry into the database.
     * The customer's name must be unique.
     *
     * @param   cr      the customer registry to be inserted.
     * @return          <code>true</code> if the customer registry
     *                  has been successfully inserted; 
     *                  <code>false</code> otherwise.
     * @see             CustomerRegistry
     * @since           1.0
     */
	
	
	
    public boolean insertNewCustomerRegistry(SimulationRegistry sr,CustomerRegistry cr) {
//    	SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<CustomerRegistry> customers=sr.getCustomerList();
        for(CustomerRegistry customer : customers) {
            if(customer.getId()==cr.getId()){
            	return false;
            }
            	
        }
        customers.add(cr);
                   
        //Insert new network entries
        NetworkMapEntryDAO neDAO = new NetworkMapEntryDAO();
        neDAO.insertNewEntityEntries(sr,cr.getName());
        
        return true;
    }
    
    /** 
     * Gets an existing customer with the given name.
     *
     * @param   customerName    the name of the customer to be retrieved.
     * @return                  the customer, if it exists; <code>null</code>
     *                          otherwise.
     * @see                     CustomerRegistry
     * @since                   1.0
     */
    public CustomerRegistry getCustomerRegistry(SimulationRegistry sr,String customerName) {
//    	SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<CustomerRegistry> customers=sr.getCustomerList();
    	CustomerRegistry cm = null;
    	for(CustomerRegistry customer : customers) {
            if(customer.getName().equals(customerName)){
            	cm=customer;
            	break;
            }
            	
        }
    	return cm;
    }
    
    /** 
     * Gets an existing customer with the given id.
     *
     * @param   customerId      the id of the customer to be retrieved.
     * @return                  the customer, if it exists; <code>null</code>
     *                          otherwise.
     * @see                     CustomerRegistry
     * @since                   1.0
     */
    public CustomerRegistry getCustomerRegistry(SimulationRegistry sr,long customerId) {
//    	SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<CustomerRegistry> customers=sr.getCustomerList();
    	CustomerRegistry cm = null;
    	for(CustomerRegistry customer : customers) {
            if(customer.getId()==customerId){
            	cm=customer;
            	break;
            }
            	
        }
    	return cm;
    }     
    
    /** 
     * Updates an existing customer registry.
     *
     * @param   customer        the customer to be updated.
     * @see                     CustomerRegistry
     * @since                   1.0
     */
    public void updateCustomerRegistry(SimulationRegistry sr,CustomerRegistry customer) {
//    	SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<CustomerRegistry> customers=sr.getCustomerList();
    	for(CustomerRegistry cm : customers) {
            if(cm.getId()==customer.getId()){
            	cm=customer;
            	break;
            }
            	
        }
    }
    
    /** 
     * Removes an existing customer with the given name.
     *
     * @param   customerName    the id of the customer to be removed.
     * @return                  <code>true</code>, if the customer has been
     *                          successfully removed; <code>false</code>
     *                          otherwise.
     * @see                     CustomerRegistry
     * @since                   1.0
     */
    public boolean removeCustomerRegistry(SimulationRegistry sr,String customerName) {
//    	SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<CustomerRegistry> customers=sr.getCustomerList();
    	for(CustomerRegistry cm : customers) {
            if(cm.getName().equals(customerName)){
            	customers.remove(cm);
                //Remove network entries
                NetworkMapEntryDAO neDAO = new NetworkMapEntryDAO();
                neDAO.removeEntries(sr,customerName);
            	return true;
            }
            	
        }
    	return false;
    	     
    }
    
    /** 
     * Gets a list of all existing customers.
     *
     * @return                  a list containing all the customer registries
     *                          in the database; <code>null</code> if no
     *                          customers were found.
     * @see                     CustomerRegistry
     * @since                   1.0
     */
 
	public List<CustomerRegistry> getListOfCustomers(SimulationRegistry sr) {
 //   	SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<CustomerRegistry> customers=sr.getCustomerList();
            
        return customers;
    }
    
    /** 
     * Gets the number of customers in the database.
     *
     * @return                  the number of customers.
     * @see                     CustomerRegistry
     * @since                   1.0
     */
    public int getNumOfCustomers(SimulationRegistry sr) {
        return getListOfCustomers(sr).size();
    }   
    
    /** 
     * Gets all existing customers' names.
     *
     * @return                  an array containing the names of all customers.
     * @see                     CustomerRegistry
     * @since                   1.0
     */
    public String[] getCustomersNames(SimulationRegistry sr) {
        List<CustomerRegistry> customerList = getListOfCustomers(sr);
        String[] names = new String[customerList.size()];

        for(int i=0; i<customerList.size(); i++) {
            names[i] = customerList.get(i).getName();
        }

        return names;
    }    

    /** 
     * Gets a list of all virtual machines deployed by a given customer.
     *
     * @param   customerId      the id of the customer of which the virtual
     *                          machines will be listed.
     * @return                  a list containing all the virtual machines
     *                          deployed by the customer; <code>null</code> 
     *                          if no virtual machines were found.
     * @see                     CustomerRegistry
     * @see                     VirtualMachineRegistry
     * @since                   1.0
     */
    public List<VirtualMachineRegistry> getListOfVms(SimulationRegistry sr,long customerId) {
 //   	SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<CustomerRegistry> customers=sr.getCustomerList();
    	List<VirtualMachineRegistry> vmlist=null;
    	for(CustomerRegistry cm : customers) {
            if(cm.getId()==customerId){
            	vmlist=cm.getVmList();
            	break;
            }
            	
        }
    	return vmlist;
    }
    
    /** 
     * Gets a virtual machine registry based on its id and the id of the
     * customer who owns it.
     * 
     * @param   vmId            the id of the virtual machine to be retrieved.
     * @param   customerId      the id of the customer who owns the virtual
     *                          machine to be retrieved.
     * @return                  the virtual machine registry, if it exists; 
     *                          <code>null</code> otherwise.
     * @see                     CustomerRegistry
     * @see                     VirtualMachineRegistry
     * @since                   1.0
     */
    public VirtualMachineRegistry getVirtualMachineRegistry(SimulationRegistry sr,long vmId, long customerId) {
 //   	SimulationRegistry sr=srDAO.getSimulationRegistry(sr);
    	List<CustomerRegistry> customers=sr.getCustomerList();
    	 CustomerRegistry customer = null;
    	for(CustomerRegistry cm : customers) {
            if(cm.getId()==customerId){
            	customer=cm;
            }
            	
        }
    	    	  
        
        VirtualMachineRegistry vm = null;
        for(VirtualMachineRegistry vr : customer.getVmList()) {
            if(vr.getId() == vmId) {
                vm = vr;
                break;
            }
        }
        
        return vm;
    }    
    
    /** 
     * Gets the number of virtual machines deployed by a given customer.
     * 
     * @param   customerId      the id of the customer who owns the virtual
     *                          machines to be counted.
     * @return                  the number of virtual machines deployed by
     *                          the customer.
     * @see                     CustomerRegistry
     * @see                     VirtualMachineRegistry
     * @since                   1.0
     */
    public int getNumOfVms(SimulationRegistry sr,long customerId) {
        int numOfVms = 0;
        for(VirtualMachineRegistry vm : getListOfVms(sr,customerId)) {
            numOfVms += vm.getAmount();
        }
        return numOfVms;
    }    
    
    public int getNumOfVms(SimulationRegistry sr,String customerName) {
    	long customerId=this.getCustomerRegistry(sr,customerName).getId();
        return getNumOfVms(sr,customerId);
    }  
    
    /** 
     * Gets the names of all virtual machines deployed by a given customer.
     * 
     * @param   customerId      the id of the customer who owns the virtual
     *                          machines.
     * @return                  an array containing the names of all virtual
     *                          machines deployed by the customer.
     * @see                     CustomerRegistry
     * @see                     VirtualMachineRegistry
     * @since                   1.0
     */
    public String[] getNamesOfVms(SimulationRegistry sr,long customerId) {
        int n = getNumOfVms(sr,customerId);
        
        String[] names = new String[n];

        for(int i=0; i<n; i++) {
            names[i]="VM"+i;
        }

        return names;
    }    
    
    /** 
     * Gets the total demand of RAM from the virtual machines deployed by
     * a given customer.
     * 
     * @param   customerId      the id of the customer who owns the virtual
     *                          machines.
     * @return                  the total demand of RAM from all virtual
     *                          machines deployed by the customer.
     * @see                     CustomerRegistry
     * @see                     VirtualMachineRegistry
     * @since                   1.0
     */
    public long getRamDemand(SimulationRegistry sr,long customerId) {
        long sum = 0;
        for(VirtualMachineRegistry vmr : getListOfVms(sr,customerId)) {
            sum+=LongOperations.saturatedAdd(sum,(vmr.getRam()*vmr.getAmount()));
        }

        return sum;
    }
    
    /** 
     * Gets the total demand of bandwidth from the virtual machines deployed by
     * a given customer.
     * 
     * @param   customerId      the id of the customer who owns the virtual
     *                          machines.
     * @return                  the total demand of bandwidth from all virtual
     *                          machines deployed by the customer.
     * @see                     CustomerRegistry
     * @see                     VirtualMachineRegistry
     * @since                   1.0
     */
    public long getBwDemand(SimulationRegistry sr,long customerId) {
        long sum = 0;

        for(VirtualMachineRegistry vmr : getListOfVms(sr,customerId)) {
            sum+=LongOperations.saturatedAdd(sum,(vmr.getBw()*vmr.getAmount()));
        }

        return sum;
    }    
    
    /** 
     * Gets the total demand of MIPS from the virtual machines deployed by
     * a given customer.
     * 
     * @param   customerId      the id of the customer who owns the virtual
     *                          machines.
     * @return                  the total demand of MIPS from all virtual
     *                          machines deployed by the customer.
     * @see                     CustomerRegistry
     * @see                     VirtualMachineRegistry
     * @since                   1.0
     */    
    public long getMipsDemand(SimulationRegistry sr,long customerId) {
        long sum = 0;

        for(VirtualMachineRegistry vmr : getListOfVms(sr,customerId)) {
            String s = Double.toString(vmr.getMips()).split("\\.")[0];
            Long mips = Long.valueOf(s);
            sum+=LongOperations.saturatedAdd(sum,(mips*vmr.getAmount()));
        }

        return sum;
    }
    
    /** 
     * Gets the total demand of storage from the virtual machines deployed by
     * a given customer.
     * 
     * @param   customerId      the id of the customer who owns the virtual
     *                          machines.
     * @return                  the total demand of storage from all virtual
     *                          machines deployed by the customer.
     * @see                     CustomerRegistry
     * @see                     VirtualMachineRegistry
     * @since                   1.0
     */     
    public long getStorageDemand(SimulationRegistry sr,long customerId) {
        long sum = 0;

        for(VirtualMachineRegistry vmr : getListOfVms(sr, customerId)) {
            sum+=LongOperations.saturatedAdd(sum,vmr.getSize()*vmr.getAmount());
        }

        return sum;
    } 
    
    /** 
     * Gets the number of cloudlets created by all customers.
     * 
     * @return                  the total number of cloudlets created by
     *                          all customers.
     * @see                     CustomerRegistry
     * @see                     UtilizationProfile
     * @since                   1.0
     */      
    public long getTotalNumOfCloudlets(SimulationRegistry sr) {
        long numOfCloudlets = 0;

        for(CustomerRegistry c : getListOfCustomers( sr)) {
            numOfCloudlets+=c.getUtilizationProfile().getNumOfCloudlets();
        }
        
        return numOfCloudlets;
    }    
    
    /** 
     * Gets the total number of virtual machines deployed by all customers.
     * 
     * @return                  the total number of virtual machines deployed by
     *                          all customers.
     * @see                     CustomerRegistry
     * @see                     UtilizationProfile
     * @since                   1.0
     */ 
    public long getTotalNumOfVms(SimulationRegistry sr) {
        long numOfVms = 0;

        for(CustomerRegistry c : getListOfCustomers( sr)) {
            numOfVms=LongOperations.saturatedAdd(numOfVms,new CustomerRegistryDAO().getNumOfVms( sr,c.getId()));
        }
                
        return numOfVms;
    }
    
    /** 
     * Gets the average length of cloudlets created by all customers.
     * 
     * @return                  the average length of cloudlets created by
     *                          all customers.
     * @see                     CustomerRegistry
     * @see                     UtilizationProfile
     * @since                   1.0
     */ 
    public long getAvgLength(SimulationRegistry sr) {
        int numOfCostumers = getNumOfCustomers(sr);
        long[] avgArray = new long[numOfCostumers];
        long longSum=0;

        int i = 0;
        for(CustomerRegistry c : getListOfCustomers(sr)) {
            avgArray[i] = c.getUtilizationProfile().getLength();
            i++;
        }

        for(i = 0; i<avgArray.length; i++) {
            longSum=LongOperations.saturatedAdd(longSum, avgArray[i]);
        }

        return longSum/numOfCostumers;
    }    
    
    /** 
     * Gets the average file size of cloudlets created by all customers.
     * 
     * @return                  the average file size of cloudlets created by
     *                          all customers.
     * @see                     CustomerRegistry
     * @see                     UtilizationProfile
     * @since                   1.0
     */ 
    public long getAvgFileSize(SimulationRegistry sr) {
        int numOfCostumers = getNumOfCustomers(sr);
        long[] avgArray = new long[numOfCostumers];
        long longSum=0;

        int i = 0;
        for(CustomerRegistry c : getListOfCustomers(sr)) {
            avgArray[i] = c.getUtilizationProfile().getFileSize();
            i++;
        }

        for(i = 0; i<avgArray.length; i++) {
            longSum=LongOperations.saturatedAdd(longSum, avgArray[i]);
        }

        return longSum/numOfCostumers;
    }    
    
    /** 
     * Gets the average output size of cloudlets created by all customers.
     * 
     * @return                  the average output size of cloudlets created by
     *                          all customers.
     * @see                     CustomerRegistry
     * @see                     UtilizationProfile
     * @since                   1.0
     */ 
    public long getAvgOutputSize(SimulationRegistry sr) {
        int numOfCostumers = getNumOfCustomers(sr);
        long[] avgArray = new long[numOfCostumers];
        long longSum=0;

        int i = 0;
        for(CustomerRegistry c : getListOfCustomers(sr)) {
            avgArray[i]= c.getUtilizationProfile().getOutputSize();
            i++;
        }

        for(i = 0; i<avgArray.length; i++) {
            longSum=LongOperations.saturatedAdd(longSum, avgArray[i]);
        }

        return longSum/numOfCostumers;
    }    
    
    /** 
     * Gets the average image size of virtual machines deployed by all 
     * customers.
     * 
     * @return                  the average image size of virtual machines
     *                          deployed by all customers.
     * @see                     CustomerRegistry
     * @see                     VirtualMachineRegistry
     * @since                   1.0
     */ 
    public long getAvgImageSize(SimulationRegistry sr) {
        long sum = 0;

        for(CustomerRegistry c : getListOfCustomers(sr)) {
            for(VirtualMachineRegistry vmr : c.getVmList()) {
                sum=LongOperations.saturatedAdd(sum,vmr.getSize()*vmr.getAmount());
            }
        }

        return sum/getTotalNumOfVms(sr);
    }    
    
    /** 
     * Gets the average amount of RAM requested by all virtual machines
     * deployed by all customers.
     * 
     * @return                  the average amount of RAM requested by all
     *                          virtual machines.
     * @see                     CustomerRegistry
     * @see                     UtilizationProfile
     * @since                   1.0
     */ 
    public long getAvgRAM(SimulationRegistry sr) {
        long sum = 0;

        for(CustomerRegistry c : getListOfCustomers(sr)) {
            for(VirtualMachineRegistry vmr : c.getVmList()) {
                sum=LongOperations.saturatedAdd(sum,vmr.getRam()*vmr.getAmount());
            }
        }

        return sum/getTotalNumOfVms(sr);
    }    
    
    /** 
     * Gets the average amount of bandwidth requested by all virtual machines
     * deployed by all customers.
     * 
     * @return                  the average amount of bandwidth requested by all
     *                          virtual machines.
     * @see                     CustomerRegistry
     * @see                     UtilizationProfile
     * @since                   1.0
     */ 
    public long getAvgBw(SimulationRegistry sr) {
        long sum = 0;

        for(CustomerRegistry c : getListOfCustomers(sr)) {
            for(VirtualMachineRegistry vmr : c.getVmList()) {
                sum=LongOperations.saturatedAdd(sum,vmr.getBw()*vmr.getAmount());
            }
        }

        return sum/getTotalNumOfVms(sr);
    }
    
}
