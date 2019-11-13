package com.tranzvision.gd.util.poi.word;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取“导出word模板”的工具类
 *
 * @author JZ
 * @since 2019-04-03
 */

public class WordUtil {


    public static StringBuffer builder;

    /**
     * 封装打开文档方法
     *
     * @param inputUrl 文档路径
     * @return POI文档对象
     */
    public static CustomXWPFDocument openDocument(String inputUrl) {
        CustomXWPFDocument xdoc = null;
        InputStream is = null;
        try {
            is = new FileInputStream(inputUrl);
            xdoc = new CustomXWPFDocument(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xdoc;
    }

    /**
     * 将Base64文件保存在本地路径
     *
     * @param base64str base64字符串
     * @param savepath  保存路径
     * @return
     */
    public static boolean generateImage(String base64str, String savepath) {   //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(savepath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取图片的宽高
     */
    public static int[] getImgProperty(String path) {
        int[] pro = new int[2];
        try {
            File picture = new File(path);
            BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
            pro[0] = sourceImg.getWidth();
            pro[1] = sourceImg.getHeight();
            return pro;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
    }

    /**
     * 删除文件
     *
     * @param fileName 删除文件
     * @return 是否成功
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        System.out.println("我开始删除文件了！");
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            System.gc();
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 根据模板生成新word文档
     *
     * @param inputUrl  模板存放地址
     * @param outputUrl 新文档存放地址
     * @param dataList  需要替换的信息集合
     * @return 返回true创建成功，返回False返回失败
     */
    public static String createWord(String inputUrl, String outputUrl,
                                    List<Map<String, Object>> dataList, Boolean convertPDF) {
        builder =new StringBuffer();
        System.out.println("Merge Word has running;");
        try {
            String[] strs = outputUrl.split("\\.");
            Map<String, Object> imgInfo = new HashMap<>();
            List<String> imgUrl = new ArrayList<>();
            String path = "";
            int imgCount = 0;
            imgInfo.put("imgUrl", imgUrl);
            imgInfo.put("path", strs[0]);
            //模板转换默认成功
            List<String> urls;
            //替换Url格式;
            //判断需要生成几个文档
            int size = dataList.size();
            if (size >= 2) {
                urls = new ArrayList<>();
                for (int i = 0; i < dataList.size(); i++) {
                    changeWord(inputUrl, strs[0] + (i + 1) + ".docx", dataList.get(i), imgInfo);
                    urls.add(strs[0] + (i + 1) + ".docx");
                }
                mergeWord(outputUrl, urls);
                for (String url : urls) {
                    deleteFile(url);
                }
            } else if (size == 1) {
                outputUrl = outputUrl;
                changeWord(inputUrl, outputUrl, dataList.get(0), imgInfo);
            }
            //休眠1秒,以便gc回收内存。
            Thread.sleep(1000);
            for (String url : imgUrl) {
                deleteFile(url);
            }
            //如果需要生成PDF格式。
            if (convertPDF) {
                convertPdf(outputUrl);
                deleteFile(outputUrl);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * 将word转换为PDF
     *
     * @param docPath
     */
    public static void convertPdf(String docPath) {
        String pdfPath = "";
        String[] strs = docPath.split("\\.");
        for(int i =0;i<strs.length;i++) {
            if(i<strs.length-1)pdfPath = pdfPath+strs[i]+".";
        }
        pdfPath = pdfPath +"pdf";
        System.out.println("pdfpath:"+pdfPath);
        XWPFDocument document;
        InputStream doc = null;
        OutputStream out = null;
        try {

            doc = new FileInputStream(docPath);
            document = new XWPFDocument(doc);
            PdfOptions options = PdfOptions.create();
            out = new FileOutputStream(pdfPath);
            PdfConverter.getInstance().convert(document, out, options);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (doc != null) doc.close();
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并文档
     *
     * @param outUrl 输出文档地址
     * @param urls   需要合并的文档地址
     */
    public static void mergeWord(String outUrl, List<String> urls) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(outUrl);
            XWPFDocument src1Document = openDocument(urls.get(0));
            XWPFParagraph p = src1Document.createParagraph();
            p.setPageBreak(true);
            for (int i = 1; i < urls.size(); i++) {
                XWPFDocument srcDocument = openDocument(urls.get(i));
                if (i != urls.size() - 1) {
                    XWPFParagraph p1 = srcDocument.createParagraph();
                    p1.setPageBreak(true);
                }
                appendBody(src1Document, srcDocument);
            }
            src1Document.write(stream);
            builder.append("文档合并完成;");
            builder.append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //合并文档
    public static void appendBody(XWPFDocument src, XWPFDocument append) throws Exception {
        CTBody src1Body = src.getDocument().getBody();
        CTBody src2Body = append.getDocument().getBody();
        List<XWPFPictureData> allPictures = append.getAllPictures();
        // 记录图片合并前及合并后的ID
        Map<String, String> map = new HashMap();
        for (XWPFPictureData picture : allPictures) {
            String before = append.getRelationId(picture);
            //将原文档中的图片加入到目标文档中
            String after = src.addPictureData(picture.getData(), Document.PICTURE_TYPE_PNG);
            map.put(before, after);
        }
        appendBody(src1Body, src2Body, map);
    }

    //合并文档
    private static void appendBody(CTBody src, CTBody append, Map<String, String> map) throws Exception {
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = append.xmlText(optionsOuter);
        String srcString = src.xmlText();
        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String sufix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));

        if (map != null && !map.isEmpty()) {
            //对xml字符串中图片ID进行替换
            for (Map.Entry<String, String> set : map.entrySet()) {
                addPart = addPart.replace(set.getKey(), set.getValue());
            }
        }
        //将两个文档的xml内容进行拼接
        CTBody makeBody = CTBody.Factory.parse(prefix + mainPart + addPart + sufix);
        src.set(makeBody);
    }

    /**
     * 替换文档中模板
     *
     * @param inputUrl  模板路径
     * @param outputUrl 文档路径
     * @param dataMap   替换信息
     */
    public static void changeWord(String inputUrl, String outputUrl,
                                  Map<String, Object> dataMap, Map<String, Object> imgInfo) {

        FileOutputStream stream = null;
        try {     //获取docx解析对象
            CustomXWPFDocument doc = openDocument(inputUrl);
            //解析替换文本段落对象
            changeText(doc, dataMap, imgInfo);
            //解析替换表格对象
            changeTable(doc, dataMap, imgInfo);
            //生成新的word
            File file = new File(outputUrl);
            stream = new FileOutputStream(file);
            doc.write(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 替换文档中段落信息;
     *
     * @param doc     模板文档;
     * @param textMap 需要替换的信息集合;
     */
    public static void changeText(CustomXWPFDocument doc, Map<String, Object> textMap, Map<String, Object> imgInfo) {
        //获取段落集合
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph paragraph;
        while (iterator.hasNext()) {
            //判断此段落时候需要进行替换
            paragraph = iterator.next();
            String text = paragraph.getText();
            int oprType = checkText(text);
            if (oprType == 1) {
                //替换文本;
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String value = changeValue(run.toString(), textMap);
                    run.setText(value, 0);
                }
                //替换图片;
                changeImg(doc, paragraph, textMap, imgInfo);
                //清除占位符;
                clearSymbol(paragraph);
            }
        }
    }

    /**
     * 替换表格对象方法
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void changeTable(CustomXWPFDocument document, Map<String, Object> textMap, Map<String, Object> imgInfo) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            //只处理行数大于等于2的表格
            XWPFTable table = tables.get(i);
            if (table.getRows().size() >= 1) {
                //判断word模板是哪种操作类型;
                int oprType = checkText(table.getText());
                switch (oprType) {
                    case 1:
                        List<XWPFTableRow> rows = table.getRows();
                        eachTable(document, rows, textMap, imgInfo);
                        break;
                    case 2:
                        insertTable(document, table, textMap, imgInfo);
                        break;
                }
            }
        }
    }

    /**
     * 插入表格数据;
     *
     * @param table   所插入的表格
     * @param textMap 信息源数据
     */
    public static void insertTable(CustomXWPFDocument doc, XWPFTable table, Map<String, Object> textMap, Map<String, Object> imgInfo) {
        int size = table.getRows().size();
        if (size == 1) {
            insertDynamicTable(doc, table, textMap, imgInfo);
        } else if (size >= 2) {
            XWPFTableRow row = table.getRow(0);
            String text = row.getCell(row.getTableICells().size() - 1).getText();
            if (checkText(text) == 2) {
                insertMixTable(doc, table, textMap, imgInfo);
            } else {
                insertFixedTable(doc, table, textMap, imgInfo);
            }
        }


    }

    //复制cell样式
    public static void copyCellStyle(XWPFTableCell targetCell, XWPFTableCell sourceCell) {
        //列属性
        targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
        //段落属性
        if (sourceCell.getParagraphs() != null && sourceCell.getParagraphs().size() > 0) {
            targetCell.getParagraphs().get(0).getCTP().setPPr(sourceCell.getParagraphs().get(0).getCTP().getPPr());
            if (sourceCell.getParagraphs().get(0).getRuns() != null && sourceCell.getParagraphs().get(0).getRuns().size() > 0) {
                XWPFRun cellR = targetCell.getParagraphs().get(0).createRun();
                cellR.setBold(sourceCell.getParagraphs().get(0).getRuns().get(0).isBold());
                cellR.setFontFamily(sourceCell.getParagraphs().get(0).getRuns().get(0).getFontFamily());
            } else {
            }
        } else {
        }
    }

    //复制cell到新增的cell;
    public static void copyCell(XWPFTableRow tableRow, XWPFTableCell sourceCell, String text) {
        XWPFTableCell targetCell = tableRow.addNewTableCell();
        //列属性
        targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
        //段落属性
        if (sourceCell.getParagraphs() != null && sourceCell.getParagraphs().size() > 0) {
            targetCell.getParagraphs().get(0).getCTP().setPPr(sourceCell.getParagraphs().get(0).getCTP().getPPr());
            if (sourceCell.getParagraphs().get(0).getRuns() != null && sourceCell.getParagraphs().get(0).getRuns().size() > 0) {
                XWPFRun cellR = targetCell.getParagraphs().get(0).createRun();
                cellR.setText(text);
                cellR.setBold(sourceCell.getParagraphs().get(0).getRuns().get(0).isBold());
                cellR.setFontFamily(sourceCell.getParagraphs().get(0).getRuns().get(0).getFontFamily());
            } else {
                targetCell.setText(text);
            }
        } else {
            targetCell.setText(text);
        }


    }

    /**
     * 遍历表格,替换占位符内容。
     *
     * @param rows    表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(CustomXWPFDocument doc, List<XWPFTableRow> rows, Map<String, Object> textMap, Map<String, Object> imgInfo) {
        for (int row = 0; row < rows.size(); row++) {
            XWPFTableRow tableRow = rows.get(row);
            List<XWPFTableCell> cells = tableRow.getTableCells();
            for (int col = 0; col < cells.size(); col++) {
                XWPFTableCell cell = cells.get(col);
                //判断单元格是否需要替换
                int nums = checkText(cell.getText());
                if (nums == 1) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        //替换文本;
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            String value = changeValue(run.toString(), textMap);
                            run.setText(value, 0);
                        }
                        //替换图片;
                        changeImg(doc, paragraph, textMap, imgInfo);
                        clearSymbol(paragraph);
                    }
                }
            }
        }
    }

    /**
     * 判断字符串是否为图片;
     *
     * @param imgBase64Str 64位字符串：
     * @return
     */
    private static boolean isImage(String imgBase64Str) {
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            try {
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] byteArray = decoder.decodeBuffer(imgBase64Str);
                byteArrayInputStream = new ByteArrayInputStream(byteArray);
                BufferedImage bufImg = ImageIO.read(byteArrayInputStream);
                if (bufImg == null) {
                    return false;
                }
                bufImg = null;
            } finally {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 替换XWPFTRun的内容。
     *
     * @param cell 表格列对象
     * @param text 需要替换的信息
     */
    public static void setRunText(XWPFTableCell cell, String text) {
        List<XWPFParagraph> paragraphs = cell.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (int i = 0; i < runs.size(); i++) {
                if (i == 0) {
                    runs.get(i).setText(text, 0);
                } else {
                    runs.get(i).setText("", 0);
                }
            }
        }
    }

    /**
     * 获取XWPFTRun的内容。
     *
     * @param cell 表格列对象
     * @return text 获得的文本内容
     */
    public String getRunText(XWPFTableCell cell) {
        String text = "";
        List<XWPFParagraph> paragraphs = cell.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                text = run.getText(0);
            }
        }
        return text;
    }

    /**
     * 提取括号内文本
     *
     * @param sourceText 源文本
     * @return 括号内文本
     */
    public static String getText(String sourceText) {
        String text = "";
        Pattern p = Pattern.compile("\\$\\[(.*)\\]");
        Matcher matcher = p.matcher(sourceText);
        while (matcher.find()) {
            text = matcher.group(1);
        }
        return text;
    }

    /**
     * 获取冒号两侧的数据;
     *
     * @param sourceText 源文本
     * @param pos        获取位置
     * @return 目标文本
     */
    public static String getText(String sourceText, int pos) {
        String text = "";
        String[] texts = sourceText.split(":");
        text = texts[pos];
        return text;
    }

    /**
     * 为固定列表格插入数据，行数不够添加新行
     *
     * @param table   需要插入数据的表格
     * @param textMap 插入数据集合
     */
    public static void insertFixedTable(CustomXWPFDocument doc, XWPFTable table, Map<String, Object> textMap, Map<String, Object> imgInfo) {
        List<Map<String, String>> spans = new ArrayList<>();
        int size = table.getRows().size();
        int zwf = size - 1;
        if (size < 2) return;
        XWPFTableRow row = table.getRow(zwf);
        String text = "";
        for (int i = 0; i < row.getTableCells().size(); i++) {
            String cellText = row.getCell(i).getText();
            int operType = checkText(cellText);
            if (operType == 2) {
                text = cellText;
                break;
            }
        }
        if ("".equals(text)) {
            table.removeRow(zwf);
            return;
        }
        String content = getText(text);
        String tableStr = getText(content, 0);
        Object obj = textMap.get(tableStr);
        Map<Integer, String> pos = new HashMap<Integer, String>();
        if (obj != null && obj instanceof Map) {
            Map maps = (Map<String, Object>) obj;
            List<HashMap<String, Object>> datas = (List<HashMap<String, Object>>) maps.get("data");
            if (datas == null || datas.size() == 0) {
                table.removeRow(zwf);
                return;
            }
            //创建行,根据需要插入的数据添加新行，不处理表头
            for (int i = 0; i < datas.size(); i++) {
                XWPFTableRow newRow = table.createRow();
                for(int j=0;j<row.getTableCells().size();j++){
                    XWPFTableCell cell =null;
                    if(j>=newRow.getTableCells().size()){
                        cell  = newRow.addNewTableCell();
                    }else{
                        cell = newRow.getCell(j);
                    }
                    copyCellStyle(cell,row.getCell(j));
                }
            }
            //替换占位符所在行;
            for (int i = 0; i < row.getTableCells().size(); i++) {
                String str = getText(row.getCell(i).getText());
                if ("".equals(str) || str == null) continue;
                String colStr = getText(str, 1);
                pos.put(i, colStr);
                }
            table.removeRow(zwf);
            //遍历表格插入数据
            List<XWPFTableRow> rows = table.getRows();
            for (int i = zwf; i < rows.size(); i++) {
                XWPFTableRow newRow = table.getRow(i);
                insertNewData(doc, table, newRow, datas, i, pos, spans, imgInfo,zwf);
            }
            mergeCell(table, spans);
        }
    }


    /**
     * 判断一个字符串是中文还是英文
     * @param str
     * @return
     */
    public static boolean isCHN(String str){
        String regEx = "[\\u4e00-\\u9fa5]+";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        if(m.find())

            return true;

        else

            return false;
    }
    /**
     * 区分字符串中中英文
     * @param str
     * @return
     */
    public static String[]  splitStr(String str){
        String [] s={};
        char [] c = str.toCharArray();
        StringBuffer newStr = new StringBuffer();
        int incept = 0;
        for (int i = 0; i < c.length; i++) {
            String ch = ""+c[i];
            if(ch.getBytes().length!=incept&&i!=0){
                newStr.append("~~").append(ch);
            }else{
                newStr.append(ch);
            }
            incept = ch.getBytes().length;
        }
        s= newStr.toString().split("~~");
        return s;
    }

    /**
     * 为表中添加数据;
     *
     * @param newRow
     * @param tableList
     * @param pos
     * @param keys
     */

    public static void insertNewData(CustomXWPFDocument doc, XWPFTable table, XWPFTableRow newRow, List<HashMap<String, Object>> tableList, int pos, Map<Integer, String> keys, List<Map<String, String>> spans, Map<String, Object> imgInfo,int zwf) {
        List<XWPFTableCell> cells = newRow.getTableCells();
        HashMap<String, Object> maps = tableList.get(pos -zwf);
		  System.out.println("pos:"+pos);
        for (int j = 0; j < cells.size(); j++) {
            XWPFTableCell cell = cells.get(j);
            String colstr = keys.get(j);
            Object obj1 = maps.get(colstr);
            if (obj1 instanceof String) {
                if (isImage(obj1.toString())) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        changeImg(doc, paragraph, obj1.toString(), imgInfo);
                    }
                } else {
                  cell.setText(obj1.toString());
				  System.out.println("cell:"+obj1.toString());
                }
            } else if (obj1 instanceof Map) {
                Map<String, String> maps2 = (Map<String, String>) obj1;
                Map<String, String> span = new HashMap<>();
                String colNum = maps2.get("col");
                String rowNum = maps2.get("row");
                String value = maps2.get("value");
                if (isImage(value)) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        changeImg(doc, paragraph, value, imgInfo);
                    }
                } else {
                    cell.setText(value);
                }
                span.put("colNum", colNum);
                span.put("rowNum", rowNum);
                span.put("col", "" + j);
                span.put("row", "" + pos);
                spans.add(span);
            }
        }
    }

    //合并列前判断格式;
    public static void mergeCol(XWPFTable table, int row, int fromCell, String colNum) {
        if (colNum != null && !"".equals(colNum)) {
            if (Integer.parseInt(colNum) > 0) {
                mergeCellsHorizontal(table, row, fromCell, fromCell + Integer.parseInt(colNum));
            }
        }
    }

    //合并行前判断格式;
    public static void mergeRow(XWPFTable table, int col, int fromRow, String rowNum) {
        if (rowNum != null && !"".equals(rowNum)) {
            if (Integer.parseInt(rowNum) > 0) {
                mergeCellsVertically(table, col, fromRow, fromRow + Integer.parseInt(rowNum));
            }
        }
    }

    /**
     * 为动态列表格插入数据，行数不够添加新行
     *
     * @param table   需要插入数据的表格
     * @param textMap 插入数据集合
     */

    public static void insertDynamicTable(CustomXWPFDocument doc, XWPFTable table, Map<String, Object> textMap, Map<String, Object> imgInfo) {
        XWPFTableRow eRow = table.getRow(0);
        if (eRow.getTableICells().size() > 2) return;
        String text = "";
        String cellText = eRow.getCell(0).getText();
        int operType = checkText(cellText);
        if (operType == 2) text = cellText;
        if ("".equals(text)) return;
        List<Map<String, String>> spans = new ArrayList<>();
        String content = getText(text);
        Object obj = textMap.get(content);
        Map maps = (Map<String, Object>) obj;
        Map<Integer, String> pos = new HashMap<Integer, String>();
        if (maps == null) return;
        List<Object> columns = (List<Object>) maps.get("dColumns");
        if (columns == null || columns.size() == 0) return;
        //创建列
        XWPFTableCell eCell = eRow.getCell(0);
        Map<String, Object> maps1 = (Map<String, Object>) columns.get(0);
        Object obj3 = maps1.get("value");
        if (obj3 instanceof String) {
            setRunText(eCell, obj3.toString());
        } else if (obj3 instanceof Map) {
            Map<String, String> span = new HashMap<String, String>();
            Map<String, String> maps3 = (Map<String, String>) obj3;
            String colNum = maps3.get("col");
            String rowNum = maps3.get("row");
            String value = maps3.get("value");
            setRunText(eCell, value);
            span.put("colNum", colNum);
            span.put("col", "" + (eRow.getTableICells().size() - 1));
            span.put("rowNum", rowNum);
            span.put("row", "0");
            spans.add(span);
        }
        pos.put(0, maps1.get("name").toString());
        for (int i = 1; i < columns.size(); i++) {
            Map<String, Object> maps2 = (Map<String, Object>) columns.get(i);
            Object obj2 = maps2.get("value");
            if (obj2 instanceof String) {
                copyCell(eRow, eCell, maps2.get("value").toString());
            } else if (obj2 instanceof Map) {
                Map<String, String> span = new HashMap<String, String>();
                Map<String, String> maps3 = (Map<String, String>) obj3;
                String colNum = maps3.get("col");
                String rowNum = maps3.get("row");
                String value = maps3.get("value");
                copyCell(eRow, eCell, value);
                span.put("colNum", colNum);
                span.put("col", "" + i);
                span.put("rowNum", rowNum);
                span.put("row", "0");
                spans.add(span);
            }
            pos.put(i, maps2.get("name").toString());
        }

        //创建行
        if (obj != null && obj instanceof Map) {
            List<HashMap<String, Object>> datas = (List<HashMap<String, Object>>) maps.get("data");
            if (datas == null || datas.size() == 0) return;
            //创建行,根据需要插入的数据添加新行，不处理表头
            for (int i = 0; i < datas.size(); i++) {
                XWPFTableRow newRow = table.createRow();
            }
            //遍历表格插入数据
            List<XWPFTableRow> rows = table.getRows();
            for (int i = 1; i < rows.size(); i++) {
                XWPFTableRow newRow = table.getRow(i);
                insertNewData(doc, table, newRow, datas, i, pos, spans, imgInfo,0);
            }
        }
        mergeCell(table, spans);

    }

    /**
     * 为混合列表格插入数据，行数不够添加新行
     *
     * @param table   需要插入数据的表格
     * @param textMap 插入数据集合
     */
    public static void insertMixTable(CustomXWPFDocument doc, XWPFTable table, Map<String, Object> textMap, Map<String, Object> imgInfo) {

        //获取模板行;
        XWPFTableRow eRow = table.getRow(0);
        int pos = eRow.getTableICells().size() - 1;
        XWPFTableCell xcell = eRow.getCell(pos);
        String xText = "";
        String xcellText = xcell.getText();
        int xcellType = checkText(xcellText);
        if (xcellType == 2) xText = xcellText;
        if ("".equals(xText)) return;
        List<Map<String, String>> spans = new ArrayList<>();
        String xContent = getText(xText);
        Object xobj = textMap.get(xContent);
        Map xmaps = (Map<String, Object>) xobj;
        Map<Integer, String> position = new HashMap<Integer, String>();
        if (xmaps == null) return;
        List<Object> columns = (List<Object>) xmaps.get("dColumns");
        if (columns == null || columns.size() == 0) return;
        //创建列头
        XWPFTableCell eCell = xcell;
        Map<String, Object> maps1 = (Map<String, Object>) columns.get(0);
        Object obj1 = maps1.get("value");
        if (obj1 instanceof String) {
            setRunText(eCell, obj1.toString());
        } else if (obj1 instanceof Map) {
            Map<String, String> emaps = (Map<String, String>) obj1;
            String colNum = emaps.get("col");
            String rowNum = emaps.get("row");
            String value = emaps.get("value");
            setRunText(eCell, value);
            Map<String, String> span = new HashMap<String, String>();
            span.put("colNum", colNum);
            span.put("col", "" + pos);
            span.put("rowNum", rowNum);
            span.put("row", "0");
            spans.add(span);
        }
        position.put(pos, maps1.get("name").toString());
        for (int i = 1; i < columns.size(); i++) {
            Map<String, Object> emaps2 = (Map<String, Object>) columns.get(i);
            Object eobj2 = emaps2.get("value");
            if (eobj2 instanceof String) {
                copyCell(eRow, eCell, emaps2.get("value").toString());
            } else if (eobj2 instanceof Map) {
                Map<String, String> span = new HashMap<String, String>();
                Map<String, String> emaps3 = (Map<String, String>) eobj2;
                String colNum = emaps3.get("col");
                String rowNum = emaps3.get("row");
                String value = emaps3.get("value");
                copyCell(eRow, eCell, value);
                span.put("colNum", colNum);
                span.put("col", "" + i);
                span.put("row", "0");
                span.put("rowNum", rowNum);
                spans.add(span);
            }
            position.put(i + pos, emaps2.get("name").toString());
        }
        //为占位符行创建列;
        for (int i = 1; i < table.getRows().size(); i++) {
            XWPFTableRow row = table.getRow(i);
            for (int j = row.getTableCells().size(); j < eRow.getTableCells().size(); j++) {
                copyCell(row, eCell, "");
            }
        }
        //创建行开始;
        int size = table.getRows().size();
        int zwf = size - 1;
        if (size < 2) return;
        XWPFTableRow lrow = table.getRow(zwf);
        List<HashMap<String, Object>> datas = (List<HashMap<String, Object>>) xmaps.get("data");
        if (datas == null || datas.size() == 0) return;
        //创建行,根据需要插入的数据添加新行，不处理表头
        for (int i = 1; i < datas.size(); i++) {
            XWPFTableRow newRow = table.createRow();
        }
        //替换占位符所在行;
        for (int i = 0; i < lrow.getTableCells().size(); i++) {
            if ("".equals(lrow.getCell(i).getText())) {
                Map<String, Object> lmaps = (HashMap<String, Object>) datas.get(0);
                String colStr = position.get(i);
                Object lobj = lmaps.get(colStr);
                if (lobj instanceof String) {
                    if (isImage(lobj.toString())) {
                        for (XWPFParagraph paragraph : lrow.getCell(i).getParagraphs()) {

                            changeImg(doc, paragraph, lobj.toString(), imgInfo);
                        }
                    } else {
                        lrow.getCell(i).setText(lobj.toString());
                    }
                } else if (lobj instanceof Map) {
                    Map<String, String> lmaps2 = (Map<String, String>) lobj;
                    String colNum = lmaps2.get("col");
                    String rowNum = lmaps2.get("row");
                    String value = lmaps2.get("value");
                    if (isImage(value)) {
                        for (XWPFParagraph paragraph : lrow.getCell(i).getParagraphs()) {
                            changeImg(doc, paragraph, value, imgInfo);
                        }
                    } else {
                        lrow.getCell(i).setText(value);
                    }
                    Map<String, String> span = new HashMap<String, String>();
                    span.put("colNum", colNum);
                    span.put("col", "" + pos);
                    span.put("rowNum", rowNum);
                    span.put("row", "0");
                    spans.add(span);
                }
            } else {
                String str = getText(lrow.getCell(i).getText());
                if ("".equals(str) || str == null) continue;
                String colStr = getText(str, 1);
                Map<String, Object> mapss1 = (HashMap<String, Object>) datas.get(0);
                Object lobj1 = mapss1.get(colStr);
                if (lobj1 instanceof String) {
                    if (isImage(lobj1.toString())) {
                        setRunText(lrow.getCell(i), "");
                        for (XWPFParagraph paragraph : lrow.getCell(i).getParagraphs()) {
                            changeImg(doc, paragraph, lobj1.toString(), imgInfo);
                        }
                    } else {
                        setRunText(lrow.getCell(i), lobj1.toString());
                    }
                    position.put(i, colStr);
                } else if (obj1 instanceof Map) {
                    Map<String, String> maps2 = (Map<String, String>) lobj1;
                    String colNum = maps2.get("col");
                    String rowNum = maps2.get("row");
                    String value = maps2.get("value");
                    if (isImage(value)) {
                        setRunText(lrow.getCell(i), "");
                        for (XWPFParagraph paragraph : lrow.getCell(i).getParagraphs()) {
                            changeImg(doc, paragraph, value, imgInfo);
                        }
                    } else {
                        setRunText(lrow.getCell(i), value);
                    }
                    position.put(i, colStr);
                    Map<String, String> span = new HashMap<String, String>();
                    span.put("colNum", colNum);
                    span.put("col", "" + pos);
                    span.put("rowNum", rowNum);
                    span.put("row", "0");
                    spans.add(span);
                }
            }
        }
        //遍历表格插入数据
        List<XWPFTableRow> rows = table.getRows();
        for (int i = pos + 1; i < rows.size(); i++) {
            XWPFTableRow newRow = table.getRow(i);
            insertNewData(doc, table, newRow, datas, i, position, spans, imgInfo,zwf);
        }
        mergeCell(table, spans);
    }


    /**
     * 统一合并单元格;
     */
    public static void mergeCell(XWPFTable table, List<Map<String, String>> spans) {
        for (int i = 0; i < spans.size(); i++) {
            Map<String, String> maps = spans.get(i);
            String colNum = maps.get("colNum");
            String rowNum = maps.get("rowNum");
            String col = maps.get("col");
            String row = maps.get("row");
            if (colNum != null && !"".equals(colNum)) {
                if (Integer.parseInt(colNum) <= 0) continue;
                mergeCellsHorizontal(table, Integer.parseInt(row), Integer.parseInt(col), Integer.parseInt(col) + Integer.parseInt(colNum));
            }
            if (rowNum != null && !"".equals(rowNum)) {
                if (Integer.parseInt(rowNum) <= 0) continue;
                mergeCellsVertically(table, Integer.parseInt(col), Integer.parseInt(row), Integer.parseInt(row) + Integer.parseInt(rowNum));
            }
        }
    }

    /**
     * 判断文本中时候包含哪一种占位符
     *
     * @param text 文本
     * @return 0代表${name},1代表${table},2代表$[table],-1 代表无匹配值;
     */
    public static int checkText(String text) {
        String regx1 = "^[\\s\\S]*\\$\\{[A-Za-z0-9_]*}[\\s\\S]*$";
        String regx2 = "^[\\s\\S]*\\$\\[[\\s\\S]*][\\s\\S]*$";
        int num = -1;
        if (text.matches(regx1)) {
            num = 1;
        } else if (text.matches(regx2)) {
            num = 2;
        }
        return num;
    }

    /**
     * 清空段落中占位符
     *
     * @param paragraph 指定段落;
     */
    public static void clearSymbol(XWPFParagraph paragraph) {
        for (XWPFRun run : paragraph.getRuns()) {
            if (run.getText(0) == null || "".equals(run.getText(0))) continue;
            if (checkText(run.getText(0)) != -1) {
                run.setText("", 0);
                builder.append("占位符:"+run.getText(0)+"清理完成。");
                builder.toString();
            }
        }
    }

    /**
     * 匹配传入信息集合与模板(文本信息)
     *
     * @param value   模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String value, Map<String, Object> textMap) {
        Set<Map.Entry<String, Object>> textSets = textMap.entrySet();
        for (Map.Entry<String, Object> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = "${" + textSet.getKey() + "}";
            Object value1 = textSet.getValue();
            if (value.indexOf(key) != -1) {
                if(value1==null) return "";
                if (!isImage(value1.toString())) {
                    value = value1.toString();
                }
            }

        }
        return value;
    }

    /**
     * 动态列表中生成图片;
     *
     * @param doc
     * @param paragraph
     * @param base64Str
     * @param imgInfo
     */
    public static void changeImg(CustomXWPFDocument doc, XWPFParagraph paragraph, String base64Str, Map<String, Object> imgInfo) {
        try {
            String content = base64Str;
            String imgUrl = generateImgUrl(content, imgInfo);
            generateImage(content, imgUrl);
            int[] imgPro = getImgProperty(imgUrl);
            byte[] byteArray = inputStream2ByteArray(new FileInputStream(imgUrl), true);
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
            doc.addPictureData(byteInputStream, getPictureType("jpg"));
            int id = doc.getAllPictures().size() - 1;
            String blipId =
                    doc.getAllPictures().get(id).getPackageRelationship().getId();
            doc.createPicture(blipId, doc.getAllPictures().size() - 1, imgPro[0], imgPro[1], paragraph);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 匹配传入信息集合与模板（图片信息）
     *
     * @param doc       模板文档
     * @param paragraph 替换段落
     * @param textMap   传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static void changeImg(CustomXWPFDocument doc, XWPFParagraph paragraph, Map<String, Object> textMap, Map<String, Object> imgInfo) {
        int pos = -1;
        String value = "";
        String value2 = "";
        Set<Map.Entry<String, Object>> textSets = textMap.entrySet();
        for (Map.Entry<String, Object> textSet : textSets) {
            for (int i = 0; i < paragraph.getRuns().size(); i++) {
                String key = "${" + textSet.getKey() + "}";
                Object value1 = textSet.getValue();
                if (paragraph.getRuns().get(i).getText(0).indexOf(key) != -1) {
                    if (isImage(value1.toString())) {
                        pos = i;
                        value = key;
                    }
                }
            }
            //匹配模板与替换值 格式${key}
            String key = "${" + textSet.getKey() + "}";
            if (value.indexOf(key) != -1) {
                String str = "";
                Object value1 = textSet.getValue();
                if (isImage(value1.toString())) {
                    try {
                        String content = value1.toString();
                        String imgUrl = generateImgUrl(content, imgInfo);
                        generateImage(content, imgUrl);
                        int[] imgPro = getImgProperty(imgUrl);
                        byte[] byteArray = inputStream2ByteArray(new FileInputStream(imgUrl), true);
                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                        doc.addPictureData(byteInputStream, getPictureType("jpg"));
                        int id = doc.getAllPictures().size() - 1;
                        String blipId =
                                doc.getAllPictures().get(id).getPackageRelationship().getId();
                        doc.createPicture(blipId, doc.getAllPictures().size() - 1, imgPro[0], imgPro[1], paragraph);
                        paragraph.getRuns().get(pos).setText("", 0);
                        builder.append("占位符:"+key+",替换图片成功!");
                        builder.append("\n");
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 将base64字符串生成图片存储在本地;
     *
     * @param content
     * @param imgInfo
     * @return
     */
    public static String generateImgUrl(String content, Map<String, Object> imgInfo) {
        String url = "";
        String path = imgInfo.get("path").toString();
        List<String> imgUrl = (List<String>) imgInfo.get("imgUrl");
        int size = imgUrl.size();
        url = path + size + ".jpg";
        imgUrl.add(url);
        return url;
    }

    /**
     * 合并列
     *
     * @param table    合并表格
     * @param row      行数
     * @param fromCell 开始单元格
     * @param toCell   结束单元格
     */
    public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            if (cellIndex >= table.getRow(row).getTableCells().size()) return;
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if (cellIndex == fromCell) {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 合并行
     *
     * @param table   合并表格
     * @param col     所在列
     * @param fromRow 开始行数
     * @param toRow   结束行数
     */
    public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            if (rowIndex >= table.getRows().size()) return;
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     *
     * @param picType 匹配图片格式
     * @return int 返回文档设别的图片格式
     */
    private static int getPictureType(String picType) {
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null) {
            if (picType.equalsIgnoreCase("png")) {
                res = CustomXWPFDocument.PICTURE_TYPE_PNG;
            } else if (picType.equalsIgnoreCase("dib")) {
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;
            } else if (picType.equalsIgnoreCase("emf")) {
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;
            } else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
            } else if (picType.equalsIgnoreCase("wmf")) {
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }


    /**
     * 将输入流中的数据写入字节数组
     *
     * @param
     * @return
     */
    public static byte[] inputStream2ByteArray(InputStream in, boolean isClose) {
        byte[] byteArray = null;
        try {
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isClose) {
                try {
                    in.close();
                } catch (Exception e2) {
                    e2.getStackTrace();
                }
            }
        }
        return byteArray;
    }
}
