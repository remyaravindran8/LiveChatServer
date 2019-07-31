
package com.chatroom.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.InsetsUIResource;

import com.chatroom.client.ClientModel;
import com.chatroom.configuration.Config;
import com.chatroom.models.Request;
import com.chatroom.models.Response;
import com.chatroom.others.Message;

public class ViewRoomsActivity {
	private JLabel jLabel;
	private JLabel jLabelTitle;
	private JFrame jFrame;
	private JButton jBtnJoinRoom;
	private JComboBox<String> jComboBox;
	private BufferedImage iconLogo;
	private BufferedImage back_arrow_image;
	private JLabel jLabel_back_arrow_image;
	private ClientModel clientModel;
	
	@SuppressWarnings("serial")
	public ViewRoomsActivity(ClientModel cm) throws IOException {
		clientModel = cm;
		jFrame = new JFrame("CHATROOM VIEW ROOMS");
		
		iconLogo = ImageIO.read(this.getClass().getResource("/logo.png"));
		back_arrow_image = ImageIO.read(this.getClass().getResource("/back_arrow.png"));
		
		jFrame.setContentPane(new JPanel() {
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("/background.png"));
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(myImage, 0, 0, this);
			}
		});
		

		jBtnJoinRoom = new JButton("JOIN ROOM");
		jLabelTitle = new JLabel("Select room that you want to join");
		try
		{
			jComboBox = new JComboBox<>(getRooms());
			initializeAllWithProperties();
		}
		catch( RuntimeException e)
		{
			e.printStackTrace();
			new MainMenuOptions(clientModel);
			jFrame.dispose();
		}
	}
	
	private String[] getRooms() throws RuntimeException{
		String[] rooms = {};
		Request request = new Request(Request.Type.VIEW_ROOMS.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),"");
		try {
			
			ClientModel.objectOutputStream.writeObject(request);
			ClientModel.objectOutputStream.flush();
			Response response = (Response) ClientModel.objectInputStream.readObject();
			if( response.getSuccess())
			{
				Message.println("Getting list of rooms...");
				rooms = response.getContents().split(",");
			}
			else
			{
				Message.println(response.getContents());
				JOptionPane.showMessageDialog(null,response.getContents(), null, JOptionPane.ERROR_MESSAGE);
				throw new RuntimeException("no_rooms");
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return rooms;
	}

	private void ListeningEvents() {
		jBtnJoinRoom.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: add join room code
			}
		});
	
		jLabel_back_arrow_image.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					new MainMenuOptions(clientModel);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				jFrame.dispose();
				
			}
		});
	}
	
	@SuppressWarnings("serial")
	private void initializeAllWithProperties() {
		
		//join room button properties
		jBtnJoinRoom.setPreferredSize(new Dimension(150,35));
		jBtnJoinRoom.setBackground(Color.WHITE);
		jBtnJoinRoom.setBorder(new LineBorder(Config.colorPrimary, 3));
		jBtnJoinRoom.setFocusPainted(false);
		
		//combo box properties
		jComboBox.setPreferredSize(new Dimension(200,35));
		jComboBox.setBackground(Color.white);
		jComboBox.setRenderer(new DefaultListCellRenderer() {
		    @Override
		    public void paint(Graphics g) {
		        setBackground(Color.WHITE);
		        setForeground(Config.colorPrimary);
		        super.paint(g);
		    }
		});
		
		jFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Insets comboBoxInsets = new Insets(4, 175, 20, 4);
		Insets textTitle = new Insets(4, 160, 20, 4);
		Insets logoInsets = new InsetsUIResource(0, 200, 50, 0);
		Insets buttonInsets = new Insets(4, 200, 20, 4);
		Insets backArrowInsets = new Insets(0, 10, 100, 0);
		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = backArrowInsets;
		jLabel_back_arrow_image = new JLabel(new ImageIcon(back_arrow_image));
		jLabel_back_arrow_image.setPreferredSize(new Dimension(50,50));
		jFrame.add(jLabel_back_arrow_image,c);
		
		c.weightx = 1.0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = logoInsets;
		c.gridy = 1;
		
		jLabel = new JLabel(new ImageIcon(iconLogo));
		jLabel.setPreferredSize(new Dimension(150,150));
		jFrame.add(jLabel,c);
		
		c.gridy = 2;
		c.insets = textTitle;
		jFrame.add(jLabelTitle,c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.insets = comboBoxInsets;
		jFrame.add(jComboBox,c);
		
		c.gridx = 0;
		c.gridy = 4;
		c.insets = buttonInsets;
		jFrame.add(jBtnJoinRoom,c);
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(864,614);
		jFrame.setResizable(false);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		jFrame.getRootPane().setDefaultButton(jBtnJoinRoom);
		jBtnJoinRoom.requestFocus();
		
		ListeningEvents();
	}
	
//	public static void main(String args[]) throws IOException {
//		new ViewRoomsActivity();
//	}	
}