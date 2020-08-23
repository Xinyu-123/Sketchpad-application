import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.awt.event.MouseEvent.BUTTON3;

class ShapeStore{
    public String type;
    public Shape shape;
    public Polygon poly;
    public String color;
    ShapeStore(String type, Shape shape, String color){
        this.type = type;
        this.shape = shape;
        this.color = color;
    }
    ShapeStore(String type, Polygon poly, String color){
        this.type = type;
        this.poly = poly;
        this.color = color;
    }

}

public class SketchPad extends JFrame implements ActionListener, WindowListener, MouseListener, MouseMotionListener, ItemListener {

    Color color = UIManager.getColor ( "Panel.background" );
    String selectedShape = new String("Free Draw");
    String selectedColor = new String("Blue");
    boolean firstClick = true;
    boolean Eraser = false;
    Shape storedChunk;
    ShapeStore movingShape;
    ShapeStore copiedShape;
    ArrayList<Integer> pointX = new ArrayList<>();
    ArrayList<Integer> pointY = new ArrayList<>();
    int nPoints = 0;
    String mode = "Draw";
    int upLX, upLY, W, H, selX1, selY1, selX2, selY2;
    String[] extrasList = {"Clear Canvas", "Eraser"};
    String[] colorList = {"Black", "Cyan", "Green", "Yellow", "Magenta", "Red", "Blue"};
    String[] shapeList = {"Free Draw", "Line", "Rectangle", "Square", "Circle", "Ellipse", "Closed Polygon", "Open Polygon"};
    String[] modes = {"Draw", "Select", "Remove Chunk", "Move Chunk", "Copy Chunk", "Paste Chunk", "Remove Shape", "Move Shape", "Cut Shape", "Copy Shape", "Paste Shape"};
    ArrayList<ShapeStore> shapes = new ArrayList<>();

    @Override //from WindowListener
    public void windowClosing(WindowEvent eve) {
        System.exit(0);
    }

    @Override //from WindowListener
    public void windowActivated(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowOpened(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowIconified(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowClosed(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowDeactivated(WindowEvent we) {
    }

    @Override //from WindowListener
    public void windowDeiconified(WindowEvent we) {
    }

    @Override //from MouseMotionListener
    public void mouseMoved(MouseEvent me) {
    }

    @Override //from MouseListener
    public void mouseClicked(MouseEvent me) {
    }

    @Override //from MouseListener
    public void mouseExited(MouseEvent me) {
    }

    @Override //from MouseListener
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }


    public SketchPad(String title) {
        super(title);

        addMouseMotionListener(this);
        addWindowListener(this);
        addMouseListener(this);
        setLayout(null);
        setMenuItems();
    }

    @Override //from ActionListener
    public void actionPerformed(ActionEvent ape) {
        Graphics ga = getGraphics();
        Object s = ape.getActionCommand();

        for (int i = 0; i != colorList.length; i++) {
            if (s.equals(colorList[i])) {
                selectedColor = colorList[i];
                return;
            }
        }
        for (int i = 0; i != shapeList.length; i++) {
            if (s.equals(shapeList[i])) {
                Eraser = false;
                firstClick = true;
                selectedShape = shapeList[i];
                pointY.clear();
                pointX.clear();
                nPoints = 0;

                return;
            }
        }
        for (String value : modes) {
            if (s.equals(value))
                mode = s.toString();
        }
        if (s.equals("Eraser")) {
            Eraser = true;
            return;
        } else if (s.equals("Clear Canvas")) {
            ga.clearRect(0, 0, 700, 700);
            shapes.clear();
            return;
        }

    }

    void chooseColor(Graphics ga) {
        for (int i = 0; i != colorList.length; i++) {
            if (selectedColor.equals(colorList[i])) {
                switch (i) {
                    case 0:
                        ga.setColor(Color.black);
                        break;
                    case 1:
                        ga.setColor(Color.cyan);
                        break;
                    case 2:
                        ga.setColor(Color.green);
                        break;
                    case 3:
                        ga.setColor(Color.yellow);
                        break;
                    case 4:
                        ga.setColor(Color.magenta);
                        break;
                    case 5:
                        ga.setColor(Color.red);
                        break;
                    case 6:
                        ga.setColor(Color.blue);
                }
            }
        }
    }



    @Override //from MouseListener
    public void mouseReleased(MouseEvent me) {

        Graphics ga = getGraphics();
        switch (mode) {
            case "Draw":
                if (Eraser) {
                    return;
                }
                chooseColor(ga);

                switch (selectedShape) {
                    case "Line":
                        selX2 = me.getX();
                        selY2 = me.getY();
                        ga.drawLine(selX1, selY1, selX2, selY2);
                        shapes.add(new ShapeStore("Line", new Line2D.Float(selX1, selY1, selX2, selY2), selectedColor));
                        break;
                    case "Ellipse":
                        selX2 = me.getX();
                        selY2 = me.getY();
                        drawSelectedShape(ga, "Ellipse");
                        break;
                    case "Circle":
                        selX2 = me.getX();
                        selY2 = me.getY();
                        drawSelectedShape(ga, "Circle");
                        break;
                    case "Square":
                        selX2 = me.getX();
                        selY2 = me.getY();
                        drawSelectedShape(ga, "Square");
                        break;
                    case "Rectangle":
                        selX2 = me.getX();
                        selY2 = me.getY();
                        drawSelectedShape(ga, "Rectangle");
                        break;

                }
                ga.setColor(Color.yellow);
                ga.drawString(".", selX1, selY1);
                ga.setColor(Color.black);
                break;

            case "Remove Chunk":
                selX2 = me.getX();
                selY2 = me.getY();
                drawSelectedShape(ga, "Rectangle");
                break;
            case "Copy Chunk":
                selX2 = me.getX();
                selY2 = me.getY();
                upLX = Math.min(selX1, selX2);
                upLY = Math.min(selY1, selY2);
                W = Math.abs(selX1 - selX2);
                H = Math.abs(selY1 - selY2);
                storedChunk = new Rectangle(upLX, upLY, W, H);
                System.out.println("Copy" + storedChunk);
                break;


            case "Remove Shape":
                selX2 = me.getX();
                selY2 = me.getY();
                for(ShapeStore shape: shapes){
                    if(shape.shape != null && shape.shape.contains(selX2, selY2)){
                        removeShape(shape);
                    }
                    if(shape.poly != null && shape.poly.contains(selX2, selY2)){
                        System.out.println(shape.poly);
                        removeShape(shape);
                        break;
                    }
                }
            case "Move Shape":
                selX2 = me.getX();
                selY2 = me.getY();
                removeShape(movingShape);
                moveShape(movingShape);
                break;
            case "Cut Shape":
                selX2 = me.getX();
                selY2 = me.getY();
                for(ShapeStore shape : shapes){
                    if(shape.shape != null && shape.shape.contains(selX2, selY2)){
                        copiedShape = shape;
                        removeShape(shape);
                    }
                    if(shape.poly != null && shape.poly.contains(selX2, selY2)){
                        copiedShape = shape;
                        for(int x : copiedShape.poly.xpoints)
                            System.out.println(x);
                        removeShape(shape);
                    }
                }
                break;
            case "Copy Shape":
                selX2 = me.getX();
                selY2 = me.getY();
                for(ShapeStore shape : shapes){
                    //System.out.println(shape.poly);
                    if(shape.shape != null && shape.shape.contains(selX2, selY2)){
                        copiedShape = shape;
                    }
                    if(shape.poly != null && shape.poly.contains(selX2, selY2)){
                        copiedShape = shape;
                    }

                }

                break;
            case "Paste Shape":
                selX2 = me.getX();
                selY2 = me.getY();

                pasteShape(copiedShape);

        }

    }

    void drawSelectedShape(Graphics ga, String sel_shape) {
        upLX = Math.min(selX1, selX2);
        upLY = Math.min(selY1, selY2);
        W = Math.abs(selX1 - selX2);
        H = Math.abs(selY1 - selY2);
        switch (mode) {
            case "Draw":
                switch (sel_shape) {
                    case "Square":
                        ga.fillRect(upLX, upLY, W, W);
                        shapes.add(new ShapeStore("Square", new Rectangle(upLX, upLY, W, W), selectedColor));
                        break;
                    case "Circle":
                        ga.fillOval(upLX, upLY, W, W);
                        shapes.add(new ShapeStore("Circle", new Ellipse2D.Float(upLX, upLY, W, W), selectedColor));
                        break;
                    case "Rectangle":
                        ga.fillRect(upLX, upLY, W, H);
                        shapes.add(new ShapeStore("Rectangle", new Rectangle(upLX, upLY, W, H), selectedColor));
                        break;
                    case "Ellipse":
                        ga.fillOval(upLX, upLY, W, H);
                        shapes.add(new ShapeStore("Ellipse", new Ellipse2D.Float(upLX, upLY, W, H), selectedColor));
                        break;
                    case "Closed Polygon":
                        pointX.add(selX1);
                        pointY.add(selY1);
                        nPoints += 1;
                        Polygon poly = new Polygon(pointX.stream().mapToInt(i -> i).toArray(), pointY.stream().mapToInt(i -> i).toArray(), nPoints);
                        ga.drawPolygon(poly);
                        shapes.add(new ShapeStore("Closed Polygon", poly, selectedColor));
                        System.out.println(shapes.get(0).type);
                        break;

                    case "Open Polygon":
                        pointX.add(selX1);
                        pointY.add(selY1);
                        nPoints += 1;
                        ga.drawPolyline( pointX.stream().mapToInt(i -> i).toArray(), pointY.stream().mapToInt(i -> i).toArray(), nPoints);
                        shapes.add(new ShapeStore("Open Polygon", new Polygon(pointX.stream().mapToInt(i -> i).toArray(), pointY.stream().mapToInt(i -> i).toArray(), nPoints), selectedColor));
                        break;
                }
                break;
            case "Remove Chunk":
                ga.clearRect(upLX, upLY, W, H);
                break;
        }

    }

    @Override //from MouseMotionListener
    public void mouseDragged(MouseEvent me) {
        Graphics ga = getGraphics();
        if(mode.equals("Draw")){

            chooseColor(ga);
            selX2 = me.getX();
            selY2 = me.getY();

            if (Eraser) {
                ga.setColor(color);
                ga.fillOval(selX2, selY2, 10, 10);
            }
            else if(selectedShape.equals("Free Draw")){
                ga.fillOval(me.getX() - 5, me.getY() - 5, 10, 10);
            }
        }else{
        }

    }

    @Override //from MouseListener
    public void mousePressed(MouseEvent me) {
        Graphics ga = getGraphics();

        switch(mode) {
            case "Draw":
                chooseColor(ga);
                if (Eraser) {
                    return;
                }
                if (selectedShape.equals("Free Draw")) {
                    ga.fillOval(selX1 - 5, selY1 - 5, 10, 10);
                }

                if(selectedShape.equals("Closed Polygon")){
                    if(me.getButton() == 3){
                        drawSelectedShape(ga, "Closed Polygon");
                        pointX.clear();
                        pointY.clear();
                        nPoints = 0;
                    }else{
                        if(!firstClick){
                            ga.drawLine(selX1, selY1, me.getX(), me.getY());
                            pointX.add(selX1);
                            pointY.add(selY1);
                            nPoints += 1;
                            selX1 = me.getX();
                            selY1 = me.getY();
                            System.out.println(pointX);
                        }else{
                            firstClick = false;
                        }

                    }
                }
                if(selectedShape.equals("Open Polygon")){
                    if(me.getButton() == 3){
                        drawSelectedShape(ga, "Open Polygon");
                        pointX.clear();
                        pointY.clear();
                        nPoints = 0;
                    }else{
                        if(!firstClick){
                            ga.drawLine(selX1, selY1, me.getX(), me.getY());
                            pointX.add(selX1);
                            pointY.add(selY1);
                            nPoints += 1;
                            selX1 = me.getX();
                            selY1 = me.getY();
                            System.out.println(pointX);
                        }else{
                            firstClick = false;
                        }

                    }
                }


                break;


            case "Move Shape":
                movingShape = null;
                upLX = 0;
                upLY = 0;
                W = 0;
                H = 0;
                selX1 = me.getX();
                selY1 = me.getY();
                for(ShapeStore shape: shapes){
                    if(shape.shape != null && shape.shape.contains(selX1, selY1)){
                        movingShape = shape;
                    }
                    if(shape.poly != null && shape.poly.contains(selX1, selY1)){
                        movingShape = shape;
                    }

                }
            case "Paste Chunk":
                System.out.println("Paste" + storedChunk);
                ga.copyArea(storedChunk.getBounds().x, storedChunk.getBounds().y, storedChunk.getBounds().width, storedChunk.getBounds().height, me.getX() - (storedChunk.getBounds().x + storedChunk.getBounds().width / 2) , me.getY() - (storedChunk.getBounds().y + storedChunk.getBounds().height / 2));
                break;
            case "Select": {
                for(ShapeStore shape: shapes){
                    if(shape.shape != null && shape.shape.contains(me.getX(), me.getY())){
                        System.out.println(shape.shape);
                    }
                    if(shape.poly != null && shape.poly.contains(me.getX(), me.getY())){
                        System.out.println(shape.poly);
                    }
                }
            }
        }
        upLX = 0;
        upLY = 0;
        W = 0;
        H = 0;
        selX1 = me.getX();
        selY1 = me.getY();


    }

    void removeShape(ShapeStore ss){
        System.out.println(ss);
        Shape sel_shape = null;
        String sel_type = "";

        if(ss.shape != null)
            sel_shape = ss.shape;

        sel_type = ss.type;

        Graphics ga = getGraphics();
        switch (sel_type) {
            case "Square":
                ga.clearRect(sel_shape.getBounds().x, sel_shape.getBounds().y, sel_shape.getBounds().width, sel_shape.getBounds().height);
                shapes.remove(ss);
                break;
            case "Circle":
                ga.setColor(color);
                ga.fillOval(sel_shape.getBounds().x, sel_shape.getBounds().y, sel_shape.getBounds().width, sel_shape.getBounds().height);
                shapes.remove(ss);
                break;
            case "Rectangle":
                ga.setColor(color);
                ga.fillRect(sel_shape.getBounds().x, sel_shape.getBounds().y, sel_shape.getBounds().width, sel_shape.getBounds().height);
                shapes.remove(ss);
                break;
            case "Ellipse":
                ga.setColor(color);
                ga.fillOval(sel_shape.getBounds().x, sel_shape.getBounds().y, sel_shape.getBounds().width, sel_shape.getBounds().height);
                shapes.remove(ss);
                break;
            case "Closed Polygon":
                System.out.println("HERE");
                ga.setColor(color);
                if(ss != null){
                    ga.drawPolygon(ss.poly);
                }
                shapes.remove(ss);

        }

    }

    void moveShape(ShapeStore ss){
        int x = 0;
        int y = 0;
        if(ss.shape != null){
            x = selX2 - ss.shape.getBounds().width/2;
            y = selY2 - ss.shape.getBounds().height/2;
        }
        Graphics ga = getGraphics();
        selectedColor = ss.color;
        chooseColor(ga);
        switch (ss.type) {
            case "Square":
                ga.fillRect(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height);
                shapes.add(new ShapeStore("Square", new Rectangle(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height), selectedColor));
                break;
            case "Circle":
                ga.fillOval(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height);
                shapes.add(new ShapeStore("Circle", new Ellipse2D.Float(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height), selectedColor));
                for(ShapeStore shape : shapes){
                    System.out.println(shape.shape);
                }
                System.out.println("_---------------------------------------------");
                break;
            case "Rectangle":
                ga.fillRect(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height);
                shapes.add(new ShapeStore("Rectangle", new Rectangle(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height), selectedColor));
                break;
            case "Ellipse":
                ga.fillOval(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height);
                shapes.add(new ShapeStore("Ellipse", new Ellipse2D.Float(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height), selectedColor));
                break;
            case "Closed Polygon":
                System.out.println("HERE");
                int[] ex = ss.poly.xpoints;
                int xsum = 0;
                for(int i : ex){
                    xsum += i;
                }
                xsum /= (ex.length+1);
                int transX = selX2 - xsum;
                int[] ey = ss.poly.ypoints;
                int ysum = 0;
                for(int i : ey){
                    ysum += i;
                }
                ysum /= (ey.length+1);
                int transY = selY2 - ysum;
                ss.poly.translate(transX , transY);

                //System.out.println(ss.poly.xpoints);
                ga.drawPolygon(ss.poly.xpoints, ss.poly.ypoints, ss.poly.npoints);
                shapes.add(new ShapeStore("Closed Polygon", new Polygon(ss.poly.xpoints, ss.poly.ypoints, ss.poly.npoints), ss.color));


        }
    }


    public void pasteShape(ShapeStore ss){
        int x = 0;
        int y = 0;
        if(ss.shape != null){
            x = selX2 - ss.shape.getBounds().width/2;
            y = selY2 - ss.shape.getBounds().height/2;
        }
        Graphics ga = getGraphics();
        selectedColor = ss.color;
        chooseColor(ga);
        switch (ss.type) {
            case "Square":
                ga.fillRect(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height);
                shapes.add(new ShapeStore("Square", new Rectangle(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height), selectedColor));
                break;
            case "Circle":
                ga.fillOval(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height);
                shapes.add(new ShapeStore("Circle", new Ellipse2D.Float(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height), selectedColor));
                for(ShapeStore shape : shapes){
                    System.out.println(shape.shape);
                }
                System.out.println("_---------------------------------------------");
                break;
            case "Rectangle":
                ga.fillRect(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height);
                shapes.add(new ShapeStore("Rectangle", new Rectangle(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height), selectedColor));
                break;
            case "Ellipse":
                ga.fillOval(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height);
                shapes.add(new ShapeStore("Ellipse", new Ellipse2D.Float(x, y, ss.shape.getBounds().width, ss.shape.getBounds().height), selectedColor));
                break;
            case "Closed Polygon":
//                System.out.println("HERE");
//                //ss.poly.translate(30,40);
//                int[] ex = ss.poly.xpoints;
//                int[] ey = ss.poly.ypoints;
//                for(int i = 0; i < ey.length; i++){
//                    ex[i] += 30;
//                }
//                System.out.println(ss.poly.xpoints);
//                ga.drawPolygon(ex, ss.poly.ypoints, ss.poly.npoints);
                //ga.drawPolygon();
            case "Open Polygon":
                System.out.println("HERE");
                int[] ex = ss.poly.xpoints;
                int xsum = 0;
                for(int i : ex){
                    xsum += i;
                }
                xsum /= (ex.length+1);
                int transX = selX2 - xsum;
                int[] ey = ss.poly.ypoints;
                int ysum = 0;
                for(int i : ey){
                    ysum += i;
                }
                ysum /= (ey.length+1);
                int transY = selY2 - ysum;
                ss.poly.translate(transX , transY);

                //System.out.println(ss.poly.xpoints);
                ga.drawPolygon(ss.poly.xpoints, ss.poly.ypoints, ss.poly.npoints);
                shapes.add(new ShapeStore("Closed Polygon", new Polygon(ss.poly.xpoints, ss.poly.ypoints, ss.poly.npoints), ss.color));
                ss.poly.translate(-transX , -transY);

        }
    }


    void setMenuItems() {
        MenuBar mBar = new MenuBar();
        Menu menuMode = new Menu("Mode");
        for(int i = 0; i < modes.length; i++){
            menuMode.add(modes[i]);
        }
        mBar.add(menuMode);
        menuMode.addActionListener(this);
        Menu menuShape = new Menu("Shape");
        for (int i = 0; i != shapeList.length; i++) {
            menuShape.add(shapeList[i]);
        }
        mBar.add(menuShape);
        menuShape.addActionListener(this);
        Menu menuColor = new Menu("Colors");
        for (int i = 0; i != colorList.length; i++) {
            menuColor.add(colorList[i]);
        }
        mBar.add(menuColor);
        menuColor.addActionListener(this);
        Menu menuExtras = new Menu("Extras");
        for (int i = 0; i != extrasList.length; i++) {
            menuExtras.add(extrasList[i]);
        }
        mBar.add(menuExtras);
        menuExtras.addActionListener(this);
        setMenuBar(mBar);
    }

    //main method menuExtrasecution starts here
    public static void main(String[] args) {
        SketchPad sp = new SketchPad("Sketchpad in Java");
        sp.setSize(700, 700);
        sp.setVisible(true);
    }


}