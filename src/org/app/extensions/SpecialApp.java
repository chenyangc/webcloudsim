package org.app.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.app.dao.CustomerRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;
import org.app.models.UtilizationProfile;
import org.app.models.VirtualMachineRegistry;
import org.app.utils.CloudsimConstants;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.network.datacenter.AppCloudlet;
import org.cloudbus.cloudsim.network.datacenter.NetworkCloudlet;
import org.cloudbus.cloudsim.network.datacenter.NetworkConstants;
import org.cloudbus.cloudsim.network.datacenter.TaskStage;

public class SpecialApp extends AppCloudlet {

	private SimulationRegistry sr;
	
	private List<Long> crId;
	
	private CustomerRegistryDAO crDAO;
	
	private Random rand;
	
	public SpecialApp(int type, int appID, double deadline, int numbervm, int userId,SimulationRegistry sr) {
		super(type, appID, deadline, numbervm, userId);
		this.sr=sr;
		exeTime = 100;
		List<CustomerRegistry> crlist=sr.getCustomerList();
		crDAO=new CustomerRegistryDAO();
		rand=new Random();
		crId=new ArrayList<Long>();
		
//		for(CustomerRegistry cr:crlist){
//			crId.add(cr.getId());
//			if(cr.getReliedCustomerName()!=null){
//				if(!crId.contains(cr.getId())){
//					crId.add(cr.getId());					
//				}
//				long reliId=crDAO.getCustomerRegistry(sr, cr.getReliedCustomerName()).getId();
//				if(!crId.contains(reliId)){
//					crId.add(reliId);					
//				}
//			}
			
//		}
		this.numbervm=3;
	}

	@Override
	public void createCloudletList(List<Integer> vmIdList) {

		for(CustomerRegistry cr:sr.getCustomerList()){
			NetworkCloudlet netcl=createNetworkCloudlet(cr);	
			clist.add(netcl);
			if(cr.getReliedCustomerName()!=null){
				String reliedName=cr.getReliedCustomerName();
				CustomerRegistry reliedCr=crDAO.getCustomerRegistry(sr, reliedName);
				NetworkCloudlet reliedCl=createNetworkCloudlet(reliedCr);
				clist.add(reliedCl);
				if(reliedCr.getReliedCustomerName()!=null){
					String reliedBname=reliedCr.getReliedCustomerName();
					CustomerRegistry reliedB=crDAO.getCustomerRegistry(sr, reliedBname);
					NetworkCloudlet reliedBc=createNetworkCloudlet(reliedB);
					clist.add(reliedBc);
					netcl.stages.add(new TaskStage(NetworkConstants.EXECUTION, 0, 1000 * 0.8, 0, 1000, netcl.getVmId(), netcl
							.getCloudletId()));
					netcl.stages.add(new TaskStage(NetworkConstants.WAIT_SEND, 1000, 0, 1, 1000, reliedCl.getVmId(), reliedCl
							.getCloudletId()));
					reliedCl.stages.add(new TaskStage(NetworkConstants.WAIT_RECV, 1000, 0, 0, 1000, netcl.getVmId(), netcl
							.getCloudletId()));
					reliedCl.stages.add(new TaskStage(NetworkConstants.EXECUTION, 0, 1000 * 0.8, 0, 1000, reliedCl.getVmId(), reliedCl
							.getCloudletId()));
					reliedCl.stages.add(new TaskStage(NetworkConstants.WAIT_SEND, 1000,0, 1, 1000, reliedBc.getVmId(), reliedBc
							.getCloudletId()));	
					reliedBc.stages.add(new TaskStage(NetworkConstants.WAIT_RECV, 1000, 0, 0, 1000, reliedCl.getVmId(), reliedCl
							.getCloudletId()));
					reliedBc.stages.add(new TaskStage(NetworkConstants.EXECUTION, 0, 1000 * 0.8, 0, 1000, reliedBc.getVmId(), reliedBc
							.getCloudletId()));
					
				}
			
				
			}
			break;
			
		}
	
	}
	private NetworkCloudlet createNetworkCloudlet(CustomerRegistry cr){
				
		UtilizationProfile up=cr.getUtilizationProfile();
		long fileSize = up.getFileSize();
		long outputSize = up.getOutputSize();
		int memory = 1000;
		UtilizationModel utilizationModel = new UtilizationModelFull();							
		List<VirtualMachineRegistry> vmlist=cr.getVmList();
		int vmNumber=vmlist.size();			
		int vmid=(int)vmlist.get(rand.nextInt(vmNumber)).getId();			
		
		Map<Integer,Integer> vmIdMap=CloudsimConstants.vmIdMap.get((int)sr.getId());			
		int newVmid=vmIdMap.get(vmid);
		NetworkCloudlet cl = new NetworkCloudlet(
				NetworkConstants.currentCloudletId,
				0,
				1,
				fileSize,
				outputSize,
				memory,
				utilizationModel,
				utilizationModel,
				utilizationModel);
			cl.numStage = 2;
			NetworkConstants.currentCloudletId++;
			cl.setUserId(userId);
			cl.submittime = CloudSim.clock();
			cl.currStagenum = -1;
			cl.setVmId(newVmid);

		return cl;
	}
	
	
}