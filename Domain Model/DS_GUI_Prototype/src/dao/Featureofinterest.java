/** 
 * This class was automatically generated  
 * using a Merode XML model and Apache Velocity
 * 
 * Merode Code Generator 2.0
 * @author MERODE Team-members
 */

package dao;

import java.util.HashSet;
import java.util.Iterator;



/**
 * 
 *
 * @hibernate.class
 *     table="HIB_FEATUREOFINTEREST"
 * @hibernate.discriminator
 *     column="class"
 * 
 *
 */

public abstract class Featureofinterest 
    implements java.io.Serializable {
    
    // --------------- state --------------------------

    /**
     * @hibernate.many-to-one
     *     column="FEATUREOFINTEREST_STATE_FK"
     *     class="dao.FeatureofinterestState"
     */
    public FeatureofinterestState getState () {
        return this.state;
    }
    
    public void setState (FeatureofinterestState state){
        this.state = state;
    }

    protected FeatureofinterestState state;

    // --------------- attributes ---------------------
    private java.lang.String name;
    /**
     * 
     *
     * @hibernate.property
     *     column="NAME"
     *     type="java.lang.String"
     *
     * @hibernate.column
     *     name="NAME"
     *     sql-type="VARCHAR(256)"
     */
    public java.lang.String getName(){
        return this.name;
    }

    public void setName(java.lang.String name){
        this.name = name;
    }
    private java.lang.String description;
    /**
     * 
     *
     * @hibernate.property
     *     column="DESCRIPTION"
     *     type="java.lang.String"
     *
     * @hibernate.column
     *     name="DESCRIPTION"
     *     sql-type="VARCHAR(256)"
     */
    public java.lang.String getDescription(){
        return this.description;
    }

    public void setDescription(java.lang.String description){
        this.description = description;
    }
    private java.lang.String id;

    /**
     * 
     *
     * @hibernate.id
     *     generator-class="uuid.hex"
     *     column="ID"
     *     type="java.lang.String"
     *
     * @hibernate.column
     *     name="ID"
     *     sql-type="VARCHAR(256)"
     */
    public java.lang.String getId()
    {
        return this.id;
    }

    public void setId(java.lang.String id){
        this.id = id;
    }


    // ------------- relations ------------------

/**
     * 
     *
     * @hibernate.set
     *     role="platformdeployment"
     *     lazy="false"
     * @hibernate.collection-key
     *     column="FEATUREOFINTEREST_FK"
     * @hibernate.collection-one-to-many
     *     class="dao.Platformdeployment"
     */
    public java.util.Collection getPlatformdeployment(){
        return this.platformdeployment;
    }

    protected void setPlatformdeployment(java.util.Collection platformdeployment){
        this.platformdeployment = platformdeployment;
    }

    private java.util.Collection platformdeployment;
    public void attachPlatformdeployment (dao.Platformdeployment object) {
        this.platformdeployment.add(object);
    }
	/**
     * 
     *
     * @hibernate.set
     *     role="property"
     *     lazy="false"
     * @hibernate.collection-key
     *     column="FEATUREOFINTEREST_FK"
     * @hibernate.collection-one-to-many
     *     class="dao.Property"
     */
    public java.util.Collection getProperty(){
        return this.property;
    }

    protected void setProperty(java.util.Collection property){
        this.property = property;
    }

    private java.util.Collection property;
    public void attachProperty (dao.Property object) {
        this.property.add(object);
    }
	// ---------- precondition of business methods  -----------
	// --- o/c ---
    public abstract void check_mecrfeatureofinterest() throws MerodeException;
	// --- o/e --- 
    public abstract void check_meendfeatureofinterest() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_mecrplatformdeployment() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_meendplatformdeployment() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_mecrdeviceresult() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_meenddeviceresult() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_mecrproperty() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_meendproperty() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_mecrdeviceusage() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_meenddeviceusage() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_deviceundeployment() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_devicedeployment() throws MerodeException;
	// --- o/dpnds --- 
    public abstract void check_mesetready() throws MerodeException;

    // ---------------- business methods  ----------------------


	/**
     *  --- o/c --- 
     */
	public abstract void mecrfeatureofinterest( java.lang.String Name,
		java.lang.String Description)
    	throws MerodeException;


	
/**
     *  --- o/e ---
     */
    public abstract void meendfeatureofinterest()
        throws MerodeException;
	
		
   /**
    * --- o/dpnds ---
    */
    public abstract void mecrplatformdeployment()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void meendplatformdeployment()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void mecrdeviceresult()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void meenddeviceresult()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void mecrproperty()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void meendproperty()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void mecrdeviceusage()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void meenddeviceusage()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void deviceundeployment()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void devicedeployment()
        throws MerodeException;	

		
   /**
    * --- o/dpnds ---
    */
    public abstract void mesetready()
        throws MerodeException;	

	
    /**
     * checking if an object is consistent 
     * against mandatory relationship(s)
     */
    public String getMandatoryInconsistency() {
		java.util.Set<String> inconsistentDpnds = new HashSet();
		String commaSeparated = "";
		Iterator it;
		//boolean isConsistent = false;

		for (String s : inconsistentDpnds){
			commaSeparated += 
				"".equals(commaSeparated) ?
					s : "," + s ;
		}

		return commaSeparated;
	}

}
