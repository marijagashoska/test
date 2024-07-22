import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class IrregularCanvasException extends Exception{
    public IrregularCanvasException(String message){
        super(message);
    }
}
class Forma{
    public double size;
    public String type;
    public Forma(String type,double size){
        this.type=type;
        this.size=size;
    }
    public double calculate(){
        if(type.equalsIgnoreCase("S")){
            return Math.pow(size,2);
        }
        else return Math.PI*Math.pow(size,2);
    }

}
class Ramka{
    public String id;
    public List<Forma>formi;


    public Ramka(String id, List<Forma> formi) {
        this.id = id;
        this.formi = formi;
    }
    public double calculate(){
        return formi.stream().mapToDouble(Forma::calculate).sum();
    }

    @Override
    public String toString() {
        long numCircles=formi.stream().filter(item->item.type.equals("C")).count();
        long numSquares=formi.stream().filter(item->item.type.equals("S")).count();
        formi.sort(Comparator.comparingDouble(Forma::calculate));
        double minArea=formi.get(0).calculate();
        formi.sort(Comparator.comparingDouble(Forma::calculate).reversed());
        double maxArea=formi.get(0).calculate();
        double average=formi.stream().mapToDouble(Forma::calculate).sum()/formi.size();
        return String.format("%s %d %d %d %.2f %.2f %.2f", id, formi.size(), numCircles, numSquares, minArea, maxArea, average);
    }
}
class ShapesApplication2{
    public double limit;
    public List<Ramka> ramki;
    public ShapesApplication2(double limit){
        this.limit=limit;
        ramki=new ArrayList<Ramka>();
    }
    public double calculateLimit(String type,double size){
        double temp;
        if(type.equalsIgnoreCase("C")){
            temp=Math.PI*Math.pow(size,2);
        }
        else{
            temp=Math.pow(size,2);
        }
        if(temp<=limit){
            return temp;
        }
        else{
            return -1;
        }
    }
    public void readCanvases(InputStream is) throws IrregularCanvasException {
        String line="";
        String array[];
        boolean valid;
        Scanner scanner=new Scanner(is);
        List<Forma>tempList;
        while(scanner.hasNextLine()){
            valid=true;
            line= scanner.nextLine();
            array=line.split(" ");
            tempList=new ArrayList<Forma>();
            for(int i=1;i<array.length;i+=2){

                try{
                    if(calculateLimit(array[i],Double.parseDouble(array[i+1]))!=-1){
                        tempList.add(new Forma(array[i],Double.parseDouble(array[i+1])));
                    }
                    else{
                        throw new IrregularCanvasException(String.format("Canvas %s has a shape with area larger than %.2f", array[0], limit));

                    }
                }
                catch (IrregularCanvasException e)
                {
                    valid=false;
                    System.out.println(e.getMessage());
                    break;
                }

            }
            //duri tuka treba da se kreira prozorecot
            if(valid)
                ramki.add(new Ramka(array[0],tempList));

        }
    }
    public void printCanvases(OutputStream os){
        ramki.sort(Comparator.comparingDouble(Ramka::calculate).reversed());
        PrintWriter pw=new PrintWriter(os);
        ramki.stream().forEach(item->pw.println(item.toString()));
        pw.close();
    }
}
public class vtora {

    public static void main(String[] args) throws IrregularCanvasException {

        ShapesApplication2 shapesApplication = new ShapesApplication2(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}