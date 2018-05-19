package nodomain.knu2018.gadgetbridge.entities;

import nodomain.knu2018.gadgetbridge.entities.DaoSession;
import de.greenrobot.dao.DaoException;

import de.greenrobot.dao.AbstractDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "CALENDAR_SYNC_STATE".
 */
public class CalendarSyncState {

    private Long id;
    private long deviceId;
    private long calendarEntryId;
    private int hash;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient CalendarSyncStateDao myDao;

    private Device device;
    private Long device__resolvedKey;


    public CalendarSyncState() {
    }

    public CalendarSyncState(Long id) {
        this.id = id;
    }

    public CalendarSyncState(Long id, long deviceId, long calendarEntryId, int hash) {
        this.id = id;
        this.deviceId = deviceId;
        this.calendarEntryId = calendarEntryId;
        this.hash = hash;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCalendarSyncStateDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public long getCalendarEntryId() {
        return calendarEntryId;
    }

    public void setCalendarEntryId(long calendarEntryId) {
        this.calendarEntryId = calendarEntryId;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    /** To-one relationship, resolved on first access. */
    public Device getDevice() {
        long __key = this.deviceId;
        if (device__resolvedKey == null || !device__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DeviceDao targetDao = daoSession.getDeviceDao();
            Device deviceNew = targetDao.load(__key);
            synchronized (this) {
                device = deviceNew;
            	device__resolvedKey = __key;
            }
        }
        return device;
    }

    public void setDevice(Device device) {
        if (device == null) {
            throw new DaoException("To-one property 'deviceId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.device = device;
            deviceId = device.getId();
            device__resolvedKey = deviceId;
        }
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
