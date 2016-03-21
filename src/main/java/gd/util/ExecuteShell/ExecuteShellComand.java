package gd.util.ExecuteShell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteShellComand {
	
	/**
	 * 在服务器执行exe、shell命令
	 * 
	 * @param command 要执行的命令字符串
	 * @return
	 */
	public String executeCommand(String command) {
		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}

	/**
	 * 将header.html、footer.html、报名表实例HTML转换成PDF格式文件
	 * 
	 * @param parentPath 	header.html文件路径
	 * @param path 			源文件路径
	 * @param sourceName 	源文件名称
	 * @param targetName 	模板文件名称
	 * @param pdfPath 		目标文件路径
	 * @return
	 */
	public String executeCommand(String parentPath, String path, String sourceName, String targetName, String pdfPath) {

		StringBuffer command = new StringBuffer("D:\\wkhtmltopdf\\bin\\wkhtmltopdf.exe ");
		command.append(" --header-html " + parentPath + "header.html ");
		command.append(" --footer-html " + parentPath + "footer.html ");
		command.append(path + sourceName);
		command.append(" " + pdfPath + targetName);

		System.out.println(command.toString());
		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command.toString());
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}
}
