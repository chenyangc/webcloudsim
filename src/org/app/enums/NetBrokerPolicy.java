
package org.app.enums;

import org.app.extensions.ExtensionsLoader;
import org.app.extensions.NetAppDatacenterBroker;
import org.app.models.SimulationRegistry;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;



/**
 * Defines native types of broker policies and implements an extension type
 * to support user-implemented new types.
 * 
 * @see         org.app.extensions.brokers.Broker
 * @author      ChenYang
 * @since       1.0
 */
public enum NetBrokerPolicy implements Serializable {

  
	
    NET_APP {
        @Override
        public NetAppDatacenterBroker createBroker(SimulationRegistry sr,String customerName, String brokerAlias) {
            try{
                return new NetAppDatacenterBroker(sr,customerName);
            }
            catch(Exception e) {
                return null;
            }

        }
    },
    

    /** The extension type. 
     *  It is used for all user-implemented new types.
     */    
    EXTENSION {
        @Override
        public NetAppDatacenterBroker createBroker(SimulationRegistry sr,String customerName, String brokerAlias) {
            try{
                Class<?>[] types = new Class<?>[]{String.class};
                Object[] arguments = new Object[]{ customerName };
                return (NetAppDatacenterBroker) ExtensionsLoader.getExtension("Broker", brokerAlias, types, arguments);
            }
            catch(Exception e) {
                return null;
            }
        }
    };
    
    /** 
     * An abstract method to be implemented by every {@link BrokerPolicy}.
     *
     * @param   customerName    the name of the customer this broker policy relates to.
     * @param   brokerAlias     the alias of the broker policy.
     * @return                  a CloudSim's DatacenterBroker subtype.
     * @since                   1.0
     */      


	/** 
     * Gets an instance of broker policy based on its alias.
     *
     * @param   alias   the alias of the broker policy.
     * @return          an instance of the type with the given alias.
     * @since           1.0
     */      
    public static NetBrokerPolicy getInstance(String alias) {
        if(alias.equals("Net App")) return NetBrokerPolicy.NET_APP;
        else return NetBrokerPolicy.EXTENSION;
    }

    public NetAppDatacenterBroker createBroker(SimulationRegistry sr,
			String customerName, String brokerAlias) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
     * Gets all active broker policies aliases.
     *
     * @return  an array of strings containing all active broker
     *          policies aliases.
     * @since   1.0
     */      
    public static String[] getBrokerPoliciesNames() {
        String[] nativePolicies = new String[] {"Net App"};
        List<String> extensionPolicies = ExtensionsLoader.getExtensionsAliasesByType("Broker");        
        extensionPolicies.addAll(Arrays.asList(nativePolicies));
        
        return extensionPolicies.toArray(new String[0]);
    }
}
