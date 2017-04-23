package reglages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import utils.State;
import controller.Controller_GUI;

/**
 * 
 * @author gtrauchessec
 *
 *	Fenetre de reglages
 *
 */
public class Main_Window extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedpane;

	private JPanel main_panel,fractals_panel,options_panel;
	private JPanel fractals_panel_header,fractals_panel_body,fractals_panel_footer,fractals_panel_subfooter_north,
	fractals_panel_subfooter_south;
	private JPanel[] fractals_subpanel,options_subpanel,options_panel_subpanel;


	private JPanel options_couleurList_panel, options_resolutionList_panel,fractals_panel_c,options_panel_subfooter_north,
	options_panel_subfooter_south,options_panel_subfooter_center,options_panel_header,options_panel_body,options_panel_footer,options_panel_zoomMan,
	options_panel_precMan,option_panel_labelMan;

	private JLabel options_label_couleur,options_label_resolution,fractals_label_zoom,fractals_label_iter,fractals_label_c,
	fractals_label_i,fractals_label_space;
	private JRadioButton[] options_radiobutton_couleur,options_radiobutton_resolution,options_radiobutton_threads; 
	private ButtonGroup options_group_couleur, options_group_resolution,options_group_threads;
	private JButton options_button_valider,options_button_fermer,fractals_button_valider,fractals_button_fermer,fractals_button_Julia,
	fractals_button_Mandelbrot,fractals_button_Boudhabrot,fractals_button_screenshot,fractals_button_default,fractals_button_c,
	options_button_zoom,options_button_zoomSaved,options_button_filechooser;
	private JTextField fractals_textfield_zoom, fractals_textfield_iter,fractals_textfield_c_re,fractals_textfield_c_im,
	options_textfield_zoomx,options_textfield_zoomy,options_textfield_zoomp,options_textfield_filechooser;
	private JCheckBox fractals_checkbox_gpu,fractals_checkbox_abb;

	private CSaved_Window csaved_window;
	private ZoomSaved_Window zoomsaved_window;

	/**
	 * Constructeur
	 */
	public Main_Window() {
		super("Reglages");
		this.setVisible(true);
		this.setSize(320-32,480-64);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		init();
		disp();
		listeners();
	}

	/**
	 * Ajout d'un onglet
	 * @param table Panel ou ajouter l'onglet
	 * @param title Nom de l'onget
	 * @return Le panel correspondant a l'onglet
	 */
	private JPanel addTab(JTabbedPane table, String title) {
		JPanel panel = new JPanel();
		table.addTab(title, panel);
		return panel;
	}

	/**
	 * Creations des elements de la fenetre
	 */
	private void init(){

		tabbedpane = new JTabbedPane();
		main_panel = new JPanel();
		main_panel.setLayout(new BorderLayout());

		main_panel.setVisible(true);
		tabbedpane.setVisible(true);

		fractals_panel = addTab(tabbedpane,"Fractales");
		fractals_panel.setLayout(new BorderLayout());
		options_panel = addTab(tabbedpane,"Options");
		options_panel.setLayout(new BorderLayout());

		options_panel_subpanel = new JPanel[3];
		for(int i=0;i<options_panel_subpanel.length;i++){
			options_panel_subpanel[i] = new JPanel();
			options_panel_subpanel[i].setLayout(new BoxLayout(options_panel_subpanel[i], BoxLayout.X_AXIS));
		}

		fractals_panel_header = new JPanel();
		fractals_panel_body = new JPanel();
		fractals_panel_body.setLayout(new GridLayout(0, 1));
		fractals_panel_footer = new JPanel();
		fractals_panel_footer.setLayout(new BorderLayout());
		fractals_panel_subfooter_north = new JPanel();
		fractals_panel_subfooter_north.setLayout(new FlowLayout());
		fractals_panel_subfooter_south = new JPanel();
		fractals_panel_subfooter_south.setLayout(new FlowLayout());
		options_panel_header = new JPanel();
		options_panel_body = new JPanel();
		options_panel_body.setLayout(new GridLayout(0, 1));
		options_panel_footer = new JPanel();
		options_panel_footer.setLayout(new BorderLayout());
		options_panel_subfooter_north = new JPanel();
		options_panel_subfooter_north.setLayout(new BorderLayout());
		options_panel_subfooter_north.setPreferredSize(new Dimension(320-32,64));
		options_panel_subfooter_south = new JPanel();
		options_panel_subfooter_center = new JPanel();
		options_panel_subfooter_center.setPreferredSize(new Dimension(320-32,64));
		options_panel_zoomMan = new JPanel();
		options_panel_zoomMan.setLayout(new BoxLayout(options_panel_zoomMan, BoxLayout.X_AXIS));
		options_panel_precMan = new JPanel();
		options_panel_precMan.setLayout(new BoxLayout(options_panel_precMan, BoxLayout.X_AXIS));
		option_panel_labelMan = new JPanel();
		option_panel_labelMan.setPreferredSize(new Dimension(320-32,32));

		fractals_label_space = new JLabel("  ");

		fractals_subpanel = new JPanel[4];
		for(int i=0;i<fractals_subpanel.length;i++){
			fractals_subpanel[i] = new JPanel();
			fractals_subpanel[i].setLayout(new BorderLayout());
		}
		fractals_subpanel[0].setLayout(new FlowLayout());

		options_subpanel = new JPanel[2];
		for(int i=0;i<options_subpanel.length;i++){
			options_subpanel[i] = new JPanel();
			options_subpanel[i].setLayout(new BorderLayout());
		}

		fractals_panel_c = new JPanel();
		fractals_panel_c.setLayout(new BoxLayout(fractals_panel_c, BoxLayout.X_AXIS));

		options_couleurList_panel = new JPanel();
		options_couleurList_panel.setLayout(new BoxLayout(options_couleurList_panel, BoxLayout.Y_AXIS));
		options_resolutionList_panel = new JPanel();
		options_resolutionList_panel.setLayout(new BoxLayout(options_resolutionList_panel, BoxLayout.Y_AXIS));

		options_label_couleur = new JLabel("Couleur:");
		options_label_resolution = new JLabel("<html>Resolution:  <font color=\"red\"> (Redemarage requis) </font></html>");

		options_radiobutton_couleur = new JRadioButton[3];
		options_radiobutton_couleur[0] = new JRadioButton("Bi-Color");
		options_radiobutton_couleur[1] = new JRadioButton("Dégradé");
		options_radiobutton_couleur[2] = new JRadioButton("Gradient");

		options_radiobutton_couleur[Controller_GUI.getCouleur()].setSelected(true);

		options_radiobutton_resolution = new JRadioButton[3];
		options_radiobutton_resolution[0] = new JRadioButton("640 x 480");
		options_radiobutton_resolution[1] = new JRadioButton("800 x 600");
		options_radiobutton_resolution[2] = new JRadioButton("1024 x 768");

		options_radiobutton_resolution[Controller_GUI.getResolution()].setSelected(true);

		options_radiobutton_threads = new JRadioButton[3];
		options_radiobutton_threads[0] = new JRadioButton("1");
		options_radiobutton_threads[1] = new JRadioButton("4");
		options_radiobutton_threads[2] = new JRadioButton("8");

		options_radiobutton_threads[Controller_GUI.getNbThreadsIndex()].setSelected(true);

		options_group_couleur = new ButtonGroup();
		for(int i=0;i<options_radiobutton_couleur.length;i++){
			options_group_couleur.add(options_radiobutton_couleur[i]);
		}
		options_group_resolution = new ButtonGroup();
		for(int i=0;i<options_radiobutton_resolution.length;i++){
			options_group_resolution.add(options_radiobutton_resolution[i]);
		}
		options_group_threads = new ButtonGroup();
		for(int i=0;i<options_radiobutton_threads.length;i++){
			options_group_threads.add(options_radiobutton_threads[i]);
		}

		fractals_label_zoom = new JLabel("Zoom step:");
		fractals_label_iter = new JLabel("Iteration Maximale:");

		fractals_label_c = new JLabel("C = ");
		fractals_label_i = new JLabel("+i ");

		fractals_button_Julia = new JButton("Julia");
		fractals_button_Mandelbrot = new JButton("Mandelbrot");
		fractals_button_Mandelbrot.setEnabled(false);
		fractals_button_Boudhabrot = new JButton("Boudhabrot");

		fractals_button_valider = new JButton("Valider");
		fractals_button_fermer = new JButton("Fermer");
		fractals_button_screenshot = new JButton("ScreenShot");
		fractals_button_default = new JButton("Reset Zoom");
		options_button_valider = new JButton("Valider");
		options_button_fermer = new JButton("Fermer");
		fractals_button_c = new JButton(">");
		options_button_zoom = new JButton("Zoom");
		options_button_zoomSaved = new JButton(">");
		options_button_filechooser = new JButton("Open");
		fractals_checkbox_gpu = new JCheckBox("OpenCL");
		fractals_checkbox_abb = new JCheckBox("Anti-Buddhabrot");

		fractals_textfield_zoom = new JTextField(Double.toString(Controller_GUI.getZoom()));
		fractals_textfield_iter = new JTextField(Double.toString(Controller_GUI.getIter_max()));
		fractals_textfield_c_re = new JTextField(Double.toString(Controller_GUI.getC_re()));
		fractals_textfield_c_im = new JTextField(Double.toString(Controller_GUI.getC_im()));

		options_textfield_filechooser = new JTextField(Controller_GUI.getGradient_path());

		options_textfield_zoomx = new JTextField(Double.toString(Controller_GUI.getZoom_coord_x()));
		options_textfield_zoomy = new JTextField(Double.toString(Controller_GUI.getZoom_coord_y()));
		options_textfield_zoomp = new JTextField(Double.toString(Controller_GUI.getPrecision()));

		//setJuliaEditable(false);

	}

	/**
	 * Assemblages des elements de la fenetre
	 */
	private void disp(){
		this.add(main_panel);
		main_panel.add(tabbedpane);

		fractals_panel.add(fractals_panel_header,BorderLayout.NORTH);
		fractals_panel.add(fractals_panel_body,BorderLayout.CENTER);
		for(int i=0;i<fractals_subpanel.length;i++)
			fractals_panel_body.add(fractals_subpanel[i]);
		fractals_subpanel[0].add(fractals_button_Julia);
		fractals_subpanel[0].add(fractals_button_Mandelbrot);
		fractals_subpanel[0].add(fractals_button_Boudhabrot);
		fractals_subpanel[1].add(configuredPanel(320-32,16),BorderLayout.NORTH);
		fractals_subpanel[1].add(fractals_panel_c,BorderLayout.CENTER);
		fractals_panel_c.add(fractals_label_c);
		fractals_panel_c.add(fractals_textfield_c_re);
		fractals_panel_c.add(fractals_label_i);
		fractals_panel_c.add(fractals_textfield_c_im);
		fractals_panel_c.add(new JLabel("  "));
		fractals_panel_c.add(fractals_button_c);
		fractals_panel_c.add(new JLabel("  "));
		fractals_subpanel[1].add(configuredPanel(320-32,32-5),BorderLayout.SOUTH);
		fractals_subpanel[2].add(fractals_label_zoom,BorderLayout.NORTH);
		fractals_subpanel[2].add(fractals_textfield_zoom,BorderLayout.CENTER);
		fractals_subpanel[2].add(configuredPanel(320-32,32-5),BorderLayout.SOUTH);
		fractals_subpanel[3].add(fractals_label_iter,BorderLayout.NORTH);
		fractals_subpanel[3].add(fractals_textfield_iter,BorderLayout.CENTER);
		fractals_subpanel[3].add(configuredPanel(320-32,32-5),BorderLayout.SOUTH);

		fractals_panel.add(fractals_panel_footer,BorderLayout.SOUTH);
		fractals_panel_footer.add(fractals_panel_subfooter_north,BorderLayout.NORTH);
		fractals_panel_subfooter_north.add(fractals_button_screenshot);
		fractals_panel_subfooter_north.add(fractals_button_default);
		fractals_panel_footer.add(fractals_panel_subfooter_south,BorderLayout.CENTER);
		fractals_panel_subfooter_south.add(fractals_button_valider);
		fractals_panel_subfooter_south.add(fractals_button_fermer);

		options_panel.add(options_panel_header,BorderLayout.NORTH);
		options_panel.add(options_panel_body,BorderLayout.CENTER);
		for(int i=0;i<options_subpanel.length;i++)
			options_panel_body.add(options_subpanel[i]);
		options_subpanel[0].add(options_label_couleur,BorderLayout.NORTH);
		options_subpanel[0].add(options_couleurList_panel,BorderLayout.CENTER);

		options_couleurList_panel.setLayout(new GridLayout(0,3));

		for(int i=0;i<options_radiobutton_couleur.length;i++){
			options_couleurList_panel.add(options_radiobutton_couleur[i]);
			if(i != 2){
				options_couleurList_panel.add(new JLabel(" "));
				options_couleurList_panel.add(new JLabel(" "));
			}
			else{
				options_couleurList_panel.add(options_textfield_filechooser);
				options_couleurList_panel.add(options_button_filechooser);
			}
		}
		options_subpanel[1].add(options_label_resolution,BorderLayout.NORTH);
		options_subpanel[1].add(options_resolutionList_panel,BorderLayout.CENTER);
		for(int i=0;i<options_radiobutton_resolution.length;i++)
			options_resolutionList_panel.add(options_radiobutton_resolution[i]);
		options_panel.add(options_panel_footer,BorderLayout.SOUTH);

		options_panel_subfooter_north.setLayout(new BorderLayout());
		options_panel_subfooter_center.setLayout(new BorderLayout());

		options_panel_footer.add(options_panel_subfooter_north,BorderLayout.NORTH);
		options_panel_subfooter_north.add(options_panel_subpanel[0],BorderLayout.NORTH);
		options_panel_subpanel[0].add(new JLabel("Zoom manuel:"));
		options_panel_subfooter_north.add(options_panel_subpanel[1],BorderLayout.CENTER);
		options_panel_subpanel[1].add(new JLabel("  Position   = "));
		options_panel_subpanel[1].add(options_textfield_zoomx);
		options_panel_subpanel[1].add(new JLabel("+i "));
		options_panel_subpanel[1].add(options_textfield_zoomy);
		options_panel_subpanel[1].add(new JLabel(" "));
		options_panel_subfooter_north.add(options_panel_subpanel[2],BorderLayout.SOUTH);
		options_panel_subpanel[2].add(new JLabel("  Precision = "));
		options_panel_subpanel[2].add(options_textfield_zoomp);
		options_panel_subpanel[2].add(new JLabel(" "));
		options_panel_subpanel[2].add(options_button_zoomSaved);
		options_panel_subpanel[2].add(new JLabel(" "));
		options_panel_subpanel[2].add(options_button_zoom);
		options_panel_subpanel[2].add(new JLabel(" "));

		options_panel_footer.add(options_panel_subfooter_center,BorderLayout.CENTER);
		options_panel_subfooter_center.setLayout(new BoxLayout(options_panel_subfooter_center, BoxLayout.X_AXIS));

		options_panel_subfooter_center.add(new JLabel("Threads: "));
		for(int i=0;i<options_radiobutton_threads.length;i++)
			options_panel_subfooter_center.add(options_radiobutton_threads[i]);

		options_panel_footer.add(options_panel_subfooter_south,BorderLayout.SOUTH);
		options_panel_subfooter_south.add(options_button_valider);
		options_panel_subfooter_south.add(options_button_fermer);

		this.revalidate();
	}

	/**
	 * Gestion des evenements
	 */
	private void listeners(){
		options_button_filechooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();

				int returnVal = fc.showOpenDialog(Main_Window.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					//Controller_GUI.setGradient_path(file.getName());
					options_textfield_filechooser.setText(file.getAbsolutePath());
				}
			}
		});

		options_button_zoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.setZoom_coord_x(Double.parseDouble(options_textfield_zoomx.getText()));
				Controller_GUI.setZoom_coord_y(Double.parseDouble(options_textfield_zoomy.getText()));
				Controller_GUI.setPrecision(Double.parseDouble(options_textfield_zoomp.getText()));

				Controller_GUI.makeZoom();
			}
		});

		options_button_valider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<options_radiobutton_couleur.length;i++)
					if(options_radiobutton_couleur[i].isSelected())
						Controller_GUI.setCouleur(i);
				for(int i=0;i<options_radiobutton_resolution.length;i++)
					if(options_radiobutton_resolution[i].isSelected())
						Controller_GUI.setResolution(i);

				Controller_GUI.setGradient_path(options_textfield_filechooser.getText());

				for(int i=0;i<options_radiobutton_threads.length;i++)
					if(options_radiobutton_threads[i].isSelected())
						Controller_GUI.setNbThreadsIndex(i);

				Controller_GUI.updateGraphic();
			}
		});
		options_button_fermer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.setVisible(false);
			}
		});
		fractals_button_valider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Controller_GUI.getState() == State.Julia){
					Controller_GUI.setC_re( Double.parseDouble(fractals_textfield_c_re.getText()) );
					Controller_GUI.setC_im( Double.parseDouble(fractals_textfield_c_im.getText()) );
				}
				Controller_GUI.setZoom( Double.parseDouble(fractals_textfield_zoom.getText()) );
				Controller_GUI.setIter_max( Double.parseDouble(fractals_textfield_iter.getText()) );
				Controller_GUI.setGPUOn(fractals_checkbox_gpu.isSelected());
				Controller_GUI.setAntibb(fractals_checkbox_abb.isSelected());
				Controller_GUI.updateGraphic();
			}
		});
		fractals_button_fermer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.setVisible(false);
			}
		});
		fractals_button_default.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.resetZoom();
			}
		});
		fractals_button_screenshot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.screenshot();
			}
		});
		fractals_button_Julia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.setState(State.Julia);
				//setJuliaEditable(true);
				fractals_button_Julia.setEnabled(false);
				fractals_button_Mandelbrot.setEnabled(true);
				fractals_button_Boudhabrot.setEnabled(true);

				clearPanel();
				addJulia();
				RefreshPanel();
			}
		});
		fractals_button_Mandelbrot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.setState(State.Mandelbrot);
				//setJuliaEditable(false);
				fractals_button_Julia.setEnabled(true);
				fractals_button_Mandelbrot.setEnabled(false);
				fractals_button_Boudhabrot.setEnabled(true);

				clearPanel();
				addMandelbrot();
				RefreshPanel();
			}
		});
		fractals_button_Boudhabrot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.setState(State.Boudhabrot);
				//setJuliaEditable(false);
				fractals_button_Julia.setEnabled(true);
				fractals_button_Mandelbrot.setEnabled(true);
				fractals_button_Boudhabrot.setEnabled(false);
				clearPanel();
				addBuddhabrot();
				RefreshPanel();
			}
		});
		fractals_button_c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startWindowCSaved();
			}
		});
		options_button_zoomSaved.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startWindowZoomSaved();
			}
		});
	}

	/**
	 * Panel pre-configure (ici pour gerer l espacement)
	 * @param w Largeur
	 * @param h Hauteur
	 * @return Le panneau configure
	 */
	private JPanel configuredPanel(int w,int h){
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(w, h));
		return p;
	}

	/**
	 * Fermeture de la fenetre de reglage
	 */
	public void quit(){
		this.dispose();
	}

	/**
	 * Mise a jour de la fenetre de reglages
	 */
	public void updateValues() {
		fractals_textfield_c_re.setText(Double.toString(Controller_GUI.getC_re()));
		fractals_textfield_c_im.setText(Double.toString(Controller_GUI.getC_im()));
		fractals_textfield_zoom.setText(Double.toString(Controller_GUI.getZoom()));
		fractals_textfield_iter.setText(Double.toString(Controller_GUI.getIter_max()));

		options_textfield_zoomx.setText(Double.toString(Controller_GUI.getZoom_coord_x()));
		options_textfield_zoomy.setText(Double.toString(Controller_GUI.getZoom_coord_y()));
		options_textfield_zoomp.setText(Double.toString(Controller_GUI.getPrecision()));
		options_textfield_filechooser.setText(Controller_GUI.getGradient_path());

		options_radiobutton_couleur[Controller_GUI.getCouleur()].setSelected(true);
		options_radiobutton_resolution[Controller_GUI.getResolution()].setSelected(true);
		options_radiobutton_threads[Controller_GUI.getNbThreadsIndex()].setSelected(true);

		fractals_checkbox_gpu.setSelected(Controller_GUI.isGPUOn());
		fractals_checkbox_abb.setSelected(Controller_GUI.isAntibb());

		switch(Controller_GUI.getState()){
		case Julia:
			fractals_button_Julia.setEnabled(false);
			fractals_button_Mandelbrot.setEnabled(true);
			fractals_button_Boudhabrot.setEnabled(true);

			clearPanel();
			addJulia();
			RefreshPanel();
			break;
		case Mandelbrot:
			fractals_button_Julia.setEnabled(true);
			fractals_button_Mandelbrot.setEnabled(false);
			fractals_button_Boudhabrot.setEnabled(true);

			clearPanel();
			addMandelbrot();
			RefreshPanel();
			break;
		case Boudhabrot:
			fractals_button_Julia.setEnabled(true);
			fractals_button_Mandelbrot.setEnabled(true);
			fractals_button_Boudhabrot.setEnabled(false);

			clearPanel();
			addBuddhabrot();
			RefreshPanel();
			break;
		}
	}

	/**
	 * Ouverture la fenetre de Julias sauvegardees
	 */
	private void startWindowCSaved(){	
		csaved_window = new CSaved_Window();
		csaved_window.setVisible(true);
	}

	/**
	 * Ouverture la fenetre de zooms sauvegardees
	 */
	private void startWindowZoomSaved(){	
		zoomsaved_window = new ZoomSaved_Window();
		zoomsaved_window.setVisible(true);
	}

	/**
	 * Suppression du panel d'option de fractale
	 */
	private void clearPanel(){
		fractals_subpanel[1].removeAll();
		fractals_panel_c.removeAll();
	}

	/**
	 * Remplissage du panel d'option de fractale (Julia)
	 */
	private void addJulia(){
		JPanel tmp_panel = new JPanel();
		
		fractals_subpanel[1].add(configuredPanel(320-32,8),BorderLayout.NORTH);
		fractals_subpanel[1].add(fractals_panel_c,BorderLayout.CENTER);
		fractals_panel_c.add(fractals_label_c);
		fractals_panel_c.add(fractals_textfield_c_re);
		fractals_panel_c.add(fractals_label_i);
		fractals_panel_c.add(fractals_textfield_c_im);
		fractals_panel_c.add(fractals_label_space);
		fractals_panel_c.add(fractals_button_c);
		fractals_panel_c.add(fractals_label_space);
		fractals_panel_c.add(fractals_checkbox_gpu);
		fractals_subpanel[1].add(tmp_panel,BorderLayout.SOUTH);
		tmp_panel.add(fractals_checkbox_gpu);
		
		fractals_panel_c.setLayout(new BoxLayout(fractals_panel_c, BoxLayout.X_AXIS));		
	}

	/**
	 * Remplissage du panel d'option de fractale (Mandelbrot)
	 */
	private void addMandelbrot(){
		fractals_subpanel[1].add(configuredPanel(320-32,11),BorderLayout.NORTH);
		fractals_subpanel[1].add(fractals_panel_c,BorderLayout.CENTER);
		fractals_panel_c.add(fractals_checkbox_gpu);
		fractals_subpanel[1].add(configuredPanel(320-32,32-5),BorderLayout.SOUTH);
		fractals_panel_c.setLayout(new FlowLayout());
	}

	/**
	 * Remplissage du panel d'option de fractale (Buddhabrot)
	 */
	private void addBuddhabrot() {
		fractals_subpanel[1].add(configuredPanel(320-32,11),BorderLayout.NORTH);
		fractals_subpanel[1].add(fractals_panel_c,BorderLayout.CENTER);
		fractals_panel_c.add(fractals_checkbox_abb);
		fractals_subpanel[1].add(configuredPanel(320-32,32-5),BorderLayout.SOUTH);
		fractals_panel_c.setLayout(new FlowLayout());
	}

	/**
	 * Affichage du panel modifie
	 */
	private void RefreshPanel(){
		this.repaint();
	}
	
	/**
	 * Active ou désactive l'option GPU
	 * @param b
	 */
	public void setGPUEnable(boolean b){
		fractals_checkbox_gpu.setEnabled(b);
	}

}