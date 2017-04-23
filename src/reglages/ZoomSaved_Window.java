package reglages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import utils.Quadruple;
import controller.Controller_GUI;

/**
 * 
 * @author gtrauchessec
 * 
 * Fenetre permettant d acceder aux zooms enregistres
 *
 */
public class ZoomSaved_Window extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private JTable table;
	private ModelTable model;
    
	private JPanel footer,subfooter_n,subfooter_c;
	private JButton button_add, button_rm, button_cl,button_ok;
	
/**
 * Constructeur
 */
	public ZoomSaved_Window() {
		setTitle("");
		setSize(320-64,320);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModal(true);
		
		init();
		disp();
		listeners();
	}
	
/**
 * Creations des elements de la fenetre
 */
	private void init(){
		
		table = new JTable(model = new ModelTable());

		model.addColumn("Nom");
		model.addColumn("X");
		model.addColumn("Y");
		model.addColumn("Zoom");

		int size = Controller_GUI.getZoomsaved().size();
		
		for(int i=0;i<size;i++){
			String name = Controller_GUI.getZoomsaved().get(i).getFirst();
			double X = Controller_GUI.getZoomsaved().get(i).getSecond();
			double Y = Controller_GUI.getZoomsaved().get(i).getThird();
			double Z = Controller_GUI.getZoomsaved().get(i).getFourth();
			
			model.addRow();
			table.setValueAt(name, i, 0);
			table.setValueAt(X, i, 1);
			table.setValueAt(Y, i, 2);
			table.setValueAt(Z, i, 3);
		}
        
        footer = new JPanel();
        	footer.setPreferredSize(new Dimension(320-64,64));
        	footer.setLayout(new BorderLayout());
        	
        subfooter_c = new JPanel();
        subfooter_n = new JPanel();
        	subfooter_n.setPreferredSize(new Dimension(320-64,32));

        button_add = new JButton("Ajouter");
        button_cl = new JButton("Annuler");
        button_ok = new JButton("Valider");
        button_rm = new JButton("Supprimer");
        	
        this.setLayout(new BorderLayout());
	}
	
/**
 * Assemblage des elements de la fenetre
 */
	private void disp(){
		
        this.add(table.getTableHeader(), BorderLayout.NORTH);
        this.add(table, BorderLayout.CENTER);
		this.add(footer,BorderLayout.SOUTH);
			footer.add(subfooter_n,BorderLayout.NORTH);
				subfooter_n.add(button_add);
				subfooter_n.add(button_rm);
			footer.add(subfooter_c,BorderLayout.CENTER);
				subfooter_c.add(button_ok);
				subfooter_c.add(button_cl);
	}
	
/**
 * Gestion des evenements
 */
	private void listeners() {
		button_cl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button_ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				Vector<Quadruple<String, Double, Double, Double>> v = Controller_GUI.getZoomsaved();
				for(int i=0;i<table.getRowCount();i++){
					v.get(i).setFirst(table.getValueAt(i, 0).toString());
					v.get(i).setSecond(Double.parseDouble(table.getValueAt(i, 1).toString()));
					v.get(i).setThird(Double.parseDouble(table.getValueAt(i, 2).toString()));
					v.get(i).setFourth(Double.parseDouble(table.getValueAt(i, 3).toString()));
				}
				if(index !=-1){
					Controller_GUI.setZoom_coord_x(v.get(index).getSecond());
					Controller_GUI.setZoom_coord_y(v.get(index).getThird());
					Controller_GUI.setPrecision(v.get(index).getFourth());
					Controller_GUI.updateSwing();
				}
				dispose();
			}
		});
		button_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int i = model.getRowCount();
				
				String name = "";
				double X = Controller_GUI.getZoom_coord_x();
				double Y = Controller_GUI.getZoom_coord_y();
				double Z = Controller_GUI.getPrecision();
				
				Controller_GUI.addZoomSaved(name, X, Y, Z);
				
				model.addRow();
				table.setValueAt(name, i, 0);
				table.setValueAt(X, i, 1);
				table.setValueAt(Y, i, 2);
				table.setValueAt(Z, i, 3);
			}
		});
		button_rm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.rmZoomSaved(table.getSelectedRow());
				int selectedRow = table.getSelectedRow();
                if(selectedRow != -1){
                    model.removeRow(selectedRow);
                }
			}
		});
		
	}
}

	