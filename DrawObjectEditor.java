import java.awt.*;
import java.nio.charset.Charset;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.*;
import java.util.Random;
import java.util.StringTokenizer;
import java.io.*;

/**
 * @author fick dich
 * This Program is a GUI to perform drawing on a graph. The program would only take the latest pressed button's command
 * as the sole command for execution. For example, you pressed the triangle button and drew two points on the Panel.
 * Then you pressed select button. The program would end the drawing process and reset the temporary variables and 
 * proceeds to select command execution.
 */
public class DrawObjectEditor extends JFrame implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static Charset ascii = Charset.forName("US-ASCII");
	
	private JPanel jp;
	private MyDrawPanel dp;
	
	private Random randomGenerator = new Random();

	private JButton jb_line, jb_circle, jb_triangle, jb_quad, jb_select, jb_move, jb_delete, jb_copy, jb_random, jb_save, jb_load, jb_export, jb_import;
	private LineButtonListener lbl;
	private CircleButtonListener cbl;
	private TriangleButtonListener tbl;
	private QuadButtonListener qbl;
	private SelectButtonListener sbl;
	private MoveButtonListener mbl;
	private DeleteButtonListener dbl;
	private CopyButtonListener ccbl;
	private RandomButtonListener rbl;
	private SaveButtonListener ssbl;
	private LoadButtonListener llbl;
	private ExportButtonListener eebl;
	private ImportButtonListener iibl;
	
	private ArrayList<Line2D> dot = new ArrayList<Line2D> ();
	private ArrayList<Line2D> lines = new ArrayList<Line2D> ();
	private ArrayList<int []> line_color = new ArrayList<int []>();
 	private ArrayList<Ellipse2D.Double> circles = new ArrayList<Ellipse2D.Double>();
 	private ArrayList<int []> circle_color = new ArrayList<int []>();
	private ArrayList<Polygon> triangles = new ArrayList<Polygon>();
	private ArrayList<int []> triangle_color = new ArrayList<int []>();
	private ArrayList<Polygon> quads = new ArrayList<Polygon>();
	private ArrayList<int []> quad_color = new ArrayList<int []>();

	private int selected_shape = -1;
	private int selected_index = -1;

	private int X_coor;
	private int Y_coor;
	private int tri_x1,tri_x2,tri_y1,tri_y2;
	private int quad_x1,quad_x2,quad_y1,quad_y2,quad_x3,quad_y3;
	private boolean hasdot = false;
	
	/** Main method of the program
	 * @param args
	 */
	public static void main(String[] args) {
		DrawObjectEditor gui = new DrawObjectEditor();
		gui.go();
	}
	
	private void go() {
		setSize(400,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jb_line = new JButton("Line");
		lbl = new LineButtonListener();
		jb_line.addActionListener(lbl);

		jb_circle = new JButton("Circle");
		cbl = new CircleButtonListener();
		jb_circle.addActionListener(cbl);
		
		jb_triangle = new JButton("Triangle");
		tbl = new TriangleButtonListener();
		jb_triangle.addActionListener(tbl);
		
		jb_quad = new JButton("Quadrilateral");
		qbl = new QuadButtonListener();
		jb_quad.addActionListener(qbl);
		
		jb_select = new JButton("Select");
		sbl = new SelectButtonListener();
		jb_select.addActionListener(sbl);
		
		jb_move = new JButton("Move");
		mbl = new MoveButtonListener();
		jb_move.addActionListener(mbl);
		
		jb_delete = new JButton("Delete");
		dbl = new DeleteButtonListener();
		jb_delete.addActionListener(dbl);
		
		jb_copy = new JButton("Copy");
		ccbl = new CopyButtonListener();
		jb_copy.addActionListener(ccbl);
		
		jb_random = new JButton("Random Color");
		rbl = new RandomButtonListener();
		jb_random.addActionListener(rbl);
		
		jb_save = new JButton("Save");
		ssbl = new SaveButtonListener();
		jb_save.addActionListener(ssbl);
		
		jb_load = new JButton("Load");
		llbl = new LoadButtonListener();
		jb_load.addActionListener(llbl);
		
		jb_export = new JButton("Export");
		eebl = new ExportButtonListener();
		jb_export.addActionListener(eebl);
		
		jb_import = new JButton("Import");
		iibl = new ImportButtonListener();
		jb_import.addActionListener(iibl);
		
		GridLayout gl = new GridLayout(4,4);
		jp = new JPanel();
		jp.setLayout(gl);
		jp.add(jb_line);
		jp.add(jb_circle);
		jp.add(jb_triangle);
		jp.add(jb_quad);
		jp.add(jb_select);
		jp.add(jb_move);
		jp.add(jb_delete);
		jp.add(jb_copy);
		jp.add(jb_random);
		jp.add(jb_save);
		jp.add(jb_load);
		jp.add(jb_export);
		jp.add(jb_import);
		
		dp = new MyDrawPanel();
		dp.addMouseListener(new Mouseclick());
		
		add(jp,BorderLayout.SOUTH);
		add(dp,BorderLayout.CENTER);

		disableFunction();
		setVisible(true);
		repaint();
	}

	private class LineButtonListener implements ActionListener{

		private boolean pressed = false;

		/**
		 * take action when button is pressed
		 */
		 public void actionPerformed(ActionEvent ae){
			if (pressed == false) {
				setDefaultShapeButton();
				pressed = true;
				enableallShape();
				disableFunction();
				jb_select.setEnabled(true);
				jb_line.setEnabled(false);
			}
			repaint();
		}
	}

	private class CircleButtonListener implements ActionListener{

		private boolean pressed = false;
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			if (pressed == false) {
				setDefaultShapeButton();
				pressed = true;
				enableallShape();
				disableFunction();
				jb_select.setEnabled(true);
				jb_circle.setEnabled( false );
			}
			repaint();
		}
	}

	private class TriangleButtonListener implements ActionListener{

		private int counter = 0;

		private boolean pressed = false;
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			if (counter == 0 && pressed == false) {
				setDefaultShapeButton();
				pressed = true;
				enableallShape();
				disableFunction();
				jb_select.setEnabled(true);
				jb_triangle.setEnabled( false );
			}
			repaint();
		}
	}

	private class QuadButtonListener implements ActionListener{

		private int counter = 0;

		private boolean pressed = false;
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			if (counter == 0 && pressed == false) {
				setDefaultShapeButton();
				pressed = true;
				enableallShape();
				disableFunction();
				jb_select.setEnabled(true);
				jb_quad.setEnabled( false );
			}
			repaint();
		}
	}

	private class SelectButtonListener implements ActionListener{

		private boolean pressed = false;
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			if (pressed == false) {
				setDefaultShapeButton();
				enableallShape();
				pressed = true;
				jb_select.setEnabled( false );
				disableFunction();
			}
			repaint();
		}
	}
	
	private class MoveButtonListener implements ActionListener{

		private boolean pressed = false;
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			if (selected_shape != -1) {
				disableFunction();
				sbl.pressed = false;
				pressed = true;
			}
		}
	}
	
	private class DeleteButtonListener implements ActionListener{
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			if (selected_shape != -1) {
				switch (selected_shape) {
				case (0):
					lines.remove(selected_index);
					line_color.remove(selected_index);
					break;
				case (1):
					circles.remove(selected_index);
					circle_color.remove(selected_index);
					break;
				case (2):
					triangles.remove(selected_index);
					triangle_color.remove(selected_index);
				case (3):
					quads.remove(selected_index);
					quad_color.remove(selected_index);
					break;
				}
				repaint();
				jb_select.setEnabled( true );
				setDefaultShapeButton();
				disableFunction();
			}
		}
	}
	
	private class CopyButtonListener implements ActionListener{
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			if (selected_shape != -1) {
				switch (selected_shape) {
				case (0):
					Line2D.Double newline = new Line2D.Double(lines.get(selected_index).getX1()-10, lines.get(selected_index).getY1()-10, lines.get(selected_index).getX2()-10, lines.get(selected_index).getY2()-10);
					lines.add(newline);
					line_color.add(copyColorArray(line_color.get(selected_index)));
					break;
				case (1):
					Ellipse2D.Double newcircle = new Ellipse2D.Double(circles.get(selected_index).x-10,circles.get(selected_index).y-10,circles.get(selected_index).height,circles.get(selected_index).height);
					circles.add(newcircle);
					circle_color.add(copyColorArray(circle_color.get(selected_index)));
					break;
				case (2):
					int [] x_temp = new int[3];
					int [] y_temp = new int[3];
					for (int i=0; i<3;i++) {
						x_temp[i] = triangles.get(selected_index).xpoints[i] -10;
						y_temp[i] = triangles.get(selected_index).ypoints[i] -10;
					}
					Polygon newtriangle = new Polygon(x_temp, y_temp,3);
					triangles.add(newtriangle);
					triangle_color.add(copyColorArray(triangle_color.get(selected_index)));
					break;
				case (3):
					int [] x_array = new int[4];
					int [] y_array = new int[4];
					for (int i=0; i<4;i++) {
						x_array[i] = quads.get(selected_index).xpoints[i] - 10;
						y_array[i] = quads.get(selected_index).ypoints[i] - 10;
					}
					Polygon newquad = new Polygon(x_array, y_array,4);
					quads.add(newquad);
					quad_color.add(copyColorArray(quad_color.get(selected_index)));
					break;
				}
				repaint();
				jb_select.setEnabled( true );
				setDefaultShapeButton();
				disableFunction();
			}
		}
	}
	
	private class RandomButtonListener implements ActionListener{
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			if (selected_shape != -1) {
				switch (selected_shape) {
				case (0):
					getRandomColor(line_color.get(selected_index));
					break;
				case (1):
					getRandomColor(circle_color.get(selected_index));
					break;
				case (2):
					getRandomColor(triangle_color.get(selected_index));
					break;
				case (3):
					getRandomColor(quad_color.get(selected_index));
					break;
				}
				repaint();
				jb_select.setEnabled( true );
				setDefaultShapeButton();
				disableFunction();
			}
		}
	}
	
	private class SaveButtonListener implements ActionListener{
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			save(lines,line_color,circles,circle_color,triangles,triangle_color,quads,quad_color,"gg.object");
		}
	}
	
	private class LoadButtonListener implements ActionListener{
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			try {
				ArrayList tmp= load();
				if (tmp == null) return;
				lines=(ArrayList<Line2D>) tmp.get(0);
				line_color = (ArrayList<int[]>) tmp.get(1);
				circles=(ArrayList<java.awt.geom.Ellipse2D.Double>) tmp.get(2);
				circle_color=(ArrayList<int[]>) tmp.get(3);
				triangles= (ArrayList<Polygon>) tmp.get(4);
				triangle_color =(ArrayList<int[]>) tmp.get(5);
				quads=(ArrayList<Polygon>) tmp.get(6);
				quad_color=(ArrayList<int[]>) tmp.get(7);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setDefaultShapeButton();
			enableallShape();
			disableFunction();
			jb_select.setEnabled( true );
			repaint();
		}
	}
	
	private class ExportButtonListener implements ActionListener{
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			export();
		}
	}
	
	private class ImportButtonListener implements ActionListener{
		/**
		 * take action when button is pressed
		 */
		public void actionPerformed(ActionEvent ae){
			Import();
		}
	}
	
	private class Mouseclick implements MouseListener{

		private void addDot(MouseEvent e) {
			X_coor = e.getX();
			Y_coor = e.getY();
			Line2D ff = new Line2D.Double( e.getPoint (), e.getPoint () );
            dot.add(ff);
            repaint();
            hasdot = true;
		}
		
		/**
		 * useless method
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		/** This method handles meaningful input by the mouseclick on the drawing panel to create different
		 * shapes or lines object and add them the respectively ArrayList. Or to perform move and select functions
		 * @param e mouse click
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			//when line button is pressed and first mouse click onto the drawing panel
			if (lbl.pressed == true && hasdot == false) {
				addDot(e);
			}
			//when line button is pressed and second mouse click onto the drawing panel
			else if(lbl.pressed == true && hasdot == true) {
				Line2D gg = new Line2D.Double(X_coor,Y_coor,e.getX(),e.getY() );
				lines.add(gg);
				line_color.add(new int[] {0,0,0,0});
				setDefaultShapeButton();
				jb_select.setEnabled(true);
				jb_line.setEnabled(true);
				repaint();
			}
			//when circle button is pressed and first mouse click onto the drawing panel
			else if (cbl.pressed == true && hasdot == false) {
				addDot(e);
			}
			//when circle button is pressed and second mouse click onto the drawing panel
			else if(cbl.pressed == true && hasdot == true) {
				double k = Math.sqrt((e.getX()- X_coor)*(e.getX()- X_coor) + (e.getY()-Y_coor)*(e.getY()-Y_coor));
				Ellipse2D.Double gg = new Ellipse2D.Double(X_coor-k,Y_coor-k,k*2,k*2);
				circles.add(gg);
				circle_color.add(new int[] {0,0,0,0});
				setDefaultShapeButton();
				jb_select.setEnabled(true);
				jb_circle.setEnabled(true);
				repaint();
			}
			//when triangle button is pressed and first mouse click onto the drawing panel
			else if (tbl.counter == 0 && hasdot == false && tbl.pressed == true) {
				addDot(e);
				tri_x1 = e.getX();
				tri_y1 = e.getY();
				tbl.counter++;
			}
			//when triangle button is pressed and second mouse click onto the drawing panel
			else if (tbl.counter == 1 && hasdot == true) {
				addDot(e);
				tri_x2 = e.getX();
				tri_y2 = e.getY();
				tbl.counter++;
			}
			//when triangle button is pressed and third mouse click onto the drawing panel
			else if (tbl.counter == 2 && hasdot == true) {
				int[] x = {tri_x1,tri_x2,e.getX()};
				int[] y = {tri_y1,tri_y2,e.getY()};
				Polygon gg = new Polygon(x,y,3);
				triangles.add(gg);
				triangle_color.add(new int[] {0,0,0,0});
				setDefaultShapeButton();
				jb_select.setEnabled(true);
				jb_triangle.setEnabled(true);
				tbl.pressed = false;
				repaint();
			}
			//when quadrilateral button is pressed and first mouse click onto the drawing panel
			else if (qbl.counter == 0 && hasdot == false && qbl.pressed == true) {
				addDot(e);
				quad_x1 = e.getX();
				quad_y1 = e.getY();
				qbl.counter++;
			}
			//when quadrilateral button is pressed and second mouse click onto the drawing panel
			else if (qbl.counter == 1 && hasdot == true) {
				addDot(e);
				quad_x2 = e.getX();
				quad_y2 = e.getY();
				qbl.counter++;
			}
			//when quadrilateral button is pressed and third mouse click onto the drawing panel
			else if (qbl.counter == 2 && hasdot == true) {
				addDot(e);
				quad_x3 = e.getX();
				quad_y3 = e.getY();
				qbl.counter++;
			}
			//when quadrilateral button is pressed and forth mouse click onto the drawing panel
			else if (qbl.counter == 3 && hasdot == true) {
				int[] x = {quad_x1,quad_x2,quad_x3,e.getX()};
				int[] y = {quad_y1,quad_y2,quad_y3,e.getY()};
				Polygon gg = new Polygon(x,y,4);
				quads.add(gg);
				quad_color.add(new int[] {0,0,0,0});
				setDefaultShapeButton();
				jb_select.setEnabled(true);
				jb_quad.setEnabled(true);
				qbl.pressed = false;
				repaint();
			}
			//when select button is pressed and mouse click on the drawing panel
			else if(sbl.pressed == true) {
				for ( int i = 0; i < lines.size(); i ++ ){
					if (lines.get(i).intersects(e.getX()-2, e.getY()-2, 4, 4)) {
						Graphics2D g1 = (Graphics2D) dp.getGraphics();
						g1.setPaint ( Color.GREEN );
						g1.draw(lines.get(i));
						selected_index = i;
		            	selected_shape = 0;
					}
				}
				for ( int i = 0; i < circles.size(); i ++ ){
		            if (circles.get(i).contains(e.getX(),e.getY())) {
		            	Graphics2D g1 = (Graphics2D) dp.getGraphics();
		            	g1.setPaint ( Color.GREEN );
		            	g1.draw(circles.get(i));
		            	selected_index = i;
		            	selected_shape = 1;
		            }
		        }
		        for ( int i = 0; i < triangles.size(); i ++ ){
		        	if (triangles.get(i).contains(e.getX(),e.getY())) {
		        		Graphics2D g1 = (Graphics2D) dp.getGraphics();
		            	g1.setPaint ( Color.GREEN );
		            	g1.draw(triangles.get(i));
		            	selected_index = i;
		            	selected_shape = 2;
		        	}
		        }
		        for ( int i = 0; i < quads.size(); i ++ ){
		        	if (quads.get(i).contains(e.getX(),e.getY())) {
		        		Graphics2D g1 = (Graphics2D) dp.getGraphics();
		            	g1.setPaint ( Color.GREEN );
		            	g1.draw(quads.get(i));
		            	selected_index = i;
		            	selected_shape = 3;
		        	}
		        }
		        if (selected_shape != -1) {
			        sbl.pressed = false;
			        enableFunction();
		        }
		    ////when move button is pressed and mouse click onto the drawing panel
			}else if (mbl.pressed == true ) {
				double x_offset;
				double y_offset;
				switch (selected_shape) {
				case (0):
					double xl = lines.get(selected_index).getX2() - lines.get(selected_index).getX1();
					double yl = lines.get(selected_index).getY2() - lines.get(selected_index).getY1();
					lines.get(selected_index).setLine(e.getX()+xl/2, e.getY()+yl/2, e.getX()-xl/2, e.getY()-yl/2);
					break;
				case (1):
					circles.get(selected_index).setFrame(e.getX()-circles.get(selected_index).height/2,e.getY()-circles.get(selected_index).height/2,circles.get(selected_index).height,circles.get(selected_index).height);
					break;
				case (2):
					x_offset = (triangles.get(selected_index).xpoints[0] + triangles.get(selected_index).xpoints[1] + triangles.get(selected_index).xpoints[2])/3;
					y_offset = (triangles.get(selected_index).ypoints[0] + triangles.get(selected_index).ypoints[1] + triangles.get(selected_index).ypoints[2])/3;
					for (int i=0; i<3;i++) {
						triangles.get(selected_index).xpoints[i] = (int) (triangles.get(selected_index).xpoints[i] + e.getX() -x_offset);
						triangles.get(selected_index).ypoints[i] = (int) (triangles.get(selected_index).ypoints[i] + e.getY() -y_offset);
					}
					Polygon tt= new Polygon(triangles.get(selected_index).xpoints,triangles.get(selected_index).ypoints,3);
					triangles.set(selected_index,tt);
					break;
				case (3):
					x_offset = (quads.get(selected_index).xpoints[0] + quads.get(selected_index).xpoints[1] + quads.get(selected_index).xpoints[2]+quads.get(selected_index).xpoints[3])/4;
					y_offset = (quads.get(selected_index).ypoints[0] + quads.get(selected_index).ypoints[1] + quads.get(selected_index).ypoints[2]+quads.get(selected_index).ypoints[3])/4;
					for (int i=0; i<4;i++) {
						quads.get(selected_index).xpoints[i] = (int) (quads.get(selected_index).xpoints[i] + e.getX() -x_offset);
						quads.get(selected_index).ypoints[i] = (int) (quads.get(selected_index).ypoints[i] + e.getY() -y_offset);
					}
					Polygon dd= new Polygon(quads.get(selected_index).xpoints,quads.get(selected_index).ypoints,4);
					quads.set(selected_index,dd);
					break;
				}
				repaint();
				mbl.pressed = false;
				jb_select.setEnabled( true );
				setDefaultShapeButton();
				disableFunction();
			}
		}

		/**
		 * useless method
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * useless method
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * useless method
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	private void getRandomColor(int[] kk) {
		 kk[0] = randomGenerator.nextInt(256);
		 kk[1] = randomGenerator.nextInt(256);
		 kk[2] = randomGenerator.nextInt(256);
		 kk[3] = 255;
	}
	
	private void setDefaultShapeButton() {
		dot.clear();
		hasdot = false;
		lbl.pressed = false;
		cbl.pressed = false;
		tbl.pressed = false;
		tbl.counter = 0;
		qbl.pressed = false;
		qbl.counter = 0;
		selected_shape = -1;
		selected_index = -1;
		sbl.pressed =  false;
	}
	
	private void enableallShape() {
		jb_circle.setEnabled( true );
		jb_line.setEnabled( true );
		jb_quad.setEnabled( true );
		jb_triangle.setEnabled( true );
	}
	
	private void disableFunction() {
		jb_move.setEnabled( false );
		jb_delete.setEnabled( false );
		jb_copy.setEnabled( false );
		jb_random.setEnabled( false );
	}
	
	private void enableFunction() {
		jb_move.setEnabled( true );
		jb_delete.setEnabled( true );
		jb_copy.setEnabled( true );
		jb_random.setEnabled( true );
	}
	
	private int[] copyColorArray(int[] gg) {
		int [] kk = new int[4];
		System.arraycopy(gg, 0, kk, 0, 4);
		return kk;
	}
	
	private void save(Serializable line,Serializable linecolor,Serializable circle,Serializable circlecolor,Serializable triangle,
			Serializable trianglecolor,Serializable quad,Serializable quadcolor,String filename) {
		JFileChooser filechooser = new JFileChooser();
		filechooser.setDialogTitle("Save as");
		File fileToSave = null;
		int userSelection = filechooser.showSaveDialog(null);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    fileToSave = filechooser.getSelectedFile();
		    if (!fileToSave.getName().endsWith(".object")) {
		    	System.out.println("can only save to .object file");
		    	return;
		    } 
		    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		}
		else return;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileToSave);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(line);
			oos.writeObject(linecolor);
			oos.writeObject(circle);
			oos.writeObject(circlecolor);
			oos.writeObject(triangle);
			oos.writeObject(trianglecolor);
			oos.writeObject(quad);
			oos.writeObject(quadcolor);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to write file!");
			return;
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ArrayList load() throws Exception{
		JFileChooser filechooser = new JFileChooser();
		File fileToSave = null;
		ArrayList temp = new ArrayList();
		int userSelection = filechooser.showDialog(this, "Load");
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    fileToSave = filechooser.getSelectedFile();
		    if (!fileToSave.getName().endsWith(".object")) {
		    	System.out.println("can only load from .object files");
		    	return null;
		    } 
		    System.out.println("Load: " + fileToSave.getAbsolutePath());
		}
		else return null;
		FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
			fis = new FileInputStream(fileToSave);
			in = new ObjectInputStream(fis);
			temp.add((ArrayList) in.readObject());
			temp.add((ArrayList) in.readObject());
			temp.add((ArrayList) in.readObject());
			temp.add((ArrayList) in.readObject());
			temp.add((ArrayList) in.readObject());
			temp.add((ArrayList) in.readObject());
			temp.add((ArrayList) in.readObject());
			temp.add((ArrayList) in.readObject());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to load file!");
			return null;
		}
        fis.close();
		return temp;
	}
	
	private void export() {
		JFileChooser filechooser = new JFileChooser();
		filechooser.setDialogTitle("Save as");
		File fileToSave = null;
		int userSelection = filechooser.showSaveDialog(this);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    fileToSave = filechooser.getSelectedFile();
		    if (!fileToSave.getName().endsWith(".txt")) {
		    	System.out.println("can only save to .txt file");
		    	return;
		    } 
		    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		}
		else return;
		FileOutputStream kk = null;
		try {
			kk = new FileOutputStream(fileToSave);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
        Writer writer = new OutputStreamWriter(kk, ascii);
        
        for ( int i=0; i<lines.size();i++ ){
        	try {
                writer.write("line;"+lines.get(i).getX1()+";"+lines.get(i).getY1()+";"+lines.get(i).getX2()+";"+lines.get(i).getY2()+";");
                if (line_color.get(i)[3] == 0) writer.write("x;x;x;");
                else writer.write(line_color.get(i)[0]+";"+line_color.get(i)[1]+";"+line_color.get(i)[2]+";");
                	writer.write("\r\n");
            } catch (IOException e) {
    			e.printStackTrace();
    			System.out.println("writes file failed");
    		}
        }
        for ( int i=0; i<circles.size();i++ ){
        	try {
                writer.write("circle;"+circles.get(i).x+";"+circles.get(i).y+";"+circles.get(i).height/2+";");
                if (circle_color.get(i)[3] == 0) writer.write("x;x;x;");
                else writer.write(circle_color.get(i)[0]+";"+circle_color.get(i)[1]+";"+circle_color.get(i)[2]+";");
                	writer.write("\r\n");
            } catch (IOException e) {
    			e.printStackTrace();
    			System.out.println("writes file failed");
    		}
        }
        for ( int i=0; i<triangles.size();i++ ){
        	try {
                writer.write("triangle;"+triangles.get(i).xpoints[0]+";"+triangles.get(i).ypoints[0]+";"+triangles.get(i).xpoints[1]+";"+triangles.get(i).ypoints[1]+";"
                		+triangles.get(i).xpoints[2]+";"+triangles.get(i).ypoints[2]+";");
                if (triangle_color.get(i)[3] == 0)writer.write("x;x;x;");
                else writer.write(triangle_color.get(i)[0]+";"+triangle_color.get(i)[1]+";"+triangle_color.get(i)[2]+";");
                writer.write("\r\n");

            } catch (IOException e) {
    			e.printStackTrace();
    			System.out.println("writes file failed");
    		}
        }
        for ( int i=0; i<quads.size();i++ ){
        	try {
                writer.write("quadrilateral;4;"+quads.get(i).xpoints[0]+";"+quads.get(i).ypoints[0]+";"+quads.get(i).xpoints[1]+";"+quads.get(i).ypoints[1]+";"
                		+quads.get(i).xpoints[2]+";"+quads.get(i).ypoints[2]+";"+quads.get(i).xpoints[3]+";"+quads.get(i).ypoints[3]+";");
                if (quad_color.get(i)[3] == 0)writer.write("x;x;x;");
                else writer.write(quad_color.get(i)[0]+";"+quad_color.get(i)[1]+";"+quad_color.get(i)[2]+";");
                writer.write("\r\n");

            } catch (IOException e) {
    			e.printStackTrace();
    			System.out.println("writes file failed");
    		}
        }
        try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void Import() {
		JFileChooser fileloader = new JFileChooser();
		fileloader.setDialogTitle("Load");
		File filetoload = null;
		int userSelection1 = fileloader.showDialog(this, "Load");
		if (userSelection1 == JFileChooser.APPROVE_OPTION) {
			filetoload = fileloader.getSelectedFile();
		    if (!filetoload.getName().endsWith(".txt")) {
		    	System.out.println("can only load from .txt files");
		    	return;
		    } 
		    System.out.println("Save as file: " + filetoload.getAbsolutePath());
		}
		else return;
		FileReader fr=null;
		try {
			fr = new FileReader(filetoload);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("load file failed");
		}
		BufferedReader br=new BufferedReader(fr);
		String line;
		StringTokenizer st;
		lines.clear();
		line_color.clear();
		circles.clear();
		circle_color.clear();
		triangles.clear();
		triangle_color.clear();
		quads.clear();
		quad_color.clear();
		try {
			while((line=br.readLine())!=null) {
				st = new StringTokenizer(line,";");
				switch ((String)st.nextElement()) {
				case ("line"):
					Line2D gg = new Line2D.Double(Double.parseDouble((String)st.nextElement()),Double.parseDouble((String)st.nextElement()),Double.parseDouble((String)st.nextElement()),Double.parseDouble((String)st.nextElement()));
					lines.add(gg);
					String t = (String) st.nextElement();
					if (t.contentEquals("x")) line_color.add(new int[] {0,0,0,0});
					else line_color.add(new int[] {Integer.parseInt(t),Integer.parseInt( (String) st.nextElement()),Integer.parseInt((String) st.nextElement()),255});
					continue;
				case ("circle"):
					double x = Double.parseDouble((String)st.nextElement());
					double y = Double.parseDouble(st.nextToken());
					double radius = Double.parseDouble(st.nextToken());
					Ellipse2D.Double newcircle = new Ellipse2D.Double(x,y,radius*2,radius*2);
					circles.add(newcircle);
					String u = (String) st.nextElement();
					if (u.contentEquals("x")) circle_color.add(new int[] {0,0,0,0});
					else circle_color.add(new int[] {Integer.parseInt(u),Integer.parseInt( (String) st.nextElement()),Integer.parseInt((String) st.nextElement()),255});
					continue;
				case "triangle":
					int x1 = Integer.parseInt((String)st.nextElement());
					int y1 = Integer.parseInt((String)st.nextElement());
					int x2 = Integer.parseInt((String)st.nextElement());
					int y2 = Integer.parseInt((String)st.nextElement());
					int x3 = Integer.parseInt((String)st.nextElement());
					int y3 = Integer.parseInt((String)st.nextElement());
					Polygon newtriangle = new Polygon(new int[] {x1,x2,x3}, new int[] {y1,y2,y3},3);
					triangles.add(newtriangle);
					String q = (String) st.nextElement();
					if (q.contentEquals("x")) triangle_color.add(new int[] {0,0,0,0});
					else triangle_color.add(new int[] {Integer.parseInt(q),Integer.parseInt( (String) st.nextElement()),Integer.parseInt((String) st.nextElement()),255});
					continue;
				case "quadrilateral":
					st.nextElement();
					int xx1 = Integer.parseInt((String)st.nextElement());
					int yy1 = Integer.parseInt((String)st.nextElement());
					int xx2 = Integer.parseInt((String)st.nextElement());
					int yy2 = Integer.parseInt((String)st.nextElement());
					int xx3 = Integer.parseInt((String)st.nextElement());
					int yy3 = Integer.parseInt((String)st.nextElement());
					int xx4 = Integer.parseInt((String)st.nextElement());
					int yy4 = Integer.parseInt((String)st.nextElement());
					Polygon newquad = new Polygon(new int[] {xx1,xx2,xx3,xx4}, new int[] {yy1,yy2,yy3,yy4},4);
					quads.add(newquad);
					String p = (String) st.nextElement();
					if (p.contentEquals("x")) quad_color.add(new int[] {0,0,0,0});
					else quad_color.add(new int[] {Integer.parseInt(p),Integer.parseInt( (String) st.nextElement()),Integer.parseInt((String) st.nextElement()),255});
					continue;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to load file!");
			return;
		}
		try {
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
	}

	private class MyDrawPanel extends JPanel {
		/** This method is to draw all the stored lines and shapes at each graphic refresh
		 * @param g the Panel's graph
		 */
		public void paintComponent(Graphics g) {
			Graphics2D g2d = ( Graphics2D ) g;
	        g2d.setPaint ( Color.BLACK );
	        for ( Shape shape : dot ){
	            g2d.draw ( shape );
	        }
	        for ( int i=0; i<lines.size();i++ ){
	        	if (line_color.get(i)[3]==0) g2d.draw ( lines.get(i) );
	        	else {
	        		g2d.setColor(new Color(line_color.get(i)[0],line_color.get(i)[1],line_color.get(i)[2],line_color.get(i)[3]));
	        		g2d.draw ( lines.get(i) );
	        		g2d.setPaint ( Color.BLACK );
	        	}
	        }
	        for ( int i=0; i<circles.size();i++ ){
	        	if (circle_color.get(i)[3]==0) g2d.draw ( circles.get(i) );
	        	else {
	        		g2d.setColor(new Color(circle_color.get(i)[0],circle_color.get(i)[1],circle_color.get(i)[2],circle_color.get(i)[3]));
	        		g2d.draw ( circles.get(i) );
	        		g2d.fill(circles.get(i));
	        		g2d.setPaint ( Color.BLACK );
	        	}
	        }
	        for ( int i=0; i<triangles.size();i++ ){
	        	if (triangle_color.get(i)[3]==0) g2d.draw ( triangles.get(i) );
	        	else {
	        		g2d.setColor(new Color(triangle_color.get(i)[0],triangle_color.get(i)[1],triangle_color.get(i)[2],triangle_color.get(i)[3]));
	        		g2d.draw ( triangles.get(i) );
	        		g2d.fillPolygon(triangles.get(i));
	        		g2d.setPaint ( Color.BLACK );
	        	}
	        }
	        for ( int i=0; i<quads.size();i++ ){
	        	if (quad_color.get(i)[3]==0) g2d.draw ( quads.get(i) );
	        	else {
	        		g2d.setColor(new Color(quad_color.get(i)[0],quad_color.get(i)[1],quad_color.get(i)[2],quad_color.get(i)[3]));
	        		g2d.draw ( quads.get(i) );
	        		g2d.fillPolygon(quads.get(i));
	        		g2d.setPaint ( Color.BLACK );
	        	}
	        }
		}
	}
}