import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.util.LinkedList;

import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;

public class SaleJframe implements PropertyListener {

	private JFrame frmPosSoftware;
	private JTextField ItemIDField;
	private JTextField QtyField;
	private JTextField TotalCost;
	private JTextField DateText;
	
	Beeper beep;
	ProcessSaleController psc;
	SaleLineItem sLineItem;										//ProductSpec of the last added item is stored in this
	LinkedList<SaleLineItem> sli;
	
	int counter;
	String total;
	String vat;
	String grandtotal;
	int selectIndex;
	int id;
	int quantity;
	boolean error = false; 										// This boolean controls Table update
	boolean saleInit = false;									// This boolean checks whether the Sale is initialed or not 
	boolean invalidInput = false;								// This boolean checks whether the input is valid or not (i.e. Valid input Number) 
	boolean noDiscount = false;
	boolean emptyCart = true;
	private JTextField vatAmount;
	private JTextField gTotal;
	private JTextField discountField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SaleJframe window = new SaleJframe();
					window.frmPosSoftware.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SaleJframe() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		
		frmPosSoftware = new JFrame();
		frmPosSoftware.setTitle("POS Software");
		frmPosSoftware.setBounds(100, 100, 674, 560);
		frmPosSoftware.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPosSoftware.getContentPane().setLayout(null);

		

		
		// -----------------------TextIDFields----------------------------
		
		ItemIDField = new JTextField();
		ItemIDField.setBounds(86, 60, 226, 20);
		frmPosSoftware.getContentPane().add(ItemIDField);
		ItemIDField.setColumns(10);

		JLabel lblItemId = new JLabel("Item ID");
		lblItemId.setBounds(10, 63, 46, 14);
		frmPosSoftware.getContentPane().add(lblItemId);

		QtyField = new JTextField();
		QtyField.setBounds(86, 91, 226, 20);
		frmPosSoftware.getContentPane().add(QtyField);
		QtyField.setColumns(10);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(10, 94, 66, 14);
		frmPosSoftware.getContentPane().add(lblQuantity);

		
		//--------------------AddButton----------------------------
		
		
		JButton btnAddItem = new JButton("Add Item");

		btnAddItem.setBounds(86, 122, 89, 23);
		frmPosSoftware.getContentPane().add(btnAddItem);

		//--------------------------Table Set--------------------------
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(86, 173, 517, 180);
		frmPosSoftware.getContentPane().add(scrollPane);

		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "SL#", "Item Name", "Unit Price", "Quantity", "Sub Total" }));

		
		//------------------------Total Cost Field-----------------------
		
		TotalCost = new JTextField();
		TotalCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		TotalCost.setEditable(false);
		TotalCost.setBounds(517, 364, 86, 20);
		frmPosSoftware.getContentPane().add(TotalCost);
		TotalCost.setColumns(10);

		JLabel lblTotal = new JLabel("Total :");
		lblTotal.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTotal.setBounds(465, 366, 46, 14);
		frmPosSoftware.getContentPane().add(lblTotal);
		
		//-----------------------Date Field------------------------------
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(526, 42, 46, 14);
		frmPosSoftware.getContentPane().add(lblDate);

		DateText = new JTextField();
		DateText.setEditable(false);
		DateText.setBounds(453, 60, 182, 20);
		frmPosSoftware.getContentPane().add(DateText);
		DateText.setColumns(10);

		// -----------------Add button Action-----------------------------

		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//---------------------Calling the worker Method-----------------
				
				addNewItemButtonAction(e);

				// ------------------Update table-------------------------
				
				if (saleInit && !error && !invalidInput) {									//checking whether Update is valid or not
					sli = psc.getSaleLineItemList();										//conditions include "No error in adding Item" & valid input format 
																							// & provided that the sale is initiated at first	

					sLineItem = sli.getLast();												//Just adding the latest item in current Sale List

					int colNum = table.getModel().getColumnCount();

					Object[] item = new Object[colNum];

					item[0] = ++counter;
					item[1] = sLineItem.getPs().getName();
					item[2] = sLineItem.getPs().getPrice();
					item[3] = sLineItem.getQuantity();
					item[4] = sLineItem.getSubtotal();
					((DefaultTableModel) table.getModel()).addRow(item);
					
					
					
					

				}
			}
		});
		
		//--------------------New Sale button---------------------------
		
		JButton btnNewSale = new JButton("New Sale");
		btnNewSale.setBounds(86, 26, 89, 23);
		frmPosSoftware.getContentPane().add(btnNewSale);
		
		vatAmount = new JTextField();
		vatAmount.setForeground(new Color(255, 69, 0));
		vatAmount.setFont(new Font("Tahoma", Font.BOLD, 10));
		vatAmount.setEditable(false);
		vatAmount.setBounds(517, 395, 86, 20);
		frmPosSoftware.getContentPane().add(vatAmount);
		vatAmount.setColumns(10);
		
		JLabel lblVat = new JLabel(" Discounted Total :");
		lblVat.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblVat.setBounds(390, 428, 121, 14);
		frmPosSoftware.getContentPane().add(lblVat);
		
		JLabel lblGrandTotal = new JLabel(" Grand Total :");
		lblGrandTotal.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblGrandTotal.setBounds(422, 464, 89, 14);
		frmPosSoftware.getContentPane().add(lblGrandTotal);
		
		gTotal = new JTextField();
		gTotal.setForeground(new Color(0, 0, 255));
		gTotal.setFont(new Font("Tahoma", Font.BOLD, 12));
		gTotal.setEditable(false);
		gTotal.setBounds(517, 461, 86, 20);
		frmPosSoftware.getContentPane().add(gTotal);
		gTotal.setColumns(10);
		
		discountField = new JTextField();
		discountField.setForeground(new Color(34, 139, 34));
		discountField.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountField.setEditable(false);
		discountField.setBounds(517, 426, 86, 20);
		frmPosSoftware.getContentPane().add(discountField);
		discountField.setColumns(10);
		
		JLabel label = new JLabel("  VAT :");
		label.setFont(new Font("Tahoma", Font.BOLD, 12));
		label.setBounds(465, 397, 46, 14);
		frmPosSoftware.getContentPane().add(label);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Select a Discount", "Senior Discount (10%)", "Eid Discount", "Best for Store", "Best for Customer"}));
		comboBox.setBounds(86, 377, 148, 20);
		frmPosSoftware.getContentPane().add(comboBox);
	
	
		//--------------Calculate Discount Button---------------
		
		JButton calcDiscount = new JButton("Calculate Discount");
		calcDiscount.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				
				selectIndex = comboBox.getSelectedIndex();
				
				if(!saleInit){
					JOptionPane.showMessageDialog(frmPosSoftware, "Please Start a Sale First");
					return;
				}
				
				if(emptyCart){
					JOptionPane.showMessageDialog(frmPosSoftware, "Please Add an Item First");
					return;
				}
					
				if(selectIndex==0 || noDiscount){
					JOptionPane.showMessageDialog(frmPosSoftware, "Please Select a Discount Option");
					return;
				}
					
				calculateDiscountButtonAction(arg0);				
				
				grandtotal = Integer.toString(psc.sale.getGrandTotal());				
				gTotal.setText(grandtotal);
				discountField.setText(Integer.toString(psc.sale.getTotal()));
				ItemIDField.setText("");
				QtyField.setText("");
				
				
			}
		});
		calcDiscount.setBounds(86, 429, 148, 23);
		frmPosSoftware.getContentPane().add(calcDiscount);
		
		// -----------------------New Sale Button Action----------------------------
	

		btnNewSale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//----------------Calling Worker Method----------------
				
				newSaleButtonAction(e);
				
				//-----------------Clearing Table-----------------------
				
				table.setModel(new DefaultTableModel(new Object[][] {},
						new String[] { "SL#", "Item Name", "Unit Price", "Quantity", "Sub Total" }));
						TotalCost.setText("");
						ItemIDField.setText("");
						QtyField.setText("");
						vatAmount.setText("");
						gTotal.setText("");
						discountField.setText("");

			}
		});
		
		
	}

	

	// -------------------Button Action Functions ------------------------------

	
	//--------------------Add Item Button Worker Function------------------------
	
	
	public void addNewItemButtonAction(ActionEvent e) {

	//-----------Filtering inputs----------------------
		
		try{
			id = Integer.parseInt(ItemIDField.getText());
			quantity = Integer.parseInt(QtyField.getText());
			invalidInput = false;																//Checking whether the user entered valid number or not
			
		}catch(Exception e1){
			invalidInput = true;
			JOptionPane.showMessageDialog(frmPosSoftware,  "                Invalid input Format\nPlease Enter Valid ItemID & Quantity");
			ItemIDField.setText("");
			QtyField.setText("");
		}
		
		if(!saleInit){																			//Checking Sale status before adding item
			JOptionPane.showMessageDialog(frmPosSoftware,  "Please Start a Sale First");
			ItemIDField.setText("");
			QtyField.setText("");
		}
		else{					
			
			if(invalidInput)	
				return;
			
			else if (psc.getProductSpec(id) == null) {	
				
				error = true;	
				JOptionPane.showMessageDialog(frmPosSoftware,  "No Such Item Found!!!");		//Checking item's validity 
				ItemIDField.setText("");
				QtyField.setText("");
			}
		
			else {
				try {
					psc.addItem(id, quantity);
					ItemIDField.setText("");
					QtyField.setText("");
					error = false;
					noDiscount =false;
					emptyCart = false;
	
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	
//------------------New Sale Button Worker Function-------------------------
	
	
	public void newSaleButtonAction(ActionEvent e) {
		try {
			psc = ProcessSaleController.getPscInstance();
			beep = new Beeper();																//initiating the beeper upon sale starting
			psc.makeNewSale();			
			this.register(psc);																	//Property Listener
			beep.register(psc);
			
			
			
			counter = 0;
			saleInit = true;																	//Marking the button action of "New Sale" button
			noDiscount = true;
			emptyCart = true;
			DateText.setText(psc.sale.getDate());
			

		} catch (Exception e2) {
			System.out.println("New Sale Button Exception");
		}

	}

//----------------------calculate Discount button action----------------------\
	
	
	public void calculateDiscountButtonAction(ActionEvent e){
		
		
		
		if(selectIndex==1){
			psc.selectedPricingStrategy("SeniorCitizenDiscount");
		}
		
		
		if(selectIndex==2){
			 psc.selectedPricingStrategy("EidDiscount");
		}
		
		
		if(selectIndex==3){
			psc.selectedPricingStrategy("CompositeBestForStorePS");
		}
		
		if(selectIndex==4){
			psc.selectedPricingStrategy("CompositeBestForCustomerPS");
		}
		
		
		
	}

	//---------------------Registering the JFrame to Sale----------------------------------------
	
	public void register(ProcessSaleController psc){
		psc.sale.addPropertyListener(this);
	}

//---------------------------Updating total immediately upon sale's update-----------------------------------
	
	public void onPropertyEvent(Sale source, String name, int value) {
		 
		if(name.equals("sale.total")){		
			
			 
			 TotalCost.setText(Integer.toString(value));
			 
		 }
		
		if(name.equals("vat.Amount")){		 			
			
			
			 vatAmount.setText(Integer.toString(value));
			 
		 }
		
		
	}
}
