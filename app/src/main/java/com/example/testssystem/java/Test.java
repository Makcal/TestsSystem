package com.example.testssystem.java;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.testssystem.exceptions.InvalidColumnTypeException;
import com.example.testssystem.exceptions.MissingColumnException;

import java.util.HashMap;
import java.util.Map;

public class Test extends DB_Record implements Parcelable {
    public final static String TABLE_NAME = "tests";

    public int id; // PK
    public String name;
    public Integer subsectionId = null; // FK to CategorySubsection.id
    public Integer authorId = null; // FK to User.id
    public String description = null;

    public final static String ID_COLUMN = "id";
    public final static String NAME_COLUMN = "name";
    public final static String SUBSECTION_ID_COLUMN = "subsection_id";
    public final static String AUTHOR_ID_COLUMN = "author_id";
    public final static String DESCRIPTION_COLUMN = "description";

    public Test() { }

    public Test(int id) {
        this.id = id;
    }

    public Test(String name, Integer subsectionId, Integer authorId, String description) {
        this.name = name;
        this.subsectionId = subsectionId;
        this.authorId = authorId;
        this.description = description;
    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }

    @Override
    Map<String, Integer> getPrimaryKeyColumns() {
        Map<String, Integer> map = new HashMap<>();
        map.put(ID_COLUMN, Cursor.FIELD_TYPE_INTEGER);
        return map;
    }

    @Override
    Map<String, Object> getPrimaryKey() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_COLUMN, id);
        return map;
    }

    @Override
    ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN, name);
        contentValues.put(SUBSECTION_ID_COLUMN, subsectionId);
        contentValues.put(AUTHOR_ID_COLUMN, authorId);
        contentValues.put(DESCRIPTION_COLUMN, description);
        return contentValues;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    void initFromMap(Map<String, Object> map) throws MissingColumnException, InvalidColumnTypeException {
        try {
            id = (int) map.get(ID_COLUMN);
            name = (String) map.get(NAME_COLUMN);
            subsectionId = (Integer) map.get(SUBSECTION_ID_COLUMN);
            authorId = (Integer) map.get(AUTHOR_ID_COLUMN);
            description = (String) map.get(DESCRIPTION_COLUMN);
        }
        catch (NullPointerException e) {
            throw new MissingColumnException(e);
        }
        catch (ClassCastException e) {
            throw new InvalidColumnTypeException(e);
        }
    }

    @Override
    String getCreateStatement() {
        return "CREATE TABLE `tests` (\n" +
                "  `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  `name` TEXT NOT NULL,\n" +
                "  `subsection_id` INTEGER DEFAULT NULL,\n" +
                "  `author_id` INTEGER DEFAULT NULL,\n" +
                "  `description` TEXT DEFAULT NULL,\n" +
                "  CONSTRAINT `fk_tests_subsections`\n" +
                "    FOREIGN KEY (`subsection_id`)\n" +
                "    REFERENCES `category_subsections` (`id`)\n" +
                "    ON DELETE SET NULL\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `fk_tests_users`\n" +
                "    FOREIGN KEY (`author_id`)\n" +
                "    REFERENCES `users` (`id`)\n" +
                "    ON DELETE SET NULL\n" +
                "    ON UPDATE CASCADE" +
                ");";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (subsectionId != null) {
            dest.writeByte((byte) 1);
            dest.writeInt(subsectionId);
        }
        else {
            dest.writeByte((byte) 0);
        }
        if (authorId != null) {
            dest.writeByte((byte) 1);
            dest.writeInt(authorId);
        }
        else {
            dest.writeByte((byte) 0);
        }
        dest.writeString(description);
    }

    private Test(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0) {
            subsectionId = null;
        } else {
            subsectionId = in.readInt();
        }
        if (in.readByte() == 0) {
            authorId = null;
        } else {
            authorId = in.readInt();
        }
        description = in.readString();
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };
}
