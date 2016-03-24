package com.tranzvision.gd.util.ExecuteShell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class ExecuteShellComand {

	/**
	 * 在服务器执行exe、shell命令
	 * 
	 * @param command
	 *            要执行的命令字符串
	 * @return
	 */
	public int executeCommand(String command) {
		StringBuffer output = new StringBuffer();
		int exitVal = 1;
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			
//            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");  
//            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");  
//            errorGobbler.start();  
//            outputGobbler.start();  
//            exitVal = p.waitFor();  
			
			LineNumberReader reader = new LineNumberReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
				exitVal = p.exitValue();
			}
			reader.close();
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return exitVal;

	}

	/**
	 * 将header.html、footer.html、报名表实例HTML转换成PDF格式文件
	 * 
	 * @param parentPath
	 *            header.html、footer.html文件路径
	 * @param sourcePath
	 *            源文件路径(报名表实例对应的HTML)
	 * @param sourceName
	 *            报名表实例对应的HTML文件名称
	 * @param targetName
	 *            要生产的PDF文件名称
	 * @param pdfPath
	 *            PDF的绝对路
	 * @return
	 */
	public String executeCommand(String parentPath, String sourcePath, String sourceName, String targetName,
			String pdfPath) {

		// StringBuffer command = new
		// StringBuffer("D:\\wkhtmltopdf\\bin\\wkhtmltopdf.exe ");
		StringBuffer command = new StringBuffer("wkhtmltopdf.exe ");
		command.append(" --header-html " + parentPath + "header.html ");
		command.append(" --footer-html " + parentPath + "footer.html ");
		command.append(sourcePath + sourceName);
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
