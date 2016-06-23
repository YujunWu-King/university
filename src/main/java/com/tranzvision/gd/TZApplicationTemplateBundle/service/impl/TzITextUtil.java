package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @author caoy
 * @version 创建时间：2016年6月20日 下午3:20:24 类说明
 */
public class TzITextUtil {
	public String getFieldValueAndType(String file) {
		String str_return = "";

		PdfReader reader = null;
		ByteArrayOutputStream bos = null;
		PdfStamper ps = null;
		AcroFields s = null;
		try {
			reader = new PdfReader(file);
			bos = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, bos);
			s = ps.getAcroFields();

			int i = 1;

			for (Iterator it = s.getFields().keySet().iterator(); it.hasNext(); ++i) {
				String name = (String) it.next();
				String value = s.getField(name);

				if (value == "")
					value = " ";
				int type = s.getFieldType(name);

				if ((type == 5) || (type == 6))
					type = 1;
				else {
					type = 0;
				}
				str_return = str_return + name + "∨∨" + value + "∨∨" + type + "∧∧";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				bos.close();
				reader.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str_return;
	}

	public String getPdfFileFields(String file) {
		String fields = "";
		PdfReader reader = null;
		ByteArrayOutputStream bos = null;
		PdfStamper ps = null;
		AcroFields s = null;
		try {
			reader = new PdfReader(file);
			bos = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, bos);
			s = ps.getAcroFields();
			int i = 1;
			for (Iterator it = s.getFields().keySet().iterator(); it.hasNext(); ++i) {
				String name = (String) it.next();
				System.out.println("name=" + name);
				fields = fields + name + ";";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				bos.close();
				reader.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fields;
	}

	public int isSelectByName(String file, String fieldName) {
		int int_type = 0;
		PdfReader reader = null;
		ByteArrayOutputStream bos = null;
		PdfStamper ps = null;
		AcroFields s = null;
		try {
			reader = new PdfReader(file);
			bos = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, bos);
			s = ps.getAcroFields();
			int_type = s.getFieldType(fieldName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				bos.close();
				reader.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		if ((int_type == 5) || (int_type == 6))
			int_type = 1;
		else {
			int_type = 0;
		}
		return int_type;
	}

	public String getValueByName(String file, String fieldName) {
		String fieldValue = "";
		PdfReader reader = null;
		ByteArrayOutputStream bos = null;
		PdfStamper ps = null;
		AcroFields s = null;
		try {
			reader = new PdfReader(file);
			bos = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, bos);
			s = ps.getAcroFields();
			fieldValue = s.getField(fieldName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				bos.close();
				reader.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fieldValue;
	}

	public boolean setFieldsValue(String file, String fieldsValue, ServletOutputStream out) {
		boolean result = true;
		PdfReader reader = null;
		ByteArrayOutputStream bos = null;
		PdfStamper ps = null;
		AcroFields fields = null;
		BaseFont bfChinese = null;
		String[] fieldsValueArray = (String[]) null;
		String[] fieldValueArray = (String[]) null;
		try {
			reader = new PdfReader(file);
			bos = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, bos);
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
			fields = ps.getAcroFields();
			fields.addSubstitutionFont(bfChinese);
			fieldsValueArray = fieldsValue.split("∧∧");
			for (int i = 0; i < fieldsValueArray.length; ++i) {
				fieldValueArray = fieldsValueArray[i].split("∨∨");
				if (fieldValueArray.length != 2) {
					continue;
				}
				fields.setField(fieldValueArray[0], fieldValueArray[1]);
			}
			ps.setFormFlattening(true);
			ps.close();
			out.write(bos.toByteArray());
			//out.close();
			//out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result = false;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				bos.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean setFieldsValue(String file, String fieldsValue, String outputfile) {
		boolean result = true;
		PdfReader reader = null;
		ByteArrayOutputStream bos = null;
		PdfStamper ps = null;
		AcroFields fields = null;
		BaseFont bfChinese = null;
		FileOutputStream fos = null;
		String[] fieldsValueArray = (String[]) null;
		String[] fieldValueArray = (String[]) null;
		try {
			reader = new PdfReader(file);
			bos = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, bos);
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
			fields = ps.getAcroFields();
			fields.addSubstitutionFont(bfChinese);
			fieldsValueArray = fieldsValue.split("∧∧");
			for (int i = 0; i < fieldsValueArray.length; ++i) {
				fieldValueArray = fieldsValueArray[i].split("∨∨");
				if (fieldValueArray.length != 2) {
					continue;
				}
				fields.setField(fieldValueArray[0], fieldValueArray[1]);
				// if (fieldValueArray.length != 3)
				// continue;
				// int type = fields.getFieldType(fieldValueArray[0]);
				// int lineLength = Integer.parseInt(fieldValueArray[2]);
				// if ((lineLength > 0) && (type == 4) &&
				// (Util.getLengthString(fieldValueArray[1]) > lineLength)) {
				// String strTemp = Util.addBreak(fieldValueArray[1],
				// lineLength); // 猜想加上换行
				// fields.setField(fieldValueArray[0], strTemp);
				// } else {
				// fields.setField(fieldValueArray[0], fieldValueArray[1]);
				// }
			}
			ps.setFormFlattening(true);
			ps.close();
			fos = new FileOutputStream(outputfile);
			fos.write(bos.toByteArray());
			fos.close();
		} catch (FileNotFoundException e) {
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				bos.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String addBreak(String str, int i) {
		int size = str.length();
		int m = size / i;
		StringBuilder sb = new StringBuilder(str);
		for (int n = 1; n <= m; n++) {
			if (n == 1) {
				sb.insert(i, "\n");
			} else {
				sb.insert(i * n + n - 1, "\n");
			}
		}
		return sb.toString();

	}

	public void mergePdfFiles(String[] files, String savepath) {
		try {
			Document document = new Document(new PdfReader(files[0]).getPageSize(1));

			PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));

			document.open();

			for (int i = 0; i < files.length; ++i) {
				PdfReader reader = new PdfReader(files[i]);

				int n = reader.getNumberOfPages();

				for (int j = 1; j <= n; ++j) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
			}
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void partitionPdfFile(String filepath, int N) {
		Document document = null;
		PdfCopy copy = null;
		try {
			PdfReader reader = new PdfReader(filepath);

			int n = reader.getNumberOfPages();

			if (n < N) {
				System.out.println("The document does not have " + N + " pages to partition !");
				return;
			}

			int size = n / N;
			String staticpath = filepath.substring(0, filepath.lastIndexOf("\\") + 1);
			String savepath = null;
			ArrayList savepaths = new ArrayList();
			for (int i = 1; i <= N; ++i) {
				if (i < 10) {
					savepath = filepath.substring(filepath.lastIndexOf("\\") + 1, filepath.length() - 4);
					savepath = staticpath + savepath + "0" + i + ".pdf";
					savepaths.add(savepath);
				} else {
					savepath = filepath.substring(filepath.lastIndexOf("\\") + 1, filepath.length() - 4);
					savepath = staticpath + savepath + i + ".pdf";
					savepaths.add(savepath);
				}
			}

			for (int i = 0; i < N - 1; ++i) {
				document = new Document(reader.getPageSize(1));
				copy = new PdfCopy(document, new FileOutputStream(savepaths.get(i).toString()));
				document.open();
				for (int j = size * i + 1; j <= size * (i + 1); ++j) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
				document.close();
			}

			document = new Document(reader.getPageSize(1));
			copy = new PdfCopy(document, new FileOutputStream(savepaths.get(N - 1).toString()));
			document.open();
			for (int j = size * (N - 1) + 1; j <= n; ++j) {
				document.newPage();
				PdfImportedPage page = copy.getImportedPage(reader, j);
				copy.addPage(page);
			}
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TzITextUtil t = new TzITextUtil();
		System.out.println(t.addBreak("上海交通大学安泰2015年_MBA考生申请表", 3));
		// String a =
		// t.getPdfFileFields("d://金鹰：上海交通大学安泰2015年_MBA考生申请表（解析）.pdf");
		// System.out.println(a);
		// String[] b = a.split(";");
		// for (int i = 0; i < b.length; i++) {
		// System.out.println(b[i].length());
		// }
		//
		// System.out.println("222222222222");
		// System.out.println(t.getPdfFileFields("d://GitLab使用介绍.pdf"));
		// Map<String, Object> mapTemp = new HashMap<String, Object>();
		// System.out.println(mapTemp.get("11"))
		//
		// com.lowagie.text.pdf.codec
	}

}
