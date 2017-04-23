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

import utils.Tuple;
import controller.Controller_GUI;

/**
 * 
 * @author gtrauchessec
 * 
 * Fenetre permettant d acceder aux fractales de Julia enregistres
 *
 */
public class CSaved_Window extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private JTable table;
	private ModelTable model;
    
	private JPanel footer,subfooter_n,subfooter_c;
	private JButton button_add, button_rm, button_cl,button_ok;

/**
 * Constructeur
 */
	public CSaved_Window() {
		setTitle("");
		setSize(320-64,320);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModal(true);
		//setResizable(false);
		
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
		model.addColumn("Re");
		model.addColumn("Im");

		int size = Controller_GUI.getCSaved().size();
		
		for(int i=0;i<size;i++){
			String name = Controller_GUI.getCSaved().get(i).getFirst();
			double re = Controller_GUI.getCSaved().get(i).getSecond();
			double im = Controller_GUI.getCSaved().get(i).getThird();
			
			model.addRow();
			table.setValueAt(name, i, 0);
			table.setValueAt(re, i, 1);
			table.setValueAt(im, i, 2);
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
				Vector<Tuple<String,Double,Double>> v = Controller_GUI.getCSaved();
				for(int i=0;i<table.getRowCount();i++){
					v.get(i).setFirst(table.getValueAt(i, 0).toString());
					v.get(i).setSecond(Double.parseDouble(table.getValueAt(i, 1).toString()));
					v.get(i).setThird(Double.parseDouble(table.getValueAt(i, 2).toString()));
				}
				if(index !=-1){
					Controller_GUI.setC_re(v.get(index).getSecond());
					Controller_GUI.setC_im(v.get(index).getThird());
					Controller_GUI.updateSwing();
				}
				dispose();
			}
		});
		button_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
				int i = model.getRowCount();
				
				String name = "";
				double re = Controller_GUI.getC_re();
				double im = Controller_GUI.getC_im();
				
				Controller_GUI.addCSaved(name, re, im);
				
				model.addRow();
				table.setValueAt(name, i, 0);
				table.setValueAt(re, i, 1);
				table.setValueAt(im, i, 2);
			}
		});
		button_rm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller_GUI.rmCSaved(table.getSelectedRow());
				int selectedRow = table.getSelectedRow();
                if(selectedRow != -1){
                    model.removeRow(selectedRow);
                }
			}
		});
		
	}
}

	