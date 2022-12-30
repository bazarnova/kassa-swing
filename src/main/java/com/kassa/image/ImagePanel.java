package com.kassa.image;

import com.kassa.entity.Photo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImagePanel extends JPanel {
    List<Photo> photos;

    public ImagePanel(List<Photo> photos) {
        this.photos = photos;
        init();
    }

    private void init() {
        setLayout(new BorderLayout(0, 1));
        JPanel buttonPanel = new JPanel();
        JButton button = new JButton("OK");
        buttonPanel.add(button);
        add(buttonPanel, BorderLayout.EAST);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(photos.get(2).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ImageIcon icon = new ImageIcon(img);
        JLabel label = new JLabel(icon);
        JScrollPane pane = new JScrollPane(label);
        add(pane, BorderLayout.CENTER);
    }

//    private void zoom(MouseWheelEvent e) {
//        try {
//            int wheelRotation = e.getWheelRotation();
//            Point p = e.getPoint();
//            if (wheelRotation > 0) {
//                if (zoomLevel < maxZoomLevel) {
//                    zoomLevel++;
//                    Point2D p1 = transformPoint(p);
//                    coordTransform.scale(1 / zoomMultiplicationFactor, 1 / zoomMultiplicationFactor);
//                    Point2D p2 = transformPoint(p);
//                    coordTransform.translate(p2.getX() - p1.getX(), p2.getY() - p1.getY());
//                    repaint();
//                }
//            } else {
//                if (zoomLevel > minZoomLevel) {
//                    zoomLevel--;
//                    Point2D p1 = transformPoint(p);
//                    coordTransform.scale(zoomMultiplicationFactor, zoomMultiplicationFactor);
//                    Point2D p2 = transformPoint(p);
//                    coordTransform.translate(p2.getX() - p1.getX(), p2.getY() - p1.getY());
//                    repaint();
//                }
//            }
//        } catch (NoninvertibleTransformException ex) {
//            ex.printStackTrace();
//        }
    }
}
