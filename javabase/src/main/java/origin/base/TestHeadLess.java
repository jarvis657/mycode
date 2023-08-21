package origin.base;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Author:lmq
 * @Date: 2020/7/30
 * @Desc:
 **/
public class TestHeadLess {

    public static void main(String[] args) throws Exception {
        // 设置Headless模式
        System.setProperty("java.awt.headless", "true");

        BufferedImage bi = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);

        Graphics g = bi.getGraphics();
        g.drawString(new String("Headless模式测试".getBytes(), "utf-8"), 50, 50);

        ImageIO.write(bi, "jpeg", new File("test2.jpg"));
    }
}
