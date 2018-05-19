package nodomain.knu2018.gadgetbridge.entities;

import java.util.List;
import nodomain.knu2018.gadgetbridge.entities.DaoSession;
import de.greenrobot.dao.DaoException;

import de.greenrobot.dao.AbstractDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DEVICE".
 */
public class Device {

    private Long id;
    /** Not-null value. */
    private String name;
    /** Not-null value. */
    private String manufacturer;
    /** Not-null value. */
    private String identifier;
    private int type;
    private String model;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient DeviceDao myDao;

    private List<DeviceAttributes> deviceAttributesList;

    public Device() {
    }

    public Device(Long id) {
        this.id = id;
    }

    public Device(Long id, String name, String manufacturer, String identifier, int type, String model) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.identifier = identifier;
        this.type = type;
        this.model = model;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDeviceDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    /** Not-null value. */
    public String getManufacturer() {
        return manufacturer;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /** Not-null value. */
    /**
     * The fixed identifier, i.e. MAC address of the device.
     */
    public String getIdentifier() {
        return identifier;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    /**
     * The fixed identifier, i.e. MAC address of the device.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * The DeviceType key, i.e. the GBDevice's type.
     */
    public int getType() {
        return type;
    }

    /**
     * The DeviceType key, i.e. the GBDevice's type.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * An optional model, further specifying the kind of device-
     */
    public String getModel() {
        return model;
    }

    /**
     * An optional model, further specifying the kind of device-
     */
    public void setModel(String model) {
        this.model = model;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<DeviceAttributes> getDeviceAttributesList() {
        if (deviceAttributesList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DeviceAttributesDao targetDao = daoSession.getDeviceAttributesDao();
            List<DeviceAttributes> deviceAttributesListNew = targetDao._queryDevice_DeviceAttributesList(id);
            synchronized (this) {
                if(deviceAttributesList == null) {
                    deviceAttributesList = deviceAttributesListNew;
                }
            }
        }
        return deviceAttributesList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetDeviceAttributesList() {
        deviceAttributesList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
