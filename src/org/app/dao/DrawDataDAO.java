package org.app.dao;

import java.util.List;

import org.app.models.DatacenterRegistry;
import org.app.models.DrawData;
import org.app.models.Setting;
import org.app.models.SimulationRegistry;

/**
 * DrawDataDAO provides basic CRUD operations related to the 
 * {@link DrawData} class.
 * It also provides access to general information about draw data.
 * 
 * @see         Setting
 * @author      ChenYang
 * @since       1.0
 */
public class DrawDataDAO {
	
    public void insertDrawData(SimulationRegistry sr,DrawData drawData) {
 //   	SimulationRegistry sr=srDAO.getSimulationRegistry(simulationId);
    	List<DrawData> drawDatas=sr.getDrawDatas();
//        for(DrawData dd:drawDatas) {
//            if(dd.getId()==drawData.getId())
//            	return ;
//        }
        drawDatas.add(drawData);
    	
    }
    
    /** 
     * Updates an existing DrawData.
     *
     * @param   DrawData     the DrawData to be updated.
     * @see                 DrawData
     * @since               1.0
     */       
    public void updateDrawData(SimulationRegistry sr,DrawData drawData) {
 //   	SimulationRegistry sr=srDAO.getSimulationRegistry(simulationId);
    	List<DrawData> drawDatas=sr.getDrawDatas();
        for(DrawData dd:drawDatas) {
            if(dd.getId()==drawData.getId())
            	dd=drawData;
            	break;
        }
    }

    /** 
     * Gets an existing DrawData.
     *
     * @param   name    the name of the DrawData to be retrieved.
     * @return          the DrawData with the given name, if it exists; 
     *                  <code>null</code> otherwise.
     * @see             DrawData
     * @since           1.0
     */      
    public DrawData getDrawData(SimulationRegistry sr,String name) {
        DrawData drawData = null;
//       	SimulationRegistry sr=srDAO.getSimulationRegistry(simulationId);
    	List<DrawData> drawDatas=sr.getDrawDatas();
        for(DrawData dd:drawDatas) {
            if(dd.getName().equals(name)){
            	drawData=dd;
            	break;
            }
            
        }

        return drawData;
    }
}
