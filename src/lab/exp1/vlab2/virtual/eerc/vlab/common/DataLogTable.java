package eerc.vlab.common;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class DataLogTable extends JPanel 
{
	DefaultTableModel m_model;
	JTable m_Table;
	public DataLogTable(String[] columnNames,int w, int h) {
        super(new GridLayout(1,0));
        
//        String[] columnNames = {"Polarizability",
//                "Temperature (T)",
//                "1/T"};
//
//    	Object[][] data = {
//    		{"1", "-", "-"},
//
//    	};

        
    	m_model = new DefaultTableModel();

        //MyTableModel tbModel = new MyTableModel();
    	m_Table = new JTable(m_model);
//        {
//		            public Component prepareRenderer(TableCellRenderer renderer,
//		                    int rowIndex, int vColIndex) {
//					Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
//					if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
//						c.setBackground(Color.GRAY);
//					} else {
//					// If not shaded, match the table's background
//						c.setBackground(Color.GRAY);
//					}
//					return c;
		            
//					 Color bg = new Color(200, 100, 30);
//				     c.setBackground(Color.GRAY);
				     //c.setForeground(Color.GRAY);
//				     return c;
//					}
//				};

        //tbModel.setValueAt(new Integer(100), 0, 0);
        //final JTable table = new JTable(data, columnNames);
    	m_Table.setPreferredScrollableViewportSize(new Dimension(w, h));
        //table.setOpaque(true);
        
        //setOpaque(true);
        //table.setfFillsViewportHeight(true);
        for(int i=0; i< columnNames.length;i++)
        	m_model.addColumn(columnNames[i]);
        
        //table.setGridColor(Color.black);
        //table.setSelectionBackground(Color.black); 
        //table.setSelectionForeground(Color.white);

        //You can change row heights with:
       // table.setRowHeight(int pixelHeight);

        //The height of the individual cells will be equal to the row height minus the height of the row margins.

       // By default, the selection background and foreground colors are determined by Swing's look-and-feel implementation. You can change selection colors using:
        //table.setSelectionBackground(Color.black); 
        //table.setSelectionForeground(Color.white);
        //m_model.addRow(new Object[]{"v1", "v2", "v3"});
        //m_model.addRow(new Object[]{"v1", "v2", "v3"});
        //m_model.addRow(new Object[]{"v1", "v2", "v3"});


        //You can also hide the cells’ gridlines, like this:
        //table.setShowHorizontalLines(false);
       // table.setShowVerticalLines(false);
        
       // addRowData(new String("Polarizability"));
       // addRowData("Temperature (T)");
       // addRowData("1/T");

       // model.insertRow(model.getRowCount(), new Object[]{"r1","r1","r1"});
        //tbModel.setValueAt(value, row, col)


        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(m_Table);
//        scrollPane.getViewport().setBackground(Color.GRAY);
//        scrollPane.setBackground(Color.GRAY);
//        scrollPane.setForeground(Color.GRAY);
//        scrollPane.setOpaque(true);
        //Add the scroll pane to this panel.
        add(scrollPane);
    }
	
	public void addRowData(Object[] data){
	 	m_model.insertRow(m_model.getRowCount(),data);
	}
	
	public void addDopleMomentData(Object[] data){
		boolean flag =true;
		for(int i=0; i<m_model.getRowCount();i++){
			//System.out.println((String)m_model.getValueAt(i,1) + ":" +(String)data[1]);
			if(((String)m_model.getValueAt(i,1)).compareToIgnoreCase((String)data[1]) ==0)
				flag = false;
		}
		
		if(flag)
			m_model.insertRow(m_model.getRowCount(),data);
	}
	
	public void addData(Object[] data){
		m_model.insertRow(m_model.getRowCount(),data);
	}
	public void addData(int row, Object[] data){
		m_model.insertRow(row,data);
	}
	
	public void addComboDataAtColumn(int vColIndex, String[] values ){
		
		TableColumn col = m_Table.getColumnModel().getColumn(vColIndex);
		//col.get
	    col.setCellEditor(new MyComboBoxEditor(values));	    
	    // If the cell should appear like a combobox in its
	    // non-editing state, also set the combobox renderer
	    col.setCellRenderer(new MyComboBoxRenderer(values));
	    
	}
	
	public String getComboDataAtColumn(int row,int col ){
		
		//System.out.println("value 1,2"+ m_model.getValueAt(0, 1));
		//TableColumn col = tColumnModel().getColumn(vColIndex);
		//col.get
		MyComboBoxEditor celEditor = (MyComboBoxEditor)m_Table.getCellEditor(row, col);
		//JComboBox cb = (JComboBox)celEditor.getComponent();
		System.out.println("value" + celEditor.getCellEditorValue());
//		//celEditor.get
//	    col.setCellEditor(new MyComboBoxEditor(values));	    
//	    // If the cell should appear like a combobox in its
//	    // non-editing state, also set the combobox renderer
//	    col.setCellRenderer(new MyComboBoxRenderer(values));
		 return (String)celEditor.getCellEditorValue();
	    
	}

	public void clearAllData(){
		for(int i=0; i<m_model.getRowCount();i++)
			m_model.removeRow(i);
	}
	
	public void removeRowData(int row){	
			if(row < m_model.getRowCount())
				m_model.removeRow(row);
	}
	
	
	 class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
        public MyComboBoxRenderer(String[] items) {
            super(items);
        }
    
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
    
            // Select the current value
            setSelectedItem(value);
            return this;
        }
    }
    
     class MyComboBoxEditor extends DefaultCellEditor {
        public MyComboBoxEditor(String[] items) {
            super(new JComboBox(items));
        }
    }

	class MyTableModel extends AbstractTableModel {
		String[] columnNames = {"Polarizability",
                "Temperature (T)",
                "1/T"};

		Object[][] data = {
		{"1", "-", "-"},
		
		};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            
            data[row][col] = value;
            fireTableCellUpdated(row, col);

            
        }
	}


	  
}
