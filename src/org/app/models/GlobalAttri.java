package org.app.models;

public class GlobalAttri {
    /** The id of the GlobalAttri. */
    private long id;
    
    /** The name of the GlobalAttri. */
    private String name;
    
    /** The value of the GlobalAttri. */
    private String value;

    /** The default constructor. */
    public GlobalAttri() {}

   
    public GlobalAttri(String name, String value) {
        this.name = name;
        this.value = value;
    }    
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }  
}
