

package org.app.utils;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.DatacenterRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.DatacenterRegistry;
import org.app.models.HostRegistry;
import org.app.models.SimulationRegistry;
import org.app.models.VirtualMachineRegistry;

import java.util.List;

/**
 * Provides utility methods that verifies the viability of simulations.
 * 
 * @author      Thiago T. Sá
 * @since       1.0
 */
public class Verification {
	
    /** 
     * Verifies the viability of a simulation.
     * 
     * @return  <code>true</code> if the simulation is practicable;
     *          <code>false</code> otherwise.
     * @since   1.0
     */       
    public static boolean verifyVMsDeploymentViability(SimulationRegistry sr) {
    	CustomerRegistryDAO crDAO=new CustomerRegistryDAO();
        for(CustomerRegistry cr : crDAO.getListOfCustomers(sr)) {

            List<DatacenterRegistry> dcList = getDatacentersList(sr);

            VM:
            for(VirtualMachineRegistry vmr : cr.getVmList()) {

                for(DatacenterRegistry dcr: dcList){
                    for(HostRegistry hr : dcr.getHostList()) {
                        if(hr.canRunVM(vmr)) continue VM;
                    }
                }

                return false;
            }
        }

        return true;
    }

    /** 
     * Gets a list of all simulated datacenters.
     * 
     * @return  a list of all simulated datacenters. 
     * @since   1.0
     */          
    private static List<DatacenterRegistry> getDatacentersList(SimulationRegistry sr) {
        DatacenterRegistryDAO drDAO = new DatacenterRegistryDAO();
        return drDAO.getListOfDatacenters(sr);
    }

}
