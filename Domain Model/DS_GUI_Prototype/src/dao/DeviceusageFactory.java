/** 
 * This class was automatically generated  
 * using a Merode XML model and Apache Velocity
 * 
 * Merode Code Generator 2.0
 * @author MERODE Team-members
 */

package dao;

/**
 * Factory class.
 * Is able to find and create objects of type Deviceusage.
 *
 */
public abstract class DeviceusageFactory {

   // ---------------- create method --------------------
   
   /**
    * Creates a(n) Deviceusage object.
    *
    * @return Deviceusage the created object
    */
    public static Deviceusage create(org.hibernate.Session session) throws org.hibernate.HibernateException {
        Deviceusage object = new DeviceusageImpl();
        DeviceusageState.setInitialState(session, object);
        return object;
    }


    // ---------------- finder methods  ----------------------

    /**
     *
     * Finds Deviceusage object by its primary key.
     * In Hibernate, this is just a call to load().
     *
     */
    public static Deviceusage findByPrimaryKey (org.hibernate.Session sess, java.lang.String id)
        throws org.hibernate.HibernateException {
        Deviceusage object = (Deviceusage) sess.load(DeviceusageImpl.class, id);
        return object;
    }

    public static java.util.Collection getAllObjects (org.hibernate.Session sess)
        throws org.hibernate.HibernateException {
    
        org.hibernate.Query q = sess.createQuery("from dao.Deviceusage");
        return q.list();
    }

    /**
     *
     * Finds Deviceusage object(s) using a query.
     *
     */
    public static java.util.Collection findByUsagetype (org.hibernate.Session sess, java.lang.String Usagetype)
        throws org.hibernate.HibernateException {
    
        org.hibernate.Query q = sess.createQuery("from dao.Deviceusage as c where c.Usagetype = ?");
    	q.setString (0, Usagetype);
        return q.list();
    }
    /**
     *
     * Finds Deviceusage object(s) using a query.
     *
     */
    public static java.util.Collection findByStarttime (org.hibernate.Session sess, java.lang.String Starttime)
        throws org.hibernate.HibernateException {
    
        org.hibernate.Query q = sess.createQuery("from dao.Deviceusage as c where c.Starttime = ?");
    	q.setString (0, Starttime);
        return q.list();
    }
    /**
     *
     * Finds Deviceusage object(s) using a query.
     *
     */
    public static java.util.Collection findByEndtime (org.hibernate.Session sess, java.lang.String Endtime)
        throws org.hibernate.HibernateException {
    
        org.hibernate.Query q = sess.createQuery("from dao.Deviceusage as c where c.Endtime = ?");
    	q.setString (0, Endtime);
        return q.list();
    }
}
