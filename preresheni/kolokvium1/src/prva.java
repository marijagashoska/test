import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

class Rect{
    int width;

    public Rect(int width) {
        this.width = width;

    }
    public int perimeter(){
       return width*4;
    }
}
class Window{
    public List<Rect>lista;
    public String name;
    public Window(String name,List<Rect>lista){
        this.name=name;
        this.lista=lista;
    }
    public int totalPerimeter(){
        return lista.stream().mapToInt(Rect::perimeter).sum();
    }

}
class ShapesApplication{

    public List<Window>prozorci;

    public ShapesApplication(){
    prozorci=new ArrayList<Window>();
    }
    public void printLargestCanvasTo(OutputStream os){
        PrintWriter ps=new PrintWriter(os);
        prozorci.sort(Comparator.comparingInt(Window::totalPerimeter).reversed());
        Window result=prozorci.get(0);
        ps.println(result.name +" "+ result.lista.size() + " "+result.totalPerimeter());
        ps.flush();
    }
    public int readCanvases(InputStream is){
        Scanner scanner=new Scanner(is);
        while(scanner.hasNextLine()){
            String line=scanner.nextLine();
            String arr[]=line.split(" ");
            List<Rect>temp=new ArrayList<Rect>();

            for(int i=1;i<arr.length;i++){
                Rect rect=new Rect(parseInt(arr[i]));
                temp.add(rect);
            }
            prozorci.add(new Window(arr[0],temp));
        }
        scanner.close();
        return prozorci.stream().mapToInt(window->window.lista.size()).sum();

    }
}


public class prva {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();
        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}