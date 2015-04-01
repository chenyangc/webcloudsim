package org.app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;





public class SimulationRegistry implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String name;
	
	private List<CustomerRegistry> customerList;
	
	private List<DatacenterRegistry> datacenterList;
	
	private List<DrawData> drawDatas;
		
	private List<Migration> migrations;
	
	private List<NetworkMapEntry> networkMapEntrys;
	
	private List<RandomNumber> randomNumbers;
	
	private Set<ReportData> reportDatas;
	
	private List<Setting> settings;
				
	public SimulationRegistry(){
		
	}
	
	
	public SimulationRegistry(String name){
		this.name=name;
		this.customerList = new LinkedList<CustomerRegistry>();
		this.datacenterList = new LinkedList<DatacenterRegistry>();
		this.drawDatas = new LinkedList<DrawData>();
		this.settings=new LinkedList<Setting>();
		
		this.migrations = new LinkedList<Migration>();
//		this.migrations.add(new Migration());
		
		this.networkMapEntrys = new LinkedList<NetworkMapEntry>();
//		this.networkMapEntrys.add(new NetworkMapEntry());
		
		this.randomNumbers = new LinkedList<RandomNumber>();
	//	this.randomNumbers.add(new RandomNumber());
		
		this.reportDatas = new HashSet<ReportData>();
//		this.reportDatas.add(new ReportData());
		
	
	
	}

	public long getId() {
		return id;
	}

	

	public List<DrawData> getDrawDatas() {
		return drawDatas;
	}

	public List<Migration> getMigrations() {
		return migrations;
	}

	public List<NetworkMapEntry> getNetworkMapEntrys() {
		return networkMapEntrys;
	}

	public List<RandomNumber> getRandomNumbers() {
		return randomNumbers;
	}


	public void setId(long id) {
		this.id = id;
	}

	public void setDrawDatas(List<DrawData> drawDatas) {
		this.drawDatas = drawDatas;
	}

	public void setMigrations(List<Migration> migrations) {
		this.migrations = migrations;
	}

	public void setNetworkMapEntrys(List<NetworkMapEntry> networkMapEntrys) {
		this.networkMapEntrys = networkMapEntrys;
	}

	public void setRandomNumbers(List<RandomNumber> randomNumbers) {
		this.randomNumbers = randomNumbers;
	}
	

	public Set<ReportData> getReportDatas() {
		return reportDatas;
	}


	public void setReportDatas(Set<ReportData> reportDatas) {
		this.reportDatas = reportDatas;
	}


	public List<CustomerRegistry> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<CustomerRegistry> customerList) {
		this.customerList = customerList;
	}

	public List<DatacenterRegistry> getDatacenterList() {
		return datacenterList;
	}

	public void setDatacenterList(List<DatacenterRegistry> datacenterList) {
		this.datacenterList = datacenterList;
	}
	
	

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public List<Setting> getSettings() {
		return settings;
	}


	public void setSettings(List<Setting> settings) {
		this.settings = settings;
	}


	@Override
    public boolean equals(Object simulationRegistry){
      if ( this.equals(simulationRegistry)) return true;
      if ( !(simulationRegistry instanceof SimulationRegistry) ) return false;
      SimulationRegistry sr = (SimulationRegistry)simulationRegistry;
      return this.getId()==sr.getId();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    } 

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("id="+this.getId()+" \n");
        s.append("\n++Beginning of customer list++ size=:"+this.getCustomerList().size()+"\n");
        for(CustomerRegistry cr :this.getCustomerList() ) {
            s.append("\n"+cr.toString());
        }
        s.append("\n++End of customer list++\n");

        s.append("\n++Beginning of datacenter list++size=:"+this.getDatacenterList().size()+"\n");
        for(DatacenterRegistry dr : this.getDatacenterList()) {
            s.append("\n"+dr.toString());
        }
        s.append("\n++End of datacenter list++\n");
        
        return s.toString();
    }
	
}
