import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import sign.signlink;

public class Account {

	public static ArrayList<Account> accounts = new ArrayList<Account>();
	private String username;
	private String password;
	private int clientSize;
	private int helmet;
	private int IDKHead;
	private int jaw, gender;

	public Account(String username, String password, int clientSize,
			int helmet, int IDKHead, int jaw, int gender) {
		this.username = username;
		this.password = password;
		this.clientSize = clientSize;
		this.helmet = helmet;
		this.IDKHead = IDKHead;
		this.setJaw(jaw);
		this.setGender(gender);
	}

	public static Account getAccountForName(String name) {
		for (Account a : accounts) {
			if (a.getUsername().equals(name))
				return a;
		}
		return null;
	}

	public int getClientSize() {
		return clientSize;
	}

	public void setClientSize(int clientSize) {
		this.clientSize = clientSize;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static String getFileNameWithoutExtension(File f) {
		f.getName();
		int whereDot = f.getName().lastIndexOf('.');
		if (0 < whereDot && whereDot <= f.getName().length() - 2) {
			return f.getName().substring(0, whereDot);
		}
		return "";
	}

	public static ArrayList<String> getCharacters() {
		ArrayList<String> value = new ArrayList<String>();
		File file = new File(signlink.findcachedir());
		File[] fileArray = file.listFiles();
		for (File f : fileArray) {
			try {
				int dot = f.getName().lastIndexOf('.');
				if (f.getName().substring(dot).equalsIgnoreCase(".alc")) {
					value.add(getFileNameWithoutExtension(f));
				}
			} catch (Exception e) {
			}
		}
		return value;
	}

	public static void load() {
		accounts.clear();
		for (String s : getCharacters()) {
			try {
				RandomAccessFile in = new RandomAccessFile(
						signlink.findcachedir() + s + ".alc", "rw");
				String name = in.readUTF();
				String password = in.readUTF();
				int size = in.readByte();
				int helmet = in.readShort();
				int IDKHead = in.readShort();
				int jaw = in.readShort();
				int gender = in.readByte();
				accounts.add(new Account(name, password, size, helmet, IDKHead,
						jaw, gender));

				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void delete() {
		File f = new File(signlink.findcachedir() + username + ".alc");
		boolean succes = f.delete();
		if (!succes)
			throw new IllegalArgumentException("Cant delete");
	}

	/**
	 * Saves the settings to the settings file.
	 */
	public void save() {
		try {
			RandomAccessFile out = new RandomAccessFile(signlink.findcachedir()
					+ username + ".alc", "rw");
			out.writeUTF(username);
			out.writeUTF(password);
			out.writeByte(clientSize);
			out.writeShort(helmet);
			out.writeShort(IDKHead);
			out.writeShort(jaw);
			out.writeByte(gender);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getIDKHead() {
		return IDKHead;
	}

	public void setIDKHead(int iDKHead) {
		IDKHead = iDKHead;
	}

	public int getHelmet() {
		return helmet;
	}

	public void setHelmet(int helmet) {
		this.helmet = helmet;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getJaw() {
		return jaw;
	}

	public void setJaw(int jaw) {
		this.jaw = jaw;
	}

}
