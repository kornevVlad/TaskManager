package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() throws IOException {
        return new HttpTaskManager();

    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static Gson getGson(){
       GsonBuilder gsonBuilder = new GsonBuilder();
       return gsonBuilder.create();
    }
}