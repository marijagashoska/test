import java.util.*;
import java.util.stream.Collectors;

/*
In summary, files.forEach() is a direct and simple way to iterate over a collection, whereas files.stream().forEach() is part of the Stream API, allowing for more complex operations and transformations on the collection elements before the iteration, and also supporting parallelism through parallelStream().
*/
class FileNameExistsException extends Exception{
    public FileNameExistsException(String file,String folder) {
        super(String.format("There is already a file named %s in the folder %s",file,folder));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
class File implements IFile{
    String name;
    long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(int i) {
        StringBuilder sb=new StringBuilder();
        for(int j=0;j<i;j++){
            sb.append("    ");
        }
        sb.append(this.toString());
        return sb.toString();
    }

    @Override
    public void sortBySize() {}

    @Override
    public long findLargestFile() {
        return size;
    }
    @Override
    public String toString(){
        return String.format("File name: %10s File size: %10d\n",name,size);
    }
}
class Folder extends File{
    List<IFile>files;

    public Folder(String name) {
        super(name,0);
        files=new ArrayList<IFile>();
    }

    public void addFile(IFile file) throws FileNameExistsException {
        if(files.isEmpty()){
            files.add(file);
        }
        else{
            Optional opt=files.stream().filter(item->item.getFileName().equals(file.getFileName())).findAny();
            if(!opt.isPresent()){
                files.add(file);
            }
            else{
                throw new FileNameExistsException(file.getFileName(),name);
            }
        }
    }

    @Override
    public String getFileName() {
        return super.getFileName();
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(int i) {
        StringBuilder sb=new StringBuilder();
        for(int j=0;j<i;j++){
            sb.append("    ");
        }
        sb.append(this.toString());
        files.stream().forEach(f->sb.append(f.getFileInfo(i+1)));
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        files =  files.stream()
                .sorted(Comparator.comparingLong(IFile::getFileSize))
                .collect(Collectors.toList());
        files.stream().forEach(IFile::sortBySize);
    }


    @Override
    public long findLargestFile() {
        OptionalLong opt= files.stream().mapToLong(IFile::findLargestFile).max();
        if(opt.isPresent()){
            return opt.getAsLong();
        }
        else return 0;
    }

    @Override
    public String toString() {
        return String.format("Folder name: %10s Folder size: %10d\n",this.getFileName(),this.getFileSize());
    }
}

class FileSystem{
    Folder rootdirectory;

    public FileSystem() {
        rootdirectory = new Folder("root");
    }

    public void addFile(IFile file) throws FileNameExistsException {
        rootdirectory.addFile(file);
    }
    public long findLargestFile(){
        return rootdirectory.findLargestFile();
    }
    public void sortBySize(){
        rootdirectory.sortBySize();
    }

    @Override
    public String toString() {
        return rootdirectory.getFileInfo(0);
    }
}



public class treta {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}