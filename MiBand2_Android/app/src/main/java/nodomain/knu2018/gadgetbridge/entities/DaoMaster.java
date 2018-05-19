package nodomain.knu2018.gadgetbridge.entities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import nodomain.knu2018.gadgetbridge.entities.UserAttributesDao;
import nodomain.knu2018.gadgetbridge.entities.UserDao;
import nodomain.knu2018.gadgetbridge.entities.DeviceAttributesDao;
import nodomain.knu2018.gadgetbridge.entities.DeviceDao;
import nodomain.knu2018.gadgetbridge.entities.TagDao;
import nodomain.knu2018.gadgetbridge.entities.ActivityDescriptionDao;
import nodomain.knu2018.gadgetbridge.entities.ActivityDescTagLinkDao;
import nodomain.knu2018.gadgetbridge.entities.MiBandActivitySampleDao;
import nodomain.knu2018.gadgetbridge.entities.PebbleHealthActivitySampleDao;
import nodomain.knu2018.gadgetbridge.entities.PebbleHealthActivityOverlayDao;
import nodomain.knu2018.gadgetbridge.entities.PebbleMisfitSampleDao;
import nodomain.knu2018.gadgetbridge.entities.PebbleMorpheuzSampleDao;
import nodomain.knu2018.gadgetbridge.entities.HPlusHealthActivityOverlayDao;
import nodomain.knu2018.gadgetbridge.entities.HPlusHealthActivitySampleDao;
import nodomain.knu2018.gadgetbridge.entities.No1F1ActivitySampleDao;
import nodomain.knu2018.gadgetbridge.entities.XWatchActivitySampleDao;
import nodomain.knu2018.gadgetbridge.entities.CalendarSyncStateDao;
import nodomain.knu2018.gadgetbridge.entities.BaseActivitySummaryDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 18): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 18;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        UserAttributesDao.createTable(db, ifNotExists);
        UserDao.createTable(db, ifNotExists);
        DeviceAttributesDao.createTable(db, ifNotExists);
        DeviceDao.createTable(db, ifNotExists);
        TagDao.createTable(db, ifNotExists);
        ActivityDescriptionDao.createTable(db, ifNotExists);
        ActivityDescTagLinkDao.createTable(db, ifNotExists);
        MiBandActivitySampleDao.createTable(db, ifNotExists);
        PebbleHealthActivitySampleDao.createTable(db, ifNotExists);
        PebbleHealthActivityOverlayDao.createTable(db, ifNotExists);
        PebbleMisfitSampleDao.createTable(db, ifNotExists);
        PebbleMorpheuzSampleDao.createTable(db, ifNotExists);
        HPlusHealthActivityOverlayDao.createTable(db, ifNotExists);
        HPlusHealthActivitySampleDao.createTable(db, ifNotExists);
        No1F1ActivitySampleDao.createTable(db, ifNotExists);
        XWatchActivitySampleDao.createTable(db, ifNotExists);
        CalendarSyncStateDao.createTable(db, ifNotExists);
        BaseActivitySummaryDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        UserAttributesDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
        DeviceAttributesDao.dropTable(db, ifExists);
        DeviceDao.dropTable(db, ifExists);
        TagDao.dropTable(db, ifExists);
        ActivityDescriptionDao.dropTable(db, ifExists);
        ActivityDescTagLinkDao.dropTable(db, ifExists);
        MiBandActivitySampleDao.dropTable(db, ifExists);
        PebbleHealthActivitySampleDao.dropTable(db, ifExists);
        PebbleHealthActivityOverlayDao.dropTable(db, ifExists);
        PebbleMisfitSampleDao.dropTable(db, ifExists);
        PebbleMorpheuzSampleDao.dropTable(db, ifExists);
        HPlusHealthActivityOverlayDao.dropTable(db, ifExists);
        HPlusHealthActivitySampleDao.dropTable(db, ifExists);
        No1F1ActivitySampleDao.dropTable(db, ifExists);
        XWatchActivitySampleDao.dropTable(db, ifExists);
        CalendarSyncStateDao.dropTable(db, ifExists);
        BaseActivitySummaryDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(UserAttributesDao.class);
        registerDaoClass(UserDao.class);
        registerDaoClass(DeviceAttributesDao.class);
        registerDaoClass(DeviceDao.class);
        registerDaoClass(TagDao.class);
        registerDaoClass(ActivityDescriptionDao.class);
        registerDaoClass(ActivityDescTagLinkDao.class);
        registerDaoClass(MiBandActivitySampleDao.class);
        registerDaoClass(PebbleHealthActivitySampleDao.class);
        registerDaoClass(PebbleHealthActivityOverlayDao.class);
        registerDaoClass(PebbleMisfitSampleDao.class);
        registerDaoClass(PebbleMorpheuzSampleDao.class);
        registerDaoClass(HPlusHealthActivityOverlayDao.class);
        registerDaoClass(HPlusHealthActivitySampleDao.class);
        registerDaoClass(No1F1ActivitySampleDao.class);
        registerDaoClass(XWatchActivitySampleDao.class);
        registerDaoClass(CalendarSyncStateDao.class);
        registerDaoClass(BaseActivitySummaryDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
