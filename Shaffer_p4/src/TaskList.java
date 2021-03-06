import java.io.*;
import java.util.ArrayList;

public class TaskList implements Serializable {

    private ArrayList<TaskItem> tasks = new ArrayList<TaskItem>();
    private String fromFile;
    private final String saveDir = "SaveFiles/";

    public TaskList() {}

    public TaskList(String loadFile) {
        fromFile = loadFile;
        loadList(loadFile);
    }

    public boolean outOfBounds(int index) {
        return index < 0 || index >= tasks.size();
    }

    public boolean successfullyLoaded() {
        return tasks.size() > 0;
    }

    public boolean hasDefaultSaveLocation(){
        return fromFile != null;
    }


    public boolean saveList(String fileName){
        if(!fileName.toLowerCase().contains(".txt"))
            fileName += ".txt";
        File directory = new File(saveDir);
        if (!directory.exists()){
            boolean dirSetupSuccess = directory.mkdir();
            if(!dirSetupSuccess) {
                System.out.println("ERROR: failed to create SaveFiles directory. list not saved.");
                return false;
            }
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(saveDir + fileName, false);
            OutputStream buffer = new BufferedOutputStream(fileOut);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(this);
            output.close();
            fileOut.close();
            System.out.println("SUCCESS: saved to " + saveDir + fileName);
            fromFile = fileName;
        } catch (IOException i) {
            System.out.println("ERROR: failed to save this task list.");
            return false;
        }

        return true;
    }

    public boolean saveList() {
        if (fromFile == null) {
            System.out.println("ERROR: please check the filename and try again.");
            return false;
        }
        return saveList(fromFile);
    }

    public boolean loadList(String fileName) {
        TaskList prototypeList;
        if (!fileName.toLowerCase().contains(".txt"))
            fileName += ".txt";
        try {
            FileInputStream fileIn = new FileInputStream(saveDir + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object input = in.readObject();
            if (input instanceof TaskList)
                prototypeList = (TaskList) input;
            else
                throw new ClassCastException();

            tasks = prototypeList.tasks;
            fromFile = fileName;
            in.close();
            fileIn.close();
        } catch (ClassCastException cce){
            System.out.println("ERROR: data conversion unsuccessful. try deleting the file.");
            return false;
        } catch (IOException i) {
            System.out.println("ERROR: file loading failure. file may not exist or is corrupted.");
            return false;
        } catch (ClassNotFoundException c) {
            System.out.println("ERROR: data conversion unsuccessful. try deleting the file.");
            c.printStackTrace();
            return false;
        }

        return true;
    }

    public String viewSubset(boolean complete){
        StringBuilder builder = new StringBuilder();
        builder.append(complete ? "Completed Tasks" : "Incomplete Tasks").append("\n----------------\n");
        int indexCounter = 0;

        for(TaskItem item : tasks){
            if(item.isComplete() == complete)
                builder.append(String.format("%d) %s\n", indexCounter, item.toString()));

            indexCounter++;
        }

        return builder.toString().trim();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Current Tasks").append("\n-------------\n");
        int indexCounter = 0;
        for (TaskItem item : tasks) {
            builder.append(String.format("%d) %s\n", indexCounter++, item.toString()));
        }
        return builder.toString().trim();
    }

    public TaskItem getTask(int index){
        if(outOfBounds(index))
            return null;
        return tasks.get(index);
    }

    public TaskItem getTask(String titleArg){
        for (TaskItem item : tasks) {
            if (item.getTitle().equalsIgnoreCase(titleArg)) {
                return item;
            }
        }
        return null;
    }

    public boolean addTask(TaskItem newTask) {
        for (TaskItem item : tasks) {
            if(item.getTitle().equalsIgnoreCase(newTask.getTitle())) {
                return false;
            }
        }
        tasks.add(newTask);
        return true;
    }

    public boolean removeTask(int index) {
        if (outOfBounds(index)) {
            return false;
        }
        tasks.remove(index);
        return true;
    }

    public boolean removeTask(TaskItem item) {
        return tasks.remove(item);
    }

    public boolean setCompletion(int index, boolean complete) {
        if (outOfBounds(index)) {
            return false;
        }
        tasks.get(index).setComplete(complete);
        return true;
    }

    public int size() {
        return tasks.size();
    }

    public int sizeByStatus(boolean complete){
        int counter = 0;
        for (TaskItem item : tasks)
            counter += item.isComplete() == complete ? 1 : 0;
        return counter;
    }

    public int indexOf(TaskItem item) {
        for (int i = 0; i < tasks.size(); i++) {
            TaskItem current = tasks.get(i);
            if (current.equals(item)) {
                return i;
            }
        }
        return -1;
    }
}
