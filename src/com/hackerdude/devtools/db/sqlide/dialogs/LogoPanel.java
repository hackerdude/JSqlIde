package com.hackerdude.devtools.db.sqlide.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.net.URL;


public class LogoPanel extends JPanel implements Runnable {

    private Image img;
    private final double OINC[] = {5.0, 3.0};
    private final double SINC[] = {5.0, 5.0};
    private double x, y;
    private double ix = OINC[0];
    private double iy = OINC[1];
    private double iw = SINC[0];
    private double ih = SINC[1];
    private double ew, eh;   // ellipse width & height
    private GeneralPath p = new GeneralPath();
    private AffineTransform at = new AffineTransform();
    private BasicStroke bs = new BasicStroke(20.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float();
    private Thread thread;
    private BufferedImage offImg;
    private int w, h;
    private boolean newBufferedImage;

    public LogoPanel() {
		super();
		setBackground(Color.white);
		URL url = LogoPanel.class.getResource("/com/hackerdude/images/SQLIDE-Splash.jpg");
		ImageIcon imgIcon = new ImageIcon(url);
		img = imgIcon.getImage();
		Dimension dim =new Dimension(imgIcon.getIconWidth(), imgIcon.getIconHeight());
        setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
		updateUI();

        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        }
        catch ( Exception e ) {}

    }

	public boolean isRunning() { return thread != null; }

    public Graphics2D createDemoGraphics2D(Graphics g) {
        Graphics2D g2 = null;

        if ( offImg == null || offImg.getWidth() != w ||
             offImg.getHeight() != h ) {
            offImg = (BufferedImage) createImage(w, h);
            newBufferedImage = true;
        }

        if ( offImg != null ) {
            g2 = offImg.createGraphics();
            g2.setBackground(getBackground());
        }

        // .. set attributes ..
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);

        // .. clear canvas ..
        g2.clearRect(0, 0, w, h);

        return g2;
    }

	public void paintFully(Graphics g) {
		g.clearRect(0,0,w,h);
        g.drawImage(img, 0, 0, w, h, this);
        g.dispose();
	}

    public void paint(Graphics g) {
        w = getWidth();
        h = getHeight();

        if ( w <= 0 || h <= 0 )
            return;

        Graphics2D g2 = createDemoGraphics2D(g);
		if (  thread == null ) {
			paintFully(g);
	        g2.dispose();
	        newBufferedImage = false;
			return;
		}else{
	        drawDemo(g2);
		}
        g2.dispose();

        if ( offImg != null && isShowing() ) {
            g.drawImage(offImg, 0, 0, this);
        }

        newBufferedImage = false;
    }

    public void drawDemo(Graphics2D g2) {

        if ( newBufferedImage ) {
            x = Math.random()*w;
            y = Math.random()*h;
            ew = (Math.random()*w)/2;
            eh = (Math.random()*h)/2;
        }
        x += ix;
        y += iy;
        ew += iw;
        eh += ih;
        if ( ew > w/2 ) {
            ew = w/2;
            iw = Math.random() * -w/16 - 1;
        }
        if ( ew < w/8 ) {
            ew = w/8;
            iw = Math.random() * w/16 + 1;
        }
        if ( eh > h/2 ) {
            eh = h/2;
            ih = Math.random() * -h/16 - 1;
        }
        if ( eh < h/8 ) {
            eh = h/8;
            ih = Math.random() * h/16 + 1;
        }
        if ( (x+ew) > w ) {
            x = (w - ew)-1;
            ix = Math.random() * -w/32 - 1;
        }
        if ( x < 0 ) {
            x = 2;
            ix = Math.random() * w/32 + 1;
        }
        if ( (y+eh) > h ) {
            y = (h - eh)-2;
            iy = Math.random() * -h/32 - 1;
        }
        if ( y < 0 ) {
            y = 2;
            iy = Math.random() * h/32 + 1;
        }

        ellipse.setFrame(x, y, ew, eh);
        g2.setClip(ellipse);

	    rect.setRect(x+5, y+5, ew-10, eh-10);
        g2.clip(rect);

        g2.drawImage(img, 0, 0, w, h, this);

    }


    public void start() {
        thread = new Thread(this);
        thread.start();
    }


    public synchronized void stop() {
        thread = null;
		Thread.yield();
		updateUI();

    }

    public void run() {

        Thread me = Thread.currentThread();

        while ( thread == me && isShowing() ) {
            Graphics g = getGraphics();
            paint(g);
            g.dispose();
            thread.yield();
        }
        thread = null;
    }


	public static void main(String[] args) {
		final JDialog dlg = new JDialog();
		dlg.setTitle( "About SQLIDE");
		final LogoPanel logoPanel = new LogoPanel();
		JLabel lbl = new JLabel("<HTML><B>About SQLIDE</HTML>");
		lbl.setAlignmentX(lbl.CENTER_ALIGNMENT);
		dlg.getContentPane().setLayout(new BorderLayout());
		dlg.getContentPane().add(logoPanel, BorderLayout.CENTER);
		dlg.getContentPane().add(lbl, BorderLayout.SOUTH);
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
            public void windowDeiconified(WindowEvent e) { logoPanel.start();}
            public void windowIconified(WindowEvent e) { logoPanel.stop();}
        };
        dlg.addWindowListener(l);
		dlg.pack();
		dlg.show();
        logoPanel.start();

	}

}