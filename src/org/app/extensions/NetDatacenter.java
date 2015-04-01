package org.app.extensions;

import java.util.List;

import org.app.extensions.VmAllocationPolicyExtensible;
import org.app.models.Migration;
import org.app.simulation.NewSimulation;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.predicates.PredicateType;
import org.cloudbus.cloudsim.network.datacenter.NetworkDatacenter;
import org.app.extensions.NetPowerHost;

/**
 * 
 *
 * 
 * @author      ChenYang
 * @since       1.0
 */
public class NetDatacenter extends NetworkDatacenter {
    /** The period between data collections. */
	private double monitoringInterval;
    
    /** The last time data was collected. */
	private double lastMonitoringTime;
	
	private double cloudletSubmitted;
    
    public NetDatacenter(String name, DatacenterCharacteristics characteristics,
            VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList,
            double schedulingInterval, double monitoringInterval) throws Exception {

    	super(name,characteristics,vmAllocationPolicy,storageList, schedulingInterval);
    	this.monitoringInterval = monitoringInterval;
    	this.lastMonitoringTime = 0;
    }

/** 
* An overridden version of the method that adds data collection 
* functionalities.
*
* @since                   1.0
*/       
    @Override
    protected void updateCloudletProcessing() {
    	if (getCloudletSubmitted() == -1 || getCloudletSubmitted() == CloudSim.clock()) {
    		CloudSim.cancelAll(getId(), new PredicateType(CloudSimTags.VM_DATACENTER_EVENT));
    		schedule(getId(), getSchedulingInterval(), CloudSimTags.VM_DATACENTER_EVENT);
    		return;
    	}
    	double currentTime = CloudSim.clock();
    	double timeframePower = 0.0;

    	// if some time passed since last processing
    	if (currentTime > getLastProcessTime()) {
    		double timeDiff = currentTime - getLastProcessTime();
    		double minTime = Double.MAX_VALUE;
    	

    		for (NetPowerHost host : this.<NetPowerHost>getHostList()) {
    			double hostPower = 0.0;
    			if (host.getUtilizationOfCpuMips() > 0) {
    				try {
    					hostPower = host.getPower() * timeDiff;
    					timeframePower += hostPower;
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    		}

    		for (NetPowerHost host : this.<NetPowerHost>getHostList()) {
    			double time = host.updateVmsProcessing(currentTime); // inform VMs to update processing
    			if (time < minTime) {
    				minTime = time;
    			}
    		}    		

    		if((currentTime - this.lastMonitoringTime) >= this.getMonitoringInterval()) {
    			this.lastMonitoringTime = currentTime;   		
    			//Collect monitored used resources
    			NewSimulation.getDataCollector().collectMonitoredUsedResources();
    		}

    		// schedules an event to the next time
    		if (minTime != Double.MAX_VALUE) {
    			CloudSim.cancelAll(getId(), new PredicateType(CloudSimTags.VM_DATACENTER_EVENT));
    			send(getId(), getSchedulingInterval(), CloudSimTags.VM_DATACENTER_EVENT);
    		}

    		setLastProcessTime(currentTime);
    		NewSimulation.getDataCollector().collectData();
    	}
    }



/**
* Gets the monitoring interval of this datacenter.
*
* @return the period between data collections.
*/ 
    public double getMonitoringInterval() {
    	return monitoringInterval;
    }
	/**
	 * Checks if is cloudlet submited.
	 * 
	 * @return true, if is cloudlet submited
	 */
	protected double getCloudletSubmitted() {
		return cloudletSubmitted;
	}

	/**
	 * Sets the cloudlet submited.
	 * 
	 * @param cloudletSubmitted the new cloudlet submited
	 */
	protected void setCloudletSubmitted(double cloudletSubmitted) {
		this.cloudletSubmitted = cloudletSubmitted;
	}
    
}
