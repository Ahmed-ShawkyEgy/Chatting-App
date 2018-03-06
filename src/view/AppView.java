package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class AppView extends JFrame {

	private static final long serialVersionUID = 1L;
	DefaultListModel<String> model , model2;
	
	
	public AppView(ActionListener listener,String name) 
	{
		super(name);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent event)
			{
				listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"quit"));
				System.exit(0);
			}
		});
		setSize(820,820);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = dim.width/2-this.getSize().width/2, y = dim.height/2-this.getSize().height/2;
		setLocation(x, y);
		setLayout(null);
		
		
		/* TOP */
		JButton refresh = new JButton("Refresh");
		refresh.setActionCommand("refresh");
		refresh.addActionListener(listener);
		add(refresh);
		refresh.setBounds(0,0,this.getWidth()-10,50);
		
		/* MEMBERS */
		model = new DefaultListModel<String>();
	    JList<String> list = new JList<String>(model);
	    JScrollPane pane = new JScrollPane(list);
	    
	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    add(pane);
	    pane.setBounds(0, 53, getWidth()/3, (int) (getHeight()/1.3));
		
	    
	    /* Console */
	    model2 = new DefaultListModel<String>();
	    JList<String> list2 = new JList<String>(model2);
	    JScrollPane pane2 = new JScrollPane(list2);
	    add(pane2);
	    pane2.setBounds(getWidth()/3 + 15 , 53 , getWidth()-getWidth()/3,(int)(getHeight()/1.3));
	    
		
		/* Message Form */
		JLabel label = new JLabel("Message");
		JTextField text = new JTextField();
		JLabel label1 = new JLabel("TTL");
		JTextField text1 = new JTextField();
		JButton b = new JButton("Send");
		
		text.setPreferredSize(new Dimension(300,50));
		text1.setPreferredSize(new Dimension(75,50));
		
		b.setActionCommand(text.getText());
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String command = list.getSelectedValue()+"|"+text.getText().trim() + "|" + text1.getText();
				listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,command));
			}
		});
		
		b.setPreferredSize(new Dimension(140,50));
		
		
		JPanel bottom = new JPanel();
		bottom.add(label);
		bottom.add(text);
		bottom.add(label1);
		bottom.add(text1);
		bottom.add(b);
		
		add(bottom);
		bottom.setBounds(1, (int) (getHeight()/1.3) + 60, getWidth(), getHeight());
		
		setVisible(true);
		
	}
	
	
	public void addMember(String name)
	{
		model.addElement(name);
	}
	
	public void removeMember(String name)
	{
		model.removeElement(name);
	}
	
	public void clearMembers()
	{
		model.clear();
	}
	
	public void print(String s)
	{
		model2.addElement(s);
	}
	
}
