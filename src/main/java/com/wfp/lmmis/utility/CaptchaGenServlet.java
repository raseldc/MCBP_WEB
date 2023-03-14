/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import java.io.OutputStream;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 *
 * @author Philip
 */
public class CaptchaGenServlet extends HttpServlet
{

    public static final String FILE_TYPE = "jpeg";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Max-Age", 0);

        String captchaStr = "";

        //captchaStr=CaptchaUtil.generateCaptchaTextMethod();
        captchaStr = CaptchaUtil.generateCaptchaTextMethod2(6);

        try
        {

//            int width = 100;
//            int height = 40;
//            Color bg = new Color(225, 238, 251);
//            Color fg = new Color(0, 100, 0);
//
//            Font font = new Font("Arial", Font.BOLD, 20);
//            BufferedImage cpimg = new BufferedImage(width, height, BufferedImage.OPAQUE);
//            Graphics g = cpimg.createGraphics();
//
//            g.setFont(font);
//            g.setColor(bg);
//            g.fillRect(0, 0, width, height);
//            g.setColor(fg);
//            g.drawString(captchaStr, 10, 25);
       
            int width = 150;
            int height = 50;

            BufferedImage bufferedImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = bufferedImage.createGraphics();

            Font font = new Font("Georgia", Font.BOLD, 18);
            g2d.setFont(font);

            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            rh.put(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g2d.setRenderingHints(rh);

//            GradientPaint gp = new GradientPaint(0, 0,
//                    Color.red, 0, height / 2, Color.black, true);
//
//            g2d.setPaint(gp);
            Color bg = new Color(225, 238, 251);
            g2d.setColor(bg);
            g2d.fillRect(0, 0, width, height);

//            g2d.setColor(new Color(255, 153, 0));
            g2d.setColor(new Color(13, 13, 13));

            Random r = new Random();

            int x = 0;
            int y = 0;
            char[] c = captchaStr.toCharArray();
            for (int i = 0; i < c.length; i++)
            {
                x += 10 + (Math.abs(r.nextInt()) % 15);
                y = 20 + Math.abs(r.nextInt()) % 20;
                g2d.drawChars(c, i, 1, x, y);
            }

            g2d.dispose();

            HttpSession session = request.getSession(true);
            session.setAttribute("CAPTCHA", captchaStr.toLowerCase());

            OutputStream outputStream = response.getOutputStream();

            ImageIO.write(bufferedImage, FILE_TYPE, outputStream);
            outputStream.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doPost(request, response);
    }
}
