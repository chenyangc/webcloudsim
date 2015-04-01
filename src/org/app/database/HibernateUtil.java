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

package org.app.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


/**
 * Provides utility operations related to the Hibernate framework.
 * 
 * @author      ChenYang
 * @since       1.0
 */
public class HibernateUtil {

    /** A session factory. It provides database sessions to DAO methods. */
    private static SessionFactory sessionFactory;
    
    
    public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtil.sessionFactory = sessionFactory;
	}


    /** 
     * Provides an opened database session.
     *
     * @see     #sessionFactory
     * @return  a database session.
     * @since   1.0
     */        
    public static Session getSession() {
   //     return sessionFactory.openSession();
    	return sessionFactory.openSession();
    }
    
    /** 
     * Closes a given database session.
     *
     * @param   session     the session to be closed.
     * @since               1.0
     */     
    public static void closeSession(Session session) {
        if(session != null) {
            session.clear();
            session.close();
        }
    }

    /** 
     * Closes the session factory.
     *
     * @see     #sessionFactory
     * @since   1.0
     */      
    public static void shutDown() {
        sessionFactory.close();
    }
      
   

}