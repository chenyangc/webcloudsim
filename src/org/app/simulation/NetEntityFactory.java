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

package org.app.simulation;



import org.app.dao.CustomerRegistryDAO;
import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.NetworkMapEntryDAO;
import org.app.enums.NetBrokerPolicy;
import org.app.enums.NetAllocationPolicy;
import org.app.extensions.NetDatacenter;
import org.app.extensions.NetAppDatacenterBroker;
import org.app.models.*;
import org.app.utils.CloudsimConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.network.datacenter.EdgeSwitch;
import org.cloudbus.cloudsim.network.datacenter.NetDatacenterBroker;
import org.cloudbus.cloudsim.network.datacenter.NetworkConstants;
import org.cloudbus.cloudsim.network.datacenter.NetworkHost;
import org.app.extensions.NetPowerHost;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.PeProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;



public class NetEntityFactory {
	
	private HashMap<String, NetDatacenter> datacenters ;
	private HashMap<String, NetDatacenterBroker> brokers ;
	private Map<Integer,Integer>  vmforHost;
	private SimulationRegistry sr;
	public HashMap<String, NetDatacenter> getDatacenters() {
		return datacenters;
	}

	public void setDatacenters(HashMap<String, NetDatacenter> datacenters) {
		this.datacenters = datacenters;
	}
	
	
	

	public HashMap<String, NetDatacenterBroker> getBrokers() {
		return brokers;
	}

	public void setBrokers(HashMap<String, NetDatacenterBroker> brokers) {
		this.brokers = brokers;
	}

	public Map<Integer, Integer> getVmforHost() {
		return vmforHost;
	}
	public void setVmforHost(Map<Integer, Integer> vmforHost) {
		this.vmforHost = vmforHost;
	}
	public NetEntityFactory(){
	
		
	}
	public void reBuild(SimulationRegistry sr){
		this.sr=sr;
		vmforHost=new HashMap<Integer, Integer>();
		List<CustomerRegistry> customerlist=sr.getCustomerList();
		for(CustomerRegistry cr:customerlist){
			List<VirtualMachineRegistry> vmlist=cr.getVmList();			
			for(VirtualMachineRegistry vm:vmlist){
				
				vmforHost.put((int) vm.getId(), vm.getHostId());
			//	System.out.println("!@!!!vmfor host"+ vmforHost);
			}
		}
		
		datacenters=createDatacenters(sr);
		try {
			brokers=createBrokers(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		setUpNetworkLinks(sr,datacenters, brokers);
	}
    /**
     * Creates instances of CloudSim's NetDatacenter class from a list of 
     * datacenter registries.
     * 
     * @param   dcrList a list of datacenter registries.
     * @return          a map containing names of datacenters as keys and
     *                  NetDatacenter instances as values.
     * @since           1.0
     */    
	private HashMap<String, NetDatacenter> createDatacenters(SimulationRegistry sr) {

        List<DatacenterRegistry> dcrList = sr.getDatacenterList();
        HashMap<String, NetDatacenter> map = new HashMap<String, NetDatacenter>();

        for (DatacenterRegistry dcr : dcrList) {
            List<NetPowerHost> hostList = createHosts(dcr.getHostList());
            if (hostList == null) {
                return null;
            }

            DatacenterCharacteristics chars = new DatacenterCharacteristics(dcr.getArchitecture(),
                    dcr.getOs(),
                    dcr.getVmm(),
                    hostList,
                    dcr.getTimeZone(),
                    dcr.getCostPerSec(),
                    dcr.getCostPerMem(),
                    dcr.getCostPerStorage(),
                    dcr.getCostPerBw());

            LinkedList<Storage> storageList = createStorageList(dcr.getSanList());
                

            try {
            	
                NetAllocationPolicy instance = NetAllocationPolicy.getInstance(dcr.getAllocationPolicyAlias());
                VmAllocationPolicy allocationPolicy = instance.getPolicy(vmforHost,(int)sr.getId(),hostList, dcr.getUpperUtilizationThreshold(),
                                                                         dcr.getLowerUtilizationThreshold(),
                                                                         dcr.getSchedulingInterval(),
                                                                         dcr.getAllocationPolicyAlias());
                if (allocationPolicy == null) {
                    System.out.println("Error loading \"" + dcr.getAllocationPolicyAlias() + "\" allocation policy.");
                    return null;
                }

                NetDatacenter newDC = new NetDatacenter(dcr.getName(),
                        chars,
                        allocationPolicy,
                        storageList,
                        dcr.getSchedulingInterval(),
                        dcr.getMonitoringInterval());
                CreateNetwork(hostList.size(), newDC);
                map.put(dcr.getName(), newDC);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

	
	static void CreateNetwork(int numhost, NetDatacenter dc) {

		// Edge Switch
		EdgeSwitch edgeswitch[] = new EdgeSwitch[2];

		for (int i = 0; i < 2; i++) {
			edgeswitch[i] = new EdgeSwitch("Edge" + i, NetworkConstants.EDGE_LEVEL, dc);
			
			dc.Switchlist.put(edgeswitch[i].getId(), edgeswitch[i]);

		}

		for (Host hs : dc.getHostList()) {
			NetworkHost hs1 = (NetworkHost) hs;
			hs1.bandwidth = NetworkConstants.BandWidthEdgeHost;
			int switchnum = (int) (hs.getId() / NetworkConstants.EdgeSwitchPort);
			edgeswitch[switchnum].hostlist.put(hs.getId(), hs1);
			dc.HostToSwitchid.put(hs.getId(), edgeswitch[switchnum].getId());
			hs1.sw = edgeswitch[switchnum];
			List<NetworkHost> hslist = hs1.sw.fintimelistHost.get(0D);
			if (hslist == null) {
				hslist = new ArrayList<NetworkHost>();
				hs1.sw.fintimelistHost.put(0D, hslist);
			}
			hslist.add(hs1);

		}

	}
	
	
    /**
     * Creates instances of CloudSim's NetPowerHost class from a list of 
     * host registries.
     * 
     * @param   hostList    a list of datacenter registries.
     * @return              a list of NetPowerHost instances.
     * @since               1.0
     */        
    private List<NetPowerHost> createHosts(List<HostRegistry> hostList) {
        List<NetPowerHost> list = new ArrayList<NetPowerHost>();

        int i = 0;
        Map<Integer,Integer> tmpmap=new HashMap<Integer,Integer>();
        for (HostRegistry hr : hostList) {
            for (int n = 0; n < hr.getAmount(); n++) {
            	
                List<Pe> peList = createPes(hr);
                if (peList == null) {
                    return null;
                }

                RamProvisioner rp = org.app.enums.RamProvisioner.getInstance(hr.getRamProvisionerAlias()).getProvisioner(hr.getRam(), hr.getRamProvisionerAlias());
                if (rp == null) {
                	System.out.println("Error loading \"" + hr.getRamProvisionerAlias() + "\" RAM provisioner.");
                    return null;
                }

                BwProvisioner bp = org.app.enums.BwProvisioner.getInstance(hr.getBwProvisionerAlias()).getProvisioner(hr.getBw(), hr.getBwProvisionerAlias());
                if (bp == null) {
                	System.out.println("Error loading \"" + hr.getBwProvisionerAlias() + "\" bandwidth provisioner.");
                    return null;
                }

                VmScheduler vs = org.app.enums.VmScheduler.getInstance(hr.getSchedulingPolicyAlias()).getScheduler(peList, hr.getSchedulingPolicyAlias());
                if (vs == null) {
                	System.out.println("Error loading \"" + hr.getSchedulingPolicyAlias() + "\" VM scheduler.");
                    return null;
                }
                
                PowerModel pm = org.app.enums.PowerModel.getInstance(hr.getPowerModelAlias()).getModel(hr.getMaxPower(), hr.getStaticPowerPercent(), hr.getPowerModelAlias());
                if (pm == null) {
                	System.out.println( "Error loading \"" + hr.getPowerModelAlias() + "\" power model.");
                    return null;
                }  
                
                NetPowerHost host=new NetPowerHost(i, rp, bp, hr.getStorage(), peList, vs,pm);
                
                list.add(host);              
                tmpmap.put((int)hr.getId(), i);
           
                i++;
            }
        }
        CloudsimConstants.hostIdMap.put((int)sr.getId(), tmpmap);

        return list;
    }

    /**
     * Creates instances of CloudSim's PowerPe class from a host registry.
     * 
     * @param   hr  a host registry.
     * @return      a list of PowerPe instances.
     * @since       1.0
     */     
    private  List<Pe> createPes(HostRegistry hr) {
        List<Pe> list = new ArrayList<Pe>();

        for (int i = 0; i < hr.getNumOfPes(); i++) {
            PeProvisioner pp = org.app.enums.PeProvisioner.getInstance(hr.getPeProvisionerAlias()).getProvisioner(hr.getMipsPerPe(), hr.getPeProvisionerAlias());
            if (pp == null) {
            	System.out.println("Error loading \"" + hr.getPeProvisionerAlias() + "\" PE provisioner.");
                return null;
            }

            list.add(new Pe(i, pp));
        }

        return list;
    }

    /**
     * Creates instances of CloudSim's SanStorage class from a list of
     * SAN registries.
     * 
     * @param   sanList a list of SAN registries.
     * @return          a list of SanStorage instances.
     * @since           1.0
     */         
    private LinkedList<Storage> createStorageList(List<SanStorageRegistry> sanList) {
        LinkedList<Storage> list = new LinkedList<Storage>();

        for (SanStorageRegistry sr : sanList) {
            try {
                list.add(new SanStorage(sr.getName(),
                        sr.getCapacity(),
                        sr.getBandwidth(),
                        sr.getNetworkLatency()));
            } catch (ParameterException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * Creates instances of CloudSim's NetAppDatacenterBroker class from a list of
     * customer registries.
     * 
     * @param   customerList    a list of customer registries.
     * @return                  a map containing names of customers as keys and
     *                          NetAppDatacenterBroker instances as values.
     * @since                   1.0
     */     
    private HashMap<String, NetDatacenterBroker> createBrokers(SimulationRegistry sr) throws Exception {
    	CustomerRegistryDAO crDAO=new CustomerRegistryDAO();
        List<CustomerRegistry> customerList = crDAO.getListOfCustomers(sr);
        HashMap<String, NetDatacenterBroker> map = new HashMap<String, NetDatacenterBroker>();
        
        try {
        	
            UtilizationProfile up = sr.getCustomerList().get(0).getUtilizationProfile();
           
            String brokername="broker";
            NetDatacenterBroker broker = NetBrokerPolicy.getInstance(up.getBrokerPolicyAlias()).createBroker(sr,brokername, up.getBrokerPolicyAlias());
            if (broker == null) {
            	System.out.println( "Error loading \"" + up.getBrokerPolicyAlias() + "\" broker.");
                return null;
            }
            broker.setLinkDC(datacenters.get(sr.getDatacenterList().get(0).getName()));
            

          
            for (CustomerRegistry cr : customerList) {

            	 String name = cr.getName();
//                int brokerId = broker.getId();
                
//                List<Vm> vmList = createVms(cr.getVmList(), brokerId);
//                if (vmList == null) {
//                    return null;
//                }
//
//                broker.submitVmList(vmList);
                
//                int cloudletNumber=cr.getUtilizationProfile().getNumOfCloudlets();
//                List<Cloudlet> cloudletList = createCloudlets(cloudletNumber,up, brokerId, new CustomerRegistryDAO().getNumOfVms(sr,cr.getId()));
//                if (cloudletList == null) {
//                    return null;
//                }

//                broker.submitCloudletList(cloudletList);
                map.put(cr.getName(), broker);
                
            }
        } catch (Exception ex) {
            Logger.getLogger(NewSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }



    /**
     * Sets up all the network links to be simulated,
     * 
     * @param   datacenters the datacenters being simulated.
     * @param   brokers     the brokers being simulated.
     * @since               1.0
     */      
    private void setUpNetworkLinks(SimulationRegistry sr,HashMap<String, NetDatacenter> datacenters,
            HashMap<String, NetAppDatacenterBroker> brokers) {

        NetworkMapEntryDAO neDAO = new NetworkMapEntryDAO();

        /*
         * Establish all links whose source is a datacenter
         */
        DatacenterRegistryDAO drDAO = new DatacenterRegistryDAO();
        String[] datacenterNames = drDAO.getAllDatacentersNames(sr);

        for (String source : datacenterNames) {
            NetDatacenter src = datacenters.get(source);

            List<NetworkMapEntry> destinationList = neDAO.getListOfDestinations(sr,source);

            for (NetworkMapEntry entry : destinationList) {
                String destinationName = entry.getDestination();

                if (drDAO.getDatacenterRegistry(sr,destinationName) != null) { //destination is a datacenter
                    NetDatacenter dest = datacenters.get(destinationName);
                    NetworkTopology.addLink(src.getId(), dest.getId(), entry.getBandwidth(), entry.getLatency());
                } else { //destination is a customer
                    NetAppDatacenterBroker dest = brokers.get(destinationName);
                    NetworkTopology.addLink(src.getId(), dest.getId(), entry.getBandwidth(), entry.getLatency());
                }
            }
        }


        /*
         * Establish all links whose source is a customer
         */
        CustomerRegistryDAO crDAO = new CustomerRegistryDAO();
        String[] customerNames = crDAO.getCustomersNames(sr);

        for (String source : customerNames) {
            NetAppDatacenterBroker src = brokers.get(source);

            List<NetworkMapEntry> destinationList = neDAO.getListOfDestinations(sr,source);

            for (NetworkMapEntry entry : destinationList) {
                String destinationName = entry.getDestination();

                if (drDAO.getDatacenterRegistry(sr,destinationName) != null) { //destination is a datacenter
                    NetDatacenter dest = datacenters.get(destinationName);
                    NetworkTopology.addLink(src.getId(), dest.getId(), entry.getBandwidth(), entry.getLatency());
                } else { //destination is a customer
                    NetAppDatacenterBroker dest = brokers.get(destinationName);
                    NetworkTopology.addLink(src.getId(), dest.getId(), entry.getBandwidth(), entry.getLatency());
                }
            }
        }

    }
    
}
