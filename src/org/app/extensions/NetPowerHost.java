package org.app.extensions;

import java.util.List;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.network.datacenter.NetworkHost;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

public class NetPowerHost extends NetworkHost{
	
	
	private PowerModel powerModel;
	
	private double utilizationMips;
	
	public NetPowerHost(int id, RamProvisioner ramProvisioner,
			BwProvisioner bwProvisioner, long storage,
			List<? extends Pe> peList, VmScheduler vmScheduler,PowerModel powerModel) {
		super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
		setPowerModel(powerModel);
		setUtilizationMips(0);
	}
	
	public double getPower(){
		return getPower(getUtilizationMips()/this.getTotalMips());
	}
	
	
	public double getPower(double utilization){
		double power=0;
		try {
			power=getPowerModel().getPower(utilization);
		} catch (IllegalArgumentException e) {			
			e.printStackTrace();
			System.exit(0);
		}
		return power;
	}
	


	@Override
	public double updateVmsProcessing(double currentTime) {
		double smallerTime = super.updateVmsProcessing(currentTime);
//		setPreviousUtilizationMips(getUtilizationMips());
		setUtilizationMips(0);
//		double hostTotalRequestedMips = 0;

		for (Vm vm : getVmList()) {
			getVmScheduler().deallocatePesForVm(vm);
		}

		for (Vm vm : getVmList()) {
			getVmScheduler().allocatePesForVm(vm, vm.getCurrentRequestedMips());
		}

		for (Vm vm : getVmList()) {
			double totalRequestedMips = vm.getCurrentRequestedTotalMips();
			double totalAllocatedMips = getVmScheduler().getTotalAllocatedMipsForVm(vm);

			if (!Log.isDisabled()) {
				Log.formatLine(
						"%.2f: [Host #" + getId() + "] Total allocated MIPS for VM #" + vm.getId()
								+ " (Host #" + vm.getHost().getId()
								+ ") is %.2f, was requested %.2f out of total %.2f (%.2f%%)",
						CloudSim.clock(),
						totalAllocatedMips,
						totalRequestedMips,
						vm.getMips(),
						totalRequestedMips / vm.getMips() * 100);

				List<Pe> pes = getVmScheduler().getPesAllocatedForVM(vm);
				StringBuilder pesString = new StringBuilder();
				for (Pe pe : pes) {
					pesString.append(String.format(" PE #" + pe.getId() + ": %.2f.", pe.getPeProvisioner()
							.getTotalAllocatedMipsForVm(vm)));
				}
				Log.formatLine(
						"%.2f: [Host #" + getId() + "] MIPS for VM #" + vm.getId() + " by PEs ("
								+ getNumberOfPes() + " * " + getVmScheduler().getPeCapacity() + ")."
								+ pesString,
						CloudSim.clock());
			}

			if (getVmsMigratingIn().contains(vm)) {
				Log.formatLine("%.2f: [Host #" + getId() + "] VM #" + vm.getId()
						+ " is being migrated to Host #" + getId(), CloudSim.clock());
			} else {
				if (totalAllocatedMips + 0.1 < totalRequestedMips) {
					Log.formatLine("%.2f: [Host #" + getId() + "] Under allocated MIPS for VM #" + vm.getId()
							+ ": %.2f", CloudSim.clock(), totalRequestedMips - totalAllocatedMips);
				}

				vm.addStateHistoryEntry(
						currentTime,
						totalAllocatedMips,
						totalRequestedMips,
						(vm.isInMigration() && !getVmsMigratingIn().contains(vm)));

				if (vm.isInMigration()) {
					Log.formatLine(
							"%.2f: [Host #" + getId() + "] VM #" + vm.getId() + " is in migration",
							CloudSim.clock());
					totalAllocatedMips /= 0.9; // performance degradation due to migration - 10%
				}
			}

			setUtilizationMips(getUtilizationMips() + totalAllocatedMips);
//			hostTotalRequestedMips += totalRequestedMips;
		}

//		addStateHistoryEntry(
//				currentTime,
//				getUtilizationMips(),
//				hostTotalRequestedMips,
//				(getUtilizationMips() > 0));

		return smallerTime;
	}
	
	public double getUtilizationOfCpuMips(){
		return getUtilizationMips();
	}
	public double getUtilizationOfRam(){
		return this.getRamProvisioner().getUsedRam();
	}
	public double getUtilizationOfBw(){
		return this.getBwProvisioner().getUsedBw();
	}
	public PowerModel getPowerModel() {
		return powerModel;
	}
	public void setPowerModel(PowerModel powerModel) {
		this.powerModel = powerModel;
	}
	public double getUtilizationMips() {
		return utilizationMips;
	}
	public void setUtilizationMips(double utilizationMips) {
		this.utilizationMips = utilizationMips;
	}

	public double getMaxPower() {		
		return getPower(1);
	}
	
}
