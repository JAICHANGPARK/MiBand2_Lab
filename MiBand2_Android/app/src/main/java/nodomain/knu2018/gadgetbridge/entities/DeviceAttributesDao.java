package nodomain.knu2018.gadgetbridge.entities;
import android.os.Build;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import nodomain.knu2018.gadgetbridge.entities.DeviceAttributes;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DEVICE_ATTRIBUTES".
*/
public class DeviceAttributesDao extends AbstractDao<DeviceAttributes, Long> {

    public static final String TABLENAME = "DEVICE_ATTRIBUTES";

    /**
     * Properties of entity DeviceAttributes.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FirmwareVersion1 = new Property(1, String.class, "firmwareVersion1", false, "FIRMWARE_VERSION1");
        public final static Property FirmwareVersion2 = new Property(2, String.class, "firmwareVersion2", false, "FIRMWARE_VERSION2");
        public final static Property ValidFromUTC = new Property(3, java.util.Date.class, "validFromUTC", false, "VALID_FROM_UTC");
        public final static Property ValidToUTC = new Property(4, java.util.Date.class, "validToUTC", false, "VALID_TO_UTC");
        public final static Property DeviceId = new Property(5, long.class, "deviceId", false, "DEVICE_ID");
        public final static Property VolatileIdentifier = new Property(6, String.class, "volatileIdentifier", false, "VOLATILE_IDENTIFIER");
    };

    private Query<DeviceAttributes> device_DeviceAttributesListQuery;

    public DeviceAttributesDao(DaoConfig config) {
        super(config);
    }
    
    public DeviceAttributesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DEVICE_ATTRIBUTES\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"FIRMWARE_VERSION1\" TEXT NOT NULL ," + // 1: firmwareVersion1
                "\"FIRMWARE_VERSION2\" TEXT," + // 2: firmwareVersion2
                "\"VALID_FROM_UTC\" INTEGER," + // 3: validFromUTC
                "\"VALID_TO_UTC\" INTEGER," + // 4: validToUTC
                "\"DEVICE_ID\" INTEGER NOT NULL ," + // 5: deviceId
                "\"VOLATILE_IDENTIFIER\" TEXT);"); // 6: volatileIdentifier
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DEVICE_ATTRIBUTES\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DeviceAttributes entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getFirmwareVersion1());
 
        String firmwareVersion2 = entity.getFirmwareVersion2();
        if (firmwareVersion2 != null) {
            stmt.bindString(3, firmwareVersion2);
        }
 
        java.util.Date validFromUTC = entity.getValidFromUTC();
        if (validFromUTC != null) {
            stmt.bindLong(4, validFromUTC.getTime());
        }
 
        java.util.Date validToUTC = entity.getValidToUTC();
        if (validToUTC != null) {
            stmt.bindLong(5, validToUTC.getTime());
        }
        stmt.bindLong(6, entity.getDeviceId());
 
        String volatileIdentifier = entity.getVolatileIdentifier();
        if (volatileIdentifier != null) {
            stmt.bindString(7, volatileIdentifier);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DeviceAttributes readEntity(Cursor cursor, int offset) {
        DeviceAttributes entity = new DeviceAttributes( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // firmwareVersion1
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // firmwareVersion2
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // validFromUTC
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // validToUTC
            cursor.getLong(offset + 5), // deviceId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // volatileIdentifier
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DeviceAttributes entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFirmwareVersion1(cursor.getString(offset + 1));
        entity.setFirmwareVersion2(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setValidFromUTC(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setValidToUTC(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setDeviceId(cursor.getLong(offset + 5));
        entity.setVolatileIdentifier(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DeviceAttributes entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DeviceAttributes entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "deviceAttributesList" to-many relationship of Device. */
    public List<DeviceAttributes> _queryDevice_DeviceAttributesList(long deviceId) {
        synchronized (this) {
            if (device_DeviceAttributesListQuery == null) {
                QueryBuilder<DeviceAttributes> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.DeviceId.eq(null));
                queryBuilder.orderRaw("T.'VALID_FROM_UTC' DESC");
                device_DeviceAttributesListQuery = queryBuilder.build();
            }
        }
        Query<DeviceAttributes> query = device_DeviceAttributesListQuery.forCurrentThread();
        query.setParameter(0, deviceId);
        return query.list();
    }

}
