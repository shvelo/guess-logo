package com.shvelo.guesslogo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.*;

public class DBHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH;

	private static String DB_NAME = "brands.db";

	private SQLiteDatabase myDataBase;

	private final Context myContext;
	
	private static boolean FORCE_UPDATE = false;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DBHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
		
		File dataDir = new File(context.getApplicationInfo().dataDir);
		File DBDir =  new File(dataDir, "databases");
		File DBFile = new File(DBDir, DB_NAME);
		
		DB_PATH = DBFile.getPath();
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void create() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist && !FORCE_UPDATE) {
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(DB_PATH);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void open() throws SQLException {

		// Open the database
		myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	public SQLiteDatabase get() {
		return myDataBase;
	}
	
}