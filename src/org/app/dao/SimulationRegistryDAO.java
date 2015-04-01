package org.app.dao;


import org.app.database.HibernateUtil;
import org.app.models.Setting;
import org.app.models.SimulationRegistry;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * SimulationRegistryDAO provides basic CRUD operations related to the 
 * {@link SimulationRegistry} class.
 * 
 * @see         Setting
 * @author      ChenYang
 * @since       1.0
 */

public class SimulationRegistryDAO {
	
    public void insertSimulationRegistry(SimulationRegistry SimulationRegistry) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.save(SimulationRegistry);
            session.getTransaction().commit();
       
            
        }
        catch (HibernateException ex) {
            session.getTransaction().rollback();
          
        } finally {
            HibernateUtil.closeSession(session);
        }
    }
    
       
    public void updateSimulationRegistry(SimulationRegistry SimulationRegistry) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.update(SimulationRegistry);
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            session.getTransaction().rollback();
          
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    
    public SimulationRegistry getSimulationRegistry(long id) {
        SimulationRegistry SimulationRegistry = null;
        Session session = HibernateUtil.getSession();
        try {
            SimulationRegistry = (SimulationRegistry) session.createCriteria(SimulationRegistry.class)
                                       .add(Restrictions.eq("id", id))
                                       .uniqueResult();
        }
        catch (HibernateException ex) {
 //           Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HibernateUtil.closeSession(session);
        }
 
        return SimulationRegistry;
    }
    
    public SimulationRegistry getSimulationRegistry(String name) {
        SimulationRegistry SimulationRegistry = null;
        Session session = HibernateUtil.getSession();
        try {
            SimulationRegistry = (SimulationRegistry) session.createCriteria(SimulationRegistry.class)
                                       .add(Restrictions.eq("name", name))
                                       .uniqueResult();
        }
        catch (HibernateException ex) {
          
        } finally {
            HibernateUtil.closeSession(session);
        }

        return SimulationRegistry;
    }
}
