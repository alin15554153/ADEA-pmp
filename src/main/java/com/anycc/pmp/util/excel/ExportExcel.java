package com.anycc.pmp.util.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 导出Excel公共方法 Created by DELL1 on 2016/5/16.
 */
public class ExportExcel {

    private String title; // 显示的导出标题
    private String[] rowNames; // 导出表的列名
    private List<Object[]> dataList = new ArrayList<Object[]>();

    HttpServletResponse response;
    private String filePath;
    private String fileName;

    private List<CellRangeAddress> mergedList;
    private List<Short> colorList;

    private ExcelEntity excelEntity;

    /**
     * 构造方法，传入导出的数据(写到页面).
     *
     * @param excelEntity
     */
    public ExportExcel(ExcelEntity excelEntity) {
        this.excelEntity = excelEntity;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    /**
     * 导出数据.
     *
     * @throws Exception
     */
    public void export(HttpServletResponse response) throws Exception {
        try {
            HSSFWorkbook workbook = createExportDatas();

            if (workbook != null) {
                try {
                    if(StringUtils.isEmpty(fileName)){
                        fileName =
                                "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
                    }
                    fileName= URLEncoder.encode(fileName,"UTF-8");
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    if (response != null) {
                        response.setContentType("application/octet-stream");
                        response.setHeader("Content-Disposition", headStr);
                        OutputStream out = response.getOutputStream();
                        workbook.write(out);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出数据(写到本地)
     *
     * @throws Exception
     */
    public void export(String filePath) throws Exception {
        HSSFWorkbook workbook = createExportDatas();

        if (workbook != null) {
            try {
                String fileName =
                        "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
                String headStr = "attachment; filename=\"" + fileName + "\"";
                filePath = filePath != null && !"".equals(filePath) ? filePath : System.getProperty("user.dir");
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                fileName = filePath + File.separator + fileName;

                OutputStream out = new FileOutputStream(fileName);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                workbook.write(bos);
                bos.flush();
                bos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return
     * @throws Exception
     */
    private HSSFWorkbook createExportDatas() throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
        HSSFSheet sheet = workbook.createSheet(); // 创建工作表

        // 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTitle = rowm.createCell(0);

        // sheet样式定义[getColumnTopStyle()/getStyle()均为自定义方法-下面-可扩展]
        HSSFCellStyle mainTitleStyle = this.getMainTitleStyle(workbook);
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook); // 获取列头样式对象
        HSSFCellStyle style = this.getStyle(workbook); // 单元格样式对象

        cellTitle.setCellStyle(mainTitleStyle);
        cellTitle.setCellValue(excelEntity.getTitle());
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (excelEntity.getFields().size() - 1)));

        int rowIndex = rowm.getRowNum() + 1;
        if (excelEntity.getInfo()!=null && excelEntity.getInfo().size() > 0) {
            rowIndex = createInfoExportDatas(workbook, sheet);
        }

        // 定义所需列数
        int columnNum = excelEntity.getFields().size();
        HSSFRow rowRowNames = sheet.createRow(rowIndex + 1); // 在索引rowIndex的位置创建行

        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            HSSFCell cellRowName = rowRowNames.createCell(n); // 创建列头对应个数的单元格
            cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
            HSSFRichTextString text = new HSSFRichTextString(excelEntity.getRowNames().get(n));
            cellRowName.setCellValue(text); // 设置列头单元格的值
            cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
        }

        // 将查询出的数据设置到sheet对应的单元格中
        List<Map<String, Object>> dataList = excelEntity.getDatas();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> target = dataList.get(i); // 遍历每个对象
            HSSFRow row = sheet.createRow(i + rowIndex + 2); // 创建所需的行
            for (int j = 0; j < columnNum; j++) {
                HSSFCell cell = null; /// 设置单元格的数据类型
          /*
           * if (j == 0) { cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
           * cell.setCellValue(i+1); } else { cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
           * if (!"".equals(obj[j-1]) && obj[j-1] != null) { cell.setCellValue(obj[j-1].toString());
           * //设置单元格的值 } }
           */
                cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);

                Object obj = target.get(excelEntity.getFields().get(j));

                if (!"".equals(obj) && obj != null) {
                    cell.setCellValue(obj.toString()); // 设置单元格的值
                } else {
                    cell.setCellValue(" "); // 设置单元格的值
                }

                if(null!=colorList){
                    style = this.getStyle(workbook); // 单元格样式对象
                    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    style.setFillForegroundColor(colorList.get(i));
                }



                cell.setCellStyle(style);
            }
        }

        // 让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue() != null
                                ? currentCell.getStringCellValue().getBytes().length : 0;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                if((columnWidth + 0.72) * 256<65280) {
                    sheet.setColumnWidth(colNum, (int)(columnWidth + 0.72) * 256);
                }else{
                    sheet.setColumnWidth(colNum, 65200);
                }
            }
        }

        //合并单元格
        if(null!=mergedList){
            for(CellRangeAddress cra:mergedList){
                sheet.addMergedRegion(cra);
            }
        }



        return workbook;

    }

    /**
     * 创建基本信息导出数据
     * @return
     * @throws Exception
     */
    private int createInfoExportDatas(HSSFWorkbook workbook, HSSFSheet sheet) throws Exception {

        List<InfoEntity> maps = excelEntity.getInfo();//数据

        // 产生基本信息标题行
        HSSFRow rowm = sheet.createRow(2);
        HSSFCell cellTitle = rowm.createCell(0);

        // sheet样式定义
        HSSFCellStyle infoTitleStyle = this.getInfoTitleStyle(workbook); // 获取基本信息标题样式对象

        //设置基本信息标题
        //public CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, (excelEntity.getFields().size() - 1)));
        cellTitle.setCellStyle(infoTitleStyle);
        cellTitle.setCellValue(maps.get(0).getValue().toString());


        // 计算基本信息显示
        int columnNum = excelEntity.getFields().size(); //总列数
        int dataNum = maps.size()-1;   //去除标题数据
        int dataNumByRow = dataNum / 3; //每行3个数据，总共占几行

        int currentRow = 0;

        /*int maxTextLength = 0;
        for (int i=1;i<dataNum;i++) {
            InfoEntity map = maps.get(i);
            if (map.getName() != null) {
                if (maxTextLength < map.getName().length()) {
                    maxTextLength = map.getName().length();
                }
            }
            if (map.getValue() != null) {
                if (maxTextLength < map.getValue().toString().length()) {
                    maxTextLength = map.getValue().toString().length();
                }
            }
        }*/


        for (int i=0; i<dataNumByRow;i++) {
            // 产生基本信息标题行
            HSSFRow row = sheet.createRow(i+3);

            currentRow = row.getRowNum();

            HSSFCell cell = row.createCell(0);
            sheet.addMergedRegion(new CellRangeAddress(3+i, 3+i, 0, columnNum -1 ));
            //样式
            HSSFCellStyle infoColumnTitleStyle = this.getInfoColumnStyle(workbook); // 获取基本信息列标题样式对象
            cell.setCellStyle(infoColumnTitleStyle);
            String val = "";

            if (i == dataNumByRow-1 && dataNum%3 > 0) {
                for (int j=0;j<dataNum%3;j++) {
                    InfoEntity map = maps.get(i*3+j+1);
                    val +=  map.getName() + "  :  " + map.getValue() + "    ";
                }
            } else {
                for (int j=0;j<3;j++) {
                    InfoEntity map = maps.get(i*3+j+1);
                    val += map.getName() + "  :  " + map.getValue() + "    ";
                }
            }

            cell.setCellValue(val);
        }

        return currentRow;
    }

    /**
     * 主标题.
     *
     * @param workbook
     * @return
     */
    public HSSFCellStyle getMainTitleStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 24);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("宋体");

        // 设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        // 设置底边框颜色
        //style.setBottomBorderColor(HSSFColor.WHITE.index);
        // 设置左边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);

        // 在样式中应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;
    }

    /**
     * 列头单元格样式.
     *
     * @param workbook
     * @return
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("宋体");

        // 设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);

        // 在样式中应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;
    }

    /**
     * 列数据信息单元格样式.
     *
     * @param workbook
     * @return
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)11);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 设置字体名字
        font.setFontName("宋体");

        // 设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);

        // 在样式中应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;
    }

    /**
     * 基本信息(标题样式)单元格样式.
     *
     * @param workbook
     * @return
     */
    public HSSFCellStyle getInfoTitleStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 18);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 设置字体名字
        font.setFontName("宋体");

        // 设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        // 设置底边框颜色
        //style.setBottomBorderColor(HSSFColor.WHITE.index);
        // 设置左边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        // 设置顶边框颜色
        //style.setTopBorderColor(HSSFColor.BLACK.index);

        // 在样式中应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;
    }

    /**
     * 基本信息(列数据样式)单元格样式..
     *
     * @param workbook
     * @return
     */
    public HSSFCellStyle getInfoColumnStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 12);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 设置字体名字
        font.setFontName("宋体");

        // 设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        // 设置底边框颜色
        //style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        // 设置顶边框颜色
        //style.setTopBorderColor(HSSFColor.BLACK.index);

        // 在样式中应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;
    }

    public void setMergedList(List<CellRangeAddress> mergedList) {
        this.mergedList = mergedList;
    }

    public void setColorList(List<Short> colorList) {
        this.colorList = colorList;
    }

    ////************************************以下修改完舍弃***********************************************/////
    /**
     * 构造方法，传入导出的数据(写到页面).
     *
     * @param title
     * @param rowNames
     * @param dataList
     * @param response
     */
    public ExportExcel(String title, String[] rowNames, List<Object[]> dataList,
                       HttpServletResponse response) {
        this.title = title;
        this.rowNames = rowNames;
        this.dataList = dataList;
        this.response = response;
    }

    /**
     * 构造方法，传入导出的数据（写到磁盘）
     *
     * @param title
     * @param rowNames
     * @param dataList
     * @param filePath
     */
    public ExportExcel(String title, String[] rowNames, List<Object[]> dataList, String filePath) {
        this.title = title;
        this.rowNames = rowNames;
        this.dataList = dataList;
        this.filePath = filePath;
    }

    /**
     * 导出数据.
     *
     * @throws Exception
     */
    public void export() throws Exception {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(); // 创建工作表

            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTitle = rowm.createCell(0);

            // sheet样式定义[getColumnTopStyle()/getStyle()均为自定义方法-下面-可扩展]
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook); // 获取列头样式对象
            HSSFCellStyle style = this.getStyle(workbook); // 单元格样式对象

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowNames.length - 1)));
            cellTitle.setCellStyle(columnTopStyle);
            cellTitle.setCellValue(title);

            // 定义所需列数
            int columnNum = rowNames.length;
            HSSFRow rowRowNames = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)

            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                HSSFCell cellRowName = rowRowNames.createCell(n); // 创建列头对应个数的单元格
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
                HSSFRichTextString text = new HSSFRichTextString(rowNames[n]);
                cellRowName.setCellValue(text); // 设置列头单元格的值
                cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataList.size(); i++) {
                Object[] obj = dataList.get(i); // 遍历每个对象
                HSSFRow row = sheet.createRow(i + 3); // 创建所需的行数
                for (int j = 0; j < obj.length; j++) {
                    HSSFCell cell = null; /// 设置单元格的数据类型

                    /** if (j == 0) { cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                     * cell.setCellValue(i+1); } else { cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                     * if (!"".equals(obj[j-1]) && obj[j-1] != null) { cell.setCellValue(obj[j-1].toString());
                     * //设置单元格的值 } }*/

                    cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                    if (!"".equals(obj[j]) && obj[j] != null) {
                        cell.setCellValue(obj[j].toString()); // 设置单元格的值
                    }

                    cell.setCellStyle(style);
                }
            }

            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue() != null
                                    ? currentCell.getStringCellValue().getBytes().length : 0;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if (colNum == 0) {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                } else {
                    sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                }
            }

            if (workbook != null) {
                try {
                    String fileName =
                            "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    if (this.response != null) {
                        this.response.setContentType("application/octet-stream");
                        response.setHeader("Content-Disposition", headStr);
                        OutputStream out = response.getOutputStream();
                        workbook.write(out);
                    } else {
                        File file = new File(this.filePath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        fileName = this.filePath + File.separator + fileName;

                        OutputStream out = new FileOutputStream(fileName);
                        BufferedOutputStream bos = new BufferedOutputStream(out);
                        workbook.write(bos);
                        bos.flush();
                        bos.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



   /* public static void main(String[] args) throws Exception {
        ExcelEntity excelEntity = new ExcelEntity();
        excelEntity.setTitle("测试");
        List<String> rowsNames = new ArrayList<>();
        rowsNames.add("ID");
        rowsNames.add("用户名");
        List<String> fields = new ArrayList<>();
        fields.add("id");
        fields.add("username");
        excelEntity.setRowNames(rowsNames);
        excelEntity.setFields(fields);

        List<Map<String,Object>> datas = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("username", "呵呵");
        datas.add(map);
        excelEntity.setDatas(datas);

        List<InfoEntity> infoEntities = new ArrayList<>();
        infoEntities.add(new InfoEntity("title", "人员基本信息"));
        infoEntities.add(new InfoEntity("姓名", "A"));
        infoEntities.add(new InfoEntity("性别", "男"));
        infoEntities.add(new InfoEntity("番号", "A1000"));
        infoEntities.add(new InfoEntity("别名", "A1"));
        infoEntities.add(new InfoEntity("拘室号", "A1001"));
        infoEntities.add(new InfoEntity("文化程度", "小学"));
        infoEntities.add(new InfoEntity("出生日期", "1985-05-05"));
        infoEntities.add(new InfoEntity("档案号", "D1001"));
        infoEntities.add(new InfoEntity("期限", "2016-05-05"));

        excelEntity.setInfo(infoEntities);


        ExportExcel ex = new ExportExcel(excelEntity);
        ex.export("c:/");
    }*/


}