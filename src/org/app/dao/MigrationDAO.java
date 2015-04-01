/* 
 * Copyright (c) 2010-2012 Thiago T. Sá
 * 
 * This file is part of CloudReports.
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

package org.app.dao;

import org.app.models.Migration;
import org.app.models.Setting;
import org.app.models.SimulationRegistry;

import java.util.LinkedList;
import java.util.List;


/**
 * MigrationDAO provides basic CRUD operations related to the 
 * {@link Migration} class.
 * 
 * @see         Setting
 * @author      ChenYang
 * @since       1.0
 */
public class MigrationDAO {
	
    /** 
     * Registers a set of migration occurrences into the database.
     *
     * @param   migrationList   a list of migrations to be inserted.
     * @see                     Migration
     * @since                   1.0
     */    
    public void insertMigrations(SimulationRegistry sr,List<Migration> migrationList) {
 //   	SimulationRegistry sr=srDAO.getSimulationRegistry(simulationId);
    	List<Migration> migrations=sr.getMigrations();
    	migrations.addAll(migrationList);
    
    }
    
    /** 
     * Lists all migration occurrences for a given datacenter.
     *
     * @return                  a list containing all migration occurrences;
     *                          <code>null</code> if no occurrences were found.
     * @see                     Migration
     * @since                   1.0
     */    


	public List<Migration> getMigrationList(SimulationRegistry sr,String datacenterName) {
    	List<Migration> migrationList= new LinkedList<Migration>() ;;
//    	SimulationRegistry sr=srDAO.getSimulationRegistry(simulationId);
    	List<Migration> migrations=sr.getMigrations();
        for(Migration mg:migrations){
        	if(mg.getDatacenterName().equals(datacenterName)){
        		migrationList.add(mg);
        	}
        }


        return migrationList;
    }
}
