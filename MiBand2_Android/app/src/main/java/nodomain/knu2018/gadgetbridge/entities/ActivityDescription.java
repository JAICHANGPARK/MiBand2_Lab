package nodomain.knu2018.gadgetbridge.entities;

import java.util.List;
import nodomain.knu2018.gadgetbridge.entities.DaoSession;
import de.greenrobot.dao.DaoException;

import de.greenrobot.dao.AbstractDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * A user may further specify his activity with a detailed description and the help of tags.
 * One or more tags can be added to a given activity range.
 */
public class ActivityDescription {

    private Long id;
    private int timestampFrom;
    private int timestampTo;
    private String details;
    private long userId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ActivityDescriptionDao myDao;

    private User user;
    private Long user__resolvedKey;

    private List<Tag> tagList;

    public ActivityDescription() {
    }

    public ActivityDescription(Long id) {
        this.id = id;
    }

    public ActivityDescription(Long id, int timestampFrom, int timestampTo, String details, long userId) {
        this.id = id;
        this.timestampFrom = timestampFrom;
        this.timestampTo = timestampTo;
        this.details = details;
        this.userId = userId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getActivityDescriptionDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTimestampFrom() {
        return timestampFrom;
    }

    public void setTimestampFrom(int timestampFrom) {
        this.timestampFrom = timestampFrom;
    }

    public int getTimestampTo() {
        return timestampTo;
    }

    public void setTimestampTo(int timestampTo) {
        this.timestampTo = timestampTo;
    }

    /**
     * An optional detailed description, specific to this very activity occurrence.
     */
    public String getDetails() {
        return details;
    }

    /**
     * An optional detailed description, specific to this very activity occurrence.
     */
    public void setDetails(String details) {
        this.details = details;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    /** To-one relationship, resolved on first access. */
    public User getUser() {
        long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
            	user__resolvedKey = __key;
            }
        }
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new DaoException("To-one property 'userId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            userId = user.getId();
            user__resolvedKey = userId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Tag> getTagList() {
        if (tagList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TagDao targetDao = daoSession.getTagDao();
            List<Tag> tagListNew = targetDao._queryActivityDescription_TagList(id);
            synchronized (this) {
                if(tagList == null) {
                    tagList = tagListNew;
                }
            }
        }
        return tagList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTagList() {
        tagList = null;
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
