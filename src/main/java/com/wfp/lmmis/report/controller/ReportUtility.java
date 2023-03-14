/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.report.controller;

import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Stretching;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.wfp.lmmis.utility.CommonUtility;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 *
 * @author Philip
 */
public class ReportUtility
{
    //    static String FONTPATH = ReportUtility.class.getClassLoader().getResource("fonts/SolaimanLipi.ttf").getPath(); // for ant project
    static String FONTPATH = ReportUtility.class.getClassLoader().getResource("/fonts/SolaimanLipi.ttf").getPath(); //read from /src/main/resource for maven project 
    static String FONTNAME = "SolaimanLipi";

    /**
     *
     * @return
     */
    public static Style createTitleStyle()
    {
        StyleBuilder titleStyle = new StyleBuilder(true);
        Font font = new Font(12, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        titleStyle.setFont(font);
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        titleStyle.setBorderBottom(Border.THIN());
        return titleStyle.build();
    }

    /**
     *
     * @return
     */
    public static Style createSubTitleStyle()
    {
        StyleBuilder subTitleStyle = new StyleBuilder(true);
        subTitleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
//        subTitleStyle.setFont(new Font(Font.MEDIUM, Font._FONT_VERDANA, true));
        Font font = new Font(11, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        subTitleStyle.setFont(font);
        subTitleStyle.setPaddingTop(5);
        return subTitleStyle.build();
    }

    public static Style createHeaderStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(9, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        sb.setBorder(Border.THIN());
        sb.setBorderBottom(Border.NO_BORDER());
        sb.setBackgroundColor(Color.decode("#eeeeee"));
        sb.setBorderColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.CENTER);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTransparency(Transparency.OPAQUE);
        sb.setStretchWithOverflow(true);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    public static Style createGrandTotalLegendStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(9, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        sb.setBorder(Border.THIN());
        sb.setBackgroundColor(Color.decode("#eeeeee"));
        sb.setBorderColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.CENTER);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTransparency(Transparency.OPAQUE);
        sb.setStretchWithOverflow(true);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    public static Style createDistrictGroupStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(11, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        font.setBold(true);
        sb.setFont(font);
        sb.setBorder(Border.THIN());
        sb.setBorderBottom(Border.NO_BORDER());
        sb.setBackgroundColor(Color.decode("#DDDEFD"));
        sb.setBorderColor(Color.BLACK);
        sb.setPaddingTop(5);
        sb.setPaddingBottom(5);
        sb.setPaddingLeft(10);
        sb.setHorizontalAlign(HorizontalAlign.LEFT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTransparency(Transparency.OPAQUE);
        sb.setStretchWithOverflow(true);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    public static Style createUpazilaGroupStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(11, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        font.setBold(true);
        sb.setFont(font);
        sb.setBorder(Border.THIN());
        sb.setBorderBottom(Border.NO_BORDER());
        sb.setBackgroundColor(Color.decode("#E9E5C3"));
        sb.setBorderColor(Color.BLACK);
        sb.setPaddingTop(5);
        sb.setPaddingBottom(5);
        sb.setPaddingLeft(10);
        sb.setHorizontalAlign(HorizontalAlign.LEFT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTransparency(Transparency.OPAQUE);
        sb.setStretchWithOverflow(true);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    public static Style createUnionGroupStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(10, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        font.setBold(true);
        sb.setFont(font);
        sb.setBorder(Border.THIN());
        sb.setBorderBottom(Border.NO_BORDER());
        sb.setBackgroundColor(Color.decode("#DCEBFA"));
        sb.setBorderColor(Color.BLACK);
        sb.setPaddingTop(5);
        sb.setPaddingBottom(5);
        sb.setPaddingLeft(10);
        sb.setHorizontalAlign(HorizontalAlign.LEFT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTransparency(Transparency.OPAQUE);
        sb.setStretchWithOverflow(true);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    /**
     *
     * @return
     */
    public static Style createMarkedHeaderStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(10, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        sb.setBorder(Border.THIN());
        sb.setBorderBottom(Border.NO_BORDER());
        sb.setBackgroundColor(Color.decode("#eeeeee"));
        sb.setBorderColor(Color.BLACK);
        sb.setTextColor(Color.RED);
        sb.setHorizontalAlign(HorizontalAlign.CENTER);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTransparency(Transparency.OPAQUE);
        sb.setStretchWithOverflow(true);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    public static Style createLeftRightStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        sb.setFont(Font.VERDANA_MEDIUM_BOLD);
        sb.setBorder(Border.NO_BORDER());
        sb.setBorderLeft(Border.THIN());
        sb.setBorderRight(Border.THIN());
        sb.setBorderTop(Border.NO_BORDER());
//        sb.setBorderRight(Border.THIN());
//        sb.setBorderBottom(Border.PEN_2_POINT());
        sb.setBorderColor(Color.BLACK);
        //sb.setBackgroundColor(Color.decode("#3c8dbc"));
        // sb.setTextColor(Color.WHITE);
        sb.setBackgroundColor(Color.decode("#eeeeee"));

        sb.setHorizontalAlign(HorizontalAlign.CENTER);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTransparency(Transparency.OPAQUE);
        return sb.build();
    }

    public static Style createDetailTextStyle()
    {
        StyleBuilder sb = new StyleBuilder(false);
        try
        {
            Font font = new Font(8, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
            sb.setFont(font);
            //   sb.setFont(Font.VERDANA_MEDIUM);
            sb.setTransparent(false);
            sb.setBorder(Border.THIN());
            sb.setBorderColor(Color.BLACK);
            sb.setTextColor(Color.BLACK);
            sb.setHorizontalAlign(HorizontalAlign.LEFT);
            sb.setVerticalAlign(VerticalAlign.MIDDLE);
            sb.setPaddingLeft(5);
            sb.setStretchWithOverflow(true);
            sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sb.build();
    }

    public static Style createDetailUpazilaTextStyle()
    {
        StyleBuilder sb = new StyleBuilder(false);
        try
        {
            Font font = new Font(10, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
            sb.setFont(font);
            //   sb.setFont(Font.VERDANA_MEDIUM);
            sb.setTransparent(false);
            sb.setBorder(Border.THIN());
            sb.setBorderColor(Color.BLACK);
            sb.setTextColor(Color.BLACK);
            sb.setHorizontalAlign(HorizontalAlign.LEFT);
            sb.setVerticalAlign(VerticalAlign.MIDDLE);
            sb.setPaddingLeft(5);
            sb.setStretchWithOverflow(true);
            sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sb.build();
    }

    public static Style createDetailNumberStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(8, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        //sb.setFont(Font.VERDANA_MEDIUM);
        sb.setBorder(Border.THIN());
        sb.setBorderColor(Color.BLACK);
        sb.setTextColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setPaddingRight(5);
        sb.setStretchWithOverflow(true);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    public static Style createDetailUpazilaNumberStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(10, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        //sb.setFont(Font.VERDANA_MEDIUM);
        sb.setBorder(Border.THIN());
        sb.setBorderColor(Color.BLACK);
        sb.setTextColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setPaddingRight(5);
        sb.setStretchWithOverflow(true);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    public static Style createDetailAmountStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        //sb.setFont(Font.VERDANA_MEDIUM);
        Font font = new Font(8, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        sb.setBorder(Border.THIN());
        sb.setBorderColor(Color.BLACK);
        sb.setTextColor(Color.BLACK);
        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setPaddingRight(5);
        sb.setPattern("#,##0.00");
        sb.setStretchWithOverflow(true);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    public static Style createDetailTextStyle_BOLD()
    {
        StyleBuilder sb = new StyleBuilder(false);
        try
        {
            Font font = new Font(11, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
            font.setBold(true);
            sb.setFont(font);
            sb.setTransparent(false);
            sb.setBorder(Border.THIN());
            sb.setBorderColor(Color.BLACK);
            sb.setTextColor(Color.BLACK);
            sb.setHorizontalAlign(HorizontalAlign.LEFT);
            sb.setVerticalAlign(VerticalAlign.MIDDLE);
            sb.setPaddingLeft(5);
            sb.setStretchWithOverflow(true);
            sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sb.build();
    }

    /**
     *
     * @return
     */
    public static Style createGroup1VariableStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(8, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        sb.setBorderTop(Border.THIN());
        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTextColor(new Color(50, 50, 150));
        sb.setPaddingLeft(5);
        return sb.build();
    }

    public static Style createGroup1AmountStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(8, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        sb.setBorderTop(Border.THIN());
        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setTextColor(new Color(50, 50, 150));
        sb.setPaddingLeft(5);
        sb.setPattern("#,##0.00");
        return sb.build();
    }

    public static Style createGroup2VariableStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(8, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        sb.setTextColor(new Color(150, 150, 150));
        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        return sb.build();
    }

    /**
     *
     * @return
     */
    public static Style createGroup2AmountStyle()
    {
        StyleBuilder sb = new StyleBuilder(true);
        Font font = new Font(8, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        sb.setFont(font);
        sb.setTextColor(new Color(150, 150, 150));
        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
        sb.setVerticalAlign(VerticalAlign.MIDDLE);
        sb.setPattern("#,##0.00");
        return sb.build();
    }

    public static AbstractColumn createSerialColumn(Style headerStyle, Style detailNumStyle, Locale locale)
    {
        ColumnBuilder columnSerialNo = ColumnBuilder.getNew();
        columnSerialNo.setTitle("#");
        columnSerialNo.setWidth(30);
        columnSerialNo.setFixedWidth(true);
        columnSerialNo.setHeaderStyle(headerStyle);
        columnSerialNo.setStyle(detailNumStyle);
        columnSerialNo.setCustomExpression(new CustomExpression()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public Object evaluate(Map fields, Map variables, Map parameters)
            {
//                Locale locale = LocaleContextHolder.getLocale();
                if (locale.getLanguage() == "bn")
                {
                    return CommonUtility.getNumberInBangla(variables.get("REPORT_COUNT").toString());
                }
                else
                {
                    return (Integer) variables.get("REPORT_COUNT");
                }
            }

            @Override
            public String getClassName()
            {
                return Integer.class.getName();
            }
        });
        return columnSerialNo.build();
    }

    public static Style noDataStyle()
    {
        Font font = new Font(16, FONTNAME, FONTPATH, Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        return new StyleBuilder(false).setFont(font).setTextColor(Color.RED).setHorizontalAlign(HorizontalAlign.CENTER).build();
    }

    public static Style createAlternateRowStyle(DynamicReportBuilder drb)
    {
        StyleBuilder altRowStyle = new StyleBuilder(true);
        drb.setPrintBackgroundOnOddRows(true);
        altRowStyle.setBackgroundColor(Color.decode("#eeeeee"));
        return altRowStyle.build();
    }

    public static AbstractColumn createColumn(String property, Class type,
            String title, int width, Style headerStyle, Style detailStyle)
            throws ColumnBuilderException
    {
        AbstractColumn columnState = ColumnBuilder.getNew()
                .setColumnProperty(property, type.getName()).setTitle(
                title).setWidth(Integer.valueOf(width))
                .setStyle(detailStyle).setHeaderStyle(headerStyle).build();
        return columnState;
    }

    void downloadXLS(JasperPrint jp, HttpServletResponse response) throws ColumnBuilderException, ClassNotFoundException, JRException, IOException
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        export(jp, baos);
        String fileName = "SalesReport.xls";
        response.setHeader("Content-Disposition", "inline; filename="
                + fileName);
        response.setContentType("application/vnd.ms-excel");
        response.setContentLength(baos.size());
        writeReportToResponseStream(response, baos);
    }

    /**
     *
     * @param jp
     * @param response
     * @return
     * @throws ColumnBuilderException
     * @throws ClassNotFoundException
     * @throws JRException
     * @throws IOException
     */
    public static byte[] getPDFContents(JasperPrint jp, HttpServletResponse response) throws ColumnBuilderException, ClassNotFoundException, JRException, IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jp));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        }
        catch (JRException e)
        {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     *
     * @param jp
     * @param response
     * @return
     * @throws ColumnBuilderException
     * @throws ClassNotFoundException
     * @throws JRException
     * @throws IOException
     */
    public static byte[] getHTMLContents(JasperPrint jp, HttpServletResponse response) throws ColumnBuilderException, ClassNotFoundException, JRException, IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            Exporter exporter = new HtmlExporter();
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(baos));
            exporter.setExporterInput(new SimpleExporterInput(jp));
            SimpleHtmlReportConfiguration configuration = new SimpleHtmlReportConfiguration();
            configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
            configuration.setEmbedImage(Boolean.TRUE);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        }
        catch (JRException e)
        {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] getExcelContents(JasperPrint jp, HttpServletResponse response) throws ColumnBuilderException, ClassNotFoundException, JRException, IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(new SimpleExporterInput(jp));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setDetectCellType(true);
            configuration.setCollapseRowSpan(false);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        }
        catch (JRException e)
        {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    void downloadHTML(JasperPrint jp, HttpServletResponse response) throws ColumnBuilderException, ClassNotFoundException, JRException, IOException
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        export(jp, baos);
        String fileName = "SalesReport.html";
        response.setHeader("Content-Disposition", "inline; filename="
                + fileName);
        response.setContentType("application/html");
        response.setContentLength(baos.size());
        writeReportToResponseStream(response, baos);
    }

    private void export(JasperPrint jp, ByteArrayOutputStream baos) throws JRException
    {
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(jp));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        exporter.setConfiguration(configuration);
        exporter.exportReport();

    }

    /**
     * Writes the report to the output stream
     */
    private static void writeReportToResponseStream(HttpServletResponse response,
            ByteArrayOutputStream baos)
    {

        System.out.println("Writing report to the stream");
        try
        {
            ServletOutputStream outputStream = response.getOutputStream();
            baos.writeTo(outputStream);
            outputStream.flush();
        }
        catch (Exception e)
        {
            System.out.println("Unable to write report to the output stream");
        }
    }

}
