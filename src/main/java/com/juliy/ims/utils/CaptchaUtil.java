package com.juliy.ims.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 验证码工具类
 * @author JuLiy
 * @date 2022/12/7 12:08
 */
public class CaptchaUtil {

    private static final Random random = new Random();
    private static final String[] font = {"宋体", "华文楷体", "黑体", "微软雅黑"};
    private static final int[] xArr = {23, 24, 19, 20, 21};
    private static final int[] yArr = {20, 21, 22, 23, 24, 25, 16, 17, 18};
    private static final int[] fontSize = {28, 29, 30, 21, 22, 23, 24, 27, 26, 25};
    private static final char[] c = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '3', '5', '6', '7', '8', '9'};

    private CaptchaUtil() {}

    /**
     * 画随机码图
     * @param out 输出流
     * @throws IOException 生成验证码图片时出错
     */
    public static void draw(OutputStream out, String value) throws IOException {
        int width = 110;
        int height = 40;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(randColor(240, 255));
        g.fillRect(0, 0, width, height);
        g.drawRect(1, 1, width - 2, height - 2);
        for (int i = 0; i < 10; i++) {
            g.setColor(randColor(150, 250));
            g.drawOval(random.nextInt(110), random.nextInt(24), 5 + random.nextInt(10), 5 + random.nextInt(10));
        }
        g.setFont(getFont());
        g.setColor(randColor(xArr[random.nextInt(5)], 254));
        for (int i = 0, len = value.length(); i < len; i++) {
            String rand = String.valueOf(value.charAt(i));
            int degree = random.nextInt(23);
            if (i % 2 == 0) {
                degree = degree * (-1);
            }
            int x = xArr[random.nextInt(5)] * i;
            int y = yArr[random.nextInt(8)];
            g.rotate(Math.toRadians(degree), x, y);
            g.setColor(randColor(48, 254));
            g.drawString(rand, x + 8, y + 10);
            g.rotate(-Math.toRadians(degree), x, y);
        }
        // 图片中间线
        g.setColor(randColor(0, 200));
        // width是线宽,float型
        BasicStroke bs = new BasicStroke(2);
        g.setStroke(bs);
        // 画出曲线
        QuadCurve2D.Double curve = new QuadCurve2D.Double(0d, random.nextInt(height - 8) + 4.0, width / 2.0, height / 2.0, width, random.nextInt(height - 8) + 4.0);
        g.draw(curve);
        // 销毁图像
        g.dispose();
        ImageIO.write(bi, "png", out);
    }

    private static Font getFont() {
        return new Font(font[random.nextInt(4)], Font.BOLD, fontSize[random.nextInt(10)]);
    }

    /** 给定范围获得随机颜色 */
    private static Color randColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public static String getRandom(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(c[random.nextInt(Integer.MAX_VALUE) % c.length]);
        }
        return sb.toString();
    }
}
