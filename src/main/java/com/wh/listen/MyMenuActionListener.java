package com.wh.listen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.swing.dialog.CustomDefaultDialog;
import com.swing.menu.MenuUtil2;
import com.swing.messagebox.GUIUtil23;
import com.wh.ShellMainApp;

/***
 * 设置新的密码
 * 
 * @author huangwei
 * 
 */
public class MyMenuActionListener implements ActionListener {
	private ShellMainApp manualApp;

	public MyMenuActionListener(ShellMainApp manualApp) {
		super();
		this.manualApp = manualApp;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals(MenuUtil2.ACTION_HELP)) {
			System.out.println(MenuUtil2.ACTION_HELP);
//			GUIUtil23.infoDialog("联系邮箱:huangwei@yunmasoft.com");
			CustomDefaultDialog customDefaultDialog = new CustomDefaultDialog(
                    "命令行工具", null,false);
            customDefaultDialog.setVisible(true);
		}else if (command.equals(MenuUtil2.ACTION_STR_EXIT)) {// 退出应用程序
			manualApp.dispose();
			System.exit(0);
		}else if (command.equals("同时执行")) {// 退出应用程序
			manualApp.execute();
		}
	}
}
