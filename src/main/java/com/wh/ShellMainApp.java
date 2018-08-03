package com.wh;

import com.cmd.dos.hw.ShellScriPanel;
import com.cmd.dos.hw.util.CMDUtil;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;
import com.io.hw.json.HWJacksonUtils;
import com.swing.dialog.DialogUtil;
import com.swing.dialog.GenericFrame;
import com.swing.menu.MenuUtil2;
import com.swing.messagebox.GUIUtil23;
import com.wh.listen.MyMenuActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ShellMainApp extends GenericFrame  {

	private static final long serialVersionUID = 155530167303425421L;
	private JPanel contentPane;
	private ShellScriPanel shell1;
	/***
	 * 配置文件路径
	 */
	public static final String configFilePath=System.getProperty("user.home")
			+File.separator+"conf"+File.separator+ ".shell_cmd.properties";
	private List<String>[] cmdLists=new List[2];
	private ShellScriPanel shell2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShellMainApp frame = new ShellMainApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
/*	private void setIcon(){
		ImageIcon icon = null;
		try {
			URL url = new URL(
					);
			icon = new ImageIcon(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			GUIUtil23.warningDialog(e.getMessage());
		}
		if (null != icon) {
			this.setIconImage(icon.getImage());
		}
	}*/
	/**
	 * Create the frame.
	 */
	public ShellMainApp() {
//		setIcon();
		initConfig();
		DialogUtil.lookAndFeel2();
		this.setTitle("命令行工具");// Shell script executor
		init33(this);
		try {
			setIcon("/com/wh/img/logo.png",this.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		globalShortcutKeys();
		setMenu();
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(1200, 600);
		Dimension framesize = this.getSize();
		int x = (int) screensize.getWidth() / 2 - (int) framesize.getWidth()
				/ 2;
		int y = (int) screensize.getHeight() / 2 - (int) framesize.getHeight()
				/ 2;
		this.setLocation(x, y);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		JPanel centerPanel=new JPanel();
		centerPanel.setLayout(new GridLayout(1, 2, 5, 0));
		shell1=new ShellScriPanel(null,"执行结果",null){
			private static final long serialVersionUID = 5697893283117817058L;
			@Override
			public void callback2(String cmd) {
				System.out.println("cmd:"+cmd);
				addCmdHistory1(cmd);
			}
			};
		shell2=new ShellScriPanel(null,"执行结果",null){
			private static final long serialVersionUID = 5697893283117817052L;
			@Override
			public void callback2(String cmd) {
				System.out.println("cmd:"+cmd);
				addCmdHistory2(cmd);
			}
			};
		centerPanel.add(shell1);
		shell1.getShScriptTF().addKeyListener(new KeyAdapter() {
			private int index=0;
			
			@Override
			public void keyPressed(KeyEvent e) {
				int length=cmdLists[0].size();
				if(e.getKeyCode()==KeyEvent.VK_UP){
					if(length-index>0){
						index++;
					}
					shell1.getShScriptTF().setText(cmdLists[0].get(length-index<0?0:(length-index)));
					
				}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
					if(index>1){
						index--;
					}else{
						index=1;
					}
					shell1.getShScriptTF().setText(cmdLists[0].get(length-index));
				}
			}
		});
		shell2.getShScriptTF().addKeyListener(new KeyAdapter() {
			private int index=0;
			
			@Override
			public void keyPressed(KeyEvent e) {
				int length=cmdLists[1].size();
				if(e.getKeyCode()==KeyEvent.VK_UP){
					if(length-index>0){
						index++;
					}
					shell2.getShScriptTF().setText(cmdLists[1].get(length-index<0?0:(length-index)));
					
				}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
					if(index>1){
						index--;
					}else{
						index=1;
					}
					shell2.getShScriptTF().setText(cmdLists[1].get(length-index));
				}
			}
		});
		centerPanel.add(shell2);
		contentPane.add(centerPanel);
		
		JPanel bottomPane = new JPanel();
		JLabel systemEncodingLabel = new JLabel();
		systemEncodingLabel
				.setText("<html>current os : <font color=\"red\">"
						+ SystemHWUtil.OSNAME
						+ "</font> &nbsp;&nbsp;&nbsp;&nbsp; current java encoding:<font color=\"red\">"
						+ SystemHWUtil.CURR_ENCODING + "</font></html>");
		bottomPane.add(systemEncodingLabel);
		contentPane.add(bottomPane, BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			readConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void initConfig() {
		if(cmdLists[0]==null){
			cmdLists[0]=new ArrayList<String>();
		}
		if(cmdLists[1]==null){
			cmdLists[1]=new ArrayList<String>();
		}
	}
	/***
	 * 增加菜单
	 */
	private void setMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileM = new JMenu("File");
//		JMenuItem fileM_test = new JMenuItem("测试");
//		fileM_test.setActionCommand("test");
//		fileM.add(fileM_test);

		JMenuItem fileM_execute = new JMenuItem("同时执行");
		fileM.add(fileM_execute);
		
		JMenuItem fileM_exit = new JMenuItem(MenuUtil2.ACTION_STR_EXIT);
		fileM_exit.setActionCommand(MenuUtil2.ACTION_STR_EXIT);
		fileM.add(fileM_exit);

		menuBar.add(fileM);

		MyMenuActionListener myMenuListener = new MyMenuActionListener(this);

		fileM_execute.addActionListener(myMenuListener);
		fileM_exit.addActionListener(myMenuListener);

		JMenuItem helpM = new JMenuItem("帮助");
		helpM.setActionCommand(MenuUtil2.ACTION_HELP);
		helpM.addActionListener(myMenuListener);
		menuBar.add(helpM);
		this.setJMenuBar(menuBar);
	}
	/***
	 * 设置全局快捷键,按Alt+r ,则命令输入框自动聚焦
	 */
	private void globalShortcutKeys(){
		//Add global shortcuts
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// 注册应用程序全局键盘事件, 所有的键盘事件都会被此事件监听器处理.
		toolkit.addAWTEventListener(
				new java.awt.event.AWTEventListener() {
					

					@Override
					public void eventDispatched(AWTEvent event) {
						if (event.getClass() == KeyEvent.class) {
							KeyEvent kE = ((KeyEvent) event);
							// 处理按键事件 Ctrl+Enter
							if ((kE.getKeyCode() == KeyEvent.VK_R)
									&& (((InputEvent) event)
											.isAltDown())&& kE.getID() == KeyEvent.KEY_PRESSED) {
								System.out.println("Alt+r");
								shell1.getShScriptTF().requestFocus();
							} }
						
					}
				}, java.awt.AWTEvent.KEY_EVENT_MASK);

	}
	/***
	 * 读取配置文件
	 * @throws IOException
	 */
	private void readConfig() throws IOException{
		configFile=new File(configFilePath);
		if(configFile.exists()){
			InputStream inStream=new FileInputStream(configFile);
			String input=FileUtils.getFullContent4(inStream, SystemHWUtil.CHARSET_UTF);
			resume(input);
			inStream.close();//及时关闭资源
		}
	}
	
	@Override
	public void beforeDispose() {
		super.beforeDispose();
        saveConfig();
    }

    /***
	    * 保存到配置文件中
		 * @throws IOException 
	    */
    public void saveConfig() {
        configFile=new File(configFilePath);
	       if(!configFile.exists()){
	           try {
	               SystemHWUtil.createEmptyFile(configFile);
	           } catch (IOException e) {
	               e.printStackTrace();
	               GUIUtil23.errorDialog(e);
	           }
	       }
	       CMDUtil.show(configFilePath);//因为隐藏文件是只读的
	       //处理
	       System.out.println("保存文件:"+configFilePath);
	       FileUtils.writeToFile(configFile, HWJacksonUtils.getJsonP(cmdLists), SystemHWUtil.CHARSET_UTF);
	       CMDUtil.hide(configFilePath);
	   }
	private void resume(String input) {
		cmdLists=(List[]) HWJacksonUtils.deSerialize(input, List[].class);
	}
	private void addCmdHistory1(String cmd){
		SystemHWUtil.addElement(cmdLists[0], cmd);
	}
	private void addCmdHistory2(String cmd){
		SystemHWUtil.addElement(cmdLists[1], cmd);
	}
	public void execute(){
		shell1.exeCommandAction();
		shell2.exeCommandAction();
	}
}
