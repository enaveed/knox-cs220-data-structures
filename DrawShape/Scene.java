package drawshapes;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import drawshapes.DrawShapes.ShapeType;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * A scene of shapes.  Uses the Model-View-Controller (MVC) design pattern,
 * though note that model knows something about the view, as the draw() 
 * method both in Scene and in Shape uses the Graphics object. That's kind of sloppy,
 * but it also helps keep things simple.
 * 
 * This class allows us to talk about a "scene" of shapes,
 * rather than individual shapes, and to apply operations
 * to collections of shapes.
 * 
 * @author jspacco
 *
 */
public class Scene implements Iterable<IShape>
{
    private List<IShape> shapeList=new LinkedList<IShape>();
    private SelectionRectangle selectRect;
    private boolean isDrag;
    private Point startDrag;
    private Scene scene;

    
    public void updateSelectRect(Point drag) {
        for (IShape s : this){
            s.setSelected(false);
        }
        if (drag.x > startDrag.x){
            if (drag.y > startDrag.y){
                // top-left to bottom-right
                selectRect = new SelectionRectangle(startDrag.x, drag.x, startDrag.y, drag.y);
            } else {
                // bottom-left to top-right
                selectRect = new SelectionRectangle(startDrag.x, drag.x, drag.y, startDrag.y);
            }
        } else {
            if (drag.y > startDrag.y){
                // top-right to bottom-left
                selectRect = new SelectionRectangle(drag.x, startDrag.x, startDrag.y, drag.y);
            } else {
                // bottom-left to top-right
                selectRect = new SelectionRectangle(drag.x, startDrag.x, drag.y, startDrag.y);
            }
        }
        List<IShape> selectedShapes = this.select(selectRect);
        for (IShape s : selectedShapes){
            s.setSelected(true);
        }
    }
    
    public void stopDrag() {
        this.isDrag = false;
    }
    
    public void startDrag(Point p){
        this.isDrag = true;
        this.startDrag = p;
    }
    
    /**
     * Draw all the shapes in the scene using the given Graphics object.
     * @param g
     */
    public void draw(Graphics g) {
        for (IShape s : shapeList) {
            if (s!=null){
                s.draw(g);
            }
        }
        if (isDrag) {
            selectRect.draw(g);
        }
    }
    
    /**
     * Get an iterator that can iterate through all the shapes
     * in the scene.
     */
    public Iterator<IShape> iterator() {
        return shapeList.iterator();
    }
    
    /**
     * Return a list of shapes that contain the given point.
     * @param point The point
     * @return A list of shapes that contain the given point.
     */
    public List<IShape> select(Point point)
    {
        List<IShape> selected = new LinkedList<IShape>();
        for (IShape s : shapeList){
            if (s.contains(point)){
                selected.add(s);
            }
        }
        return selected;
    }
    
    /**
     * Return a list of shapes in the scene that intersect the given shape.
     * @param s The shape
     * @return A list of shapes intersecting the given shape.
     */
    public List<IShape> select(IShape shape)
    {
        List<IShape> selected = new LinkedList<IShape>();
        for (IShape s : shapeList){
            if (s.intersects(shape)){
                selected.add(s);
            }
        }
        return selected;
    }
    
    /**
     * Add a shape to the scene.  It will be rendered next time
     * the draw() method is invoked.
     * @param s
     */
    public void addShape(IShape s) {
        shapeList.add(s);
    }
    
    /**
     * Remove a list of shapes from the given scene.
     * @param shapesToRemove
     */
    public void removeShapes(Collection<IShape> shapesToRemove) {
        shapeList.removeAll(shapesToRemove);
    }
    
    @Override   
    public String toString() {
        String shapeText = "";
        for (IShape s : shapeList) {
            shapeText += s.toString() + "\n";
        }
        return shapeText;
    }
    
    public void moveSelected(int dx, int dy)
    {
        for (IShape s : shapeList){
            if (s.isSelected()) s.move(dx, dy);
        }
    }

    public void loadFromFile(File selectedFile) throws IOException 
    {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'loadFromFile'");
        shapeList.clear();
        Scanner scan = new Scanner(new FileInputStream(selectedFile));

        while(scan.hasNext())
        {
            String shapeType = scan.next();
            if(shapeType.equalsIgnoreCase(ShapeType.SQUARE.toString())){
                //read square from this format:
                //SQUARE 515 251 100 RED false
                int x = scan.nextInt();
                int y = scan.nextInt();
                int side = scan.nextInt();
                String colorString = scan.next();
                boolean selected = scan.nextBoolean();

                Color color = Util.stringToColor(colorString);
                Square sq = new Square(color, x, y, side);
                sq.setSelected(selected);
                addShape(sq);


            } else if (shapeType.equalsIgnoreCase("CIRCLE")) {

            } else if (shapeType.equalsIgnoreCase("RECTANGLE")) {

            } else{
                throw new UnsupportedOperationException("Unknown Shape" + shapeType);
            }
        }
    }

    public Scene copy() {
        Scene copy = new Scene();
        for (IShape s : shapeList) {
            copy.addShape(s.copy()); //instead of adding the shape we are adding the copy of the shape
        }
        return copy;
    }

    public void reload(Scene other) {
        this.shapeList = other.shapeList;
    }

    //for Z ordering where we can control shape visibility bringing it forward or backward

    public void bringToFront(IShape shape) {
        if (shapeList.remove(shape)) {
            shapeList.add(shape); // Add to end = top layer
        }
    }
    
    public void sendToBack(IShape shape) {
        if (shapeList.remove(shape)) {
            shapeList.add(0, shape); // Add at start = bottom layer
        }
    }

    public IShape getSelectedShape() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'getSelectedShape'");

        for (IShape s : scene) {
            if (s.isSelected()) {
                return s;
            }
        }
        return null;
    }
    
    public void duplicateSelected() {
        for (IShape s : shapeList) {
            if (s.isSelected()) {
                IShape copy = s.copy();
                copy.move(100, 100); // So it's visible
                copy.setColor(new Color(
                    (int)(Math.random() * 256),
                    (int)(Math.random() * 256),
                    (int)(Math.random() * 256)
                ));
                shapeList.add(copy);
                break;
            }
        }
    }

    /*
 * In version 2 of my DrawShapes project, I added two new features:
 * 
 * 1. Z-ordering: This allows the user to change which shape appears on top.
 *    Pressing the 'f' key brings the selected shape to the front,
 *    and pressing 'b' sends it to the back.
 * 
 * 2. Duplicating shapes: When the user presses the 'x' key,
 *    the selected shape is duplicated. The new shape is slightly moved 
 *    and given a random color to make it easy to see.
 */




    
    
    
}
