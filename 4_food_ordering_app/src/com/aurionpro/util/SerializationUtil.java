package com.aurionpro.util;

import java.io.*;

public class SerializationUtil {

	public static <T> boolean serialize(T object, String filePath) {
		try (FileOutputStream fileOut = new FileOutputStream(filePath);
				ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(object);
			 System.out.println("INFO: Object of type " +
			 object.getClass().getSimpleName() + " successfully serialized to " +
			 filePath);
			return true;
		} catch (IOException i) {
			System.err.println("ERROR: Error during serialization to " + filePath + ": " + i.getMessage());
			 i.printStackTrace(); 
			return false;
		}
	}

	@SuppressWarnings("unchecked") 
	public static <T> T deserialize(String filePath) {
		File file = new File(filePath);
		if (!file.exists() || file.length() == 0) {
			 System.out.println("INFO: File not found or empty: " + filePath + ". Returning null.");
			return null; 
		}

		try (FileInputStream fileIn = new FileInputStream(filePath);
				ObjectInputStream in = new ObjectInputStream(fileIn)) {
			T object = (T) in.readObject();
			 System.out.println("INFO: Object of type " +
			 object.getClass().getSimpleName() + " successfully deserialized from " +
			 filePath);
			return object;
		} catch (FileNotFoundException f) {
			System.err.println("ERROR: File not found during deserialization from " + filePath + ": " + f.getMessage());
			return null;
		} catch (IOException i) {
			System.err.println("ERROR: Error during deserialization from " + filePath + ": " + i.getMessage());
			 i.printStackTrace(); 
			return null;
		} catch (ClassNotFoundException c) {
			System.err
					.println("ERROR: Class not found during deserialization from " + filePath + ": " + c.getMessage());
			 c.printStackTrace();
			return null;
		}
	}
}