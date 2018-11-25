package materialpurchasing.server.IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import materialpurchasing.server.component.BaseComponentServiceImpl;

public class IOImpl {

	String name;
	public IOImpl(String name){
		this.name=name;
	}
	
	public BaseComponentServiceImpl readFromFile(Class<BaseComponentServiceImpl> class1) throws FileNotFoundException{
		BufferedReader bufferedReader = new BufferedReader(new FileReader("Output.json"));

        Gson gson = new Gson();
        return gson.fromJson(bufferedReader,class1);
	}
	
	public void writeToFile() throws IOException {
		try (Writer writer = new FileWriter("Output.json")) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(this, writer);
		}
	}
}
