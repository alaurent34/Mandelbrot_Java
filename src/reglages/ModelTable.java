package reglages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author gtrauchessec
 *
 * Representation d'une ligne de tableau JTable
 * pour la creation d'un tableau dynamique
 *
 */
class Row{

	private Map<String, Object> values = new HashMap<String, Object>();

	public Object getValueForCol(String string) {
		if(values.containsKey(string)){
			return values.get(string);
		}
		return "";
	}

	public void setValueForCol(Object aValue, String columnIndex) {
		values.put(columnIndex, aValue);
	}

}

/**
 * 
 * @author gtrauchessec
 *	
 *	Tableau JTable dynamique
 */
public class ModelTable extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	
	int colIndex=0;
	private ArrayList<String> cols = new ArrayList<String>();
	private ArrayList<Row> rows = new ArrayList<Row>();

/**
 * Renvoie vrai si la cellule est editable
 * @param rowIndex Index ligne
 * @param columnIndex Index colonne
 * @return Vrai si la cellule est editable
 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(rowIndex<5)
			return false;
		return true;
	}

/**
 *  Renvoie le nom de la colonne
 *  @param column Index colonne
 *  @return Le nom de la colonne
 */
	public String getColumnName(int column) {
		return cols.get(column).toString();
	}
	
/**
 * Renvoie le nombre de lignes
 * @return Le nombre de lignes
 */
	public int getRowCount() {
		return rows.size();
	}
	
/**
 * Ajoute une ligne
 */
	public void addRow() {
		rows.add(new Row());
		fireTableRowsInserted(rows.size(), rows.size());
	}
	
/**
 * Supprime une ligne
 * @param selectedRow Ligne a supprimer
 */
	public void removeRow(int selectedRow) {
		if(selectedRow<5)
			return;
		rows.remove(selectedRow);
		fireTableRowsDeleted(selectedRow, selectedRow);
	}
	
/**
 * Renvoie l'objet de la cellule
 * @param rowIndex Index ligne
 * @param columnIndex Index colonne
 * @return L'objet dans la cellule
 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Row rowData = rows.get(rowIndex);
		return rowData.getValueForCol(cols.get(columnIndex));
	}
/**
 * Donne un nom a la colonne
 * @param name Nom
 */
    public void addColumn(String name) {
        cols.add(name);
        fireTableStructureChanged();
        
    }

/**
 * Donne une valeur a une cellule
 * @param aValue Valeur
 * @param rowIndex Index ligne
 * @param columnIndex Index colonne
 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Row rowData = rows.get(rowIndex);
        rowData.setValueForCol(aValue,cols.get(columnIndex));
        fireTableCellUpdated(rowIndex, columnIndex);
    }
/**
 * Renvoie de nombre de colonne
 * @return Le nombre de colonne
 */
	public int getColumnCount() {
		return cols.size();
	}
}
