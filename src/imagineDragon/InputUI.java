/*************************************************************************************************
 * CS 562 Project 
 * Author: Yujie Du(10372723), Chuanhui Zhang(10387654)
 * 
 * InputUI class is used to implement the GUI for the ESQL Database System. Therefore, it contains main() method.
 * 
 *************************************************************************************************/

package imagineDragon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InputUI extends JFrame{

	private static JFrame frame = new JFrame();//The UI Frame which contains four Panels
	
	/** Initialize variables for The NorthWest Panel **/
	private static JPanel panelNW = new JPanel(); //The NorthWest Panel
	private static JLabel urlLabel = new JLabel("url:"); //The Label for url
	private static JTextField urlTf = new JTextField(""); //The Textfield for url
	private static JLabel usrLabel = new JLabel("username:"); //The Label for username
	private static JTextField usrTf = new JTextField(""); //The TextField for username
	private static JLabel pwdLabel = new JLabel("password:"); //The Label for password
	private static JTextField pwdTf = new JTextField(""); //The TextField for password
	private static JButton connectNW = new JButton("connect"); //The submit button
	private static JButton clearNW = new JButton("clear"); //The clear button
	private static JLabel statusNW = new JLabel("Waiting for input"); //The status Label
	
	/** Initialize variables for the NorthEast Panel **/	
	private static JPanel panelNE = new JPanel(); //The NorthEast Panel
	private static JLabel selectLabel = new JLabel("SELECT ATTRIBUTE(S): "); //The Label for select attribute(s)
	private static JTextField selectTf = new JTextField(""); //The TextField for select attribute(s)
	private static JLabel gvLabel = new JLabel("NUMBER OF GROUPING VARIABLES(n): "); //The Label for grouping variable(s)
	private static JTextField gvTf = new JTextField(""); //The TextField for grouping variable(s)
	private static JLabel gaLabel = new JLabel("GROUPING ATTRIBUTES(V): "); //The Label for grouping attribute(s)
	private static JTextField gaTf = new JTextField(""); //The TextField for grouping attribute(s)
	private static JLabel afLabel = new JLabel("F-VECT([F]): "); //The Label for function-vector(s)
	private static JTextField afTf = new JTextField(""); //The TextField for function-vector(s)
	private static JLabel condLabel = new JLabel("SELECT CONDITION-VECT([\u03B4]): "); //The Label for select condition-vector(s)
	private static JTextField condTf = new JTextField(""); //The TextField for select condition-vector(s)
	private static JCheckBox isMinLoop = new JCheckBox("Min Loop");
	private static JCheckBox isOutputHere = new JCheckBox("Output Here");
	private static JButton submitNE = new JButton("submit");//The submit button
	private static JButton clearNE = new JButton("clear");//The clear button
	private static JButton executeNE = new JButton("execute");
	private static JLabel statusNE = new JLabel("Waiting for input"); //The status Label

	/** Initialize variables for The SouthWest Panel **/
	private static JPanel panelSW = new JPanel(); //The NorthWest Panel
	private static JButton stage1 = new JButton("stage1"); //button to change to stage1
	private static JButton stage2 = new JButton("stage2"); //button to change to stage2
	private static JLabel tnameLabel = new JLabel("tableName:"); //The Label for tableName
	private static JTextField tnameTf = new JTextField(""); //The Textfield for tableName
	private static JLabel schemaLabel = new JLabel("schema:"); //The Label for schema
	private static JTextField schemaTf = new JTextField(""); //The TextField for schema
	private static JButton executeSW = new JButton("execute"); //The execute button
	private static JButton clearSW = new JButton("clear"); //The clear button
	private static JLabel schemaStatusSW = new JLabel("Waiting for input"); //The status Label for schema
	private static JButton importDataSW = new JButton("Import Data"); //The import data button
	private static JButton importESQLSW = new JButton("Import ESQL"); //The import ESQL button
	private static JLabel importStatusSW = new JLabel("Status"); //The status Label for import data/ESQL
	
	/** Initialize variables for The SouthEast Panel **/
	private static JPanel panelSE = new JPanel(); //The SouthEast Panel
    private static JTextArea textAreaSE = new JTextArea(); //The TextArea for output
	private static JScrollPane paneSE = new JScrollPane(textAreaSE); //The ScrollPane which contains TextArea

	/** Initialize variables*/
	private static final long serialVersionUID = 8014699926065854589L;
	private static InputUI instance = null;
	private static DatabaseManager dm;
	
	private static void initUI() {
		/** Initialize the UI of frame **/
		frame = new JFrame("Simple ESQL Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(50, 50, 800, 400);
		
		initPanelNW();   //initialize the NorthWest Panel
		initPanelNE();   //initialize the NorthEast Panel
		initPanelSW();   //initialize the SouthWest Panel
		initPanelSE();   //initialize the SouthEast Panel

		frame.setLayout(new GridLayout(2, 1));
		frame.add(panelNW);
		frame.add(panelNE);
		frame.add(panelSW);
		frame.add(panelSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		addListeners();
		test();
	}
	
	private static void test(){ //method for test input data
		urlTf.setText("jdbc:postgresql://localhost:5432/postgres");
		usrTf.setText("postgres");
		pwdTf.setText("12345678");
		tnameTf.setText("sales");
		schemaTf.setText("create table sales (cust varchar(20), prod varchar(20), day int, month int, year int, state char(2), quant int);");
		selectTf.setText("cust, prod, avg_0_quant + max_1_state, min_1_state, sum_1_quant, avg_2_quant, count_3_state");
		gvTf.setText("3");
		gaTf.setText("cust, prod");
		afTf.setText("avg_0_quant, sum_1_quant, max_1_state, min_1_state, avg_2_quant, count_3_state");
		condTf.setText("state.compareTo(\"NJ\") > 0, state.compareTo(max_1_state) < 0 , quant > avg_2_quant+avg_0_quant");
	}

	private static void initPanelNW(){ //initialize the NorthWest Panel
		panelNW.setLayout(new GridLayout(6, 1));
		panelNW.add(urlLabel);
		panelNW.add(urlTf);
		panelNW.add(usrLabel);
		panelNW.add(usrTf);
		panelNW.add(pwdLabel);
		panelNW.add(pwdTf);
		panelNW.add(connectNW);
		panelNW.add(clearNW);
		panelNW.add(statusNW);
	}
	
	private static void initPanelNE(){ //initialize the NorthEast Panel
		panelNE.setLayout(new GridLayout(8, 1));
		panelNE.add(selectLabel);
		panelNE.add(selectTf);
		panelNE.add(gvLabel);
		panelNE.add(gvTf);
		panelNE.add(gaLabel);
		panelNE.add(gaTf);
		panelNE.add(afLabel);
		panelNE.add(afTf);
		panelNE.add(condLabel);
		panelNE.add(condTf);
		panelNE.add(isMinLoop);
		panelNE.add(isOutputHere);
		panelNE.add(submitNE);
		panelNE.add(clearNE);
		panelNE.add(executeNE);
		panelNE.add(statusNE);
	}
	
	private static void initPanelSW(){ //initialize the SouthWest Panel
		panelSW.setLayout(new GridLayout(7, 1));
		stage1.setEnabled(false);
		stage2.setEnabled(false);
		panelSW.add(stage1);
		panelSW.add(stage2);
		panelSW.add(tnameLabel);
		panelSW.add(tnameTf);
		panelSW.add(schemaLabel);
		panelSW.add(schemaTf);
		panelSW.add(executeSW);
		panelSW.add(clearSW);
		panelSW.add(schemaStatusSW);
		panelSW.add(new Label("")); //For spacing
		panelSW.add(importDataSW);
		panelSW.add(importESQLSW);
		panelSW.add(importStatusSW);
	}
	
	private static void initPanelSE(){ //initialize the SouthEast Panel
		panelSE.setLayout(new GridLayout(1, 0));
		panelSE.add(paneSE);
		textAreaSE.setEditable(false);
	}
	
	private static void addListeners() { //listener method for all buttons
		
		connectNW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String url = urlTf.getText();
				String usr = usrTf.getText();
				String pwd = pwdTf.getText();
				dm = new DatabaseManager(url, usr, pwd);
				String _statusNW = dm.connect();
				if(_statusNW.contains("Success connecting server!")) stageChanged(true);
				else { 
					stageChanged(false);
					stage2.setEnabled(false);
				}
				statusNW.setText(_statusNW);
			}
		});
		
		clearNW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				urlTf.setText("");
				usrTf.setText("");
				pwdTf.setText("");			
			}
		});
		
		submitNE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ArrayList<String> selAttrList = new ArrayList<String>(Arrays.asList(selectTf.getText().replaceAll(" ","").split(","))); //eliminate whitespace
				int numOfGv = Integer.parseInt(gvTf.getText().replaceAll(" ",""));
				ArrayList<String> gaList = new ArrayList<String>(Arrays.asList(gaTf.getText().replaceAll(" ","").split(",")));
				ArrayList<String> afList = new ArrayList<String>(Arrays.asList(afTf.getText().replaceAll(" ","").split(",")));
				ArrayList<String> condList = new ArrayList<String>(Arrays.asList(condTf.getText().replaceAll(" ","").split(",")));
				condList.add(0, "true");
				MFStructInfo mfsi = new MFStructInfo(selAttrList, numOfGv, gaList, afList, condList);
				String path = "./src/imagineDragon";
				long start = System.nanoTime();
				MFStructGenerator mfsg = new MFStructGenerator(path, mfsi);
				mfsg.createMFStruct();
				File fileTest=new File("./src/imagineDragon/MFStruct.java");
				if(isMinLoop.isSelected()) {
					boolean isZeroPresent = false;
					for(String af: afList) {
						if(af.contains("0")) {
							isZeroPresent = true;
							break;
						}
					}
					TopologicalSortWithUpdate tswu = new TopologicalSortWithUpdate(mfsi, numOfGv+1, isZeroPresent);
					tswu.run();
					MFMainMinLoopGenerator generator = new MFMainMinLoopGenerator(path, mfsi, dm, tswu, isOutputHere.isSelected());
					generator.run();
				} else {
					MFMainGenerator generator = new MFMainGenerator("./src/imagineDragon", mfsi, dm, isOutputHere.isSelected());
					generator.run();
				}
				File fileTest2=new File("./src/imagineDragon/MFMain.java");
				while((!fileTest.exists() || !fileTest2.exists())&& start < 1000000000){} 
				statusNE.setText(System.nanoTime() - start + "ns");
				//if(isOutputHere.isSelected()) executeNE.setEnabled(true);
				//else executeNE.setEnabled(false);
			}
		});
		
		
		clearNE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				selectTf.setText("");
				gvTf.setText("");
				gaTf.setText("");
				afTf.setText("");
				condTf.setText("");				
			}
		});
		
		executeNE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					long start = System.nanoTime();
					Class c = String[].class;
					URL classUrls = new File(".").toURI().toURL();
					URL[] urls = new URL[]{classUrls};
					URLClassLoader loader = new URLClassLoader(urls);
					Class mfMain =  Class.forName ("imagineDragon.MFMain", true, loader);
					//Class<?> mfMain = ClassLoader.getSystemClassLoader().loadClass("imagineDragon.MFMain");
					Method main = mfMain.getMethod("main", c);
					String[] params = null; // init params accordingly
					main.invoke(null, (Object) params); // static method doesn't have an instance
					statusNE.setText(System.nanoTime() - start + "ns");
					loader.close();
					System.gc();
				} catch (NoSuchMethodException | SecurityException
						| ClassNotFoundException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		stage1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				stageChanged(false);			
			}
		});
		
		stage2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				stageChanged(true);						
			}
		});
		
		executeSW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String tableName = tnameTf.getText();	
				String schema = schemaTf.getText();
				String _statusSW = dm.retrieve(tableName, schema);
				if(_statusSW.contains("Schema invalid!") || _statusSW.contains("No such table!")) schemaStatusSW.setText(_statusSW);
				else {
					schemaStatusSW.setText("Execute successfully");
					setTextAreaSE(_statusSW);
				}
			}
		});
		
		clearSW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				tnameTf.setText("");
				schemaTf.setText("");
			}
		});
		
		importDataSW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					 final JFileChooser fc = new JFileChooser();
					 fc.setCurrentDirectory(new File("."));
				     FileNameExtensionFilter filter = new FileNameExtensionFilter("sql", "SQL");
				     fc.setFileFilter(filter);
				     String path = fc.showOpenDialog(InputUI.getInstance()) == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile().toString() : "Error"; 
				     if(!path.equals("Error")) {
				    	 try {
				        	BufferedReader br = new BufferedReader(new FileReader(new File(path)));
				        	String s;
				       		StringBuffer sb = new StringBuffer();
				       		while((s = br.readLine()) != null)  
				            {  
				       			sb.append(s);  
				            }  
				            br.close();
				            importStatusSW.setText(dm.importData(sb.toString()));  
				    	 } catch (Exception e) {
				      		e.printStackTrace();
				       	}
				     }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		});
		
		importESQLSW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					 final JFileChooser fc = new JFileChooser();
					 fc.setCurrentDirectory(new File("."));
				     FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				     fc.setFileFilter(filter);
				     String path = fc.showOpenDialog(InputUI.getInstance()) == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile().toString() : "Error"; 
				     if(!path.equals("Error")) {
				    	 try {
				        	BufferedReader br = new BufferedReader(new FileReader(new File(path)));
				        	String s;
				       		int length = 0;
				       		String[] field = new String[5];
				       		while((s = br.readLine()) != null)  
				            {  
				       			field[length++] = s.replaceAll("\\s+","");
				            }  
				            br.close();
							selectTf.setText(field[0]);
							gvTf.setText(field[1]);
							gaTf.setText(field[2]);
							afTf.setText(field[3]);
							condTf.setText(field[4]);
				            importStatusSW.setText("Import Success");  
				    	 } catch (Exception e) {
				      		e.printStackTrace();
				       	}
				     }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		});
		
	}
	
	private static void lockPanelNW(boolean lock){ //whether to lock NorthWest Panel
		urlTf.setEnabled(lock);
		usrTf.setEnabled(lock);
		pwdTf.setEnabled(lock);
		connectNW.setEnabled(lock);
		clearNW.setEnabled(lock);
	}
	
	private static void lockPanelNE(boolean lock){ //whether to lock NorthEast Panel
		selectTf.setEnabled(lock);
		gvTf.setEnabled(lock);
		gaTf.setEnabled(lock);
		afTf.setEnabled(lock);
		condTf.setEnabled(lock);
		isMinLoop.setEnabled(lock);
		isOutputHere.setEnabled(false); // This checkbox is not used for the moment
		submitNE.setEnabled(lock);
		clearNE.setEnabled(lock);
	}
	
	private static void lockPanelSW(boolean lock){ //whether to lock SouthWest Panel
		tnameTf.setEnabled(lock);
		schemaTf.setEnabled(lock);
		executeSW.setEnabled(lock);
		clearSW.setEnabled(lock);
		importDataSW.setEnabled(lock);
		importESQLSW.setEnabled(lock);
	}

	public static void stageChanged(boolean stage) { //if stage is changed to another one, update the locks
		lockPanelNW(!stage);
		lockPanelNE(stage);
		lockPanelSW(stage);
		stage1.setEnabled(stage);
		stage2.setEnabled(!stage);
	}
	
	public static void setTextAreaSE(String text){ //set the text output for the SouthEast Panel
		textAreaSE.setText(text);
		Font font = new Font("Courier New",Font.PLAIN, 15);
		textAreaSE.setFont(font);
	}
	
	public static InputUI getInstance() { //Singleton Pattern
		if(instance == null) {
	         instance = new InputUI();
	    }
	    return instance;
	}
	
	protected InputUI(){ //Constructor
		initUI();
	}

	public static void main(String[] args){
		InputUI.getInstance();
		InputUI.stageChanged(false);
		stage2.setEnabled(false);
		executeNE.setEnabled(false);
	}
}
