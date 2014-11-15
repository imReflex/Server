import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class NPCDefThing2 {

	private static NPCDefinition[] definitions;

	public static boolean initialize() {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int npcs = 0;
		BufferedReader characterfile = null;
		definitions = new NPCDefinition[NPCDef.streamIndices.length];
		try {
			characterfile = new BufferedReader(new FileReader("./npc.cfg"));
		} catch (FileNotFoundException fileex) {
			fileex.printStackTrace();
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			try {
				characterfile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					int npcType = Integer.parseInt(token3[0]);
					String npcName = token3[1];
					int combat = Integer.parseInt(token3[2]);
					int HP = Integer.parseInt(token3[3]);
					definitions[npcType] = new NPCDefinition(npcName, combat,
							HP);
					npcs++;
				}
			} else {
				if (line.equals("[ENDOFNPCLIST]")) {
					System.out.println("Loaded " + npcs + " npcs.");
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					System.out.println("Loaded " + npcs + " npcs." + definitions.length);
					readNew();
					System.out.println("2");
					write();
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
				ioexception1.printStackTrace();
				System.out.println(line);
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	private static void readNew() {
		for (int i = 0; i < definitions.length; i++) {
			NPCDefinition npcDef = definitions[i];
			if (npcDef != null)
				continue;
			NPCDef npc = NPCDef.forID(i);
			definitions[i] = new NPCDefinition(npc.name == null ? "None"
					: npc.name, npc.combatLevel, 0);
		}
	}

	private static void write() {
		System.out.println(definitions.length);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("./npc_new.cfg", true));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < definitions.length; i++) {
			NPCDefinition def = definitions[i];
			System.out.println("written " + i);
			try {
				bw.write("npc = " + i + "		" + def.npcName + "				"
						+ def.npcCombat + "	" + def.npcHealth + "");
				bw.newLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static class NPCDefinition {
		public int npcCombat;
		public int npcHealth;
		public String npcName;

		private NPCDefinition(String npcName, int npcCombat, int npcHealth) {
			this.npcName = npcName;
			this.npcCombat = npcCombat;
			this.npcHealth = npcHealth;
		}
	}
}
