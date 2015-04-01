/* 
 * Copyright (c) 2010-2012 Thiago T. Sá
 * 
 * This file is part of org.app.
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

package org.app.extensions;


import org.app.dao.CustomerRegistryDAO;
import org.app.dao.SettingDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;
import org.app.models.VirtualMachineRegistry;
import org.app.utils.CloudsimConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.network.datacenter.AppCloudlet;
import org.cloudbus.cloudsim.network.datacenter.NetDatacenterBroker;
import org.cloudbus.cloudsim.network.datacenter.NetworkCloudletSpaceSharedScheduler;
import org.cloudbus.cloudsim.network.datacenter.NetworkConstants;
import org.cloudbus.cloudsim.network.datacenter.NetworkVm;
import org.cloudbus.cloudsim.network.datacenter.WorkflowApp;


/**
 * A subtype of CloudSim's DatacenterBroker class. 
 * All user-implemented new broker policies must be a subtype of this class.
 * 
 * @see         BrokerPolicy
 * @author      chenyang
 * @since       1.0
 */
public abstract class NetBroker extends NetDatacenterBroker {
    
    /** The maximum length of cloudlets assigned to this broker. */
    private long maxLengthOfCloudlets;
    
    /** The cloudlet id. */
    private int cloudletId;
    

    private int cloudletsSubmitted=0;
    
    private Map<Integer, Integer> appCloudletRecieved;
    
    private SimulationRegistry sr;
    /** 
     * Initializes a new instance of this class with the given name.
     *
     * @param   name    the name of the broker.
     * @since           1.0
     */     
    public NetBroker(SimulationRegistry sr,String name) throws Exception {
        super(name);
        this.sr=sr;
        this.cloudletId = sr.getCustomerList().get(0).getUtilizationProfile().getNumOfCloudlets();
        this.maxLengthOfCloudlets = sr.getCustomerList().get(0).getUtilizationProfile().getLength();
        this.appCloudletRecieved=new HashMap<Integer,Integer>();
    }

    
    /** 
     * Processes the characteristics of datacenters assigned to this broker.
     *
     * @param   ev  a simulation event.
     * @since       1.0
     */        
    @Override
    protected void processResourceCharacteristics(SimEvent ev) {
        DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
        getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);

        if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
                setDatacenterRequestedIdsList(new ArrayList<Integer>());
                createVmsInDatacenterBase(getDatacenterIdsList().get(0));
        }
    }
    
    @Override
	protected void createVmsInDatacenterBase(int datacenterId) {
		// send as much vms as possible for this datacenter before trying the
		// next one
		int requestedVms = 0;

		// All host will have two VMs (assumption) VM is the minimum unit
		if (createvmflag) {
			CreateVMs(datacenterId);
			createvmflag = false;
		}
		
		// generate Application execution Requests
		for (int i = 0; i < 2; i++) {
//组合任务
//			this.getAppCloudletList().add(
//					new SpecialApp(AppCloudlet.APP_Workflow, NetworkConstants.currentAppId, 0, 0, getId(),sr));
//普通任务
			this.getAppCloudletList().add(
					new WorkflowApp(AppCloudlet.APP_Workflow, NetworkConstants.currentAppId, 0, 0, getId()));
	
			NetworkConstants.currentAppId++;

		}
		
		int k = 0;

		
		// schedule the application on VMs
		for (AppCloudlet app : this.getAppCloudletList()) {

			List<Integer> vmids = new ArrayList<Integer>();
			int numVms = linkDC.getVmList().size();
			UniformDistr ufrnd = new UniformDistr(0, numVms, 5);
			for (int i = 0; i < app.numbervm; i++) {

				int vmid = (int) ufrnd.sample();
				vmids.add(vmid);

			}

			if (vmids != null) {
				if (!vmids.isEmpty()) {

					app.createCloudletList(vmids);
					for (int i = 0; i < app.clist.size(); i++) {
						app.clist.get(i).setUserId(getId());
						
						appCloudletRecieved.put(app.appID, app.numbervm);
						this.getCloudletSubmittedList().add(app.clist.get(i));
						cloudletsSubmitted++;
						
						Log.printLine(CloudSim.clock()+": "+getName()+ ": Sending cloudlet "+app.clist.get(i).getCloudletId()+" to VM #"+app.clist.get(i).getVmId());
						// Sending cloudlet
						sendNow(
								getVmsToDatacentersMap().get(this.getVmList().get(0).getId()),
								CloudSimTags.CLOUDLET_SUBMIT,
								app.clist.get(i));
					}
					System.out.println("app" + (k++));
				}
			}

		}	
		
		setAppCloudletList(new ArrayList<AppCloudlet>());
		if (NetworkConstants.iteration < 10) {

			NetworkConstants.iteration++;
			this.schedule(getId(), NetworkConstants.nexttime, CloudSimTags.NextCycle);
		}

		setVmsRequested(requestedVms);
		setVmsAcks(0);
	}
    private void CreateVMs(int datacenterId) {
		// two VMs per host
//		int numVM = linkDC.getHostList().size() * NetworkConstants.maxhostVM;
    	List<CustomerRegistry> crlist=sr.getCustomerList();
    	int i=0;
    	Map<Integer,Integer> tmpmap=new HashMap<Integer,Integer>();
    	for(CustomerRegistry customer:crlist){
			List<VirtualMachineRegistry> vmlist=customer.getVmList();
    		for (VirtualMachineRegistry vm:vmlist) {
    			int vmid =i;
    			int mips = (int) vm.getMips();
    			long size = vm.getSize(); // image size (MB)
    			int ram = vm.getRam(); // vm memory (MB)
    			long bw = vm.getBw();
    			int pesNumber = vm.getPesNumber();
    			String vmm = vm.getVmm(); // VMM name
    			// create VM
    			NetworkVm netvm = new NetworkVm(
    					vmid,
    					getId(),
    					mips,
    					pesNumber,
    					ram,
    					bw,
    					size,
    					vmm,
    					new NetworkCloudletSpaceSharedScheduler());
    			
    			
    			tmpmap.put((int)vm.getId(),vmid);
    			CloudsimConstants.vmIdMap.put((int)sr.getId(),tmpmap);  
 
    			i++;
    			linkDC.processVmCreateNetwork(netvm);
    			// add the VM to the vmList
    			getVmList().add(netvm);
    			getVmsToDatacentersMap().put(vmid, datacenterId);
    			getVmsCreatedList().add(VmList.getById(getVmList(), vmid));
    		}
    		
    	}
     	
	}

    
   
    /** 
     * Creates virtual machines in the datacenters managed by this broker.
     *
     * @param   datacenterIdList    the list of datacenters managed by this broker.
     * @since                       1.0
     */       
    protected void createVmsInDatacenter(List<Integer> datacenterIdList) {

        List<Vm> auxList = new ArrayList<Vm>();
        for(Vm vm : getVmList()) {
            auxList.add(vm);
        }

        int requestedVms = 0;
        for (Integer datacenterId : datacenterIdList) {
            String datacenterName = CloudSim.getEntityName(datacenterId);
            for (Vm vm : auxList) {
                if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
                    Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId() + " in " + datacenterName);
                    sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
                    requestedVms++;
                    auxList.remove(vm);
                    break;
                }
            }
            getDatacenterRequestedIdsList().add(datacenterId);
            setVmsRequested(requestedVms);
        }

        setVmsAcks(0);
    }

    
    public int getCloudletsSubmitted() {
		return cloudletsSubmitted;
	}

   
	public Map<Integer, Integer> getAppCloudletRecieved() {
		return appCloudletRecieved;
	}


	public void setCloudletsSubmitted(int cloudletsSubmitted) {
		this.cloudletsSubmitted = cloudletsSubmitted;
	}


	public void setAppCloudletRecieved(Map<Integer, Integer> appCloudletRecieved) {
		this.appCloudletRecieved = appCloudletRecieved;
	}


	/** 
     * An abstract method whose implementations must return an id of
     * a datacenter.
     *
     * @return  the id of a datacenter.
     * @since   1.0
     */     
    public abstract int getDatacenterId();
    
    /** 
     * An abstract method whose implementations must return a list of ids
     * from datacenters managed by the broker.
     *
     * @return  a list of ids from datacenters managed by this broker.
     * @since   1.0
     */  
    public abstract List<Integer> getDatacenterIdList();

}
