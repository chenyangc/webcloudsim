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

package org.app.reports;


import org.app.dao.MigrationDAO;
import org.app.dao.ReportDataDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.Migration;
import org.app.models.ReportData;
import org.app.models.SimulationRegistry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.network.datacenter.NetDatacenterBroker;
import org.app.extensions.NetPowerHost;



/**
 * Provides methods to collect simulation data.
 * 
 * @author      Thiago T. Sá,chenyang
 * @since       1.0
 */
public class NetDataCollector  {
    
	DecimalFormat df ;
    /** The label used for the bandwidth resources. */
    private static final String BANDWIDTH = "BANDWIDTH";
    
    /** The label used for the CPU resources. */
    private static final String CPU = "CPU";
    
    /** The label used for the power resources. */
    private static final String POWER = "POWER";    
    
    /** The label used for the RAM resources. */
    private static final String RAM = "RAM";
    
    /** The number of samples to be considered when dealing with monitored
     *  resources.
     */
    private static final int SAMPLES = 2;

    /** A list of transient report data. */
    private Set<ReportData> dataList;
    
    /** A map of datacenters. */
    private HashMap<String,Datacenter> datacenters;
    
    /** A map of brokers. */
    private HashMap<String,NetDatacenterBroker> brokers;
    
    /** A list of monitored resources. */
    private List<ReportData> monitoredUsedResources;
    
    /** The last moment data was collected. */
    private double lastClock;
    
    private SimulationRegistry sr;

    /** 
     * Creates an instance of a data collector.
     * 
     * @param   datacenters a map of datacenters to keep track of.
     * @param   brokers     a map of brokers to keep track of.NetAppDatacenterBroker
     * @since               1.0
     */     
    public NetDataCollector(SimulationRegistry sr,HashMap<String,Datacenter> datacenters, HashMap<String,NetDatacenterBroker> brokers) {
        
//        Database.cleanTempReport();
        this.datacenters = datacenters;
        this.brokers = brokers;
        this.monitoredUsedResources = new ArrayList<ReportData>();
        this.dataList = new HashSet<ReportData>();
        this.sr=sr;
        insertClearedData(0);
        this.df= new DecimalFormat("#.00");
    }
    
    /**
     * Gets the last moment data was collected.
     * 
     * @return  the last moment data was collected.
     */
    public double getLastClock() {
        return lastClock;
    }
    
    /**
     * Collects data from virtual machines and hosts and adds them to a
     * transient list.
     * If the transient list's size gets greater than 1000, the data is flushed
     * to the database.
     * 
     * @see     #flushData()
     * @since   1.0
     */    
    public void collectData() {
        collectVmsData();
        collectHostsData();
        if(dataList.size() > 100) {
          
        	flushData();            
        }
    }

    /**
     * Collects data from virtual machines and adds them to a transient list.
     * 
     * @since   1.0
     */     
    private void collectVmsData() {
        List<String> brokerNames = Arrays.asList(brokers.keySet().toArray(new String[0]));
        double currentTime = CloudSim.clock();
        this.lastClock = currentTime;

        for(String brokerName : brokerNames) {
            NetDatacenterBroker broker = brokers.get(brokerName);
            double overallRam = 0,
                   overallCpu = 0,
                   overallBandwidth = 0;
            int currentSimulation = (int)sr.getId();
            
            List<Vm> vmsList = broker.getVmList();
            
  
            for(Vm vm : vmsList) {
            	
            	double cpuUtilization=0;
            	 double ramUtilization =0;
            	 double bwUtilization=0;
                int vmId = vm.getId();
                if(vm.getCloudletScheduler().runningCloudlets()==0){
                	cpuUtilization=0;
                	ramUtilization=0;
                	bwUtilization=0;
                }
                else{
                	cpuUtilization=vm.getCloudletScheduler().runningCloudlets()*25;                	
                	ramUtilization=vm.getCloudletScheduler().runningCloudlets()*20;  
                	bwUtilization=vm.getCloudletScheduler().runningCloudlets()*15;  
                }
                

  //              System.out.println("datalist size:"+dataList.size());
//                double ramUtilization = (vm.getCurrentAllocatedRam()/vm.getRam())*100;
                dataList.add(new ReportData(RAM, null, brokerName, null, vmId, currentTime, ramUtilization, currentSimulation));
                overallRam += ramUtilization;
                

                
//                double cpuUtilization = (vm.getCurrentRequestedTotalMips()/vm.getMips())*100;
               
                dataList.add(new ReportData(CPU, null, brokerName, null, vmId, currentTime, cpuUtilization, currentSimulation));
                overallCpu += cpuUtilization;
                
//                double bwUtilization = (vm.getCurrentAllocatedBw()/vm.getBw())*100;
                dataList.add(new ReportData(BANDWIDTH, null, brokerName, null, vmId, currentTime, bwUtilization, currentSimulation));
                overallBandwidth += bwUtilization;
            }
            
            int numOfVms = vmsList.size();
            dataList.add(new ReportData(RAM, null, brokerName, currentTime, overallRam/numOfVms, currentSimulation));            
            dataList.add(new ReportData(CPU, null, brokerName, currentTime, overallCpu/numOfVms, currentSimulation));            
            dataList.add(new ReportData(BANDWIDTH, null, brokerName, currentTime, overallBandwidth/numOfVms, currentSimulation));
        }
        
    }

    /**
     * Collects data from hosts and adds them to a transient list.
     * 
     * @since   1.0
     */         
    private void collectHostsData() {
        List<String> datacenterNames = Arrays.asList(datacenters.keySet().toArray(new String[0]));
        double currentTime = CloudSim.clock();
        this.lastClock = currentTime;
        for(String datacenterName : datacenterNames) {
            Datacenter datacenter = datacenters.get(datacenterName);
            double overallRam = 0,
                   overallCpu = 0,
                   overallBandwidth = 0,
                   overallPower = 0;   
            int currentSimulation = (int)sr.getId();
            
            List<NetPowerHost> hostsList = datacenter.getHostList();
            for(NetPowerHost host : hostsList) {
           
            	
                int hostId =host.getId();
                
                double ramUtilization = ( host.getUtilizationOfRam()/host.getRam())*100;
                dataList.add(new ReportData(RAM, datacenterName, null, hostId, null, currentTime, ramUtilization, currentSimulation));
                overallRam += ramUtilization;
                
                double cpuUtilization = (host.getUtilizationOfCpuMips()/host.getTotalMips())*100;
                dataList.add(new ReportData(CPU, datacenterName, null, hostId, null, currentTime, cpuUtilization, currentSimulation));
                overallCpu += cpuUtilization;
                
                double bwUtilization = (host.getUtilizationOfBw()/host.getBw())*100;
                dataList.add(new ReportData(BANDWIDTH, datacenterName, null, hostId, null, currentTime, bwUtilization, currentSimulation));
                overallBandwidth += bwUtilization;
    //            System.out.println("!!!!!band"+bwUtilization+ " over  "+overallBandwidth+"bw"+host.getUtilizationOfBw());
                
                double powerUtilization = (host.getPower()/host.getMaxPower())*100;
                dataList.add(new ReportData(POWER, datacenterName, null, hostId, null, currentTime, powerUtilization, currentSimulation));
                overallPower += powerUtilization;
            }
            
            int numOfHosts = hostsList.size();
            
            dataList.add(new ReportData(RAM, datacenterName, null, currentTime, overallRam/numOfHosts, currentSimulation));            
            dataList.add(new ReportData(CPU, datacenterName, null, currentTime, overallCpu/numOfHosts, currentSimulation));            
            dataList.add(new ReportData(BANDWIDTH, datacenterName, null, currentTime, overallBandwidth/numOfHosts, currentSimulation));            
            dataList.add(new ReportData(POWER, datacenterName, null, currentTime, Double.parseDouble(df.format(overallPower/numOfHosts)), currentSimulation));
        }
    }
    
    /**
     * Inserts cleared data into a report.
     * 
     * @param   time    the simulation time at which the cleared data must be
     *                  inserted.
     * @since   1.0
     */       
    public void insertClearedData(double time) {
        //Create cleared data for virtual machines
        List<String> brokerNames = Arrays.asList(brokers.keySet().toArray(new String[0]));
        ReportDataDAO rdDAO = new ReportDataDAO();
        double currentTime = time;
        int currentSimulation = (int)sr.getId();

        for (String brokerName : brokerNames) {
            NetDatacenterBroker broker = brokers.get(brokerName);

            List<Vm> vmsList = broker.getVmList();
 //��¼ÿ��������ʼ����Դ������
            for (Vm vm : vmsList) {
                int vmId = vm.getId();

                rdDAO.insertVmData(sr,RAM, brokerName, vmId, currentTime, 0, currentSimulation);
                rdDAO.insertVmData(sr,CPU, brokerName, vmId, currentTime, 0, currentSimulation);
                rdDAO.insertVmData(sr,BANDWIDTH, brokerName, vmId, currentTime, 0, currentSimulation);
            }
//��¼ȫ��������ʼ����Դ������
            rdDAO.insertCustomerOverallData(sr,RAM, brokerName, currentTime, 0, currentSimulation);
            rdDAO.insertCustomerOverallData(sr,CPU, brokerName, currentTime, 0, currentSimulation);
            rdDAO.insertCustomerOverallData(sr,BANDWIDTH, brokerName, currentTime, 0, currentSimulation);
        }
        
        //Create cleared data for hosts
        List<String> datacenterNames = Arrays.asList(datacenters.keySet().toArray(new String[0]));
        for (String datacenterName : datacenterNames) {
            Datacenter datacenter = datacenters.get(datacenterName);

            List<Host> hostsList = datacenter.getHostList();
            for (Host host : hostsList) {
                int hostId = host.getId();

                rdDAO.insertHostData(sr,RAM, datacenterName, hostId, currentTime, 0, currentSimulation);
                rdDAO.insertHostData(sr,CPU, datacenterName, hostId, currentTime, 0, currentSimulation);
                rdDAO.insertHostData(sr,BANDWIDTH, datacenterName, hostId, currentTime, 0, currentSimulation);
                rdDAO.insertHostData(sr,POWER, datacenterName, hostId, currentTime, 0, currentSimulation);
            }

            rdDAO.insertDatacenterOverallData(sr,RAM, datacenterName, currentTime, 0, currentSimulation);
            rdDAO.insertDatacenterOverallData(sr,CPU, datacenterName, currentTime, 0, currentSimulation);
            rdDAO.insertDatacenterOverallData(sr,BANDWIDTH, datacenterName, currentTime, 0, currentSimulation);
            rdDAO.insertDatacenterOverallData(sr,POWER, datacenterName, currentTime, 0, currentSimulation);
        }
    }

    /**
     * Inserts migration data into the database.
     * 
     * @param   migrationList   the list of migration data to be inserted.
     * @since   1.0
     */         
    public void flushMigrations(List<Migration> migrationList) {
//    	SimulationRegistryDAO ssrDAO=new SimulationRegistryDAO();
//    	 GlobalAttriDAO gaDAO=new GlobalAttriDAO();

//    	 SimulationRegistry sr=ssrDAO.getSimulationRegistry(Long.valueOf(gaDAO.getGlobalAttri("nowSimulationId").getValue()));
    	
    	 MigrationDAO mDAO = new MigrationDAO();
        mDAO.insertMigrations(sr,migrationList);
    }
    
    /**
     * Inserts report data from {@link #dataList} into the database.
     *  
     * @since   1.0
     */       
    public void flushData() {
        if(!this.dataList.isEmpty()) {
            ReportDataDAO rDAO = new ReportDataDAO();
            rDAO.insertDataSet(sr,dataList);
            this.dataList.clear();

       }
    }
    
    /**
     * Collects monitored resources.
     * 
     * @see     DatacenterRegistry#monitoringInterval
     * @since   1.0
     */       
    public void collectMonitoredUsedResources() {
        List<String> datacenterNames = Arrays.asList(datacenters.keySet().toArray(new String[0]));
        double currentTime = CloudSim.clock();
        for (String datacenterName : datacenterNames) {
            Datacenter datacenter = datacenters.get(datacenterName);
            List<NetPowerHost> hostsList = datacenter.getHostList();
            for (NetPowerHost host : hostsList) {
                int hostId = host.getId();

                double ramUtilization = (host.getUtilizationOfRam() / host.getRam()) * 100;
                insertMonitoredUsedResources(new ReportData(RAM, datacenterName, null, hostId, null, currentTime, ramUtilization, 0));

                double cpuUtilization = (host.getUtilizationOfCpuMips() / host.getTotalMips()) * 100;
                insertMonitoredUsedResources(new ReportData(CPU, datacenterName, null, hostId, null, currentTime, cpuUtilization, 0));
            }
        }
    }
    
    /**
     * Inserts monitored data into {@link #monitoredUsedResources}.
     * 
     * @param   data    the data to be inserted.
     * @since   1.0
     */         
    private void insertMonitoredUsedResources(ReportData data) {
        List<ReportData> sampleList = new ArrayList<ReportData>();
        for(ReportData sample : this.monitoredUsedResources) {
            if(sample.getDatacenterName().equals(data.getDatacenterName()) &&
               sample.getHostId().equals(data.getHostId()) &&
               sample.getType().equals(data.getType())) {
                sampleList.add(sample);
            }
        }
        
        if(sampleList.size() >= SAMPLES) {
            ReportData olderSample = sampleList.get(0);
            for(ReportData sample : sampleList) {
                if(sample.getTime() < olderSample.getTime()) {
                    olderSample = sample;
                }
            }
            
            this.monitoredUsedResources.remove(olderSample);
        }
        
        this.monitoredUsedResources.add(data);
    }
    
    /**
     * Gets a list of monitored resources.
     * 
     * @param   datacenterName  the name of the monitored datacenter.
     * @param   hostId          the id of the monitored host.
     * @param   type            the type of the monitored resource.
     * @since   1.0
     */       
    public List<Double> getMonitoredUsedResources(String datacenterName, int hostId, String type) {
        List<Double> valuesList = new ArrayList<Double>();
        for (ReportData sample : this.monitoredUsedResources) {
            if (sample.getDatacenterName().equals(datacenterName)
                    && sample.getHostId().equals(hostId)
                    && sample.getType().equals(type)) {
                valuesList.add(sample.getAmount());
            }
        }
        
        return valuesList;
    }    
}
