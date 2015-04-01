package org.app.dao;


import org.app.database.HibernateUtil;
import org.app.models.DrawData;
import org.app.models.GlobalAttri;
import org.app.models.Setting;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


/**
 * GlobalAttriDAO provides basic CRUD operations related to the 
 * {@link GlobalAttri} class.
 * It also provides access to general information about global attributes.
 * 
 * @see         Setting
 * @author      ChenYang
 * @since       1.0
 */

public class GlobalAttriDAO {
    public void insertGlobalAttri(GlobalAttri globalAttri) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.save(globalAttri);
            session.getTransaction().commit();
        }
        catch (HibernateException ex) {
            session.getTransaction().rollback();
 //           Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HibernateUtil.closeSession(session);
        }
    }
    
       
    public void updateGlobalAttri(GlobalAttri globalAttri) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.update(globalAttri);
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            session.getTransaction().rollback();
 //           Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    
    public GlobalAttri getGlobalAttri(String name) {
        GlobalAttri globalAttri = null;
        Session session = HibernateUtil.getSession();
        try {
            globalAttri = (GlobalAttri) session.createCriteria(GlobalAttri.class)
                                       .add(Restrictions.eq("name", name))
                                       .uniqueResult();
        }
        catch (HibernateException ex) {
 //           Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HibernateUtil.closeSession(session);
        }

        return globalAttri;
    }
}
