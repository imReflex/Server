
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileOperations {

    public FileOperations()  {
    }

    public static final byte[] ReadFile(String s) {
        try  {
            File file = new File(s);
            int i = (int)file.length();
            byte abyte0[] = new byte[i];
            DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(s)));
            datainputstream.readFully(abyte0, 0, i);
            datainputstream.close();
            TotalRead++;
            return abyte0;
        } catch(Exception exception) {
        }
        return null;
    }

    public static final void WriteFile(String s, byte abyte0[]) {
        try {
            (new File((new File(s)).getParent())).mkdirs();
            FileOutputStream fileoutputstream = new FileOutputStream(s);
            fileoutputstream.write(abyte0, 0, abyte0.length);
            fileoutputstream.close();
            TotalWrite++;
            CompleteWrite++;
        }  catch(Throwable throwable) {
            System.out.println((new StringBuilder()).append("Write Error: ").append(s).toString());
        }
    }
    
    public static void copy(int id) {
    	File to = new File("C:/Users/allen_000/Dropbox/RX2014/workspace/Update Server/data/fs/pack/model/80" + id + ".dat");
    	if(to.exists())
    		return;
    	try {
		InputStream in = new FileInputStream(new File("C:/Users/allen_000/Downloads/377 Models/Models/" + id + ".dat"));
		OutputStream out = new FileOutputStream(to/*new File("C:/Users/Allen/Desktop/Client Development/caches/725/missing/" + id + ".gz")*/);
		System.out.println("Copying file:" + id);
		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
    	} catch(Exception e) {
    		System.out.println("Error loading for ID: " + id);
    		e.printStackTrace();
    	}
	}
	
	public static boolean FileExists(String file) {
		File f = new File(file);
		if(f.exists())
			return true;
		else
			return false;
	}

    public static int TotalRead = 0;
    public static int TotalWrite = 0;
    public static int CompleteWrite = 0;
}