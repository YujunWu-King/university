package com.tranzvision.gd.util.ExecuteShell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteShellComand {

	/**
	 * 在服务器执行exe、shell命令
	 * 
	 * @param command
	 *            要执行的命令字符串
	 * @return
	 */
	public String executeCommand(String command[]) {
		StringBuffer output = new StringBuffer();
		try {

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(true);
			Process process = pb.start();
			BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			// not "process.getInputStream()"
			String line = inStreamReader.readLine();
			while (line != null) {
				// System.out.println(line); //or whatever else
				output.append(line + "\n");
				line = inStreamReader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}

}
